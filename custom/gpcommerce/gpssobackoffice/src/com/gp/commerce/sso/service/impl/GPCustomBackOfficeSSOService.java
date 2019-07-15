/**
 *
 */
package com.gp.commerce.sso.service.impl;

import de.hybris.platform.core.model.user.UserModel;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.gp.commerce.sso.DefaultGPBackOfficeSSOService;


/**
 * @author rbadisa
 *
 */
public class GPCustomBackOfficeSSOService extends DefaultGPBackOfficeSSOService
{

	private static final Logger LOG = Logger.getLogger(GPCustomBackOfficeSSOService.class);

	@Override
	public UserModel getOrCreateSSOUser(final String id, final String name, final Collection<String> roles)
	{
		LOG.info("In getOrCreateSSOUser ----------------" + id + " Name" + name);
		if (CollectionUtils.isEmpty(roles))
		{
			roles.add("backofficeuser");
		}
		return super.getOrCreateSSOUser(id.toLowerCase(), name, roles);
	}
}
