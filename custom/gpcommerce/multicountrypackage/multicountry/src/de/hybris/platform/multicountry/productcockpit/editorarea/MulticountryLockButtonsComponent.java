/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package de.hybris.platform.multicountry.productcockpit.editorarea;

import static de.hybris.platform.multicountry.constants.MulticountryConstants.ADMINGROUP_UID;
import static de.hybris.platform.multicountry.constants.MulticountryConstants.CONFIG_PCM_SINGLE_LOCK;

import de.hybris.platform.cockpit.components.editorarea.EditorAreaNewButtonComponent;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.EditorAreaListener;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Constants.USER;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;


/**
 * Adds the lock/unlock and unlock all buttons.
 */
public class MulticountryLockButtonsComponent extends EditorAreaNewButtonComponent
{
	private final boolean singleLockOnly = Boolean.parseBoolean(Config.getString(CONFIG_PCM_SINGLE_LOCK, "false"));

	/**
	 * Listens to the object change and update events, and (a) changes the label of the unlock/lock button and (b)
	 * hides/unhides the unblock all button.
	 *
	 */
	private class ButtonEditorAreaListener implements EditorAreaListener
	{
		private final Button lockButton;
		private final Button unlockAllButton;

		protected ButtonEditorAreaListener(final Button lockButton, final Button unlockAllButton)
		{
			this.lockButton = lockButton;
			this.unlockAllButton = unlockAllButton;
		}

		@Override
		public void currentObjectChanged(final TypedObject previous, final TypedObject current)
		{
			updateLockButton(current);
			updateUnlockAllButton(current);
		}

		@Override
		public void nextItemClicked(final TypedObject currentObj)
		{
			//no-op
		}

		@Override
		public void valuesStored(final ObjectValueContainer valueContainer)
		{
			//no-op
		}

		@Override
		public void valuesUpdated(final ObjectValueContainer valueContainer)
		{
			//no-op
		}

		@Override
		public void browseItemClicked(final TypedObject currentObj)
		{
			//no-op
		}

		@Override
		public void previousItemClicked(final TypedObject currentObj)
		{
			//no-op
		}

		@Override
		public void currentObjectUpdated()
		{
			final TypedObject currentObject = UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea()
					.getCurrentObject();
			updateLockButton(currentObject);
			updateUnlockAllButton(currentObject);
		}

		@Override
		public void valueChanged(final ObjectValueContainer valueContainer, final PropertyDescriptor propertyDescriptor)
		{
			//no-op
		}

		/**
		 * Updates the lock/unlock button, changing the label and tooltip depending if there's a lock done by the current
		 * user.
		 *
		 * @param currentObject
		 *           The object
		 */
		private void updateLockButton(final TypedObject currentObject)
		{
			if (lockButton == null)
			{
				//no button => nothing to do
				return;
			}

			//Look for current product & user
			final Object product = currentObject.getObject();
			final UserModel userModel = UISessionUtils.getCurrentSession().getUser();

			if (isProductModel(product) && isEmployee(userModel))
			{
				// Lock/unlock button should be shown => get the right label depending on the actual user locks.
				final ProductModel productModel = (ProductModel) product;
				final boolean lockedByUser = isLockedByUser(productModel, userModel);
				if (lockedByUser)
				{
					lockButton.setLabel(Labels.getLabel("multicountry.editorarea.button.unlock"));
					lockButton.setTooltiptext(Labels.getLabel("multicountry.editorarea.button.unlock.tooltip"));
				}
				else
				{
					lockButton.setLabel(Labels.getLabel("multicountry.editorarea.button.lock"));
					lockButton.setTooltiptext(Labels.getLabel("multicountry.editorarea.button.lock.tooltip"));
				}
				lockButton.setVisible(true);
				//allow a single lock or multiple locks?
				lockButton.setDisabled(isLockedByAnyUser(productModel) && !lockedByUser && singleLockOnly);
			}
			else
			{
				// This is not a product or user is not an employee => hide the lock button.
				lockButton.setVisible(false);
			}
		}

		/**
		 * Updates the unlock all button, changing the label and tooltip depending if there's at least one lock done by
		 * any user.
		 *
		 * @param currentObject
		 *           The unlock all button
		 */
		private void updateUnlockAllButton(final TypedObject currentObject)
		{
			if (unlockAllButton == null)
			{
				//no button => nothing to do
				return;
			}

			//Look for current product & user
			final Object product = currentObject.getObject();
			final UserModel userModel = UISessionUtils.getCurrentSession().getUser();

			if (isProductModel(product) && isEmployee(userModel))
			{
				// Lock-all button should only be visible when there's any lock over this product.
				unlockAllButton.setVisible(isLockedByAnyUser((ProductModel) product));
			}
			else
			{
				// This is not a product or user is not an employee => hide the lock button.
				unlockAllButton.setVisible(false);
			}
		}
	}

	public MulticountryLockButtonsComponent()
	{
		super();

		final UserModel userModel = UISessionUtils.getCurrentSession().getUser();
		if (!isEmployee(userModel))
		{
			//Not an employee, no extra buttons are shown.
			return;
		}

		//Create the lock button for all employees
		final Button lockButton = createLockButton();

		//Create the unlock-all button only for IT Admins.
		final Button unlockAllButton = isValidAdmin(userModel) ? createUnlockAllButton() : null;

		//Add an editor area listener for changing buttons depending on the product's locks.
		final UIEditorArea editorArea = UISessionUtils.getCurrentSession().getCurrentPerspective().getEditorArea();
		editorArea.addEditorAreaListener(new ButtonEditorAreaListener(lockButton, unlockAllButton));
	}

	/**
	 * Creates the lock button that add/removes a locks done by the user.
	 */
	private Button createLockButton()
	{
		//Create the button
		final Button lockButton = new Button(Labels.getLabel("multicountry.editorarea.button.lock"));
		lockButton.setTooltiptext(Labels.getLabel("multicountry.editorarea.button.lock.tooltip"));
		lockButton.setParent(getContainerDiv());
		lockButton.setSclass("btncreatenew");
		lockButton.setDisabled(false);
		lockButton.setStyle("position: relative; right: 5px; top: 0px");

		//Assign a listener to the click event
		lockButton.addEventListener(Events.ON_CLICK, event -> {
			//Get session, editorArea and current user.
				final UISession session = UISessionUtils.getCurrentSession();
				final UIEditorArea editorArea = session.getCurrentPerspective().getEditorArea();
				final UserModel userModel = session.getUser();

				//Get the current product and its locks.
				final ProductModel productModel = (ProductModel) editorArea.getCurrentObject().getObject();
				final Collection<EmployeeModel> lockedBy = new ArrayList<>(productModel.getLockedBy());

				//Check if the user is locking or unlocking the product
				if (isLockedByUser(productModel, userModel))
				{
					lockedBy.remove(userModel);
				}
				else
				{
					//No lock by this user => lock it
					lockedBy.add((EmployeeModel) userModel);
				}

				//Save the changes...
				productModel.setLockedBy(lockedBy);
				session.getModelService().save(productModel);

				//...and update the area
				editorArea.update();
			});

		return lockButton;
	}


	/**
	 * Creates the Unlock All button
	 */
	private Button createUnlockAllButton()
	{
		//Create the button
		final Button unlockAllButton = new Button(Labels.getLabel("multicountry.editorarea.button.unlockall"));
		unlockAllButton.setTooltiptext(Labels.getLabel("multicountry.editorarea.button.unlockall.tooltip"));
		unlockAllButton.setParent(getContainerDiv());
		unlockAllButton.setSclass("btncreatenew");
		unlockAllButton.setDisabled(false);
		unlockAllButton.setStyle("position: relative; right: 5px; top: 0px");

		//Assign a listener to the click event
		unlockAllButton.addEventListener(Events.ON_CLICK, ev -> {

			//Get session,editorArea and product.
				final UISession session = UISessionUtils.getCurrentSession();
				final UIEditorArea editorArea = session.getCurrentPerspective().getEditorArea();

				final ProductModel productModel = (ProductModel) editorArea.getCurrentObject().getObject();
				if (!isLockedByAnyUser(productModel))
				{
					//No locks? => nothing to do
				return;
			}

			//Remove all locks..
			productModel.setLockedBy(Collections.emptyList());
			session.getModelService().save(productModel);

			//...and update the area
			editorArea.update();
		});

		return unlockAllButton;
	}


	/**
	 * Checks if the given user belongs to the IT Admin Group, or is admin itself.
	 *
	 * @param userModel
	 *           The user
	 */
	private boolean isValidAdmin(final UserModel userModel)
	{
		//Is admin or user belongs to multicountryadmingroup
		return USER.ADMIN_EMPLOYEE.equals(userModel.getUid())
				|| userModel.getAllGroups().stream().anyMatch(ug -> ADMINGROUP_UID.equals(ug.getUid()));
	}

	private static boolean isProductModel(final Object product)
	{
		return product instanceof ProductModel;
	}

	private static boolean isEmployee(final UserModel user)
	{
		return user instanceof EmployeeModel;
	}

	private static boolean isLockedByAnyUser(final ProductModel productModel)
	{
		return !productModel.getLockedBy().isEmpty();
	}

	private static boolean isLockedByUser(final ProductModel productModel, final UserModel userModel)
	{
		return productModel.getLockedBy().contains(userModel);
	}
}
