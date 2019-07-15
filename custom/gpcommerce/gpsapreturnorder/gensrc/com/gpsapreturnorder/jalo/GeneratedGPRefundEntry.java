/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gpsapreturnorder.jalo;

import com.gpsapreturnorder.constants.GpsapreturnorderConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.ordersplitting.jalo.ConsignmentEntry;
import de.hybris.platform.returns.jalo.RefundEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.returns.jalo.RefundEntry GPRefundEntry}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPRefundEntry extends RefundEntry
{
	/** Qualifier of the <code>GPRefundEntry.consignmentEntry</code> attribute **/
	public static final String CONSIGNMENTENTRY = "consignmentEntry";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(RefundEntry.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(CONSIGNMENTENTRY, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPRefundEntry.consignmentEntry</code> attribute.
	 * @return the consignmentEntry
	 */
	public ConsignmentEntry getConsignmentEntry(final SessionContext ctx)
	{
		return (ConsignmentEntry)getProperty( ctx, CONSIGNMENTENTRY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPRefundEntry.consignmentEntry</code> attribute.
	 * @return the consignmentEntry
	 */
	public ConsignmentEntry getConsignmentEntry()
	{
		return getConsignmentEntry( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPRefundEntry.consignmentEntry</code> attribute. 
	 * @param value the consignmentEntry
	 */
	public void setConsignmentEntry(final SessionContext ctx, final ConsignmentEntry value)
	{
		setProperty(ctx, CONSIGNMENTENTRY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPRefundEntry.consignmentEntry</code> attribute. 
	 * @param value the consignmentEntry
	 */
	public void setConsignmentEntry(final ConsignmentEntry value)
	{
		setConsignmentEntry( getSession().getSessionContext(), value );
	}
	
}
