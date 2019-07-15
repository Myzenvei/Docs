/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.core.jalo.GPMarketingCategories;
import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2.jalo.contents.CMSItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPTipsAndKnowledgeInfo GPTipsAndKnowledgeInfo}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPTipsAndKnowledgeInfo extends CMSItem
{
	/** Qualifier of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkUrl</code> attribute **/
	public static final String KNOWLEDGELINKURL = "knowledgeLinkUrl";
	/** Qualifier of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkText</code> attribute **/
	public static final String KNOWLEDGELINKTEXT = "knowledgeLinkText";
	/** Qualifier of the <code>GPTipsAndKnowledgeInfo.tipText</code> attribute **/
	public static final String TIPTEXT = "tipText";
	/** Qualifier of the <code>GPTipsAndKnowledgeInfo.tipAltText</code> attribute **/
	public static final String TIPALTTEXT = "tipAltText";
	/** Qualifier of the <code>GPTipsAndKnowledgeInfo.knowledgeMedia</code> attribute **/
	public static final String KNOWLEDGEMEDIA = "knowledgeMedia";
	/** Qualifier of the <code>GPTipsAndKnowledgeInfo.isExternalLink</code> attribute **/
	public static final String ISEXTERNALLINK = "isExternalLink";
	/** Qualifier of the <code>GPTipsAndKnowledgeInfo.marketingCategoriesList</code> attribute **/
	public static final String MARKETINGCATEGORIESLIST = "marketingCategoriesList";
	/** Relation ordering override parameter constants for GPKnowledgeTipRelation from ((gpcommonaddon))*/
	protected static String GPKNOWLEDGETIPRELATION_SRC_ORDERED = "relation.GPKnowledgeTipRelation.source.ordered";
	protected static String GPKNOWLEDGETIPRELATION_TGT_ORDERED = "relation.GPKnowledgeTipRelation.target.ordered";
	/** Relation disable markmodifed parameter constants for GPKnowledgeTipRelation from ((gpcommonaddon))*/
	protected static String GPKNOWLEDGETIPRELATION_MARKMODIFIED = "relation.GPKnowledgeTipRelation.markmodified";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(CMSItem.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(KNOWLEDGELINKURL, AttributeMode.INITIAL);
		tmp.put(KNOWLEDGELINKTEXT, AttributeMode.INITIAL);
		tmp.put(TIPTEXT, AttributeMode.INITIAL);
		tmp.put(TIPALTTEXT, AttributeMode.INITIAL);
		tmp.put(KNOWLEDGEMEDIA, AttributeMode.INITIAL);
		tmp.put(ISEXTERNALLINK, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.isExternalLink</code> attribute.
	 * @return the isExternalLink - Is External Link
	 */
	public Boolean isIsExternalLink(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ISEXTERNALLINK);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.isExternalLink</code> attribute.
	 * @return the isExternalLink - Is External Link
	 */
	public Boolean isIsExternalLink()
	{
		return isIsExternalLink( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.isExternalLink</code> attribute. 
	 * @return the isExternalLink - Is External Link
	 */
	public boolean isIsExternalLinkAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsExternalLink( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.isExternalLink</code> attribute. 
	 * @return the isExternalLink - Is External Link
	 */
	public boolean isIsExternalLinkAsPrimitive()
	{
		return isIsExternalLinkAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.isExternalLink</code> attribute. 
	 * @param value the isExternalLink - Is External Link
	 */
	public void setIsExternalLink(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ISEXTERNALLINK,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.isExternalLink</code> attribute. 
	 * @param value the isExternalLink - Is External Link
	 */
	public void setIsExternalLink(final Boolean value)
	{
		setIsExternalLink( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.isExternalLink</code> attribute. 
	 * @param value the isExternalLink - Is External Link
	 */
	public void setIsExternalLink(final SessionContext ctx, final boolean value)
	{
		setIsExternalLink( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.isExternalLink</code> attribute. 
	 * @param value the isExternalLink - Is External Link
	 */
	public void setIsExternalLink(final boolean value)
	{
		setIsExternalLink( getSession().getSessionContext(), value );
	}
	
	@Override
	public boolean isMarkModifiedDisabled(final Item referencedItem)
	{
		ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("GPMarketingCategories");
		if(relationSecondEnd0.isAssignableFrom(referencedItem.getComposedType()))
		{
			return Utilities.getMarkModifiedOverride(GPKNOWLEDGETIPRELATION_MARKMODIFIED);
		}
		return true;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkText</code> attribute.
	 * @return the knowledgeLinkText
	 */
	public String getKnowledgeLinkText(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPTipsAndKnowledgeInfo.getKnowledgeLinkText requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, KNOWLEDGELINKTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkText</code> attribute.
	 * @return the knowledgeLinkText
	 */
	public String getKnowledgeLinkText()
	{
		return getKnowledgeLinkText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkText</code> attribute. 
	 * @return the localized knowledgeLinkText
	 */
	public Map<Language,String> getAllKnowledgeLinkText(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,KNOWLEDGELINKTEXT,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkText</code> attribute. 
	 * @return the localized knowledgeLinkText
	 */
	public Map<Language,String> getAllKnowledgeLinkText()
	{
		return getAllKnowledgeLinkText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkText</code> attribute. 
	 * @param value the knowledgeLinkText
	 */
	public void setKnowledgeLinkText(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPTipsAndKnowledgeInfo.setKnowledgeLinkText requires a session language", 0 );
		}
		setLocalizedProperty(ctx, KNOWLEDGELINKTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkText</code> attribute. 
	 * @param value the knowledgeLinkText
	 */
	public void setKnowledgeLinkText(final String value)
	{
		setKnowledgeLinkText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkText</code> attribute. 
	 * @param value the knowledgeLinkText
	 */
	public void setAllKnowledgeLinkText(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,KNOWLEDGELINKTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkText</code> attribute. 
	 * @param value the knowledgeLinkText
	 */
	public void setAllKnowledgeLinkText(final Map<Language,String> value)
	{
		setAllKnowledgeLinkText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkUrl</code> attribute.
	 * @return the knowledgeLinkUrl
	 */
	public String getKnowledgeLinkUrl(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPTipsAndKnowledgeInfo.getKnowledgeLinkUrl requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, KNOWLEDGELINKURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkUrl</code> attribute.
	 * @return the knowledgeLinkUrl
	 */
	public String getKnowledgeLinkUrl()
	{
		return getKnowledgeLinkUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkUrl</code> attribute. 
	 * @return the localized knowledgeLinkUrl
	 */
	public Map<Language,String> getAllKnowledgeLinkUrl(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,KNOWLEDGELINKURL,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkUrl</code> attribute. 
	 * @return the localized knowledgeLinkUrl
	 */
	public Map<Language,String> getAllKnowledgeLinkUrl()
	{
		return getAllKnowledgeLinkUrl( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkUrl</code> attribute. 
	 * @param value the knowledgeLinkUrl
	 */
	public void setKnowledgeLinkUrl(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPTipsAndKnowledgeInfo.setKnowledgeLinkUrl requires a session language", 0 );
		}
		setLocalizedProperty(ctx, KNOWLEDGELINKURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkUrl</code> attribute. 
	 * @param value the knowledgeLinkUrl
	 */
	public void setKnowledgeLinkUrl(final String value)
	{
		setKnowledgeLinkUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkUrl</code> attribute. 
	 * @param value the knowledgeLinkUrl
	 */
	public void setAllKnowledgeLinkUrl(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,KNOWLEDGELINKURL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeLinkUrl</code> attribute. 
	 * @param value the knowledgeLinkUrl
	 */
	public void setAllKnowledgeLinkUrl(final Map<Language,String> value)
	{
		setAllKnowledgeLinkUrl( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeMedia</code> attribute.
	 * @return the knowledgeMedia - Stores Media Model for Knowledge Tile
	 */
	public Media getKnowledgeMedia(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPTipsAndKnowledgeInfo.getKnowledgeMedia requires a session language", 0 );
		}
		return (Media)getLocalizedProperty( ctx, KNOWLEDGEMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeMedia</code> attribute.
	 * @return the knowledgeMedia - Stores Media Model for Knowledge Tile
	 */
	public Media getKnowledgeMedia()
	{
		return getKnowledgeMedia( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeMedia</code> attribute. 
	 * @return the localized knowledgeMedia - Stores Media Model for Knowledge Tile
	 */
	public Map<Language,Media> getAllKnowledgeMedia(final SessionContext ctx)
	{
		return (Map<Language,Media>)getAllLocalizedProperties(ctx,KNOWLEDGEMEDIA,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.knowledgeMedia</code> attribute. 
	 * @return the localized knowledgeMedia - Stores Media Model for Knowledge Tile
	 */
	public Map<Language,Media> getAllKnowledgeMedia()
	{
		return getAllKnowledgeMedia( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeMedia</code> attribute. 
	 * @param value the knowledgeMedia - Stores Media Model for Knowledge Tile
	 */
	public void setKnowledgeMedia(final SessionContext ctx, final Media value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPTipsAndKnowledgeInfo.setKnowledgeMedia requires a session language", 0 );
		}
		setLocalizedProperty(ctx, KNOWLEDGEMEDIA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeMedia</code> attribute. 
	 * @param value the knowledgeMedia - Stores Media Model for Knowledge Tile
	 */
	public void setKnowledgeMedia(final Media value)
	{
		setKnowledgeMedia( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeMedia</code> attribute. 
	 * @param value the knowledgeMedia - Stores Media Model for Knowledge Tile
	 */
	public void setAllKnowledgeMedia(final SessionContext ctx, final Map<Language,Media> value)
	{
		setAllLocalizedProperties(ctx,KNOWLEDGEMEDIA,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.knowledgeMedia</code> attribute. 
	 * @param value the knowledgeMedia - Stores Media Model for Knowledge Tile
	 */
	public void setAllKnowledgeMedia(final Map<Language,Media> value)
	{
		setAllKnowledgeMedia( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.marketingCategoriesList</code> attribute.
	 * @return the marketingCategoriesList
	 */
	public Collection<GPMarketingCategories> getMarketingCategoriesList(final SessionContext ctx)
	{
		final List<GPMarketingCategories> items = getLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPKNOWLEDGETIPRELATION,
			"GPMarketingCategories",
			null,
			Utilities.getRelationOrderingOverride(GPKNOWLEDGETIPRELATION_SRC_ORDERED, true),
			false
		);
		return items;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.marketingCategoriesList</code> attribute.
	 * @return the marketingCategoriesList
	 */
	public Collection<GPMarketingCategories> getMarketingCategoriesList()
	{
		return getMarketingCategoriesList( getSession().getSessionContext() );
	}
	
	public long getMarketingCategoriesListCount(final SessionContext ctx)
	{
		return getLinkedItemsCount(
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPKNOWLEDGETIPRELATION,
			"GPMarketingCategories",
			null
		);
	}
	
	public long getMarketingCategoriesListCount()
	{
		return getMarketingCategoriesListCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.marketingCategoriesList</code> attribute. 
	 * @param value the marketingCategoriesList
	 */
	public void setMarketingCategoriesList(final SessionContext ctx, final Collection<GPMarketingCategories> value)
	{
		setLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPKNOWLEDGETIPRELATION,
			null,
			value,
			Utilities.getRelationOrderingOverride(GPKNOWLEDGETIPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(GPKNOWLEDGETIPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.marketingCategoriesList</code> attribute. 
	 * @param value the marketingCategoriesList
	 */
	public void setMarketingCategoriesList(final Collection<GPMarketingCategories> value)
	{
		setMarketingCategoriesList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to marketingCategoriesList. 
	 * @param value the item to add to marketingCategoriesList
	 */
	public void addToMarketingCategoriesList(final SessionContext ctx, final GPMarketingCategories value)
	{
		addLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPKNOWLEDGETIPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(GPKNOWLEDGETIPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(GPKNOWLEDGETIPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to marketingCategoriesList. 
	 * @param value the item to add to marketingCategoriesList
	 */
	public void addToMarketingCategoriesList(final GPMarketingCategories value)
	{
		addToMarketingCategoriesList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from marketingCategoriesList. 
	 * @param value the item to remove from marketingCategoriesList
	 */
	public void removeFromMarketingCategoriesList(final SessionContext ctx, final GPMarketingCategories value)
	{
		removeLinkedItems( 
			ctx,
			false,
			GpcommonaddonConstants.Relations.GPKNOWLEDGETIPRELATION,
			null,
			Collections.singletonList(value),
			Utilities.getRelationOrderingOverride(GPKNOWLEDGETIPRELATION_SRC_ORDERED, true),
			false,
			Utilities.getMarkModifiedOverride(GPKNOWLEDGETIPRELATION_MARKMODIFIED)
		);
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from marketingCategoriesList. 
	 * @param value the item to remove from marketingCategoriesList
	 */
	public void removeFromMarketingCategoriesList(final GPMarketingCategories value)
	{
		removeFromMarketingCategoriesList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.tipAltText</code> attribute.
	 * @return the tipAltText
	 */
	public String getTipAltText(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPTipsAndKnowledgeInfo.getTipAltText requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, TIPALTTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.tipAltText</code> attribute.
	 * @return the tipAltText
	 */
	public String getTipAltText()
	{
		return getTipAltText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.tipAltText</code> attribute. 
	 * @return the localized tipAltText
	 */
	public Map<Language,String> getAllTipAltText(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,TIPALTTEXT,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.tipAltText</code> attribute. 
	 * @return the localized tipAltText
	 */
	public Map<Language,String> getAllTipAltText()
	{
		return getAllTipAltText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.tipAltText</code> attribute. 
	 * @param value the tipAltText
	 */
	public void setTipAltText(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPTipsAndKnowledgeInfo.setTipAltText requires a session language", 0 );
		}
		setLocalizedProperty(ctx, TIPALTTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.tipAltText</code> attribute. 
	 * @param value the tipAltText
	 */
	public void setTipAltText(final String value)
	{
		setTipAltText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.tipAltText</code> attribute. 
	 * @param value the tipAltText
	 */
	public void setAllTipAltText(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,TIPALTTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.tipAltText</code> attribute. 
	 * @param value the tipAltText
	 */
	public void setAllTipAltText(final Map<Language,String> value)
	{
		setAllTipAltText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.tipText</code> attribute.
	 * @return the tipText
	 */
	public String getTipText(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPTipsAndKnowledgeInfo.getTipText requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, TIPTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.tipText</code> attribute.
	 * @return the tipText
	 */
	public String getTipText()
	{
		return getTipText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.tipText</code> attribute. 
	 * @return the localized tipText
	 */
	public Map<Language,String> getAllTipText(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,TIPTEXT,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfo.tipText</code> attribute. 
	 * @return the localized tipText
	 */
	public Map<Language,String> getAllTipText()
	{
		return getAllTipText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.tipText</code> attribute. 
	 * @param value the tipText
	 */
	public void setTipText(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedGPTipsAndKnowledgeInfo.setTipText requires a session language", 0 );
		}
		setLocalizedProperty(ctx, TIPTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.tipText</code> attribute. 
	 * @param value the tipText
	 */
	public void setTipText(final String value)
	{
		setTipText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.tipText</code> attribute. 
	 * @param value the tipText
	 */
	public void setAllTipText(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,TIPTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfo.tipText</code> attribute. 
	 * @param value the tipText
	 */
	public void setAllTipText(final Map<Language,String> value)
	{
		setAllTipText( getSession().getSessionContext(), value );
	}
	
}
