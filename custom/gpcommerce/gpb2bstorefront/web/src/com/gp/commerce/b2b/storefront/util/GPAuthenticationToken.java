/*
 * [y] hybris Platform
 *
 * Copyright (c) 2017 SAP SE or an SAP affiliate company.  All  rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.gp.commerce.b2b.storefront.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public class GPAuthenticationToken  extends UsernamePasswordAuthenticationToken {

    public GPAuthenticationToken(final String principal,
                                              final Collection<? extends GrantedAuthority> authorities)
    {
        super(principal, "", authorities);
    }



}
