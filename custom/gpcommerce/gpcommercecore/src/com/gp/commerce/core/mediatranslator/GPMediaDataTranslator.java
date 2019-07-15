/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.mediatranslator;

import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.util.Config;


import org.apache.log4j.Logger;

public class GPMediaDataTranslator extends MediaDataTranslator {

	private static final Logger LOG = Logger
			.getLogger(GPMediaDataTranslator.class);
	@Override
	public void performImport(String cellValue, Item processedItem)
			throws ImpExException {
		String folderPath = Config.getString("media.impex.import.prefix","jar:com.gp.commerce.core.setup.CoreSystemSetup&/gpcommercecore/import/cockpits/cmscockpit/");
		String formattedPath = folderPath + cellValue;
		LOG.debug("formattedPath : " + formattedPath);
		super.performImport(formattedPath, processedItem);
        
	}
}
