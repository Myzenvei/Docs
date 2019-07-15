/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.forms;

import java.util.List;

import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;

/**
 * Share Product Form POJO
 */
public class ShareProductForm {

	private List<String> recipientEmails;
	private String senderEmail;
	private String senderName;
	private String subject;
	private String senderMessage;
	private Boolean attachPDF;
	private Boolean addLink;
	private List<ProductWsDTO> products;
	private String encodedString;
	
	
	
	
	public String getEncodedString() {
		return encodedString;
	}
	public void setEncodedString(String encodedString) {
		this.encodedString = encodedString;
	}
	public List<ProductWsDTO> getProducts() {
		return products;
	}
	public void setProducts(List<ProductWsDTO> products) {
		this.products = products;
	}
	public Boolean getAttachPDF() {
		return attachPDF;
	}
	public void setAttachPDF(Boolean attachPDF) {
		this.attachPDF = attachPDF;
	}
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
}
