package de.hybris.multicountry.backoffice.widgets.actions;

import static com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils.notifyUser;

import de.hybris.multicountry.backoffice.services.ProductLockService;
import de.hybris.platform.acceleratorservices.dataexport.googlelocal.model.Product;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.access.method.P;

import com.google.common.collect.Lists;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEventTypes;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationUtils;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.impl.DefaultWidgetModel;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.search.data.pageable.Pageable;


/**
 * @author cyrill.pedol@sap.com
 *
 */
public abstract class AbstractProductLockAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, List>
{

	private static final String PARENT_WIDGET_MODEL = "parentWidgetModel";
	private static final String PAGEABLE = "pageable";

	@Resource
	private ProductLockService productLockService;

	protected abstract Predicate<Object> getPerformCondition();

	protected abstract boolean performAction(final ProductModel product);

	@Override
	public boolean canPerform(final ActionContext<Object> ctx)
	{
		final List<Object> data = getData(ctx);
		return CollectionUtils.isNotEmpty(data) && data.stream().allMatch(getPerformCondition());
	}

	/**
	 * Locks or unlocks the object(s) in the context.
	 *
	 * @param context The context
	 * @return An action result with the list of objects from the context that were successfully locked.
	 */
	@Override
	public ActionResult<List> perform(final ActionContext<Object> context)
	{
		final List<Object> data = getData(context);
		if (CollectionUtils.isEmpty(data)) //error out immediately if the context is null or empty
		{
			return new ActionResult<>(ActionResult.ERROR);
		}

		final List<Object> updatedObjects = new ArrayList<>();
		final List<Object> failedObjects = new ArrayList<>();
		for (final Object object : data)
		{
			if (object instanceof ProductModel && performAction((ProductModel) object))
			{
				updatedObjects.add(object);
			}
			else
			{
				failedObjects.add(object);
			}
		}

		//notify all the succesfully updated objects, send output
		if (updatedObjects.size() > 0)
		{
			autoRefreshProductList(context);
			showSuccessNotification(context, updatedObjects);
		}

		//notify all the failed objects
		if (failedObjects.size() > 0)
		{
			showFailureNotification(context, failedObjects);
		}

		// return success if all objects were updated
		return new ActionResult<>(failedObjects.isEmpty() ? ActionResult.SUCCESS : ActionResult.ERROR);
	}

	private List<Object> getData(final ActionContext<Object> context)
	{
		if (context.getData() instanceof Collection)
		{
			@SuppressWarnings("unchecked")
			final Collection<Object> data = (Collection<Object>) context.getData();
			return data.stream().filter(Objects::nonNull).collect(Collectors.toList());
		}
		else
		{
			return context.getData() == null ? Collections.emptyList() : Lists.newArrayList(context.getData());
		}
	}

	public ProductLockService getProductLockService()
	{
		return productLockService;
	}

	@Override
	public boolean needsConfirmation(final ActionContext<Object> ctx)
	{
		return false;
	}

	protected String getNotificationSource(final ActionContext<Object> ctx)
	{
		return NotificationUtils.getWidgetNotificationSource(ctx);
	}

	protected void showSuccessNotification(final ActionContext<Object> ctx, final List<Object> lockedObjects)
	{
		final String notificationSource = getNotificationSource(ctx);
		notifyUser(notificationSource, NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE, NotificationEvent.Level.SUCCESS,
				lockedObjects);
	}

	protected void showFailureNotification(final ActionContext<Object> ctx, final List<Object> lockedObjects)
	{
		final String notificationSource = getNotificationSource(ctx);
		notifyUser(notificationSource, NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE, NotificationEvent.Level.FAILURE,
				lockedObjects);
	}

	private void autoRefreshProductList(final ActionContext<Object> ctx)
	{
		if (ctx.getData() instanceof ProductModel)
		{
			sendOutput("selectedItem", ctx.getData());
		}
		else
		{
			final Pageable pageable = getPageable(ctx);
			if (pageable != null)
			{
				pageable.refresh();
				sendOutput("outputRefreshContext", pageable);
			}
		}
	}

	private Pageable getPageable(final ActionContext<Object> ctx)
	{
		final Object widgetModel = ctx.getParameter(PARENT_WIDGET_MODEL);
		Pageable pageable = null;
		if (widgetModel instanceof DefaultWidgetModel)
		{
			final DefaultWidgetModel defaultWidgetModel = ((DefaultWidgetModel) ctx.getParameter(PARENT_WIDGET_MODEL));
			final Object page = defaultWidgetModel.get(PAGEABLE);
			pageable = page instanceof Pageable ? (Pageable) page : null;
		}

		return pageable;
	}
}
