package com.azry.sps.console.client.tabs.SystemParameter;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.systemparameters.SystemParameterDto;
import com.azry.sps.console.shared.systemparameters.SystemParametersTable;
import com.azry.sps.console.shared.systemparameters.TabService;
import com.azry.sps.console.shared.systemparameters.TabServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.theme.neptune.client.base.button.Css3ButtonCellAppearance;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.List;

public class SystemParameterTab {
	private final TabServiceAsync tabServlet = GWT.create(TabService.class);

	private ZToolBar getToolbar(){
		ZToolBar toolbar = new ZToolBar();
		final ZTextField codeField = new ZTextField.Builder()
			.emptyText(Mes.get("codeEmptyText"))
			.build();
		toolbar.add(codeField);

		final ZTextField valueField = new ZTextField.Builder()
			.emptyText(Mes.get("valueEmptyText"))
			.build();
		toolbar.add(valueField);

		ZButton searchButton = new ZButton.Builder()
			.appearance(new Css3ButtonCellAppearance<String>())
			.icon(FAIconsProvider.getIcons().search())
			.text(Mes.get("searchEntry"))
			.build();
		searchButton.getElement().getStyle().setColor("white");
		toolbar.add(searchButton);

		ZButton clearButton = new ZButton.Builder()
			.appearance(new Css3ButtonCellAppearance<String>())
			.icon(FAIconsProvider.getIcons().eraser())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					codeField.setText(null);
					valueField.setText(null);
				}
			} )
			.build();

		clearButton.getElement().getStyle().setBorderStyle(Style.BorderStyle.HIDDEN);
		searchButton.getElement().getStyle().setColor("white");
		toolbar.add(clearButton);

		ZButton addButton = new ZButton.Builder()
			.appearance(new Css3ButtonCellAppearance<String>())
			.icon(FAIconsProvider.getIcons().plus())
			.text(Mes.get("addEntry"))
			.build();

		addButton.getElement().getStyle().setColor("white");
		toolbar.add(addButton);

		toolbar.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
		toolbar.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
		toolbar.getElement().getStyle().setHeight(2, Style.Unit.EM);
		toolbar.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
		return toolbar;
	}

	public HTML getMenuItem(final TabPanel centerPanel){

		String img = "<i style='width:16px; height:16px;' class='fa fa-wrench'></i>";

		HTML menuItem = new HTML(img + Mes.get("sysPar"));
		menuItem.setStyleName("menuItem");

		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				tabServlet.getSystemParameterTab(new AsyncCallback<List<SystemParameterDto>>() {
					@Override
					public void onFailure(Throwable throwable) {

					}

					@Override
					public void onSuccess(List<SystemParameterDto> result) {
						VerticalLayoutContainer content = new VerticalLayoutContainer();
						content.add(getToolbar());
						ZGrid<SystemParameterDto> grid = new ZGrid<>(SystemParametersTable.getListStore(result), SystemParametersTable.getMyColumnModel());
						grid.setColumnResize(false);
						grid.getView().setForceFit(true);
						grid.getView().setColumnLines(true);
						content.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));

						centerPanel.add(content, Mes.get("sysPar"));
						TabItemConfig config = centerPanel.getConfig(content);
						config.setClosable(true);
						centerPanel.update(content, config);
					}
				});

			}
		});
		return menuItem;
	}
}
