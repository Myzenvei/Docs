package com.gp.backoffice.variant.renderers;

import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;

import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

import de.hybris.platform.platformbackoffice.variant.VariantAttributesSectionRenderer;

public class GPVariantAttributesSectionRenderer extends VariantAttributesSectionRenderer {

	public static final Logger LOG = Logger.getLogger(GPVariantAttributesSectionRenderer.class);

	@Override
	public Collection<String> getRenderedQualifiers(final WidgetInstanceManager widgetInstanceManager) {
		try {
			LOG.debug("inside GPVariantAttributesSectionRenderer");
			final Object obj = widgetInstanceManager.getModel().getValue("EditedVariantProduct", Object.class);
			if (null != obj) // null check added
			{
				final String typeCode = this.getTypeFacade().getType(obj);
				return this.getRenderedQualifiers(this.getTypeFacade().load(typeCode));
			}
		} catch (final TypeNotFoundException var3) {
			LOG.debug("Type not found", var3);
		}
		return Collections.emptyList();
	}

}
