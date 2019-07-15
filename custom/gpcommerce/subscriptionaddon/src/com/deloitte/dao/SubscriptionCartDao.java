/**
 *
 */
package com.deloitte.dao;

import java.util.List;

import com.deloitte.model.SubscriptionCartModel;


/**
 * @author asomjal
 *
 */
public interface SubscriptionCartDao
{
	List<SubscriptionCartModel> getSubscriptionCartModels();

	List<SubscriptionCartModel> getSubcriptionCartsForUser(String userId);
}
