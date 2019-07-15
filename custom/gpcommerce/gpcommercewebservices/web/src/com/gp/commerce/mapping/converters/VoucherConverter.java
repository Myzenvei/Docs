/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package com.gp.commerce.mapping.converters;

import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.commercewebservicescommons.dto.voucher.VoucherWsDTO;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.voucher.jalo.VoucherManager;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.mapping.WsDTOMapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;


/**
 * Bidirectional converter between {@link VoucherData} and {@Link VoucherWsDTO}
 */
@WsDTOMapping
public class VoucherConverter extends BidirectionalConverter<VoucherWsDTO, String> //NOSONAR
{
	private static final Logger LOG = Logger.getLogger(VoucherConverter.class);

	private VoucherFacade voucherFacade;
	private DataMapper dataMapper;
	@Resource(name = "sessionService")
	private SessionService sessionService;
	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name ="modelService")
	private ModelService modelService;
	@Override
	public String convertTo(final VoucherWsDTO source, final Type<String> destinationType, final MappingContext mappingContext)
	{
		if (source != null && source.getCode() != null)
		{
			return source.getCode();
		}
		return null;
	}

	@Override
	public VoucherWsDTO convertFrom(final String source, final Type<VoucherWsDTO> destinationType,
			final MappingContext mappingContext)
	{
		VoucherData voucherData= null;
		try
		{
			if(voucherFacade.checkVoucherCode(source))
			{
			   voucherData = voucherFacade.getVoucher(source); 
			}
			else {
				//Removing the expired coupons from the Cart.
				CartModel cart= cartService.getSessionCart();
				Collection<String> appliedCouponCodes = cart.getAppliedCouponCodes();
				Collection<String> appliedCoupons = CollectionUtils.isNotEmpty(appliedCouponCodes) ? new ArrayList<>(appliedCouponCodes) : null;
				if(CollectionUtils.isNotEmpty(appliedCoupons)) {
					appliedCoupons.remove(source);
				}
				cart.setAppliedCouponCodes(appliedCoupons);
				modelService.save(cart);
			}
			
		}
		catch (final VoucherOperationException e)
		{
			LOG.error("Couldn't get data for voucher: " + source, e);
			throw new IllegalArgumentException("Invalid voucher code " + source,e);
		}
		return null != voucherData ? dataMapper.map(voucherData, VoucherWsDTO.class) : new VoucherWsDTO();
	}

	@Required
	public void setVoucherFacade(final VoucherFacade voucherFacade)
	{
		this.voucherFacade = voucherFacade;
	}

	protected VoucherFacade getVoucherFacade()
	{
		return voucherFacade;
	}

	@Required
	public void setDataMapper(final DataMapper dataMapper)
	{
		this.dataMapper = dataMapper;
	}

	protected DataMapper getDataMapper()
	{
		return dataMapper;
	}

}
