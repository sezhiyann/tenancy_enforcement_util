package com.examples.common.tenancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class TenantUtilTest {

  @Test
  void setTenantId() {
    TenantUtil.setTenantId("1");
    assertEquals("1", TenantUtil.getTenantId());
  }

  @Test
  void getTenantId_returns_value() {
    TenantUtil.setTenantId("1");
    assertEquals("1", TenantUtil.getTenantId());
  }

  @Test
  void getTenantId_not_exists() {
    assertThrows(RuntimeException.class, () -> TenantUtil.getTenantId());
  }

  @Test
  void setTenantField_returns_value() {
    TenantUtil.setTenantField("test", "testfield");
    assertEquals("testfield", TenantUtil.getTenantField("test"));
  }

  @Test
  void setTenantField_empty_collection() {
    assertThrows(RuntimeException.class, () -> TenantUtil.setTenantField(null, "testfield"));
  }

  @Test
  void setTenantField_empty_field() {
    assertThrows(RuntimeException.class, () -> TenantUtil.setTenantField("test", null));
  }

  @Test
  void setTenantField() {
    TenantUtil.setTenantField("test", "testfield");
    assertEquals("testfield", TenantUtil.getTenantField("test"));
  }

  @Test
  void getTenantField_empty() {
    assertThrows(RuntimeException.class, () -> TenantUtil.getTenantField("test"));
  }

  @Test
  void isTenancyEnabled() {
  }

  @Test
  void setTenancyEnabled() {
  }

  @AfterEach
  void clear() {
    try {
      Method method = TenantUtil.class.getDeclaredMethod("clear");
      method.setAccessible(true);

      method.invoke(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}