/**
 *
 */
package com.gpintegration.core.cronjob;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gpintegration.constants.GpintegrationConstants;
import com.gpintegration.service.impl.GPDefaultCommerceSCPIService;
import com.gpintegration.user.impl.GPAddCustomerToSfdcResponse;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.servicelayer.user.UserService;

/**
 * @author vdannina
 *
 */
public class GPCustomerDataSyncCronJob extends AbstractJobPerformable<CronJobModel> {
	private static final String MAX_RETRY_COUNT = "maxCount";
	private static final Logger LOG = Logger.getLogger(GPCustomerDataSyncCronJob.class);
	private TimeService timeService;
	private UserService userService;
	@Resource
	private ConfigurationService configurationService;
	@Resource
	private GPDefaultCommerceSCPIService gpCommerceSCPIService;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de.
	 * hybris.platform.cronjob.model. CronJobModel )
	 */
	@Override
	public PerformResult perform(final CronJobModel arg0) {
		LOG.info("Data Synch CronJob started sucessfully");
		
		if (configurationService.getConfiguration().getBoolean(GpintegrationConstants.SCPI_FEATURE)) {

			try {
				for (String uid : fetchCustomerFailedToAddInSfdc()) {
					if (clearAbortRequestedIfNeeded(arg0))
					{
						LOG.debug("The job is aborted.");
						return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
					}
					CustomerModel searchModel = new CustomerModel();
					searchModel.setUid(uid);
					CustomerModel customerModel = flexibleSearchService.getModelByExample(searchModel);
					if(gpCommerceSCPIService.isDataReplicationNeeded(customerModel)) {
						GPAddCustomerToSfdcResponse gpAddCustomerToSfdcResponse;
						String b2bUnit = configurationService.getConfiguration().getString(GpintegrationConstants.B2B_UNIT);
						if ((uid.split(GpintegrationConstants.SITE_DELIMITER)[1]).equalsIgnoreCase(b2bUnit)) {
							gpAddCustomerToSfdcResponse = gpCommerceSCPIService
									.addCustomerToSfdc((B2BCustomerModel) customerModel);
						} else {
							gpAddCustomerToSfdcResponse = gpCommerceSCPIService.addCustomerToSfdc(customerModel);
						}
						setFeildsByResponse(customerModel, gpAddCustomerToSfdcResponse);
					}else {
						LOG.error("SFDC Contact replication failed validation: Invalid customer id" +customerModel.getCustomerID() + "or duplicate record  " + customerModel.getAddToSfdcErrorCode());
					}
				}
			} catch (Exception e) {
				LOG.error("Error in response from SCPI" + e.getMessage(),e);
			}
		} else {
			LOG.info("Add customer to SFDC is not enabled");
		}

		LOG.info("Data Synch CronJob finised sucessfully");
		return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
	}

	private void setFeildsByResponse(CustomerModel customerModel,
			GPAddCustomerToSfdcResponse gpAddCustomerToSfdcResponse) {
		Integer retryCount = customerModel.getAddToSfdcRetryCount() != null? customerModel.getAddToSfdcRetryCount() : 0;
		if (null == gpAddCustomerToSfdcResponse) {
			LOG.error("Error sending customer data to SCPI");
			customerModel.setAddToSfdcStatus(false);
			retryCount++;
			// 502 is if service is not available
			customerModel.setAddToSfdcErrorCode(GpintegrationConstants.SERVICE_UNAVAILABLE_ERROR_CODE);
		} else {
			if (!gpAddCustomerToSfdcResponse.getContactResponse().isStatus()) {
				LOG.info("Error Code received from SCPI:"
						+ gpAddCustomerToSfdcResponse.getContactResponse().getErrorCode());
				LOG.info("Error Msg received from SCPI:"
						+ gpAddCustomerToSfdcResponse.getContactResponse().getErrorMessage());
				customerModel.setAddToSfdcStatus(false);
				customerModel.setAddToSfdcErrorCode(
						gpAddCustomerToSfdcResponse.getContactResponse().getErrorCode());
				retryCount++;
			} else {
				LOG.info("Customer Data updated to SFDC sucessfully");
				customerModel.setAddToSfdcStatus(true);
				customerModel.setAddToSfdcContactId(
						gpAddCustomerToSfdcResponse.getContactResponse().getContactId());
			}
		}
		customerModel.setAddToSfdcRetryCount(retryCount);
		modelService.save(customerModel);
	}

	protected List<String> fetchCustomerFailedToAddInSfdc() {
		int noOfRecordsToBeFetched = Integer
				.parseInt(configurationService.getConfiguration().getString("gp.scpi.no.of.records"));
		FlexibleSearch flexibleSearch = Registry.getCurrentTenant().getJaloConnection().getFlexibleSearch();
		String query = "SELECT {uid} from {Customer} where {addtosfdcstatus} in (NULL,0) and {uid} like '%|%' and {addToSfdcRetryCount} < ?maxCount";
		Map<String, Object> params = new HashMap();
		params.put(MAX_RETRY_COUNT, configurationService.getConfiguration().getInteger("gp.scpi.max.retry.count", 5));
		SearchResult<String> searchResult = flexibleSearch.search(query, params, String.class);
		if (searchResult.getCount() <= noOfRecordsToBeFetched){
			return searchResult.getResult();
		}
		else{
			return searchResult.getResult().subList(0, noOfRecordsToBeFetched);
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
	
	@Override
	public boolean isAbortable() {
		return true;
	} 

}
