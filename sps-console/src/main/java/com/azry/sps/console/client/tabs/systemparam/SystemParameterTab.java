package com.azry.sps.console.client.tabs.systemparam;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.tabs.systemparam.widgets.SystemParametersModifyWindow;
import com.azry.sps.console.client.tabs.systemparam.widgets.SystemParametersTable;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDTO;
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

	private boolean isManage;

	public SystemParameterTab(boolean isManage){
		this.isManage = isManage;
		content = new VerticalLayoutContainer();
		codeField = new ZTextField.Builder()
			.emptyText(Mes.get("codeEmptyText"))
			.build();

		valueField = new ZTextField.Builder()
			.emptyText(Mes.get("valueEmptyText"))
			.build();
		initWidget(content);
		ServicesFactory.getSystemParameterService().getSystemParameterTab(new HashMap<String, String>(),
			new ServiceCallback<List<SystemParameterDTO>>(this) {
			@Override
			public void onServiceSuccess(List<SystemParameterDTO> result) {
				assembleContent(result);
			}
		});
	}

	private void assembleContent(List<SystemParameterDTO> data){
		content.add(getToolbar());
		ZGrid<SystemParameterDTO> grid = new ZGrid<>(SystemParametersTable.setListStore(data), SystemParametersTable.getMyColumnModel(isManage));
		grid.setColumnResize(false);
		grid.getView().setForceFit(true);
		grid.getView().setColumnLines(true);
		content.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));

	}

	private ZToolBar getToolbar() {
		ZToolBar toolbar = new ZToolBar(1, -1);

		toolbar.add(codeField);
		toolbar.add(valueField);

		toolbar.add(getSearchButton());
		toolbar.add(getClearButton());

		ZButton addButton = new ZButton.Builder()
			.visible(isManage)
			.appearance (new Css3ButtonCellAppearance<String>())
			.icon (FAIconsProvider.getIcons().plus())
			.text (Mes.get("addEntry"))
			.visible(isManage)
			.handler (new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					new SystemParametersModifyWindow(null, SystemParametersTable.getListStore(), ActionMode.ADD);
				}
			})
			.build();

		toolbar.add(addButton);

		toolbar.getElement().getStyle().setHeight(1.7, Style.Unit.EM);
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
					ServicesFactory.getSystemParameterService().getSystemParameterTab(params, new ServiceCallback<List<SystemParameterDTO>>() {

						@Override
						public void onServiceSuccess(List<SystemParameterDTO> result) {
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
