package com.examples.common.tenancy.data;

import com.examples.common.tenancy.AdditionalContextIdHolder;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Document(collection = "entity_with_tenant_and_context")
public class EntityWithTenantAndContext extends BaseMongoEntity<String> implements
    AdditionalContextIdHolder {


  public static final String TENANT_ID = "tenant_id";
  private static final String ADDITIONAL_CONTEXT_ID = "additional_context_id";

  @Field(TENANT_ID)
  private String tenantId;

  @Field(ADDITIONAL_CONTEXT_ID)
  private String additionalContextId;


  @Override
  public String getTenantIdFieldName() {
    return TENANT_ID;
  }

  public Consumer<String> getTenantIdSetterMethod() {
    return this::setTenantId;
  }

  @Override
  public String getAdditionalContextIdFieldName() {
    return ADDITIONAL_CONTEXT_ID;
  }

  @Override
  public Consumer<String> getAdditionalContextIdSetterMethod() {
    return this::setAdditionalContextId;
  }
}
