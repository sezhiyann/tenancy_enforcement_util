package com.examples.common.tenancy.mongo;

import com.examples.common.tenancy.AdditionalContextIdHolder;
import com.examples.common.tenancy.TenantContextUtil;
import com.examples.common.tenancy.TenantEnforcementException;
import com.examples.common.tenancy.TenantIdHolder;
import com.mongodb.client.MongoClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.mongodb.core.MongoDatabaseFactorySupport;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.ClassUtils;

@Configuration
public class TenancyEnforcementConfiguration {

  //TODO: Can we pick this up from somewhere? for now this is reasonable limit.
  public static final String BASE_PACKAGE = "com.examples";

  //mandatory to have this name, else apps will fail.
  @Bean(name = "mongoTemplate")
  TenancyEnforcedMongoTemplate mongoTemplate(
      MongoDatabaseFactorySupport<MongoClient> factorySupport) {
    return new TenancyEnforcedMongoTemplate(factorySupport);
  }

  @PostConstruct
  void gatherTenantFieldInformation()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

    ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(
        false);
    componentProvider.addIncludeFilter(new AnnotationTypeFilter(Document.class));

    for (BeanDefinition candidate : componentProvider.findCandidateComponents(BASE_PACKAGE)) {
      String clazzName = Objects.requireNonNull(candidate.getBeanClassName());
      Class<?> aClass = ClassUtils.forName(clazzName,
          TenancyEnforcementConfiguration.class.getClassLoader());

      boolean tenancyEnabled = TenantIdHolder.class.isAssignableFrom(aClass);

      boolean additionalContextEnabled = AdditionalContextIdHolder.class.isAssignableFrom(aClass);

      Document annotation = aClass.getAnnotation(Document.class);
      String collectionName = annotation.collection();

      TenantContextUtil.setTenancyEnabled(collectionName, tenancyEnabled);
      TenantContextUtil.setAdditionalContextEnabled(collectionName, additionalContextEnabled);

      Constructor<?> defaultConstructor = aClass.getDeclaredConstructor();
      if (defaultConstructor == null) {
        throw new TenantEnforcementException(
            "Entities holding tenant specific data are required to have default constructors :"
                + clazzName);
      }

      if (tenancyEnabled) {
        TenantIdHolder tenantIdHolder = (TenantIdHolder) defaultConstructor.newInstance(null);
        String tenantIdFieldName = tenantIdHolder.getTenantIdFieldName();
        TenantContextUtil.setTenantField(collectionName, tenantIdFieldName);
      }

      if (additionalContextEnabled) {
        AdditionalContextIdHolder additionalContextIdHolder = (AdditionalContextIdHolder) defaultConstructor.newInstance(
            null);
        String additionalContextIdFieldName = additionalContextIdHolder.getAdditionalContextIdFieldName();
        TenantContextUtil.setAdditionalContextField(collectionName, additionalContextIdFieldName);
      }
    }
  }

  @PreDestroy
  void onShutdown() {
    TenantContextUtil.unSet();
  }
}
