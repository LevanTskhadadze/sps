package com.azry.sps.console.client.tabs;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.channel.ChannelTab;
import com.azry.sps.console.client.tabs.servicegroup.ServiceGroupTab;
import com.azry.sps.console.client.tabs.systemparam.SystemParameterTab;
import com.azry.sps.console.client.tabs.usergroup.UserGroupTab;
import com.azry.sps.console.client.tabs.users.UsersTab;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;

import java.util.List;

public class TabBuilder {

	private static ServiceGroupTab serviceGroupPage;

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
				serviceGroupPage = new ServiceGroupTab();
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


	private static UsersTab usersTab;
	public static HTML getUsersMenuItem (final TabPanel centerPanel) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-user'></i>";

		final HTML menuItem = new HTML(img + Mes.get("users"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (usersTab != null) {
					if(usersTab.getTitle().equals("closed")) {
						usersTab = null;
					}
					else {
						centerPanel.setActiveWidget(usersTab);
						return;
					}
				}
				ServicesFactory.getUserGroupService().getUserGroups(new ServiceCallback<List<UserGroupDTO>>() {
					@Override
					public void onServiceSuccess(List<UserGroupDTO> result) {
						usersTab = new UsersTab(result);
						centerPanel.add(usersTab, Mes.get("users"));

						TabItemConfig config = centerPanel.getConfig(usersTab);
						config.setIcon(FAIconsProvider.getIcons().wrench());
						config.setClosable(true);

						centerPanel.update(usersTab, config);
						centerPanel.setActiveWidget(usersTab);
					}
				});



			}
		});
		return menuItem;
	}


	private static UserGroupTab userGroupTab;

	public static HTML getUserGroupMenuItem (final TabPanel centerPanel) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-users'></i>";

		final HTML menuItem = new HTML(img + Mes.get("userGroups"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (userGroupTab != null) {
					if(userGroupTab.getTitle().equals("closed")) {
						userGroupTab = null;
					}
					else {
						centerPanel.setActiveWidget(userGroupTab);
						return;
					}
				}

						userGroupTab = new UserGroupTab();
						centerPanel.add(userGroupTab, Mes.get("userGroups"));

						TabItemConfig config = centerPanel.getConfig(userGroupTab);
						config.setIcon(FAIconsProvider.getIcons().users());
						config.setClosable(true);

						centerPanel.update(userGroupTab, config);
						centerPanel.setActiveWidget(userGroupTab);
			}
		});
		return menuItem;
	}

	private static ChannelTab channelTab;

	public static HTML getChannelMenuItem (final TabPanel centerPanel) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-arrow-circle-right'></i>";

		final HTML menuItem = new HTML(img + Mes.get("channels"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (channelTab != null) {
					if(channelTab.getTitle().equals("closed")) {
						channelTab = null;
					}
					else {
						centerPanel.setActiveWidget(channelTab);
						return;
					}
				}

				channelTab = new ChannelTab();
				centerPanel.add(channelTab, Mes.get("channels"));

				TabItemConfig config = centerPanel.getConfig(channelTab);
				config.setIcon(FAIconsProvider.getIcons().arrow_circle_right());
				config.setClosable(true);

				centerPanel.update(channelTab, config);
				centerPanel.setActiveWidget(channelTab);
			}
		});
		return menuItem;
	}

}
