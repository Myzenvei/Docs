/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.populators;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.gp.commerce.core.model.SplitEntryModel;
import com.gp.commerce.user.data.AddressDataList;

import de.hybris.platform.b2b.constants.B2BConstants;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

public class GPOrderHistoryPopulator implements Populator<OrderModel, OrderHistoryData> {

	private Converter<AddressModel, AddressData> addressConverter;
	private Converter<PrincipalModel, PrincipalData> principalConverter;

	@Override
	public void populate(OrderModel source, OrderHistoryData target) throws ConversionException {
		int totalQty=0 ;
		AddressDataList addList =new AddressDataList();
		List<AddressData> addressList =new ArrayList<>() ;
		List<AbstractOrderEntryModel> orderEntryList=source.getEntries() ;
		for(AbstractOrderEntryModel orderEntry : orderEntryList) {
			List<SplitEntryModel> spliEntryList =orderEntry.getSplitEntry() ;  
			if(CollectionUtils.isNotEmpty(spliEntryList)) {
				for(SplitEntryModel  splitEntry : spliEntryList) {
					totalQty += Integer.valueOf(splitEntry.getQty());
					if(splitEntry.getDeliveryAddress() != null) { 
						boolean addressExists=false;
						AddressData addressData=getAddressConverter().convert(splitEntry.getDeliveryAddress());
						for(AddressData address :addressList) {
							if(address.getId().equals(addressData.getId())) {
								addressExists=true ;
							}
						}
						if(!addressExists) {
						addressList.add(addressData) ;
						}
					}
				}
			}
		}
		target.setCreationTime(source.getUser().getCreationtime());
		addList.setAddresses(addressList);
		addList.setIsAdmin(isAdmin(source));
		target.setShippingAddress(addList);
		target.setTotalNumberOfProducts(String.valueOf(totalQty)) ;
		target.setUser(getPrincipalConverter().convert(source.getUser()));
		target.setSourceNetSuite(source.isSourceNetSuite());
	}
	
	
 
	private boolean isAdmin(OrderModel source){
		Set<PrincipalGroupModel>  userGroups=	source.getUser().getAllgroups() ;
				for (final PrincipalGroupModel principalGroupModel : userGroups)
				{
					final String groupId = principalGroupModel.getUid();
					if (groupId != null && groupId.equals(B2BConstants.B2BADMINGROUP))
					{
						return true;

					}
				}
				return false;
	}
 
	public Converter<AddressModel, AddressData> getAddressConverter() {
		return addressConverter;
	}
	public void setAddressConverter(Converter<AddressModel, AddressData> addressConverter) {
		this.addressConverter = addressConverter;
	}

	protected Converter<PrincipalModel, PrincipalData> getPrincipalConverter() {
		return principalConverter;
	}

	public void setPrincipalConverter(Converter<PrincipalModel, PrincipalData> principalConverter) {
		this.principalConverter = principalConverter;
	}
	
}
