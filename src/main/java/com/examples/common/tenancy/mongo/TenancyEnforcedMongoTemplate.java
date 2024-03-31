package com.examples.common.tenancy.mongo;

import com.examples.common.tenancy.TenantIdHolder;
import com.examples.common.tenancy.TenantUtil;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.bson.Document;
import org.springframework.data.domain.Window;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.mongodb.core.CollectionPreparer;
import org.springframework.data.mongodb.core.DocumentCallbackHandler;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.FindAndReplaceOptions;
import org.springframework.data.mongodb.core.MongoDatabaseFactorySupport;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoWriter;
import org.springframework.data.mongodb.core.mapreduce.MapReduceOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

//why inheritance instead of composition?
//so that where ever we pass mongo template, we can use this class. less work.
//why not aop?
//Make sure everybody knows that this is being applied. Hard dependency.
public class TenancyEnforcedMongoTemplate extends MongoTemplate {

  public static final UnsupportedOperationException UNSUPPORTED_OPERATION_EXCEPTION = new UnsupportedOperationException(
      "Operation not supported at this moment");

  //TODO : validate all mongo template methods are implemented here.

  public TenancyEnforcedMongoTemplate(MongoDatabaseFactorySupport factorySupport) {
    super(factorySupport);
  }

  public <T> Stream<T> stream(Query query, Class<T> entityType, String collectionName) {
    return super.stream(setTenantIdRestriction(entityType, query), entityType, collectionName);
  }


  protected <T> T doFindOne(String collectionName,
      CollectionPreparer<MongoCollection<Document>> collectionPreparer,
      Document query, Document fields, Class<T> entityClass) {
    return super.doFindOne(collectionName, collectionPreparer,
        setTenantIdRestriction(collectionName, query), fields, entityClass);
  }


  public void executeQuery(Query query, String collectionName, DocumentCallbackHandler dch) {
    super.executeQuery(setTenantIdRestriction(collectionName, query), collectionName, dch);
  }


  public <T> T findOne(Query query, Class<T> entityClass, String collectionName) {
    return super.findOne(setTenantIdRestriction(entityClass, query), entityClass, collectionName);
  }

  public boolean exists(Query query, @Nullable Class<?> entityClass, String collectionName) {
    return super.exists(setTenantIdRestriction(collectionName, query), entityClass, collectionName);
  }


  public <T> List<T> find(Query query, Class<T> entityClass, String collectionName) {
    return super.find(setTenantIdRestriction(entityClass, query), entityClass, collectionName);
  }


  public <T> List<T> findDistinct(Query query, String field, String collectionName,
      Class<?> entityClass, Class<T> resultClass) {
    return super.findDistinct(setTenantIdRestriction(collectionName, query), field, collectionName,
        entityClass, resultClass);
  }


  public <T> T findAndModify(Query query, UpdateDefinition update, FindAndModifyOptions options,
      Class<T> entityClass, String collectionName) {
    return super.findAndModify(setTenantIdRestriction(collectionName, query), update, options,
        entityClass, collectionName);
  }

  public <S, T> T findAndReplace(Query query, S replacement, FindAndReplaceOptions options,
      Class<S> entityType, String collectionName, Class<T> resultType) {
    return super.findAndReplace(setTenantIdRestriction(collectionName, query),
        setTenantIdRestriction(replacement), options,
        entityType, collectionName, resultType);
  }


  protected <T> List<T> doFindAndDelete(String collectionName, Query query, Class<T> entityClass) {
    return super.doFindAndDelete(collectionName, setTenantIdRestriction(collectionName, query),
        entityClass);
  }

  public long count(Query query, @Nullable Class<?> entityClass, String collectionName) {
    return super.count(setTenantIdRestriction(collectionName, query), entityClass, collectionName);
  }

  public long exactCount(Query query, @Nullable Class<?> entityClass, String collectionName) {
    return super.exactCount(setTenantIdRestriction(collectionName, query), entityClass,
        collectionName);
  }

  protected <T> Collection<T> doInsertBatch(String collectionName,
      Collection<? extends T> batchToSave, MongoWriter<T> writer) {
    return super.doInsertBatch(collectionName, setTenantIdRestriction(batchToSave),
        writer);
  }


  public <T> T insert(T objectToSave, String collectionName) {
    return super.insert(setTenantIdRestriction(objectToSave), collectionName);
  }

  public <T> T save(T objectToSave, String collectionName) {
    T objectWithTenantId = setTenantIdRestriction(objectToSave);
    return super.save(objectWithTenantId, collectionName);
  }


  public <T> Window<T> scroll(Query query, Class<T> entityType, String collectionName) {
    return super.scroll(setTenantIdRestriction(collectionName, query), entityType, collectionName);
  }

  protected UpdateResult doUpdate(String collectionName, Query query, UpdateDefinition update,
      @Nullable Class<?> entityClass, boolean upsert, boolean multi) {
    Query modifiedQuery = setTenantIdRestriction(collectionName, query);
    UpdateDefinition modifiedUpdate = setTenantIdRestriction(collectionName, update);
    return super.doUpdate(collectionName, modifiedQuery, modifiedUpdate, entityClass, upsert,
        multi);
  }

  protected <T> DeleteResult doRemove(String collectionName, Query query,
      @Nullable Class<T> entityClass,
      boolean multi) {
    return super.doRemove(collectionName, setTenantIdRestriction(collectionName, query),
        entityClass, multi);
  }

  public <T> T findAndRemove(Query query, Class<T> entityClass, String collectionName) {
    return super.findAndRemove(setTenantIdRestriction(collectionName, query), entityClass,
        collectionName);
  }


  public Document executeCommand(Document command, @Nullable ReadPreference readPreference) {
    throw UNSUPPORTED_OPERATION_EXCEPTION;
  }

  public Document executeCommand(String jsonCommand) {
    throw UNSUPPORTED_OPERATION_EXCEPTION;
  }

  public Document executeCommand(Document command) {
    throw UNSUPPORTED_OPERATION_EXCEPTION;
  }

  public <T> ExecutableFind<T> query(Class<T> domainType) {
    throw UNSUPPORTED_OPERATION_EXCEPTION;
  }

  public <T> ExecutableUpdate<T> update(Class<T> domainType) {
    throw UNSUPPORTED_OPERATION_EXCEPTION;
  }

  public <T> ExecutableRemove<T> remove(Class<T> domainType) {
    throw UNSUPPORTED_OPERATION_EXCEPTION;
  }

  public <T> ExecutableInsert<T> insert(Class<T> domainType) {
    throw UNSUPPORTED_OPERATION_EXCEPTION;
  }

  public <T> ExecutableAggregation<T> aggregateAndReturn(Class<T> domainType) {
    throw UNSUPPORTED_OPERATION_EXCEPTION;
  }

  public <T> List<T> findAll(Class<T> entityClass, String collectionName) {
    throw UNSUPPORTED_OPERATION_EXCEPTION;
  }

  public <T> List<T> mapReduce(Query query, Class<?> domainType, String inputCollectionName,
      String mapFunction, String reduceFunction, @Nullable MapReduceOptions mapReduceOptions,
      Class<T> resultType) {
    throw UNSUPPORTED_OPERATION_EXCEPTION;
  }

  public <T> GeoResults<T> geoNear(NearQuery near, Class<?> domainType, String collectionName,
      Class<T> returnType) {
    throw UNSUPPORTED_OPERATION_EXCEPTION;
  }

  private UpdateDefinition setTenantIdRestriction(String collectionName, UpdateDefinition update) {
    setTenantIdRestriction(collectionName, update.getUpdateObject());
    return update;
  }

  private Document setTenantIdRestriction(String collectionName, Document query) {
    if (!TenantUtil.isTenancyEnabled(collectionName)) {
      return query;
    }

    String tenantField = TenantUtil.getTenantField(collectionName);
    String tenantId = TenantUtil.getTenantId();

    query.put(tenantField, tenantId);

    return query;
  }

  private <T> Query setTenantIdRestriction(Class<T> entityClass, Query query) {
    if (!TenantIdHolder.class.isAssignableFrom(entityClass)) {
      return query;
    }
    return setTenantIdRestriction(this.getCollectionName(entityClass), query);
  }

  private Query setTenantIdRestriction(String collectionName, Query query) {
    if (!TenantUtil.isTenancyEnabled(collectionName)) {
      return query;
    }
    String tenantField = TenantUtil.getTenantField(collectionName);
    String tenantId = TenantUtil.getTenantId();
    return addTenantIdToQuery(query, tenantField, tenantId);
  }

  private Query addTenantIdToQuery(Query query, String tenantField, String tenantId) {
    return query.addCriteria(Criteria.where(tenantField).is(tenantId));
  }

  private <T> T setTenantIdRestriction(T objectToSave) {
    if (objectToSave == null || !TenantIdHolder.class.isAssignableFrom(objectToSave.getClass())) {
      return objectToSave;
    }

    Consumer<String> tenantIdSetterMethod = ((TenantIdHolder) objectToSave).getTenantIdSetterMethod();

    if (tenantIdSetterMethod == null) {
      throw new RuntimeException(
          "Tenant id setter method should not be empty " + objectToSave.getClass());
    }

    tenantIdSetterMethod.accept(TenantUtil.getTenantId());

    return objectToSave;
  }

  private <T> Collection<? extends T> setTenantIdRestriction(Collection<? extends T> batchToSave) {
    if (CollectionUtils.isEmpty(batchToSave)) {
      return batchToSave;
    }
    batchToSave.forEach(this::setTenantIdRestriction);
    return batchToSave;
  }

}