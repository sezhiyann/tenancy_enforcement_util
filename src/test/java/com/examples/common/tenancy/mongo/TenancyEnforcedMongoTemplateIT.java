package com.examples.common.tenancy.mongo;


import static com.examples.common.tenancy.data.BaseMongoEntity.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.examples.common.tenancy.AbstractIntegrationTest;
import com.examples.common.tenancy.TenantUtil;
import com.examples.common.tenancy.data.EntityWithCompany;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

class TenancyEnforcedMongoTemplateIT extends AbstractIntegrationTest {

  public static final String CONE = "cone";
  public static final String CTWO = "ctwo";
  public static final String CONE_TEST_ID = "6606905a5cf50f20304f4850";
  public static final String CTWO_TEST_ID = "66069054c3da2e0c64152d55";
  public static final String CFOUR = "cfour";
  public static final String CFIVE = "cfive";
  public static final String CONE_ID_FOR_REMOVE = "66069054c3da2e0c64152d10";
  public static final String CTWO_ID_FOR_REMOVE = "66069054c3da2e0c64152d11";
  public static final String TEST_CFOUR = "test_cfour";
  public static final String TEST_CFIVE = "test_cfive";
  public static final String CODE = "code";
  public static final String COMPANY_ID = "company_id";
  public static final String TEST_CONE = "test_cone";
  public static final String NAME = "name";
  public static final String TEST_REPLACE = "test-replace";
  public static final String REPLACED_ENTITY = "replaced-entity";
  public static final String CONE_ID_FOR_REPLACE = "66069054c3da2e0c64152d58";
  public static final String TEST_STR = "test-";

  @Autowired
  @Qualifier("mongoTemplate")
  private TenancyEnforcedMongoTemplate tenancyEnforcedMongoTemplate;

  @Test
  void count_ForCurrentCompanyId() {
    TenantUtil.setTenantId(CONE);

    Query query = Query.query(Criteria.where(ID).is(CONE_TEST_ID));
    long count = tenancyEnforcedMongoTemplate.count(
        query, EntityWithCompany.class);

    List<EntityWithCompany> all = defaultMongoTemplate.findAll(EntityWithCompany.class);

    assertEquals(1, count, "Count not correct for current company");
  }

  @Test
  void count_ForAnotherCompanyId() {
    TenantUtil.setTenantId(CONE);

    long count = tenancyEnforcedMongoTemplate.count(
        Query.query(Criteria.where(ID).is(CTWO_TEST_ID)), EntityWithCompany.class);

    assertEquals(0, count, "Count came correct for another company");
  }

  @Test
  void exactCount_ForCurrentCompanyId() {
    TenantUtil.setTenantId(CONE);

    long count = tenancyEnforcedMongoTemplate.exactCount(
        Query.query(Criteria.where(ID).is(CONE_TEST_ID)), EntityWithCompany.class);

    assertEquals(1, count, "Count not correct for current company");
  }

  @Test
  void exactCount_ForAnotherCompanyId() {
    TenantUtil.setTenantId(CONE);

    long count = tenancyEnforcedMongoTemplate.exactCount(
        Query.query(Criteria.where(ID).is(CTWO_TEST_ID)), EntityWithCompany.class);

    assertEquals(0, count, "Count came correct for another company");
  }

  @Test
  void findById_ForCurrentCompanyId() {
    TenantUtil.setTenantId(CONE);

    EntityWithCompany byId = tenancyEnforcedMongoTemplate.findById(CONE_TEST_ID,
        EntityWithCompany.class);

    assertNotNull(byId, "Entity not found for the current company");
  }

  @Test
  void findById_ForAnotherCompanyId() {
    TenantUtil.setTenantId(CTWO);

    EntityWithCompany byId = tenancyEnforcedMongoTemplate.findById(CONE_TEST_ID,
        EntityWithCompany.class);

    assertNull(byId, "Entity not found for the current company");
  }

  @Test
  void findDistinct_ForCurrentCompanyId() {
    TenantUtil.setTenantId(CONE);
    List<String> entity = tenancyEnforcedMongoTemplate.findDistinct(CODE, EntityWithCompany.class,
        String.class);

    assertEquals(1, entity.stream().count());
    assertTrue(entity.stream().anyMatch(code -> TEST_CONE.equals(code)),
        "Not able to fetch current company's data");
    assertFalse(entity.stream().anyMatch(code -> "test_ctwo".equals(code)),
        "Able to fetch another company's data");
  }

  @Test
  void findDistinct_ForAnotherCompanyId() {
    TenantUtil.setTenantId(CTWO);
    List<String> entity = tenancyEnforcedMongoTemplate.findDistinct(CODE, EntityWithCompany.class,
        String.class);

    assertEquals(1, entity.stream().count());
    assertFalse(entity.stream().anyMatch(code -> TEST_CONE.equals(code)),
        "Able to fetch another company's data");
    assertTrue(entity.stream().anyMatch(code -> "test_ctwo".equals(code)),
        "Not able to fetch current company's data");
  }


  @Test
  void findAndModify_forCurrentCompanyId() {
    TenantUtil.setTenantId(CONE);

    tenancyEnforcedMongoTemplate.findAndModify(
        Query.query(Criteria.where(ID).is(CONE_TEST_ID)),
        Update.update(NAME, "name-updated"), EntityWithCompany.class);

    EntityWithCompany entityWithCompany = defaultMongoTemplate.findById(CONE_TEST_ID,
        EntityWithCompany.class);
    assertEquals("name-updated", entityWithCompany.getName(),
        "name not updated for the current company");
  }

  @Test
  void findAndModify_forAnotherCompanyId() {
    TenantUtil.setTenantId(CTWO);

    tenancyEnforcedMongoTemplate.findAndModify(
        Query.query(Criteria.where(ID).is(CONE_TEST_ID)),
        Update.update(NAME, "name-updated-1"), EntityWithCompany.class);

    EntityWithCompany entityWithCompany = defaultMongoTemplate.findById(CONE_TEST_ID,
        EntityWithCompany.class);
    assertNotEquals("name-updated-1", entityWithCompany.getName(),
        "name updated for the another company");
  }

  @Test
  void findAndReplace_forCurrentCompanyId() {
    TenantUtil.setTenantId(CONE);

    EntityWithCompany toReplace = EntityWithCompany.builder().name(REPLACED_ENTITY)
        .code(TEST_CONE).build();
    tenancyEnforcedMongoTemplate.findAndReplace(
        Query.query(Criteria.where(ID).is("66069054c3da2e0c64152d57")),
        toReplace);

    EntityWithCompany entityWithCompany = defaultMongoTemplate.findById("66069054c3da2e0c64152d57",
        EntityWithCompany.class);
    assertEquals(REPLACED_ENTITY, entityWithCompany.getName(),
        "name not updated for the current company");
    assertEquals(CONE, entityWithCompany.getCompanyId(),
        "Company id not updated for the current company");
  }

  @Test
  void findAndReplace_forAnotherCompanyId() {
    TenantUtil.setTenantId(CONE);

    EntityWithCompany toReplace = EntityWithCompany.builder().name(REPLACED_ENTITY)
        .code(TEST_CONE).build();
    tenancyEnforcedMongoTemplate.findAndReplace(
        Query.query(Criteria.where(ID).is(CONE_ID_FOR_REPLACE)),
        toReplace);

    EntityWithCompany entityWithCompany = defaultMongoTemplate.findById(CONE_ID_FOR_REPLACE,
        EntityWithCompany.class);
    assertEquals(TEST_REPLACE, entityWithCompany.getName(),
        "Able to update name for another company");
    assertEquals(CTWO, entityWithCompany.getCompanyId(),
        "Able to update company id for another company");
  }

  @Test
  void findAndRemove_forCurrentCompanyId() {
    TenantUtil.setTenantId(CONE);

    tenancyEnforcedMongoTemplate.findAndRemove(
        Query.query(Criteria.where(ID).is(CONE_ID_FOR_REMOVE)), EntityWithCompany.class);

    EntityWithCompany entityWithCompany = defaultMongoTemplate.findById(CONE_ID_FOR_REMOVE,
        EntityWithCompany.class);
    assertNull(entityWithCompany, "entry not deleted for the current company");
  }

  @Test
  void findAndRemove_forAnotherCompanyId() {
    TenantUtil.setTenantId(CONE);

    tenancyEnforcedMongoTemplate.findAndRemove(
        Query.query(Criteria.where(ID).is(CTWO_ID_FOR_REMOVE)), EntityWithCompany.class);

    EntityWithCompany entityWithCompany = defaultMongoTemplate.findById(CTWO_ID_FOR_REMOVE,
        EntityWithCompany.class);
    assertNotNull(entityWithCompany, "entry deleted for the another company");
  }

  @Test
  void find_ForCurrentCompanyId() {
    TenantUtil.setTenantId(CONE);

    Query query = Query.query(Criteria.where(NAME).exists(true));
    List<EntityWithCompany> entityWithCompanies = tenancyEnforcedMongoTemplate.find(query,
        EntityWithCompany.class);

    assertTrue(entityWithCompanies.stream().anyMatch(
            entityWithCompany -> CONE_TEST_ID.equals(entityWithCompany.getId())),
        "Not able to fetch current company's data");
  }

  @Test
  void find_ForAnotherCompanyId() {
    TenantUtil.setTenantId(CTWO);

    Query query = Query.query(Criteria.where(NAME).exists(true));
    List<EntityWithCompany> entityWithCompanies = tenancyEnforcedMongoTemplate.find(query,
        EntityWithCompany.class);

    assertFalse(entityWithCompanies.stream().anyMatch(
            entityWithCompany -> CONE_TEST_ID.equals(entityWithCompany.getId())),
        "Not able to fetch current company's data");
  }

  @Test
  void save_withOutCompanyId() {
    TenantUtil.setTenantId(CONE);
    EntityWithCompany entityWithCompany = EntityWithCompany.builder()
        .name(TEST_STR + Instant.now().getEpochSecond())
        .code(TEST_CONE)
        .build();
    EntityWithCompany saved = tenancyEnforcedMongoTemplate.save(entityWithCompany);

    EntityWithCompany actual = defaultMongoTemplate.findById(entityWithCompany.getId(),
        EntityWithCompany.class);

    assertTrue(CONE.equals(actual.getCompanyId()), "Company id not set properly");
  }

  @Test
  void save_withAnotherCompanyId() {
    TenantUtil.setTenantId(CONE);
    EntityWithCompany entityWithCompany = EntityWithCompany.builder()
        .name(TEST_STR + Instant.now().getEpochSecond())
        .code(TEST_CONE)
        .companyId(CTWO)
        .build();
    EntityWithCompany saved = tenancyEnforcedMongoTemplate.save(entityWithCompany);

    EntityWithCompany entity = defaultMongoTemplate.findById(entityWithCompany.getId(),
        EntityWithCompany.class);

    assertTrue(CONE.equals(entity.getCompanyId()), "Company id not set properly");
  }

  @Test
  void insertCollection_forCurrentCompanyId() {
    TenantUtil.setTenantId(CFOUR);
    List<EntityWithCompany> entityWithCompanies = List.of(EntityWithCompany.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_CFOUR)
            .companyId(CFOUR)
            .build(),
        EntityWithCompany.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_CFOUR)
            .companyId(CFOUR)
            .build()
    );

    tenancyEnforcedMongoTemplate.insert(entityWithCompanies, EntityWithCompany.class);

    Criteria criteria = Criteria.where(CODE).is(TEST_CFOUR)
        .andOperator(Criteria.where(COMPANY_ID).is(CFOUR));
    List<EntityWithCompany> entityWithCompanyList = defaultMongoTemplate.find(Query.query(criteria),
        EntityWithCompany.class);
    assertEquals(2, entityWithCompanyList.size(),
        "Batch not saved properly for the current company id");
  }

  @Test
  void insertCollection_forAnotherCompanyId() {
    TenantUtil.setTenantId(CFIVE);
    List<EntityWithCompany> entityWithCompanies = List.of(EntityWithCompany.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_CFIVE)
            .companyId(CFOUR)
            .build(),
        EntityWithCompany.builder()
            .name(TEST_STR + Instant.now().getEpochSecond())
            .code(TEST_CFIVE)
            .companyId(CFOUR)
            .build()
    );

    tenancyEnforcedMongoTemplate.insert(entityWithCompanies, EntityWithCompany.class);

    Criteria criteria = Criteria.where(CODE).is(TEST_CFIVE)
        .andOperator(Criteria.where(COMPANY_ID).is(CFOUR));
    List<EntityWithCompany> entityWithCompanyList = defaultMongoTemplate.find(Query.query(criteria),
        EntityWithCompany.class);
    assertEquals(0, entityWithCompanyList.size(), "Batch saved for the another company id");
  }
}
