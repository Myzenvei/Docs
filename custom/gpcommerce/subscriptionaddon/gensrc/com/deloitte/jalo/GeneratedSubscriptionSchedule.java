/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at Apr 18, 2019 3:07:06 PM                     ---
 * ----------------------------------------------------------------
 */
package com.deloitte.jalo;

import com.deloitte.constants.SubscriptionaddonConstants;
import com.deloitte.jalo.SubscriptionCart;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link de.hybris.platform.jalo.GenericItem SubscriptionSchedule}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedSubscriptionSchedule extends GenericItem
{
	/** Qualifier of the <code>SubscriptionSchedule.nextActivationDate</code> attribute **/
	public static final String NEXTACTIVATIONDATE = "nextActivationDate";
	/** Qualifier of the <code>SubscriptionSchedule.promotionId</code> attribute **/
	public static final String PROMOTIONID = "promotionId";
	/** Qualifier of the <code>SubscriptionSchedule.subscriptionStatus</code> attribute **/
	public static final String SUBSCRIPTIONSTATUS = "subscriptionStatus";
	/** Qualifier of the <code>SubscriptionSchedule.subscriptionCart</code> attribute **/
	public static final String SUBSCRIPTIONCART = "subscriptionCart";
	/**
	* {@link BidirectionalOneToManyHandler} for handling 1:n SUBSCRIPTIONCART's relation attributes from 'one' side.
	**/
	protected static final BidirectionalOneToManyHandler<GeneratedSubscriptionSchedule> SUBSCRIPTIONCARTHANDLER = new BidirectionalOneToManyHandler<GeneratedSubscriptionSchedule>(
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
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put(NEXTACTIVATIONDATE, AttributeMode.INITIAL);
		tmp.put(PROMOTIONID, AttributeMode.INITIAL);
		tmp.put(SUBSCRIPTIONSTATUS, AttributeMode.INITIAL);
		tmp.put(SUBSCRIPTIONCART, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes) throws JaloBusinessException
	{
		SUBSCRIPTIONCARTHANDLER.newInstance(ctx, allAttributes);
		return super.createItem( ctx, type, allAttributes );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionSchedule.nextActivationDate</code> attribute.
	 * @return the nextActivationDate - Next Activation Date
	 */
	public Date getNextActivationDate(final SessionContext ctx)
	{
		return (Date)getProperty( ctx, NEXTACTIVATIONDATE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionSchedule.nextActivationDate</code> attribute.
	 * @return the nextActivationDate - Next Activation Date
	 */
	public Date getNextActivationDate()
	{
		return getNextActivationDate( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionSchedule.nextActivationDate</code> attribute. 
	 * @param value the nextActivationDate - Next Activation Date
	 */
	public void setNextActivationDate(final SessionContext ctx, final Date value)
	{
		setProperty(ctx, NEXTACTIVATIONDATE,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionSchedule.nextActivationDate</code> attribute. 
	 * @param value the nextActivationDate - Next Activation Date
	 */
	public void setNextActivationDate(final Date value)
	{
		setNextActivationDate( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionSchedule.promotionId</code> attribute.
	 * @return the promotionId - promotion Id
	 */
	public Collection<String> getPromotionId(final SessionContext ctx)
	{
		Collection<String> coll = (Collection<String>)getProperty( ctx, PROMOTIONID);
		return coll != null ? coll : Collections.EMPTY_LIST;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionSchedule.promotionId</code> attribute.
	 * @return the promotionId - promotion Id
	 */
	public Collection<String> getPromotionId()
	{
		return getPromotionId( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionSchedule.promotionId</code> attribute. 
	 * @param value the promotionId - promotion Id
	 */
	public void setPromotionId(final SessionContext ctx, final Collection<String> value)
	{
		setProperty(ctx, PROMOTIONID,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionSchedule.promotionId</code> attribute. 
	 * @param value the promotionId - promotion Id
	 */
	public void setPromotionId(final Collection<String> value)
	{
		setPromotionId( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionSchedule.subscriptionCart</code> attribute.
	 * @return the subscriptionCart
	 */
	public SubscriptionCart getSubscriptionCart(final SessionContext ctx)
	{
		return (SubscriptionCart)getProperty( ctx, SUBSCRIPTIONCART);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionSchedule.subscriptionCart</code> attribute.
	 * @return the subscriptionCart
	 */
	public SubscriptionCart getSubscriptionCart()
	{
		return getSubscriptionCart( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionSchedule.subscriptionCart</code> attribute. 
	 * @param value the subscriptionCart
	 */
	public void setSubscriptionCart(final SessionContext ctx, final SubscriptionCart value)
	{
		SUBSCRIPTIONCARTHANDLER.addValue( ctx, value, this  );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionSchedule.subscriptionCart</code> attribute. 
	 * @param value the subscriptionCart
	 */
	public void setSubscriptionCart(final SubscriptionCart value)
	{
		setSubscriptionCart( getSession().getSessionContext(), value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionSchedule.subscriptionStatus</code> attribute.
	 * @return the subscriptionStatus - subscription Status
	 */
	public EnumerationValue getSubscriptionStatus(final SessionContext ctx)
	{
		return (EnumerationValue)getProperty( ctx, SUBSCRIPTIONSTATUS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>SubscriptionSchedule.subscriptionStatus</code> attribute.
	 * @return the subscriptionStatus - subscription Status
	 */
	public EnumerationValue getSubscriptionStatus()
	{
		return getSubscriptionStatus( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionSchedule.subscriptionStatus</code> attribute. 
	 * @param value the subscriptionStatus - subscription Status
	 */
	public void setSubscriptionStatus(final SessionContext ctx, final EnumerationValue value)
	{
		setProperty(ctx, SUBSCRIPTIONSTATUS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>SubscriptionSchedule.subscriptionStatus</code> attribute. 
	 * @param value the subscriptionStatus - subscription Status
	 */
	public void setSubscriptionStatus(final EnumerationValue value)
	{
		setSubscriptionStatus( getSession().getSessionContext(), value );
	}
	
}
