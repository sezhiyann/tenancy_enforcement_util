package com.examples.common.tenancy.mongo;


import static com.examples.common.tenancy.data.BaseMongoEntity.CODE;
import static com.examples.common.tenancy.data.BaseMongoEntity.ID;
import static com.examples.common.tenancy.data.BaseMongoEntity.NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.examples.common.tenancy.AbstractIntegrationTest;
import com.examples.common.tenancy.data.SimpleEntity;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.CollectionUtils;

class SimpleEntityIT extends AbstractIntegrationTest {

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

  public static final String TEST_TTWO = "test_ttwo";
  public static final String TEST_REPLACE = "test-replace";
  public static final String REPLACED_ENTITY = "replaced-entity";
  public static final String TONE_ID_FOR_REPLACE = "66069054c3da2e0c64152d58";
  public static final String TEST_STR = "test-";

  public static final String TENANT_ID_FOR_REPLACE = "66069054c3da2e0c64152d57";
  public static final String NAME_UPDATED_1 = "name-updated-1";
  public static final String NAME_UPDATED = "name-updated";

  public static final String TENANT_CHECK = "tenant-check";

  @Autowired
  @Qualifier("mongoTemplate")
  private TenancyEnforcedMongoTemplate tenancyEnforcedMongoTemplate;

  @Test
  void count() {
    Query query = Query.query(Criteria.where(NAME).is("count-check"));
    long count = tenancyEnforcedMongoTemplate.count(query, SimpleEntity.class);
    assertEquals(1, count, "Count not correct");
  }


  @Test
  void exactCount() {
    long count = tenancyEnforcedMongoTemplate.exactCount(
        Query.query(Criteria.where(NAME).is("exact-check")), SimpleEntity.class);

    assertEquals(1, count, "Count not correct");
  }


  @Test
  void findById() {
    SimpleEntity byId = tenancyEnforcedMongoTemplate.findById("6606905a5cf50f20304f4850",
        SimpleEntity.class);

    assertNotNull(byId, "Entity not found for the current tenant");
  }


  @Test
  void findDistinct() {
    List<String> entity = tenancyEnforcedMongoTemplate.findDistinct(CODE, SimpleEntity.class,
        String.class);
    assertEquals(2, entity.stream().count());
    assertTrue(entity.stream().anyMatch(code -> TEST_TONE.equals(code)),
        "Not able to fetch correct data");
    assertTrue(entity.stream().anyMatch(code -> TEST_TTWO.equals(code)),
        "Able to fetch another tenant's data");
  }


  @Test
  void findAndModify() {
    SimpleEntity modified = tenancyEnforcedMongoTemplate.findAndModify(
        Query.query(Criteria.where(ID).is("6606905a5cf50f20304f4850")),
        Update.update(NAME, NAME_UPDATED), SimpleEntity.class);
    assertNotNull(modified, "not updated");

    SimpleEntity entityWithTenant = defaultMongoTemplate.findById("6606905a5cf50f20304f4850",
        SimpleEntity.class);

    assertEquals("name-updated", entityWithTenant.getName(),
        "name not updated for the current tenant");
  }


  @Test
  void findAndReplace() {
    SimpleEntity toReplace = SimpleEntity.builder().name(REPLACED_ENTITY)
        .code(TEST_TONE).build();

    SimpleEntity replaced = tenancyEnforcedMongoTemplate.findAndReplace(
        Query.query(Criteria.where(ID).is("66069054c3da2e0c64152d57")),
        toReplace);

    assertNotNull(replaced, "not replaced");

    SimpleEntity entityWithTenant = defaultMongoTemplate.findById("66069054c3da2e0c64152d57",
        SimpleEntity.class);
    assertEquals(REPLACED_ENTITY, entityWithTenant.getName(),
        "name not updated for the current tenant");
  }


  @Test
  void findAndRemove() {

    Query query = Query.query(Criteria.where(ID).is("66069054c3da2e0c64152d10"));
    tenancyEnforcedMongoTemplate.findAndRemove(query, SimpleEntity.class);

    SimpleEntity entityWithTenant = defaultMongoTemplate.findById("66069054c3da2e0c64152d10",
        SimpleEntity.class);
    assertNull(entityWithTenant, "entry not deleted");
  }


  @Test
  void find() {
    Query query = Query.query(Criteria.where(NAME).is("count-check"));
    List<SimpleEntity> entityWithCompanies = tenancyEnforcedMongoTemplate.find(query,
        SimpleEntity.class);

    assertEquals(1, entityWithCompanies.stream().count());
    assertTrue(entityWithCompanies.stream().anyMatch(
            entityWithTenant -> "6606905a5cf50f20304f4840".equals(entityWithTenant.getId())),
        "Not able to fetch data");
  }

  @Test
  void save() {
    String name = TEST_STR + Instant.now().getEpochSecond();
    SimpleEntity entityWithTenant = SimpleEntity.builder()
        .name(name)
        .code(TEST_TONE)
        .build();
    SimpleEntity saved = tenancyEnforcedMongoTemplate.save(entityWithTenant);

    List<SimpleEntity> actual = defaultMongoTemplate.find(
        Query.query(Criteria.where(NAME).is(name)),
        SimpleEntity.class);

    assertEquals(1, actual.stream().count());
  }


  @Test
  void insertCollection() {
    String insert1 = "insert1" + Instant.now().getEpochSecond();
    String insert2 = "insert2" + Instant.now().getEpochSecond();
    List<SimpleEntity> entityWithCompanies = List.of(SimpleEntity.builder()
            .name(insert1)
            .code(TEST_TONE)
            .build(),
        SimpleEntity.builder()
            .name(insert2)
            .code(TEST_TONE)
            .build()
    );

    Collection<SimpleEntity> insert = tenancyEnforcedMongoTemplate.insert(entityWithCompanies,
        SimpleEntity.class);

    assertFalse(CollectionUtils.isEmpty(insert));
    assertTrue(insert.size() == 2);

    Criteria criteria = Criteria.where(CODE).is(TEST_TONE)
        .andOperator(Criteria.where(NAME).in(insert1, insert2));
    List<SimpleEntity> entityWithTenantList = defaultMongoTemplate.find(Query.query(criteria),
        SimpleEntity.class);
    assertEquals(2, entityWithTenantList.size(),
        "Batch not saved properly for the current tenant id");
  }

}
