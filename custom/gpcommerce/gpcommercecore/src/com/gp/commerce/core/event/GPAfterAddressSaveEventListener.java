/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;


import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;

import java.util.Collection;

import javax.annotation.Resource;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.core.services.event.GPEmailItemEvent;

public class GPAfterAddressSaveEventListener implements AfterSaveListener
{
	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "baseSiteService")
	private BaseSiteService baseSiteService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	private ConfigurationService configurationService;

	public GPUserService getUserService() {
		return userService;
	}

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public void setUserService(final GPUserService userService) {
		this.userService = userService;
	}
	@Resource(name = "userService")
	private GPUserService userService;
	@Resource(name = "eventService")
	private EventService eventService;
	@Resource(name = "modelService")
	private ModelService modelService;

	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;


	public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService() {
		return b2bUnitService;
	}

	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService) {
		this.b2bUnitService = b2bUnitService;
	}

	@Override
	public void afterSave(final Collection<AfterSaveEvent> events)
	{

		for (final AfterSaveEvent event : events)
		{
			final int type = event.getType();
			if (AfterSaveEvent.UPDATE == type)
			{
				final PK pk = event.getPk();
				if (GpcommerceCoreConstants.ADDRESS_MODEL_TYPECODE == pk.getTypeCode())
				{
					final AddressModel addressModel = (AddressModel) modelService.get(pk);

					if (null != addressModel.getOwner() && addressModel.getOwner() instanceof B2BCustomerModel)
					{
						final B2BCustomerModel customerModel = (B2BCustomerModel) addressModel.getOwner();
						if (null != customerModel.getDefaultB2BUnit().getB2bUnitLevel()
								&& !GpcommerceCoreConstants.B2B_UNIT_L1
										.equalsIgnoreCase(customerModel.getDefaultB2BUnit().getB2bUnitLevel())
								&& getUserService().isGPAdminApproved(addressModel))
						{
								final GPEmailItemEvent item = new GPEmailItemEvent();
								item.setAddress(addressModel);
								if (GpcommerceCoreConstants.APPROVED.equals(addressModel.getApprovalStatus().getCode()))
								{
									item.setEmailSubject(EmailSubjectType.ADDRESS_APPROVED.getSubject());
								}
								if (GpcommerceCoreConstants.REJECTED.equals(addressModel.getApprovalStatus().getCode()))
								{
									item.setEmailSubject(EmailSubjectType.ADDRESS_REJECTED.getSubject());
								}

								eventService.publishEvent(initializeEvent(item, customerModel));
							}
						}
					}
				}
			}
		}

	protected AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event,
			final B2BCustomerModel customerModel)
	{
		event.setBaseStore(customerModel.getSite().getStores().get(0));
		event.setSite(customerModel.getSite());
		event.setCustomer(customerModel);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		return event;
	}
}
