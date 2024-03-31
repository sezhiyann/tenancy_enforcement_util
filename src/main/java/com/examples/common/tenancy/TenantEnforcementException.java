package com.examples.common.tenancy;

public class TenantEnforcementException extends RuntimeException {

  public TenantEnforcementException(String msg) {
    super(msg);
  }
}
