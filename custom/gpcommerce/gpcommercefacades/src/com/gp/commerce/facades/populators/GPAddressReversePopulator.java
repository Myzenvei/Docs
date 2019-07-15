/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import com.gp.commerce.core.enums.GPApprovalEnum;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.commercefacades.user.data.AddressData;

public class GPAddressReversePopulator extends AddressReversePopulator {


	private EnumerationService enumerationService;
	private B2BUnitService b2bUnitService;
	private ConfigurationService configurationService;
	private UserService userService;

	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	public B2BUnitService getB2bUnitService() {
		return b2bUnitService;
	}

	public void setB2bUnitService(B2BUnitService b2bUnitService) {
		this.b2bUnitService = b2bUnitService;
	}

	public EnumerationService getEnumerationService() {
		return enumerationService;
	}

	public void setEnumerationService(EnumerationService enumerationService) {
		this.enumerationService = enumerationService;
	}
	
	@Override
	public void populate(final AddressData source, final AddressModel target)
	{
		super.populate(source, target);
		
		UserModel user = userService.getCurrentUser();
		target.setEmail(source.getEmail());
		
		if(user instanceof B2BCustomerModel){
			if(source.getPalletShipment()!=null){
				target.setPalletShipment(source.getPalletShipment());
			}
			B2BUnitModel b2bUnit=null;
			if(source.getUnit()!=null && source.getUnit().getUid()!=null ){
				b2bUnit=(B2BUnitModel) b2bUnitService.getUnitForUid(source.getUnit().getUid());
				target.setB2bUnit(b2bUnit);
			}
	
				if(source.getApprovalStatus()!=null){
				final GPApprovalEnum status = (GPApprovalEnum) getEnumerationService().getEnumerationValue(
					GPApprovalEnum.class.getSimpleName(), source.getApprovalStatus());
				target.setApprovalStatus(status);
			}
		
		}
	}


}
