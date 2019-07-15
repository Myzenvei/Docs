/* 
*  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
*  This software is the confidential and proprietary information of Georgia-Pacific.
*/

package com.gp.commerce.facades.populators;

import de.hybris.platform.converters.Populator;

import com.gp.commerce.core.model.FAQModel;
import com.gp.commerce.gpcommerceaddon.facades.GPFAQData;


/**
 * Populator to populate FAQ Data with question and answers
 */
public class GPFAQPopulator implements Populator<FAQModel, GPFAQData>
{

	@Override
	public void populate(final FAQModel source, final GPFAQData target)
	{
		target.setAnswer(source.getAnswer());
		target.setQuestion(source.getQuestion());
	}
}
