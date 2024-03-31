package com.examples.common.tenancy.data;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseMongoEntity<G extends Serializable> implements Serializable {

  public static final String ID = "_id";
  public static final String CODE = "code";
  public static final String NAME = "name";

  @Field(NAME)
  private String name;

  @Field(CODE)
  private String code;

  @Id
  protected G id;

  @Override
  public boolean equals(Object entity) {
    if (entity instanceof BaseMongoEntity) {
      return this.getId().equals(((BaseMongoEntity) entity).getId());
    }
    return false;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((id == null) ? 0 : id.hashCode());
    return result;
  }
}