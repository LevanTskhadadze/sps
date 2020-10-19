package com.azry.sps.console.client.tabs;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.SystemParameter.SystemParameterTab;
import com.azry.sps.console.client.tabs.servicegroup.ServiceGroupPage;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.servicegroup.ServiceGroupDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;

import java.util.List;

public class TabBuilder {

	private static ServiceGroupPage serviceGroupPage;

	public static HTML getServiceGroupMenuItem (final TabPanel centerPanel) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-tag'></i>";

		HTML menuItem = new HTML(img + Mes.get("serviceGroups"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {

				if (serviceGroupPage != null) {
					centerPanel.setActiveWidget(serviceGroupPage);
					return;
				}
				serviceGroupPage = new ServiceGroupPage();
				centerPanel.add(serviceGroupPage, Mes.get("serviceGroups"));

				TabItemConfig config = centerPanel.getConfig(serviceGroupPage);
				config.setIcon(FAIconsProvider.getIcons().tag());
				config.setClosable(true);

				centerPanel.update(serviceGroupPage, config);
				centerPanel.setActiveWidget(serviceGroupPage);
			}

		});
		return menuItem;
	}

}
