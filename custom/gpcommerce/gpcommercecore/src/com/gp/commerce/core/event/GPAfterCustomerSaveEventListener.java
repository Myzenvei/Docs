/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.event;


import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commerceservices.event.AbstractCommerceUserEvent;
import de.hybris.platform.commerceservices.event.ForgottenPwdEvent;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.tx.AfterSaveEvent;
import de.hybris.platform.tx.AfterSaveListener;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.gp.commerce.core.constants.GpcommerceCoreConstants;
import com.gp.commerce.core.services.GPUserService;
import com.gp.commerce.core.services.event.GPEmailItemEvent;


public class GPAfterCustomerSaveEventListener implements AfterSaveListener
{
	private static final Logger LOGGER = LogManager.getLogger(GPAfterCustomerSaveEventListener.class);
	private BaseStoreService baseStoreService;
	private BaseSiteService baseSiteService;
	private CommonI18NService commonI18NService;
	private ConfigurationService configurationService;
	private EventService eventService;
	private ModelService modelService;
	private long tokenValiditySeconds;
	private SecureTokenService secureTokenService;
	private GPUserService userService;
	private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
	private static final String BASESITE_DELIMITER = "gpcommercewebservices.user.delimiter";
	private static final String BUSINESS_USER_EMAIL = "gp.businessuser.email.id";

	public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
	{
		return b2bUnitService;
	}

	public void setB2bUnitService(final B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
	{
		this.b2bUnitService = b2bUnitService;
	}

	public ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

	public BaseStoreService getBaseStoreService()
	{
		return baseStoreService;
	}

	public void setBaseStoreService(final BaseStoreService baseStoreService)
	{
		this.baseStoreService = baseStoreService;
	}

	public BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}

	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	public GPUserService getUserService()
	{
		return userService;
	}

	public void setUserService(final GPUserService userService)
	{
		this.userService = userService;
	}

	@Override
	public void afterSave(final Collection<AfterSaveEvent> events)
	{
		for (final AfterSaveEvent event : events)
		{
			final int type = event.getType();
			final PK pk = event.getPk();
			if (GpcommerceCoreConstants.CUSTOMER_MODEL_TYPECODE == pk.getTypeCode())
			{
				if (AfterSaveEvent.CREATE == type)
				{
					LOGGER.debug("Sending email for created customer");
					final CustomerModel customerModel = modelService.get(pk);

					//Check if the customer is L3 or L2
					boolean isL2L3Unit = false;
					B2BCustomerModel b2bCustomer = null;
					if (customerModel instanceof B2BCustomerModel)
					{
						b2bCustomer = (B2BCustomerModel) customerModel;
						if (!GpcommerceCoreConstants.B2B_UNIT_L1
								.equalsIgnoreCase(((B2BCustomerModel) customerModel).getDefaultB2BUnit().getB2bUnitLevel()))
						{
							isL2L3Unit = true;
						}
						//Send email on user registration
						if (isL2L3Unit && null != b2bCustomer
								&& b2bCustomer.getUserApprovalStatus().getCode().equalsIgnoreCase(GpcommerceCoreConstants.PENDING))
						{
							final Collection<B2BCustomerModel> administrators = getB2bUnitService()
									.getUsersOfUserGroup((b2bCustomer).getDefaultB2BUnit(), B2BConstants.B2BADMINGROUP, false);

							final GPEmailItemEvent item = new GPEmailItemEvent();
							//TODO : Bcc email from properties file
							item.setBccEmail(Config.getParameter(BUSINESS_USER_EMAIL));
							item.setInvitedCustomer(b2bCustomer);
							if (CollectionUtils.isNotEmpty(administrators))
							{
								final List<B2BCustomerModel> list = new ArrayList<>(administrators);
								item.setAdminModel(list.get(0));
							}
							List<String> listEmails=new ArrayList<>();
							for (final B2BCustomerModel administrator : administrators)
							{
								listEmails.add(administrator.getEmail());
							}
								item.setToEmails(listEmails);
							item.setEmailSubject(EmailSubjectType.NEW_USER_REVIEW.getSubject());
							eventService.publishEvent(initializeEvent(item, b2bCustomer));
						}
						else
						{
							final GPEmailItemEvent item = new GPEmailItemEvent();
							item.setEmailSubject(EmailSubjectType.CUSTOMER_REGISTRATION.getSubject());
							if(b2bCustomer.getIsBackofficeCreated()){
								item.setBackOfficeUser(true);

								//Forgot Password Link
								final long timeStamp = getTokenValiditySeconds() > 0L ? new Date().getTime() : 0L;
								final SecureToken data = new SecureToken(customerModel.getUid(), timeStamp);
								final String token = getSecureTokenService().encryptData(data);
								b2bCustomer.setToken(token);
								item.setToken(token);
								eventService.publishEvent(initializeEvent(item, b2bCustomer));
							} else {
								item.setBackOfficeUser(false);
								eventService.publishEvent(initializeEvent(item, b2bCustomer));
							}
						}
					}

				}
				else if (AfterSaveEvent.UPDATE == type)
				{
					if (GpcommerceCoreConstants.CUSTOMER_MODEL_TYPECODE == pk.getTypeCode())
					{
						final CustomerModel customerModel = modelService.get(pk);
						if (customerModel instanceof B2BCustomerModel)
						{

							if (getUserService().isCustomerApproved((B2BCustomerModel) customerModel))
							{
								final B2BUnitModel b2bUnit = ((B2BCustomerModel) customerModel).getDefaultB2BUnit();
								 Collection<B2BCustomerModel> administrators = getB2bUnitService().getUsersOfUserGroup(
										b2bUnit, B2BConstants.B2BADMINGROUP, false);
								B2BUnitModel rootUnit = b2bUnitService.getRootUnit(b2bUnit);
								if(CollectionUtils.isEmpty(administrators) && CollectionUtils.isNotEmpty(b2bUnit.getGroups())){
                           administrators = getB2bUnitService().getUsersOfUserGroup(rootUnit, B2BConstants.B2BADMINGROUP, false);
								}

								if (CollectionUtils.isNotEmpty(administrators))
								{
									((B2BCustomerModel) customerModel).setActive(true);
									if (null != ((B2BCustomerModel) customerModel).getNewEmail()
											&& !((B2BCustomerModel) customerModel).getNewEmail().isEmpty())
									{
										final String uidDelimiter = configurationService.getConfiguration().getString(BASESITE_DELIMITER);
										((B2BCustomerModel) customerModel).setUid(((B2BCustomerModel) customerModel).getNewEmail() + uidDelimiter + ((B2BCustomerModel)customerModel).getSite().getUid());
										((B2BCustomerModel) customerModel).setEmail(((B2BCustomerModel) customerModel).getNewEmail());
										((B2BCustomerModel) customerModel).setNewEmail(null);
										getModelService().save(customerModel);
									}

									final GPEmailItemEvent item = new GPEmailItemEvent();
									item.setInvitedCustomer(customerModel);
									item.setEmailSubject(EmailSubjectType.L3_ADMIN_APPROVED.getSubject());
									final List<B2BCustomerModel> list = new ArrayList<>(administrators);
									List<String> listEmails=new ArrayList<>();
									for (final B2BCustomerModel administrator : list)
									{
										listEmails.add(administrator.getEmail());
									}
										item.setToEmails(listEmails);
									eventService.publishEvent(initializeEvent(item, list.get(0)));

									//YCOM-8446 : Customer Registration Email for Newly Approved Buyers/Admins
									if (!GpcommerceCoreConstants.B2B_UNIT_L1
											.equalsIgnoreCase(((B2BCustomerModel) customerModel).getDefaultB2BUnit().getB2bUnitLevel()))
									{
										final B2BCustomerModel b2bCustomerModel = (B2BCustomerModel) customerModel;

										LOGGER.info("Sending email for Approved customer");

										final GPEmailItemEvent itemEvent = new GPEmailItemEvent();
										itemEvent.setEmailSubject(EmailSubjectType.CUSTOMER_REGISTRATION.getSubject());
										itemEvent.setBackOfficeUser(true);
										itemEvent.setToken(b2bCustomerModel.getToken());
										eventService.publishEvent(initializeEvent(itemEvent, b2bCustomerModel));

									}

								}

							}
						}
					}
				}
				else if (AfterSaveEvent.REMOVE == type)
				{
					LOGGER.debug("Remove Method is called");
				}
			}
		}
     }
	public void sendForgotPasswordEvent(final CustomerModel customerModel)
	{
		//Reset Password Link
		final long timeStamp = getTokenValiditySeconds() > 0L ? new Date().getTime() : 0L;
		final SecureToken data = new SecureToken(customerModel.getUid(), timeStamp);
		final String token = getSecureTokenService().encryptData(data);
		customerModel.setToken(token);
		getEventService().publishEvent(initializeEvent(new ForgottenPwdEvent(token), customerModel));
	}

	protected ModelService getModelService()
	{
		return modelService;
	}

	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected SecureTokenService getSecureTokenService()
	{
		return secureTokenService;
	}

	@Required
	public void setSecureTokenService(final SecureTokenService secureTokenService)
	{
		this.secureTokenService = secureTokenService;
	}

	protected long getTokenValiditySeconds()
	{
		return tokenValiditySeconds;
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	@Required
	public void setTokenValiditySeconds(final long tokenValiditySeconds)
	{
		if (tokenValiditySeconds < 0)
		{
			throw new IllegalArgumentException("tokenValiditySeconds has to be >= 0");
		}
		this.tokenValiditySeconds = tokenValiditySeconds;
	}

	private AbstractCommerceUserEvent initializeEvent(final AbstractCommerceUserEvent event, final CustomerModel customerModel)
	{
		if (!customerModel.getSite().getStores().isEmpty())
		{
			event.setBaseStore(customerModel.getSite().getStores().get(0));
		}
		event.setSite(customerModel.getSite());
		event.setCustomer(customerModel);
		event.setLanguage(commonI18NService.getCurrentLanguage());
		event.setCurrency(commonI18NService.getCurrentCurrency());
		return event;
	}

}
