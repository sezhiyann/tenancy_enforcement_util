package com.examples.common.tenancy.mongo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.examples.common.tenancy.AbstractIntegrationTest;
import com.examples.common.tenancy.TenantUtil;
import com.examples.common.tenancy.data.EntityWithCompany;
import com.examples.common.tenancy.data.EntityWithCompanyRepository;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class EntityWithCompanyRepositoryIT extends AbstractIntegrationTest {

  public static final String CTHREE = "cthree";
  public static final String CFOUR = "cfour";
  @Autowired
  private EntityWithCompanyRepository repository;


  @Test
  void save_forCurrentCompany() {
    TenantUtil.setTenantId(CTHREE);
    EntityWithCompany entityWithCompany = EntityWithCompany.builder()
        .name("test-" + Instant.now().getEpochSecond())
        .code("test_cthree")
        .build();

    EntityWithCompany save = repository.save(entityWithCompany);

    EntityWithCompany actual = defaultMongoTemplate.findById(save.getId(), EntityWithCompany.class);
    assertEquals(CTHREE, actual.getCompanyId(), "Company id not saved.");
  }

  @Test
  void save_forAnotherCompany() {
    TenantUtil.setTenantId(CTHREE);
    EntityWithCompany entityWithCompany = EntityWithCompany.builder()
        .name("test-" + Instant.now().getEpochSecond())
        .code("test_cthree")
        .companyId(CFOUR)
        .build();

    EntityWithCompany save = repository.save(entityWithCompany);

    EntityWithCompany actual = defaultMongoTemplate.findById(save.getId(), EntityWithCompany.class);
    assertNotEquals(CFOUR, actual.getCompanyId(), "Able to save data for another company.");
    assertEquals(CTHREE, actual.getCompanyId(), "Company id not saved.");
  }
}
