/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gp.commerce.core.jalo;

import com.gp.commerce.gpcommerceaddon.constants.GpcommonaddonConstants;
import de.hybris.platform.cms2lib.components.ProductCarouselComponent;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link com.gp.commerce.core.jalo.GPTipsAndKnowledgeInfoComponent GPTipsAndKnowledgeInfoComponent}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGPTipsAndKnowledgeInfoComponent extends ProductCarouselComponent
{
	/** Qualifier of the <code>GPTipsAndKnowledgeInfoComponent.fetchCount</code> attribute **/
	public static final String FETCHCOUNT = "fetchCount";
	/** Qualifier of the <code>GPTipsAndKnowledgeInfoComponent.displayCount</code> attribute **/
	public static final String DISPLAYCOUNT = "displayCount";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(ProductCarouselComponent.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(FETCHCOUNT, AttributeMode.INITIAL);
		tmp.put(DISPLAYCOUNT, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfoComponent.displayCount</code> attribute.
	 * @return the displayCount - Header Text
	 */
	public Integer getDisplayCount(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, DISPLAYCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfoComponent.displayCount</code> attribute.
	 * @return the displayCount - Header Text
	 */
	public Integer getDisplayCount()
	{
		return getDisplayCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfoComponent.displayCount</code> attribute. 
	 * @return the displayCount - Header Text
	 */
	public int getDisplayCountAsPrimitive(final SessionContext ctx)
	{
		Integer value = getDisplayCount( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfoComponent.displayCount</code> attribute. 
	 * @return the displayCount - Header Text
	 */
	public int getDisplayCountAsPrimitive()
	{
		return getDisplayCountAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfoComponent.displayCount</code> attribute. 
	 * @param value the displayCount - Header Text
	 */
	public void setDisplayCount(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, DISPLAYCOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfoComponent.displayCount</code> attribute. 
	 * @param value the displayCount - Header Text
	 */
	public void setDisplayCount(final Integer value)
	{
		setDisplayCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfoComponent.displayCount</code> attribute. 
	 * @param value the displayCount - Header Text
	 */
	public void setDisplayCount(final SessionContext ctx, final int value)
	{
		setDisplayCount( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfoComponent.displayCount</code> attribute. 
	 * @param value the displayCount - Header Text
	 */
	public void setDisplayCount(final int value)
	{
		setDisplayCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfoComponent.fetchCount</code> attribute.
	 * @return the fetchCount - Header Text
	 */
	public Integer getFetchCount(final SessionContext ctx)
	{
		return (Integer)getProperty( ctx, FETCHCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfoComponent.fetchCount</code> attribute.
	 * @return the fetchCount - Header Text
	 */
	public Integer getFetchCount()
	{
		return getFetchCount( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfoComponent.fetchCount</code> attribute. 
	 * @return the fetchCount - Header Text
	 */
	public int getFetchCountAsPrimitive(final SessionContext ctx)
	{
		Integer value = getFetchCount( ctx );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>GPTipsAndKnowledgeInfoComponent.fetchCount</code> attribute. 
	 * @return the fetchCount - Header Text
	 */
	public int getFetchCountAsPrimitive()
	{
		return getFetchCountAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfoComponent.fetchCount</code> attribute. 
	 * @param value the fetchCount - Header Text
	 */
	public void setFetchCount(final SessionContext ctx, final Integer value)
	{
		setProperty(ctx, FETCHCOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfoComponent.fetchCount</code> attribute. 
	 * @param value the fetchCount - Header Text
	 */
	public void setFetchCount(final Integer value)
	{
		setFetchCount( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfoComponent.fetchCount</code> attribute. 
	 * @param value the fetchCount - Header Text
	 */
	public void setFetchCount(final SessionContext ctx, final int value)
	{
		setFetchCount( ctx,Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>GPTipsAndKnowledgeInfoComponent.fetchCount</code> attribute. 
	 * @param value the fetchCount - Header Text
	 */
	public void setFetchCount(final int value)
	{
		setFetchCount( getSession().getSessionContext(), value );
	}
	
}
