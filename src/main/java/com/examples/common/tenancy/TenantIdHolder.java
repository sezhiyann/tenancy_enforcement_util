package com.examples.common.tenancy;

import java.util.function.Consumer;

public interface TenantIdHolder {
  
  //field name that holds the tenant information.
  String getTenantIdFieldName();

  Consumer<String> getTenantIdSetterMethod();
}
