/*
 * Copyright (c) 2019. Georgia-Pacific.  All rights reserved.
 * This software is the confidential and proprietary information of Georgia-Pacific.
 *
 *
 */

package com.gp.commerce.core.catalog;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNullStandardMessage;

import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.persistence.audit.AuditType;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import de.hybris.platform.persistence.audit.gateway.AuditSearchQuery;
import de.hybris.platform.persistence.audit.gateway.AuditStorageUtils;
import de.hybris.platform.persistence.audit.gateway.JsonAuditRecord;
import de.hybris.platform.persistence.audit.gateway.ReadAuditGateway;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.audit.payload.converter.PayloadConverterRegistry;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import de.hybris.platform.persistence.audit.payload.json.TypedValue;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.gp.commerce.core.model.GPCompetitorProductModel;
import com.gp.commerce.core.model.ShippingRestrictionModel;
import com.gp.commerce.core.product.services.GPProductService;

/**
 * This class provides services to process product Audits
 */
public class GPProductAuditService {

    private static final Logger LOG = Logger.getLogger(GPProductAuditService.class);
	private final Set<String> productFeatureBlackList = new HashSet<>();
	private final Set<String> productBlackList = new HashSet<>();
    private ReadAuditGateway readAuditGateway;
    private WriteAuditGateway writeAuditGateway;
    private ModelService modelService;
    private CatalogService catalogService;
    private JdbcTemplate jdbcTemplate;
    private PayloadConverterRegistry payloadConverterRegistry;
    private GPProductService gpProductService;
    private Set<String> nonProductTypes;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyy-mm-dd hh:mm:ss.SSS");

	/**
	 * Removes all Audit logs
	 */
    public void removeAllAuditLogs() {

        int numRemoved = writeAuditGateway.removeAuditRecordsForType("Product");
        LOG.info("Products removed " + numRemoved);
        numRemoved = writeAuditGateway.removeAuditRecordsForType("ProductFeature");
        LOG.info("ProductFeature removed " + numRemoved);

    }

    /**
     * Save audit products.
     *
     * @param lastSaveDate the last save date
     */
    public void saveAuditProducts(final Date lastSaveDate) {

        final HashMap<String, GPProductAuditRecord> gpProductAuditRecordMap = new HashMap<>();
		productBlackList.addAll(Arrays.asList(StringUtils.split(Config.getParameter("product.blacklist.attributes"), ",")));
		productFeatureBlackList
				.addAll(Arrays.asList(StringUtils.split(Config.getParameter("product.features.blacklist.attributes"), ",")));

        nonProductTypes =new HashSet<>();
        nonProductTypes.add(GPProductAuditRecord.SHIPPING_RESTRICTION);
        nonProductTypes.add(GPProductAuditRecord.GP_COMPETITOR_2_ADDRESS_REL);
        nonProductTypes.add(GPProductAuditRecord.GP_COMPETITOR_2_PRODUCT_REL);
        nonProductTypes.add(GPProductAuditRecord.PRODUCT_2_B2BUNIT_REL);

        List<PK> prodPKList = null;
        List<PK> prodFeaturePKList = null;
        List<PK> shippingRestrictionPKList = null;
        final List<PK> competitor2ProductRelPKList = new ArrayList<>();
        List<PK> temporaryPKList = null;

        final long milliseconds = System.currentTimeMillis()-259200000;
		Date queryDate=new Date(milliseconds);
		if(null!=lastSaveDate) {
			//To read audit records 10 minutes prior to last start time.
			queryDate=new Date(lastSaveDate.getTime()-600000);
		}

        prodPKList = getPKsByTimestamp(GPProductAuditRecord.PRODUCT, queryDate);
        prodFeaturePKList = getPKsByTimestamp(GPProductAuditRecord.PRODUCT_FEATURE, queryDate);
        shippingRestrictionPKList = getPKsByTimestamp(GPProductAuditRecord.SHIPPING_RESTRICTION, queryDate);
        temporaryPKList = getPKsByTimestamp(GPProductAuditRecord.PRODUCT_2_B2BUNIT_REL, queryDate);
        if(CollectionUtils.isNotEmpty(temporaryPKList)) {
        	competitor2ProductRelPKList.addAll(temporaryPKList);
        }
        temporaryPKList = getPKsByTimestamp(GPProductAuditRecord.GP_COMPETITOR_2_ADDRESS_REL, queryDate);
        if(CollectionUtils.isNotEmpty(temporaryPKList)) {
        	competitor2ProductRelPKList.addAll(temporaryPKList);
        }
        temporaryPKList = getPKsByTimestamp(GPProductAuditRecord.GP_COMPETITOR_2_PRODUCT_REL, queryDate);
        if(CollectionUtils.isNotEmpty(temporaryPKList)) {
        	competitor2ProductRelPKList.addAll(temporaryPKList);
        }
		LOG.debug("PKs of audit logs:" + prodPKList + "\n" + prodFeaturePKList);

		LOG.debug("printingAuditProducts");
		Stream<AuditRecord> records;
		AuditSearchQuery auditSearchQuery;
		final int batchSize = 100;
		int start = 0;
		int end = 0;
        //need to filter on timestamp to reduce the number or rows returned
	    if(CollectionUtils.isNotEmpty(prodPKList))
		{
			int remainingSize = prodPKList.size();
			end = (remainingSize >= batchSize) ? batchSize : remainingSize;
			int iterations = remainingSize / batchSize;
			iterations = iterations + (remainingSize % batchSize) == 0 ? 0 : 1;
			for (int i = 0; i < iterations; i++)
			{
				auditSearchQuery = AuditSearchQuery.forType(GPProductAuditRecord.PRODUCT)
						.withPkSearchRules(prodPKList.subList(start, end).toArray(new PK[0])).build();
				records = readAuditGateway.search(auditSearchQuery);
				saveAttributeChangesToGPProductAuditMap(records.collect(Collectors.toList()), gpProductAuditRecordMap);

				remainingSize = remainingSize - batchSize;
				start = end;
				end = (remainingSize >= batchSize) ? end + batchSize : end + remainingSize;
			}
			start = 0;
	    }
        //get ProductFeatures
		Stream<AuditRecord> pfRecords;
	    if(CollectionUtils.isNotEmpty(prodFeaturePKList)) {
			int remainingSize = prodFeaturePKList.size();
			end = (remainingSize >= batchSize) ? batchSize : remainingSize;
			int iterations = remainingSize / batchSize;
			iterations = iterations + (remainingSize % batchSize) == 0 ? 0 : 1;
			for (int i = 0; i < iterations; i++)
			{
				auditSearchQuery = AuditSearchQuery.forType(GPProductAuditRecord.PRODUCT_FEATURE)
						.withPkSearchRules(prodFeaturePKList.subList(start, end).toArray(new PK[0])).build();
				pfRecords = readAuditGateway.search(auditSearchQuery);
				final List<AuditRecord> pfRecordsList = pfRecords.sorted(Comparator.comparing(AuditRecord::getTimestamp))
						.collect(Collectors.toList());
				saveAttributeChangesToGPProductAuditMap(pfRecordsList, gpProductAuditRecordMap);
				remainingSize = remainingSize - batchSize;
				start = end;
				end = (remainingSize >= batchSize) ? end + batchSize : end + remainingSize;
			}
	    }

	    final Stream<AuditRecord> srRecords;
	    if(CollectionUtils.isNotEmpty(shippingRestrictionPKList)) {
	        auditSearchQuery = AuditSearchQuery.forType(GPProductAuditRecord.SHIPPING_RESTRICTION)
	                .withPkSearchRules(shippingRestrictionPKList.toArray(new PK[0])).build();
	        srRecords = readAuditGateway.search(auditSearchQuery);
	        final List<AuditRecord> srRecordsList = srRecords.sorted(Comparator.comparing(AuditRecord::getTimestamp))
	                .collect(Collectors.toList());
	        saveAttributeChangesToGPProductAuditMap(srRecordsList, gpProductAuditRecordMap);
	    }

	    final Stream<AuditRecord> cpRecords;
	    if(CollectionUtils.isNotEmpty(competitor2ProductRelPKList)) {
	        auditSearchQuery = AuditSearchQuery.forType(GPProductAuditRecord.PRODUCT_2_B2BUNIT_REL)
	                .withPkSearchRules(competitor2ProductRelPKList.toArray(new PK[0])).build();
	        cpRecords = readAuditGateway.search(auditSearchQuery);
	        final List<AuditRecord> cpRecordsList = cpRecords.sorted(Comparator.comparing(AuditRecord::getTimestamp))
	                .collect(Collectors.toList());
	        saveAttributeChangesToGPProductAuditMap(cpRecordsList, gpProductAuditRecordMap);
	    }

	    final Stream<AuditRecord> addressRecords;
	    if(CollectionUtils.isNotEmpty(competitor2ProductRelPKList)) {
	        auditSearchQuery = AuditSearchQuery.forType(GPProductAuditRecord.GP_COMPETITOR_2_ADDRESS_REL)
	                .withPkSearchRules(competitor2ProductRelPKList.toArray(new PK[0])).build();
	        addressRecords = readAuditGateway.search(auditSearchQuery);
	        final List<AuditRecord> addressRecordsList = addressRecords.sorted(Comparator.comparing(AuditRecord::getTimestamp))
	                .collect(Collectors.toList());
	        saveAttributeChangesToGPProductAuditMap(addressRecordsList, gpProductAuditRecordMap);
	    }

	        final Set<String> keys = gpProductAuditRecordMap.keySet();
        for (final String key : keys) {
            gpProductAuditRecordMap.get(key).print();

        }

        gpProductService.updateProductAlertHistory(gpProductAuditRecordMap);

    }

    private void saveAttributeChangesToGPProductAuditMap(final List<AuditRecord> auditRecordList,
                                                         final HashMap<String, GPProductAuditRecord> gpProductAuditRecordMap) {

        //need to sort by the timemodified value in the afterSaveMap asc
        Collections.sort(auditRecordList, new Comparator<AuditRecord>() {
            @Override
            public int compare(final AuditRecord o1, final AuditRecord o2) {
                Date o1Date = null;
                Date o2Date = null;

                if (o1.getAuditType().equals(AuditType.CREATION) ||
                        o1.getAuditType().equals(AuditType.MODIFICATION)) {
                    o1Date = (Date) o1.getAttributeAfterOperation(GPProductAuditRecord.MODIFICATION_TIME);
                } else {
                    o1Date = (Date) o1.getAttributeBeforeOperation(GPProductAuditRecord.MODIFICATION_TIME);
                }

                if (o2.getAuditType().equals(AuditType.CREATION) ||
                        o2.getAuditType().equals(AuditType.MODIFICATION)) {
                    o2Date = (Date) o2.getAttributeAfterOperation(GPProductAuditRecord.MODIFICATION_TIME);
                } else {
                    o2Date = (Date) o2.getAttributeBeforeOperation(GPProductAuditRecord.MODIFICATION_TIME);
                }

                return o1Date.compareTo(o2Date);

            }
        });

        for (final AuditRecord auditRecord : auditRecordList) {

            if ((!AuditType.DELETION.equals(auditRecord.getAuditType()))
            		&& (nonProductTypes.contains(auditRecord.getType()) || !isOnlineCatalogRecord(auditRecord))) {

                final GPProductAuditRecord gpProductAuditRecord = createOrGetGPProductAuditRecord(auditRecord,
                        gpProductAuditRecordMap);
                if (gpProductAuditRecord != null) {
                    copyJsonAttributesToGPAuditRecord(gpProductAuditRecord, (JsonAuditRecord)auditRecord);
                } else {
					LOG.debug("No GPProductAuditRecord was created!");
                }
            }

        }
    }

	private boolean isOnlineCatalogRecord(final AuditRecord auditRecord) {
        final PK catVersionPK = (PK) auditRecord.getAttribute(GPProductAuditRecord.CATALOG_VERSION);
        boolean skipProcessing = false;
        if (catVersionPK != null) {
            final CatalogVersionModel catalogVersionModel = (CatalogVersionModel) modelService.get((catVersionPK));

            if (catalogVersionModel.getVersion().equalsIgnoreCase(GPProductAuditRecord.STAGED)) {
                skipProcessing = true;
            }
			LOG.debug("catVersion=" + catalogVersionModel.getVersion());
        }

        return skipProcessing;
    }

	/**
	 * This is an Before operation, which returns the Audit Record attributes based on the audit records provided
	 * 
	 * @param jsonAuditRecord {@link JsonAuditRecord}
	 * @return the audit record attributes
	 */
    public Map<String, Object> getAttributesBeforeOperation(final JsonAuditRecord jsonAuditRecord) {

        final AuditPayload auditPayload = jsonAuditRecord.getAuditPayload();
        return this.getAttributesFromJsonPayload(auditPayload);

    }

	/**
	 * This is an After operation, which returns the Audit Record attributes based on the audit records provided
	 * 
	 * @param jsonAuditRecord {@link JsonAuditRecord}
	 * @return the audit record attributes
	 */
    public Map<String, Object> getAttributesAfterOperation(final JsonAuditRecord jsonAuditRecord) {

        final AuditPayload auditPayload = jsonAuditRecord.getAuditPayloadAfterOperation();
        return this.getAttributesFromJsonPayload(auditPayload);

    }

    protected Map<String, Object> getAttributesFromJsonPayload(final AuditPayload auditPayload) {
        final Map<String, Object> attributes = new ConcurrentHashMap();
        final Iterator payLoadIt = auditPayload.getAttributes().entrySet().iterator();

        while (payLoadIt.hasNext()) {
            final Map.Entry<String, TypedValue> entry = (Map.Entry) payLoadIt.next();
            final String key = entry.getKey();
            try {
            	final Object attribute = GPJsonAuditUtils.getAttribute(key, auditPayload, getPayloadConverterRegistry());
            	attributes.put(key, attribute);
            } catch(Exception e) {
            	LOG.error("Error in getAttributesFromJsonPayload: key = "+key+" and auditPayload is: "+auditPayload, e);
            }
        }

        return attributes;
    }

    private void copyJsonAttributesToGPAuditRecord(final GPProductAuditRecord gpProductAuditRecord,
                                                   final JsonAuditRecord auditRecord) {

        final Map<String, Object> beforeMap = getAttributesBeforeOperation(auditRecord);
        final Map<String, Object> enBeforeMap = auditRecord.getAttributesBeforeOperation(GPProductAuditRecord.LOCAL_EN);
        beforeMap.putAll(enBeforeMap);

        final Map<String, Object> afterMap = getAttributesAfterOperation(auditRecord);
        final Map<String, Object> enAfterMap = auditRecord.getAttributesAfterOperation(GPProductAuditRecord.LOCAL_EN);
        afterMap.putAll(enAfterMap);

        final AuditType auditType = auditRecord.getAuditType();


        Set<String> kSet = beforeMap.keySet();
        if (afterMap.keySet().size()>= beforeMap.keySet().size()) {
            kSet = afterMap.keySet();
        }

        final String typeCode = auditRecord.getType();
        if(typeCode.equalsIgnoreCase(GPProductAuditRecord.PRODUCT_FEATURE)) {
        	kSet.removeAll(productFeatureBlackList);
		}
		else if (typeCode.equalsIgnoreCase(GPProductAuditRecord.PRODUCT))
		{
			kSet.removeAll(productBlackList);
		}

        for (final String key : kSet) {

            final Object beforeVal = beforeMap.get(key);
            final Object afterVal = afterMap.get(key);

			LOG.debug(auditRecord.getPk() + ":" + auditRecord.getType() + "::"
                    + auditRecord.getAuditType() + ":" + sdf.format(auditRecord.getTimestamp())
                    + "--" + key + " was:" + beforeVal + " now is:" + afterVal);

            final GPProductAuditAttribute gpProductAuditAttribute = getOrCreateGPProductAuditRecord(auditRecord, gpProductAuditRecord, key);
            if (gpProductAuditAttribute != null) {
                if (auditType == AuditType.CREATION) {
                    gpProductAuditAttribute.setAttributes(gpProductAuditAttribute.getOldAttributeValue(), afterVal);
                } else {
                    gpProductAuditAttribute.setAttributes(beforeVal, afterVal);
                }
            } else {
				LOG.debug("gpProductAuditAttribute is null for key=" + key);
            }


        }
    }


    private GPProductAuditAttribute getOrCreateGPProductAuditRecord(final AuditRecord auditRecord,
                                                                    final GPProductAuditRecord gpProductAuditRecord, final String key) {

        final Map<String, GPProductAuditAttribute> productValMap = gpProductAuditRecord.getProductAttributeMap();
        final Map<String, Map<String, GPProductAuditAttribute>> productFeatureValMap = gpProductAuditRecord.getProductFeatureAttributeMap();
        final Map<String, GPProductAuditAttribute> shippingRestrictionValMap = gpProductAuditRecord.getShippingRestrictionAttributeMap();

        GPProductAuditAttribute gpProductAuditAttribute = null;
        if (getAuditRecordType(auditRecord).equals(GPProductAuditRecord.PRODUCT)) {
            gpProductAuditAttribute = productValMap.get(key);
            if (gpProductAuditAttribute == null) {
                gpProductAuditAttribute = new GPProductAuditAttribute();
                gpProductAuditAttribute.setAttributeName(key);
                productValMap.put(key, gpProductAuditAttribute);
            }

        } else if (getAuditRecordType(auditRecord).equals(GPProductAuditRecord.PRODUCT_FEATURE)) {

            String qualifier = (String) auditRecord.getAttributeBeforeOperation(GPProductAuditRecord.QUALIFIER);
            if (qualifier == null) {
                //on creation the b4 value is null, on delete the afterval is null.
                qualifier = (String) auditRecord.getAttributeAfterOperation(GPProductAuditRecord.QUALIFIER);
            }
            Map<String, GPProductAuditAttribute> gpProductAuditAttributeMap = gpProductAuditRecord.productFeatureAttributeMap
                    .get(qualifier);

            if (gpProductAuditAttributeMap == null) {
                LOG.info("creating new gpPrdAttrMap for " + qualifier);
                gpProductAuditAttributeMap = new HashMap<>();
                productFeatureValMap.put(qualifier, gpProductAuditAttributeMap);

            }

            //look for attribute
            gpProductAuditAttribute = gpProductAuditAttributeMap.get(key);
            if (gpProductAuditAttribute == null) {
                LOG.info("creating new gpPrdAttr for " + key);
                gpProductAuditAttribute = new GPProductAuditAttribute();
                gpProductAuditAttribute.setAttributeName(key);
                gpProductAuditAttributeMap.put(key, gpProductAuditAttribute);

            }
        } else if(getAuditRecordType(auditRecord).equals(GPProductAuditRecord.SHIPPING_RESTRICTION)) {
            gpProductAuditAttribute = shippingRestrictionValMap.get(key);
            if (gpProductAuditAttribute == null) {
                gpProductAuditAttribute = new GPProductAuditAttribute();
                gpProductAuditAttribute.setAttributeName(key);
                shippingRestrictionValMap.put(key, gpProductAuditAttribute);
            }
        }
        return gpProductAuditAttribute;
    }

	/**
	 * Fetches the type of auditRecord based on the auditRecord provided
	 * 
	 * @param auditRecord the {@link AuditRecord}
	 * @return the audit record type {@link GPProductAuditRecord}
	 */
    public static String getAuditRecordType(final AuditRecord auditRecord) {
        final String type = auditRecord.getType();
        if (type.equalsIgnoreCase(GPProductAuditRecord.PRODUCT) ||
                type.equalsIgnoreCase(GPProductAuditRecord.GP_PRODUCT)) {
            return GPProductAuditRecord.PRODUCT;
        }
        return type;
    }

    private GPProductAuditRecord createOrGetGPProductAuditRecord(final AuditRecord auditRecord,
                                                                 final Map<String, GPProductAuditRecord> gpProductAuditRecordMap) {


        validateParameterNotNullStandardMessage("auditRecord", auditRecord);
        validateParameterNotNullStandardMessage("gpProductAuditRecordMap", gpProductAuditRecordMap);

        final String auditType = auditRecord.getType();
        GPProductAuditRecord gpProductAuditRecord = null;
        GPProductAuditRecord retRecord = null;
        ProductModel productModel = null;
        String productCode = "";
        if(auditType.equalsIgnoreCase(GPProductAuditRecord.SHIPPING_RESTRICTION)) {
        	final PK srPK = (PK) auditRecord.getAttribute("pk");
        	ShippingRestrictionModel srModel = null;
        	try {
        	srModel = modelService.get(srPK);
        	productCode = srModel.getProductCode();
        	}catch(final Exception e) {
        		LOG.error("could not get model for Shipping restriction PK: "+srPK, e);
        		return null;
        	}

		} else if(nonProductTypes.contains(auditType)) {
			final PK sourcePK = (PK) auditRecord.getAttribute("source");
			try {
				productModel = modelService.get(sourcePK);
				productCode = productModel.getCode();
			}catch(final Exception e) {
				LOG.error("could not get model for Relation PK: " + sourcePK, e);
				return null;
			}
		}
		else
		{
			PK productPK = null;
			if (auditRecord.getAttribute("product") == null)
			{
				productPK = (PK) auditRecord.getAttribute("pk");
			}
			else
			{
				productPK = (PK) auditRecord.getAttribute("product");
			}
			try {
			productModel = modelService.get(productPK);
			productCode = productModel.getCode();
			}catch(final Exception e) {
				LOG.error("could not get model for Product PK: "+productPK, e);
        		return null;
			}
		}


        gpProductAuditRecord = gpProductAuditRecordMap.get(productCode);
        //set the record to return
        retRecord = gpProductAuditRecord;

        if (gpProductAuditRecord == null) {

            gpProductAuditRecord = new GPProductAuditRecord();
            gpProductAuditRecord.setProduct(productModel);
            if(productModel!=null) {
            	gpProductAuditRecord.setAssociatedDistributors(productModel.getDistributorIds());
            	if(productModel instanceof GPCompetitorProductModel) {
            		final GPCompetitorProductModel competitorProduct = (GPCompetitorProductModel)productModel;
            		gpProductAuditRecord.setAssociatedAddresses(competitorProduct.getCompetitorProductAddresses());
            		gpProductAuditRecord.setAssociatedProducts(competitorProduct.getProduct());
            	}

            }

            retRecord = gpProductAuditRecord;
        }


        LOG.info("found gpProduct Rec:" + productCode + ":" + gpProductAuditRecord);
        gpProductAuditRecordMap.put(productCode, gpProductAuditRecord);

        LOG.info("returning " + retRecord + " of type " + retRecord.getClass());
        return retRecord;
    }

	/**
	 * Fetches PK's based on the Timestamps provided
	 * 
	 * @param typeCode  the type code used
	 * @param startDate the date
	 * @return List of {@link PK}'s
	 */
    public List<PK> getPKsByTimestamp(final String typeCode, final Date startDate) {
        final String query = MessageFormat.format("SELECT * FROM {0} where timestamp  >=?",
                AuditStorageUtils.getAuditTableName(typeCode));
        return jdbcTemplate.query(query,
                new Object[]{startDate}, new ResultSetExtractor<List<PK>>() {
                    @Override
                    public List<PK> extractData(final ResultSet rs) throws SQLException,
                            DataAccessException {

                        final List<PK> list = new ArrayList();
                        while (rs.next()) {

                            list.add(toPK(rs.getString(2)));
                        }
                        return list;
                    }
                });
    }

    protected PK toPK(final Object pk) {
        if (pk instanceof Long) {
            return PK.fromLong((Long) pk);
        } else {
            return pk instanceof String ? PK.parse((String) pk) : null;
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public CatalogService getCatalogService() {
        return catalogService;
    }

    public void setCatalogService(final CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(final ModelService modelService) {
        this.modelService = modelService;
    }

    public ReadAuditGateway getReadAuditGateway() {
        return readAuditGateway;
    }

    public void setReadAuditGateway(final ReadAuditGateway readAuditGateway) {
        this.readAuditGateway = readAuditGateway;
    }

    public WriteAuditGateway getWriteAuditGateway() {
        return writeAuditGateway;
    }

    public void setWriteAuditGateway(final WriteAuditGateway writeAuditGateway) {
        this.writeAuditGateway = writeAuditGateway;
    }

    public PayloadConverterRegistry getPayloadConverterRegistry() {
        return payloadConverterRegistry;
    }

    public void setPayloadConverterRegistry(final PayloadConverterRegistry payloadConverterRegistry) {
        this.payloadConverterRegistry = payloadConverterRegistry;
    }

	public GPProductService getGpProductService() {
		return gpProductService;
	}

	public void setGpProductService(final GPProductService gpProductService) {
		this.gpProductService = gpProductService;
	}
}
