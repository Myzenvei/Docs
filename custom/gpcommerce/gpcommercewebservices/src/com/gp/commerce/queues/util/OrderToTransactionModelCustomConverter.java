/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.queues.util;

import com.arvatosystems.avalara.custommodels.impl.DelegateCreateTransactionModel;
import com.arvatosystems.avalara.service.converters.AvalaraConverter;
import com.arvatosystems.avalara.service.discountdispersement.DiscountDisperser;
import com.arvatosystems.avalara.service.utils.AvalaraFieldUtils;
import com.arvatosystems.avlara.rest.model.AddressesModel;
import com.arvatosystems.avlara.rest.model.CreateTransactionModel;
import com.arvatosystems.avlara.rest.model.LineItemModel;
import com.arvatosystems.avlara.rest.model.TaxOverrideModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hamcrest.core.IsInstanceOf;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.Map;

public class OrderToTransactionModelCustomConverter implements AvalaraConverter<AbstractOrderModel, CreateTransactionModel> {
	 @Resource(
		        name = "defaultDiscountDisperser"
		    )
		    private DiscountDisperser discountDisperser;
		    @Resource
		    private AvalaraConverter<AbstractOrderEntryModel, LineItemModel> lineItemConverter;
		    @Resource(
		        name = "abstractOrderModelToAddressesModelConverter"
		    )
		    private AvalaraConverter<AbstractOrderModel, AddressesModel> addressesConverter;
		    @Resource(
		        name = "abstractOrderToTaxOverrideModelConverter"
		    )
		    private AvalaraConverter<AbstractOrderModel, TaxOverrideModel> taxOverrideConverter;
		    @Resource
		    private AvalaraConverter<AbstractOrderModel, Map<String, String>> parametersConverter;
		    @Resource
		    private AvalaraConverter<AbstractOrderModel, LineItemModel> shippingCostConverter;
		    
		    private static final Logger LOG = Logger.getLogger(OrderToTransactionModelCustomConverter.class);
		    
		    public OrderToTransactionModelCustomConverter() {
		    }

		    public CreateTransactionModel convert(AbstractOrderModel input) {
		        CreateTransactionModel answer = new CreateTransactionModel();
		        answer.setAddresses((AddressesModel)this.addressesConverter.convert(input));
		        answer.setCode(AvalaraFieldUtils.truncateString(input.getCode(), 50));
		        answer.setCurrencyCode(input.getCurrency() != null ? AvalaraFieldUtils.truncateString(input.getCurrency().getIsocode(), 3) : "");
		        UserModel user = input.getUser();
		        if(user instanceof CustomerModel){
		        	if (LOG.isDebugEnabled())
		    		{
		    			LOG.debug("Customer netsuite id " + ((CustomerModel)user).getContactEmail());
		    		}
		        	answer.setCustomerCode(((CustomerModel)user).getContactEmail());
		        }
		        answer.setDate(OffsetDateTime.now().truncatedTo(ChronoUnit.DAYS));
		        answer.setDescription(AvalaraFieldUtils.truncateString(input.getDescription(), 2048));
		        String email = user instanceof CustomerModel ? ((CustomerModel)user).getContactEmail() : "";
		        answer.setEmail(AvalaraFieldUtils.truncateString(email, 50));
		        this.setOrderLines(input, answer);
		        answer.setParameters((Map)this.parametersConverter.convert(input));
		        answer.setPurchaseOrderNo(AvalaraFieldUtils.truncateString(input.getCode(), 50));
		        answer.setReferenceCode(AvalaraFieldUtils.truncateString(input.getCode(), 1024));
		        answer.setTaxOverride((TaxOverrideModel)this.taxOverrideConverter.convert(input));
		        return new DelegateCreateTransactionModel(answer, input);
		    }

		    protected void setOrderLines(AbstractOrderModel source, CreateTransactionModel target) {
		        List<LineItemModel> convertedLineItems = this.setOrderEntryLines(source);
		        List<LineItemModel> discountedLines = this.discountDisperser.disperseGlobalDiscounts(source, convertedLineItems);
		        this.setShippingOrderLines(source, discountedLines);
		        target.setLines(discountedLines);
		    }

		    protected void setShippingOrderLines(AbstractOrderModel input, List<LineItemModel> convertedLineItems) {
		        if (input.getDeliveryMode() != null && input.getDeliveryCost() != null) {
		            convertedLineItems.add((LineItemModel)this.shippingCostConverter.convert(input));
		        }

		    }

		    protected List<LineItemModel> setOrderEntryLines(AbstractOrderModel input) {
		        List<LineItemModel> convertedLineItems = new ArrayList();
		        Iterator var4 = input.getEntries().iterator();

		        while(var4.hasNext()) {
		            AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel)var4.next();
		            convertedLineItems.add((LineItemModel)this.lineItemConverter.convert(orderEntry));
		        }

		        return convertedLineItems;
		    }

		    public AvalaraConverter<AbstractOrderEntryModel, LineItemModel> getLineItemConverter() {
		        return this.lineItemConverter;
		    }

		    public void setLineItemConverter(AvalaraConverter<AbstractOrderEntryModel, LineItemModel> lineItemConverter) {
		        this.lineItemConverter = lineItemConverter;
		    }

		    public AvalaraConverter<AbstractOrderModel, AddressesModel> getAddressesConverter() {
		        return this.addressesConverter;
		    }

		    public void setAddressesConverter(AvalaraConverter<AbstractOrderModel, AddressesModel> addressesConverter) {
		        this.addressesConverter = addressesConverter;
		    }

		    public AvalaraConverter<AbstractOrderModel, TaxOverrideModel> getTaxOverrideConverter() {
		        return this.taxOverrideConverter;
		    }

		    public void setTaxOverrideConverter(AvalaraConverter<AbstractOrderModel, TaxOverrideModel> taxOverrideConverter) {
		        this.taxOverrideConverter = taxOverrideConverter;
		    }

		    public AvalaraConverter<AbstractOrderModel, Map<String, String>> getParametersConverter() {
		        return this.parametersConverter;
		    }

		    public void setParametersConverter(AvalaraConverter<AbstractOrderModel, Map<String, String>> parametersConverter) {
		        this.parametersConverter = parametersConverter;
		    }

		    public AvalaraConverter<AbstractOrderModel, LineItemModel> getShippingCostConverter() {
		        return this.shippingCostConverter;
		    }

		    public void setShippingCostConverter(AvalaraConverter<AbstractOrderModel, LineItemModel> shippingCostConverter) {
		        this.shippingCostConverter = shippingCostConverter;
		    }

		    public DiscountDisperser getDiscountDisperser() {
		        return this.discountDisperser;
		    }

		    public void setDiscountDisperser(DiscountDisperser discountDisperser) {
		        this.discountDisperser = discountDisperser;
		    }
}
