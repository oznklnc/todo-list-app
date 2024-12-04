package com.demo.todo.list.app.config;

import com.couchbase.client.core.msg.kv.DurabilityLevel;
import com.couchbase.client.java.env.ClusterEnvironment;
import com.couchbase.client.java.transactions.config.TransactionsConfig;
import com.demo.todo.list.app.config.properties.CouchbaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.auditing.EnableCouchbaseAuditing;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
@EnableCouchbaseRepositories(basePackages = "com.demo.todo.list.app")
@EnableCouchbaseAuditing
@Profile("!unittest")
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    private final CouchbaseProperties couchbaseProperties;

    @Override
    public String getConnectionString() {
        return couchbaseProperties.getConnectionString();
    }

    @Override
    public String getUserName() {
        return couchbaseProperties.getUsername();
    }

    @Override
    public String getPassword() {
        return couchbaseProperties.getPassword();
    }

    @Override
    public String getBucketName() {
        return couchbaseProperties.getBucketName();
    }

    @Override
    protected String getScopeName() {
        return couchbaseProperties.getScopeName();
    }

    @Override
    protected boolean autoIndexCreation() {
        return true;
    }




    @Override
    protected void configureEnvironment(final ClusterEnvironment.Builder builder) {
        builder.transactionsConfig(
                TransactionsConfig.builder()
                        .timeout(Duration.ofSeconds(120))
                        .durabilityLevel(DurabilityLevel.NONE)
        );
    }

}
