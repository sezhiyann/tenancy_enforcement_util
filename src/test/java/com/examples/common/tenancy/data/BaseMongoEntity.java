package com.examples.common.tenancy.data;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

@Data

public class BaseMongoEntity<G extends Serializable> implements Serializable {

  public static final String ID = "_id";
  public static final String CREATE_TS = "create_ts";
  public static final String CREATED_BY = "created_by";
  public static final String UPDATED_BY = "updated_by";
  public static final String UPDATE_TS = "update_ts";
  public static final String ENTITY_VERSION = "entity_version";
  public static final String LATEST_SOURCE = "latest_source";

  @Id
  protected G id;

  @LastModifiedDate
  @Field(UPDATE_TS)
  private Date updateTs;

  @CreatedDate
  @Field(CREATE_TS)
  private Date createTs;

  @Field(CREATED_BY)
  @CreatedBy
  private G createdBy;

  @Field(UPDATED_BY)
  @LastModifiedBy
  private G lastModifiedBy;

  public G getId() {
    return id;
  }

  public void setId(G id) {
    this.id = id;
  }

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