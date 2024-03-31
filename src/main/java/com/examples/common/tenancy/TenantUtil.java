package com.examples.common.tenancy;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

public final class TenantUtil {

  private static ThreadLocal<String> tenantIdPerRequest = new ThreadLocal<>();

  private static Map<String, String> mapOfTenantFieldNames = new HashMap<>(0);

  private static Map<String, Boolean> mapOfTenancyEnabledCollections = new HashMap<>(0);

  public static void setTenantId(String tenantId) {
    tenantIdPerRequest.set(tenantId);
  }

  public static String getTenantId() {
    String tenantId = tenantIdPerRequest.get();
    if (!StringUtils.hasText(tenantId)) {
      throw new RuntimeException("Tenant Id retrieved before initialization");
    }
    return tenantId;

  }

  public static void setTenantField(String collectionName, String tenantIdFieldName) {
    if (!StringUtils.hasText(collectionName)) {
      //TODO: change to platform specific exception.
      throw new RuntimeException("Collection Name cannot be null");
    }

    if (!StringUtils.hasText(tenantIdFieldName)) {
      //TODO: change to platform specific exception.
      throw new RuntimeException(
          "Entities holding tenant specific data are required to return field names via getTenantIdFieldName :"
              + collectionName);
    }

    //also create a map with fields against collection names.
    mapOfTenantFieldNames.put(collectionName, tenantIdFieldName);
  }

  public static String getTenantField(String collectionName) {
    String tenantIdFieldName = mapOfTenantFieldNames.get(collectionName);
    if (!StringUtils.hasText(tenantIdFieldName)) {
      //TODO: change to platform specific exception.
      throw new RuntimeException(
          "Collection is not configured with tenant id field : " + collectionName);
    }
    return tenantIdFieldName;
  }


  public static boolean isTenancyEnabled(String collectionName) {
    Boolean b = mapOfTenancyEnabledCollections.get(collectionName);
    if (b == null) {
      //TODO: change to platform specific exception.
      throw new RuntimeException(
          "Clazz is not configured properly, not able to find if tenancy is enabled or not : "
              + collectionName);
    }
    return b;
  }

  public static void setTenancyEnabled(String collectionName, boolean tenancyEnabled) {
    if (!StringUtils.hasText(collectionName)) {
      //TODO: change to platform specific exception.
      throw new RuntimeException("Clazz cannot be emtpy");
    }
    mapOfTenancyEnabledCollections.put(collectionName, tenancyEnabled);
  }

  private static void clear() {
    tenantIdPerRequest.remove();
    mapOfTenantFieldNames.clear();
    mapOfTenancyEnabledCollections.clear();
  }

}
