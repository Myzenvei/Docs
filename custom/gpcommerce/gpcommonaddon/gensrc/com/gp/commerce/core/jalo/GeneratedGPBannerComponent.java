/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPImageTileComponent;
import com.gp.commerce.core.jalo.GPMarketingSidebySideComponent;
import com.gp.commerce.core.jalo.GPMediaComponent;
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
 * Generated class for type {@link com.gp.commerce.core.jalo.GPBannerComponent GPBannerComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPBannerComponent extends SimpleResponsiveBannerComponent
{
	/** Qualifier of the <code>GPBannerComponent.headline</code> attribute **/
	public static final String HEADLINE = "headline";
	/** Qualifier of the <code>GPBannerComponent.headerColor</code> attribute **/
	public static final String HEADERCOLOR = "headerColor";
	/** Qualifier of the <code>GPBannerComponent.content</code> attribute **/
	public static final String CONTENT = "content";
	/** Qualifier of the <code>GPBannerComponent.contentColor</code> attribute **/
	public static final String CONTENTCOLOR = "contentColor";
	/** Qualifier of the <code>GPBannerComponent.ctaColor</code> attribute **/
	public static final String CTACOLOR = "ctaColor";
	/** Qualifier of the <code>GPBannerComponent.ctaText</code> attribute **/
	public static final String CTATEXT = "ctaText";
	/** Qualifier of the <code>GPBannerComponent.textalignment</code> attribute **/
	public static final String TEXTALIGNMENT = "textalignment";
	/** Qualifier of the <code>GPBannerComponent.backgroundColor</code> attribute **/
	public static final String BACKGROUNDCOLOR = "backgroundColor";
	/** Qualifier of the <code>GPBannerComponent.imageAlignment</code> attribute **/
	public static final String IMAGEALIGNMENT = "imageAlignment";
	/** Qualifier of the <code>GPBannerComponent.imageLink</code> attribute **/
	public static final String IMAGELINK = "imageLink";
	/** Qualifier of the <code>GPBannerComponent.mediaForText</code> attribute **/
	public static final String MEDIAFORTEXT = "mediaForText";
	/** Qualifier of the <code>GPBannerComponent.backgroundVideo</code> attribute **/
	public static final String BACKGROUNDVIDEO = "backgroundVideo";
	/** Qualifier of the <code>GPBannerComponent.isVideo</code> attribute **/
	public static final String ISVIDEO = "isVideo";
	/** Qualifier of the <code>GPBannerComponent.playIconColor</code> attribute **/
	public static final String PLAYICONCOLOR = "playIconColor";
	/** Qualifier of the <code>GPBannerComponent.isExternalImageLink</code> attribute **/
	public static final String ISEXTERNALIMAGELINK = "isExternalImageLink";
	/** Qualifier of the <code>GPBannerComponent.isVideoCtaLink</code> attribute **/
	public static final String ISVIDEOCTALINK = "isVideoCtaLink";
	/** Qualifier of the <code>GPBannerComponent.imageTile</code> attribute **/
	public static final String IMAGETILE = "imageTile";
	/** Relation ordering override parameter constants for GpImagetile2GpBannerRelation from ((gpcommonaddon))*/
	protected static String GPIMAGETILE2GPBANNERRELATION_SRC_ORDERED = "relation.GpImagetile2GpBannerRelation.source.ordered";
	protected static String GPIMAGETILE2GPBANNERRELATION_TGT_ORDERED = "relation.GpImagetile2GpBannerRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for GpImagetile2GpBannerRelation from ((gpcommonaddon))*/
	protected static String GPIMAGETILE2GPBANNERRELATION_MARKMODIFIED = "relation.GpImagetile2GpBannerRelation.markmodified";
	/** Qualifier of the <code>GPBannerComponent.marketingSide</code> attribute **/
	public static final String MARKETINGSIDE = "marketingSide";
	/** Relation ordering override parameter constants for GpMarketingSideBySide2GpBannerRelation from ((gpcommonaddon))*/
	protected static String GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_SRC_ORDERED = "relation.GpMarketingSideBySide2GpBannerRelation.source.ordered";
	protected static String GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_TGT_ORDERED = "relation.GpMarketingSideBySide2GpBannerRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for GpMarketingSideBySide2GpBannerRelation from ((gpcommonaddon))*/
	protected static String GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_MARKMODIFIED = "relation.GpMarketingSideBySide2GpBannerRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(SimpleResponsiveBannerComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(HEADLINE, AttributeMode.INITIAL);
		tmp.put(HEADERCOLOR, AttributeMode.INITIAL);
		tmp.put(CONTENT, AttributeMode.INITIAL);
		tmp.put(CONTENTCOLOR, AttributeMode.INITIAL);
		tmp.put(CTACOLOR, AttributeMode.INITIAL);
		tmp.put(CTATEXT, AttributeMode.INITIAL);
		tmp.put(TEXTALIGNMENT, AttributeMode.INITIAL);
		tmp.put(BACKGROUNDCOLOR, AttributeMode.INITIAL);
		tmp.put(IMAGEALIGNMENT, AttributeMode.INITIAL);
		tmp.put(IMAGELINK, AttributeMode.INITIAL);
		tmp.put(MEDIAFORTEXT, AttributeMode.INITIAL);
		tmp.put(BACKGROUNDVIDEO, AttributeMode.INITIAL);
		tmp.put(ISVIDEO, AttributeMode.INITIAL);
		tmp.put(PLAYICONCOLOR, AttributeMode.INITIAL);
		tmp.put(ISEXTERNALIMAGELINK, AttributeMode.INITIAL);
		tmp.put(ISVIDEOCTALINK, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.backgroundColor</code> attribute.
	 * @return the backgroundColor - The background color for this  component.
	 */
	public String getBackgroundColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BACKGROUNDCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.backgroundColor</code> attribute.
	 * @return the backgroundColor - The background color for this  component.
	 */
	public String getBackgroundColor()
	{
		return getBackgroundColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.backgroundColor</code> attribute. 
	 * @param value the backgroundColor - The background color for this  component.
	 */
	public void setBackgroundColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BACKGROUNDCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.backgroundColor</code> attribute. 
	 * @param value the backgroundColor - The background color for this  component.
	 */
	public void setBackgroundColor(final String value)
	{
		setBackgroundColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.backgroundVideo</code> attribute.
	 * @return the backgroundVideo - The background video for this  component.
	 */
	public String getBackgroundVideo(final SessionContext ctx)
	{
		return (String)getProperty( ctx, BACKGROUNDVIDEO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.backgroundVideo</code> attribute.
	 * @return the backgroundVideo - The background video for this  component.
	 */
	public String getBackgroundVideo()
	{
		return getBackgroundVideo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.backgroundVideo</code> attribute. 
	 * @param value the backgroundVideo - The background video for this  component.
	 */
	public void setBackgroundVideo(final SessionContext ctx, final String value)
	{
		setProperty(ctx, BACKGROUNDVIDEO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.backgroundVideo</code> attribute. 
	 * @param value the backgroundVideo - The background video for this  component.
	 */
	public void setBackgroundVideo(final String value)
	{
		setBackgroundVideo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.content</code> attribute.
	 * @return the content - The content attached to this  component.
	 */
	public String getContent(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CONTENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.content</code> attribute.
	 * @return the content - The content attached to this  component.
	 */
	public String getContent()
	{
		return getContent( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.content</code> attribute. 
	 * @param value the content - The content attached to this  component.
	 */
	public void setContent(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CONTENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.content</code> attribute. 
	 * @param value the content - The content attached to this  component.
	 */
	public void setContent(final String value)
	{
		setContent( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.contentColor</code> attribute.
	 * @return the contentColor - The content color attached to this  component.
	 */
	public String getContentColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CONTENTCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.contentColor</code> attribute.
	 * @return the contentColor - The content color attached to this  component.
	 */
	public String getContentColor()
	{
		return getContentColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.contentColor</code> attribute. 
	 * @param value the contentColor - The content color attached to this  component.
	 */
	public void setContentColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CONTENTCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.contentColor</code> attribute. 
	 * @param value the contentColor - The content color attached to this  component.
	 */
	public void setContentColor(final String value)
	{
		setContentColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.ctaColor</code> attribute.
	 * @return the ctaColor - The cta button color attached to this image component.
	 */
	public String getCtaColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTACOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.ctaColor</code> attribute.
	 * @return the ctaColor - The cta button color attached to this image component.
	 */
	public String getCtaColor()
	{
		return getCtaColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.ctaColor</code> attribute. 
	 * @param value the ctaColor - The cta button color attached to this image component.
	 */
	public void setCtaColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTACOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.ctaColor</code> attribute. 
	 * @param value the ctaColor - The cta button color attached to this image component.
	 */
	public void setCtaColor(final String value)
	{
		setCtaColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.ctaText</code> attribute.
	 * @return the ctaText - The cta text  attached to this image component.
	 */
	public String getCtaText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CTATEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.ctaText</code> attribute.
	 * @return the ctaText - The cta text  attached to this image component.
	 */
	public String getCtaText()
	{
		return getCtaText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.ctaText</code> attribute. 
	 * @param value the ctaText - The cta text  attached to this image component.
	 */
	public void setCtaText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CTATEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.ctaText</code> attribute. 
	 * @param value the ctaText - The cta text  attached to this image component.
	 */
	public void setCtaText(final String value)
	{
		setCtaText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.headerColor</code> attribute.
	 * @return the headerColor - The component header color attached to this  component.
	 */
	public String getHeaderColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HEADERCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.headerColor</code> attribute.
	 * @return the headerColor - The component header color attached to this  component.
	 */
	public String getHeaderColor()
	{
		return getHeaderColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.headerColor</code> attribute. 
	 * @param value the headerColor - The component header color attached to this  component.
	 */
	public void setHeaderColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HEADERCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.headerColor</code> attribute. 
	 * @param value the headerColor - The component header color attached to this  component.
	 */
	public void setHeaderColor(final String value)
	{
		setHeaderColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.headline</code> attribute.
	 * @return the headline - The component header attached to this  component.
	 */
	public String getHeadline(final SessionContext ctx)
	{
		return (String)getProperty( ctx, HEADLINE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.headline</code> attribute.
	 * @return the headline - The component header attached to this  component.
	 */
	public String getHeadline()
	{
		return getHeadline( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.headline</code> attribute. 
	 * @param value the headline - The component header attached to this  component.
	 */
	public void setHeadline(final SessionContext ctx, final String value)
	{
		setProperty(ctx, HEADLINE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.headline</code> attribute. 
	 * @param value the headline - The component header attached to this  component.
	 */
	public void setHeadline(final String value)
	{
		setHeadline( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.imageAlignment</code> attribute.
	 * @return the imageAlignment - The background color for this  component.
	 */
	public String getImageAlignment(final SessionContext ctx)
	{
		return (String)getProperty( ctx, IMAGEALIGNMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.imageAlignment</code> attribute.
	 * @return the imageAlignment - The background color for this  component.
	 */
	public String getImageAlignment()
	{
		return getImageAlignment( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.imageAlignment</code> attribute. 
	 * @param value the imageAlignment - The background color for this  component.
	 */
	public void setImageAlignment(final SessionContext ctx, final String value)
	{
		setProperty(ctx, IMAGEALIGNMENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.imageAlignment</code> attribute. 
	 * @param value the imageAlignment - The background color for this  component.
	 */
	public void setImageAlignment(final String value)
	{
		setImageAlignment( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.imageLink</code> attribute.
	 * @return the imageLink - The image link attribute for this  component.
	 */
	public String getImageLink(final SessionContext ctx)
	{
		return (String)getProperty( ctx, IMAGELINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.imageLink</code> attribute.
	 * @return the imageLink - The image link attribute for this  component.
	 */
	public String getImageLink()
	{
		return getImageLink( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.imageLink</code> attribute. 
	 * @param value the imageLink - The image link attribute for this  component.
	 */
	public void setImageLink(final SessionContext ctx, final String value)
	{
		setProperty(ctx, IMAGELINK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.imageLink</code> attribute. 
	 * @param value the imageLink - The image link attribute for this  component.
	 */
	public void setImageLink(final String value)
	{
		setImageLink( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.imageTile</code> attribute.
	 * @return the imageTile
	 */
	public Collection<GPImageTileComponent> getImageTile(final SessionContext ctx)
	{
		final List<GPImageTileComponent> items = getLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPIMAGETILE2GPBANNERRELATION,
			"GPImageTileComponent",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.imageTile</code> attribute.
	 * @return the imageTile
	 */
	public Collection<GPImageTileComponent> getImageTile()
	{
		return getImageTile( getSession().getSessionContext() );
	}
	
	public long getImageTileCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPIMAGETILE2GPBANNERRELATION,
			"GPImageTileComponent",
			null
		);
	}
	
	public long getImageTileCount()
	{
		return getImageTileCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.imageTile</code> attribute. 
	 * @param value the imageTile
	 */
	public void setImageTile(final SessionContext ctx, final Collection<GPImageTileComponent> value)
	{
		setLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPIMAGETILE2GPBANNERRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(GPIMAGETILE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.imageTile</code> attribute. 
	 * @param value the imageTile
	 */
	public void setImageTile(final Collection<GPImageTileComponent> value)
	{
		setImageTile( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to imageTile. 
	 * @param value the item to add to imageTile
	 */
	public void addToImageTile(final SessionContext ctx, final GPImageTileComponent value)
	{
		addLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPIMAGETILE2GPBANNERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPIMAGETILE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to imageTile. 
	 * @param value the item to add to imageTile
	 */
	public void addToImageTile(final GPImageTileComponent value)
	{
		addToImageTile( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from imageTile. 
	 * @param value the item to remove from imageTile
	 */
	public void removeFromImageTile(final SessionContext ctx, final GPImageTileComponent value)
	{
		removeLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPIMAGETILE2GPBANNERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPIMAGETILE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from imageTile. 
	 * @param value the item to remove from imageTile
	 */
	public void removeFromImageTile(final GPImageTileComponent value)
	{
		removeFromImageTile( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isExternalImageLink</code> attribute.
	 * @return the isExternalImageLink
	 */
	public Boolean isIsExternalImageLink(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ISEXTERNALIMAGELINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isExternalImageLink</code> attribute.
	 * @return the isExternalImageLink
	 */
	public Boolean isIsExternalImageLink()
	{
		return isIsExternalImageLink( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isExternalImageLink</code> attribute. 
	 * @return the isExternalImageLink
	 */
	public boolean isIsExternalImageLinkAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsExternalImageLink( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isExternalImageLink</code> attribute. 
	 * @return the isExternalImageLink
	 */
	public boolean isIsExternalImageLinkAsPrimitive()
	{
		return isIsExternalImageLinkAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isExternalImageLink</code> attribute. 
	 * @param value the isExternalImageLink
	 */
	public void setIsExternalImageLink(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ISEXTERNALIMAGELINK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isExternalImageLink</code> attribute. 
	 * @param value the isExternalImageLink
	 */
	public void setIsExternalImageLink(final Boolean value)
	{
		setIsExternalImageLink( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isExternalImageLink</code> attribute. 
	 * @param value the isExternalImageLink
	 */
	public void setIsExternalImageLink(final SessionContext ctx, final boolean value)
	{
		setIsExternalImageLink( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isExternalImageLink</code> attribute. 
	 * @param value the isExternalImageLink
	 */
	public void setIsExternalImageLink(final boolean value)
	{
		setIsExternalImageLink( getSession().getSessionContext(), value );
	}
	
	@Override
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("GPImageTileComponent");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(GPIMAGETILE2GPBANNERRELATION_MARKMODIFIED);
		}
		ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("GpMarketingSideBySideComponent");
		if(relationSecondEnd1.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isVideo</code> attribute.
	 * @return the isVideo - The boolean for background video for this  component.
	 */
	public Boolean isIsVideo(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ISVIDEO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isVideo</code> attribute.
	 * @return the isVideo - The boolean for background video for this  component.
	 */
	public Boolean isIsVideo()
	{
		return isIsVideo( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isVideo</code> attribute. 
	 * @return the isVideo - The boolean for background video for this  component.
	 */
	public boolean isIsVideoAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsVideo( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isVideo</code> attribute. 
	 * @return the isVideo - The boolean for background video for this  component.
	 */
	public boolean isIsVideoAsPrimitive()
	{
		return isIsVideoAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isVideo</code> attribute. 
	 * @param value the isVideo - The boolean for background video for this  component.
	 */
	public void setIsVideo(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ISVIDEO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isVideo</code> attribute. 
	 * @param value the isVideo - The boolean for background video for this  component.
	 */
	public void setIsVideo(final Boolean value)
	{
		setIsVideo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isVideo</code> attribute. 
	 * @param value the isVideo - The boolean for background video for this  component.
	 */
	public void setIsVideo(final SessionContext ctx, final boolean value)
	{
		setIsVideo( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isVideo</code> attribute. 
	 * @param value the isVideo - The boolean for background video for this  component.
	 */
	public void setIsVideo(final boolean value)
	{
		setIsVideo( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isVideoCtaLink</code> attribute.
	 * @return the isVideoCtaLink - The boolean for background CTA video for this  component.
	 */
	public Boolean isIsVideoCtaLink(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ISVIDEOCTALINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isVideoCtaLink</code> attribute.
	 * @return the isVideoCtaLink - The boolean for background CTA video for this  component.
	 */
	public Boolean isIsVideoCtaLink()
	{
		return isIsVideoCtaLink( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isVideoCtaLink</code> attribute. 
	 * @return the isVideoCtaLink - The boolean for background CTA video for this  component.
	 */
	public boolean isIsVideoCtaLinkAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsVideoCtaLink( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.isVideoCtaLink</code> attribute. 
	 * @return the isVideoCtaLink - The boolean for background CTA video for this  component.
	 */
	public boolean isIsVideoCtaLinkAsPrimitive()
	{
		return isIsVideoCtaLinkAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isVideoCtaLink</code> attribute. 
	 * @param value the isVideoCtaLink - The boolean for background CTA video for this  component.
	 */
	public void setIsVideoCtaLink(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ISVIDEOCTALINK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isVideoCtaLink</code> attribute. 
	 * @param value the isVideoCtaLink - The boolean for background CTA video for this  component.
	 */
	public void setIsVideoCtaLink(final Boolean value)
	{
		setIsVideoCtaLink( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isVideoCtaLink</code> attribute. 
	 * @param value the isVideoCtaLink - The boolean for background CTA video for this  component.
	 */
	public void setIsVideoCtaLink(final SessionContext ctx, final boolean value)
	{
		setIsVideoCtaLink( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.isVideoCtaLink</code> attribute. 
	 * @param value the isVideoCtaLink - The boolean for background CTA video for this  component.
	 */
	public void setIsVideoCtaLink(final boolean value)
	{
		setIsVideoCtaLink( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.marketingSide</code> attribute.
	 * @return the marketingSide
	 */
	public Collection<GPMarketingSidebySideComponent> getMarketingSide(final SessionContext ctx)
	{
		final List<GPMarketingSidebySideComponent> items = getLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPMARKETINGSIDEBYSIDE2GPBANNERRELATION,
			"GPMarketingSidebySideComponent",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.marketingSide</code> attribute.
	 * @return the marketingSide
	 */
	public Collection<GPMarketingSidebySideComponent> getMarketingSide()
	{
		return getMarketingSide( getSession().getSessionContext() );
	}
	
	public long getMarketingSideCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPMARKETINGSIDEBYSIDE2GPBANNERRELATION,
			"GPMarketingSidebySideComponent",
			null
		);
	}
	
	public long getMarketingSideCount()
	{
		return getMarketingSideCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.marketingSide</code> attribute. 
	 * @param value the marketingSide
	 */
	public void setMarketingSide(final SessionContext ctx, final Collection<GPMarketingSidebySideComponent> value)
	{
		setLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPMARKETINGSIDEBYSIDE2GPBANNERRELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.marketingSide</code> attribute. 
	 * @param value the marketingSide
	 */
	public void setMarketingSide(final Collection<GPMarketingSidebySideComponent> value)
	{
		setMarketingSide( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to marketingSide. 
	 * @param value the item to add to marketingSide
	 */
	public void addToMarketingSide(final SessionContext ctx, final GPMarketingSidebySideComponent value)
	{
		addLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPMARKETINGSIDEBYSIDE2GPBANNERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to marketingSide. 
	 * @param value the item to add to marketingSide
	 */
	public void addToMarketingSide(final GPMarketingSidebySideComponent value)
	{
		addToMarketingSide( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from marketingSide. 
	 * @param value the item to remove from marketingSide
	 */
	public void removeFromMarketingSide(final SessionContext ctx, final GPMarketingSidebySideComponent value)
	{
		removeLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPMARKETINGSIDEBYSIDE2GPBANNERRELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(GPMARKETINGSIDEBYSIDE2GPBANNERRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from marketingSide. 
	 * @param value the item to remove from marketingSide
	 */
	public void removeFromMarketingSide(final GPMarketingSidebySideComponent value)
	{
		removeFromMarketingSide( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.mediaForText</code> attribute.
	 * @return the mediaForText - It is a media container containing images for specific resolutions for text component
	 */
	public GPMediaComponent getMediaForText(final SessionContext ctx)
	{
		return (GPMediaComponent)getProperty( ctx, MEDIAFORTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.mediaForText</code> attribute.
	 * @return the mediaForText - It is a media container containing images for specific resolutions for text component
	 */
	public GPMediaComponent getMediaForText()
	{
		return getMediaForText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.mediaForText</code> attribute. 
	 * @param value the mediaForText - It is a media container containing images for specific resolutions for text component
	 */
	public void setMediaForText(final SessionContext ctx, final GPMediaComponent value)
	{
		setProperty(ctx, MEDIAFORTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.mediaForText</code> attribute. 
	 * @param value the mediaForText - It is a media container containing images for specific resolutions for text component
	 */
	public void setMediaForText(final GPMediaComponent value)
	{
		setMediaForText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.playIconColor</code> attribute.
	 * @return the playIconColor - The play icon for background video for this  component.
	 */
	public String getPlayIconColor(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PLAYICONCOLOR);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.playIconColor</code> attribute.
	 * @return the playIconColor - The play icon for background video for this  component.
	 */
	public String getPlayIconColor()
	{
		return getPlayIconColor( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.playIconColor</code> attribute. 
	 * @param value the playIconColor - The play icon for background video for this  component.
	 */
	public void setPlayIconColor(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PLAYICONCOLOR,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.playIconColor</code> attribute. 
	 * @param value the playIconColor - The play icon for background video for this  component.
	 */
	public void setPlayIconColor(final String value)
	{
		setPlayIconColor( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.textalignment</code> attribute.
	 * @return the textalignment - The alignment of the text in this component.
	 */
	public String getTextalignment(final SessionContext ctx)
	{
		return (String)getProperty( ctx, TEXTALIGNMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPBannerComponent.textalignment</code> attribute.
	 * @return the textalignment - The alignment of the text in this component.
	 */
	public String getTextalignment()
	{
		return getTextalignment( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.textalignment</code> attribute. 
	 * @param value the textalignment - The alignment of the text in this component.
	 */
	public void setTextalignment(final SessionContext ctx, final String value)
	{
		setProperty(ctx, TEXTALIGNMENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPBannerComponent.textalignment</code> attribute. 
	 * @param value the textalignment - The alignment of the text in this component.
	 */
	public void setTextalignment(final String value)
	{
		setTextalignment( getSession().getSessionContext(), value );
	}
	
}
