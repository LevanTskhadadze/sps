package com.azry.sps.console.client;


import com.azry.gxt.client.zcomp.bootstrap.GxtTheme;
import com.azry.gxt.client.zcomp.bootstrap.ZComp;
import com.azry.gxt.client.zcomp.resources.ZIconsProvider;
import com.azry.sps.console.client.error.ErrorHandlerInit;
import com.azry.sps.console.client.tabs.TabBuilder;
import com.azry.sps.console.client.tabs.systemparam.SystemParameterTab;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.cell.core.client.ButtonCell;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.menu.Menu;


public class ConsoleEntryPoint implements EntryPoint {

	public static final Viewport VIEWPORT = new Viewport();

	private HTML getFooter(){
		HTML footerText = new HTML("This is a footer");
		footerText.setStyleName("FText");
		return footerText;
	}


	private String getMenuInnerHTML() {
		return "<table>" +
			"	<tr>" +
			"		<td ><img src=\"data:image/png;base64,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\"/></td>" +
			"	</tr>" +
			"</table>";
	}


	private TextButton getMenu(final TabPanel centerPanel) {
		final TextButton button = new TextButton();

		final Menu menu = new Menu();
		button.setMenu(menu);

		//TODO unify table creation process and move getMenuItem to TabBuilder class.
		SystemParameterTab systemParameterTab = new SystemParameterTab();
		HTML systemParameterMenuItem = systemParameterTab.getMenuItem(centerPanel);
		menu.add(systemParameterMenuItem);

		HTML serviceGroupMenuItem = TabBuilder.getServiceGroupMenuItem(centerPanel);
		menu.add(serviceGroupMenuItem);



		// button.setIcon(FAIconsProvider.getIcons().cog());
		button.setHTML(getMenuInnerHTML());
		button.setIconAlign(ButtonCell.IconAlign.RIGHT);
		button.setStylePrimaryName("menuButton");
		return button;
	}

	private HorizontalLayoutContainer getHeader(TabPanel centerPanel, SystemUserDTO user){
		HorizontalLayoutContainer navbar = new HorizontalLayoutContainer();
		navbar.setHeight(42);

		navbar.setStyleName("navbar");
		FlexTable menuButtonContainer = new FlexTable();
		menuButtonContainer.setWidget(0, 0, getMenu(centerPanel));

		navbar.add(menuButtonContainer, new HorizontalLayoutContainer.HorizontalLayoutData(1, -1));

		FlexTable userContainer = new FlexTable();
		userContainer.setHeight("44px");

		HTML username = new HTML(user.getUserName());
		username.setStyleName("user-block-username");

		HTML logoutItem = new HTML("<i style='width:16px; height:16px;' class='fa fa-sign-out'></i>" + Mes.get("logout"));
		logoutItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				Window.Location.replace("logout");
			}
		});

		logoutItem.setStyleName("logoutButton");

		FlexTable userButtonContainer = new FlexTable();
		userButtonContainer.setWidget(0, 0, logoutItem);
		userButtonContainer.setStyleName("userButtonContainer");
		userButtonContainer.setHeight("38px");

		userContainer.setWidget(0, 0, username);
		userContainer.setWidget(0, 1, userButtonContainer);
		navbar.add(userContainer);
		return navbar;
	}

	@Override
	public void onModuleLoad() {
		ErrorHandlerInit errorHandler = new ErrorHandlerInit();

		ServicesFactory.getUserService().loadAuthorisedUser(new ServiceCallback<SystemUserDTO>() {
			@Override
			public void onServiceSuccess(SystemUserDTO result) {
				VIEWPORT.add(BuildUI(result));
			}
		});
	}

	private BorderLayoutContainer BuildUI(SystemUserDTO user){
		ZComp.setTheme(GxtTheme.NEPTUNE);
		ZComp.setCustomIconsProvider(new ZIconsProvider());
		RootPanel.get().add(VIEWPORT);
		BorderLayoutContainer mainFrame = new BorderLayoutContainer();

		TabPanel centerPanel = new TabPanel();
		centerPanel.setStyleName("centerTabs");

		HorizontalLayoutContainer header = getHeader(centerPanel, user);

		HTML footerText = getFooter();

		BorderLayoutContainer.BorderLayoutData navbarData = new BorderLayoutContainer.BorderLayoutData();
		navbarData.setSize(44);

		BorderLayoutContainer.BorderLayoutData footerData= new BorderLayoutContainer.BorderLayoutData();
		footerData.setSize(25);


		mainFrame.setNorthWidget(header, navbarData);
		mainFrame.setCenterWidget(centerPanel);
		mainFrame.setSouthWidget(footerText, footerData);

		return mainFrame;
	}
}