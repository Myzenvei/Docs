/* 
 *  Copyright (c) 2018 Georgia-Pacific.  All rights reserved.
 *  This software is the confidential and proprietary information of Georgia-Pacific.
 */
package com.gp.commerce.core.services.event;

import java.util.List;

import com.gp.commerce.core.model.WishlistProcessModel;

import de.hybris.platform.acceleratorservices.model.email.EmailAttachmentModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.store.BaseStoreModel;

public class ShareWishlistEvent extends AbstractEvent
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String toEmail;
	private WishlistProcessModel process;
	private BaseSiteModel site;
	private BaseStoreModel baseStore;
	private CustomerModel customer;
	private String senderEmail;
	private String senderName;
	private String subject;
	private String senderMessage;
	private Boolean attachPDF;
	private Boolean soldTo;
	private Boolean addLink;
	private List<EmailAttachmentModel> emailAttachmentList;
	
	public BaseStoreModel getBaseStore() {
		return baseStore;
	}

	public void setBaseStore(BaseStoreModel baseStore) {
		this.baseStore = baseStore;
	}

	public CustomerModel getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerModel customer) {
		this.customer = customer;
	}

	public LanguageModel getLanguage() {
		return language;
	}

	public void setLanguage(LanguageModel language) {
		this.language = language;
	}

	public CurrencyModel getCurrency() {
		return currency;
	}

	public void setCurrency(CurrencyModel currency) {
		this.currency = currency;
	}
	private LanguageModel language;
	private CurrencyModel currency;

	public WishlistProcessModel getProcess() {
		return process;
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public BaseSiteModel getSite() {
		return site;
	}
	
	/**
	 * @param site
	 *           the baseSite to set
	 */
	public void setSite(final BaseSiteModel site)
	{
		this.site = site;
	}
	/**
	 * Default constructor
	 * @param process 
	 * 
	 */
	public ShareWishlistEvent(WishlistProcessModel process)
	{
		super(process);
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

	public Boolean getSoldTo() {
		return soldTo;
	}

	public void setSoldTo(Boolean soldTo) {
		this.soldTo = soldTo;
	}

	public List<EmailAttachmentModel> getEmailAttachmentList() {
		return emailAttachmentList;
	}

	public void setEmailAttachmentList(List<EmailAttachmentModel> emailAttachmentList) {
		this.emailAttachmentList = emailAttachmentList;
	}

	public Boolean getAddLink() {
		return addLink;
	}

	public void setAddLink(Boolean addLink) {
		this.addLink = addLink;
	}

}