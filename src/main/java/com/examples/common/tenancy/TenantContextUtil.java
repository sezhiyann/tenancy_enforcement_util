package com.examples.common.tenancy;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

public final class TenantContextUtil {

  private TenantContextUtil() {
  }

  private static final ThreadLocal<String> tenantIdPerRequest = new ThreadLocal<>();
  private static final Map<String, String> mapOfTenantFieldNames = new HashMap<>(0);

  private static final Map<String, Boolean> mapOfTenancyEnabledCollections = new HashMap<>(0);


  private static final ThreadLocal<String> contextIdPerRequest = new ThreadLocal<>();
  private static final Map<String, String> mapOfContextFieldNames = new HashMap<>(0);

  private static final Map<String, Boolean> mapOfAdditionalContextEnabledCollections = new HashMap<>(
      0);

  public static void setTenantId(String tenantId) {
    tenantIdPerRequest.set(tenantId);
  }

  public static String getTenantId() {
    String tenantId = tenantIdPerRequest.get();
    if (!StringUtils.hasText(tenantId)) {
      throw new TenantEnforcementException("Tenant Id retrieved before initialization");
    }
    return tenantId;

  }

  public static void setTenantField(String collectionName, String tenantIdFieldName) {
    if (!StringUtils.hasText(collectionName)) {

      throw new TenantEnforcementException("Collection Name cannot be null");
    }

    if (!StringUtils.hasText(tenantIdFieldName)) {

      throw new TenantEnforcementException(
          "Entities holding tenant specific data are required to return field names via getTenantIdFieldName :"
              + collectionName);
    }

    //also create a map with fields against collection names.
    mapOfTenantFieldNames.put(collectionName, tenantIdFieldName);
  }

  public static String getTenantField(String collectionName) {
    String tenantIdFieldName = mapOfTenantFieldNames.get(collectionName);
    if (!StringUtils.hasText(tenantIdFieldName)) {

      throw new TenantEnforcementException(
          "Collection is not configured with tenant id field : " + collectionName);
    }
    return tenantIdFieldName;
  }


  public static boolean isTenancyEnabled(String collectionName) {
    Boolean b = mapOfTenancyEnabledCollections.get(collectionName);
    if (b == null) {

      throw new TenantEnforcementException(
          "Clazz is not configured properly, not able to find if tenancy is enabled or not : "
              + collectionName);
    }
    return b;
  }

  public static void setTenancyEnabled(String collectionName, boolean tenancyEnabled) {
    if (!StringUtils.hasText(collectionName)) {

      throw new TenantEnforcementException("Clazz cannot be emtpy");
    }
    mapOfTenancyEnabledCollections.put(collectionName, tenancyEnabled);
  }


  public static void setAdditionalContextId(String tenantId) {
    contextIdPerRequest.set(tenantId);
  }

  public static String getAdditionalContextId() {
    String tenantId = contextIdPerRequest.get();
    if (!StringUtils.hasText(tenantId)) {
      throw new TenantEnforcementException("Tenant Id retrieved before initialization");
    }
    return tenantId;

  }

  public static void setAdditionalContextField(String collectionName, String tenantIdFieldName) {
    if (!StringUtils.hasText(collectionName)) {

      throw new TenantEnforcementException("Collection Name cannot be null");
    }

    if (!StringUtils.hasText(tenantIdFieldName)) {

      throw new TenantEnforcementException(
          "Entities holding tenant specific data are required to return field names via getTenantIdFieldName :"
              + collectionName);
    }

    //also create a map with fields against collection names.
    mapOfContextFieldNames.put(collectionName, tenantIdFieldName);
  }

  public static String getAdditionalContextField(String collectionName) {
    String tenantIdFieldName = mapOfContextFieldNames.get(collectionName);
    if (!StringUtils.hasText(tenantIdFieldName)) {

      throw new TenantEnforcementException(
          "Collection is not configured with tenant id field : " + collectionName);
    }
    return tenantIdFieldName;
  }


  public static boolean isAdditionalContextEnabled(String collectionName) {
    Boolean b = mapOfAdditionalContextEnabledCollections.get(collectionName);
    if (b == null) {

      throw new TenantEnforcementException(
          "Clazz is not configured properly, not able to find if tenancy is enabled or not : "
              + collectionName);
    }
    return b;
  }

  public static void setAdditionalContextEnabled(String collectionName, boolean tenancyEnabled) {
    if (!StringUtils.hasText(collectionName)) {

      throw new TenantEnforcementException("Clazz cannot be emtpy");
    }
    mapOfAdditionalContextEnabledCollections.put(collectionName, tenancyEnabled);
  }


  public static void unSet() {
    tenantIdPerRequest.remove();
    contextIdPerRequest.remove();
  }

}
