/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPBannerComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.acceleratorcms.jalo.components.SimpleResponsiveBannerComponent;
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
 * Generated class for type {@link com.gp.commerce.core.jalo.GPImageTileComponent GPImageTileComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPImageTileComponent extends SimpleResponsiveBannerComponent
{
	/** Qualifier of the <code>GPImageTileComponent.componentHeader</code> attribute **/
	public static final String COMPONENTHEADER = "componentHeader";
	/** Qualifier of the <code>GPImageTileComponent.componentHeaderColor</code> attribute **/
	public static final String COMPONENTHEADERCOLOR = "componentHeaderColor";
	/** Qualifier of the <code>GPImageTileComponent.backgroundColor</code> attribute **/
	public static final String BACKGROUNDCOLOR = "backgroundColor";
	/** Qualifier of the <code>GPImageTileComponent.noOfColumns</code> attribute **/
	public static final String NOOFCOLUMNS = "noOfColumns";
	/** Qualifier of the <code>GPImageTileComponent.ctaText</code> attribute **/
	public static final String CTATEXT = "ctaText";
	/** Qualifier of the <code>GPImageTileComponent.ctaTextColor</code> attribute **/
	public static final String CTATEXTCOLOR = "ctaTextColor";
	/** Qualifier of the <code>GPImageTileComponent.isCarousel</code> attribute **/
	public static final String ISCAROUSEL = "isCarousel";
	/** Qualifier of the <code>GPImageTileComponent.subHeaderText</code> attribute **/
	public static final String SUBHEADERTEXT = "subHeaderText";
	/** Qualifier of the <code>GPImageTileComponent.highlighterText</code> attribute **/
	public static final String HIGHLIGHTERTEXT = "highlighterText";
	/** Qualifier of the <code>GPImageTileComponent.banners</code> attribute **/
	public static final String BANNERS = "banners";
	/** Relation ordering override parameter constants for GpImagetile2GpBannerRelation from ((gpcommonaddon))*/
	protected static String GPIMAGETILE2GPBANNERRELATION_SRC_ORDERED = "relation.GpImagetile2GpBannerRelation.source.ordered";
	protected static String GPIMAGETILE2GPBANNERRELATION_TGT_ORDERED = "relation.GpImagetile2GpBannerRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for GpImagetile2GpBannerRelation from ((gpcommonaddon))*/
	protected static String GPIMAGETILE2GPBANNERRELATION_MARKMODIFIED = "relation.GpImagetile2GpBannerRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleResponsiveBannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(COMPONENTHEADER, AttributeMode.INITIAL);
		tmp.put(COMPONENTHEADERCOLOR, AttributeMode.INITIAL);
		tmp.put(BACKGROUNDCOLOR, AttributeMode.INITIAL);
		tmp.put(NOOFCOLUMNS, AttributeMode.INITIAL);
		tmp.put(CTATEXT, AttributeMode.INITIAL);
		tmp.put(CTATEXTCOLOR, AttributeMode.INITIAL);
		tmp.put(ISCAROUSEL, AttributeMode.INITIAL);
		tmp.put(SUBHEADERTEXT, AttributeMode.INITIAL);
		tmp.put(HIGHLIGHTERTEXT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.backgroundColor</code> attribute.
	 * @return the backgroundColor - The background color for this  component.
	 */
	public String getBackgroundColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BACKGROUNDCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.backgroundColor</code> attribute.
	 * @return the backgroundColor - The background color for this  component.
	 */
	public String getBackgroundColor()
	{
		return getBackgroundColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.backgroundColor</code> attribute. 
	 * @param value the backgroundColor - The background color for this  component.
	 */
	public void setBackgroundColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BACKGROUNDCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.backgroundColor</code> attribute. 
	 * @param value the backgroundColor - The background color for this  component.
	 */
	public void setBackgroundColor(final String value)
	{
		setBackgroundColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.banners</code> attribute.
	 * @return the banners
	 */
	public Collection<GPBannerComponent> getBanners(final SessionContext ctx)
	{
		final List<GPBannerComponent> items = getLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPIMAGETILE2GPBANNERRELATION,
			"GPBannerComponent",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.banners</code> attribute.
	 * @return the banners
	 */
	public Collection<GPBannerComponent> getBanners()
	{
		return getBanners( getSession().getSessionContext() );
	}
	
	public long getBannersCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPIMAGETILE2GPBANNERRELATION,
			"GPBannerComponent",
			null
		);
	}
	
	public long getBannersCount()
	{
		return getBannersCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.banners</code> attribute. 
	 * @param value the banners
	 */
	public void setBanners(final SessionContext ctx, final Collection<GPBannerComponent> value)
	{
		setLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPIMAGETILE2GPBANNERRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(GPIMAGETILE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.banners</code> attribute. 
	 * @param value the banners
	 */
	public void setBanners(final Collection<GPBannerComponent> value)
	{
		setBanners( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to banners. 
	 * @param value the item to add to banners
	 */
	public void addToBanners(final SessionContext ctx, final GPBannerComponent value)
	{
		addLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPIMAGETILE2GPBANNERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPIMAGETILE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to banners. 
	 * @param value the item to add to banners
	 */
	public void addToBanners(final GPBannerComponent value)
	{
		addToBanners( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from banners. 
	 * @param value the item to remove from banners
	 */
	public void removeFromBanners(final SessionContext ctx, final GPBannerComponent value)
	{
		removeLinkedItems( 
			ctx,
			true,
			GpcommonaddonConstants.Relations.GPIMAGETILE2GPBANNERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPIMAGETILE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from banners. 
	 * @param value the item to remove from banners
	 */
	public void removeFromBanners(final GPBannerComponent value)
	{
		removeFromBanners( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.componentHeader</code> attribute.
	 * @return the componentHeader - The component header  that is attached to this  component.
	 */
	public String getComponentHeader(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COMPONENTHEADER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.componentHeader</code> attribute.
	 * @return the componentHeader - The component header  that is attached to this  component.
	 */
	public String getComponentHeader()
	{
		return getComponentHeader( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.componentHeader</code> attribute. 
	 * @param value the componentHeader - The component header  that is attached to this  component.
	 */
	public void setComponentHeader(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COMPONENTHEADER,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.componentHeader</code> attribute. 
	 * @param value the componentHeader - The component header  that is attached to this  component.
	 */
	public void setComponentHeader(final String value)
	{
		setComponentHeader( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.componentHeaderColor</code> attribute.
	 * @return the componentHeaderColor - The component header color to this  component.
	 */
	public String getComponentHeaderColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, COMPONENTHEADERCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.componentHeaderColor</code> attribute.
	 * @return the componentHeaderColor - The component header color to this  component.
	 */
	public String getComponentHeaderColor()
	{
		return getComponentHeaderColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.componentHeaderColor</code> attribute. 
	 * @param value the componentHeaderColor - The component header color to this  component.
	 */
	public void setComponentHeaderColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, COMPONENTHEADERCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.componentHeaderColor</code> attribute. 
	 * @param value the componentHeaderColor - The component header color to this  component.
	 */
	public void setComponentHeaderColor(final String value)
	{
		setComponentHeaderColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.ctaText</code> attribute.
	 * @return the ctaText - The cta text for the component.
	 */
	public String getCtaText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTATEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.ctaText</code> attribute.
	 * @return the ctaText - The cta text for the component.
	 */
	public String getCtaText()
	{
		return getCtaText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.ctaText</code> attribute. 
	 * @param value the ctaText - The cta text for the component.
	 */
	public void setCtaText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTATEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.ctaText</code> attribute. 
	 * @param value the ctaText - The cta text for the component.
	 */
	public void setCtaText(final String value)
	{
		setCtaText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.ctaTextColor</code> attribute.
	 * @return the ctaTextColor - The cta text color for the component.
	 */
	public String getCtaTextColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTATEXTCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.ctaTextColor</code> attribute.
	 * @return the ctaTextColor - The cta text color for the component.
	 */
	public String getCtaTextColor()
	{
		return getCtaTextColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.ctaTextColor</code> attribute. 
	 * @param value the ctaTextColor - The cta text color for the component.
	 */
	public void setCtaTextColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTATEXTCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.ctaTextColor</code> attribute. 
	 * @param value the ctaTextColor - The cta text color for the component.
	 */
	public void setCtaTextColor(final String value)
	{
		setCtaTextColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.highlighterText</code> attribute.
	 * @return the highlighterText - Highlighter text for the component.
	 */
	public String getHighlighterText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HIGHLIGHTERTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.highlighterText</code> attribute.
	 * @return the highlighterText - Highlighter text for the component.
	 */
	public String getHighlighterText()
	{
		return getHighlighterText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.highlighterText</code> attribute. 
	 * @param value the highlighterText - Highlighter text for the component.
	 */
	public void setHighlighterText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HIGHLIGHTERTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.highlighterText</code> attribute. 
	 * @param value the highlighterText - Highlighter text for the component.
	 */
	public void setHighlighterText(final String value)
	{
		setHighlighterText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.isCarousel</code> attribute.
	 * @return the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public Boolean isIsCarousel(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ISCAROUSEL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.isCarousel</code> attribute.
	 * @return the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public Boolean isIsCarousel()
	{
		return isIsCarousel( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.isCarousel</code> attribute. 
	 * @return the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public boolean isIsCarouselAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsCarousel( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.isCarousel</code> attribute. 
	 * @return the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public boolean isIsCarouselAsPrimitive()
	{
		return isIsCarouselAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.isCarousel</code> attribute. 
	 * @param value the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public void setIsCarousel(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ISCAROUSEL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.isCarousel</code> attribute. 
	 * @param value the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public void setIsCarousel(final Boolean value)
	{
		setIsCarousel( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.isCarousel</code> attribute. 
	 * @param value the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public void setIsCarousel(final SessionContext ctx, final boolean value)
	{
		setIsCarousel( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.isCarousel</code> attribute. 
	 * @param value the isCarousel - Boolean attribute to determine whether it s a list or carousel.
	 */
	public void setIsCarousel(final boolean value)
	{
		setIsCarousel( getSession().getSessionContext(), value );
	}
	
	@Override
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("GPBannerComponent");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(GPIMAGETILE2GPBANNERRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.noOfColumns</code> attribute.
	 * @return the noOfColumns - The number of tiles to be in the component.
	 */
	public String getNoOfColumns(final SessionContext ctx)
	{
		return (String)getProperty( ctx, NOOFCOLUMNS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.noOfColumns</code> attribute.
	 * @return the noOfColumns - The number of tiles to be in the component.
	 */
	public String getNoOfColumns()
	{
		return getNoOfColumns( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.noOfColumns</code> attribute. 
	 * @param value the noOfColumns - The number of tiles to be in the component.
	 */
	public void setNoOfColumns(final SessionContext ctx, final String value)
	{
		setProperty(ctx, NOOFCOLUMNS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.noOfColumns</code> attribute. 
	 * @param value the noOfColumns - The number of tiles to be in the component.
	 */
	public void setNoOfColumns(final String value)
	{
		setNoOfColumns( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.subHeaderText</code> attribute.
	 * @return the subHeaderText - Sub Header text for the component.
	 */
	public String getSubHeaderText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, SUBHEADERTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPImageTileComponent.subHeaderText</code> attribute.
	 * @return the subHeaderText - Sub Header text for the component.
	 */
	public String getSubHeaderText()
	{
		return getSubHeaderText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.subHeaderText</code> attribute. 
	 * @param value the subHeaderText - Sub Header text for the component.
	 */
	public void setSubHeaderText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, SUBHEADERTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPImageTileComponent.subHeaderText</code> attribute. 
	 * @param value the subHeaderText - Sub Header text for the component.
	 */
	public void setSubHeaderText(final String value)
	{
		setSubHeaderText( getSession().getSessionContext(), value );
	}
	
}
