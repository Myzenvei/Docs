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

import de.hybris.platform.contextualattributevalues.constants.ContextualattributevaluesConstants;
import de.hybris.platform.contextualattributevalues.jalo.ContextualAttributeValue;
import de.hybris.platform.contextualattributevalues.jalo.ContextualAttributesContext;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedProperty;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type <code>ContextualattributevaluesManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedContextualattributevaluesManager extends Extension
{
	/**
	* {@link OneToManyHandler} for handling 1:n CONTEXTUALATTRIBUTEVALUES's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ContextualAttributeValue> PRODUCT2CONTEXTUALATTRIBUTEVALUERELCONTEXTUALATTRIBUTEVALUESHANDLER = new OneToManyHandler<ContextualAttributeValue>(
	ContextualattributevaluesConstants.TC.CONTEXTUALATTRIBUTEVALUE,
	false,
	"product",
	null,
	false,
	true,
	CollectionType.COLLECTION
	);
	/**
	* {@link OneToManyHandler} for handling 1:n CONTEXTS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ContextualAttributesContext> SOLRFACETSEARCHCONFIG2CONTEXTUALATTRIBUTESCONTEXTRELATIONCONTEXTSHANDLER = new OneToManyHandler<ContextualAttributesContext>(
	ContextualattributevaluesConstants.TC.CONTEXTUALATTRIBUTESCONTEXT,
	false,
	"facetSearchConfigs",
	"facetSearchConfigsPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("contextual", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.solrfacetsearch.jalo.config.SolrIndexedProperty", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("contextualAttributesContext", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.store.BaseStore", Collections.unmodifiableMap(tmp));
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
	 * <i>Generated method</i> - Getter of the <code>SolrFacetSearchConfig.contexts</code> attribute.
	 * @return the contexts
	 */
	public List<ContextualAttributesContext> getContexts(final SessionContext ctx, final SolrFacetSearchConfig item)
	{
		return (List<ContextualAttributesContext>)SOLRFACETSEARCHCONFIG2CONTEXTUALATTRIBUTESCONTEXTRELATIONCONTEXTSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrFacetSearchConfig.contexts</code> attribute.
	 * @return the contexts
	 */
	public List<ContextualAttributesContext> getContexts(final SolrFacetSearchConfig item)
	{
		return getContexts( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrFacetSearchConfig.contexts</code> attribute. 
	 * @param value the contexts
	 */
	public void setContexts(final SessionContext ctx, final SolrFacetSearchConfig item, final List<ContextualAttributesContext> value)
	{
		SOLRFACETSEARCHCONFIG2CONTEXTUALATTRIBUTESCONTEXTRELATIONCONTEXTSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrFacetSearchConfig.contexts</code> attribute. 
	 * @param value the contexts
	 */
	public void setContexts(final SolrFacetSearchConfig item, final List<ContextualAttributesContext> value)
	{
		setContexts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to contexts. 
	 * @param value the item to add to contexts
	 */
	public void addToContexts(final SessionContext ctx, final SolrFacetSearchConfig item, final ContextualAttributesContext value)
	{
		SOLRFACETSEARCHCONFIG2CONTEXTUALATTRIBUTESCONTEXTRELATIONCONTEXTSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to contexts. 
	 * @param value the item to add to contexts
	 */
	public void addToContexts(final SolrFacetSearchConfig item, final ContextualAttributesContext value)
	{
		addToContexts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from contexts. 
	 * @param value the item to remove from contexts
	 */
	public void removeFromContexts(final SessionContext ctx, final SolrFacetSearchConfig item, final ContextualAttributesContext value)
	{
		SOLRFACETSEARCHCONFIG2CONTEXTUALATTRIBUTESCONTEXTRELATIONCONTEXTSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from contexts. 
	 * @param value the item to remove from contexts
	 */
	public void removeFromContexts(final SolrFacetSearchConfig item, final ContextualAttributesContext value)
	{
		removeFromContexts( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedProperty.contextual</code> attribute.
	 * @return the contextual
	 */
	public Boolean isContextual(final SessionContext ctx, final SolrIndexedProperty item)
	{
		return (Boolean)item.getProperty( ctx, ContextualattributevaluesConstants.Attributes.SolrIndexedProperty.CONTEXTUAL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedProperty.contextual</code> attribute.
	 * @return the contextual
	 */
	public Boolean isContextual(final SolrIndexedProperty item)
	{
		return isContextual( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedProperty.contextual</code> attribute. 
	 * @return the contextual
	 */
	public boolean isContextualAsPrimitive(final SessionContext ctx, final SolrIndexedProperty item)
	{
		Boolean value = isContextual( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SolrIndexedProperty.contextual</code> attribute. 
	 * @return the contextual
	 */
	public boolean isContextualAsPrimitive(final SolrIndexedProperty item)
	{
		return isContextualAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrIndexedProperty.contextual</code> attribute. 
	 * @param value the contextual
	 */
	public void setContextual(final SessionContext ctx, final SolrIndexedProperty item, final Boolean value)
	{
		item.setProperty(ctx, ContextualattributevaluesConstants.Attributes.SolrIndexedProperty.CONTEXTUAL,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrIndexedProperty.contextual</code> attribute. 
	 * @param value the contextual
	 */
	public void setContextual(final SolrIndexedProperty item, final Boolean value)
	{
		setContextual( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrIndexedProperty.contextual</code> attribute. 
	 * @param value the contextual
	 */
	public void setContextual(final SessionContext ctx, final SolrIndexedProperty item, final boolean value)
	{
		setContextual( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SolrIndexedProperty.contextual</code> attribute. 
	 * @param value the contextual
	 */
	public void setContextual(final SolrIndexedProperty item, final boolean value)
	{
		setContextual( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.contextualAttributesContext</code> attribute.
	 * @return the contextualAttributesContext - Contextual attribute context
	 */
	public ContextualAttributesContext getContextualAttributesContext(final SessionContext ctx, final BaseStore item)
	{
		return (ContextualAttributesContext)item.getProperty( ctx, ContextualattributevaluesConstants.Attributes.BaseStore.CONTEXTUALATTRIBUTESCONTEXT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>BaseStore.contextualAttributesContext</code> attribute.
	 * @return the contextualAttributesContext - Contextual attribute context
	 */
	public ContextualAttributesContext getContextualAttributesContext(final BaseStore item)
	{
		return getContextualAttributesContext( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.contextualAttributesContext</code> attribute. 
	 * @param value the contextualAttributesContext - Contextual attribute context
	 */
	public void setContextualAttributesContext(final SessionContext ctx, final BaseStore item, final ContextualAttributesContext value)
	{
		item.setProperty(ctx, ContextualattributevaluesConstants.Attributes.BaseStore.CONTEXTUALATTRIBUTESCONTEXT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>BaseStore.contextualAttributesContext</code> attribute. 
	 * @param value the contextualAttributesContext - Contextual attribute context
	 */
	public void setContextualAttributesContext(final BaseStore item, final ContextualAttributesContext value)
	{
		setContextualAttributesContext( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.contextualAttributeValues</code> attribute.
	 * @return the contextualAttributeValues
	 */
	public Collection<ContextualAttributeValue> getContextualAttributeValues(final SessionContext ctx, final Product item)
	{
		return PRODUCT2CONTEXTUALATTRIBUTEVALUERELCONTEXTUALATTRIBUTEVALUESHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.contextualAttributeValues</code> attribute.
	 * @return the contextualAttributeValues
	 */
	public Collection<ContextualAttributeValue> getContextualAttributeValues(final Product item)
	{
		return getContextualAttributeValues( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.contextualAttributeValues</code> attribute. 
	 * @param value the contextualAttributeValues
	 */
	public void setContextualAttributeValues(final SessionContext ctx, final Product item, final Collection<ContextualAttributeValue> value)
	{
		PRODUCT2CONTEXTUALATTRIBUTEVALUERELCONTEXTUALATTRIBUTEVALUESHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.contextualAttributeValues</code> attribute. 
	 * @param value the contextualAttributeValues
	 */
	public void setContextualAttributeValues(final Product item, final Collection<ContextualAttributeValue> value)
	{
		setContextualAttributeValues( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to contextualAttributeValues. 
	 * @param value the item to add to contextualAttributeValues
	 */
	public void addToContextualAttributeValues(final SessionContext ctx, final Product item, final ContextualAttributeValue value)
	{
		PRODUCT2CONTEXTUALATTRIBUTEVALUERELCONTEXTUALATTRIBUTEVALUESHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to contextualAttributeValues. 
	 * @param value the item to add to contextualAttributeValues
	 */
	public void addToContextualAttributeValues(final Product item, final ContextualAttributeValue value)
	{
		addToContextualAttributeValues( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from contextualAttributeValues. 
	 * @param value the item to remove from contextualAttributeValues
	 */
	public void removeFromContextualAttributeValues(final SessionContext ctx, final Product item, final ContextualAttributeValue value)
	{
		PRODUCT2CONTEXTUALATTRIBUTEVALUERELCONTEXTUALATTRIBUTEVALUESHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from contextualAttributeValues. 
	 * @param value the item to remove from contextualAttributeValues
	 */
	public void removeFromContextualAttributeValues(final Product item, final ContextualAttributeValue value)
	{
		removeFromContextualAttributeValues( getSession().getSessionContext(), item, value );
	}
	
	public ContextualAttributesContext createContextualAttributesContext(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ContextualattributevaluesConstants.TC.CONTEXTUALATTRIBUTESCONTEXT );
			return (ContextualAttributesContext)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ContextualAttributesContext : "+e.getMessage(), 0 );
		}
	}
	
	public ContextualAttributesContext createContextualAttributesContext(final Map attributeValues)
	{
		return createContextualAttributesContext( getSession().getSessionContext(), attributeValues );
	}
	
	public ContextualAttributeValue createContextualAttributeValue(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( ContextualattributevaluesConstants.TC.CONTEXTUALATTRIBUTEVALUE );
			return (ContextualAttributeValue)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating ContextualAttributeValue : "+e.getMessage(), 0 );
		}
	}
	
	public ContextualAttributeValue createContextualAttributeValue(final Map attributeValues)
	{
		return createContextualAttributeValue( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return ContextualattributevaluesConstants.EXTENSIONNAME;
	}
	
}
