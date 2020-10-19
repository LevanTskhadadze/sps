package com.azry.sps.console.client.tabs.servicegroup;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.EnterKeyBinder;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZGrid;
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
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
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

public class ServiceGroupPage extends Composite {

	VerticalLayoutContainer verticalLayoutContainer;

	ZGrid<ServiceGroupDTO> grid;

	ZToolBar toolBar;

	ZTextField name;

	ZButton searchButton;

	ZButton clearFiltersButton;

	ZButton addButton;


	private ListStore<ServiceGroupDTO> gridStore = new ListStore<>(new ModelKeyProvider<ServiceGroupDTO>() {
		@Override
		public String getKey(ServiceGroupDTO groupDTO) {
			return String.valueOf(groupDTO.getId());
		}
	});

	Store.StoreSortInfo storeSortInfo;

	private ListLoader<ListLoadConfig, ListLoadResult<ServiceGroupDTO>> loader;


	public ServiceGroupPage() {
		verticalLayoutContainer = new VerticalLayoutContainer();
		initToolbar();
		initGrid();
		initWidget(verticalLayoutContainer);
		buildDisplay();
	}

	private void buildDisplay() {
		verticalLayoutContainer.add(toolBar);
		verticalLayoutContainer.add(grid);
	}

	private void initToolbar() {
		name = new ZTextField.Builder()
			.width(200)
			.emptyText(Mes.get("name"))
			.build();


		searchButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().search())
			.text(Mes.get("search"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					grid.getLoader().load();
				}
			})
			.build();

		clearFiltersButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().eraser())
			.tooltip(Mes.get("clearFilter"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					clearFilter();
				}
			})
			.build();

		addButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().plus())
			.text(Mes.get("add"))
			.appearance(new Css3ButtonCellAppearance<String>())
//			.visible(isManage)
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					new ServiceGroupWindow(null, ActionMode.ADD){
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
				ServicesFactory.getServiceGroupService().getFilteredServiceGroups(name.getValue(), new ServiceCallback<List<ServiceGroupDTO>>() {
					@Override
					public void onServiceSuccess(List<ServiceGroupDTO> result) {
						callback.onSuccess(new ListLoadResultBean<>(result));
					}
				});
			}
		};

		loader = new ListLoader<>(proxy);
		loader.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, ServiceGroupDTO, ListLoadResult<ServiceGroupDTO>>(gridStore));

		storeSortInfo = new Store.StoreSortInfo(new ValueProvider<ServiceGroupDTO, Long>() {
			@Override
			public Long getValue(ServiceGroupDTO groupDTO) {
				return groupDTO.getPriority();
			}

			@Override
			public void setValue(ServiceGroupDTO o, Long o2) { }

			@Override
			public String getPath() {
				return null;
			}
		}, SortDir.ASC);

		gridStore.addSortInfo(storeSortInfo);

		grid = new ZGrid(gridStore, getColumns());
		grid.getView().setColumnLines(true);
		grid.getView().setAutoFill(true);
		grid.getView().setForceFit(true);
		grid.getView().setColumnLines(true);
		grid.getView().setStripeRows(true);
		grid.setColumnReordering(false);
		grid.setColumnResize(false);
		grid.setLoader(loader);
		new QuickTip(grid);
		grid.addAttachHandler(new AttachEvent.Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent attachEvent) {
				if (attachEvent.isAttached()) {
					loader.load();
				}
			}
		});
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
			.width(100)
			.header(Mes.get("createTime"))
			.valueProvider(new ZStringProvider<ServiceGroupDTO>() {
				@Override
				public String getProperty(ServiceGroupDTO groupDTO) {
					return FormatDate.formatDateTime(groupDTO.getCreateTime());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceGroupDTO, String>()
			.width(100)
			.header(Mes.get("lastUpdateTime"))
			.valueProvider(new ZStringProvider<ServiceGroupDTO>() {
				@Override
				public String getProperty(ServiceGroupDTO groupDTO) {
					return FormatDate.formatDateTime(groupDTO.getLastUpdateTime());
				}
			})
			.build());

//		if (canEdit()) {
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
									gridStore.applySort(false);
								}
							}.showInCenter();
						}
					})
					.build())
				.build());

//		}

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
								ServicesFactory.getServiceGroupService().deleteServiceGroup(dto.getId(), new ServiceCallback<Void>() {
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

		return new ColumnModel<>(columns);
	}

	private void clearFilter() {
		name.setValue(null);
	}
}