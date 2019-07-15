/*
 * Copyright (c) 2019. Georgia-Pacific.  All rights reserved.
 * This software is the confidential and proprietary information of Georgia-Pacific.
 *
 *
 */

package com.gp.commerce.core.catalog;

import com.gp.commerce.core.jalo.ShippingRestriction;

import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.AddressModel;

import org.apache.log4j.Logger;

import java.util.*;

public class GPProductAuditRecord {

    final static String PRODUCT_FEATURE="ProductFeature";
    final static String PRODUCT="Product";
    final static String GP_PRODUCT="GPCommerceProduct";
    final static String QUALIFIER="qualifier";
    final static String RAW_VALUE="rawvalue";
    final static String STRING_VALUE="stringvalue";
    final static String BOOLEAN_VALUE="booleanvalue";
    final static String LOCAL_EN="en";
    final static String STAGED="Staged";
    final static String MODIFICATION_TIME="modifiedtime";
    final static String CATALOG_VERSION="catalogversion";
    final static String SHIPPING_RESTRICTION ="ShippingRestriction";
    final static String PRODUCT_CODE="productCode";
    final static String GP_COMPETITOR_2_PRODUCT_REL="GPCompProd2Product";
    final static String GP_COMPETITOR_2_ADDRESS_REL="GPCompProd2Address";
    final static String PRODUCT_2_B2BUNIT_REL = "Product2B2BUnitRel";

    ProductModel product;
    String name;
    Map<String,GPProductAuditAttribute> productAttributeMap = new HashMap<>();
    Map<String,Map<String,GPProductAuditAttribute>> productFeatureAttributeMap = new HashMap<>();
    Map<String,GPProductAuditAttribute> shippingRestrictionAttributeMap = new HashMap<>();
    Map<String,GPProductAuditAttribute> competitorProductAttributeMap = new HashMap<>();
    Collection<AddressModel> associatedAddresses = new ArrayList<>();
    Collection<ProductModel> associatedProducts =new ArrayList<>();
    Collection<B2BUnitModel> associatedDistributors = new ArrayList<>();
    
    HashSet<String> productFeatureBlackList = new HashSet<>();
    HashSet<String> productBlackList = new HashSet<>();


    private static final Logger LOG = Logger.getLogger(GPProductAuditRecord.class);


    public void print(){
        LOG.debug("***********************START FOR Product "+product+"******************************");
        Set<String> keys = productAttributeMap.keySet();
        for(String key : keys){
            GPProductAuditAttribute gpProductAuditAttribute = productAttributeMap.get(key);
            LOG.debug("attr:" + gpProductAuditAttribute.getAttributeName()+":"+
                    "old:" + gpProductAuditAttribute.getOldAttributeValue()+":"+
                    "new:" + gpProductAuditAttribute.getNewAttributeValue());
            if(gpProductAuditAttribute.isChanged()){
                LOG.debug("%%%%%%%%%%%%%%%%%%%%%%%%%attribute has CHANGED"+"%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            }
        }
        LOG.debug("+++++++++++++++++++++++++Product Feature Attributes+++++++++++++++++++++++++++++++++");
        keys = productFeatureAttributeMap.keySet();
        for(String key : keys){
            Map<String,GPProductAuditAttribute> gpProductFeatureMap = productFeatureAttributeMap.get(key);

                GPProductAuditAttribute gpProductAuditAttribute = gpProductFeatureMap.get(GPProductAuditRecord.QUALIFIER);
                LOG.debug("NAME:" + gpProductAuditAttribute.getNewAttributeValue());
            gpProductAuditAttribute = gpProductFeatureMap.get(GPProductAuditRecord.RAW_VALUE);
            LOG.debug("attr:" + gpProductAuditAttribute.getAttributeName()+":"+
                    "\told:" + gpProductAuditAttribute.getOldAttributeValue()+":"+
                    "\tnew:" + gpProductAuditAttribute.getNewAttributeValue());
            gpProductAuditAttribute = gpProductFeatureMap.get(GPProductAuditRecord.STRING_VALUE);
            LOG.debug("attr:" + gpProductAuditAttribute.getAttributeName()+":"+
                    "\told:" + gpProductAuditAttribute.getOldAttributeValue()+":"+
                    "\tnew:" + gpProductAuditAttribute.getNewAttributeValue());
            gpProductAuditAttribute = gpProductFeatureMap.get(GPProductAuditRecord.BOOLEAN_VALUE);
            LOG.debug("attr:" + gpProductAuditAttribute.getAttributeName()+":"+
                    "\told:" + gpProductAuditAttribute.getOldAttributeValue()+":"+
                    "\tnew:" + gpProductAuditAttribute.getNewAttributeValue());
            gpProductAuditAttribute = gpProductFeatureMap.get(GPProductAuditRecord.RAW_VALUE);


            if(gpProductAuditAttribute.isChanged()){
                LOG.debug("%%%%%%%%%%%%%%%%%%%%%%%%%attribute has CHANGED"+"%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            }
            LOG.debug("\n");


        }


        LOG.debug("***********************END FOR Product "+ product+"******************************");
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public Map<String, GPProductAuditAttribute> getProductAttributeMap() {
        return productAttributeMap;
    }

    public void setProductAttributeMap(Map<String, GPProductAuditAttribute> productAttributeMap) {
        this.productAttributeMap = productAttributeMap;
    }

    public Map<String, Map<String,GPProductAuditAttribute>> getProductFeatureAttributeMap() {
        return productFeatureAttributeMap;
    }

    public void setProductFeatureAttributeMap(Map<String, Map<String,GPProductAuditAttribute>> productFeatureAttributeMap) {
        this.productFeatureAttributeMap = productFeatureAttributeMap;
    }



	public Map<String, GPProductAuditAttribute> getShippingRestrictionAttributeMap() {
		return shippingRestrictionAttributeMap;
	}



	public void setShippingRestrictionAttributeMap(Map<String, GPProductAuditAttribute> shippingRestrictionAttributeMap) {
		this.shippingRestrictionAttributeMap = shippingRestrictionAttributeMap;
	}



	public Map<String, GPProductAuditAttribute> getCompetitorProductAttributeMap() {
		return competitorProductAttributeMap;
	}



	public void setCompetitorProductAttributeMap(Map<String, GPProductAuditAttribute> competitorProductAttributeMap) {
		this.competitorProductAttributeMap = competitorProductAttributeMap;
	}

	public void setAssociatedDistributors(Collection<B2BUnitModel> associatedDistributors) {
		this.associatedDistributors = associatedDistributors;
	}



	public Collection<AddressModel> getAssociatedAddresses() {
		return associatedAddresses;
	}



	public void setAssociatedAddresses(Collection<AddressModel> associatedAddresses) {
		this.associatedAddresses = associatedAddresses;
	}



	public Collection<ProductModel> getAssociatedProducts() {
		return associatedProducts;
	}




	public void setAssociatedProducts(Collection<ProductModel> associatedProducts) {
		this.associatedProducts = associatedProducts;
	}



	public Collection<B2BUnitModel> getAssociatedDistributors() {
		return associatedDistributors;
	}



	public HashSet<String> getProductFeatureBlackList() {
		return productFeatureBlackList;
	}



	public void setProductFeatureBlackList(HashSet<String> productFeatureBlackList) {
		this.productFeatureBlackList = productFeatureBlackList;
	}



	public HashSet<String> getProductBlackList() {
		return productBlackList;
	}



	public void setProductBlackList(HashSet<String> productBlackList) {
		this.productBlackList = productBlackList;
	}
}
