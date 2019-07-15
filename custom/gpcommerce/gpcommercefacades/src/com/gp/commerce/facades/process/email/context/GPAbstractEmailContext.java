/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.process.email.context;

import static de.hybris.platform.multicountry.constants.MulticountryConstants.AVAILABILITY_GROUPS;

import javax.annotation.Resource;

import org.apache.commons.lang.WordUtils;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.NumberTool;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.model.ShareProductEmailProcessModel;
import com.gp.commerce.core.services.GPCMSSiteService;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.store.services.BaseStoreService;

public class GPAbstractEmailContext<T extends BusinessProcessModel> extends AbstractEmailContext<T> {
	
	private static final String DEFAULT = "DEFAULT";

	private static final String EUROPE1_PRICE_FACTORY_UPG = "Europe1PriceFactory_UPG";

	private static final String SOLD_TO_UNIT_ID = "soldToUnitId";

	private static final String SOLD_TO_ROOTCATEGORY = "soldToRootcategory";
	public static final String NUMBER_TOOL = "numberTool";
	
	private String customerNumber;
	
	@Resource(name = "cmsSiteService")
	private GPCMSSiteService cmsSiteService;
	
	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;
	

	public String getCustomerNumber() {
		customerNumber = cmsSiteService.getSiteConfig(GpcommerceCoreConstants.CONTACT_NUMBER);
		return customerNumber;
	}

	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}

	@Override
	protected BaseSiteModel getSite(T businessProcessModel) {
		return null;
	}

	@Override
	protected CustomerModel getCustomer(T businessProcessModel) {
		return null;
	}

	@Override
	protected LanguageModel getEmailLanguage(T businessProcessModel) {
		return null;
	}
	
	@Override
	public String getDisplayName() {
		return WordUtils.capitalizeFully((String) get(AbstractEmailContext.DISPLAY_NAME));
	}

	/**
	 * @return the cmsSiteService
	 */
	public GPCMSSiteService getCmsSiteService() {
		return cmsSiteService;
	}

	/**
	 * @param cmsSiteService the cmsSiteService to set
	 */
	public void setCmsSiteService(GPCMSSiteService cmsSiteService) {
		this.cmsSiteService = cmsSiteService;
	}

	/**
	 * @return the sessionService
	 */
	public SessionService getSessionService() {
		return sessionService;
	}

	/**
	 * @param sessionService the sessionService to set
	 */
	public void setSessionService(SessionService sessionService) {
		this.sessionService = sessionService;
	}

	/**
	 * @return the baseStoreService
	 */
	public BaseStoreService getBaseStoreService() {
		return baseStoreService;
	}

	/**
	 * @param baseStoreService the baseStoreService to set
	 */
	public void setBaseStoreService(BaseStoreService baseStoreService) {
		this.baseStoreService = baseStoreService;
	}

	@Override
	public void init(final T businessProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(businessProcessModel, emailPageModel);
		put(NUMBER_TOOL, new NumberTool());
		sessionService.getCurrentSession().setAttribute(AVAILABILITY_GROUPS, baseStoreService.getCurrentBaseStore().getAvailabilityGroups());
		sessionService.getCurrentSession().setAttribute(SOLD_TO_ROOTCATEGORY, DEFAULT);
		sessionService.getCurrentSession().setAttribute(SOLD_TO_UNIT_ID, DEFAULT);
		sessionService.getCurrentSession().setAttribute(EUROPE1_PRICE_FACTORY_UPG, baseStoreService.getCurrentBaseStore().getUserPriceGroup());
	}

	
}