package com.bazaarvoice.hybris.controllers.pages;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.acceleratorstorefrontcommons.controllers.util.GlobalMessages;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import de.hybris.platform.yacceleratorstorefront.controllers.ControllerConstants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = BVSubmissionContainerController.BV_SUBMISSIONCONTAINER_URL)
public class BVSubmissionContainerController extends AbstractPageController {

    public static final String SUBMISSION_CONTAINER_UID = "submissionContainer";
    public static final String BV_SUBMISSIONCONTAINER_URL = "/bv/submissioncontainer";

    @RequestMapping(method = RequestMethod.GET)
    public String submissionContainer(final Model model, HttpServletRequest request, HttpServletResponse response) throws CMSItemNotFoundException {
        storeCmsPageInModel(model, getContentPageForLabelOrId(SUBMISSION_CONTAINER_UID));
        String storefrontContextRoot = getSiteConfigService().getProperty("storefrontContextRoot");
        if (storefrontContextRoot == null || (!request.getRequestURL().toString().contains(storefrontContextRoot))){
            storefrontContextRoot = "";
        }
        String containerRelativeUrl = storefrontContextRoot + BV_SUBMISSIONCONTAINER_URL;
        if (!request.getRequestURL().toString().contains(containerRelativeUrl)){
            GlobalMessages.addErrorMessage(model, "system.error.page.not.found");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return ControllerConstants.Views.Pages.Error.ErrorNotFoundPage;
        }
        model.addAttribute("containerRelativeUrl", containerRelativeUrl);
        return getViewForPage(model);
    }
}