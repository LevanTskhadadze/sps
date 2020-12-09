package com.azry.sps.console.client.tabs.servicegroup;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.EnterKeyBinder;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZGridView;
import com.azry.gxt.client.zcomp.ZIconButtonCell;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.gxt.client.zcomp.helper.GridClickHandler;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.FormatDate;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.servicegroup.ServiceGroupDTO;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.theme.neptune.client.base.button.Css3ButtonCellAppearance;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

import java.util.ArrayList;
import java.util.List;

public class ServiceGroupTab extends Composite {

	private final VerticalLayoutContainer verticalLayoutContainer;

	private ZGrid<ServiceGroupDTO> grid;

	private ZToolBar toolBar;

	private ZTextField name;

	private final ListStore<ServiceGroupDTO> gridStore = new ListStore<>(new ModelKeyProvider<ServiceGroupDTO>() {
		@Override
		public String getKey(ServiceGroupDTO groupDTO) {
			return String.valueOf(groupDTO.getId());
		}
	});

	private ListLoader<ListLoadConfig, ListLoadResult<ServiceGroupDTO>> loader;

	boolean isManage;

	public ServiceGroupTab(boolean isManage) {
		this.isManage = isManage;
		verticalLayoutContainer = new VerticalLayoutContainer();
		initWidget(verticalLayoutContainer);
		initToolbar();
		initGrid();
		buildDisplay();
	}

	private void buildDisplay() {
		verticalLayoutContainer.add(toolBar);
		verticalLayoutContainer.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
	}

	private void initToolbar() {
		name = new ZTextField.Builder()
			.width(200)
			.emptyText(Mes.get("name"))
			.build();


		ZButton searchButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().search())
			.text(Mes.get("search"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					loader.load();
				}
			})
			.build();

		ZButton clearFiltersButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().eraser())
			.tooltip(Mes.get("clearFilter"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					clearFilter();
				}
			})
			.build();

		ZButton addButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().plus())
			.text(Mes.get("add"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.visible(isManage)
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					new ServiceGroupWindow(null, ActionMode.ADD) {
						@Override
						public void onSave(ServiceGroupDTO dto) {
							gridStore.add(dto);
						}
					}.showInCenter();
				}
			})
			.build();

		new EnterKeyBinder.Builder(searchButton)
			.add(name)
			.bind();


		toolBar = new ZToolBar(1, -1);
		toolBar.setEnableOverflow(false);

		toolBar.add(name);
		toolBar.add(searchButton);
		toolBar.add(clearFiltersButton);
		toolBar.add(addButton);
	}

	private void initGrid() {

		RpcProxy<ListLoadConfig, ListLoadResult<ServiceGroupDTO>> proxy = new RpcProxy<ListLoadConfig, ListLoadResult<ServiceGroupDTO>>() {
			@Override
			public void load(ListLoadConfig loadConfig, final AsyncCallback<ListLoadResult<ServiceGroupDTO>> callback) {
				ServicesFactory.getServiceGroupService().getFilteredServiceGroups(name.getValue(),
					new ServiceCallback<List<ServiceGroupDTO>>(ServiceGroupTab.this) {
					@Override
					public void onServiceSuccess(List<ServiceGroupDTO> result) {
						callback.onSuccess(new ListLoadResultBean<>(result));
					}
				});
			}
		};

		loader = new ListLoader<>(proxy);
		loader.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, ServiceGroupDTO, ListLoadResult<ServiceGroupDTO>>(gridStore));

		grid = new ZGrid<>(gridStore, getColumns(), new ZGridView<ServiceGroupDTO>());
		grid.getView().setColumnLines(true);
		grid.getView().setAutoFill(true);
		grid.getView().setForceFit(true);
		grid.getView().setStripeRows(true);
		grid.setLoader(loader);
		new QuickTip(grid);
		loader.load();
	}


	private ColumnModel<ServiceGroupDTO> getColumns() {
		List<ColumnConfig<ServiceGroupDTO, ?>> columns = new ArrayList<>();

		columns.add(new ZColumnConfig.Builder<ServiceGroupDTO, String>()
			.width(120)
			.fixed()
			.valueProvider(new ZStringProvider<ServiceGroupDTO>() {
				@Override
				public String getProperty(ServiceGroupDTO groupDTO) {
					return String.valueOf(groupDTO.getId());
				}
			})
			.header(Mes.get("id"))
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceGroupDTO, String>()
			.width(250)
			.header(Mes.get("name"))
			.valueProvider(new ZStringProvider<ServiceGroupDTO>() {
				@Override
				public String getProperty(ServiceGroupDTO groupDTO) {
					return groupDTO.getName();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceGroupDTO, String>()
			.width(50)
			.header(Mes.get("priority"))
			.valueProvider(new ZStringProvider<ServiceGroupDTO>() {
				@Override
				public String getProperty(ServiceGroupDTO groupDTO) {
					return String.valueOf(groupDTO.getPriority());
				}
			})
			.build());


		columns.add(new ZColumnConfig.Builder<ServiceGroupDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("createTime"))
			.valueProvider(new ZStringProvider<ServiceGroupDTO>() {
				@Override
				public String getProperty(ServiceGroupDTO groupDTO) {
					return FormatDate.formatDateTime(groupDTO.getCreateTime());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceGroupDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("lastUpdateTime"))
			.valueProvider(new ZStringProvider<ServiceGroupDTO>() {
				@Override
				public String getProperty(ServiceGroupDTO groupDTO) {
					return FormatDate.formatDateTime(groupDTO.getLastUpdateTime());
				}
			})
			.build());

		if (isManage) {
			columns.add(new ZColumnConfig.Builder<ServiceGroupDTO, String>()
				.width(32)
				.fixed()
				.cell(new ZIconButtonCell.Builder<ServiceGroupDTO, String>()
					.gridStore(gridStore)
					.tooltip(Mes.get("edit"))
					.icon(FAIconsProvider.getIcons().pencil())
					.clickHandler(new GridClickHandler<ServiceGroupDTO>() {
						@Override
						public void onClick(Cell.Context context, final ServiceGroupDTO dto) {
							new ServiceGroupWindow(dto, ActionMode.EDIT) {
								@Override
								public void onSave(ServiceGroupDTO dto) {
									gridStore.update(dto);
								}
							}.showInCenter();
						}
					})
					.build())
				.build());

			columns.add(new ZColumnConfig.Builder<ServiceGroupDTO, String>()
				.width(32)
				.fixed()
				.cell(new ZIconButtonCell.Builder<ServiceGroupDTO, String>()
					.gridStore(gridStore)
					.icon(FAIconsProvider.getIcons().trash())
					.tooltip(Mes.get("delete"))
					.clickHandler(new GridClickHandler<ServiceGroupDTO>() {
						@Override
						public void onClick(Cell.Context context, final ServiceGroupDTO dto) {
							new ZConfirmDialog(Mes.get("confirm"), Mes.get("objectDeleteConfirmation")) {
								@Override
								public void onConfirm() {
									ServicesFactory.getServiceGroupService().deleteServiceGroup(dto.getId(), new ServiceCallback<Void>(this) {
										@Override
										public void onServiceSuccess(Void result) {
											gridStore.remove(gridStore.indexOf(dto));
										}
									});
								}
							}.show();
						}
					})
					.build())
				.build());
		} else {
			columns.add(new ZColumnConfig.Builder<ServiceGroupDTO, String>()
				.width(32)
				.fixed()
				.cell(new ZIconButtonCell.Builder<ServiceGroupDTO, String>()
					.gridStore(gridStore)
					.tooltip(Mes.get("view"))
					.icon(FAIconsProvider.getIcons().eye())
					.clickHandler(new GridClickHandler<ServiceGroupDTO>() {
						@Override
						public void onClick(Cell.Context context, final ServiceGroupDTO dto) {
							new ServiceGroupWindow(dto, ActionMode.VIEW) {
								@Override
								public void onSave(ServiceGroupDTO dto) {
								}
							}.showInCenter();
						}
					})
					.build())
				.build());
		}

		return new ColumnModel<>(columns);
	}

	private void clearFilter() {
		name.setValue(null);
	}
}