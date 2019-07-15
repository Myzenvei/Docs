package com.gp.commerce.facades.product;

import de.hybris.platform.core.model.product.ProductModel;

/**
 * Builder class to be used in Product test data preparation
 * @author megverma
 *
 */
public class ProductBuilder {

    ProductModel product = new ProductModel();
    
    public ProductBuilder withCode(String code) {
        this.product.setCode(code);
        return this;
    }

    public ProductModel build() {	
        return product;
    }
}