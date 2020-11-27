package com.azry.sps.console.client.tabs.systemparam;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.systemparam.table.SystemParametersModifyWindow;
import com.azry.sps.console.client.tabs.systemparam.table.SystemParametersTable;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.google.gwt.dom.client.Style;
import com.sencha.gxt.theme.neptune.client.base.button.Css3ButtonCellAppearance;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemParameterTab extends Composite {

	private VerticalLayoutContainer content;

	private final ZTextField codeField;

	private final ZTextField valueField;

	public SystemParameterTab(){
		super();
		content = new VerticalLayoutContainer();
		codeField = new ZTextField.Builder()
			.emptyText(Mes.get("codeEmptyText"))
			.build();

		valueField = new ZTextField.Builder()
			.emptyText(Mes.get("valueEmptyText"))
			.build();
		initWidget(content);
		ServicesFactory.getSystemParameterService().getSystemParameterTab(new HashMap<String, String>(),
			new ServiceCallback<List<SystemParameterDto>>(this) {
			@Override
			public void onServiceSuccess(List<SystemParameterDto> result) {
				assembleContent(result);
			}
		});



	}

	private void assembleContent(List<SystemParameterDto> data){
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
					ServicesFactory.getSystemParameterService().getSystemParameterTab(params, new ServiceCallback<List<SystemParameterDto>>() {

						@Override
						public void onServiceSuccess(List<SystemParameterDto> result) {
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


}
