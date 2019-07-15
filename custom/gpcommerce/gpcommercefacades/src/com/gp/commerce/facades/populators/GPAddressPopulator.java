/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.facades.populators;

import com.gp.commerce.core.enums.GPApprovalEnum;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2bcommercefacades.company.data.B2BUnitData;
import de.hybris.platform.commercefacades.user.converters.populator.AddressPopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;

public class GPAddressPopulator extends AddressPopulator {

	@Override
	public void populate(final AddressModel source, final AddressData target)
	{
		super.populate(source, target);
		target.setUrl(source.getUrl());
		if(null != source.getCode()) {
			target.setCode(source.getCode());
		}
		target.setCreationTime(source.getCreationtime());
		if(null != source.getB2bUnit()){
			final B2BUnitData b2BUnitData = new B2BUnitData();
			b2BUnitData.setUid(source.getB2bUnit().getUid());
			b2BUnitData.setName(source.getB2bUnit().getLocName());
			b2BUnitData.setActive(Boolean.TRUE.equals(source.getB2bUnit().getActive()));
			target.setUnit(b2BUnitData);
			if(source.getApprovalStatus()!=null){
				target.setApprovalStatus(source.getApprovalStatus().getCode());
			}
			target.setPalletShipment(source.getPalletShipment());
			target.setCompanyName(source.getCompany());
			ItemModel owner = source.getOwner();
			
			if(null != owner && owner instanceof B2BCustomerModel){
				
				String b2bCustomerId=((B2BCustomerModel) owner).getUid();
				target.setUserId(b2bCustomerId);

			}
		}else {
			target.setApprovalStatus(GPApprovalEnum.ACTIVE.getCode());
			ItemModel owner = source.getOwner();
			
			if(null != owner && owner instanceof CustomerModel){
				
				String customerId=((CustomerModel) owner).getUid();
				target.setUserId(customerId);

			}
		}
	}
}
