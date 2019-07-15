/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.contextualattributevalues.jalo;

import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.contextualattributevalues.constants.ContextualattributevaluesConstants;
import de.hybris.platform.contextualattributevalues.jalo.ContextualAttributesContext;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem ContextualAttributeValue}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedContextualAttributeValue extends GenericItem
{
	/** Qualifier of the <code>ContextualAttributeValue.subscribable</code> attribute **/
	public static final String SUBSCRIBABLE = "subscribable";
	/** Qualifier of the <code>ContextualAttributeValue.nDaysBeforeSubscription</code> attribute **/
	public static final String NDAYSBEFORESUBSCRIPTION = "nDaysBeforeSubscription";
	/** Qualifier of the <code>ContextualAttributeValue.catalogVersion</code> attribute **/
	public static final String CATALOGVERSION = "catalogVersion";
	/** Qualifier of the <code>ContextualAttributeValue.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>ContextualAttributeValue.ean</code> attribute **/
	public static final String EAN = "ean";
	/** Qualifier of the <code>ContextualAttributeValue.description</code> attribute **/
	public static final String DESCRIPTION = "description";
	/** Qualifier of the <code>ContextualAttributeValue.picture</code> attribute **/
	public static final String PICTURE = "picture";
	/** Qualifier of the <code>ContextualAttributeValue.galleryImages</code> attribute **/
	public static final String GALLERYIMAGES = "galleryImages";
	/** Qualifier of the <code>ContextualAttributeValue.context</code> attribute **/
	public static final String CONTEXT = "context";
	/** Qualifier of the <code>ContextualAttributeValue.featureList</code> attribute **/
	public static final String FEATURELIST = "featureList";
	/** Qualifier of the <code>ContextualAttributeValue.sellingStatement</code> attribute **/
	public static final String SELLINGSTATEMENT = "sellingStatement";
	/** Qualifier of the <code>ContextualAttributeValue.productSubtitle</code> attribute **/
	public static final String PRODUCTSUBTITLE = "productSubtitle";
	/** Qualifier of the <code>ContextualAttributeValue.priceText</code> attribute **/
	public static final String PRICETEXT = "priceText";
	/** Qualifier of the <code>ContextualAttributeValue.minOrderQuantity</code> attribute **/
	public static final String MINORDERQUANTITY = "minOrderQuantity";
	/** Qualifier of the <code>ContextualAttributeValue.maxOrderQuantity</code> attribute **/
	public static final String MAXORDERQUANTITY = "maxOrderQuantity";
	/** Qualifier of the <code>ContextualAttributeValue.lowStockLevelThreshold</code> attribute **/
	public static final String LOWSTOCKLEVELTHRESHOLD = "lowStockLevelThreshold";
	/** Qualifier of the <code>ContextualAttributeValue.promoText</code> attribute **/
	public static final String PROMOTEXT = "promoText";
	/** Qualifier of the <code>ContextualAttributeValue.hiddenFromSearch</code> attribute **/
	public static final String HIDDENFROMSEARCH = "hiddenFromSearch";
	/** Qualifier of the <code>ContextualAttributeValue.product</code> attribute **/
	public static final String PRODUCT = "product";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n PRODUCT's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedContextualAttributeValue> PRODUCTHANDLER = new BidirectionalOneToManyHandler<GeneratedContextualAttributeValue>(
	ContextualattributevaluesConstants.TC.CONTEXTUALATTRIBUTEVALUE,
	false,
	"product",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(SUBSCRIBABLE, AttributeMode.INITIAL);
		tmp.put(NDAYSBEFORESUBSCRIPTION, AttributeMode.INITIAL);
		tmp.put(CATALOGVERSION, AttributeMode.INITIAL);
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(EAN, AttributeMode.INITIAL);
		tmp.put(DESCRIPTION, AttributeMode.INITIAL);
		tmp.put(PICTURE, AttributeMode.INITIAL);
		tmp.put(GALLERYIMAGES, AttributeMode.INITIAL);
		tmp.put(CONTEXT, AttributeMode.INITIAL);
		tmp.put(FEATURELIST, AttributeMode.INITIAL);
		tmp.put(SELLINGSTATEMENT, AttributeMode.INITIAL);
		tmp.put(PRODUCTSUBTITLE, AttributeMode.INITIAL);
		tmp.put(PRICETEXT, AttributeMode.INITIAL);
		tmp.put(MINORDERQUANTITY, AttributeMode.INITIAL);
		tmp.put(MAXORDERQUANTITY, AttributeMode.INITIAL);
		tmp.put(LOWSTOCKLEVELTHRESHOLD, AttributeMode.INITIAL);
		tmp.put(PROMOTEXT, AttributeMode.INITIAL);
		tmp.put(HIDDENFROMSEARCH, AttributeMode.INITIAL);
		tmp.put(PRODUCT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.catalogVersion</code> attribute.
	 * @return the catalogVersion - CatalogVersion this type is catalog aware
	 */
	public CatalogVersion getCatalogVersion(final SessionContext ctx)
	{
		return (CatalogVersion)getProperty( ctx, CATALOGVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.catalogVersion</code> attribute.
	 * @return the catalogVersion - CatalogVersion this type is catalog aware
	 */
	public CatalogVersion getCatalogVersion()
	{
		return getCatalogVersion( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - CatalogVersion this type is catalog aware
	 */
	public void setCatalogVersion(final SessionContext ctx, final CatalogVersion value)
	{
		setProperty(ctx, CATALOGVERSION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.catalogVersion</code> attribute. 
	 * @param value the catalogVersion - CatalogVersion this type is catalog aware
	 */
	public void setCatalogVersion(final CatalogVersion value)
	{
		setCatalogVersion( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.context</code> attribute.
	 * @return the context - Context of the attribute.
	 */
	public ContextualAttributesContext getContext(final SessionContext ctx)
	{
		return (ContextualAttributesContext)getProperty( ctx, CONTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.context</code> attribute.
	 * @return the context - Context of the attribute.
	 */
	public ContextualAttributesContext getContext()
	{
		return getContext( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.context</code> attribute. 
	 * @param value the context - Context of the attribute.
	 */
	public void setContext(final SessionContext ctx, final ContextualAttributesContext value)
	{
		setProperty(ctx, CONTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.context</code> attribute. 
	 * @param value the context - Context of the attribute.
	 */
	public void setContext(final ContextualAttributesContext value)
	{
		setContext( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		PRODUCTHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.description</code> attribute.
	 * @return the description - contextual attribute.
	 */
	public String getDescription(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedContextualAttributeValue.getDescription requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, DESCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.description</code> attribute.
	 * @return the description - contextual attribute.
	 */
	public String getDescription()
	{
		return getDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.description</code> attribute. 
	 * @return the localized description - contextual attribute.
	 */
	public Map<Language,String> getAllDescription(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,DESCRIPTION,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.description</code> attribute. 
	 * @return the localized description - contextual attribute.
	 */
	public Map<Language,String> getAllDescription()
	{
		return getAllDescription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.description</code> attribute. 
	 * @param value the description - contextual attribute.
	 */
	public void setDescription(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedContextualAttributeValue.setDescription requires a session language", 0 );
		}
		setLocalizedProperty(ctx, DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.description</code> attribute. 
	 * @param value the description - contextual attribute.
	 */
	public void setDescription(final String value)
	{
		setDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.description</code> attribute. 
	 * @param value the description - contextual attribute.
	 */
	public void setAllDescription(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,DESCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.description</code> attribute. 
	 * @param value the description - contextual attribute.
	 */
	public void setAllDescription(final Map<Language,String> value)
	{
		setAllDescription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.ean</code> attribute.
	 * @return the ean - contextual attribute.
	 */
	public String getEan(final SessionContext ctx)
	{
		return (String)getProperty( ctx, EAN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.ean</code> attribute.
	 * @return the ean - contextual attribute.
	 */
	public String getEan()
	{
		return getEan( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.ean</code> attribute. 
	 * @param value the ean - contextual attribute.
	 */
	public void setEan(final SessionContext ctx, final String value)
	{
		setProperty(ctx, EAN,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.ean</code> attribute. 
	 * @param value the ean - contextual attribute.
	 */
	public void setEan(final String value)
	{
		setEan( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.featureList</code> attribute.
	 * @return the featureList - A list of features for the product.
	 */
	public List<String> getFeatureList(final SessionContext ctx)
	{
		List<String> coll = (List<String>)getProperty( ctx, FEATURELIST);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.featureList</code> attribute.
	 * @return the featureList - A list of features for the product.
	 */
	public List<String> getFeatureList()
	{
		return getFeatureList( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.featureList</code> attribute. 
	 * @param value the featureList - A list of features for the product.
	 */
	public void setFeatureList(final SessionContext ctx, final List<String> value)
	{
		setProperty(ctx, FEATURELIST,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.featureList</code> attribute. 
	 * @param value the featureList - A list of features for the product.
	 */
	public void setFeatureList(final List<String> value)
	{
		setFeatureList( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.galleryImages</code> attribute.
	 * @return the galleryImages - A list of additional images for the product.
	 */
	public List<MediaContainer> getGalleryImages(final SessionContext ctx)
	{
		List<MediaContainer> coll = (List<MediaContainer>)getProperty( ctx, GALLERYIMAGES);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.galleryImages</code> attribute.
	 * @return the galleryImages - A list of additional images for the product.
	 */
	public List<MediaContainer> getGalleryImages()
	{
		return getGalleryImages( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.galleryImages</code> attribute. 
	 * @param value the galleryImages - A list of additional images for the product.
	 */
	public void setGalleryImages(final SessionContext ctx, final List<MediaContainer> value)
	{
		setProperty(ctx, GALLERYIMAGES,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.galleryImages</code> attribute. 
	 * @param value the galleryImages - A list of additional images for the product.
	 */
	public void setGalleryImages(final List<MediaContainer> value)
	{
		setGalleryImages( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.hiddenFromSearch</code> attribute.
	 * @return the hiddenFromSearch
	 */
	public Boolean isHiddenFromSearch(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, HIDDENFROMSEARCH);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.hiddenFromSearch</code> attribute.
	 * @return the hiddenFromSearch
	 */
	public Boolean isHiddenFromSearch()
	{
		return isHiddenFromSearch( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.hiddenFromSearch</code> attribute. 
	 * @return the hiddenFromSearch
	 */
	public boolean isHiddenFromSearchAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isHiddenFromSearch( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.hiddenFromSearch</code> attribute. 
	 * @return the hiddenFromSearch
	 */
	public boolean isHiddenFromSearchAsPrimitive()
	{
		return isHiddenFromSearchAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.hiddenFromSearch</code> attribute. 
	 * @param value the hiddenFromSearch
	 */
	public void setHiddenFromSearch(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, HIDDENFROMSEARCH,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.hiddenFromSearch</code> attribute. 
	 * @param value the hiddenFromSearch
	 */
	public void setHiddenFromSearch(final Boolean value)
	{
		setHiddenFromSearch( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.hiddenFromSearch</code> attribute. 
	 * @param value the hiddenFromSearch
	 */
	public void setHiddenFromSearch(final SessionContext ctx, final boolean value)
	{
		setHiddenFromSearch( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.hiddenFromSearch</code> attribute. 
	 * @param value the hiddenFromSearch
	 */
	public void setHiddenFromSearch(final boolean value)
	{
		setHiddenFromSearch( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.lowStockLevelThreshold</code> attribute.
	 * @return the lowStockLevelThreshold - Indicates the contextual low inventory threshold default value.
	 */
	public Integer getLowStockLevelThreshold(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, LOWSTOCKLEVELTHRESHOLD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.lowStockLevelThreshold</code> attribute.
	 * @return the lowStockLevelThreshold - Indicates the contextual low inventory threshold default value.
	 */
	public Integer getLowStockLevelThreshold()
	{
		return getLowStockLevelThreshold( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.lowStockLevelThreshold</code> attribute. 
	 * @return the lowStockLevelThreshold - Indicates the contextual low inventory threshold default value.
	 */
	public int getLowStockLevelThresholdAsPrimitive(final SessionContext ctx)
	{
		Integer value = getLowStockLevelThreshold( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.lowStockLevelThreshold</code> attribute. 
	 * @return the lowStockLevelThreshold - Indicates the contextual low inventory threshold default value.
	 */
	public int getLowStockLevelThresholdAsPrimitive()
	{
		return getLowStockLevelThresholdAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.lowStockLevelThreshold</code> attribute. 
	 * @param value the lowStockLevelThreshold - Indicates the contextual low inventory threshold default value.
	 */
	public void setLowStockLevelThreshold(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, LOWSTOCKLEVELTHRESHOLD,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.lowStockLevelThreshold</code> attribute. 
	 * @param value the lowStockLevelThreshold - Indicates the contextual low inventory threshold default value.
	 */
	public void setLowStockLevelThreshold(final Integer value)
	{
		setLowStockLevelThreshold( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.lowStockLevelThreshold</code> attribute. 
	 * @param value the lowStockLevelThreshold - Indicates the contextual low inventory threshold default value.
	 */
	public void setLowStockLevelThreshold(final SessionContext ctx, final int value)
	{
		setLowStockLevelThreshold( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.lowStockLevelThreshold</code> attribute. 
	 * @param value the lowStockLevelThreshold - Indicates the contextual low inventory threshold default value.
	 */
	public void setLowStockLevelThreshold(final int value)
	{
		setLowStockLevelThreshold( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.maxOrderQuantity</code> attribute.
	 * @return the maxOrderQuantity - Indicates the contextual maximum allowable quantity at the stock level
	 */
	public Integer getMaxOrderQuantity(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, MAXORDERQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.maxOrderQuantity</code> attribute.
	 * @return the maxOrderQuantity - Indicates the contextual maximum allowable quantity at the stock level
	 */
	public Integer getMaxOrderQuantity()
	{
		return getMaxOrderQuantity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.maxOrderQuantity</code> attribute. 
	 * @return the maxOrderQuantity - Indicates the contextual maximum allowable quantity at the stock level
	 */
	public int getMaxOrderQuantityAsPrimitive(final SessionContext ctx)
	{
		Integer value = getMaxOrderQuantity( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.maxOrderQuantity</code> attribute. 
	 * @return the maxOrderQuantity - Indicates the contextual maximum allowable quantity at the stock level
	 */
	public int getMaxOrderQuantityAsPrimitive()
	{
		return getMaxOrderQuantityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.maxOrderQuantity</code> attribute. 
	 * @param value the maxOrderQuantity - Indicates the contextual maximum allowable quantity at the stock level
	 */
	public void setMaxOrderQuantity(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, MAXORDERQUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.maxOrderQuantity</code> attribute. 
	 * @param value the maxOrderQuantity - Indicates the contextual maximum allowable quantity at the stock level
	 */
	public void setMaxOrderQuantity(final Integer value)
	{
		setMaxOrderQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.maxOrderQuantity</code> attribute. 
	 * @param value the maxOrderQuantity - Indicates the contextual maximum allowable quantity at the stock level
	 */
	public void setMaxOrderQuantity(final SessionContext ctx, final int value)
	{
		setMaxOrderQuantity( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.maxOrderQuantity</code> attribute. 
	 * @param value the maxOrderQuantity - Indicates the contextual maximum allowable quantity at the stock level
	 */
	public void setMaxOrderQuantity(final int value)
	{
		setMaxOrderQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.minOrderQuantity</code> attribute.
	 * @return the minOrderQuantity - Indicates the contextual minimum allowable quantity at the stock level
	 */
	public Integer getMinOrderQuantity(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, MINORDERQUANTITY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.minOrderQuantity</code> attribute.
	 * @return the minOrderQuantity - Indicates the contextual minimum allowable quantity at the stock level
	 */
	public Integer getMinOrderQuantity()
	{
		return getMinOrderQuantity( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.minOrderQuantity</code> attribute. 
	 * @return the minOrderQuantity - Indicates the contextual minimum allowable quantity at the stock level
	 */
	public int getMinOrderQuantityAsPrimitive(final SessionContext ctx)
	{
		Integer value = getMinOrderQuantity( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.minOrderQuantity</code> attribute. 
	 * @return the minOrderQuantity - Indicates the contextual minimum allowable quantity at the stock level
	 */
	public int getMinOrderQuantityAsPrimitive()
	{
		return getMinOrderQuantityAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.minOrderQuantity</code> attribute. 
	 * @param value the minOrderQuantity - Indicates the contextual minimum allowable quantity at the stock level
	 */
	public void setMinOrderQuantity(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, MINORDERQUANTITY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.minOrderQuantity</code> attribute. 
	 * @param value the minOrderQuantity - Indicates the contextual minimum allowable quantity at the stock level
	 */
	public void setMinOrderQuantity(final Integer value)
	{
		setMinOrderQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.minOrderQuantity</code> attribute. 
	 * @param value the minOrderQuantity - Indicates the contextual minimum allowable quantity at the stock level
	 */
	public void setMinOrderQuantity(final SessionContext ctx, final int value)
	{
		setMinOrderQuantity( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.minOrderQuantity</code> attribute. 
	 * @param value the minOrderQuantity - Indicates the contextual minimum allowable quantity at the stock level
	 */
	public void setMinOrderQuantity(final int value)
	{
		setMinOrderQuantity( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.name</code> attribute.
	 * @return the name - contextual attribute.
	 */
	public String getName(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedContextualAttributeValue.getName requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.name</code> attribute.
	 * @return the name - contextual attribute.
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.name</code> attribute. 
	 * @return the localized name - contextual attribute.
	 */
	public Map<Language,String> getAllName(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,NAME,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.name</code> attribute. 
	 * @return the localized name - contextual attribute.
	 */
	public Map<Language,String> getAllName()
	{
		return getAllName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.name</code> attribute. 
	 * @param value the name - contextual attribute.
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedContextualAttributeValue.setName requires a session language", 0 );
		}
		setLocalizedProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.name</code> attribute. 
	 * @param value the name - contextual attribute.
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.name</code> attribute. 
	 * @param value the name - contextual attribute.
	 */
	public void setAllName(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.name</code> attribute. 
	 * @param value the name - contextual attribute.
	 */
	public void setAllName(final Map<Language,String> value)
	{
		setAllName( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.nDaysBeforeSubscription</code> attribute.
	 * @return the nDaysBeforeSubscription - N Days before placing next order of the subscription
	 */
	public Integer getNDaysBeforeSubscription(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, NDAYSBEFORESUBSCRIPTION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.nDaysBeforeSubscription</code> attribute.
	 * @return the nDaysBeforeSubscription - N Days before placing next order of the subscription
	 */
	public Integer getNDaysBeforeSubscription()
	{
		return getNDaysBeforeSubscription( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.nDaysBeforeSubscription</code> attribute. 
	 * @return the nDaysBeforeSubscription - N Days before placing next order of the subscription
	 */
	public int getNDaysBeforeSubscriptionAsPrimitive(final SessionContext ctx)
	{
		Integer value = getNDaysBeforeSubscription( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.nDaysBeforeSubscription</code> attribute. 
	 * @return the nDaysBeforeSubscription - N Days before placing next order of the subscription
	 */
	public int getNDaysBeforeSubscriptionAsPrimitive()
	{
		return getNDaysBeforeSubscriptionAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.nDaysBeforeSubscription</code> attribute. 
	 * @param value the nDaysBeforeSubscription - N Days before placing next order of the subscription
	 */
	public void setNDaysBeforeSubscription(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, NDAYSBEFORESUBSCRIPTION,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.nDaysBeforeSubscription</code> attribute. 
	 * @param value the nDaysBeforeSubscription - N Days before placing next order of the subscription
	 */
	public void setNDaysBeforeSubscription(final Integer value)
	{
		setNDaysBeforeSubscription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.nDaysBeforeSubscription</code> attribute. 
	 * @param value the nDaysBeforeSubscription - N Days before placing next order of the subscription
	 */
	public void setNDaysBeforeSubscription(final SessionContext ctx, final int value)
	{
		setNDaysBeforeSubscription( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.nDaysBeforeSubscription</code> attribute. 
	 * @param value the nDaysBeforeSubscription - N Days before placing next order of the subscription
	 */
	public void setNDaysBeforeSubscription(final int value)
	{
		setNDaysBeforeSubscription( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.picture</code> attribute.
	 * @return the picture - contextual attribute.
	 */
	public Media getPicture(final SessionContext ctx)
	{
		return (Media)getProperty( ctx, PICTURE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.picture</code> attribute.
	 * @return the picture - contextual attribute.
	 */
	public Media getPicture()
	{
		return getPicture( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.picture</code> attribute. 
	 * @param value the picture - contextual attribute.
	 */
	public void setPicture(final SessionContext ctx, final Media value)
	{
		setProperty(ctx, PICTURE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.picture</code> attribute. 
	 * @param value the picture - contextual attribute.
	 */
	public void setPicture(final Media value)
	{
		setPicture( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.priceText</code> attribute.
	 * @return the priceText - Weblist price of the product.
	 */
	public String getPriceText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PRICETEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.priceText</code> attribute.
	 * @return the priceText - Weblist price of the product.
	 */
	public String getPriceText()
	{
		return getPriceText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.priceText</code> attribute. 
	 * @param value the priceText - Weblist price of the product.
	 */
	public void setPriceText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PRICETEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.priceText</code> attribute. 
	 * @param value the priceText - Weblist price of the product.
	 */
	public void setPriceText(final String value)
	{
		setPriceText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.product</code> attribute.
	 * @return the product
	 */
	public Product getProduct(final SessionContext ctx)
	{
		return (Product)getProperty( ctx, PRODUCT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.product</code> attribute.
	 * @return the product
	 */
	public Product getProduct()
	{
		return getProduct( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final SessionContext ctx, final Product value)
	{
		PRODUCTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.product</code> attribute. 
	 * @param value the product
	 */
	public void setProduct(final Product value)
	{
		setProduct( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.productSubtitle</code> attribute.
	 * @return the productSubtitle - A subtitle for the product.
	 */
	public String getProductSubtitle(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedContextualAttributeValue.getProductSubtitle requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, PRODUCTSUBTITLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.productSubtitle</code> attribute.
	 * @return the productSubtitle - A subtitle for the product.
	 */
	public String getProductSubtitle()
	{
		return getProductSubtitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.productSubtitle</code> attribute. 
	 * @return the localized productSubtitle - A subtitle for the product.
	 */
	public Map<Language,String> getAllProductSubtitle(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,PRODUCTSUBTITLE,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.productSubtitle</code> attribute. 
	 * @return the localized productSubtitle - A subtitle for the product.
	 */
	public Map<Language,String> getAllProductSubtitle()
	{
		return getAllProductSubtitle( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.productSubtitle</code> attribute. 
	 * @param value the productSubtitle - A subtitle for the product.
	 */
	public void setProductSubtitle(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedContextualAttributeValue.setProductSubtitle requires a session language", 0 );
		}
		setLocalizedProperty(ctx, PRODUCTSUBTITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.productSubtitle</code> attribute. 
	 * @param value the productSubtitle - A subtitle for the product.
	 */
	public void setProductSubtitle(final String value)
	{
		setProductSubtitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.productSubtitle</code> attribute. 
	 * @param value the productSubtitle - A subtitle for the product.
	 */
	public void setAllProductSubtitle(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,PRODUCTSUBTITLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.productSubtitle</code> attribute. 
	 * @param value the productSubtitle - A subtitle for the product.
	 */
	public void setAllProductSubtitle(final Map<Language,String> value)
	{
		setAllProductSubtitle( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.promoText</code> attribute.
	 * @return the promoText
	 */
	public String getPromoText(final SessionContext ctx)
	{
		return (String)getProperty( ctx, PROMOTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.promoText</code> attribute.
	 * @return the promoText
	 */
	public String getPromoText()
	{
		return getPromoText( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.promoText</code> attribute. 
	 * @param value the promoText
	 */
	public void setPromoText(final SessionContext ctx, final String value)
	{
		setProperty(ctx, PROMOTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.promoText</code> attribute. 
	 * @param value the promoText
	 */
	public void setPromoText(final String value)
	{
		setPromoText( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.sellingStatement</code> attribute.
	 * @return the sellingStatement - A sellingstmt for the product.
	 */
	public String getSellingStatement(final SessionContext ctx)
	{
		if( ctx == null || ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedContextualAttributeValue.getSellingStatement requires a session language", 0 );
		}
		return (String)getLocalizedProperty( ctx, SELLINGSTATEMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.sellingStatement</code> attribute.
	 * @return the sellingStatement - A sellingstmt for the product.
	 */
	public String getSellingStatement()
	{
		return getSellingStatement( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.sellingStatement</code> attribute. 
	 * @return the localized sellingStatement - A sellingstmt for the product.
	 */
	public Map<Language,String> getAllSellingStatement(final SessionContext ctx)
	{
		return (Map<Language,String>)getAllLocalizedProperties(ctx,SELLINGSTATEMENT,C2LManager.getInstance().getAllLanguages());
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.sellingStatement</code> attribute. 
	 * @return the localized sellingStatement - A sellingstmt for the product.
	 */
	public Map<Language,String> getAllSellingStatement()
	{
		return getAllSellingStatement( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.sellingStatement</code> attribute. 
	 * @param value the sellingStatement - A sellingstmt for the product.
	 */
	public void setSellingStatement(final SessionContext ctx, final String value)
	{
		if ( ctx == null) 
		{
			throw new JaloInvalidParameterException( "ctx is null", 0 );
		}
		if( ctx.getLanguage() == null )
		{
			throw new JaloInvalidParameterException("GeneratedContextualAttributeValue.setSellingStatement requires a session language", 0 );
		}
		setLocalizedProperty(ctx, SELLINGSTATEMENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.sellingStatement</code> attribute. 
	 * @param value the sellingStatement - A sellingstmt for the product.
	 */
	public void setSellingStatement(final String value)
	{
		setSellingStatement( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.sellingStatement</code> attribute. 
	 * @param value the sellingStatement - A sellingstmt for the product.
	 */
	public void setAllSellingStatement(final SessionContext ctx, final Map<Language,String> value)
	{
		setAllLocalizedProperties(ctx,SELLINGSTATEMENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.sellingStatement</code> attribute. 
	 * @param value the sellingStatement - A sellingstmt for the product.
	 */
	public void setAllSellingStatement(final Map<Language,String> value)
	{
		setAllSellingStatement( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.subscribable</code> attribute.
	 * @return the subscribable
	 */
	public Boolean isSubscribable(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, SUBSCRIBABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.subscribable</code> attribute.
	 * @return the subscribable
	 */
	public Boolean isSubscribable()
	{
		return isSubscribable( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.subscribable</code> attribute. 
	 * @return the subscribable
	 */
	public boolean isSubscribableAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isSubscribable( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributeValue.subscribable</code> attribute. 
	 * @return the subscribable
	 */
	public boolean isSubscribableAsPrimitive()
	{
		return isSubscribableAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.subscribable</code> attribute. 
	 * @param value the subscribable
	 */
	public void setSubscribable(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, SUBSCRIBABLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.subscribable</code> attribute. 
	 * @param value the subscribable
	 */
	public void setSubscribable(final Boolean value)
	{
		setSubscribable( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.subscribable</code> attribute. 
	 * @param value the subscribable
	 */
	public void setSubscribable(final SessionContext ctx, final boolean value)
	{
		setSubscribable( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributeValue.subscribable</code> attribute. 
	 * @param value the subscribable
	 */
	public void setSubscribable(final boolean value)
	{
		setSubscribable( getSession().getSessionContext(), value );
	}
	
}
