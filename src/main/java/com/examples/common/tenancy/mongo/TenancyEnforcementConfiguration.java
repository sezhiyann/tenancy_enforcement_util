package com.examples.common.tenancy.mongo;

import com.examples.common.tenancy.TenantIdHolder;
import com.examples.common.tenancy.TenantUtil;
import com.mongodb.client.MongoClient;
import jakarta.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
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
  private ApplicationContext applicationContext;

  //mandatory to have this name, else apps will fail.
  @Bean(name = "mongoTemplate")
  TenancyEnforcedMongoTemplate mongoTemplate(
      MongoDatabaseFactorySupport<MongoClient> factorySupport) {
    return new TenancyEnforcedMongoTemplate(factorySupport);
  }

  @PostConstruct
  void gatherTenantFieldInformation() throws Exception {

    ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(
        false);
    componentProvider.addIncludeFilter(new AnnotationTypeFilter(Document.class));

    for (BeanDefinition candidate : componentProvider.findCandidateComponents(BASE_PACKAGE)) {
      Class<?> aClass = ClassUtils.forName(candidate.getBeanClassName(),
          TenancyEnforcementConfiguration.class.getClassLoader());

      String clazzName = aClass.getName();
      boolean tenancyEnabled = TenantIdHolder.class.isAssignableFrom(aClass);

      Document annotation = aClass.getAnnotation(Document.class);
      String collectionName = annotation.collection();

      TenantUtil.setTenancyEnabled(collectionName, tenancyEnabled);

      if (!tenancyEnabled) {
        continue;
      }

      Constructor<?> defaultConstructor = aClass.getDeclaredConstructor();
      if (defaultConstructor == null) {
        throw new RuntimeException(
            "Entities holding tenant specific data are required to have default constructors :"
                + clazzName);
      }

      TenantIdHolder tenantIdHolder = (TenantIdHolder) defaultConstructor.newInstance(null);
      String tenantIdFieldName = tenantIdHolder.getTenantIdFieldName();
      TenantUtil.setTenantField(collectionName, tenantIdFieldName);

    }
  }


}
