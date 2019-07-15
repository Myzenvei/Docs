/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 12 Jul, 2019 11:25:50 AM                    ---
 * ----------------------------------------------------------------
 */
package com.gpsapreturnorder.jalo;

import com.gpsapreturnorder.constants.GpsapreturnorderConstants;
import com.gpsapreturnorder.jalo.GPRefundEntry;
import de.hybris.platform.basecommerce.constants.BasecommerceConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.returns.jalo.ReturnRequest;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generated class for type <code>GpsapreturnorderManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedGpsapreturnorderManager extends Extension
{
	/**
	* {@link OneToManyHandler} for handling 1:n RETURNREQUESTS's relation attributes from 'many' side.
	**/
	protected static final OneToManyHandler<ReturnRequest> CONSIGNMENT2RETURNREQUESTRETURNREQUESTSHANDLER = new OneToManyHandler<ReturnRequest>(
	BasecommerceConstants.TC.RETURNREQUEST,
	false,
	"consignment",
	"consignmentPOS",
	true,
	true,
	CollectionType.LIST
	);
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>();
		tmp.put("retryCount", AttributeMode.INITIAL);
		tmp.put("consignmentPOS", AttributeMode.INITIAL);
		tmp.put("consignment", AttributeMode.INITIAL);
		ttmp.put("de.hybris.platform.returns.jalo.ReturnRequest", Collections.unmodifiableMap(tmp));
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
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.consignment</code> attribute.
	 * @return the consignment
	 */
	public Consignment getConsignment(final SessionContext ctx, final ReturnRequest item)
	{
		return (Consignment)item.getProperty( ctx, GpsapreturnorderConstants.Attributes.ReturnRequest.CONSIGNMENT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.consignment</code> attribute.
	 * @return the consignment
	 */
	public Consignment getConsignment(final ReturnRequest item)
	{
		return getConsignment( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.consignment</code> attribute. 
	 * @param value the consignment
	 */
	public void setConsignment(final SessionContext ctx, final ReturnRequest item, final Consignment value)
	{
		item.setProperty(ctx, GpsapreturnorderConstants.Attributes.ReturnRequest.CONSIGNMENT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.consignment</code> attribute. 
	 * @param value the consignment
	 */
	public void setConsignment(final ReturnRequest item, final Consignment value)
	{
		setConsignment( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.consignmentPOS</code> attribute.
	 * @return the consignmentPOS
	 */
	 Integer getConsignmentPOS(final SessionContext ctx, final ReturnRequest item)
	{
		return (Integer)item.getProperty( ctx, GpsapreturnorderConstants.Attributes.ReturnRequest.CONSIGNMENTPOS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.consignmentPOS</code> attribute.
	 * @return the consignmentPOS
	 */
	 Integer getConsignmentPOS(final ReturnRequest item)
	{
		return getConsignmentPOS( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.consignmentPOS</code> attribute. 
	 * @return the consignmentPOS
	 */
	 int getConsignmentPOSAsPrimitive(final SessionContext ctx, final ReturnRequest item)
	{
		Integer value = getConsignmentPOS( ctx,item );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.consignmentPOS</code> attribute. 
	 * @return the consignmentPOS
	 */
	 int getConsignmentPOSAsPrimitive(final ReturnRequest item)
	{
		return getConsignmentPOSAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.consignmentPOS</code> attribute. 
	 * @param value the consignmentPOS
	 */
	 void setConsignmentPOS(final SessionContext ctx, final ReturnRequest item, final Integer value)
	{
		item.setProperty(ctx, GpsapreturnorderConstants.Attributes.ReturnRequest.CONSIGNMENTPOS,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.consignmentPOS</code> attribute. 
	 * @param value the consignmentPOS
	 */
	 void setConsignmentPOS(final ReturnRequest item, final Integer value)
	{
		setConsignmentPOS( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.consignmentPOS</code> attribute. 
	 * @param value the consignmentPOS
	 */
	 void setConsignmentPOS(final SessionContext ctx, final ReturnRequest item, final int value)
	{
		setConsignmentPOS( ctx, item, Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.consignmentPOS</code> attribute. 
	 * @param value the consignmentPOS
	 */
	 void setConsignmentPOS(final ReturnRequest item, final int value)
	{
		setConsignmentPOS( getSession().getSessionContext(), item, value );
	}
	
	public GPRefundEntry createGPRefundEntry(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( GpsapreturnorderConstants.TC.GPREFUNDENTRY );
			return (GPRefundEntry)type.newInstance( ctx, attributeValues );
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
			throw new JaloSystemException( e ,"error creating GPRefundEntry : "+e.getMessage(), 0 );
		}
	}
	
	public GPRefundEntry createGPRefundEntry(final Map attributeValues)
	{
		return createGPRefundEntry( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return GpsapreturnorderConstants.EXTENSIONNAME;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.retryCount</code> attribute.
	 * @return the retryCount
	 */
	public Integer getRetryCount(final SessionContext ctx, final ReturnRequest item)
	{
		return (Integer)item.getProperty( ctx, GpsapreturnorderConstants.Attributes.ReturnRequest.RETRYCOUNT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.retryCount</code> attribute.
	 * @return the retryCount
	 */
	public Integer getRetryCount(final ReturnRequest item)
	{
		return getRetryCount( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.retryCount</code> attribute. 
	 * @return the retryCount
	 */
	public int getRetryCountAsPrimitive(final SessionContext ctx, final ReturnRequest item)
	{
		Integer value = getRetryCount( ctx,item );
		return value != null ? value.intValue() : 0;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>ReturnRequest.retryCount</code> attribute. 
	 * @return the retryCount
	 */
	public int getRetryCountAsPrimitive(final ReturnRequest item)
	{
		return getRetryCountAsPrimitive( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.retryCount</code> attribute. 
	 * @param value the retryCount
	 */
	public void setRetryCount(final SessionContext ctx, final ReturnRequest item, final Integer value)
	{
		item.setProperty(ctx, GpsapreturnorderConstants.Attributes.ReturnRequest.RETRYCOUNT,value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.retryCount</code> attribute. 
	 * @param value the retryCount
	 */
	public void setRetryCount(final ReturnRequest item, final Integer value)
	{
		setRetryCount( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.retryCount</code> attribute. 
	 * @param value the retryCount
	 */
	public void setRetryCount(final SessionContext ctx, final ReturnRequest item, final int value)
	{
		setRetryCount( ctx, item, Integer.valueOf( value ) );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>ReturnRequest.retryCount</code> attribute. 
	 * @param value the retryCount
	 */
	public void setRetryCount(final ReturnRequest item, final int value)
	{
		setRetryCount( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consignment.returnRequests</code> attribute.
	 * @return the returnRequests
	 */
	public List<ReturnRequest> getReturnRequests(final SessionContext ctx, final Consignment item)
	{
		return (List<ReturnRequest>)CONSIGNMENT2RETURNREQUESTRETURNREQUESTSHANDLER.getValues( ctx, item );
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>Consignment.returnRequests</code> attribute.
	 * @return the returnRequests
	 */
	public List<ReturnRequest> getReturnRequests(final Consignment item)
	{
		return getReturnRequests( getSession().getSessionContext(), item );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consignment.returnRequests</code> attribute. 
	 * @param value the returnRequests
	 */
	public void setReturnRequests(final SessionContext ctx, final Consignment item, final List<ReturnRequest> value)
	{
		CONSIGNMENT2RETURNREQUESTRETURNREQUESTSHANDLER.setValues( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>Consignment.returnRequests</code> attribute. 
	 * @param value the returnRequests
	 */
	public void setReturnRequests(final Consignment item, final List<ReturnRequest> value)
	{
		setReturnRequests( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to returnRequests. 
	 * @param value the item to add to returnRequests
	 */
	public void addToReturnRequests(final SessionContext ctx, final Consignment item, final ReturnRequest value)
	{
		CONSIGNMENT2RETURNREQUESTRETURNREQUESTSHANDLER.addValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Adds <code>value</code> to returnRequests. 
	 * @param value the item to add to returnRequests
	 */
	public void addToReturnRequests(final Consignment item, final ReturnRequest value)
	{
		addToReturnRequests( getSession().getSessionContext(), item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from returnRequests. 
	 * @param value the item to remove from returnRequests
	 */
	public void removeFromReturnRequests(final SessionContext ctx, final Consignment item, final ReturnRequest value)
	{
		CONSIGNMENT2RETURNREQUESTRETURNREQUESTSHANDLER.removeValue( ctx, item, value );
	}
	
	/**
	 * <i>Generated method</i> - Removes <code>value</code> from returnRequests. 
	 * @param value the item to remove from returnRequests
	 */
	public void removeFromReturnRequests(final Consignment item, final ReturnRequest value)
	{
		removeFromReturnRequests( getSession().getSessionContext(), item, value );
	}
	
}
