package com.azry.sps.console.client.tabs.services;


import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZPagingToolBar;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.services.widgets.ServiceModifyWindow;
import com.azry.sps.console.client.tabs.services.widgets.ServiceTable;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.theme.neptune.client.base.button.Css3ButtonCellAppearance;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicesTab extends Composite {

	private final VerticalLayoutContainer content;

	private ZTextField nameField;

	private ZSimpleComboBox<String> activeComboBox;

	private PagingLoader<PagingLoadConfig, PagingLoadResult<ServiceDTO>> pagingLoader;

	ZGrid<ServiceDTO> grid;

	public ServicesTab() {
		super();

		content = new VerticalLayoutContainer();

		assembleContent();

		initWidget(content);

		initPaging();

	}

	private void initPaging() {
		RpcProxy<PagingLoadConfig, PagingLoadResult<ServiceDTO>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<ServiceDTO>>() {
			@Override
			public void load(PagingLoadConfig loadConfig, final AsyncCallback<PagingLoadResult<ServiceDTO>> callback) {
				Map<String, String> params = new HashMap<>();
				params.put("name", nameField.getCurrentValue());
				params.put("active", activeComboBox.getCurrentValue());


				ServicesFactory.getServiceTabService().getServices(params,
											loadConfig.getOffset(),
											loadConfig.getLimit(),
											new ServiceCallback<PagingLoadResult<ServiceDTO>>() {
					@Override
					public void onServiceSuccess(PagingLoadResult<ServiceDTO> result) {
						callback.onSuccess(result);
					}
				});

			}
		};

		pagingLoader = new PagingLoader<>(proxy);
		pagingLoader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, ServiceDTO, PagingLoadResult<ServiceDTO>>(ServiceTable.getListStore()));
		grid.setLoader(pagingLoader);
		List<Integer> pagingPossibleValues = new ArrayList<>();
		pagingPossibleValues.add(20);
		pagingPossibleValues.add(50);
		pagingPossibleValues.add(100);
		pagingPossibleValues.add(200);
		ZPagingToolBar pager = new ZPagingToolBar.Builder<>(pagingLoader)
			.possibleValue(pagingPossibleValues)
			.build();
		pager.getCombo();
		content.add(pager);
		pagingLoader.load();
	}


	private void assembleContent() {
		content.add(constructToolbar());

		grid = new ZGrid<>(ServiceTable.setListStore(new ArrayList<ServiceDTO>()), ServiceTable.getMyColumnModel());
		grid.setColumnResize(false);
		grid.getView().setForceFit(true);
		grid.getView().setColumnLines(true);
		content.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));

	}

	private ZToolBar constructToolbar() {
		nameField = new ZTextField.Builder()
			.emptyText(Mes.get("serviceNameShort"))
			.build();

		List<String> values = new ArrayList<>();
		values.add("1");
		values.add("0");
		activeComboBox = new ZSimpleComboBox.Builder<String>()
			.values(values)
			.keyProvider(new ModelKeyProvider<String>() {
				@Override
				public String getKey(String item) {
					return item.equals("1") ? "true" : "false";
				}
			})
			.labelProvider(new LabelProvider<String>() {
				@Override
				public String getLabel(String item) {
					return (item.equals("1") ? Mes.get("active") : Mes.get("inactive"));
				}
			})
			.width(200)
			.listWidth(400)
			.editable(false)
			.noSelectionLabel(Mes.get("active") + "/" + Mes.get("inactive"))
			.build();

		ZToolBar toolBar = new ZToolBar();

		toolBar.add(nameField);
		toolBar.add(activeComboBox);

		toolBar.add(getSearchButton());
		toolBar.add(getClearButton());
		toolBar.add(getAddButton());

		return toolBar;
	}

	private ZButton getSearchButton(){
		return new ZButton.Builder()
			.appearance(new Css3ButtonCellAppearance<String>())
			.icon(FAIconsProvider.getIcons().search())
			.text(Mes.get("searchEntry"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					pagingLoader.load();

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
					nameField.setText(null);
					activeComboBox.setValue(null);
				}
			} )
			.build();
	}

	private ZButton getAddButton(){
		return new ZButton.Builder()
			.appearance (new Css3ButtonCellAppearance<String>())
			.icon (FAIconsProvider.getIcons().plus())
			.text (Mes.get("addEntry"))
			.handler (new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					ServicesFactory.getChannelService().getFilteredChannels("", null, new ServiceCallback<List<ChannelDTO>>() {
						@Override
						public void onServiceSuccess(List<ChannelDTO> result) {
							new ServiceModifyWindow(null, ServiceTable.getListStore(), result);
						}
					});
				}
			})
			.build();
	}

}
