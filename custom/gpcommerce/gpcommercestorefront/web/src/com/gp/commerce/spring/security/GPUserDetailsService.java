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
import com.gp.commerce.spring.security.GPUserDetails;
import java.util.HashMap;
import java.util.Map;


/**
 * The Class GPUserDetailsService.
 *
 * @author vdannina
 */
public class GPUserDetailsService extends CoreUserDetailsService {
    /* (non-Javadoc)
     * @see de.hybris.platform.spring.security.CoreUserDetailsService#loadUserByUsername(java.lang.String)
     */
	/**
	 * This method loads user by username
	 * @param username
	 * 			the user name
	 * @return GPUserDetails object
	 */
    public GPUserDetails loadUserByUsername(String username){
        CoreUserDetails coreUserDetails = super.loadUserByUsername(username);
        return new GPUserDetails(coreUserDetails,fetchCustomerFailedLogins(username));
    }

    /**
     * This method is used to fetch the failedLoginCount which is persisted
     * @param uid
     * @return
     */
    protected Integer fetchCustomerFailedLogins(String uid){
        FlexibleSearch flexibleSearch = Registry.getCurrentTenant().getJaloConnection().getFlexibleSearch();
        String query = "SELECT {failedLoginAttempts} from {Customer} where {uid} = ?uid";
        Map<String, Object> params = new HashMap();
        params.put("uid", uid);
        SearchResult<Integer> searchResult = flexibleSearch.search(query, params, Integer.class);
        if(searchResult.getCount()==0){
        		return new Integer("0");
        }
        return searchResult.getResult().get(0);
    }
}
