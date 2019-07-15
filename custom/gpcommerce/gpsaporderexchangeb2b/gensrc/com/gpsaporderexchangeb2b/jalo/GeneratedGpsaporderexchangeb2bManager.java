/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gpsaporderexchangeb2b.jalo;

import com.gpsaporderexchangeb2b.constants.Gpsaporderexchangeb2bConstants;
import com.gpsaporderexchangeb2b.jalo.SendOrderToDatahubCronjob;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>Gpsaporderexchangeb2bManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGpsaporderexchangeb2bManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("exportStatus", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.ordersplitting.jalo.Consignment", Collections.unmodifiableMap(tmp));
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public SendOrderToDatahubCronjob createSendOrderToDatahubCronjob(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( Gpsaporderexchangeb2bConstants.TC.SENDORDERTODATAHUBCRONJOB );
			return (SendOrderToDatahubCronjob)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating SendOrderToDatahubCronjob : "+e.getMessage(), 0 );
		}
	}
	
	public SendOrderToDatahubCronjob createSendOrderToDatahubCronjob(final Map attributeValues)
	{
		return createSendOrderToDatahubCronjob( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consignment.exportStatus</code> attribute.
	 * @return the exportStatus
	 */
	public EnumerationValue getExportStatus(final SessionContext ctx, final Consignment item)
	{
		return (EnumerationValue)item.getProperty( ctx, Gpsaporderexchangeb2bConstants.Attributes.Consignment.EXPORTSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consignment.exportStatus</code> attribute.
	 * @return the exportStatus
	 */
	public EnumerationValue getExportStatus(final Consignment item)
	{
		return getExportStatus( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consignment.exportStatus</code> attribute. 
	 * @param value the exportStatus
	 */
	public void setExportStatus(final SessionContext ctx, final Consignment item, final EnumerationValue value)
	{
		item.setProperty(ctx, Gpsaporderexchangeb2bConstants.Attributes.Consignment.EXPORTSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consignment.exportStatus</code> attribute. 
	 * @param value the exportStatus
	 */
	public void setExportStatus(final Consignment item, final EnumerationValue value)
	{
		setExportStatus( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return Gpsaporderexchangeb2bConstants.EXTENSIONNAME;
	}
	
}
