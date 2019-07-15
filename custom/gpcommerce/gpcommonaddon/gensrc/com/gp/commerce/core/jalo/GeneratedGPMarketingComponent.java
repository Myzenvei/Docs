/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.acceleratorcms.jalo.components.SimpleResponsiveBannerComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPMarketingComponent GPMarketingComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPMarketingComponent extends SimpleResponsiveBannerComponent
{
	/** Qualifier of the <code>GPMarketingComponent.headerText</code> attribute **/
	public static final String HEADERTEXT = "headerText";
	/** Qualifier of the <code>GPMarketingComponent.headerTextColor</code> attribute **/
	public static final String HEADERTEXTCOLOR = "headerTextColor";
	/** Qualifier of the <code>GPMarketingComponent.backgroundColor</code> attribute **/
	public static final String BACKGROUNDCOLOR = "backgroundColor";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleResponsiveBannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(HEADERTEXT, AttributeMode.INITIAL);
		tmp.put(HEADERTEXTCOLOR, AttributeMode.INITIAL);
		tmp.put(BACKGROUNDCOLOR, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingComponent.backgroundColor</code> attribute.
	 * @return the backgroundColor - The background color of the component.
	 */
	public String getBackgroundColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BACKGROUNDCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingComponent.backgroundColor</code> attribute.
	 * @return the backgroundColor - The background color of the component.
	 */
	public String getBackgroundColor()
	{
		return getBackgroundColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingComponent.backgroundColor</code> attribute. 
	 * @param value the backgroundColor - The background color of the component.
	 */
	public void setBackgroundColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BACKGROUNDCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingComponent.backgroundColor</code> attribute. 
	 * @param value the backgroundColor - The background color of the component.
	 */
	public void setBackgroundColor(final String value)
	{
		setBackgroundColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingComponent.headerText</code> attribute.
	 * @return the headerText - The component header text attached to this  component.
	 */
	public String getHeaderText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HEADERTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingComponent.headerText</code> attribute.
	 * @return the headerText - The component header text attached to this  component.
	 */
	public String getHeaderText()
	{
		return getHeaderText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingComponent.headerText</code> attribute. 
	 * @param value the headerText - The component header text attached to this  component.
	 */
	public void setHeaderText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HEADERTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingComponent.headerText</code> attribute. 
	 * @param value the headerText - The component header text attached to this  component.
	 */
	public void setHeaderText(final String value)
	{
		setHeaderText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingComponent.headerTextColor</code> attribute.
	 * @return the headerTextColor - The component header color attached to this  component.
	 */
	public String getHeaderTextColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HEADERTEXTCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPMarketingComponent.headerTextColor</code> attribute.
	 * @return the headerTextColor - The component header color attached to this  component.
	 */
	public String getHeaderTextColor()
	{
		return getHeaderTextColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingComponent.headerTextColor</code> attribute. 
	 * @param value the headerTextColor - The component header color attached to this  component.
	 */
	public void setHeaderTextColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HEADERTEXTCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPMarketingComponent.headerTextColor</code> attribute. 
	 * @param value the headerTextColor - The component header color attached to this  component.
	 */
	public void setHeaderTextColor(final String value)
	{
		setHeaderTextColor( getSession().getSessionContext(), value );
	}
	
}
