/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.workflow.job;

import com.gp.commerce.core.catalog.GPVariantUtils;
import com.gp.commerce.core.model.GPCommerceProductModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.stream.Collectors;

public class CreateBaseAndVariantProductsJob implements AutomatedWorkflowTemplateJob {

    private static final Logger LOGGER = Logger.getLogger(CreateBaseAndVariantProductsJob.class);
    private GPVariantUtils gpVariantUtils;
    private ModelService modelService;

    @Override
    public WorkflowDecisionModel perform(WorkflowActionModel action) {
        final List<GPCommerceProductModel> gpCommerceProducts = getAttachedProducts(action);

        if(CollectionUtils.isEmpty(gpCommerceProducts)) {
            LOGGER.warn("No GPCommerceProduct type found in workflow");
            return null;
        }

        LOGGER.info("Attempting to create base and variant products for items in workflow");

        List<ProductModel> attachments = convertProducts(gpCommerceProducts);
        setAttachedProducts((List<ItemModel>)(List<?>)attachments,action);

        return action.getDecisions().iterator().next();

    }

    private  List<ProductModel> convertProducts(List<GPCommerceProductModel> gpCommerceProducts) {
        //iterate through each item
        //validate that the product needs conversion: check if it has variant information
        //do conversion

        //This is only a shell
        return  gpVariantUtils.createGPVariants(gpCommerceProducts);
    }

    private List<GPCommerceProductModel> getAttachedProducts(WorkflowActionModel action) {
        final List<ItemModel> attachments = action.getAttachmentItems();
        if (attachments != null)
        {
            return attachments.stream().filter(item -> item instanceof GPCommerceProductModel).map(item -> (GPCommerceProductModel) item)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void setAttachedProducts(List<ItemModel> attachedProducts, WorkflowActionModel action) {
        if(action != null){

            WorkflowModel workflowModel = action.getWorkflow();
            List<WorkflowItemAttachmentModel> workflowItemAttachments = attachedProducts.stream()
                    .map(prod -> {
                        WorkflowItemAttachmentModel att = (WorkflowItemAttachmentModel)this.modelService.create(WorkflowItemAttachmentModel.class);
                        att.setCode("toCheck");
                        att.setItem(prod);
                        att.setWorkflow(workflowModel);
                        return att;
                    }).collect(Collectors.toList());

            LOGGER.info("setting new attachments to " + workflowItemAttachments);
           action.setAttachments(workflowItemAttachments);

        }
    }

    public GPVariantUtils getGpVariantUtils() {
        return gpVariantUtils;
    }

    public void setGpVariantUtils(GPVariantUtils gpVariantUtils) {
        this.gpVariantUtils = gpVariantUtils;
    }

    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
}
