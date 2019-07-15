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
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.solrfacetsearch.jalo.config.SolrFacetSearchConfig;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem ContextualAttributesContext}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedContextualAttributesContext extends GenericItem
{
	/** Qualifier of the <code>ContextualAttributesContext.name</code> attribute **/
	public static final String NAME = "name";
	/** Qualifier of the <code>ContextualAttributesContext.code</code> attribute **/
	public static final String CODE = "code";
	/** Qualifier of the <code>ContextualAttributesContext.facetSearchConfigsPOS</code> attribute **/
	public static final String FACETSEARCHCONFIGSPOS = "facetSearchConfigsPOS";
	/** Qualifier of the <code>ContextualAttributesContext.facetSearchConfigs</code> attribute **/
	public static final String FACETSEARCHCONFIGS = "facetSearchConfigs";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n FACETSEARCHCONFIGS's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedContextualAttributesContext> FACETSEARCHCONFIGSHANDLER = new BidirectionalOneToManyHandler<GeneratedContextualAttributesContext>(
	ContextualattributevaluesConstants.TC.CONTEXTUALATTRIBUTESCONTEXT,
	false,
	"facetSearchConfigs",
	"facetSearchConfigsPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(NAME, AttributeMode.INITIAL);
		tmp.put(CODE, AttributeMode.INITIAL);
		tmp.put(FACETSEARCHCONFIGSPOS, AttributeMode.INITIAL);
		tmp.put(FACETSEARCHCONFIGS, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributesContext.code</code> attribute.
	 * @return the code
	 */
	public String getCode(final SessionContext ctx)
	{
		return (String)getProperty( ctx, CODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributesContext.code</code> attribute.
	 * @return the code
	 */
	public String getCode()
	{
		return getCode( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributesContext.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final SessionContext ctx, final String value)
	{
		setProperty(ctx, CODE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributesContext.code</code> attribute. 
	 * @param value the code
	 */
	public void setCode(final String value)
	{
		setCode( getSession().getSessionContext(), value );
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		FACETSEARCHCONFIGSHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributesContext.facetSearchConfigs</code> attribute.
	 * @return the facetSearchConfigs
	 */
	public SolrFacetSearchConfig getFacetSearchConfigs(final SessionContext ctx)
	{
		return (SolrFacetSearchConfig)getProperty( ctx, FACETSEARCHCONFIGS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributesContext.facetSearchConfigs</code> attribute.
	 * @return the facetSearchConfigs
	 */
	public SolrFacetSearchConfig getFacetSearchConfigs()
	{
		return getFacetSearchConfigs( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributesContext.facetSearchConfigs</code> attribute. 
	 * @param value the facetSearchConfigs
	 */
	public void setFacetSearchConfigs(final SessionContext ctx, final SolrFacetSearchConfig value)
	{
		FACETSEARCHCONFIGSHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributesContext.facetSearchConfigs</code> attribute. 
	 * @param value the facetSearchConfigs
	 */
	public void setFacetSearchConfigs(final SolrFacetSearchConfig value)
	{
		setFacetSearchConfigs( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributesContext.facetSearchConfigsPOS</code> attribute.
	 * @return the facetSearchConfigsPOS
	 */
	 Integer getFacetSearchConfigsPOS(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, FACETSEARCHCONFIGSPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributesContext.facetSearchConfigsPOS</code> attribute.
	 * @return the facetSearchConfigsPOS
	 */
	 Integer getFacetSearchConfigsPOS()
	{
		return getFacetSearchConfigsPOS( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributesContext.facetSearchConfigsPOS</code> attribute. 
	 * @return the facetSearchConfigsPOS
	 */
	 int getFacetSearchConfigsPOSAsPrimitive(final SessionContext ctx)
	{
		Integer value = getFacetSearchConfigsPOS( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributesContext.facetSearchConfigsPOS</code> attribute. 
	 * @return the facetSearchConfigsPOS
	 */
	 int getFacetSearchConfigsPOSAsPrimitive()
	{
		return getFacetSearchConfigsPOSAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributesContext.facetSearchConfigsPOS</code> attribute. 
	 * @param value the facetSearchConfigsPOS
	 */
	 void setFacetSearchConfigsPOS(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, FACETSEARCHCONFIGSPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributesContext.facetSearchConfigsPOS</code> attribute. 
	 * @param value the facetSearchConfigsPOS
	 */
	 void setFacetSearchConfigsPOS(final Integer value)
	{
		setFacetSearchConfigsPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributesContext.facetSearchConfigsPOS</code> attribute. 
	 * @param value the facetSearchConfigsPOS
	 */
	 void setFacetSearchConfigsPOS(final SessionContext ctx, final int value)
	{
		setFacetSearchConfigsPOS( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributesContext.facetSearchConfigsPOS</code> attribute. 
	 * @param value the facetSearchConfigsPOS
	 */
	 void setFacetSearchConfigsPOS(final int value)
	{
		setFacetSearchConfigsPOS( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributesContext.name</code> attribute.
	 * @return the name
	 */
	public String getName(final SessionContext ctx)
	{
		return (String)getProperty( ctx, NAME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ContextualAttributesContext.name</code> attribute.
	 * @return the name
	 */
	public String getName()
	{
		return getName( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributesContext.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final SessionContext ctx, final String value)
	{
		setProperty(ctx, NAME,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ContextualAttributesContext.name</code> attribute. 
	 * @param value the name
	 */
	public void setName(final String value)
	{
		setName( getSession().getSessionContext(), value );
	}
	
}
