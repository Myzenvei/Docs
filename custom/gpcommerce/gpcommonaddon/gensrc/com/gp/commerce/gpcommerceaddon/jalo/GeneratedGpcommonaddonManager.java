/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.gpcommerceaddon.jalo;

import com.gp.commerce.core.jalo.GPBannerComponent;
import com.gp.commerce.core.jalo.GPBrandBarComponent;
import com.gp.commerce.core.jalo.GPBundleImageComponent;
import com.gp.commerce.core.jalo.GPHeaderNavigationComponent;
import com.gp.commerce.core.jalo.GPImageComponent;
import com.gp.commerce.core.jalo.GPImageLinkComponent;
import com.gp.commerce.core.jalo.GPImageTextComponent;
import com.gp.commerce.core.jalo.GPImageTileComponent;
import com.gp.commerce.core.jalo.GPMarketingCategories;
import com.gp.commerce.core.jalo.GPMarketingComponent;
import com.gp.commerce.core.jalo.GPMarketingSidebySideComponent;
import com.gp.commerce.core.jalo.GPMediaComponent;
import com.gp.commerce.core.jalo.GPMediaContainerlink;
import com.gp.commerce.core.jalo.GPPreCuratedListComponent;
import com.gp.commerce.core.jalo.GPProductCarouselComponent;
import com.gp.commerce.core.jalo.GPProductReferencesComponent;
import com.gp.commerce.core.jalo.GPProductSolutionCMSComponent;
import com.gp.commerce.core.jalo.GPRotatingImagesComponent;
import com.gp.commerce.core.jalo.GPTabbedImageTileComponent;
import com.gp.commerce.core.jalo.GPTipsAndKnowledgeInfo;
import com.gp.commerce.core.jalo.GPTipsAndKnowledgeInfoComponent;
import com.gp.commerce.core.jalo.GPVideoBannerComponent;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import com.gp.commerce.gpcommerceaddon.jalo.GPBundleCarouselComponent;
import com.gp.commerce.gpcommerceaddon.jalo.GPCheckoutFooterNavigationComponent;
import com.gp.commerce.gpcommerceaddon.jalo.GPFAQCMSParagraphComponent;
import com.gp.commerce.gpcommerceaddon.jalo.GPFeatureProductsComponent;
import com.gp.commerce.gpcommerceaddon.jalo.GPIndustrialSolutionsComponent;
import com.gp.commerce.gpcommerceaddon.jalo.GPJuicerFeedComponent;
import com.gp.commerce.gpcommerceaddon.jalo.GPLiveChatComponent;
import com.gp.commerce.gpcommerceaddon.jalo.GPNavigationComponent;
import com.gp.commerce.gpcommerceaddon.jalo.GPQuplesComponent;
import com.gp.commerce.gpcommerceaddon.jalo.GPUnsubscribeComponent;
import com.gp.commerce.gpcommerceaddon.jalo.GPXpressHeaderNavigationComponent;
import com.gp.commerce.gpcommerceaddon.jalo.MardiGrasFAQComponent;
import com.gp.commerce.gpcommerceaddon.jalo.MardiGrasFooterComponent;
import com.gp.commerce.gpcommerceaddon.jalo.MardiGrasHeroBannerComponent;
import com.gp.commerce.gpcommerceaddon.jalo.MardiGrasImageAndTextComponent;
import com.gp.commerce.gpcommerceaddon.jalo.MardiGrasStoreLocatorComponent;
import com.gp.commerce.gpcommerceaddon.jalo.MardigrasBoredomComponent;
import com.gp.commerce.gpcommerceaddon.jalo.MardigrasCouponsComponent;
import com.gp.commerce.gpcommerceaddon.jalo.MardigrasHeaderNavigationComponent;
import com.gp.commerce.gpcommerceaddon.jalo.MardigrasOurPacksComponent;
import com.gp.commerce.gpcommerceaddon.jalo.MardigrasVideosComponent;
import com.gp.commerce.gpcommerceaddon.jalo.MaridgrasNapkinsComponent;
import de.hybris.platform.acceleratorcms.jalo.components.AbstractResponsiveBannerComponent;
import de.hybris.platform.acceleratorcms.jalo.components.FooterNavigationComponent;
import de.hybris.platform.acceleratorcms.jalo.components.NavigationComponent;
import de.hybris.platform.acceleratorcms.jalo.components.SimpleResponsiveBannerComponent;
import de.hybris.platform.cms2.jalo.contents.CMSItem;
import de.hybris.platform.cms2.jalo.contents.components.AbstractCMSComponent;
import de.hybris.platform.cms2.jalo.contents.components.CMSLinkComponent;
import de.hybris.platform.cms2.jalo.contents.components.SimpleCMSComponent;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type <code>GpcommonaddonManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGpcommonaddonManager extends Extension
{
	/** Relation ordering override parameter constants for Rotating2simpleResponsiveRelation from ((gpcommonaddon))*/
	protected static String ROTATING2SIMPLERESPONSIVERELATION_SRC_ORDERED = "relation.Rotating2simpleResponsiveRelation.source.ordered";
	protected static String ROTATING2SIMPLERESPONSIVERELATION_TGT_ORDERED = "relation.Rotating2simpleResponsiveRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for Rotating2simpleResponsiveRelation from ((gpcommonaddon))*/
	protected static String ROTATING2SIMPLERESPONSIVERELATION_MARKMODIFIED = "relation.Rotating2simpleResponsiveRelation.markmodified";
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("markAsViewAll", AttributeMode.INITIAL);
		tmp.put("socialLink", AttributeMode.INITIAL);
		tmp.put("linkType", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.cms2.jalo.contents.components.CMSLinkComponent", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("compStyleDesktop", AttributeMode.INITIAL);
		tmp.put("compStyleMobile", AttributeMode.INITIAL);
		tmp.put("isExternalLink", AttributeMode.INITIAL);
		tmp.put("componentTheme", AttributeMode.INITIAL);
		tmp.put("ctaStyle", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.cms2.jalo.contents.components.AbstractCMSComponent", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("footerLogo", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.acceleratorcms.jalo.components.FooterNavigationComponent", Collections.unmodifiableMap(tmp));
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
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.componentTheme</code> attribute.
	 * @return the componentTheme - component theme
	 */
	public String getComponentTheme(final SessionContext ctx, final AbstractCMSComponent item)
	{
		return (String)item.getProperty( ctx, GpcommonaddonConstants.Attributes.AbstractCMSComponent.COMPONENTTHEME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.componentTheme</code> attribute.
	 * @return the componentTheme - component theme
	 */
	public String getComponentTheme(final AbstractCMSComponent item)
	{
		return getComponentTheme( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.componentTheme</code> attribute. 
	 * @param value the componentTheme - component theme
	 */
	public void setComponentTheme(final SessionContext ctx, final AbstractCMSComponent item, final String value)
	{
		item.setProperty(ctx, GpcommonaddonConstants.Attributes.AbstractCMSComponent.COMPONENTTHEME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.componentTheme</code> attribute. 
	 * @param value the componentTheme - component theme
	 */
	public void setComponentTheme(final AbstractCMSComponent item, final String value)
	{
		setComponentTheme( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.compStyleDesktop</code> attribute.
	 * @return the compStyleDesktop - css style to define width for desktop
	 */
	public String getCompStyleDesktop(final SessionContext ctx, final AbstractCMSComponent item)
	{
		return (String)item.getProperty( ctx, GpcommonaddonConstants.Attributes.AbstractCMSComponent.COMPSTYLEDESKTOP);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.compStyleDesktop</code> attribute.
	 * @return the compStyleDesktop - css style to define width for desktop
	 */
	public String getCompStyleDesktop(final AbstractCMSComponent item)
	{
		return getCompStyleDesktop( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.compStyleDesktop</code> attribute. 
	 * @param value the compStyleDesktop - css style to define width for desktop
	 */
	public void setCompStyleDesktop(final SessionContext ctx, final AbstractCMSComponent item, final String value)
	{
		item.setProperty(ctx, GpcommonaddonConstants.Attributes.AbstractCMSComponent.COMPSTYLEDESKTOP,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.compStyleDesktop</code> attribute. 
	 * @param value the compStyleDesktop - css style to define width for desktop
	 */
	public void setCompStyleDesktop(final AbstractCMSComponent item, final String value)
	{
		setCompStyleDesktop( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.compStyleMobile</code> attribute.
	 * @return the compStyleMobile - css style to define width for mobile
	 */
	public String getCompStyleMobile(final SessionContext ctx, final AbstractCMSComponent item)
	{
		return (String)item.getProperty( ctx, GpcommonaddonConstants.Attributes.AbstractCMSComponent.COMPSTYLEMOBILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.compStyleMobile</code> attribute.
	 * @return the compStyleMobile - css style to define width for mobile
	 */
	public String getCompStyleMobile(final AbstractCMSComponent item)
	{
		return getCompStyleMobile( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.compStyleMobile</code> attribute. 
	 * @param value the compStyleMobile - css style to define width for mobile
	 */
	public void setCompStyleMobile(final SessionContext ctx, final AbstractCMSComponent item, final String value)
	{
		item.setProperty(ctx, GpcommonaddonConstants.Attributes.AbstractCMSComponent.COMPSTYLEMOBILE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.compStyleMobile</code> attribute. 
	 * @param value the compStyleMobile - css style to define width for mobile
	 */
	public void setCompStyleMobile(final AbstractCMSComponent item, final String value)
	{
		setCompStyleMobile( getSession().getSessionContext(), item, value );
	}
	
	public GPBannerComponent createGPBannerComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPBANNERCOMPONENT );
			return (GPBannerComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPBannerComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPBannerComponent createGPBannerComponent(final Map attributeValues)
	{
		return createGPBannerComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPBrandBarComponent createGPBrandBarComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPBRANDBARCOMPONENT );
			return (GPBrandBarComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPBrandBarComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPBrandBarComponent createGPBrandBarComponent(final Map attributeValues)
	{
		return createGPBrandBarComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPBundleCarouselComponent createGPBundleCarouselComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPBUNDLECAROUSELCOMPONENT );
			return (GPBundleCarouselComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPBundleCarouselComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPBundleCarouselComponent createGPBundleCarouselComponent(final Map attributeValues)
	{
		return createGPBundleCarouselComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPBundleImageComponent createGPBundleImageComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPBUNDLEIMAGECOMPONENT );
			return (GPBundleImageComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPBundleImageComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPBundleImageComponent createGPBundleImageComponent(final Map attributeValues)
	{
		return createGPBundleImageComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPCheckoutFooterNavigationComponent createGPCheckoutFooterNavigationComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPCHECKOUTFOOTERNAVIGATIONCOMPONENT );
			return (GPCheckoutFooterNavigationComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPCheckoutFooterNavigationComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPCheckoutFooterNavigationComponent createGPCheckoutFooterNavigationComponent(final Map attributeValues)
	{
		return createGPCheckoutFooterNavigationComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPFAQCMSParagraphComponent createGPFAQCMSParagraphComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPFAQCMSPARAGRAPHCOMPONENT );
			return (GPFAQCMSParagraphComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPFAQCMSParagraphComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPFAQCMSParagraphComponent createGPFAQCMSParagraphComponent(final Map attributeValues)
	{
		return createGPFAQCMSParagraphComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPFeatureProductsComponent createGPFeatureProductsComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPFEATUREPRODUCTSCOMPONENT );
			return (GPFeatureProductsComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPFeatureProductsComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPFeatureProductsComponent createGPFeatureProductsComponent(final Map attributeValues)
	{
		return createGPFeatureProductsComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPHeaderNavigationComponent createGPHeaderNavigationComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPHEADERNAVIGATIONCOMPONENT );
			return (GPHeaderNavigationComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPHeaderNavigationComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPHeaderNavigationComponent createGPHeaderNavigationComponent(final Map attributeValues)
	{
		return createGPHeaderNavigationComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPImageComponent createGPImageComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPIMAGECOMPONENT );
			return (GPImageComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPImageComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPImageComponent createGPImageComponent(final Map attributeValues)
	{
		return createGPImageComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPImageLinkComponent createGPImageLinkComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPIMAGELINKCOMPONENT );
			return (GPImageLinkComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPImageLinkComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPImageLinkComponent createGPImageLinkComponent(final Map attributeValues)
	{
		return createGPImageLinkComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPImageTextComponent createGPImageTextComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPIMAGETEXTCOMPONENT );
			return (GPImageTextComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPImageTextComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPImageTextComponent createGPImageTextComponent(final Map attributeValues)
	{
		return createGPImageTextComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPImageTileComponent createGPImageTileComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPIMAGETILECOMPONENT );
			return (GPImageTileComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPImageTileComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPImageTileComponent createGPImageTileComponent(final Map attributeValues)
	{
		return createGPImageTileComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPIndustrialSolutionsComponent createGPIndustrialSolutionsComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPINDUSTRIALSOLUTIONSCOMPONENT );
			return (GPIndustrialSolutionsComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPIndustrialSolutionsComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPIndustrialSolutionsComponent createGPIndustrialSolutionsComponent(final Map attributeValues)
	{
		return createGPIndustrialSolutionsComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPJuicerFeedComponent createGPJuicerFeedComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPJUICERFEEDCOMPONENT );
			return (GPJuicerFeedComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPJuicerFeedComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPJuicerFeedComponent createGPJuicerFeedComponent(final Map attributeValues)
	{
		return createGPJuicerFeedComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPLiveChatComponent createGPLiveChatComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPLIVECHATCOMPONENT );
			return (GPLiveChatComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPLiveChatComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPLiveChatComponent createGPLiveChatComponent(final Map attributeValues)
	{
		return createGPLiveChatComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPMarketingCategories createGPMarketingCategories(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPMARKETINGCATEGORIES );
			return (GPMarketingCategories)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPMarketingCategories : "+e.getMessage(), 0 );
		}
	}
	
	public GPMarketingCategories createGPMarketingCategories(final Map attributeValues)
	{
		return createGPMarketingCategories( getSession().getSessionContext(), attributeValues );
	}
	
	public GPMarketingComponent createGPMarketingComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPMARKETINGCOMPONENT );
			return (GPMarketingComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPMarketingComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPMarketingComponent createGPMarketingComponent(final Map attributeValues)
	{
		return createGPMarketingComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPMarketingSidebySideComponent createGPMarketingSidebySideComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPMARKETINGSIDEBYSIDECOMPONENT );
			return (GPMarketingSidebySideComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPMarketingSidebySideComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPMarketingSidebySideComponent createGPMarketingSidebySideComponent(final Map attributeValues)
	{
		return createGPMarketingSidebySideComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPMediaComponent createGPMediaComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPMEDIACOMPONENT );
			return (GPMediaComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPMediaComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPMediaComponent createGPMediaComponent(final Map attributeValues)
	{
		return createGPMediaComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPMediaContainerlink createGPMediaContainerlink(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPMEDIACONTAINERLINK );
			return (GPMediaContainerlink)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPMediaContainerlink : "+e.getMessage(), 0 );
		}
	}
	
	public GPMediaContainerlink createGPMediaContainerlink(final Map attributeValues)
	{
		return createGPMediaContainerlink( getSession().getSessionContext(), attributeValues );
	}
	
	public GPNavigationComponent createGPNavigationComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPNAVIGATIONCOMPONENT );
			return (GPNavigationComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPNavigationComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPNavigationComponent createGPNavigationComponent(final Map attributeValues)
	{
		return createGPNavigationComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPPreCuratedListComponent createGPPreCuratedListComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPPRECURATEDLISTCOMPONENT );
			return (GPPreCuratedListComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPPreCuratedListComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPPreCuratedListComponent createGPPreCuratedListComponent(final Map attributeValues)
	{
		return createGPPreCuratedListComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPProductCarouselComponent createGPProductCarouselComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPPRODUCTCAROUSELCOMPONENT );
			return (GPProductCarouselComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPProductCarouselComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPProductCarouselComponent createGPProductCarouselComponent(final Map attributeValues)
	{
		return createGPProductCarouselComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPProductReferencesComponent createGPProductReferencesComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPPRODUCTREFERENCESCOMPONENT );
			return (GPProductReferencesComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPProductReferencesComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPProductReferencesComponent createGPProductReferencesComponent(final Map attributeValues)
	{
		return createGPProductReferencesComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPProductSolutionCMSComponent createGPProductSolutionCMSComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPPRODUCTSOLUTIONCMSCOMPONENT );
			return (GPProductSolutionCMSComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPProductSolutionCMSComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPProductSolutionCMSComponent createGPProductSolutionCMSComponent(final Map attributeValues)
	{
		return createGPProductSolutionCMSComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPQuplesComponent createGPQuplesComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPQUPLESCOMPONENT );
			return (GPQuplesComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPQuplesComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPQuplesComponent createGPQuplesComponent(final Map attributeValues)
	{
		return createGPQuplesComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPRotatingImagesComponent createGPRotatingImagesComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPROTATINGIMAGESCOMPONENT );
			return (GPRotatingImagesComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPRotatingImagesComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPRotatingImagesComponent createGPRotatingImagesComponent(final Map attributeValues)
	{
		return createGPRotatingImagesComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPTabbedImageTileComponent createGPTabbedImageTileComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPTABBEDIMAGETILECOMPONENT );
			return (GPTabbedImageTileComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPTabbedImageTileComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPTabbedImageTileComponent createGPTabbedImageTileComponent(final Map attributeValues)
	{
		return createGPTabbedImageTileComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPTipsAndKnowledgeInfo createGPTipsAndKnowledgeInfo(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPTIPSANDKNOWLEDGEINFO );
			return (GPTipsAndKnowledgeInfo)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPTipsAndKnowledgeInfo : "+e.getMessage(), 0 );
		}
	}
	
	public GPTipsAndKnowledgeInfo createGPTipsAndKnowledgeInfo(final Map attributeValues)
	{
		return createGPTipsAndKnowledgeInfo( getSession().getSessionContext(), attributeValues );
	}
	
	public GPTipsAndKnowledgeInfoComponent createGPTipsAndKnowledgeInfoComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPTIPSANDKNOWLEDGEINFOCOMPONENT );
			return (GPTipsAndKnowledgeInfoComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPTipsAndKnowledgeInfoComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPTipsAndKnowledgeInfoComponent createGPTipsAndKnowledgeInfoComponent(final Map attributeValues)
	{
		return createGPTipsAndKnowledgeInfoComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPUnsubscribeComponent createGPUnsubscribeComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPUNSUBSCRIBECOMPONENT );
			return (GPUnsubscribeComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPUnsubscribeComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPUnsubscribeComponent createGPUnsubscribeComponent(final Map attributeValues)
	{
		return createGPUnsubscribeComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPVideoBannerComponent createGPVideoBannerComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPVIDEOBANNERCOMPONENT );
			return (GPVideoBannerComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPVideoBannerComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPVideoBannerComponent createGPVideoBannerComponent(final Map attributeValues)
	{
		return createGPVideoBannerComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public GPXpressHeaderNavigationComponent createGPXpressHeaderNavigationComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.GPXPRESSHEADERNAVIGATIONCOMPONENT );
			return (GPXpressHeaderNavigationComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPXpressHeaderNavigationComponent : "+e.getMessage(), 0 );
		}
	}
	
	public GPXpressHeaderNavigationComponent createGPXpressHeaderNavigationComponent(final Map attributeValues)
	{
		return createGPXpressHeaderNavigationComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public MardigrasBoredomComponent createMardigrasBoredomComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.MARDIGRASBOREDOMCOMPONENT );
			return (MardigrasBoredomComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MardigrasBoredomComponent : "+e.getMessage(), 0 );
		}
	}
	
	public MardigrasBoredomComponent createMardigrasBoredomComponent(final Map attributeValues)
	{
		return createMardigrasBoredomComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public MardigrasCouponsComponent createMardigrasCouponsComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.MARDIGRASCOUPONSCOMPONENT );
			return (MardigrasCouponsComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MardigrasCouponsComponent : "+e.getMessage(), 0 );
		}
	}
	
	public MardigrasCouponsComponent createMardigrasCouponsComponent(final Map attributeValues)
	{
		return createMardigrasCouponsComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public MardiGrasFAQComponent createMardiGrasFAQComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.MARDIGRASFAQCOMPONENT );
			return (MardiGrasFAQComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MardiGrasFAQComponent : "+e.getMessage(), 0 );
		}
	}
	
	public MardiGrasFAQComponent createMardiGrasFAQComponent(final Map attributeValues)
	{
		return createMardiGrasFAQComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public MardiGrasFooterComponent createMardiGrasFooterComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.MARDIGRASFOOTERCOMPONENT );
			return (MardiGrasFooterComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MardiGrasFooterComponent : "+e.getMessage(), 0 );
		}
	}
	
	public MardiGrasFooterComponent createMardiGrasFooterComponent(final Map attributeValues)
	{
		return createMardiGrasFooterComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public MardigrasHeaderNavigationComponent createMardigrasHeaderNavigationComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.MARDIGRASHEADERNAVIGATIONCOMPONENT );
			return (MardigrasHeaderNavigationComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MardigrasHeaderNavigationComponent : "+e.getMessage(), 0 );
		}
	}
	
	public MardigrasHeaderNavigationComponent createMardigrasHeaderNavigationComponent(final Map attributeValues)
	{
		return createMardigrasHeaderNavigationComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public MardiGrasHeroBannerComponent createMardiGrasHeroBannerComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.MARDIGRASHEROBANNERCOMPONENT );
			return (MardiGrasHeroBannerComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MardiGrasHeroBannerComponent : "+e.getMessage(), 0 );
		}
	}
	
	public MardiGrasHeroBannerComponent createMardiGrasHeroBannerComponent(final Map attributeValues)
	{
		return createMardiGrasHeroBannerComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public MardiGrasImageAndTextComponent createMardiGrasImageAndTextComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.MARDIGRASIMAGEANDTEXTCOMPONENT );
			return (MardiGrasImageAndTextComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MardiGrasImageAndTextComponent : "+e.getMessage(), 0 );
		}
	}
	
	public MardiGrasImageAndTextComponent createMardiGrasImageAndTextComponent(final Map attributeValues)
	{
		return createMardiGrasImageAndTextComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public MardigrasOurPacksComponent createMardigrasOurPacksComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.MARDIGRASOURPACKSCOMPONENT );
			return (MardigrasOurPacksComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MardigrasOurPacksComponent : "+e.getMessage(), 0 );
		}
	}
	
	public MardigrasOurPacksComponent createMardigrasOurPacksComponent(final Map attributeValues)
	{
		return createMardigrasOurPacksComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public MardiGrasStoreLocatorComponent createMardiGrasStoreLocatorComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.MARDIGRASSTORELOCATORCOMPONENT );
			return (MardiGrasStoreLocatorComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MardiGrasStoreLocatorComponent : "+e.getMessage(), 0 );
		}
	}
	
	public MardiGrasStoreLocatorComponent createMardiGrasStoreLocatorComponent(final Map attributeValues)
	{
		return createMardiGrasStoreLocatorComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public MardigrasVideosComponent createMardigrasVideosComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.MARDIGRASVIDEOSCOMPONENT );
			return (MardigrasVideosComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MardigrasVideosComponent : "+e.getMessage(), 0 );
		}
	}
	
	public MardigrasVideosComponent createMardigrasVideosComponent(final Map attributeValues)
	{
		return createMardigrasVideosComponent( getSession().getSessionContext(), attributeValues );
	}
	
	public MaridgrasNapkinsComponent createMaridgrasNapkinsComponent(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpcommonaddonConstants.TC.MARIDGRASNAPKINSCOMPONENT );
			return (MaridgrasNapkinsComponent)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating MaridgrasNapkinsComponent : "+e.getMessage(), 0 );
		}
	}
	
	public MaridgrasNapkinsComponent createMaridgrasNapkinsComponent(final Map attributeValues)
	{
		return createMaridgrasNapkinsComponent( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.ctaStyle</code> attribute.
	 * @return the ctaStyle - cta style to define width for desktop
	 */
	public String getCtaStyle(final SessionContext ctx, final AbstractCMSComponent item)
	{
		return (String)item.getProperty( ctx, GpcommonaddonConstants.Attributes.AbstractCMSComponent.CTASTYLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.ctaStyle</code> attribute.
	 * @return the ctaStyle - cta style to define width for desktop
	 */
	public String getCtaStyle(final AbstractCMSComponent item)
	{
		return getCtaStyle( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.ctaStyle</code> attribute. 
	 * @param value the ctaStyle - cta style to define width for desktop
	 */
	public void setCtaStyle(final SessionContext ctx, final AbstractCMSComponent item, final String value)
	{
		item.setProperty(ctx, GpcommonaddonConstants.Attributes.AbstractCMSComponent.CTASTYLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.ctaStyle</code> attribute. 
	 * @param value the ctaStyle - cta style to define width for desktop
	 */
	public void setCtaStyle(final AbstractCMSComponent item, final String value)
	{
		setCtaStyle( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.footerLogo</code> attribute.
	 * @return the footerLogo
	 */
	public GPImageLinkComponent getFooterLogo(final SessionContext ctx, final FooterNavigationComponent item)
	{
		return (GPImageLinkComponent)item.getProperty( ctx, GpcommonaddonConstants.Attributes.FooterNavigationComponent.FOOTERLOGO);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>FooterNavigationComponent.footerLogo</code> attribute.
	 * @return the footerLogo
	 */
	public GPImageLinkComponent getFooterLogo(final FooterNavigationComponent item)
	{
		return getFooterLogo( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FooterNavigationComponent.footerLogo</code> attribute. 
	 * @param value the footerLogo
	 */
	public void setFooterLogo(final SessionContext ctx, final FooterNavigationComponent item, final GPImageLinkComponent value)
	{
		item.setProperty(ctx, GpcommonaddonConstants.Attributes.FooterNavigationComponent.FOOTERLOGO,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>FooterNavigationComponent.footerLogo</code> attribute. 
	 * @param value the footerLogo
	 */
	public void setFooterLogo(final FooterNavigationComponent item, final GPImageLinkComponent value)
	{
		setFooterLogo( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return GpcommonaddonConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.isExternalLink</code> attribute.
	 * @return the isExternalLink
	 */
	public Boolean isIsExternalLink(final SessionContext ctx, final AbstractCMSComponent item)
	{
		return (Boolean)item.getProperty( ctx, GpcommonaddonConstants.Attributes.AbstractCMSComponent.ISEXTERNALLINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.isExternalLink</code> attribute.
	 * @return the isExternalLink
	 */
	public Boolean isIsExternalLink(final AbstractCMSComponent item)
	{
		return isIsExternalLink( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.isExternalLink</code> attribute. 
	 * @return the isExternalLink
	 */
	public boolean isIsExternalLinkAsPrimitive(final SessionContext ctx, final AbstractCMSComponent item)
	{
		Boolean value = isIsExternalLink( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractCMSComponent.isExternalLink</code> attribute. 
	 * @return the isExternalLink
	 */
	public boolean isIsExternalLinkAsPrimitive(final AbstractCMSComponent item)
	{
		return isIsExternalLinkAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.isExternalLink</code> attribute. 
	 * @param value the isExternalLink
	 */
	public void setIsExternalLink(final SessionContext ctx, final AbstractCMSComponent item, final Boolean value)
	{
		item.setProperty(ctx, GpcommonaddonConstants.Attributes.AbstractCMSComponent.ISEXTERNALLINK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.isExternalLink</code> attribute. 
	 * @param value the isExternalLink
	 */
	public void setIsExternalLink(final AbstractCMSComponent item, final Boolean value)
	{
		setIsExternalLink( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.isExternalLink</code> attribute. 
	 * @param value the isExternalLink
	 */
	public void setIsExternalLink(final SessionContext ctx, final AbstractCMSComponent item, final boolean value)
	{
		setIsExternalLink( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractCMSComponent.isExternalLink</code> attribute. 
	 * @param value the isExternalLink
	 */
	public void setIsExternalLink(final AbstractCMSComponent item, final boolean value)
	{
		setIsExternalLink( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.linkType</code> attribute.
	 * @return the linkType
	 */
	public String getLinkType(final SessionContext ctx, final CMSLinkComponent item)
	{
		return (String)item.getProperty( ctx, GpcommonaddonConstants.Attributes.CMSLinkComponent.LINKTYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.linkType</code> attribute.
	 * @return the linkType
	 */
	public String getLinkType(final CMSLinkComponent item)
	{
		return getLinkType( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSLinkComponent.linkType</code> attribute. 
	 * @param value the linkType
	 */
	public void setLinkType(final SessionContext ctx, final CMSLinkComponent item, final String value)
	{
		item.setProperty(ctx, GpcommonaddonConstants.Attributes.CMSLinkComponent.LINKTYPE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSLinkComponent.linkType</code> attribute. 
	 * @param value the linkType
	 */
	public void setLinkType(final CMSLinkComponent item, final String value)
	{
		setLinkType( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.markAsViewAll</code> attribute.
	 * @return the markAsViewAll
	 */
	public Boolean isMarkAsViewAll(final SessionContext ctx, final CMSLinkComponent item)
	{
		return (Boolean)item.getProperty( ctx, GpcommonaddonConstants.Attributes.CMSLinkComponent.MARKASVIEWALL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.markAsViewAll</code> attribute.
	 * @return the markAsViewAll
	 */
	public Boolean isMarkAsViewAll(final CMSLinkComponent item)
	{
		return isMarkAsViewAll( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.markAsViewAll</code> attribute. 
	 * @return the markAsViewAll
	 */
	public boolean isMarkAsViewAllAsPrimitive(final SessionContext ctx, final CMSLinkComponent item)
	{
		Boolean value = isMarkAsViewAll( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.markAsViewAll</code> attribute. 
	 * @return the markAsViewAll
	 */
	public boolean isMarkAsViewAllAsPrimitive(final CMSLinkComponent item)
	{
		return isMarkAsViewAllAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSLinkComponent.markAsViewAll</code> attribute. 
	 * @param value the markAsViewAll
	 */
	public void setMarkAsViewAll(final SessionContext ctx, final CMSLinkComponent item, final Boolean value)
	{
		item.setProperty(ctx, GpcommonaddonConstants.Attributes.CMSLinkComponent.MARKASVIEWALL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSLinkComponent.markAsViewAll</code> attribute. 
	 * @param value the markAsViewAll
	 */
	public void setMarkAsViewAll(final CMSLinkComponent item, final Boolean value)
	{
		setMarkAsViewAll( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSLinkComponent.markAsViewAll</code> attribute. 
	 * @param value the markAsViewAll
	 */
	public void setMarkAsViewAll(final SessionContext ctx, final CMSLinkComponent item, final boolean value)
	{
		setMarkAsViewAll( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSLinkComponent.markAsViewAll</code> attribute. 
	 * @param value the markAsViewAll
	 */
	public void setMarkAsViewAll(final CMSLinkComponent item, final boolean value)
	{
		setMarkAsViewAll( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SimpleResponsiveBannerComponent.rotatingBanner</code> attribute.
	 * @return the rotatingBanner
	 */
	public Collection<GPRotatingImagesComponent> getRotatingBanner(final SessionContext ctx, final SimpleResponsiveBannerComponent item)
	{
		final List<GPRotatingImagesComponent> items = item.getLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.ROTATING2SIMPLERESPONSIVERELATION,
			"GPRotatingImagesComponent",
			null,
			false,
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SimpleResponsiveBannerComponent.rotatingBanner</code> attribute.
	 * @return the rotatingBanner
	 */
	public Collection<GPRotatingImagesComponent> getRotatingBanner(final SimpleResponsiveBannerComponent item)
	{
		return getRotatingBanner( getSession().getSessionContext(), item );
	}
	
	public long getRotatingBannerCount(final SessionContext ctx, final SimpleResponsiveBannerComponent item)
	{
		return item.getLinkedItemsCount(
			ctx,
			false,
			GpcommonaddonConstants.Relations.ROTATING2SIMPLERESPONSIVERELATION,
			"GPRotatingImagesComponent",
			null
		);
	}
	
	public long getRotatingBannerCount(final SimpleResponsiveBannerComponent item)
	{
		return getRotatingBannerCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SimpleResponsiveBannerComponent.rotatingBanner</code> attribute. 
	 * @param value the rotatingBanner
	 */
	public void setRotatingBanner(final SessionContext ctx, final SimpleResponsiveBannerComponent item, final Collection<GPRotatingImagesComponent> value)
	{
		item.setLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.ROTATING2SIMPLERESPONSIVERELATION,
			null,
			value,
			false,
			false,
			Utilities.getMarkModifiedOverride(ROTATING2SIMPLERESPONSIVERELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SimpleResponsiveBannerComponent.rotatingBanner</code> attribute. 
	 * @param value the rotatingBanner
	 */
	public void setRotatingBanner(final SimpleResponsiveBannerComponent item, final Collection<GPRotatingImagesComponent> value)
	{
		setRotatingBanner( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to rotatingBanner. 
	 * @param value the item to add to rotatingBanner
	 */
	public void addToRotatingBanner(final SessionContext ctx, final SimpleResponsiveBannerComponent item, final GPRotatingImagesComponent value)
	{
		item.addLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.ROTATING2SIMPLERESPONSIVERELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ROTATING2SIMPLERESPONSIVERELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to rotatingBanner. 
	 * @param value the item to add to rotatingBanner
	 */
	public void addToRotatingBanner(final SimpleResponsiveBannerComponent item, final GPRotatingImagesComponent value)
	{
		addToRotatingBanner( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from rotatingBanner. 
	 * @param value the item to remove from rotatingBanner
	 */
	public void removeFromRotatingBanner(final SessionContext ctx, final SimpleResponsiveBannerComponent item, final GPRotatingImagesComponent value)
	{
		item.removeLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.ROTATING2SIMPLERESPONSIVERELATION,
			null,
			Collections.singletonList(value),
			false,
			false,
			Utilities.getMarkModifiedOverride(ROTATING2SIMPLERESPONSIVERELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from rotatingBanner. 
	 * @param value the item to remove from rotatingBanner
	 */
	public void removeFromRotatingBanner(final SimpleResponsiveBannerComponent item, final GPRotatingImagesComponent value)
	{
		removeFromRotatingBanner( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.socialLink</code> attribute.
	 * @return the socialLink
	 */
	public Boolean isSocialLink(final SessionContext ctx, final CMSLinkComponent item)
	{
		return (Boolean)item.getProperty( ctx, GpcommonaddonConstants.Attributes.CMSLinkComponent.SOCIALLINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.socialLink</code> attribute.
	 * @return the socialLink
	 */
	public Boolean isSocialLink(final CMSLinkComponent item)
	{
		return isSocialLink( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.socialLink</code> attribute. 
	 * @return the socialLink
	 */
	public boolean isSocialLinkAsPrimitive(final SessionContext ctx, final CMSLinkComponent item)
	{
		Boolean value = isSocialLink( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>CMSLinkComponent.socialLink</code> attribute. 
	 * @return the socialLink
	 */
	public boolean isSocialLinkAsPrimitive(final CMSLinkComponent item)
	{
		return isSocialLinkAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSLinkComponent.socialLink</code> attribute. 
	 * @param value the socialLink
	 */
	public void setSocialLink(final SessionContext ctx, final CMSLinkComponent item, final Boolean value)
	{
		item.setProperty(ctx, GpcommonaddonConstants.Attributes.CMSLinkComponent.SOCIALLINK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSLinkComponent.socialLink</code> attribute. 
	 * @param value the socialLink
	 */
	public void setSocialLink(final CMSLinkComponent item, final Boolean value)
	{
		setSocialLink( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSLinkComponent.socialLink</code> attribute. 
	 * @param value the socialLink
	 */
	public void setSocialLink(final SessionContext ctx, final CMSLinkComponent item, final boolean value)
	{
		setSocialLink( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>CMSLinkComponent.socialLink</code> attribute. 
	 * @param value the socialLink
	 */
	public void setSocialLink(final CMSLinkComponent item, final boolean value)
	{
		setSocialLink( getSession().getSessionContext(), item, value );
	}
	
}
