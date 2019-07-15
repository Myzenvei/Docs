/*
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.workflow.job;

import com.gp.commerce.core.catalog.GPKitVariantUtils;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import org.apache.commons.collections4.CollectionUtils;
import com.gp.commerce.core.model.GPProductKitModel;

import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import java.util.stream.Collectors;

public class CreateKitBaseAndVariantProductsJob implements AutomatedWorkflowTemplateJob {

    private static final Logger LOGGER = Logger.getLogger(CreateKitBaseAndVariantProductsJob.class);
    private GPKitVariantUtils gpKitVariantUtils;
    public GPKitVariantUtils getGpKitVariantUtils() {
		return gpKitVariantUtils;
	}

	public void setGpKitVariantUtils(GPKitVariantUtils gpKitVariantUtils) {
		this.gpKitVariantUtils = gpKitVariantUtils;
	}

	private ModelService modelService;

    @Override
    public WorkflowDecisionModel perform(WorkflowActionModel action) {
        final List<GPProductKitModel> gpKitProducts = getAttachedProducts(action);

        if(CollectionUtils.isEmpty(gpKitProducts)) {
            LOGGER.warn("No GPProductKitModel type found in workflow");
            return null;
        }

        LOGGER.info("Attempting to create base and variant products for items in workflow");

        List<ProductModel> attachments = convertProducts(gpKitProducts);
        setAttachedProducts((List<ItemModel>)(List<?>)attachments,action);

        return action.getDecisions().iterator().next();

    }

    /**
     * @param gpKitProducts
     * @return Create Kit Variants 
     */
    private  List<ProductModel> convertProducts(List<GPProductKitModel> gpKitProducts) {
        //iterate through each item
        //validate that the product needs conversion: check if it has variant information
        //do conversion

        //This is only a shell
        return  gpKitVariantUtils.createGPVariants(gpKitProducts);
    }

    /**
     * @param action
     * @return Return Kit Products
     */
    private List<GPProductKitModel> getAttachedProducts(WorkflowActionModel action) {
        final List<ItemModel> attachments = action.getAttachmentItems();
        if (attachments != null)
        {
            return attachments.stream().filter(item -> item instanceof GPProductKitModel).map(item -> (GPProductKitModel) item)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    /**
     * @param attachedProducts
     * @param Set attachments to the action
     */
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


    public ModelService getModelService() {
        return modelService;
    }

    public void setModelService(ModelService modelService) {
        this.modelService = modelService;
    }
}
