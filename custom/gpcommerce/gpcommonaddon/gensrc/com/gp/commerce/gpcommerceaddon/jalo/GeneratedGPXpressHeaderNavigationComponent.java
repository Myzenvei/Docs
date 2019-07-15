/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.gpcommerceaddon.jalo;

import com.gp.commerce.core.jalo.GPHeaderNavigationComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.CMSLinkComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPHeaderNavigationComponent GPXpressHeaderNavigationComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPXpressHeaderNavigationComponent extends GPHeaderNavigationComponent
{
	/** Qualifier of the <code>GPXpressHeaderNavigationComponent.myLists</code> attribute **/
	public static final String MYLISTS = "myLists";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(GPHeaderNavigationComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(MYLISTS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPXpressHeaderNavigationComponent.myLists</code> attribute.
	 * @return the myLists - The CMS navigation node of this navigation component.
	 */
	public CMSLinkComponent getMyLists(final SessionContext ctx)
	{
		return (CMSLinkComponent)getProperty( ctx, MYLISTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPXpressHeaderNavigationComponent.myLists</code> attribute.
	 * @return the myLists - The CMS navigation node of this navigation component.
	 */
	public CMSLinkComponent getMyLists()
	{
		return getMyLists( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPXpressHeaderNavigationComponent.myLists</code> attribute. 
	 * @param value the myLists - The CMS navigation node of this navigation component.
	 */
	public void setMyLists(final SessionContext ctx, final CMSLinkComponent value)
	{
		setProperty(ctx, MYLISTS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPXpressHeaderNavigationComponent.myLists</code> attribute. 
	 * @param value the myLists - The CMS navigation node of this navigation component.
	 */
	public void setMyLists(final CMSLinkComponent value)
	{
		setMyLists( getSession().getSessionContext(), value );
	}
	
}
