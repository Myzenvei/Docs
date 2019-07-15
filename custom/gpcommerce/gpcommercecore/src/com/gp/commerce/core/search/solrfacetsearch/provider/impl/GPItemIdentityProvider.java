/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.CatalogTypeService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.provider.IdentityProvider;

import java.util.Iterator;
import java.util.Set;

public class GPItemIdentityProvider implements IdentityProvider<ItemModel> {
	private static final long serialVersionUID = 1L;
	private CatalogTypeService catalogTypeService;
	private ModelService modelService;

	public String getIdentifier(final IndexConfig indexConfig, final ItemModel item) {
		String identifier;
		if (item instanceof PriceRowModel) {
			final ProductModel product = ((PriceRowModel) item).getProduct();
			final CatalogVersionModel catalogVersion = product.getCatalogVersion();
			final String code = product.getCode();
			identifier = catalogVersion.getCatalog().getId() + "/" + catalogVersion.getVersion() + "/" + code + "/" + item.getPk();
		} else if (this.catalogTypeService.isCatalogVersionAwareModel(item)) {
			identifier = this.prepareCatalogAwareItemIdentifier(item);
		} else {
			identifier = item.getPk().getLongValueAsString();
		}

		return identifier;
	}

	protected String prepareCatalogAwareItemIdentifier(final ItemModel item) {
		final Set<String> catalogVersionUniqueKeyAttributes = this.catalogTypeService
				.getCatalogVersionUniqueKeyAttribute(item.getItemtype());
		final String catalogVersionContainerAttribute = this.catalogTypeService
				.getCatalogVersionContainerAttribute(item.getItemtype());
		final CatalogVersionModel catalogVersion = (CatalogVersionModel) this.modelService.getAttributeValue(item,
				catalogVersionContainerAttribute);
		final Iterator<String> catalogVersionUniqueKeyIterator = catalogVersionUniqueKeyAttributes.iterator();
		final StringBuilder itemKey = new StringBuilder("");

		while (catalogVersionUniqueKeyIterator.hasNext()) {
			final String codePart = catalogVersionUniqueKeyIterator.next();
			itemKey.append("/").append(this.modelService.getAttributeValue(item, codePart).toString());
		}

		return catalogVersion.getCatalog().getId() + "/" + catalogVersion.getVersion() + itemKey.toString();
	}

	public CatalogTypeService getCatalogTypeService() {
		return this.catalogTypeService;
	}

	public void setCatalogTypeService(final CatalogTypeService catalogTypeService) {
		this.catalogTypeService = catalogTypeService;
	}

	public ModelService getModelService() {
		return this.modelService;
	}

	public void setModelService(final ModelService modelService) {
		this.modelService = modelService;
	}
}
