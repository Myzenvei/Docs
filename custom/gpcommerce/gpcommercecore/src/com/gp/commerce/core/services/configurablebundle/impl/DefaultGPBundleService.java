/*
 * Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *
 * This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.configurablebundle.impl;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

import com.gp.commerce.core.dao.GPBundleDao;
import de.hybris.platform.core.model.order.CartEntryModel;
import org.apache.commons.collections4.CollectionUtils;

import com.gp.commerce.core.constants.GpErrorConstants;
import com.gp.commerce.core.exceptions.GPBundleSetupException;
import com.gp.commerce.core.services.configurablebundle.GPBundleService;

import de.hybris.platform.configurablebundleservices.bundle.BundleTemplateService;
import de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel;
import de.hybris.platform.configurablebundleservices.model.BundleTemplateModel;
import org.apache.log4j.Logger;

/**
 * This class is used for GP Bundle service
 */
public class DefaultGPBundleService implements GPBundleService {

    private static final String BUNDLE_ID_MSG = " for bundle: ";

    @Resource (name = "bundleTemplateService")
    private BundleTemplateService bundleTemplateService;

    @Resource
    private GPBundleDao gpBundleDao;

	private static final Logger LOG=Logger.getLogger(DefaultGPBundleService.class);

    @Override
    public Boolean validateBundleSetup(@Nonnull final String bundleId) throws GPBundleSetupException {

        BundleTemplateModel rootBundleTemplate = getRootBundleTemplate(bundleId);
        if (rootBundleTemplate == null) {
            throw new GPBundleSetupException(GpErrorConstants.GPBUNDLE_NO_ROOT_BUNDLE_CODE, GpErrorConstants.GPBUNDLE_NO_ROOT_BUNDLE_MSG + BUNDLE_ID_MSG + bundleId);
        }
        List<BundleTemplateModel> childBundleTemplateList = rootBundleTemplate.getChildTemplates();
        if(CollectionUtils.isEmpty(childBundleTemplateList)) {
            throw new GPBundleSetupException(GpErrorConstants.GPBUNDLE_NO_CHILD_BUNDLE_CODE, GpErrorConstants.GPBUNDLE_NO_CHILD_BUNDLE_MSG + BUNDLE_ID_MSG + bundleId);
        }

        validateChildBundleSetup(childBundleTemplateList,bundleId);

        return Boolean.TRUE;

    }

    @Override
    public BundleTemplateModel getRootBundleTemplate(@Nonnull String bundleId) throws GPBundleSetupException {
        BundleTemplateModel bundleTemplate = bundleTemplateService.getBundleTemplateForCode(bundleId);
        return bundleTemplateService.getRootBundleTemplate(bundleTemplate);
    }

    protected Boolean validateChildBundleSetup(List<BundleTemplateModel> childBundleTemplateList, String bundleId) throws GPBundleSetupException {

    	if(childBundleTemplateList != null) {
    		for (BundleTemplateModel childBundleTemplate : childBundleTemplateList) {
    			if(childBundleTemplate.getChildTemplates() != null) {
    				 for(BundleTemplateModel nestedChildBundleTemplate : childBundleTemplate.getChildTemplates()) {
    	                    if (nestedChildBundleTemplate != null) {
    	                        throw new GPBundleSetupException(GpErrorConstants.GPBUNDLE_GRANDCHILD_BUNDLE_CODE, GpErrorConstants.GPBUNDLE_GRANDCHILD_BUNDLE_MSG + BUNDLE_ID_MSG + bundleId);
    	                    }
    	                }
    			}
               
                if (childBundleTemplate.getProducts() == null) {
                    throw new GPBundleSetupException(GpErrorConstants.GPBUNDLE_GRANDCHILD_NOPRODUCT_CODE, GpErrorConstants.GPBUNDLE_GRANDCHILD_NOPRODUCT_MSG + BUNDLE_ID_MSG + bundleId);
                }
                BundleSelectionCriteriaModel bundleSelectionCriteria = childBundleTemplate.getBundleSelectionCriteria();
                if(bundleSelectionCriteria == null) {
                    throw new GPBundleSetupException(GpErrorConstants.GPBUNDLE_GRANDCHILD_NOSELECTCRITERIA_CODE, GpErrorConstants.GPBUNDLE_GRANDCHILD_NOSELECTCRITERIA_MSG + BUNDLE_ID_MSG + bundleId);
                }
            }
    		return Boolean.TRUE;
    	} else {
			return Boolean.FALSE;
		}
    	
        
    }

    @Override
    public List<CartEntryModel> getOrderEntriesByBundleNum(int bundleNo, String cartId) {

        List<CartEntryModel> orderEntryModels= gpBundleDao.getOrderEntriesByBundleNo(bundleNo,cartId);

        if(orderEntryModels==null){

            LOG.error("No order entries found with bundle number "+bundleNo+" in cart "+cartId);
        }

        return orderEntryModels;
    }
    
    
    public BundleTemplateService getBundleTemplateService() {
        return bundleTemplateService;
    }

    public void setBundleTemplateService(BundleTemplateService bundleTemplateService) {
        this.bundleTemplateService = bundleTemplateService;
    }

}


