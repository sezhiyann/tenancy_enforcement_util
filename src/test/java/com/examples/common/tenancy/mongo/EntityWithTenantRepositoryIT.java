package com.examples.common.tenancy.mongo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.examples.common.tenancy.AbstractIntegrationTest;
import com.examples.common.tenancy.TenantContextUtil;
import com.examples.common.tenancy.data.EntityWithTenant;
import com.examples.common.tenancy.data.EntityWithTenantRepository;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityWithTenantRepositoryIT extends AbstractIntegrationTest {

  public static final String TTHREE = "tthree";
  public static final String TFOUR = "tfour";
  public static final String TEST_tthree = "test_tthree";
  public static final String TEST_STR = "test-";
  @Autowired
  private EntityWithTenantRepository repository;


  @Test
  void save_forCurrentTenant() {
    TenantContextUtil.setTenantId(TTHREE);
    EntityWithTenant entityWithTenant = EntityWithTenant.builder()
        .name(TEST_STR + Instant.now().getEpochSecond())
        .code(TEST_tthree)
        .build();

    EntityWithTenant save = repository.save(entityWithTenant);

    EntityWithTenant actual = defaultMongoTemplate.findById(save.getId(), EntityWithTenant.class);
    assertEquals(TTHREE, actual.getTenantId(), "Tenant id not saved.");
  }

  @Test
  void save_forAnotherTenant() {
    TenantContextUtil.setTenantId(TTHREE);
    EntityWithTenant entityWithTenant = EntityWithTenant.builder()
        .name(TEST_STR + Instant.now().getEpochSecond())
        .code(TEST_tthree)
        .tenantId(TFOUR)
        .build();

    EntityWithTenant save = repository.save(entityWithTenant);

    EntityWithTenant actual = defaultMongoTemplate.findById(save.getId(), EntityWithTenant.class);
    assertNotEquals(TFOUR, actual.getTenantId(), "Able to save data for another tenant.");
    assertEquals(TTHREE, actual.getTenantId(), "Tenant id not saved.");
  }
}
