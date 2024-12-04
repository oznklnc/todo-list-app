package com.demo.todo.list.app.initializer;


import com.couchbase.client.core.error.CollectionExistsException;
import com.couchbase.client.core.error.IndexNotFoundException;
import com.couchbase.client.core.error.InternalServerFailureException;
import com.couchbase.client.core.retry.reactor.Retry;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.manager.collection.CollectionManager;
import com.couchbase.client.java.manager.collection.CollectionSpec;
import com.couchbase.client.java.manager.query.CreatePrimaryQueryIndexOptions;
import com.couchbase.client.java.manager.query.WatchQueryIndexesOptions;
import com.demo.todo.list.app.config.properties.CouchbaseProperties;
import com.demo.todo.list.app.entity.ProjectDocument;
import com.demo.todo.list.app.entity.TodoDocument;
import com.demo.todo.list.app.entity.UserDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static com.couchbase.client.core.util.CbThrowables.findCause;
import static com.couchbase.client.core.util.CbThrowables.hasCause;


/**
 * This class run after the application startup. It automatically sets up all needed indexes
 */
@Slf4j
@Component
@Profile("!unittest")
@RequiredArgsConstructor
public class DBSetupRunners implements CommandLineRunner {

    private final CouchbaseTemplate couchbaseTemplate;
    private final CouchbaseProperties couchbaseProperties;

    private static final WatchQueryIndexesOptions WATCH_PRIMARY = WatchQueryIndexesOptions
            .watchQueryIndexesOptions()
            .watchPrimary(true);
    private static final String DEFAULT_INDEX_NAME = "#primary";
    private CreatePrimaryQueryIndexOptions options;

    @Override
    public void run(String... args) {
        Cluster cluster = couchbaseTemplate.getCouchbaseClientFactory().getCluster();
        Bucket bucket = couchbaseTemplate.getCouchbaseClientFactory().getBucket();

        String defaultBucket = couchbaseProperties.getBucketName();
        String defaultScope = couchbaseProperties.getScopeName();
        try {
            cluster.queryIndexes().createPrimaryIndex(defaultBucket);
            log.info("Created primary index" + defaultBucket);
        } catch (Exception e) {
            log.info("Primary index already exists on bucket " + defaultBucket);
        }

        var collections = List.of(UserDocument.USER_COLLECTION_NAME, ProjectDocument.PROJECT_COLLECTION_NAME, TodoDocument.TODO_COLLECTION_NAME);

        collections.stream().forEach(col -> createCollection(bucket, defaultScope, col));
        collections.stream().forEach(col -> setupPrimaryIndex(cluster, defaultBucket, defaultScope, col));


        try {
            //final QueryResult result = cluster.query("CREATE INDEX default_profile_title_index ON " + bucket.name() + "._default." + ArticleDocument.ARTICLE_COLLECTION_NAME + "(title)");
            Thread.sleep(1000);
        } catch (Exception e) {
            log.info(String.format("Failed to create secondary index on article.title: %s", e.getMessage()));
        }
        log.info("Application is ready.");
    }

    private void setupPrimaryIndex(Cluster cluster, String bucketName, String scope, String collectionName) {

        Mono.fromRunnable(() -> createIndex(cluster, bucketName, scope, collectionName))
                .retryWhen(Retry.onlyIf(ctx ->
                                findCause(ctx.exception(), InternalServerFailureException.class)
                                        .filter(exception -> CouchbaseError.create(exception)
                                                .getErrorEntries().stream()
                                                .anyMatch(err -> err.getMessage().contains("GSI")))
                                        .isPresent())
                        .exponentialBackoff(Duration.ofMillis(50), Duration.ofSeconds(3))
                        .timeout(Duration.ofSeconds(60))
                        .toReactorRetry())
                .block();
        Mono.fromRunnable(() -> waitForIndex(cluster, bucketName, scope, collectionName))
                .retryWhen(Retry.onlyIf(ctx -> hasCause(ctx.exception(), IndexNotFoundException.class))
                        .exponentialBackoff(Duration.ofMillis(50), Duration.ofSeconds(3))
                        .timeout(Duration.ofSeconds(30))
                        .toReactorRetry())
                .block();
        IndexCommons.waitUntilReady(cluster, bucketName, Duration.ofSeconds(60));

    }

    private void createCollection(Bucket bucket, String scope, String collectionName) {
        CollectionManager collectionManager = bucket.collections();
        try {
            CollectionSpec spec = CollectionSpec.create(collectionName, scope);
            collectionManager.createCollection(spec);
            log.info("Created collection '" + spec.name() + "' in scope '" + spec.scopeName() + "' of bucket '" + bucket.name() + "'");
        } catch (CollectionExistsException e) {
            log.info("Collection{} already exists", collectionName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createIndex(Cluster cluster, String bucketName, String scope, String collection) {
        var options = CreatePrimaryQueryIndexOptions.createPrimaryQueryIndexOptions()
                .ignoreIfExists(true)
                .numReplicas(0);
        if (collection != null && scope != null) {
            options.collectionName(collection).scopeName(scope);
        }
        cluster.queryIndexes().createPrimaryIndex(bucketName,
                options);
    }

    private void waitForIndex(Cluster cluster, String bucketName, String scope, String collection) {
        if (collection != null && scope != null) {
            WATCH_PRIMARY.collectionName(collection).scopeName(scope);
        }
        cluster.queryIndexes().watchIndexes(bucketName, Collections.singletonList(DEFAULT_INDEX_NAME),
                Duration.ofSeconds(10), WATCH_PRIMARY);
    }
}
