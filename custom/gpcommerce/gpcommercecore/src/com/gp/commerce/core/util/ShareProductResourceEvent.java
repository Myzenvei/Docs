/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.util;

import java.util.List;

import de.hybris.platform.servicelayer.event.events.AbstractEvent;

/**
 * Share Product Event POJO
 *
 */
public class ShareProductResourceEvent extends AbstractEvent{

	private List<String> recipientEmails;
	private String senderEmail;
	private String senderName;
	private String senderMessage;
	private String resourceTitle;
	private String resourceDescription;
	private String imgurl;
	private String resourcePageUrl;
	private String embeddedLink;
	private Boolean checkBoxSelected;
	
	public List<String> getRecipientEmails() {
		return recipientEmails;
	}
	public void setRecipientEmails(List<String> recipientEmails) {
		this.recipientEmails = recipientEmails;
	}
	public String getSenderEmail() {
		return senderEmail;
	}
	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderMessage() {
		return senderMessage;
	}
	public void setSenderMessage(String senderMessage) {
		this.senderMessage = senderMessage;
	}
	public String getResourceTitle() {
		return resourceTitle;
	}
	public void setResourceTitle(String resourceTitle) {
		this.resourceTitle = resourceTitle;
	}
	public String getResourceDescription() {
		return resourceDescription;
	}
	public void setResourceDescription(String resourceDescription) {
		this.resourceDescription = resourceDescription;
	}
	public String getImgurl() {
		return imgurl;
	}
	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}
	public String getResourcePageUrl() {
		return resourcePageUrl;
	}
	public void setResourcePageUrl(String resourcePageUrl) {
		this.resourcePageUrl = resourcePageUrl;
	}
	public String getEmbeddedLink() {
		return embeddedLink;
	}
	public void setEmbeddedLink(String embeddedLink) {
		this.embeddedLink = embeddedLink;
	}
	public Boolean getCheckBoxSelected() {
		return checkBoxSelected;
	}
	public void setCheckBoxSelected(Boolean checkBoxSelected) {
		this.checkBoxSelected = checkBoxSelected;
	}
	
}
