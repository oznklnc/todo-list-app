package com.springboot.couchbase.springbootrealworld.runners;


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
import com.couchbase.client.java.query.QueryResult;
import com.springboot.couchbase.springbootrealworld.configuration.ClusterProperties;
import com.springboot.couchbase.springbootrealworld.domain.article.entity.ArticleDocument;
import com.springboot.couchbase.springbootrealworld.domain.article.entity.CommentDocument;
import com.springboot.couchbase.springbootrealworld.domain.article.entity.FavoriteDocument;
import com.springboot.couchbase.springbootrealworld.domain.profile.entity.FollowDocument;
import com.springboot.couchbase.springbootrealworld.domain.tag.entity.ArticleTagRelationDocument;
import com.springboot.couchbase.springbootrealworld.domain.user.entity.UserDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.couchbase.core.CouchbaseTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;

import static com.couchbase.client.core.util.CbThrowables.findCause;
import static com.couchbase.client.core.util.CbThrowables.hasCause;


/**
 * This class run after the application startup. It automatically sets up all needed indexes
 */
@Component
public class DBSetupRunners implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(DBSetupRunners.class);

    @Autowired
    private CouchbaseTemplate couchbaseTemplate;
    @Autowired
    private ClusterProperties clusterProperties;

    private static final WatchQueryIndexesOptions WATCH_PRIMARY = WatchQueryIndexesOptions
            .watchQueryIndexesOptions()
            .watchPrimary(true);
    private static final String DEFAULT_INDEX_NAME = "#primary";
    private CreatePrimaryQueryIndexOptions options;

    @Override
    public void run(String... args) {
        Cluster cluster = couchbaseTemplate.getCouchbaseClientFactory().getCluster();
        Bucket bucket = couchbaseTemplate.getCouchbaseClientFactory().getBucket();

        String defaultBucket = clusterProperties.getDefaultBucket();
        String defaultScope = clusterProperties.getDefaultScope();
        try {
            cluster.queryIndexes().createPrimaryIndex(defaultBucket);
            logger.info("Created primary index" + defaultBucket);
        } catch (Exception e) {
            logger.info("Primary index already exists on bucket " + defaultBucket);
        }

        var collections = Arrays.asList(UserDocument.USER_COLLECTION_NAME, ArticleDocument.ARTICLE_COLLECTION_NAME, FavoriteDocument.FAVORITE_COLLECTION_NAME,
                CommentDocument.COMMENT_COLLECTION_NAME, ArticleTagRelationDocument.TAG_COLLECTION_NAME, FollowDocument.FOLLOW_COLLECTION_NANE);

        collections.stream().forEach(col -> createCollection(bucket, defaultScope, col));
        collections.stream().forEach(col -> setupPrimaryIndex(cluster, defaultBucket, defaultScope, col));


        try {
            final QueryResult result = cluster.query("CREATE INDEX default_profile_title_index ON " + bucket.name() + "._default." + ArticleDocument.ARTICLE_COLLECTION_NAME + "(title)");
            Thread.sleep(1000);
        } catch (Exception e) {
            logger.info(String.format("Failed to create secondary index on article.title: %s", e.getMessage()));
        }
        logger.info("Application is ready.");
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
            logger.info("Created collection '" + spec.name() + "' in scope '" + spec.scopeName() + "' of bucket '" + bucket.name() + "'");
        } catch (CollectionExistsException e) {
            logger.info(String.format("Collection <%s> already exists", collectionName));
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
