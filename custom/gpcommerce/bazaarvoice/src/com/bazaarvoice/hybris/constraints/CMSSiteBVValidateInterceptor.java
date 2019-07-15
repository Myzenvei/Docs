package com.bazaarvoice.hybris.constraints;

import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by artlaber on 3/17/14.
 */
public class CMSSiteBVValidateInterceptor implements ValidateInterceptor {

	private static final Logger LOG = Logger.getLogger(CMSSiteBVValidateInterceptor.class);

	private I18NService i18NService;

	@Autowired(required = true)
	public void setI18NService(I18NService i18NService){
		this.i18NService = i18NService;
	}

	@Override
	public void onValidate(Object o, InterceptorContext interceptorContext) throws InterceptorException {
		if (!checkLocalizedValues((CMSSiteModel) o))
			throw new InterceptorException("Bazaarvoice locales are invalid, ensure that the format matches requirements (language_COUNTRY), for example: en_US, de_DE etc...");
	}

	private boolean checkLocalizedValues(CMSSiteModel cmsSiteModel) {
		final Set<Locale> locs = i18NService.getSupportedLocales();

		for (final Locale locale : locs)
		{
			final String localeValue = cmsSiteModel.getBvLocale(locale);
			if (localeValue != null && !localeValue.isEmpty())
			{
				if (!localeValue.matches("[a-z]{2}_[A-Z]{2}"))
					return false;
			}
		}
		return true;
	}

}
