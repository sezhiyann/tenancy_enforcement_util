package com.examples.common.tenancy.data;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityWithTenantAndContextRepository extends
    MongoRepository<EntityWithTenantAndContext, String> {

}
