package com.examples.common.tenancy.mongo;


import static com.examples.common.tenancy.data.BaseMongoEntity.CODE;
import static com.examples.common.tenancy.data.BaseMongoEntity.ID;
import static com.examples.common.tenancy.data.BaseMongoEntity.NAME;
import static com.examples.common.tenancy.data.EntityWithTenantAndContext.TENANT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.examples.common.tenancy.AbstractIntegrationTest;
import com.examples.common.tenancy.TenantContextUtil;
import com.examples.common.tenancy.data.EntityWithTenant;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

class TenancyEnforcedMongoTemplateIT extends AbstractIntegrationTest {

  public static final String TONE = "tone";
  public static final String TTWO = "ttwo";
  public static final String TONE_TEST_ID = "6606905a5cf50f20304f4850";
  public static final String TTWO_TEST_ID = "66069054c3da2e0c64152d55";
  public static final String TFOUR = "tfour";
  public static final String TFIVE = "cfive";
  public static final String TONE_ID_FOR_REMOVE = "66069054c3da2e0c64152d10";
  public static final String TTWO_ID_FOR_REMOVE = "66069054c3da2e0c64152d11";
  public static final String TEST_TFOUR = "test_tfour";
  public static final String TEST_TFIVE = "test_cfive";
  public static final String TEST_TONE = "test_tone";
  public static final String TEST_REPLACE = "test-replace";
  public static final String REPLACED_ENTITY = "replaced-entity";
  public static final String TONE_ID_FOR_REPLACE = "66069054c3da2e0c64152d58";
  public static final String TEST_STR = "test-";

  public static final String TENANT_ID_FOR_REPLACE = "66069054c3da2e0c64152d57";
  public static final String NAME_UPDATED_1 = "name-updated-1";
  public static final String NAME_UPDATED = "name-updated";

  public static final String TEST_TTWO = "test_ttwo";

  @Autowired
  @Qualifier("mongoTemplate")
  private TenancyEnforcedMongoTemplate tenancyEnforcedMongoTemplate;

  @Test
  void count_ForCurrentTenantId() {
    TenantContextUtil.setTenantId(TONE);

    Query query = Query.query(Criteria.where(ID).is(TONE_TEST_ID));
    long count = tenancyEnforcedMongoTemplate.count(
        query, EntityWithTenant.class);

    List<EntityWithTenant> all = defaultMongoTemplate.findAll(EntityWithTenant.class);

    assertEquals(1, count, "Count not correct for current tenant");
  }

  @Test
  void count_ForAnotherTenantId() {
    TenantContextUtil.setTenantId(TONE);

    long count = tenancyEnforcedMongoTemplate.count(
        Query.query(Criteria.where(ID).is(TTWO_TEST_ID)), EntityWithTenant.class);

    assertEquals(0, count, "Count came correct for another tenant");
  }

  @Test
  void exactCount_ForCurrentTenantId() {
    TenantContextUtil.setTenantId(TONE);

    long count = tenancyEnforcedMongoTemplate.exactCount(
        Query.query(Criteria.where(ID).is(TONE_TEST_ID)), EntityWithTenant.class);

    assertEquals(1, count, "Count not correct for current tenant");
  }

  @Test
  void exactCount_ForAnotherTenantId() {
    TenantContextUtil.setTenantId(TONE);

    long count = tenancyEnforcedMongoTemplate.exactCount(
        Query.query(Criteria.where(ID).is(TTWO_TEST_ID)), EntityWithTenant.class);

    assertEquals(0, count, "Count came correct for another tenant");
  }

  @Test
  void findById_ForCurrentTenantId() {
    TenantContextUtil.setTenantId(TONE);

    EntityWithTenant byId = tenancyEnforcedMongoTemplate.findById(TONE_TEST_ID,
        EntityWithTenant.class);

    assertNotNull(byId, "Entity not found for the current tenant");
  }

  @Test
  void findById_ForAnotherTenantId() {
    TenantContextUtil.setTenantId(TTWO);

    EntityWithTenant byId = tenancyEnforcedMongoTemplate.findById(TONE_TEST_ID,
        EntityWithTenant.class);

    assertNull(byId, "Entity not found for the current tenant");
  }

  @Test
  void findDistinct_ForCurrentTenantId() {
    TenantContextUtil.setTenantId(TONE);
    List<String> entity = tenancyEnforcedMongoTemplate.findDistinct(CODE, EntityWithTenant.class,
        String.class);

    assertEquals(1, entity.stream().count());
    assertTrue(entity.stream().anyMatch(code -> TEST_TONE.equals(code)),
        "Not able to fetch current tenant's data");
    assertFalse(entity.stream().anyMatch(code -> TEST_TTWO.equals(code)),
        "Able to fetch another tenant's data");
  }

  @Test
  void findDistinct_ForAnotherTenantId() {
    TenantContextUtil.setTenantId(TTWO);
    List<String> entity = tenancyEnforcedMongoTemplate.findDistinct(CODE, EntityWithTenant.class,
        String.class);

    assertEquals(1, entity.stream().count());
    assertFalse(entity.stream().anyMatch(code -> TEST_TONE.equals(code)),
        "Able to fetch another tenant's data");
    assertTrue(entity.stream().anyMatch(code -> TEST_TTWO.equals(code)),
        "Not able to fetch current tenant's data");
  }


  @Test
  void findAndModify_forCurrentTenantId() {
    TenantContextUtil.setTenantId(TONE);

    tenancyEnforcedMongoTemplate.findAndModify(
        Query.query(Criteria.where(ID).is(TONE_TEST_ID)),
        Update.update(NAME, NAME_UPDATED), EntityWithTenant.class);

    EntityWithTenant entityWithTenant = defaultMongoTemplate.findById(TONE_TEST_ID,
        EntityWithTenant.class);
    assertEquals("name-updated", entityWithTenant.getName(),
        "name not updated for the current tenant");
  }

  @Test
  void findAndModify_forAnotherTenantId() {
    TenantContextUtil.setTenantId(TTWO);

    tenancyEnforcedMongoTemplate.findAndModify(
        Query.query(Criteria.where(ID).is(TONE_TEST_ID)),
        Update.update(NAME, NAME_UPDATED_1), EntityWithTenant.class);

    EntityWithTenant entityWithTenant = defaultMongoTemplate.findById(TONE_TEST_ID,
        EntityWithTenant.class);
    assertNotEquals("name-updated-1", entityWithTenant.getName(),
        "name updated for the another tenant");
  }

  @Test
  void findAndReplace_forCurrentTenantId() {
    TenantContextUtil.setTenantId(TONE);

    EntityWithTenant toReplace = EntityWithTenant.builder().name(REPLACED_ENTITY)
        .code(TEST_TONE).build();
    tenancyEnforcedMongoTemplate.findAndReplace(
        Query.query(Criteria.where(ID).is(TENANT_ID_FOR_REPLACE)),

        toReplace);

    EntityWithTenant entityWithTenant = defaultMongoTemplate.findById(TENANT_ID_FOR_REPLACE,
        EntityWithTenant.class);
    assertEquals(REPLACED_ENTITY, entityWithTenant.getName(),
        "name not updated for the current tenant");
    assertEquals(TONE, entityWithTenant.getTenantId(),
        "tenant id not updated for the current tenant");
  }

  @Test
  void findAndReplace_forAnotherTenantId() {
    TenantContextUtil.setTenantId(TONE);

    EntityWithTenant toReplace = EntityWithTenant.builder().name(REPLACED_ENTITY)
        .code(TEST_TONE).build();
    tenancyEnforcedMongoTemplate.findAndReplace(
        Query.query(Criteria.where(ID).is(TONE_ID_FOR_REPLACE)),
        toReplace);

    EntityWithTenant entityWithTenant = defaultMongoTemplate.findById(TONE_ID_FOR_REPLACE,
        EntityWithTenant.class);
    assertEquals(TEST_REPLACE, entityWithTenant.getName(),
        "Able to update name for another tenant");
    assertEquals(TTWO, entityWithTenant.getTenantId(),
        "Able to update tenant id for another tenant");
  }

  @Test
  void findAndRemove_forCurrentTenantId() {
    TenantContextUtil.setTenantId(TONE);

    tenancyEnforcedMongoTemplate.findAndRemove(
        Query.query(Criteria.where(ID).is(TONE_ID_FOR_REMOVE)), EntityWithTenant.class);

    EntityWithTenant entityWithTenant = defaultMongoTemplate.findById(TONE_ID_FOR_REMOVE,
        EntityWithTenant.class);
    assertNull(entityWithTenant, "entry not deleted for the current tenant");
  }

  @Test
  void findAndRemove_forAnotherTenantId() {
    TenantContextUtil.setTenantId(TONE);

    tenancyEnforcedMongoTemplate.findAndRemove(
        Query.query(Criteria.where(ID).is(TTWO_ID_FOR_REMOVE)), EntityWithTenant.class);

    EntityWithTenant entityWithTenant = defaultMongoTemplate.findById(TTWO_ID_FOR_REMOVE,
        EntityWithTenant.class);
    assertNotNull(entityWithTenant, "entry deleted for the another tenant");
  }

  @Test
  void find_ForCurrentTenantId() {
    TenantContextUtil.setTenantId(TONE);

    Query query = Query.query(Criteria.where(NAME).exists(true));
    List<EntityWithTenant> entityWithCompanies = tenancyEnforcedMongoTemplate.find(query,
        EntityWithTenant.class);

    assertTrue(entityWithCompanies.stream().anyMatch(
            entityWithTenant -> TONE_TEST_ID.equals(entityWithTenant.getId())),
        "Not able to fetch current tenant's data");
  }

  @Test
  void find_ForAnotherTenantId() {
    TenantContextUtil.setTenantId(TTWO);

    Query query = Query.query(Criteria.where(NAME).exists(true));
    List<EntityWithTenant> entityWithCompanies = tenancyEnforcedMongoTemplate.find(query,
        EntityWithTenant.class);

    assertFalse(entityWithCompanies.stream().anyMatch(
            entityWithTenant -> TONE_TEST_ID.equals(entityWithTenant.getId())),
        "Not able to fetch current tenant's data");
  }

  @Test
  void save_withOutTenantId() {
    TenantContextUtil.setTenantId(TONE);
    EntityWithTenant entityWithTenant = EntityWithTenant.builder()
        .name(TEST_STR + Instant.now().getEpochSecond())
        .code(TEST_TONE)
        .build();
    EntityWithTenant saved = tenancyEnforcedMongoTemplate.save(entityWithTenant);

    EntityWithTenant actual = defaultMongoTemplate.findById(entityWithTenant.getId(),
        EntityWithTenant.class);

    assertEquals(TONE, actual.getTenantId(), "tenant id not set properly");
  }

  @Test
  void save_withAnotherTenantId() {
    TenantContextUtil.setTenantId(TONE);
    EntityWithTenant entityWithTenant = EntityWithTenant.builder()
        .name(TEST_STR + Instant.now().getEpochSecond())
        .code(TEST_TONE)
        .tenantId(TTWO)
        .build();
    EntityWithTenant saved = tenancyEnforcedMongoTemplate.save(entityWithTenant);

    EntityWithTenant entity = defaultMongoTemplate.findById(entityWithTenant.getId(),
        EntityWithTenant.class);

    assertEquals(TONE, entity.getTenantId(), "tenant id not set properly");
  }

  @Test
  void insertCollection_forCurrentTenantId() {
    TenantContextUtil.setTenantId(TFOUR);
    List<EntityWithTenant> entityWithCompanies = List.of(EntityWithTenant.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_TFOUR)
            .tenantId(TFOUR)
            .build(),
        EntityWithTenant.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_TFOUR)
            .tenantId(TFOUR)
            .build()
    );

    tenancyEnforcedMongoTemplate.insert(entityWithCompanies, EntityWithTenant.class);

    Criteria criteria = Criteria.where(CODE).is(TEST_TFOUR)
        .andOperator(Criteria.where(TENANT_ID).is(TFOUR));
    List<EntityWithTenant> entityWithTenantList = defaultMongoTemplate.find(Query.query(criteria),
        EntityWithTenant.class);
    assertEquals(2, entityWithTenantList.size(),
        "Batch not saved properly for the current tenant id");
  }

  @Test
  void insertCollection_forAnotherTenantId() {
    TenantContextUtil.setTenantId(TFIVE);
    List<EntityWithTenant> entityWithCompanies = List.of(EntityWithTenant.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_TFIVE)
            .tenantId(TFOUR)
            .build(),
        EntityWithTenant.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_TFIVE)
            .tenantId(TFOUR)
            .build()
    );

    tenancyEnforcedMongoTemplate.insert(entityWithCompanies, EntityWithTenant.class);

    Criteria criteria = Criteria.where(CODE).is(TEST_TFIVE)
        .andOperator(Criteria.where(TENANT_ID).is(TFOUR));
    List<EntityWithTenant> entityWithTenantList = defaultMongoTemplate.find(Query.query(criteria),
        EntityWithTenant.class);
    assertEquals(0, entityWithTenantList.size(), "Batch saved for the another tenant id");
  }
}
