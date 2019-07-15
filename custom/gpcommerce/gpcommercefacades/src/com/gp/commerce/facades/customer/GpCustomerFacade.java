/**
 *
 */
package com.gp.commerce.facades.customer;

import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.QuplesData;
import de.hybris.platform.commercefacades.user.data.RegisterData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.customer.TokenInvalidatedException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import org.springframework.web.multipart.MultipartFile;

import com.gp.commerce.core.exceptions.GPCommerceSaveDataException;
import com.gp.commerce.facades.data.PdfDownloadUserData;
import com.gp.commerce.facades.marketing.data.UpdatePreferenceData;



// TODO: Auto-generated Javadoc
/**
 * The Interface GpCustomerFacade.
 *
 * @author vdannina
 */
public interface GpCustomerFacade extends CustomerFacade
{
	
	/**
	 * Register a user with given parameters.
	 *
	 * @param registerData           the user data the user will be registered with
	 * @throws DuplicateUidException            if the login is not unique
	 * @throws UnknownIdentifierException            if the title code is invalid
	 * @throws IllegalArgumentException            if required data is missing
	 */
	void register(RegisterData registerData) throws DuplicateUidException, UnknownIdentifierException, IllegalArgumentException;

	/**
	 * Verify token validity.
	 *
	 * @param token           forgot password token
	 * @return status
	 * @throws TokenInvalidatedException            check for invalid token
	 * @throws IllegalArgumentException            if required data is missing
	 */
	Boolean verfiyTokenValidity(String token) throws TokenInvalidatedException, IllegalArgumentException;

	/**
	 * This method gets user id.
	 *
	 * @param token the token
	 * @return user id
	 */
	String getUserId(final String token);

	/**
	 * This method returns the tax exemption status of the customer.
	 *
	 * @return String
	 */
	String getCustomerTaxExemptionStatus();

	/**
	 * This method uploads tax exemption documents submitted by the customer.
	 *
	 * @param taxExemptionRequestDocumentArray the tax exemption request document array
	 * @throws GPCommerceSaveDataException the GP commerce save data exception
	 */
	void submitTaxExemptionDocuments(MultipartFile[] taxExemptionRequestDocumentArray) throws GPCommerceSaveDataException;

	/**
	 * updates customer personal details with given parameters.
	 *
	 * @param customerData 			the user data the user was registered with
	 * @return customer data
	 * @throws DuplicateUidException 			if the login is not unique
	 */

	CustomerData updatePersonalDetails(CustomerData customerData) throws DuplicateUidException;

	/**
	 * disconnect user from social account.
	 *
	 * @param registerData 			the user data the user was registered with
	 * @throws DuplicateUidException 			if the login is not unique
	 * @throws IllegalArgumentException 			if required data is missing
	 */
	void disconnect(RegisterData registerData) throws DuplicateUidException,IllegalArgumentException;

	/**
	 * connect user to social account.
	 *
	 * @param registration 			the user registration data
	 * @throws DuplicateUidException 			if the login is not unique
	 */
	void connect(RegisterData registration) throws DuplicateUidException;

	/**
	 * Update Customer.
	 *
	 * @param customerData the customer data
	 * @param isEmailChanged the is email changed
	 * @return CustomerData
	 */
	CustomerData updateCustomer(CustomerData customerData, Boolean isEmailChanged);

	/**
	 * Get Koch Auth Token (idToken, accessToken, refreshToken).
	 *
	 * @param autorizationCode the autorization code
	 * @return RegisterData
	 */
	RegisterData getKochAuthToken(String autorizationCode);

	/**
	 * Creates update contact.
	 *
	 * @param userPreferences           The object holding the user preferences.
	 * @param create           true if contact has to be created, false if it has to be updated.
	 * @return status code of the webservice response.
	 */
	String createUpdateContact(UpdatePreferenceData userPreferences, boolean create);

	/**
	 * Create a contact in yMarketing and generate a Quples token using the same.
	 *
	 * @param userPreferences
	 *           Information provided by user.
	 * @return Quples Data containing the quples token.
	 */
	public QuplesData createQuplesToken(final UpdatePreferenceData userPreferences);
	
	/**
	 * Save User Details entered in pdf download form.
	 *
	 * @param pdfDownloadUserData the pdf download user data
	 * @return Quples Data containing the quples token.
	 */
	 
	void pdfDownloadUserDetails(PdfDownloadUserData pdfDownloadUserData);

	/**
	 * Gets the pdf download user details.
	 *
	 * @return the pdf download user details
	 */
	PdfDownloadUserData getPdfDownloadUserDetails();
	
	/**
	 * Create Guest User for anonymous checkout.
	 *
	 * @param email           Email Id provided by user provided by user.
	 * @param name the name
	 * @return status.
	 * @throws DuplicateUidException the duplicate uid exception
	 */
	void createGuestUserForAnonymousCheckout(String email, String name) throws DuplicateUidException;
	
	/**
	 * Register user.
	 *
	 * @param registration the registration
	 * @return the register data
	 * @throws DuplicateUidException the duplicate uid exception
	 * @throws UnknownIdentifierException the unknown identifier exception
	 * @throws IllegalArgumentException the illegal argument exception
	 */
	RegisterData registerUser(RegisterData registration) throws DuplicateUidException, UnknownIdentifierException, IllegalArgumentException;
}
