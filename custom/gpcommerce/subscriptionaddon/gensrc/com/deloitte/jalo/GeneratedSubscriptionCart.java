/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Apr 18, 2019 3:07:06 PM                     ---
 * ----------------------------------------------------------------
 */
package com.deloitte.jalo;

import com.deloitte.constants.SubscriptionaddonConstants;
import com.deloitte.jalo.SubscriptionSchedule;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.order.Cart SubscriptionCart}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSubscriptionCart extends Cart
{
	/** Qualifier of the <code>SubscriptionCart.frequency</code> attribute **/
	public static final String FREQUENCY = "frequency";
	/** Qualifier of the <code>SubscriptionCart.nextActivationDate</code> attribute **/
	public static final String NEXTACTIVATIONDATE = "nextActivationDate";
	/** Qualifier of the <code>SubscriptionCart.isActive</code> attribute **/
	public static final String ISACTIVE = "isActive";
	/** Qualifier of the <code>SubscriptionCart.subscriptionSchedule</code> attribute **/
	public static final String SUBSCRIPTIONSCHEDULE = "subscriptionSchedule";
	/**
	* {@link OneToManyHandler} for handling 1:n SUBSCRIPTIONSCHEDULE's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<SubscriptionSchedule> SUBSCRIPTIONSCHEDULEHANDLER = new OneToManyHandler<SubscriptionSchedule>(
	SubscriptionaddonConstants.TC.SUBSCRIPTIONSCHEDULE,
	false,
	"subscriptionCart",
	null,
	false,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(Cart.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(FREQUENCY, AttributeMode.INITIAL);
		tmp.put(NEXTACTIVATIONDATE, AttributeMode.INITIAL);
		tmp.put(ISACTIVE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionCart.frequency</code> attribute.
	 * @return the frequency - Subscription Frequency
	 */
	public EnumerationValue getFrequency(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, FREQUENCY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionCart.frequency</code> attribute.
	 * @return the frequency - Subscription Frequency
	 */
	public EnumerationValue getFrequency()
	{
		return getFrequency( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionCart.frequency</code> attribute. 
	 * @param value the frequency - Subscription Frequency
	 */
	public void setFrequency(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, FREQUENCY,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionCart.frequency</code> attribute. 
	 * @param value the frequency - Subscription Frequency
	 */
	public void setFrequency(final EnumerationValue value)
	{
		setFrequency( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionCart.isActive</code> attribute.
	 * @return the isActive - Active status
	 */
	public Boolean isIsActive(final SessionContext ctx)
	{
		return (Boolean)getProperty( ctx, ISACTIVE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionCart.isActive</code> attribute.
	 * @return the isActive - Active status
	 */
	public Boolean isIsActive()
	{
		return isIsActive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionCart.isActive</code> attribute. 
	 * @return the isActive - Active status
	 */
	public boolean isIsActiveAsPrimitive(final SessionContext ctx)
	{
		Boolean value = isIsActive( ctx );
		return value != null ? value.booleanValue() : false;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionCart.isActive</code> attribute. 
	 * @return the isActive - Active status
	 */
	public boolean isIsActiveAsPrimitive()
	{
		return isIsActiveAsPrimitive( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionCart.isActive</code> attribute. 
	 * @param value the isActive - Active status
	 */
	public void setIsActive(final SessionContext ctx, final Boolean value)
	{
		setProperty(ctx, ISACTIVE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionCart.isActive</code> attribute. 
	 * @param value the isActive - Active status
	 */
	public void setIsActive(final Boolean value)
	{
		setIsActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionCart.isActive</code> attribute. 
	 * @param value the isActive - Active status
	 */
	public void setIsActive(final SessionContext ctx, final boolean value)
	{
		setIsActive( ctx,Boolean.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionCart.isActive</code> attribute. 
	 * @param value the isActive - Active status
	 */
	public void setIsActive(final boolean value)
	{
		setIsActive( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionCart.nextActivationDate</code> attribute.
	 * @return the nextActivationDate - Next Activation Date
	 */
	public Date getNextActivationDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, NEXTACTIVATIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionCart.nextActivationDate</code> attribute.
	 * @return the nextActivationDate - Next Activation Date
	 */
	public Date getNextActivationDate()
	{
		return getNextActivationDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionCart.nextActivationDate</code> attribute. 
	 * @param value the nextActivationDate - Next Activation Date
	 */
	public void setNextActivationDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, NEXTACTIVATIONDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionCart.nextActivationDate</code> attribute. 
	 * @param value the nextActivationDate - Next Activation Date
	 */
	public void setNextActivationDate(final Date value)
	{
		setNextActivationDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionCart.subscriptionSchedule</code> attribute.
	 * @return the subscriptionSchedule
	 */
	public List<SubscriptionSchedule> getSubscriptionSchedule(final SessionContext ctx)
	{
		return (List<SubscriptionSchedule>)SUBSCRIPTIONSCHEDULEHANDLER.getValues( ctx, this );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionCart.subscriptionSchedule</code> attribute.
	 * @return the subscriptionSchedule
	 */
	public List<SubscriptionSchedule> getSubscriptionSchedule()
	{
		return getSubscriptionSchedule( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionCart.subscriptionSchedule</code> attribute. 
	 * @param value the subscriptionSchedule
	 */
	public void setSubscriptionSchedule(final SessionContext ctx, final List<SubscriptionSchedule> value)
	{
		SUBSCRIPTIONSCHEDULEHANDLER.setValues( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionCart.subscriptionSchedule</code> attribute. 
	 * @param value the subscriptionSchedule
	 */
	public void setSubscriptionSchedule(final List<SubscriptionSchedule> value)
	{
		setSubscriptionSchedule( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to subscriptionSchedule. 
	 * @param value the item to add to subscriptionSchedule
	 */
	public void addToSubscriptionSchedule(final SessionContext ctx, final SubscriptionSchedule value)
	{
		SUBSCRIPTIONSCHEDULEHANDLER.addValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to subscriptionSchedule. 
	 * @param value the item to add to subscriptionSchedule
	 */
	public void addToSubscriptionSchedule(final SubscriptionSchedule value)
	{
		addToSubscriptionSchedule( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from subscriptionSchedule. 
	 * @param value the item to remove from subscriptionSchedule
	 */
	public void removeFromSubscriptionSchedule(final SessionContext ctx, final SubscriptionSchedule value)
	{
		SUBSCRIPTIONSCHEDULEHANDLER.removeValue( ctx, this, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from subscriptionSchedule. 
	 * @param value the item to remove from subscriptionSchedule
	 */
	public void removeFromSubscriptionSchedule(final SubscriptionSchedule value)
	{
		removeFromSubscriptionSchedule( getSession().getSessionContext(), value );
	}
	
}
