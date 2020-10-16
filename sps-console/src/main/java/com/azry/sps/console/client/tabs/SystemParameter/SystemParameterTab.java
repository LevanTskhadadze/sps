package com.azry.sps.console.client.tabs.SystemParameter;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.SystemParameter.table.SystemParametersModifyWindow;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.azry.sps.console.client.tabs.SystemParameter.table.SystemParametersTable;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemParameterTab {

	private VerticalLayoutContainer content;

	private final ZTextField codeField= new ZTextField.Builder()
		.emptyText(Mes.get("codeEmptyText"))
		.build();

	private final ZTextField valueField = new ZTextField.Builder()
		.emptyText(Mes.get("valueEmptyText"))
		.build();

	private void assembleContent(List<SystemParameterDto> data){
		if (content != null) {
			return;
		}

		content = new VerticalLayoutContainer();
		content.add(getToolbar());
		ZGrid<SystemParameterDto> grid = new ZGrid<>(SystemParametersTable.setListStore(data), SystemParametersTable.getMyColumnModel());
		grid.setColumnResize(false);
		grid.getView().setForceFit(true);
		grid.getView().setColumnLines(true);
		content.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));

	}

	private ZToolBar getToolbar() {
		ZToolBar toolbar = new ZToolBar();

		toolbar.add(codeField);
		toolbar.add(valueField);

		toolbar.add(getSearchButton());
		toolbar.add(getClearButton());

		ZButton addButton = new ZButton.Builder()
			.appearance (new Css3ButtonCellAppearance<String>())
			.icon (FAIconsProvider.getIcons().plus())
			.text (Mes.get("addEntry"))
			.handler (new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					new SystemParametersModifyWindow(null, SystemParametersTable.getListStore());
				}
			})
			.build();

		addButton.getElement().getStyle().setColor("white");
		toolbar.add(addButton);

		toolbar.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
		toolbar.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
		toolbar.getElement().getStyle().setHeight(2, Style.Unit.EM);
		toolbar.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
		return toolbar;
	}


	private ZButton getSearchButton(){
		return new ZButton.Builder()
			.appearance(new Css3ButtonCellAppearance<String>())
			.icon(FAIconsProvider.getIcons().search())
			.text(Mes.get("searchEntry"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					Map<String, String> params = new HashMap<>();
					params.put("code", codeField.getValue());
					params.put("value", valueField.getValue());
					ServicesFactory.getSystemParameterService().getSystemParameterTab(params, new AsyncCallback<List<SystemParameterDto>>() {
						@Override
						public void onFailure(Throwable caught) {

						}

						@Override
						public void onSuccess(List<SystemParameterDto> result) {
							SystemParametersTable.setListStore(result);
						}
					});

				}
			})
			.build();
	}

	private ZButton getClearButton(){
		return new ZButton.Builder()
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
	}


	public HTML getMenuItem (final TabPanel centerPanel) {

		String img = "<i style='width:16px; height:16px;' class='fa fa-wrench'></i>";

		HTML menuItem = new HTML(img + Mes.get("sysPar"));
		menuItem.setStyleName("menuItem");
		menuItem.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent clickEvent) {
				ServicesFactory.getSystemParameterService().getSystemParameterTab(new HashMap<String, String>(), new AsyncCallback<List<SystemParameterDto>>() {
					@Override
					public void onFailure(Throwable throwable) {

					}

					@Override
					public void onSuccess(List<SystemParameterDto> result) {
						if(content != null) {
							centerPanel.setActiveWidget(content);
							return;
						}

						assembleContent(result);
						centerPanel.add(content, Mes.get("sysPar"));

						TabItemConfig config = centerPanel.getConfig(content);
						config.setIcon(FAIconsProvider.getIcons().wrench());
						config.setClosable(true);

						centerPanel.update(content, config);
					}
				});

			}
		});
		return menuItem;
	}
}
