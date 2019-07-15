/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/
package com.gp.commerce.spring.security;

import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.spring.security.CoreUserDetails;
import de.hybris.platform.spring.security.CoreUserDetailsService;

import java.util.HashMap;
import java.util.Map;


/**
 * The Class GPUserDetailsService.
 *
 * @author vdannina
 */
public class GPUserDetailsService extends CoreUserDetailsService {
    
	private static final String QUERY = "SELECT {failedLoginAttempts} from {Customer} where {uid} = ?uid";
	private static final String UID = "uid";

	/**
	 * This method is used to load user by username
	 * @param username
	 * 			the username
	 * @return user details
	 */
    public GPUserDetails loadUserByUsername(String username){
        CoreUserDetails coreUserDetails = super.loadUserByUsername(username);
        return new GPUserDetails(coreUserDetails,fetchCustomerFailedLogins(username));
    }

    /**
     * This method is used to fetch the failedLoginCount which is persisted.
     *
     * @param uid the uid
     * @return the integer
     */
    protected Integer fetchCustomerFailedLogins(String uid){
        FlexibleSearch flexibleSearch = Registry.getCurrentTenant().getJaloConnection().getFlexibleSearch();
        Map<String, Object> params = new HashMap();
        params.put(UID, uid);
        SearchResult<Integer> searchResult = flexibleSearch.search(QUERY, params, Integer.class);
        if(searchResult.getCount()==0){
        	return new Integer("0");
        }
        return searchResult.getResult().get(0);
    }
}
