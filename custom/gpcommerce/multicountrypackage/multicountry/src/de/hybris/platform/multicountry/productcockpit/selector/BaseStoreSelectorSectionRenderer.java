/**
 *
 */
package de.hybris.platform.multicountry.productcockpit.selector;

import de.hybris.platform.cockpit.components.navigationarea.renderer.DefaultSectionSelectorSectionRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;


/**
 * @author brendan
 *
 */
public class BaseStoreSelectorSectionRenderer extends DefaultSectionSelectorSectionRenderer
{
	private Section _section;

	@Override
	public void render(final SectionPanel panel, final Component parent, final Component captionComponent, final Section section)
	{
		super.render(panel, parent, captionComponent, section);
		this._section = section;
		final Div mainDiv = (Div) parent.getChildren().get(0);

		if (mainDiv.getChildren().get(0) instanceof Listbox)
		{
			final Listbox listBox = (Listbox) mainDiv.getChildren().get(0);
			final Menupopup ctxMenu = createContextMenu();
			//final Menuitem menuItem = new Menuitem("test");
			//menuItem.setParent(ctxMenu);
			listBox.setContext(ctxMenu);
			parent.appendChild(ctxMenu);
		}
		if (mainDiv.getChildren().get(0) instanceof org.zkoss.zul.Label)
		{
			final org.zkoss.zul.Label label = (org.zkoss.zul.Label) mainDiv.getChildren().get(0);
			final Menupopup ctxMenu = createContextMenu();
			//final Menuitem menuItem = new Menuitem("test");
			//menuItem.setParent(ctxMenu);
			label.setContext(ctxMenu);
			parent.appendChild(ctxMenu);
		}
	}

	protected Menupopup createContextMenu()
	{

		final Menupopup ret = new Menupopup();


		final Menuitem menuItem = new Menuitem("deselect all");
		menuItem.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(final Event event) throws Exception //NOPMD: ZK Specific
			{
				final BaseStoreSectionSelectionSection baseStoreSection = (BaseStoreSectionSelectionSection) _section;
				baseStoreSection.setSelectedBaseStore(null);
				baseStoreSection.refreshView();
			}
		});
		menuItem.setParent(ret);


		return ret;


	}
}
