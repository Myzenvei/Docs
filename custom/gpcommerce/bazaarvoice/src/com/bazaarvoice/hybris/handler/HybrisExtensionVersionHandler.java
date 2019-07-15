/**
 * 
 */
package com.bazaarvoice.hybris.handler;

import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;

import org.springframework.stereotype.Component;

import com.bazaarvoice.hybris.constants.BazaarvoiceConstants;
import com.bazaarvoice.hybris.model.BazaarvoiceConfigModel;


/**
 * @author christina romashchenko
 * 
 */
@Component
public class HybrisExtensionVersionHandler implements DynamicAttributeHandler<String, BazaarvoiceConfigModel>
{


	@Override
	public String get(final BazaarvoiceConfigModel arg0)
	{
		return BazaarvoiceConstants.HYBRIS_EXTENSION_VERSION;
	}

	@Override
	public void set(final BazaarvoiceConfigModel arg0, final String arg1)
	{
		throw new UnsupportedOperationException();

	}

}
