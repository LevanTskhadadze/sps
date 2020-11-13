package com.azry.sps.console.client.tabs.commissions.servicecommissions;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.EnterKeyBinder;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZGridView;
import com.azry.gxt.client.zcomp.ZIconButtonCell;
import com.azry.gxt.client.zcomp.ZPagingToolBar;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.gxt.client.zcomp.helper.GridClickHandler;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.FormatDate;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.commission.CommissionRateTypeDTO;
import com.azry.sps.console.shared.dto.commission.servicecommission.ServiceCommissionsDto;
import com.azry.sps.console.shared.dto.services.ServiceEntityDto;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.theme.neptune.client.base.button.Css3ButtonCellAppearance;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ServiceCommissionsTab extends Composite {

	private List<ServiceEntityDto> serviceEntityDTOs;

	private final VerticalLayoutContainer verticalLayoutContainer;

	private ZGrid<ServiceCommissionsDto> grid;

	private ZToolBar toolBar;

	private ZPagingToolBar pagingToolBar;

	private ZSimpleComboBox<ServiceEntityDto> service;


	private final ListStore<ServiceCommissionsDto> gridStore = new ListStore<>(new ModelKeyProvider<ServiceCommissionsDto>() {
		@Override
		public String getKey(ServiceCommissionsDto dto) {
			return String.valueOf(dto.getId());
		}
	});

	Store.StoreSortInfo<ServiceCommissionsDto> storeSortInfo;

	private PagingLoader<PagingLoadConfig, PagingLoadResult<ServiceCommissionsDto>> loader;


	public ServiceCommissionsTab() {
		initToolbar();
		ServicesFactory.getServiceTabService().getAllServiceEntities(new ServiceCallback<List<ServiceEntityDto>>() {
			@Override
			public void onServiceSuccess(List<ServiceEntityDto> result) {
				serviceEntityDTOs = result;
				Collections.sort(serviceEntityDTOs,new Comparator<ServiceEntityDto>() {
					@Override
					public int compare(ServiceEntityDto o1, ServiceEntityDto o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
				initServiceComboboxData(serviceEntityDTOs);
			}
		});
		verticalLayoutContainer = new VerticalLayoutContainer();
		initGrid();
		initWidget(verticalLayoutContainer);
		buildDisplay();
	}

	private void buildDisplay() {
		verticalLayoutContainer.add(toolBar);
		verticalLayoutContainer.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
		verticalLayoutContainer.add(pagingToolBar);
	}

	private void initToolbar() {

		service = new ZSimpleComboBox.Builder<ServiceEntityDto>()
			.keyProvider(new ModelKeyProvider<ServiceEntityDto>() {
				@Override
				public String getKey(ServiceEntityDto dto) {
					return String.valueOf(dto.getId());
				}
			})
			.labelProvider(new LabelProvider<ServiceEntityDto>() {
				@Override
				public String getLabel(ServiceEntityDto dto) {
					return dto.getName();
				}
			})
			.noSelectionLabel(Mes.get("allServices"))
			.template(new AbstractSafeHtmlRenderer<ServiceEntityDto>() {
				@Override
				public SafeHtml render(ServiceEntityDto object) {
					String inlineStyle = "font-size: 13px;";
					if (object.getId() == -1) {
						inlineStyle += "color: grey; border-top: 1px dotted grey; border-bottom: 1px dotted grey;";
					}
					return SafeHtmlUtils.fromSafeConstant("<div style='" + inlineStyle + "'>" + object.getName() + "</div>");
				}
			})
			.enableSorting(false)
			.editable(false)
			.width(200)
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

		//			.visible(isManage)
		ZButton addButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().plus())
			.text(Mes.get("add"))
			.appearance(new Css3ButtonCellAppearance<String>())
//			.visible(isManage)
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					new ServiceCommissionsWindow(null, serviceEntityDTOs, ActionMode.ADD) {
						@Override
						public void onSave(ServiceCommissionsDto dto) {
							gridStore.add(dto);
						}
					}.showInCenter();
				}
			})
			.build();

		new EnterKeyBinder.Builder(searchButton)
			.add(service)
			.bind();


		toolBar = new ZToolBar(1, -1);
		toolBar.setEnableOverflow(false);


		toolBar.add(service);
		toolBar.add(searchButton);
		toolBar.add(clearFiltersButton);
		toolBar.add(addButton);
	}


	private void initServiceComboboxData(List<ServiceEntityDto> dtos) {

		service.add(new ServiceEntityDto(-1, Mes.get("allServicesSelected")));
		service.addAll(dtos);
	}


	private String getServiceIdForFilter() {
		if (service != null) {
			ServiceEntityDto dto = service.getValue();
			return dto == null ? null : String.valueOf(dto.getId());
		}
		return null;
	}

	private void initGrid() {

		RpcProxy<PagingLoadConfig, PagingLoadResult<ServiceCommissionsDto>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<ServiceCommissionsDto>>() {
			@Override
			public void load(PagingLoadConfig loadConfig, final AsyncCallback<PagingLoadResult<ServiceCommissionsDto>> callback) {
				ServicesFactory.getServiceCommissionsService().getServiceCommissions(
						getServiceIdForFilter(),
						loadConfig,
						new ServiceCallback<PagingLoadResult<ServiceCommissionsDto>>() {
					@Override
					public void onServiceSuccess(PagingLoadResult<ServiceCommissionsDto> result) {
						callback.onSuccess(result);
					}
				});
			}
		};

		loader = new PagingLoader<>(proxy);
		loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, ServiceCommissionsDto, PagingLoadResult<ServiceCommissionsDto>>(gridStore));

		storeSortInfo = new Store.StoreSortInfo<>(new ValueProvider<ServiceCommissionsDto, Date>() {
			@Override
			public Date getValue(ServiceCommissionsDto dto) {
				return dto.getLastUpdateTime();
			}

			@Override
			public void setValue(ServiceCommissionsDto o, Date o2) { }

			@Override
			public String getPath() {
				return null;
			}
		}, SortDir.DESC);

		gridStore.addSortInfo(storeSortInfo);

		List<Integer> pageSize = new ArrayList<>();
		Collections.addAll(pageSize, 50, 100, 500, 1000);

		pagingToolBar = new ZPagingToolBar.Builder<>(loader)
			.defaultValue(50)
			.possibleValue(pageSize)
			.build();

		grid = new ZGrid<>(gridStore, getColumns(), new ZGridView<ServiceCommissionsDto>());
		grid.getView().setColumnLines(true);
		grid.getView().setAutoFill(true);
		grid.getView().setForceFit(true);
		grid.getView().setStripeRows(true);
		grid.getView().getHeader().setDisableSortIcon(true);
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


	private ColumnModel<ServiceCommissionsDto> getColumns() {
		List<ColumnConfig<ServiceCommissionsDto, ?>> columns = new ArrayList<>();

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDto, String>()
			.width(50)
//			.fixed()
			.valueProvider(new ZStringProvider<ServiceCommissionsDto>() {
				@Override
				public String getProperty(ServiceCommissionsDto dto) {
					return String.valueOf(dto.getPriority());
				}
			})
			.header(Mes.get("priority"))
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDto, String>()
			.width(250)
//			.fixed()
			.valueProvider(new ZStringProvider<ServiceCommissionsDto>() {
				@Override
				public String getProperty(ServiceCommissionsDto dto) {
					return dto.isAllServices() ? Mes.get("allServices") : Mes.get("services") + ":" + dto.getServicesIds().size();
				}
			})
			.header(Mes.get("services"))
			.build());


		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDto, String>()
			.width(200)
			.fixed()
			.header(Mes.get("minCommission"))
			.valueProvider(new ZStringProvider<ServiceCommissionsDto>() {
				@Override
				public String getProperty(ServiceCommissionsDto dto) {
					String pSign = dto.getRateType() == CommissionRateTypeDTO.PERCENT ? "%" : "";
					return dto.getMinCommission() == null ? Mes.get("notDefined") : (dto.getMinCommission() + pSign);
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDto, String>()
			.width(200)
			.fixed()
			.header(Mes.get("maxCommission"))
			.valueProvider(new ZStringProvider<ServiceCommissionsDto>() {
				@Override
				public String getProperty(ServiceCommissionsDto dto) {
					String pSign = dto.getRateType() == CommissionRateTypeDTO.PERCENT ? "%" : "";
					return dto.getMaxCommission() == null ? Mes.get("notDefined") : (dto.getMaxCommission() + pSign);
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDto, String>()
			.width(100)
			.fixed()
			.header(Mes.get("commission"))
			.valueProvider(new ZStringProvider<ServiceCommissionsDto>() {
				@Override
				public String getProperty(ServiceCommissionsDto dto) {
					String pSign = dto.getRateType() == CommissionRateTypeDTO.PERCENT ? "%" : "";
					return dto.getCommission() + pSign;
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDto, String>()
			.width(200)
			.fixed()
			.header(Mes.get("createTime"))
			.valueProvider(new ZStringProvider<ServiceCommissionsDto>() {
				@Override
				public String getProperty(ServiceCommissionsDto dto) {
					return FormatDate.formatDateTime(dto.getCreateTime());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDto, String>()
			.width(200)
			.fixed()
			.header(Mes.get("lastUpdateTime"))
			.valueProvider(new ZStringProvider<ServiceCommissionsDto>() {
				@Override
				public String getProperty(ServiceCommissionsDto dto) {
					return FormatDate.formatDateTime(dto.getLastUpdateTime());
				}
			})
			.build());

//		if (canEdit()) {
		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDto, String>()
			.width(32)
			.fixed()
			.cell(new ZIconButtonCell.Builder<ServiceCommissionsDto, String>()
				.gridStore(gridStore)
				.tooltip(Mes.get("edit"))
				.icon(FAIconsProvider.getIcons().pencil())
				.clickHandler(new GridClickHandler<ServiceCommissionsDto>() {
					@Override
					public void onClick(Cell.Context context, final ServiceCommissionsDto dto) {
						new ServiceCommissionsWindow(dto, serviceEntityDTOs, ActionMode.EDIT) {
							@Override
							public void onSave(ServiceCommissionsDto dto) {
								gridStore.update(dto);
								gridStore.applySort(false);
							}
						}.showInCenter();
					}
				})
				.build())
			.build());

//		}

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDto, String>()
			.width(32)
			.fixed()
			.cell(new ZIconButtonCell.Builder<ServiceCommissionsDto, String>()
				.gridStore(gridStore)
				.icon(FAIconsProvider.getIcons().trash())
				.tooltip(Mes.get("delete"))
				.clickHandler(new GridClickHandler<ServiceCommissionsDto>() {
					@Override
					public void onClick(Cell.Context context, final ServiceCommissionsDto dto) {
						new ZConfirmDialog(Mes.get("confirm"), Mes.get("objectDeleteConfirmation")) {
							@Override
							public void onConfirm() {
								ServicesFactory.getServiceCommissionsService().deleteServiceCommissions(dto.getId(), new ServiceCallback<Void>() {
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
		service.setValue(null);
		loader.load();
	}
}
