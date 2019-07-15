/*
 * Copyright (c) 2019. Georgia-Pacific.  All rights reserved.
 * This software is the confidential and proprietary information of Georgia-Pacific.
 *
 *
 */

package com.gp.commerce.core.catalog;

import de.hybris.platform.persistence.audit.internal.AuditRecordUtil;
import de.hybris.platform.persistence.audit.payload.converter.PayloadConverterRegistry;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import de.hybris.platform.persistence.audit.payload.json.LocalizedTypedValue;
import de.hybris.platform.persistence.audit.payload.json.LocalizedValue;
import de.hybris.platform.persistence.audit.payload.json.MapBasedTypedValue;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.stream.Collectors;

public class GPJsonAuditUtils {

    public static Object getAttribute(String key, AuditPayload payload, PayloadConverterRegistry payloadConverterRegistry) {

        if (payload.isCollection(key)) {
            return GPJsonAuditUtils.convertCollection(payload.getCollection(key), payload.getAttributeType(key),
                    payload.getCollectionType(key),payloadConverterRegistry);
        } else {
            String attrValue = payload.getAttribute(key);
            Class attributeType = payload.getAttributeType(key);
            if (attrValue == null) {
                LocalizedTypedValue localizedValues = payload.getLocalizedAttribute(key);
                if (localizedValues != null) {
                    return GPJsonAuditUtils.convertLocalizedTypeValueToMap(localizedValues, attributeType,
                            null,payloadConverterRegistry);
                }

                MapBasedTypedValue mapBasedAttribute = payload.getMapBasedAttribute(key);
                if (mapBasedAttribute != null) {
                    return GPJsonAuditUtils.deserializeFromString(mapBasedAttribute.getValue());
                }
            }

            return payloadConverterRegistry.convertToObject(attrValue, attributeType);
        }
    }

    public static Object convertCollection(List<String> collection, Class attributeType, int collectionType
    , PayloadConverterRegistry payloadConverterRegistry) {
        Collection<Object> convertedObjects = AuditRecordUtil.buildCollectionForType(collectionType);
        Iterator collIt = collection.iterator();

        while(collIt.hasNext()) {
            String obj = (String)collIt.next();
            convertedObjects.add(payloadConverterRegistry.convertToObject(obj, attributeType));
        }

        return convertedObjects;
    }


    public static Map<String, Object> convertLocalizedTypeValueToMap(LocalizedTypedValue localizedValues,
                                                               Class attributeType,
                                                               String langIsoCode,
                                                               PayloadConverterRegistry payloadConverterRegistry) {
        if (localizedValues == null) {
            return null;
        } else {
            Map<String, Object> localizedValuesMap = new HashMap();
            Iterator localizedValueIterator = localizedValues.getValues().iterator();

            while(true) {
                LocalizedValue attrValue;
                List val;
                do {
                    if (!localizedValueIterator.hasNext()) {
                        return localizedValuesMap;
                    }

                    attrValue = (LocalizedValue)localizedValueIterator.next();
                    val = (List)attrValue.getValue().stream().map((v) -> {
                        return payloadConverterRegistry.convertToObject(v, attributeType);
                    }).collect(Collectors.toList());
                } while(langIsoCode != null && !langIsoCode.equalsIgnoreCase(attrValue.getLanguage()));

                if (val.size() == 1) {
                    localizedValuesMap.put(attrValue.getLanguage(), val.get(0));
                } else {
                    localizedValuesMap.put(attrValue.getLanguage(), val);
                }
            }
        }
    }

    public static Object deserializeFromString(String value) {
        ObjectInputStream ois = null;

        Object retObject;
        try {
            byte[] data = Base64.getDecoder().decode(value);
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            retObject = ois.readObject();
        } catch (Exception var7) {
            throw new IllegalStateException(var7);
        } finally {
            IOUtils.closeQuietly(ois);
        }

        return retObject;
    }
}
