/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPMediaContainerlink;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPBundleImageComponent GPBundleImageComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPBundleImageComponent extends SimpleCMSComponent
{
	/** Qualifier of the <code>GPBundleImageComponent.bundleImageList</code> attribute **/
	public static final String BUNDLEIMAGELIST = "bundleImageList";
	/** Qualifier of the <code>GPBundleImageComponent.title</code> attribute **/
	public static final String TITLE = "title";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleCMSComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(BUNDLEIMAGELIST, AttributeMode.INITIAL);
		tmp.put(TITLE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBundleImageComponent.bundleImageList</code> attribute.
	 * @return the bundleImageList - A list of bundle promotion media container with Link
	 */
	public List<GPMediaContainerlink> getBundleImageList(final SessionContext ctx)
	{
		List<GPMediaContainerlink> coll = (List<GPMediaContainerlink>)getProperty( ctx, BUNDLEIMAGELIST);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBundleImageComponent.bundleImageList</code> attribute.
	 * @return the bundleImageList - A list of bundle promotion media container with Link
	 */
	public List<GPMediaContainerlink> getBundleImageList()
	{
		return getBundleImageList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBundleImageComponent.bundleImageList</code> attribute. 
	 * @param value the bundleImageList - A list of bundle promotion media container with Link
	 */
	public void setBundleImageList(final SessionContext ctx, final List<GPMediaContainerlink> value)
	{
		setProperty(ctx, BUNDLEIMAGELIST,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBundleImageComponent.bundleImageList</code> attribute. 
	 * @param value the bundleImageList - A list of bundle promotion media container with Link
	 */
	public void setBundleImageList(final List<GPMediaContainerlink> value)
	{
		setBundleImageList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBundleImageComponent.title</code> attribute.
	 * @return the title
	 */
	public String getTitle(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBundleImageComponent.title</code> attribute.
	 * @return the title
	 */
	public String getTitle()
	{
		return getTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBundleImageComponent.title</code> attribute. 
	 * @param value the title
	 */
	public void setTitle(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBundleImageComponent.title</code> attribute. 
	 * @param value the title
	 */
	public void setTitle(final String value)
	{
		setTitle( getSession().getSessionContext(), value );
	}
	
}
