package com.examples.common.tenancy.mongo;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.bson.Document;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.core.MongoDatabaseFactorySupport;

class TenancyEnforcedUnSupportedTest {

  static TenancyEnforcedMongoTemplate tenancyEnforcedMongoTemplate;

  @BeforeAll
  public static void setup() {
    MongoDatabaseFactorySupport factorySupportMock = Mockito.mock(
        MongoDatabaseFactorySupport.class);

    when(factorySupportMock.getExceptionTranslator()).thenReturn(Mockito.mock(
        PersistenceExceptionTranslator.class));

    tenancyEnforcedMongoTemplate = new TenancyEnforcedMongoTemplate(
        factorySupportMock);
  }

  @Test
  void executeCommand_Document() {
    assertThrows(UnsupportedOperationException.class,
        () -> tenancyEnforcedMongoTemplate.executeCommand((Document) null));
  }

  @Test
  void executeCommand_Document_ReadPreference() {
    assertThrows(UnsupportedOperationException.class,
        () -> tenancyEnforcedMongoTemplate.executeCommand(null, null));
  }

  @Test
  void executeCommand_String() {
    assertThrows(UnsupportedOperationException.class,
        () -> tenancyEnforcedMongoTemplate.executeCommand((String) null));
  }

  @Test
  void query() {
    assertThrows(UnsupportedOperationException.class,
        () -> tenancyEnforcedMongoTemplate.query(String.class));
  }

  @Test
  void update() {
    assertThrows(UnsupportedOperationException.class,
        () -> tenancyEnforcedMongoTemplate.update(String.class));
  }

  @Test
  void remove() {
    assertThrows(UnsupportedOperationException.class,
        () -> tenancyEnforcedMongoTemplate.remove(String.class));
  }

  @Test
  void insert() {
    assertThrows(UnsupportedOperationException.class,
        () -> tenancyEnforcedMongoTemplate.insert(String.class));
  }

  @Test
  void aggregateAndReturn() {
    assertThrows(UnsupportedOperationException.class,
        () -> tenancyEnforcedMongoTemplate.aggregateAndReturn(String.class));
  }

  @Test
  void findAll_class_collectionName() {
    assertThrows(UnsupportedOperationException.class,
        () -> tenancyEnforcedMongoTemplate.findAll(String.class, ""));
  }

  @Test
  void mapReduce() {
    assertThrows(UnsupportedOperationException.class,
        () -> tenancyEnforcedMongoTemplate.mapReduce(null, String.class, "", "", "", null,
            String.class));
  }

  @Test
  void geoNear() {
    assertThrows(UnsupportedOperationException.class,
        () -> tenancyEnforcedMongoTemplate.geoNear(null, String.class, "", String.class));
  }
}