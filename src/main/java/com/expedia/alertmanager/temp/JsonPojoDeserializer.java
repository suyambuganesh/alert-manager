/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Copyright 2018 The Apache Software Foundation.
 *
 * From: https://github.com/apache/kafka/blob/1.1.0/streams/examples/src/main/java/org/apache/kafka/streams/examples/pageview/JsonPOJODeserializer.java
 */
package com.expedia.alertmanager.temp;

import com.expedia.metrics.jackson.MetricsJavaModule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class JsonPojoDeserializer<T> {
    private ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> tClass;

    /**
     * Default constructor needed by Kafka
     */
    public JsonPojoDeserializer() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModule(new MetricsJavaModule());
    }

    @SuppressWarnings("unchecked")
    public void configure(Map<String, ?> props) {
        Object propClass = props.get("JsonPojoClass");
        if (propClass instanceof Class) {
            tClass = (Class<T>) propClass;
        } else {
            try {
                tClass = (Class<T>) Class.forName(propClass.toString());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public T deserialize(byte[] bytes) {
        if (bytes == null)
            return null;

        T data;
        try {
            data = objectMapper.readValue(bytes, tClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return data;
    }
}
