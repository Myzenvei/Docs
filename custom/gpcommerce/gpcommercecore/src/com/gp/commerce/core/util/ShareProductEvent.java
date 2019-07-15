/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.util;

import java.util.List;

import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;

/**
 * Share Product Event POJO
 *
 */
public class ShareProductEvent extends AbstractEvent{

	private List<String> recipientEmails;
	private String senderEmail;
	private String senderName;
	private String subject;
	private String senderMessage;
	private Boolean attachPDF;
	private Boolean soldTo;
	private List<EmailAttachmentModel> emailAttachmentList;
	
	public List<EmailAttachmentModel> getEmailAttachmentList() {
		return emailAttachmentList;
	}
	public void setEmailAttachmentList(List<EmailAttachmentModel> emailAttachmentList) {
		this.emailAttachmentList = emailAttachmentList;
	}
	private List<ProductModel> product;
	private Boolean addLink;
	
	public Boolean getAddLink() {
		return addLink;
	}
	public void setAddLink(Boolean addLink) {
		this.addLink = addLink;
	}
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
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSenderMessage() {
		return senderMessage;
	}
	public void setSenderMessage(String senderMessage) {
		this.senderMessage = senderMessage;
	}
	public Boolean getAttachPDF() {
		return attachPDF;
	}
	public void setAttachPDF(Boolean attachPDF) {
		this.attachPDF = attachPDF;
	}
	public List<ProductModel> getProduct() {
		return product;
	}
	public void setProduct(List<ProductModel> product) {
		this.product = product;
	}
	public Boolean getSoldTo() {
		return soldTo;
	}
	public void setSoldTo(Boolean soldTo) {
		this.soldTo = soldTo;
	}
	
	
	}
