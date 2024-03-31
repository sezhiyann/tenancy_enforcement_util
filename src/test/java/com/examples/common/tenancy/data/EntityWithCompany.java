package com.examples.common.tenancy.data;

import com.examples.common.tenancy.TenantIdHolder;
import java.util.function.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "entity_with_company")
public class EntityWithCompany extends BaseMongoEntity<String> implements TenantIdHolder {

  private static final String CODE = "code";
  private static final String COMPANY_ID = "company_id";
  private static final String NAME = "name";

  @Field(NAME)
  private String name;

  @Field(CODE)
  private String code;

  @Field(COMPANY_ID)
  private String companyId;

  @Override
  public String getTenantIdFieldName() {
    return COMPANY_ID;
  }

  public Consumer<String> getTenantIdSetterMethod() {
    return this::setCompanyId;
  }
}
