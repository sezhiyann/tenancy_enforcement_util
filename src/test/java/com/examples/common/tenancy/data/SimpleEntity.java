package com.examples.common.tenancy.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@SuperBuilder
@Document(collection = "simple_entity")
public class SimpleEntity extends BaseMongoEntity<String> {

}
