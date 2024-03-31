package com.examples.common.tenancy;

import com.mongodb.client.MongoClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoDatabaseFactorySupport;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories
@TestConfiguration
@Import(TestContainersConfiguration.class)
public class IntegrationTestConfiguration {

  @Bean("defaultMongoTemplate")
  MongoTemplate defaultMongoTemplate(MongoDatabaseFactorySupport<MongoClient> factorySupport) {
    return new MongoTemplate(factorySupport);
  }

}
