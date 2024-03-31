package com.examples.common.tenancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TenantContextUtilTest {

  @Test
  void setTenantId() {
    TenantContextUtil.setTenantId("1");
    assertEquals("1", TenantContextUtil.getTenantId());
  }

  @Test
  void getTenantId_returns_value() {
    TenantContextUtil.setTenantId("1");
    assertEquals("1", TenantContextUtil.getTenantId());
  }

  @Test
  void getTenantId_not_exists() {
    TenantContextUtil.setTenantId(null);
    assertThrows(RuntimeException.class, () -> TenantContextUtil.getTenantId());
  }

  @Test
  void setTenantField_returns_value() {
    TenantContextUtil.setTenantField("test", "testfield");
    assertEquals("testfield", TenantContextUtil.getTenantField("test"));
  }

  @Test
  void setTenantField_empty_collection() {
    assertThrows(RuntimeException.class, () -> TenantContextUtil.setTenantField(null, "testfield"));
  }

  @Test
  void setTenantField_empty_field() {
    assertThrows(RuntimeException.class, () -> TenantContextUtil.setTenantField("test", null));
  }

  @Test
  void setTenantField() {
    TenantContextUtil.setTenantField("test", "testfield");
    assertEquals("testfield", TenantContextUtil.getTenantField("test"));
  }

  @Test
  void getTenantField_empty() {
    assertThrows(RuntimeException.class, () -> TenantContextUtil.getTenantField("test"));
  }

  @Test
  void setTenancyEnabled() {
    TenantContextUtil.setTenancyEnabled("test", true);
    assertTrue(TenantContextUtil.isTenancyEnabled("test"), "Not returning true");
  }

  @Test
  void setAdditionalContextId() {
    TenantContextUtil.setAdditionalContextId("1");
    assertEquals("1", TenantContextUtil.getAdditionalContextId());
  }

  @Test
  void getAdditionalContextId_returns_value() {
    TenantContextUtil.setAdditionalContextId("1");
    assertEquals("1", TenantContextUtil.getAdditionalContextId());
  }

  @Test
  void getAdditionalContextId_not_exists() {
    TenantContextUtil.setAdditionalContextId(null);
    assertThrows(RuntimeException.class, () -> TenantContextUtil.getAdditionalContextId());
  }

  @Test
  void setAdditionalContextField_returns_value() {
    TenantContextUtil.setAdditionalContextField("test", "testfield");
    assertEquals("testfield", TenantContextUtil.getAdditionalContextField("test"));
  }

  @Test
  void setAdditionalContextField_empty_collection() {
    assertThrows(RuntimeException.class,
        () -> TenantContextUtil.setAdditionalContextField(null, "testfield"));
  }

  @Test
  void setAdditionalContextField_empty_field() {
    assertThrows(RuntimeException.class,
        () -> TenantContextUtil.setAdditionalContextField("test", null));
  }

  @Test
  void setAdditionalContextField() {
    TenantContextUtil.setAdditionalContextField("test", "testfield");
    assertEquals("testfield", TenantContextUtil.getAdditionalContextField("test"));
  }

  @Test
  void getAdditionalContextField_empty() {
    assertThrows(RuntimeException.class, () -> TenantContextUtil.getAdditionalContextField("test"));
  }

  @Test
  void setAdditionalContextEnabled() {
    TenantContextUtil.setAdditionalContextEnabled("test", true);
    assertTrue(TenantContextUtil.isAdditionalContextEnabled("test"), "Not returning true");
  }

}