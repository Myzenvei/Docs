/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPBrandBarComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2.jalo.contents.components.CMSImageComponent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPImageLinkComponent GPImageLinkComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPImageLinkComponent extends CMSImageComponent
{
	/** Qualifier of the <code>GPImageLinkComponent.url</code> attribute **/
	public static final String URL = "url";
	/** Qualifier of the <code>GPImageLinkComponent.title</code> attribute **/
	public static final String TITLE = "title";
	/** Qualifier of the <code>GPImageLinkComponent.titleColor</code> attribute **/
	public static final String TITLECOLOR = "titleColor";
	/** Qualifier of the <code>GPImageLinkComponent.brandBar</code> attribute **/
	public static final String BRANDBAR = "brandBar";
	/** Relation ordering override parameter constants for BrandBar2GpImageLinkRelation from ((gpcommonaddon))*/
	protected static String BRANDBAR2GPIMAGELINKRELATION_SRC_ORDERED = "relation.BrandBar2GpImageLinkRelation.source.ordered";
	protected static String BRANDBAR2GPIMAGELINKRELATION_TGT_ORDERED = "relation.BrandBar2GpImageLinkRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for BrandBar2GpImageLinkRelation from ((gpcommonaddon))*/
	protected static String BRANDBAR2GPIMAGELINKRELATION_MARKMODIFIED = "relation.BrandBar2GpImageLinkRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CMSImageComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(URL, AttributeMode.INITIAL);
		tmp.put(TITLE, AttributeMode.INITIAL);
		tmp.put(TITLECOLOR, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageLinkComponent.brandBar</code> attribute.
	 * @return the brandBar
	 */
	public Collection<GPBrandBarComponent> getBrandBar(final SessionContext ctx)
	{
		final List<GPBrandBarComponent> items = getLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.BRANDBAR2GPIMAGELINKRELATION,
			"GPBrandBarComponent",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageLinkComponent.brandBar</code> attribute.
	 * @return the brandBar
	 */
	public Collection<GPBrandBarComponent> getBrandBar()
	{
		return getBrandBar( getSession().getSessionContext() );
	}
	
	public long getBrandBarCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			GpcommonaddonConstants.Relations.BRANDBAR2GPIMAGELINKRELATION,
			"GPBrandBarComponent",
			null
		);
	}
	
	public long getBrandBarCount()
	{
		return getBrandBarCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageLinkComponent.brandBar</code> attribute. 
	 * @param value the brandBar
	 */
	public void setBrandBar(final SessionContext ctx, final Collection<GPBrandBarComponent> value)
	{
		setLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.BRANDBAR2GPIMAGELINKRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(BRANDBAR2GPIMAGELINKRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageLinkComponent.brandBar</code> attribute. 
	 * @param value the brandBar
	 */
	public void setBrandBar(final Collection<GPBrandBarComponent> value)
	{
		setBrandBar( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to brandBar. 
	 * @param value the item to add to brandBar
	 */
	public void addToBrandBar(final SessionContext ctx, final GPBrandBarComponent value)
	{
		addLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.BRANDBAR2GPIMAGELINKRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BRANDBAR2GPIMAGELINKRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to brandBar. 
	 * @param value the item to add to brandBar
	 */
	public void addToBrandBar(final GPBrandBarComponent value)
	{
		addToBrandBar( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from brandBar. 
	 * @param value the item to remove from brandBar
	 */
	public void removeFromBrandBar(final SessionContext ctx, final GPBrandBarComponent value)
	{
		removeLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.BRANDBAR2GPIMAGELINKRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(BRANDBAR2GPIMAGELINKRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from brandBar. 
	 * @param value the item to remove from brandBar
	 */
	public void removeFromBrandBar(final GPBrandBarComponent value)
	{
		removeFromBrandBar( getSession().getSessionContext(), value );
	}
	
	@Override
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("GPBrandBarComponent");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(BRANDBAR2GPIMAGELINKRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageLinkComponent.title</code> attribute.
	 * @return the title - title for image.
	 */
	public String getTitle(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageLinkComponent.title</code> attribute.
	 * @return the title - title for image.
	 */
	public String getTitle()
	{
		return getTitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageLinkComponent.title</code> attribute. 
	 * @param value the title - title for image.
	 */
	public void setTitle(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageLinkComponent.title</code> attribute. 
	 * @param value the title - title for image.
	 */
	public void setTitle(final String value)
	{
		setTitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageLinkComponent.titleColor</code> attribute.
	 * @return the titleColor - title color for image.
	 */
	public String getTitleColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TITLECOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageLinkComponent.titleColor</code> attribute.
	 * @return the titleColor - title color for image.
	 */
	public String getTitleColor()
	{
		return getTitleColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageLinkComponent.titleColor</code> attribute. 
	 * @param value the titleColor - title color for image.
	 */
	public void setTitleColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TITLECOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageLinkComponent.titleColor</code> attribute. 
	 * @param value the titleColor - title color for image.
	 */
	public void setTitleColor(final String value)
	{
		setTitleColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageLinkComponent.url</code> attribute.
	 * @return the url - url for image.
	 */
	public String getUrl(final SessionContext ctx)
	{
		return (String)getProperty( ctx, URL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageLinkComponent.url</code> attribute.
	 * @return the url - url for image.
	 */
	public String getUrl()
	{
		return getUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageLinkComponent.url</code> attribute. 
	 * @param value the url - url for image.
	 */
	public void setUrl(final SessionContext ctx, final String value)
	{
		setProperty(ctx, URL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageLinkComponent.url</code> attribute. 
	 * @param value the url - url for image.
	 */
	public void setUrl(final String value)
	{
		setUrl( getSession().getSessionContext(), value );
	}
	
}
