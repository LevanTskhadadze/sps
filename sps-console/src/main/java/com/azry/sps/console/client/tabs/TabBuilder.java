package com.azry.sps.console.client.tabs;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.SystemParameter.SystemParameterTab;
import com.azry.sps.console.client.tabs.servicegroup.ServiceGroupPage;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.servicegroup.ServiceGroupDTO;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;

import java.util.HashMap;
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
					if(serviceGroupPage.getTitle().equals("closed")) {
						serviceGroupPage = null;
					}
					else {

						centerPanel.setActiveWidget(serviceGroupPage);
						return;
					}
				}
				serviceGroupPage = new ServiceGroupPage();
				serviceGroupPage.setVisible(true);
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

	private static SystemParameterTab systemParameterTab;
	public static HTML getSystemParameterMenuItem (final TabPanel centerPanel) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-wrench'></i>";

		HTML menuItem = new HTML(img + Mes.get("sysPar"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (systemParameterTab != null) {
					if(systemParameterTab.getTitle().equals("closed")) {
						systemParameterTab = null;
					}
					else {
						centerPanel.setActiveWidget(systemParameterTab);
						return;
					}
				}

				systemParameterTab = new SystemParameterTab();
				centerPanel.add(systemParameterTab, Mes.get("sysPar"));

				TabItemConfig config = centerPanel.getConfig(systemParameterTab);
				config.setIcon(FAIconsProvider.getIcons().wrench());
				config.setClosable(true);

				centerPanel.update(systemParameterTab, config);
				centerPanel.setActiveWidget(systemParameterTab);
			}
		});
		return menuItem;
	}


}
