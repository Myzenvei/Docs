package de.hybris.platform.multicountry.productcockpit.listview;

import static de.hybris.platform.cockpit.services.sync.SynchronizationService.SYNCHRONIZATION_OK;

import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent.ChangeType;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.multicountry.model.productavailabilitygroup.ProductAvailabilityAssignmentModel;
import de.hybris.platform.productcockpit.components.listview.impl.ProductSynchronizationStatus;

import java.util.Collections;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Image;


public class ProductAvailabilityAssignmentStatus extends ProductSynchronizationStatus
{

	@Override
	protected String getTypeRestriction()
	{
		return ProductAvailabilityAssignmentModel._TYPECODE;
	}


	@Override
	public EventListener getEventListener(final Context context)
	{

		return new EventListener()
		{
			private final EventListener listener = new EventListener()
			{
				@Override
				public void onEvent(final Event event)
				{
					try
					{
						if (getTypeService().getBaseType(ProductAvailabilityAssignmentModel._TYPECODE).isAssignableFrom(
								context.getItem().getType()))
						{

							final List<SyncItemJobModel>[] matrixRules = getSyncJobs(context);
							getSynchronizationService().performSynchronization(Collections.singletonList(context.getItem()), null,
									matrixRules[0].get(0).getTargetVersion(), null);

							UISessionUtils.getCurrentSession().sendGlobalEvent(
									new ItemChangedEvent(this, context.getItem(), Collections.emptyList(), ChangeType.CHANGED));

						}
					}
					finally
					{
						Clients.showBusy(null, false);
					}
				}
			};

			@Override
			public void onEvent(final Event event)
			{

				final Object status = context.getMap().get(CURRENT_STATUS_KEY);
				if (isSyncStatusOk(status))
				{
					return; //product already synched, nothing to do.
				}

				final Component target = event.getTarget();
				target.removeEventListener("onSync", this.listener);
				target.addEventListener("onSync", this.listener);

				if (target instanceof Image)
				{
					((Image) target).setSrc(getSyncBusyImg());
				}
				else
				{
					Clients.showBusy(Labels.getLabel("busy.sync"), true);
				}
				Events.echoEvent("onSync", event.getTarget(), null);
			}

			protected boolean isSyncStatusOk(final Object status)
			{
				return (status instanceof Integer && ((Integer) status) == SYNCHRONIZATION_OK);
			}
		};
	}
}
