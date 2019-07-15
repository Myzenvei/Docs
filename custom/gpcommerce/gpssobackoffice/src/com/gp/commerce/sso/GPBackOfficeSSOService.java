/**
 *
 */
package com.gp.commerce.sso;

import de.hybris.platform.core.model.user.UserModel;

import java.util.Collection;


/**
 * @author rbadisa
 *
 */
public interface GPBackOfficeSSOService
{

	UserModel getOrCreateSSOUser(String id, String name, Collection<String> roles);
}
