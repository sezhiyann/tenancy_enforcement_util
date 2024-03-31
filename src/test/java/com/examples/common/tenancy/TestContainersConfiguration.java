package com.examples.common.tenancy;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

@TestConfiguration
@Slf4j
@ImportTestcontainers
@ConditionalOnProperty(name = "testcontainers.enabled", havingValue = "true", matchIfMissing = true)
public class TestContainersConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractIntegrationTest.class);
  @Container
  static final MongoDBContainer mongoDBContainer =
      new MongoDBContainer("mongo:6.0.12")
          .withExposedPorts(27017)
          .withCommand("--replSet rs0")
          .withClasspathResourceMapping(
              "mongodb", "/docker-entrypoint-initdb.d/", BindMode.READ_ONLY)
          .withCreateContainerCmdModifier(
              cmd -> {
                cmd.getHostConfig()
                    .withMemory(256 * 1024 * 1024L)
                    .withMemorySwap(256 * 1024 * 1024L);
              })
          .withLogConsumer(new Slf4jLogConsumer(LOGGER))
          .waitingFor(Wait.forListeningPort());


  @DynamicPropertySource
  static void integrationProperties(DynamicPropertyRegistry registry) throws IOException {
    System.out.println("************************** ports ****************************************");
    Integer mongoDBContainerMappedPort = mongoDBContainer.getMappedPort(27017);
    System.out.println("Mongodb Port : " + mongoDBContainerMappedPort);
    registry.add("MONGODB_PORT", () -> mongoDBContainerMappedPort);
    registry.add("MONGODB_DATABASE", () -> "test");
    System.out.println("******************************************************************");

  }
}
