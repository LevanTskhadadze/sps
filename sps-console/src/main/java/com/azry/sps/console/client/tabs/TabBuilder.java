package com.azry.sps.console.client.tabs;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.channel.ChannelTab;
import com.azry.sps.console.client.tabs.commissions.clientcommissions.ClientCommissionsTab;
import com.azry.sps.console.client.tabs.commissions.servicecommissions.ServiceCommissionsTab;
import com.azry.sps.console.client.tabs.payment.PaymentTab;
import com.azry.sps.console.client.tabs.perfompayment.PerformPaymentsTab;
import com.azry.sps.console.client.tabs.servicegroup.ServiceGroupTab;
import com.azry.sps.console.client.tabs.services.ServicesTab;
import com.azry.sps.console.client.tabs.systemparam.SystemParameterTab;
import com.azry.sps.console.client.tabs.usergroup.UserGroupTab;
import com.azry.sps.console.client.tabs.users.UsersTab;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.menu.Menu;

import java.util.List;

public class TabBuilder {

	private static ServiceGroupTab serviceGroupPage;

	public static HTML getServiceGroupMenuItem(final TabPanel centerPanel, final Menu menu) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-tag'></i>";

		HTML menuItem = new HTML(img + Mes.get("serviceGroups"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (serviceGroupPage != null) {
					if (serviceGroupPage.getTitle().equals("closed")) {
						serviceGroupPage = null;
					}
					else {
						centerPanel.setActiveWidget(serviceGroupPage);
						menu.hide();
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
				menu.hide();
			}

		});
		return menuItem;
	}

	private static SystemParameterTab systemParameterTab;
	public static HTML getSystemParameterMenuItem(final TabPanel centerPanel, final Menu menu) {

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
						menu.hide();
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
				menu.hide();
			}
		});
		return menuItem;
	}


	private static UsersTab usersTab;
	public static HTML getUsersMenuItem(final TabPanel centerPanel, final Menu menu) {

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
						menu.hide();
						return;
					}
				}
				ServicesFactory.getUserGroupService().getUserGroups(new ServiceCallback<List<UserGroupDTO>>() {
					@Override
					public void onServiceSuccess(List<UserGroupDTO> result) {
						usersTab = new UsersTab(result);
						centerPanel.add(usersTab, Mes.get("users"));

						TabItemConfig config = centerPanel.getConfig(usersTab);
						config.setIcon(FAIconsProvider.getIcons().user());
						config.setClosable(true);

						centerPanel.update(usersTab, config);
						centerPanel.setActiveWidget(usersTab);
						menu.hide();
					}
				});



			}
		});
		return menuItem;
	}


	private static UserGroupTab userGroupTab;

	public static HTML getUserGroupMenuItem(final TabPanel centerPanel, final Menu menu) {

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
						menu.hide();
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
				menu.hide();
			}
		});
		return menuItem;
	}

	private static ChannelTab channelTab;

	public static HTML getChannelMenuItem(final TabPanel centerPanel, final Menu menu) {

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
						menu.hide();
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
				menu.hide();
			}
		});
		return menuItem;
	}

	private static ServicesTab servicesTab;
	public static HTML getServicesMenuItem(final TabPanel centerPanel, final Menu menu) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-leaf'></i>";

		final HTML menuItem = new HTML(img + Mes.get("services"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (servicesTab != null) {
					if(servicesTab.getTitle().equals("closed")) {
						servicesTab = null;
					}
					else {
						centerPanel.setActiveWidget(servicesTab);
						menu.hide();
						return;
					}
				}

				servicesTab = new ServicesTab();
				centerPanel.add(servicesTab, Mes.get("services"));

				TabItemConfig config = centerPanel.getConfig(servicesTab);
				config.setIcon(FAIconsProvider.getIcons().leaf());
				config.setClosable(true);

				centerPanel.update(servicesTab, config);
				centerPanel.setActiveWidget(servicesTab);
				menu.hide();
			}
		});
		return menuItem;
	}

	private static ClientCommissionsTab clientCommissionsTab;

	public static HTML getClientCommissionsMenuItem(final TabPanel centerPanel, final Menu menu) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-money'></i>";

		final HTML menuItem = new HTML(img + Mes.get("clientCommissions"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (clientCommissionsTab != null) {
					if(clientCommissionsTab.getTitle().equals("closed")) {
						clientCommissionsTab = null;
					}
					else {
						centerPanel.setActiveWidget(clientCommissionsTab);
						menu.hide();
						return;
					}
				}

				clientCommissionsTab = new ClientCommissionsTab();
				centerPanel.add(clientCommissionsTab, Mes.get("clientCommissions"));

				TabItemConfig config = centerPanel.getConfig(clientCommissionsTab);
				config.setIcon(FAIconsProvider.getIcons().money());
				config.setClosable(true);

				centerPanel.update(clientCommissionsTab, config);
				centerPanel.setActiveWidget(clientCommissionsTab);
				menu.hide();
			}
		});
		return menuItem;
	}

	private static ServiceCommissionsTab serviceCommissionsTab;

	public static HTML getServiceCommissionsMenuItem(final TabPanel centerPanel, final Menu menu) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-briefcase'></i>";

		final HTML menuItem = new HTML(img + Mes.get("serviceCommissions"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (serviceCommissionsTab != null) {
					if (serviceCommissionsTab.getTitle().equals("closed")) {
						serviceCommissionsTab = null;
					}
					else {
						centerPanel.setActiveWidget(serviceCommissionsTab);
						menu.hide();
						return;
					}
				}

				serviceCommissionsTab = new ServiceCommissionsTab();
				centerPanel.add(serviceCommissionsTab, Mes.get("serviceCommissions"));

				TabItemConfig config = centerPanel.getConfig(serviceCommissionsTab);
				config.setIcon(FAIconsProvider.getIcons().briefcase());
				config.setClosable(true);

				centerPanel.update(serviceCommissionsTab, config);
				centerPanel.setActiveWidget(serviceCommissionsTab);
				menu.hide();
			}
		});
		return menuItem;
	}

	private static PerformPaymentsTab performPaymentsTab;

	public static HTML getPerformPaymentsTabMenuItem(final TabPanel centerPanel, final Menu menu) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-star'></i>";

		final HTML menuItem = new HTML(img + Mes.get("performPayment"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (performPaymentsTab != null) {
					if (performPaymentsTab.getTitle().equals("closed")) {
						performPaymentsTab = null;
					}
					else {
						centerPanel.setActiveWidget(performPaymentsTab);
						menu.hide();
						return;
					}
				}

				performPaymentsTab = new PerformPaymentsTab();
				centerPanel.add(performPaymentsTab, Mes.get("performPaymentsTab"));

				TabItemConfig config = centerPanel.getConfig(performPaymentsTab);
				config.setIcon(FAIconsProvider.getIcons().star());
				config.setClosable(true);

				centerPanel.update(performPaymentsTab, config);
				centerPanel.setActiveWidget(performPaymentsTab);
				menu.hide();
			}
		});
		return menuItem;
	}


	private static PaymentTab paymentTab;

	public static HTML getPaymentTabMenuItem(final TabPanel centerPanel, final Menu menu) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-credit-card'></i>";

		final HTML menuItem = new HTML(img + Mes.get("payments"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				if (paymentTab != null) {
					if (paymentTab.getTitle().equals("closed")) {
						paymentTab = null;
					}
					else {
						centerPanel.setActiveWidget(paymentTab);
						menu.hide();
						return;
					}
				}

				ServicesFactory.getServiceTabService().getAllServices(new ServiceCallback<List<ServiceDto>>() {
					@Override
					public void onServiceSuccess(final List<ServiceDto> sevices) {
						ServicesFactory.getChannelService().getChannels(new ServiceCallback<List<ChannelDTO>>() {
							@Override
							public void onServiceSuccess(List<ChannelDTO> channels) {
								paymentTab = new PaymentTab(sevices, channels);
								centerPanel.add(paymentTab, Mes.get("payments"));

								TabItemConfig config = centerPanel.getConfig(paymentTab);
								config.setIcon(FAIconsProvider.getIcons().credit_card());
								config.setClosable(true);

								centerPanel.update(paymentTab, config);
								centerPanel.setActiveWidget(paymentTab);
								menu.hide();
							}
						});
					}
				});

			}
		});
		return menuItem;
	}
}
