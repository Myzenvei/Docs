/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPPreCuratedListComponent GPPreCuratedListComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPPreCuratedListComponent extends SimpleCMSComponent
{
	/** Qualifier of the <code>GPPreCuratedListComponent.paginationRows</code> attribute **/
	public static final String PAGINATIONROWS = "paginationRows";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(PAGINATIONROWS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPPreCuratedListComponent.paginationRows</code> attribute.
	 * @return the paginationRows
	 */
	public String getPaginationRows(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PAGINATIONROWS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPPreCuratedListComponent.paginationRows</code> attribute.
	 * @return the paginationRows
	 */
	public String getPaginationRows()
	{
		return getPaginationRows( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPPreCuratedListComponent.paginationRows</code> attribute. 
	 * @param value the paginationRows
	 */
	public void setPaginationRows(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PAGINATIONROWS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPPreCuratedListComponent.paginationRows</code> attribute. 
	 * @param value the paginationRows
	 */
	public void setPaginationRows(final String value)
	{
		setPaginationRows( getSession().getSessionContext(), value );
	}
	
}
