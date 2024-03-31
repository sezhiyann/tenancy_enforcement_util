package com.examples.common.tenancy;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

public final class TenantContextUtil {

  private static final ThreadLocal<String> tenantIdPerRequest = new ThreadLocal<>();
  private static final Map<String, String> mapOfTenantFieldNames = new HashMap<>(0);
  private static final Map<String, Boolean> mapOfTenancyEnabledCollections
      = new HashMap<>(0);
  private static final ThreadLocal<String> contextIdPerRequest = new ThreadLocal<>();
  private static final Map<String, String> mapOfContextFieldNames = new HashMap<>(0);
  private static final Map<String, Boolean> mapOfContextEnabledCollections
      = new HashMap<>(0);

  private TenantContextUtil() {
  }

  public static String getTenantId() {
    String tenantId = tenantIdPerRequest.get();
    if (!StringUtils.hasText(tenantId)) {
      throw new TenantEnforcementException("Tenant Id retrieved before initialization");
    }
    return tenantId;

  }

  public static void setTenantId(String tenantId) {
    tenantIdPerRequest.set(tenantId);
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
    Boolean tenancyEnabled = mapOfTenancyEnabledCollections.get(collectionName);
    if (tenancyEnabled == null) {
      throw new TenantEnforcementException(
          "Clazz is not configured properly, not able to find if tenancy is enabled or not : "
              + collectionName);
    }
    return tenancyEnabled;
  }

  public static void setTenancyEnabled(String collectionName, boolean tenancyEnabled) {
    if (!StringUtils.hasText(collectionName)) {
      throw new TenantEnforcementException("Clazz cannot be emtpy");
    }
    mapOfTenancyEnabledCollections.put(collectionName, tenancyEnabled);
  }

  public static String getAdditionalContextId() {
    String additionalContextId = contextIdPerRequest.get();
    if (!StringUtils.hasText(additionalContextId)) {
      throw new TenantEnforcementException("Additional context id retrieved before initialization");
    }
    return additionalContextId;

  }

  public static void setAdditionalContextId(String additionalContextId) {
    contextIdPerRequest.set(additionalContextId);
  }

  public static void setAdditionalContextField(String collectionName,
      String additionalContextFieldName) {
    if (!StringUtils.hasText(collectionName)) {
      throw new TenantEnforcementException("Collection Name cannot be null");
    }

    if (!StringUtils.hasText(additionalContextFieldName)) {
      throw new TenantEnforcementException(
          "Entities holding additional context data are required to return field names via getAdditionalContextFieldName :"
              + collectionName);
    }

    //also create a map with fields against collection names.
    mapOfContextFieldNames.put(collectionName, additionalContextFieldName);
  }

  public static String getAdditionalContextField(String collectionName) {
    String additionalContextFieldName = mapOfContextFieldNames.get(collectionName);
    if (!StringUtils.hasText(additionalContextFieldName)) {
      throw new TenantEnforcementException(
          "Collection is not configured with additional context id field : " + collectionName);
    }
    return additionalContextFieldName;
  }

  public static boolean isAdditionalContextEnabled(String collectionName) {
    Boolean additionalContextEnabled = mapOfContextEnabledCollections.get(collectionName);
    if (additionalContextEnabled == null) {
      throw new TenantEnforcementException(
          "Clazz is not configured properly, not able to find if additional context is enabled or not : "
              + collectionName);
    }
    return additionalContextEnabled;
  }

  public static void setAdditionalContextEnabled(String collectionName,
      boolean additionalContextEnabled) {
    if (!StringUtils.hasText(collectionName)) {
      throw new TenantEnforcementException("Clazz cannot be emtpy");
    }
    mapOfContextEnabledCollections.put(collectionName, additionalContextEnabled);
  }


  public static void unSet() {
    tenantIdPerRequest.remove();
    contextIdPerRequest.remove();
  }

}
