package com.examples.common.tenancy;

import java.util.function.Consumer;

public interface AdditionalContextIdHolder extends TenantIdHolder {

  //field name that holds the additional context information.
  String getAdditionalContextIdFieldName();

  Consumer<String> getAdditionalContextIdSetterMethod();
}
