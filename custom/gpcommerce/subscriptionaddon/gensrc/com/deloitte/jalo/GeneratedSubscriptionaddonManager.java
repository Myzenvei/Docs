/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Apr 18, 2019 3:07:06 PM                     ---
 * ----------------------------------------------------------------
 */
package com.deloitte.jalo;

import com.deloitte.constants.SubscriptionaddonConstants;
import com.deloitte.jalo.SubscriptionCart;
import com.deloitte.jalo.SubscriptionSchedule;
import de.hybris.platform.europe1.jalo.PDTRow;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type <code>SubscriptionaddonManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSubscriptionaddonManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("frequency", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.europe1.jalo.PriceRow", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("subscribable", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.product.Product", Collections.unmodifiableMap(tmp));
		tmp = new HashMap<String, AttributeMode>();
		tmp.put("frequency", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.jalo.order.AbstractOrderEntry", Collections.unmodifiableMap(tmp));
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
	
	public SubscriptionCart createSubscriptionCart(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionaddonConstants.TC.SUBSCRIPTIONCART );
			return (SubscriptionCart)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating SubscriptionCart : "+e.getMessage(), 0 );
		}
	}
	
	public SubscriptionCart createSubscriptionCart(final Map attributeValues)
	{
		return createSubscriptionCart( getSession().getSessionContext(), attributeValues );
	}
	
	public SubscriptionSchedule createSubscriptionSchedule(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( SubscriptionaddonConstants.TC.SUBSCRIPTIONSCHEDULE );
			return (SubscriptionSchedule)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating SubscriptionSchedule : "+e.getMessage(), 0 );
		}
	}
	
	public SubscriptionSchedule createSubscriptionSchedule(final Map attributeValues)
	{
		return createSubscriptionSchedule( getSession().getSessionContext(), attributeValues );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.frequency</code> attribute.
	 * @return the frequency - Subscription Frequency
	 */
	public EnumerationValue getFrequency(final SessionContext ctx, final PriceRow item)
	{
		return (EnumerationValue)item.getProperty( ctx, SubscriptionaddonConstants.Attributes.PriceRow.FREQUENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>PriceRow.frequency</code> attribute.
	 * @return the frequency - Subscription Frequency
	 */
	public EnumerationValue getFrequency(final PriceRow item)
	{
		return getFrequency( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PriceRow.frequency</code> attribute. 
	 * @param value the frequency - Subscription Frequency
	 */
	public void setFrequency(final SessionContext ctx, final PriceRow item, final EnumerationValue value)
	{
		item.setProperty(ctx, SubscriptionaddonConstants.Attributes.PriceRow.FREQUENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>PriceRow.frequency</code> attribute. 
	 * @param value the frequency - Subscription Frequency
	 */
	public void setFrequency(final PriceRow item, final EnumerationValue value)
	{
		setFrequency( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.frequency</code> attribute.
	 * @return the frequency
	 */
	public EnumerationValue getFrequency(final SessionContext ctx, final AbstractOrderEntry item)
	{
		return (EnumerationValue)item.getProperty( ctx, SubscriptionaddonConstants.Attributes.AbstractOrderEntry.FREQUENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>AbstractOrderEntry.frequency</code> attribute.
	 * @return the frequency
	 */
	public EnumerationValue getFrequency(final AbstractOrderEntry item)
	{
		return getFrequency( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.frequency</code> attribute. 
	 * @param value the frequency
	 */
	public void setFrequency(final SessionContext ctx, final AbstractOrderEntry item, final EnumerationValue value)
	{
		item.setProperty(ctx, SubscriptionaddonConstants.Attributes.AbstractOrderEntry.FREQUENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>AbstractOrderEntry.frequency</code> attribute. 
	 * @param value the frequency
	 */
	public void setFrequency(final AbstractOrderEntry item, final EnumerationValue value)
	{
		setFrequency( getSession().getSessionContext(), item, value );
	}
	
	@Override
	public String getName()
	{
		return SubscriptionaddonConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.subscribable</code> attribute.
	 * @return the subscribable
	 */
	public Boolean isSubscribable(final SessionContext ctx, final Product item)
	{
		return (Boolean)item.getProperty( ctx, SubscriptionaddonConstants.Attributes.Product.SUBSCRIBABLE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.subscribable</code> attribute.
	 * @return the subscribable
	 */
	public Boolean isSubscribable(final Product item)
	{
		return isSubscribable( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.subscribable</code> attribute. 
	 * @return the subscribable
	 */
	public boolean isSubscribableAsPrimitive(final SessionContext ctx, final Product item)
	{
		Boolean value = isSubscribable( ctx,item );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Product.subscribable</code> attribute. 
	 * @return the subscribable
	 */
	public boolean isSubscribableAsPrimitive(final Product item)
	{
		return isSubscribableAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.subscribable</code> attribute. 
	 * @param value the subscribable
	 */
	public void setSubscribable(final SessionContext ctx, final Product item, final Boolean value)
	{
		item.setProperty(ctx, SubscriptionaddonConstants.Attributes.Product.SUBSCRIBABLE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.subscribable</code> attribute. 
	 * @param value the subscribable
	 */
	public void setSubscribable(final Product item, final Boolean value)
	{
		setSubscribable( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.subscribable</code> attribute. 
	 * @param value the subscribable
	 */
	public void setSubscribable(final SessionContext ctx, final Product item, final boolean value)
	{
		setSubscribable( ctx, item, Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Product.subscribable</code> attribute. 
	 * @param value the subscribable
	 */
	public void setSubscribable(final Product item, final boolean value)
	{
		setSubscribable( getSession().getSessionContext(), item, value );
	}
	
}
