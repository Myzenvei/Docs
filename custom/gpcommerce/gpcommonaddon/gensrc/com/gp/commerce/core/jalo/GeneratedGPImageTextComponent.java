/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPBannerComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPImageTextComponent GPImageTextComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPImageTextComponent extends GPBannerComponent
{
	/** Qualifier of the <code>GPImageTextComponent.imageWidth</code> attribute **/
	public static final String IMAGEWIDTH = "imageWidth";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(GPBannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(IMAGEWIDTH, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTextComponent.imageWidth</code> attribute.
	 * @return the imageWidth - Used for controlling the image width of the component
	 */
	public String getImageWidth(final SessionContext ctx)
	{
		return (String)getProperty( ctx, IMAGEWIDTH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTextComponent.imageWidth</code> attribute.
	 * @return the imageWidth - Used for controlling the image width of the component
	 */
	public String getImageWidth()
	{
		return getImageWidth( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTextComponent.imageWidth</code> attribute. 
	 * @param value the imageWidth - Used for controlling the image width of the component
	 */
	public void setImageWidth(final SessionContext ctx, final String value)
	{
		setProperty(ctx, IMAGEWIDTH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTextComponent.imageWidth</code> attribute. 
	 * @param value the imageWidth - Used for controlling the image width of the component
	 */
	public void setImageWidth(final String value)
	{
		setImageWidth( getSession().getSessionContext(), value );
	}
	
}
