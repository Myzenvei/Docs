/**
 *
 */
package com.deloitte.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;

import com.deloitte.dao.SubscriptionCartDao;
import com.deloitte.model.SubscriptionCartModel;


/**
 * @author asomjal
 *
 */
public class SubscriptionCartDaoImpl implements SubscriptionCartDao
{
	//private static final String SUBSCRIPTION_CART_QUERY = "select {pk} from {SubscriptionCart} where {nextActivationDate} = TIMESTAMP(?currentDate) AND {isActive} = ?isActive";

	private static final String SUBSCRIPTION_CART_QUERY = "select {pk} from {SubscriptionCart} where {isActive} = ?isActive";

	private static final String SUBSCRIPTION_CART_USER_QUERY = "select {pk} from {SubscriptionCart} where {isActive} = ?isActive AND {user} = ?user";

	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "userService")
	private UserService userService;

	@Override
	public List<SubscriptionCartModel> getSubscriptionCartModels()
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(SUBSCRIPTION_CART_QUERY);
		//query.addQueryParameter("currentDate", getCurrentDate());
		query.addQueryParameter("isActive", Boolean.TRUE);
		final SearchResult<SubscriptionCartModel> searchResult = flexibleSearchService.search(query);
		return searchResult.getResult();
	}

	@Override
	public List<SubscriptionCartModel> getSubcriptionCartsForUser(final String userId)
	{
		final FlexibleSearchQuery query = new FlexibleSearchQuery(SUBSCRIPTION_CART_USER_QUERY);
		query.addQueryParameter("isActive", Boolean.TRUE);
		if (StringUtils.isNotBlank(userId))
		{
			query.addQueryParameter("user", userService.getUserForUID(userId));
		}
		else
		{
			query.addQueryParameter("user", userService.getCurrentUser());
		}
		final SearchResult<SubscriptionCartModel> searchResult = flexibleSearchService.search(query);
		return searchResult.getResult();
	}

	private String getCurrentDate()
	{
		final Calendar c = new GregorianCalendar();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		final Date d1 = c.getTime();
		final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.m");
		return sf.format(d1);
	}

	private String getCurrentDateNew()
	{
		final LocalDate tomorrow = LocalDate.now().plusDays(0);
		final Date today = Date.from(tomorrow.atStartOfDay(ZoneId.systemDefault()).toInstant());
		final SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.m");
		return sf.format(today);
	}
}
