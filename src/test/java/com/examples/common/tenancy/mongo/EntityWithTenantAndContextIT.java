package com.examples.common.tenancy.mongo;


import static com.examples.common.tenancy.data.BaseMongoEntity.CODE;
import static com.examples.common.tenancy.data.BaseMongoEntity.ID;
import static com.examples.common.tenancy.data.BaseMongoEntity.NAME;
import static com.examples.common.tenancy.data.EntityWithTenantAndContext.ADDITIONAL_CONTEXT_ID;
import static com.examples.common.tenancy.data.EntityWithTenantAndContext.TENANT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.examples.common.tenancy.AbstractIntegrationTest;
import com.examples.common.tenancy.TenantContextUtil;
import com.examples.common.tenancy.data.EntityWithTenantAndContext;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

class EntityWithTenantAndContextIT extends AbstractIntegrationTest {

  public static final String TONE = "tone";
  public static final String TTWO = "ttwo";
  public static final String TONE_TEST_ID = "6606905a5cf50f20304f4850";
  public static final String TFOUR = "tfour";
  public static final String TFIVE = "cfive";
  public static final String TONE_ID_FOR_REMOVE = "66069054c3da2e0c64152d10";
  public static final String TTWO_ID_FOR_REMOVE = "66069054c3da2e0c64152d11";
  public static final String TEST_TFOUR = "test_tfour";
  public static final String TEST_TFIVE = "test_cfive";
  public static final String TEST_TONE = "test_tone";

  public static final String TEST_TTWO = "test_ttwo";
  public static final String TEST_REPLACE = "test-replace";
  public static final String REPLACED_ENTITY = "replaced-entity";
  public static final String TTWO_ID_FOR_REPLACE = "66069054c3da2e0c64152d58";
  public static final String TEST_STR = "test-";

  public static final String TENANT_ID_FOR_REPLACE = "66069054c3da2e0c64152d57";
  public static final String NAME_UPDATED_1 = "name-updated-1";
  public static final String NAME_UPDATED = "name-updated";

  public static final String TENANT_CHECK = "tenant-check";
  public static final String TONE_AONE = "tone_aone";
  public static final String TTWO_AONE = "ttwo_aone";
  public static final String TONE_ATWO = "tone_atwo";
  public static final String NAME_UPDATED_2 = "name-updated-2";
  public static final String TFOUR_AONE = "tfour_aone";
  public static final String TFOUR_ATWO = "tfour_atwo";
  public static final String TFIVE_AONE = "tfive_aone";

  @Autowired
  @Qualifier("mongoTemplate")
  private TenancyEnforcedMongoTemplate tenancyEnforcedMongoTemplate;

  @Test
  void count_ForCurrentTenantAndContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    Query query = Query.query(Criteria.where(NAME).is(TENANT_CHECK));
    long count = tenancyEnforcedMongoTemplate.count(query, EntityWithTenantAndContext.class);
    assertEquals(1, count, "Count not correct for current tenant");
  }

  @Test
  void count_ForTenantAndAnotherContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    Query query = Query.query(Criteria.where(ID).is("6606905a5cf50f20304f4841"));
    long count = tenancyEnforcedMongoTemplate.count(query, EntityWithTenantAndContext.class);
    assertEquals(0, count, "Count not correct for current tenant");
  }


  @Test
  void count_ForAnotherTenantId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);
    Query query = Query.query(Criteria.where(ID).is("66069054c3da2e0c64152d55"));
    long count = tenancyEnforcedMongoTemplate.count(query, EntityWithTenantAndContext.class);

    assertEquals(0, count, "Able to Count correct for another tenant");
  }

  @Test
  void exactCount_ForCurrentTenantAndContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    Query query = Query.query(Criteria.where(NAME).is(TENANT_CHECK));
    long count = tenancyEnforcedMongoTemplate.exactCount(query, EntityWithTenantAndContext.class);
    assertEquals(1, count, "Count not correct for current tenant");
  }

  @Test
  void exactCount_ForCurrentTenantAndAnotherContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    Query query = Query.query(Criteria.where(ID).is("6606905a5cf50f20304f4841"));
    long count = tenancyEnforcedMongoTemplate.exactCount(query, EntityWithTenantAndContext.class);
    assertEquals(0, count, "Count not correct for current tenant");
  }

  @Test
  void exactCount_ForAnotherTenantId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    long count = tenancyEnforcedMongoTemplate.exactCount(
        Query.query(Criteria.where(CODE).is(TEST_TTWO)), EntityWithTenantAndContext.class);

    assertEquals(0, count, "Count came correct for another tenant");
  }

  @Test
  void findById_ForCurrentTenantAndContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    EntityWithTenantAndContext byId = tenancyEnforcedMongoTemplate.findById(TONE_TEST_ID,
        EntityWithTenantAndContext.class);

    assertNotNull(byId, "Entity not found for the current tenant");
    assertEquals(byId.getTenantId(), TONE, "Tenant id not matching");
    assertEquals(byId.getAdditionalContextId(), TONE_AONE, "Tenant id not matching");
  }

  @Test
  void findById_ForCurrentTenantAndAnotherContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_ATWO);

    EntityWithTenantAndContext byId = tenancyEnforcedMongoTemplate.findById(TONE_TEST_ID,
        EntityWithTenantAndContext.class);

    assertNull(byId, "Entity not found for the current tenant");
  }

  @Test
  void findById_ForAnotherTenantId() {
    TenantContextUtil.setTenantId(TTWO);
    TenantContextUtil.setAdditionalContextId(TTWO_AONE);

    EntityWithTenantAndContext byId = tenancyEnforcedMongoTemplate.findById(TONE_TEST_ID,
        EntityWithTenantAndContext.class);

    assertNull(byId, "Entity found for the another tenant");
  }

  @Test
  void findDistinct_ForCurrentTenantAndContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    List<String> entity = tenancyEnforcedMongoTemplate.findDistinct(CODE,
        EntityWithTenantAndContext.class,
        String.class);

    assertEquals(1, entity.stream().count());
    assertFalse(entity.stream().anyMatch(code -> TEST_TTWO.equals(code)),
        "Able to fetch another tenant's data");
    assertFalse(entity.stream().anyMatch(code -> TONE_ATWO.equals(code)),
        "Able to fetch another context's data");
    assertTrue(entity.stream().anyMatch(code -> TEST_TONE.equals(code)),
        "Not able to fetch current tenant's data");
  }

  @Test
  void findDistinct_ForAnotherTenantId() {
    TenantContextUtil.setTenantId(TTWO);
    TenantContextUtil.setAdditionalContextId(TTWO_AONE);
    List<String> entity = tenancyEnforcedMongoTemplate.findDistinct(CODE,
        EntityWithTenantAndContext.class,
        String.class);

    assertEquals(1, entity.stream().count());
    assertFalse(entity.stream().anyMatch(code -> TONE_AONE.equals(code)),
        "Able to fetch another context's data");
    assertFalse(entity.stream().anyMatch(code -> TEST_TONE.equals(code)),
        "Able to fetch another tenant's data");
    assertTrue(entity.stream().anyMatch(code -> TEST_TTWO.equals(code)),
        "Not able to fetch current tenant's data");
  }

  @Test
  void findAndModify_forCurrentTenantId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    tenancyEnforcedMongoTemplate.findAndModify(
        Query.query(Criteria.where(ID).is(TONE_TEST_ID)),
        Update.update(NAME, NAME_UPDATED), EntityWithTenantAndContext.class);

    EntityWithTenantAndContext entityWithTenant = defaultMongoTemplate.findById(TONE_TEST_ID,
        EntityWithTenantAndContext.class);
    assertEquals("name-updated", entityWithTenant.getName(),
        "name not updated for the current tenant");
  }

  @Test
  void findAndModify_forCurrentTenantAndAnotherContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_ATWO);

    EntityWithTenantAndContext modified = tenancyEnforcedMongoTemplate.findAndModify(
        Query.query(Criteria.where(ID).is(TONE_TEST_ID)),
        Update.update(NAME, NAME_UPDATED_2), EntityWithTenantAndContext.class);

    assertNull(modified,
        "name updated for the another context id");

    EntityWithTenantAndContext entityWithTenant = defaultMongoTemplate.findById(TONE_TEST_ID,
        EntityWithTenantAndContext.class);
    assertNotEquals(NAME_UPDATED_2, entityWithTenant.getName(),
        "name updated for the another context id");
  }

  @Test
  void findAndModify_forAnotherTenantId() {
    TenantContextUtil.setTenantId(TTWO);
    TenantContextUtil.setAdditionalContextId(TTWO_AONE);

    tenancyEnforcedMongoTemplate.findAndModify(
        Query.query(Criteria.where(ID).is(TONE_TEST_ID)),
        Update.update(NAME, NAME_UPDATED_1), EntityWithTenantAndContext.class);

    EntityWithTenantAndContext entityWithTenant = defaultMongoTemplate.findById(TONE_TEST_ID,
        EntityWithTenantAndContext.class);
    assertNotEquals(NAME_UPDATED_1, entityWithTenant.getName(),
        "name updated for the another tenant");
  }

  @Test
  void findAndReplace_forCurrentTenantAndContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    EntityWithTenantAndContext toReplace = EntityWithTenantAndContext.builder()
        .name(REPLACED_ENTITY)
        .code(TEST_TONE).build();

    EntityWithTenantAndContext replaced = tenancyEnforcedMongoTemplate.findAndReplace(
        Query.query(Criteria.where(ID).is(TENANT_ID_FOR_REPLACE)),
        toReplace);

    assertNotNull(replaced, "Not replaced for current tenant and context id");

    EntityWithTenantAndContext entityWithTenant = defaultMongoTemplate.findById(
        TENANT_ID_FOR_REPLACE,
        EntityWithTenantAndContext.class);
    assertEquals(REPLACED_ENTITY, entityWithTenant.getName(),
        "name not updated for the current tenant");
    assertEquals(TONE, entityWithTenant.getTenantId(),
        "tenant id not updated for the current tenant");
    assertEquals(TONE_AONE, entityWithTenant.getAdditionalContextId(),
        "tenant id not updated for the current tenant");
  }

  @Test
  void findAndReplace_forTenantAndAnotherContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    String replace = "replace-3";
    EntityWithTenantAndContext toReplace = EntityWithTenantAndContext.builder()
        .name(replace)
        .code(TEST_TONE).build();
    EntityWithTenantAndContext replaced = tenancyEnforcedMongoTemplate.findAndReplace(
        Query.query(Criteria.where(ID).is("66069054c3da2e0c64152d67")),
        toReplace);

    assertNull(replaced, "able to replace for another context");

    EntityWithTenantAndContext entityWithTenant = defaultMongoTemplate.findById(
        "66069054c3da2e0c64152d67",
        EntityWithTenantAndContext.class);
    assertNotEquals(replace, entityWithTenant.getName(),
        "Able to update name for another tenant");
    assertNotEquals(TONE_AONE, entityWithTenant.getAdditionalContextId(),
        "Able to update context id");
    assertEquals(TONE, entityWithTenant.getTenantId());
    assertEquals(TONE_ATWO, entityWithTenant.getAdditionalContextId());
  }

  @Test
  void findAndReplace_forAnotherTenantId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    String replace = "replace-2";
    EntityWithTenantAndContext toReplace = EntityWithTenantAndContext.builder()
        .name(replace)
        .code(TEST_TTWO)
        .tenantId(TONE)
        .additionalContextId(TONE_AONE)
        .build();
    EntityWithTenantAndContext replaced = tenancyEnforcedMongoTemplate.findAndReplace(
        Query.query(Criteria.where(ID).is(TTWO_ID_FOR_REPLACE)),
        toReplace);

    assertNull(replaced, "able to replace for another tenant");

    EntityWithTenantAndContext entityWithTenant = defaultMongoTemplate.findById(TTWO_ID_FOR_REPLACE,
        EntityWithTenantAndContext.class);
    assertEquals(TEST_REPLACE, entityWithTenant.getName(),
        "Able to update name for another tenant");
    assertEquals(TTWO, entityWithTenant.getTenantId(),
        "Able to update tenant id for another tenant");
    assertEquals(TTWO_AONE, entityWithTenant.getAdditionalContextId(),
        "Able to update tenant id for another tenant");
  }

  @Test
  void findAndRemove_forCurrentTenantId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    tenancyEnforcedMongoTemplate.findAndRemove(
        Query.query(Criteria.where(ID).is(TONE_ID_FOR_REMOVE)), EntityWithTenantAndContext.class);

    EntityWithTenantAndContext entityWithTenant = defaultMongoTemplate.findById(TONE_ID_FOR_REMOVE,
        EntityWithTenantAndContext.class);
    assertNull(entityWithTenant, "entry not deleted for the current tenant");
  }

  @Test
  void findAndRemove_forTenantAndAnotherContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    tenancyEnforcedMongoTemplate.findAndRemove(
        Query.query(Criteria.where(ID).is("66069054c3da2e0c64152d19")),
        EntityWithTenantAndContext.class);

    EntityWithTenantAndContext entityWithTenant = defaultMongoTemplate.findById(
        "66069054c3da2e0c64152d19",
        EntityWithTenantAndContext.class);
    assertNotNull(entityWithTenant, "entry deleted for the tenant and context");
  }

  @Test
  void findAndRemove_forAnotherTenantId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    tenancyEnforcedMongoTemplate.findAndRemove(
        Query.query(Criteria.where(ID).is(TTWO_ID_FOR_REMOVE)), EntityWithTenantAndContext.class);

    EntityWithTenantAndContext entityWithTenant = defaultMongoTemplate.findById(TTWO_ID_FOR_REMOVE,
        EntityWithTenantAndContext.class);
    assertNotNull(entityWithTenant, "entry deleted for the another tenant");
  }

  @Test
  void find_ForCurrentTenantAndContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    Query query = Query.query(Criteria.where(NAME).exists(true));
    List<EntityWithTenantAndContext> entityWithCompanies = tenancyEnforcedMongoTemplate.find(query,
        EntityWithTenantAndContext.class);

    assertTrue(entityWithCompanies.stream().anyMatch(
            entityWithTenant -> TONE_TEST_ID.equals(entityWithTenant.getId())),
        "Not able to fetch current tenant's data");

    assertFalse(entityWithCompanies.stream().anyMatch(
            entityWithTenant -> !TONE.equals(entityWithTenant.getTenantId())),
        "able to fetch for another tenant data");

    assertFalse(entityWithCompanies.stream().anyMatch(
            entityWithTenant -> !TONE_AONE.equals(entityWithTenant.getAdditionalContextId())),
        "able to fetch for another context data");
  }

  @Test
  void find_ForAnotherTenantId() {
    TenantContextUtil.setTenantId(TTWO);
    TenantContextUtil.setAdditionalContextId(TONE_ATWO);

    Query query = Query.query(Criteria.where(NAME).exists(true));
    List<EntityWithTenantAndContext> entityWithCompanies = tenancyEnforcedMongoTemplate.find(query,
        EntityWithTenantAndContext.class);

    assertFalse(entityWithCompanies.stream().anyMatch(
            entityWithTenant -> TONE_TEST_ID.equals(entityWithTenant.getId())),
        "Not able to fetch current tenant's data");

    assertFalse(entityWithCompanies.stream().anyMatch(
            entityWithTenant -> !TTWO.equals(entityWithTenant.getTenantId())),
        "able to fetch for another tenant data");

    assertFalse(entityWithCompanies.stream().anyMatch(
            entityWithTenant -> !TEST_TTWO.equals(entityWithTenant.getAdditionalContextId())),
        "able to fetch for another context data");
  }

  @Test
  void save_withOutTenantId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    EntityWithTenantAndContext entityWithTenant = EntityWithTenantAndContext.builder()
        .name(TEST_STR + Instant.now().getEpochSecond())
        .code(TEST_TONE)
        .build();
    EntityWithTenantAndContext saved = tenancyEnforcedMongoTemplate.save(entityWithTenant);

    EntityWithTenantAndContext actual = defaultMongoTemplate.findById(entityWithTenant.getId(),
        EntityWithTenantAndContext.class);

    assertEquals(TONE, actual.getTenantId(), "tenant id not set properly");
    assertEquals(TONE_AONE, actual.getAdditionalContextId(), "context id not set properly");
  }

  @Test
  void save_withTenantIdAndContextId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    EntityWithTenantAndContext entityWithTenant = EntityWithTenantAndContext.builder()
        .name(TEST_STR + Instant.now().getEpochSecond())
        .code(TEST_TONE)
        .tenantId(TONE)
        .additionalContextId(TONE_ATWO)
        .build();
    EntityWithTenantAndContext saved = tenancyEnforcedMongoTemplate.save(entityWithTenant);

    EntityWithTenantAndContext entity = defaultMongoTemplate.findById(entityWithTenant.getId(),
        EntityWithTenantAndContext.class);

    assertNotEquals(TONE_ATWO, entity.getAdditionalContextId(),
        "context id set for another context");

    assertEquals(TONE, entity.getTenantId(), "tenant id not set properly");
    assertEquals(TONE_AONE, entity.getAdditionalContextId(), "context id not set properly");
  }

  @Test
  void save_withAnotherTenantId() {
    TenantContextUtil.setTenantId(TONE);
    TenantContextUtil.setAdditionalContextId(TONE_AONE);

    EntityWithTenantAndContext entityWithTenant = EntityWithTenantAndContext.builder()
        .name(TEST_STR + Instant.now().getEpochSecond())
        .code(TEST_TONE)
        .tenantId(TTWO)
        .additionalContextId(TONE_ATWO)
        .build();
    EntityWithTenantAndContext saved = tenancyEnforcedMongoTemplate.save(entityWithTenant);

    EntityWithTenantAndContext entity = defaultMongoTemplate.findById(entityWithTenant.getId(),
        EntityWithTenantAndContext.class);

    assertNotEquals(TTWO, entity.getTenantId(), "tenant id set for another tenant");
    assertNotEquals(TONE_ATWO, entity.getAdditionalContextId(),
        "context id set for another context");

    assertEquals(TONE, entity.getTenantId(), "tenant id not set properly");
    assertEquals(TONE_AONE, entity.getAdditionalContextId(), "context id not set properly");
  }

  @Test
  void insertCollection_forCurrentTenantId() {
    TenantContextUtil.setTenantId(TFOUR);
    TenantContextUtil.setAdditionalContextId(TFOUR_AONE);

    long epochSecond = Instant.now().getEpochSecond();
    List<EntityWithTenantAndContext> entityWithCompanies = List.of(
        EntityWithTenantAndContext.builder()
            .name(TEST_STR + epochSecond)
            .code("tfour-insert")
            .build(),
        EntityWithTenantAndContext.builder()
            .name(TEST_STR + epochSecond)
            .code("tfour-insert")
            .build()
    );

    tenancyEnforcedMongoTemplate.insert(entityWithCompanies, EntityWithTenantAndContext.class);

    Criteria criteria = Criteria.where(CODE).is("tfour-insert")
        .andOperator(Criteria.where(TENANT_ID).is(TFOUR),
            Criteria.where(NAME).is(TEST_STR + epochSecond),
            Criteria.where(ADDITIONAL_CONTEXT_ID).is(TFOUR_AONE));
    List<EntityWithTenantAndContext> entityWithTenantList = defaultMongoTemplate.find(
        Query.query(criteria),
        EntityWithTenantAndContext.class);
    assertEquals(2, entityWithTenantList.size(),
        "Batch not saved properly for the current tenant id");
  }

  @Test
  void insertCollection_forTenantAndAnotherContextId() {
    TenantContextUtil.setTenantId(TFOUR);
    TenantContextUtil.setAdditionalContextId(TFOUR_AONE);

    List<EntityWithTenantAndContext> entityWithCompanies = List.of(
        EntityWithTenantAndContext.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_TFOUR)
            .additionalContextId(TFOUR_ATWO)
            .build(),
        EntityWithTenantAndContext.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_TFOUR)
            .additionalContextId(TFOUR_ATWO)
            .build()
    );

    tenancyEnforcedMongoTemplate.insert(entityWithCompanies, EntityWithTenantAndContext.class);

    Criteria criteria = Criteria.where(CODE).is(TEST_TFOUR)
        .andOperator(Criteria.where(TENANT_ID).is(TFOUR),
            Criteria.where(ADDITIONAL_CONTEXT_ID).is(TFOUR_ATWO));
    List<EntityWithTenantAndContext> entityWithTenantList = defaultMongoTemplate.find(
        Query.query(criteria),
        EntityWithTenantAndContext.class);
    assertEquals(0, entityWithTenantList.size(),
        "Batch saved for the another context id");
  }


  @Test
  void insertCollection_forAnotherTenantId() {
    TenantContextUtil.setTenantId(TFIVE);
    TenantContextUtil.setAdditionalContextId(TFIVE_AONE);
    List<EntityWithTenantAndContext> entityWithCompanies = List.of(
        EntityWithTenantAndContext.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_TFIVE)
            .tenantId(TFOUR)
            .build(),
        EntityWithTenantAndContext.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_TFIVE)
            .tenantId(TFOUR)
            .build()
    );

    tenancyEnforcedMongoTemplate.insert(entityWithCompanies, EntityWithTenantAndContext.class);

    Criteria criteria = Criteria.where(CODE).is(TEST_TFIVE)
        .andOperator(Criteria.where(TENANT_ID).is(TFOUR));
    List<EntityWithTenantAndContext> entityWithTenantList = defaultMongoTemplate.find(
        Query.query(criteria),
        EntityWithTenantAndContext.class);
    assertEquals(0, entityWithTenantList.size(), "Batch saved for the another tenant id");
  }
}
