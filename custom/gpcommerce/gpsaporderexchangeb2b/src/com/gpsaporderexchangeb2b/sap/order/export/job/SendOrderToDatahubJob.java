/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2018 SAP SE
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 */
package com.gpsaporderexchangeb2b.sap.order.export.job;

import de.hybris.platform.core.enums.ExportStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.sap.orderexchange.outbound.SendToDataHubHelper;
import de.hybris.platform.sap.orderexchange.outbound.SendToDataHubResult;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.gpsaporderexchangeb2b.model.SendOrderToDatahubCronjobModel;




/**
 * @author Rahul Verma
 *
 */
public class SendOrderToDatahubJob extends AbstractJobPerformable<SendOrderToDatahubCronjobModel>
{
	private static final Logger LOG = Logger.getLogger(SendOrderToDatahubJob.class);
	private SendToDataHubHelper<ConsignmentModel> sendOrderToDataHubHelper;

	private int maxTries;
	public static final String ORDER_DETAILS = "SELECT {o.pk} FROM {order as o} WHERE {o.code}=?orderNumber";
	public static final String ORDERS_NOTEXPORTED = "SELECT {o.pk} FROM {Order as o} WHERE {o.exportStatus}=?notExported AND {o.status}=?paymentAuthorized";
	private static final String DATAHUB_STATUS_URL = "gp.datahub.status.url";
	private static final String REQUEST_PROPERTY = "text/plain";



	/*
	 * (non-Javadoc)
	 *
	 * @see de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable#perform(de .hybris.platform.cronjob.model.
	 * CronJobModel )
	 */
	@Override
	public PerformResult perform(final SendOrderToDatahubCronjobModel cronjob)
	{
		
			LOG.debug("Starting SendOrderToDatahubCronJob .........");
			final List<OrderModel> orders = getNotExportedOrders();
			LOG.debug("order ........." + orders);
			if (CollectionUtils.isNotEmpty(orders))
			{
				LOG.debug("order size........." + orders.size());
				for (final OrderModel order : orders)
				{
					try
					{
						if (clearAbortRequestedIfNeeded(cronjob))
						{
							LOG.debug("The job is aborted.");
							return new PerformResult(CronJobResult.UNKNOWN, CronJobStatus.ABORTED);
						}
						if (null != order && isBufferPeriodOver(order, cronjob)
								&& order.getSite().getUid().equals(cronjob.getBaseSite().getUid()) && isDataHubAvailable())
						{
	
							sendSalesOrderToDatahub(order);
						}
					}
					catch (final IOException e)
					{
	
						LOG.error("Sending Order to datahub encountered error ****************** " + e);
					}
				}
			}
	
			return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
		
	}

	private void sendSalesOrderToDatahub(final OrderModel order)
	{

		for (final ConsignmentModel consignment : order.getConsignments())
		{

			if (null != consignment && (consignment.getExportStatus() == ExportStatus.NOTEXPORTED))
			{
				boolean success = false;
				int count = 0;
				while (!success && count++ < this.maxTries)
				{
					LOG.debug("Retry count........." + count);
					LOG.debug("Max retry count........." + this.maxTries);
					try
					{
						final SendToDataHubResult result = sendOrderToDataHubHelper.createAndSendRawItem(consignment);
						if (result.isSuccess())
						{
							success = true;
							setConsignmentstatus(consignment, ExportStatus.EXPORTED);
							LOG.debug("Consignment Successfully transferred to Datahub........." + consignment.getCode());
						}
						else
						{
							setConsignmentstatus(consignment, ExportStatus.NOTEXPORTED);
							LOG.debug("Consignment not Successfully transferred to Datahub........." + order.getCode());
						}
					}
					catch (final Exception e)
					{
						LOG.debug("Exception occurred while sending consignment, " + consignment.getCode() + " to datahub");
						LOG.error(e.getMessage(),e);
					}
				}
			}
		}
		setOrderStatus(order);

	}

	protected void setOrderStatus(final OrderModel order)
	{
		boolean isExported = true;
		for (final ConsignmentModel consignment : order.getConsignments())
		{
			if (consignment.getExportStatus() == ExportStatus.NOTEXPORTED)
			{
				isExported = false;
			}
		}
		if (!isExported)
		{
			order.setExportStatus(ExportStatus.NOTEXPORTED);
		}
		else
		{
			order.setExportStatus(ExportStatus.EXPORTED);
		}
		modelService.save(order);
	}

	protected void setConsignmentstatus(final ConsignmentModel consignment, final ExportStatus exportStatus)
	{
		consignment.setExportStatus(exportStatus);
		modelService.save(consignment);
	}

	public int getMaxTries()
	{
		return maxTries;
	}

	public void setMaxTries(final int maxTries)
	{
		this.maxTries = maxTries;
	}

	/**
	 * @return the sendOrderToDataHubHelper
	 */
	public SendToDataHubHelper<ConsignmentModel> getSendOrderToDataHubHelper()
	{
		return sendOrderToDataHubHelper;
	}

	/**
	 * @param sendOrderToDataHubHelper
	 *           the sendOrderToDataHubHelper to set
	 */
	public void setSendOrderToDataHubHelper(final SendToDataHubHelper<ConsignmentModel> sendOrderToDataHubHelper)
	{
		this.sendOrderToDataHubHelper = sendOrderToDataHubHelper;
	}


	// service
	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	@Override
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

	public List<OrderModel> getNotExportedOrders()
	{
		LOG.debug("Entering the method: getAllOrders");
		final FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(ORDERS_NOTEXPORTED);
		flexibleSearchQuery.addQueryParameter("notExported", ExportStatus.NOTEXPORTED);
		flexibleSearchQuery.addQueryParameter("paymentAuthorized", OrderStatus.PAYMENT_AUTHORIZED);
		final SearchResult<OrderModel> searchResult = getFlexibleSearchService().search(flexibleSearchQuery);
		if (searchResult.getCount() > 0)
		{
			return searchResult.getResult();
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug("No Orders found in NOT EXPORTED Status: Exiting the method: getOrderDetails");
			}
		}
		return Collections.emptyList();
	}

	private boolean isBufferPeriodOver(final OrderModel order, final SendOrderToDatahubCronjobModel cronjob)
	{
		final Date date = new Date();
		final long diff = (date.getTime() - order.getCreationtime().getTime()) / (60 * 60 * 1000);
		final Integer bufferTime = cronjob.getBaseSite().getRemorseWindow();
		if (null != bufferTime && (Long.valueOf(diff) >= Long.valueOf(bufferTime)))
		{
			return true;
		}

		return false;

	}

	private boolean isDataHubAvailable() throws IOException
	{
		final String pubURL = Config.getParameter(DATAHUB_STATUS_URL);
		final URL url1 = new URL(pubURL);
		final HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

		//Datahub Availability Check
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type", REQUEST_PROPERTY);
		final String userpass = "admin" + ":" + "nimda";
		final String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
		connection.setRequestProperty("Authorization", basicAuth);
		final int response = connection.getResponseCode();
		LOG.info("The response code is ************** " + response);
		final BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
		final String responseLines = br.readLine();
		LOG.info("The response content is *************** " + responseLines);
		connection.disconnect();
		return response == 200 ? true : false;

	}

	@Override
	public boolean isAbortable() {
		return true;
	} 




}
