package com.examples.common.tenancy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;


@Import(IntegrationTestConfiguration.class)
@SpringBootTest
public abstract class AbstractIntegrationTest {

  @Autowired
  @Qualifier("defaultMongoTemplate")
  protected MongoTemplate defaultMongoTemplate;

}
