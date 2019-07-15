/**
 *
 */
package com.gpintegration.core.cronjob;

import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

import com.gp.commerce.core.model.ShippingNotificationModel;
import com.gp.commerce.core.services.GPConsignmentTrackingService;
import com.gp.commerce.facade.data.NsConsignmentData;
import com.gp.commerce.facade.data.NsItemData;
import com.gpintegration.core.services.event.GPOrderStatusUpdateEvent;
import com.gpintegration.dto.netsuite.GPNetsuiteCCAckDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteOrderUpdateDTO;
import com.gpintegration.dto.netsuite.GPNetsuiteResponseDTO;
import com.gpintegration.exception.GPIntegrationException;
import com.gpintegration.service.GPNetsuiteReplicationService;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * @author vdannina
 *
 */
public class GPOrderStatusUpdateNSCronJob extends AbstractJobPerformable<CronJobModel> {
	private static final String NS = "NS";
	private static final Logger LOG = Logger.getLogger(GPOrderStatusUpdateNSCronJob.class);
	@Resource
	private TimeService timeService;
	@Resource
	private UserService userService;
	@Resource
	private EventService eventService;
	@Resource
	private ConfigurationService configurationService;
	@Resource
	private GPNetsuiteReplicationService gpNetsuiteReplicationService;
	@Resource
	private GPConsignmentTrackingService gpConsignmentTrackingService;
	private final String TRACKING_URL_PREFIX = "gp.netsuite.customer.order.trackingUrl";
	private final String INVALID_CONSIGNMENT = "gp.error.invalid.consignment.msg";
	private final String IS_NETSUITE_ENV_SPECIFIC = "gp.netsuite.is.env.specific";
	

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.
	 * hybris.platform.cronjob.model. CronJobModel )
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0) {
		Configuration configuration = configurationService.getConfiguration();
		LOG.debug("Fetching orders status from NS");
		try {
			GPNetsuiteOrderUpdateDTO shipments = gpNetsuiteReplicationService.getShippedOrders();
			if (null != shipments.getShipments()) {
				for (NsConsignmentData consignmentData : shipments.getShipments()) {
					if (clearAbortRequestedIfNeeded(arg0))
					{
						LOG.debug("The job is aborted.");
						return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
					}
					if(!updateOrderStatus(consignmentData)) {
						if(configuration.getBoolean(IS_NETSUITE_ENV_SPECIFIC)) {
							ackNetSuite(configuration.getString(INVALID_CONSIGNMENT),consignmentData.getNsPaymentId(),"error");
						}
					}
					else {
						LOG.debug("update from Ns is successfully saved in shipping table, consignmentID: "+consignmentData.getHybrisConsignmentId());
					}
				}
			}
			else
			{
				LOG.info("No consignments are shipped");
			}
		} catch (GPIntegrationException e) {
			LOG.error("Fetching orders status from NS failed" + e.getMessage(),e);
		}
		LOG.debug("Fetching orders status from NS sucessfull");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}
	
	public boolean updateOrderStatus(NsConsignmentData consignmentData) {
		Configuration configuration = configurationService.getConfiguration();
		try {
			ConsignmentModel consignmentModel = gpConsignmentTrackingService.getConsignmentByCode(consignmentData.getHybrisConsignmentId());
			if (null != consignmentModel) {
				if (null != consignmentData.getNsPaymentId()) {
					consignmentModel.setPaymentId(consignmentData.getNsPaymentId());
					modelService.save(consignmentModel);
				}
				ShippingNotificationModel shippingNotificationModel = new ShippingNotificationModel();
				for (NsItemData item : consignmentData.getItems()) {
					if(!saveShippingInfo(consignmentData, configuration, shippingNotificationModel, item)) {
						return false;
					}
				}
				return true;

			} else {
				LOG.error(configuration.getString(INVALID_CONSIGNMENT));
				return false;
			}
		} catch (Exception e) {
			LOG.error(configuration.getString(INVALID_CONSIGNMENT),e);
			return false;
		}
	}

	private void ackNetSuite(String msg, String nsPaymentId, String status) {
		GPNetsuiteCCAckDTO ack = new GPNetsuiteCCAckDTO();
		ack.setMessage(msg);
		ack.setNsPaymentId(nsPaymentId);
		ack.setStatus(status);
		try {
			GPNetsuiteResponseDTO response = gpNetsuiteReplicationService.acknowledgeCCOfPayment(ack);
			LOG.info("Ack from Net Suite, Debit capture status:"+response.getStatus());
		} catch (GPIntegrationException e) {
			LOG.error("Ack to Netsuite failed",e);
		}
		
	}

	private boolean saveShippingInfo(NsConsignmentData consignmentData, Configuration configuration,
			ShippingNotificationModel shippingNotificationModel, NsItemData item) {
		try {
		LOG.debug("Consignment:"+consignmentData.getHybrisConsignmentId()+" is fullfilled and processing for billing now");
		shippingNotificationModel.setConsignmentCode(consignmentData.getHybrisConsignmentId());
		shippingNotificationModel.setConsignmentEntryNumber(item.getConsignmentEntryNumber());
		shippingNotificationModel.setQuantityConfirmed(item.getQuantityConfirmed());
		shippingNotificationModel.setQuantityRejected(item.getQuantityRejected());
		shippingNotificationModel.setReasonForRejection(item.getReasonForRejection());
		shippingNotificationModel.setQuantityShipped(item.getQuantityShipped());
		shippingNotificationModel.setTrackingNumber(item.getTrackingId());
		shippingNotificationModel.setTrackingURL(configuration.getString(TRACKING_URL_PREFIX)+item.getTrackingId());
		shippingNotificationModel.setSource(NS);
		String code = String.valueOf(setShippingNotificationUID());
		shippingNotificationModel.setCode(code);
		shippingNotificationModel.setIsCCAcknowledged(false);
		modelService.save(shippingNotificationModel);
		LOG.debug("Consignment is saved into shipping table");
		return true;
		}
		catch(Exception e) {
			if(null != consignmentData.getHybrisConsignmentId())
				LOG.error("Inavlid consignment :" + consignmentData.getHybrisConsignmentId() +e.getMessage(),e);
			return false;
		}
	}

	
	protected TimeService getTimeService() {
		return timeService;
	}

	public void setTimeService(final TimeService timeService) {
		this.timeService = timeService;
	}

	protected UserService getUserService() {
		return userService;
	}

	public void setUserService(final UserService userService) {
		this.userService = userService;
	}
	
	public EventService getEventService() {
		return eventService;
	}

	public void setEventService(final EventService eventService) {
		this.eventService = eventService;
	}
	
	public GPConsignmentTrackingService getGpConsignmentTrackingService() {
		return gpConsignmentTrackingService;
	}

	public void setGpConsignmentTrackingService(GPConsignmentTrackingService gpConsignmentTrackingService) {
		this.gpConsignmentTrackingService = gpConsignmentTrackingService;
	}
	
	public long setShippingNotificationUID() {
		Random rand = new Random();
		int inc = rand.nextInt(1000000000);
		final long id = Long.parseLong(String.valueOf(System.currentTimeMillis()).substring(1, 10).concat(String.valueOf(inc)));
		return id;
	}
	@Override
	public boolean isAbortable() {
		return true;
	} 

}
