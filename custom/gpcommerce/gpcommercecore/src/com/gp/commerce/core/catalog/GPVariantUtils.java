/*
 * Copyright (c) 2019. Georgia-Pacific.  All rights reserved.
 * This software is the confidential and proprietary information of Georgia-Pacific.
 *
 *
 */

package com.gp.commerce.core.catalog;

import com.gp.commerce.core.model.GPCommerceProductModel;
import com.gp.commerce.core.model.GPGenericVariantProductModel;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.variants.model.*;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GPVariantUtils {

    private static final Logger LOGGER = Logger.getLogger(GPVariantUtils.class);

    public final static String BP_PREFIX = "BP-";
    public final static String ONLINE_CATALOG_VERSION = "Online";
    public final static String STAGE_CATALOG_VERSION = "Staged";

    private String gpDefaultVariantCategory;
    private String gpDefaultVariantValueCategory;
    private String gpDefaultVariantCatalogId;
    private String gpDefaultVariantProduct;

    private VariantCategoryModel defaultVariantCategory;
    private VariantValueCategoryModel defaultVariantValueCategory;
    private VariantTypeModel defaultVariantProductType;
    private CatalogVersionModel stageCatalogVersionModel;
    private CatalogVersionModel onlineCatalogVersionModel;


    private ModelService modelService;
    private CategoryService categoryService;
    private CatalogVersionService catalogVersionService;
    private VariantsService variantsService;
    private ProductService productService;


    public List<ProductModel> createGPVariants(List<GPCommerceProductModel> products) {

        LOGGER.info(("processing products " + products));
        boolean success = false;
        boolean addedVariants = false;
        List<ProductModel> productModelClones = new ArrayList<>();
        GPCommerceProductModel baseProduct = null;
        Transaction tx = Transaction.current();
        tx.begin();
        try {
            setup();
            for (ProductModel product : products) {
                if (baseProduct == null) {
                    //create a base product
                    baseProduct = setupBaseProduct((GPCommerceProductModel) product);
                    productModelClones.add(baseProduct);

                }
                if (!containsVariant(product, baseProduct)) {
                    GenericVariantProductModel genericVariantProductModel = convertToVariant(product, baseProduct);

                    if (genericVariantProductModel != null) {
                        productModelClones.add(genericVariantProductModel);

                        LOGGER.info(("adding new variant  " + genericVariantProductModel));
                        modelService.save(genericVariantProductModel);
                        addedVariants = true;
                    }
                }

                //if we added a variant(s), then need to clear the prices on the baseProd, they should  have been copied
                //over to the variant
                if (addedVariants) {
                    baseProduct.setEurope1Prices(new ArrayList<>());
                    modelService.save(baseProduct);
                }
            }
            success = true;

        } catch (Exception e) {
        	 LOGGER.error(e.getMessage(),e);
            success = false;
        } finally {
            if (success)
                tx.commit();
            else {
                LOGGER.info("rolling back transaction");
                tx.rollback();
            }
        }

        LOGGER.info("returning list of product clones " + productModelClones);
        return productModelClones;

    }

    protected void setup() {
        if (stageCatalogVersionModel == null) {
            this.stageCatalogVersionModel = catalogVersionService.getCatalogVersion(gpDefaultVariantCatalogId,
                    STAGE_CATALOG_VERSION);
        }
        if (onlineCatalogVersionModel == null) {
            this.onlineCatalogVersionModel = catalogVersionService.getCatalogVersion(gpDefaultVariantCatalogId,
                    ONLINE_CATALOG_VERSION);
        }

        if (defaultVariantCategory == null) {
            defaultVariantCategory = (VariantCategoryModel) categoryService.getCategoryForCode(stageCatalogVersionModel, gpDefaultVariantCategory);
        }

        if (defaultVariantValueCategory == null) {
            defaultVariantValueCategory = (VariantValueCategoryModel) categoryService.getCategoryForCode(stageCatalogVersionModel, gpDefaultVariantValueCategory);
        }
        if (defaultVariantProductType == null) {
            defaultVariantProductType = variantsService.getVariantTypeForCode(gpDefaultVariantProduct);
        }
    }



    protected GPCommerceProductModel setupBaseProduct(GPCommerceProductModel product) {

        GPCommerceProductModel baseProduct = product;
        String code = product.getCode();
        if (!code.startsWith(BP_PREFIX)) {

            baseProduct = modelService.clone(product);
            baseProduct.setCode(BP_PREFIX + code);

            List<CategoryModel> categoryModels = null;
            Collection<CategoryModel> categoryModelCollection = baseProduct.getSupercategories();
            if(categoryModelCollection != null){
                categoryModels = new ArrayList<>(categoryModelCollection);
            }else{
                categoryModels = new ArrayList<CategoryModel>();
            }
            categoryModels.add(defaultVariantCategory);
            baseProduct.setSupercategories(categoryModels);
            baseProduct.setApprovalStatus(ArticleApprovalStatus.CHECK);
            baseProduct.setVariantType(defaultVariantProductType);
            modelService.save(baseProduct);

        }
        return baseProduct;

    }

    protected GenericVariantProductModel convertToVariant(ProductModel product, ProductModel baseProduct) {

        LOGGER.info("converting product " + product.getCode() + " associated to baseProduct " + baseProduct.getCode());

        product.setEurope1Taxes(new ArrayList<>());
        GenericVariantProductModel genericVariantProductModel = modelService.clone(product,
                GPGenericVariantProductModel.class, null);

        //remove product stg/online
        removeGPCommerceProduct(product);

        String vcode = genericVariantProductModel.getCode();
        LOGGER.info("create variant for  " + vcode);
        if (vcode.startsWith(BP_PREFIX)) {
            genericVariantProductModel.setCode(vcode.replace(BP_PREFIX, ""));
        }

        //set the correct super cats
        List<CategoryModel> categoryModels = new ArrayList<>();
        categoryModels.add(defaultVariantValueCategory);
        genericVariantProductModel.setSupercategories(categoryModels);

        //remove tax rows
        genericVariantProductModel.setEurope1Taxes(new ArrayList<>());

        genericVariantProductModel.setApprovalStatus(ArticleApprovalStatus.CHECK);
        genericVariantProductModel.setBaseProduct(baseProduct);

        LOGGER.info("saving variant product " + genericVariantProductModel.getCode());
        modelService.save(genericVariantProductModel);

        return genericVariantProductModel;
    }

    private void removeGPCommerceProduct(ProductModel product) {

        LOGGER.info(("marking product  " + product +  "  for delete"));
        //get the  online version as well and  delete it
        ProductModel onlineProduct = productService.getProductForCode(onlineCatalogVersionModel, product.getCode());
        modelService.remove(product);
        modelService.remove(onlineProduct);

    }

    protected boolean containsVariant(ProductModel product, ProductModel baseProduct) {
        boolean hasVariant = false;

        String vcode =  product.getCode();

        //if this is the baseproduct, then remove the BP_PREFIX
        if (vcode.startsWith(BP_PREFIX)) {
            vcode = vcode.replaceAll(BP_PREFIX,"");
        }

        final String variantCode = vcode;

        Collection<VariantProductModel> variants = baseProduct.getVariants();
        if(variants != null) {
            hasVariant = variants.stream()
                    .anyMatch(pvars -> pvars.getCode().equalsIgnoreCase(variantCode));
            LOGGER.info("checking " + product.getCode() + " is a variant of " + baseProduct.getCode() + "=" + hasVariant);
        }
        return hasVariant;
    }
    
    public String getGpDefaultVariantCategory() {
        return gpDefaultVariantCategory;
    }

    public void setGpDefaultVariantCategory(String gpDefaultVariantCategory) {
        this.gpDefaultVariantCategory = gpDefaultVariantCategory;
    }

    public String getGpDefaultVariantValueCategory() {
        return gpDefaultVariantValueCategory;
    }

    public void setGpDefaultVariantValueCategory(String gpDefaultVariantValueCategory) {
        this.gpDefaultVariantValueCategory = gpDefaultVariantValueCategory;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }

    public String getGpDefaultVariantCatalogId() {
        return gpDefaultVariantCatalogId;
    }

    public void setGpDefaultVariantCatalogId(String gpDefaultVariantCatalogId) {
        this.gpDefaultVariantCatalogId = gpDefaultVariantCatalogId;
    }

    public VariantCategoryModel getDefaultVariantCategory() {
        return defaultVariantCategory;
    }

    public void setDefaultVariantCategory(VariantCategoryModel defaultVariantCategory) {
        this.defaultVariantCategory = defaultVariantCategory;
    }

    public VariantValueCategoryModel getDefaultVariantValueCategory() {
        return defaultVariantValueCategory;
    }

    public void setDefaultVariantValueCategory(VariantValueCategoryModel defaultVariantValueCategory) {
        this.defaultVariantValueCategory = defaultVariantValueCategory;
    }

    public CatalogVersionModel getStageCatalogVersionModel() {
        return stageCatalogVersionModel;
    }

    public void setStageCatalogVersionModel(CatalogVersionModel stageCatalogVersionModel) {
        this.stageCatalogVersionModel = stageCatalogVersionModel;
    }

    public CatalogVersionService getCatalogVersionService() {
        return catalogVersionService;
    }

    public void setCatalogVersionService(CatalogVersionService catalogVersionService) {
        this.catalogVersionService = catalogVersionService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public VariantTypeModel getDefaultVariantProductType() {
        return defaultVariantProductType;
    }

    public void setDefaultVariantProductType(VariantTypeModel defaultVariantProductType) {
        this.defaultVariantProductType = defaultVariantProductType;
    }

    public String getGpDefaultVariantProduct() {
        return gpDefaultVariantProduct;
    }

    public void setGpDefaultVariantProduct(String gpDefaultVariantProduct) {
        this.gpDefaultVariantProduct = gpDefaultVariantProduct;
    }

    public VariantsService getVariantsService() {
        return variantsService;
    }

    public void setVariantsService(VariantsService variantsService) {
        this.variantsService = variantsService;
    }

    public ProductService getProductService() {
        return productService;
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
