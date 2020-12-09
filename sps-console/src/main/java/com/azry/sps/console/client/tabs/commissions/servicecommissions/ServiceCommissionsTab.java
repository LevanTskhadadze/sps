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
import com.azry.sps.console.shared.dto.commission.servicecommission.ServiceCommissionsDTO;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
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
import java.util.List;

public class ServiceCommissionsTab extends Composite {

	private List<ServiceDTO> serviceDTOs;

	private final VerticalLayoutContainer verticalLayoutContainer;

	private ZGrid<ServiceCommissionsDTO> grid;

	private ZToolBar toolBar;

	private ZPagingToolBar pagingToolBar;

	private ZSimpleComboBox<ServiceDTO> service;


	private final ListStore<ServiceCommissionsDTO> gridStore = new ListStore<>(new ModelKeyProvider<ServiceCommissionsDTO>() {
		@Override
		public String getKey(ServiceCommissionsDTO dto) {
			return String.valueOf(dto.getId());
		}
	});

	private PagingLoader<PagingLoadConfig, PagingLoadResult<ServiceCommissionsDTO>> loader;

	boolean isManage;

	public ServiceCommissionsTab(boolean isManage) {
		verticalLayoutContainer = new VerticalLayoutContainer();
		this.isManage = isManage;
		initWidget(verticalLayoutContainer);
		initToolbar();
		ServicesFactory.getServiceTabService().getAllServices(new ServiceCallback<List<ServiceDTO>>(this) {
			@Override
			public void onServiceSuccess(List<ServiceDTO> result) {
				serviceDTOs = result;
				Collections.sort(serviceDTOs,new Comparator<ServiceDTO>() {
					@Override
					public int compare(ServiceDTO o1, ServiceDTO o2) {
						return o1.getName().compareTo(o2.getName());
					}
				});
				initServiceComboboxData(serviceDTOs);
			}
		});
		initGrid();
		buildDisplay();
	}

	private void buildDisplay() {
		verticalLayoutContainer.add(toolBar);
		verticalLayoutContainer.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
		verticalLayoutContainer.add(pagingToolBar);
	}

	private void initToolbar() {

		service = new ZSimpleComboBox.Builder<ServiceDTO>()
			.keyProvider(new ModelKeyProvider<ServiceDTO>() {
				@Override
				public String getKey(ServiceDTO dto) {
					return String.valueOf(dto.getId());
				}
			})
			.labelProvider(new LabelProvider<ServiceDTO>() {
				@Override
				public String getLabel(ServiceDTO dto) {
					return dto.getName();
				}
			})
			.noSelectionLabel(Mes.get("allServices"))
			.template(new AbstractSafeHtmlRenderer<ServiceDTO>() {
				@Override
				public SafeHtml render(ServiceDTO object) {
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

		ZButton addButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().plus())
			.text(Mes.get("add"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.visible(isManage)
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					new ServiceCommissionsWindow(null, serviceDTOs, ActionMode.ADD) {
						@Override
						public void onSave(ServiceCommissionsDTO dto) {
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


	private void initServiceComboboxData(List<ServiceDTO> dtos) {

		service.add(new ServiceDTO(-1, Mes.get("allServicesSelected")));
		service.addAll(dtos);
	}


	private String getServiceIdForFilter() {
		if (service != null) {
			ServiceDTO dto = service.getValue();
			return dto == null ? null : String.valueOf(dto.getId());
		}
		return null;
	}

	private void initGrid() {
		RpcProxy<PagingLoadConfig, PagingLoadResult<ServiceCommissionsDTO>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<ServiceCommissionsDTO>>() {
			@Override
			public void load(PagingLoadConfig loadConfig, final AsyncCallback<PagingLoadResult<ServiceCommissionsDTO>> callback) {
				ServicesFactory.getServiceCommissionsService().getServiceCommissions(
						getServiceIdForFilter(),
						loadConfig,
						new ServiceCallback<PagingLoadResult<ServiceCommissionsDTO>>(ServiceCommissionsTab.this) {
					@Override
					public void onServiceSuccess(PagingLoadResult<ServiceCommissionsDTO> result) {
						callback.onSuccess(result);
					}
				});
			}
		};

		loader = new PagingLoader<>(proxy);
		loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, ServiceCommissionsDTO, PagingLoadResult<ServiceCommissionsDTO>>(gridStore));

		List<Integer> pageSize = new ArrayList<>();
		Collections.addAll(pageSize, 50, 100, 500, 1000);

		pagingToolBar = new ZPagingToolBar.Builder<>(loader)
			.defaultValue(50)
			.possibleValue(pageSize)
			.build();

		grid = new ZGrid<>(gridStore, getColumns(), new ZGridView<ServiceCommissionsDTO>());
		grid.getView().setColumnLines(true);
		grid.getView().setAutoFill(true);
		grid.getView().setForceFit(true);
		grid.getView().setStripeRows(true);
		grid.setLoader(loader);
		new QuickTip(grid);
		loader.load();
	}


	private ColumnModel<ServiceCommissionsDTO> getColumns() {
		List<ColumnConfig<ServiceCommissionsDTO, ?>> columns = new ArrayList<>();

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDTO, String>()
			.width(120)
			.fixed()
			.valueProvider(new ZStringProvider<ServiceCommissionsDTO>() {
				@Override
				public String getProperty(ServiceCommissionsDTO dto) {
					return String.valueOf(dto.getPriority());
				}
			})
			.header(Mes.get("priority"))
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDTO, String>()
			.width(250)
			.valueProvider(new ZStringProvider<ServiceCommissionsDTO>() {
				@Override
				public String getProperty(ServiceCommissionsDTO dto) {
					return dto.isAllServices() ? Mes.get("allServices") : Mes.get("services") + ":" + dto.getServicesIds().size();
				}
			})
			.header(Mes.get("services"))
			.build());


		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("minCommission"))
			.valueProvider(new ZStringProvider<ServiceCommissionsDTO>() {
				@Override
				public String getProperty(ServiceCommissionsDTO dto) {
					String pSign = dto.getRateType() == CommissionRateTypeDTO.PERCENT ? "%" : "";
					return dto.getMinCommission() == null ? Mes.get("notDefined") : (dto.getMinCommission() + pSign);
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("maxCommission"))
			.valueProvider(new ZStringProvider<ServiceCommissionsDTO>() {
				@Override
				public String getProperty(ServiceCommissionsDTO dto) {
					String pSign = dto.getRateType() == CommissionRateTypeDTO.PERCENT ? "%" : "";
					return dto.getMaxCommission() == null ? Mes.get("notDefined") : (dto.getMaxCommission() + pSign);
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDTO, String>()
			.width(100)
			.fixed()
			.header(Mes.get("commission"))
			.valueProvider(new ZStringProvider<ServiceCommissionsDTO>() {
				@Override
				public String getProperty(ServiceCommissionsDTO dto) {
					String pSign = dto.getRateType() == CommissionRateTypeDTO.PERCENT ? "%" : "";
					return dto.getCommission() + pSign;
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("createTime"))
			.valueProvider(new ZStringProvider<ServiceCommissionsDTO>() {
				@Override
				public String getProperty(ServiceCommissionsDTO dto) {
					return FormatDate.formatDateTime(dto.getCreateTime());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceCommissionsDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("lastUpdateTime"))
			.valueProvider(new ZStringProvider<ServiceCommissionsDTO>() {
				@Override
				public String getProperty(ServiceCommissionsDTO dto) {
					return FormatDate.formatDateTime(dto.getLastUpdateTime());
				}
			})
			.build());

		if (isManage) {
			columns.add(new ZColumnConfig.Builder<ServiceCommissionsDTO, String>()
				.width(32)
				.fixed()
				.cell(new ZIconButtonCell.Builder<ServiceCommissionsDTO, String>()
					.gridStore(gridStore)
					.tooltip(Mes.get("edit"))
					.icon(FAIconsProvider.getIcons().pencil())
					.clickHandler(new GridClickHandler<ServiceCommissionsDTO>() {
						@Override
						public void onClick(Cell.Context context, final ServiceCommissionsDTO dto) {
							new ServiceCommissionsWindow(dto, serviceDTOs, ActionMode.EDIT) {
								@Override
								public void onSave(ServiceCommissionsDTO dto) {
									gridStore.update(dto);
								}
							}.showInCenter();
						}
					})
					.build())
				.build());

			columns.add(new ZColumnConfig.Builder<ServiceCommissionsDTO, String>()
				.width(32)
				.fixed()
				.cell(new ZIconButtonCell.Builder<ServiceCommissionsDTO, String>()
					.gridStore(gridStore)
					.icon(FAIconsProvider.getIcons().trash())
					.tooltip(Mes.get("delete"))
					.clickHandler(new GridClickHandler<ServiceCommissionsDTO>() {
						@Override
						public void onClick(Cell.Context context, final ServiceCommissionsDTO dto) {
							new ZConfirmDialog(Mes.get("confirm"), Mes.get("objectDeleteConfirmation")) {
								@Override
								public void onConfirm() {
									ServicesFactory.getServiceCommissionsService().deleteServiceCommissions(dto.getId(), new ServiceCallback<Void>(this) {
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
			columns.add(new ZColumnConfig.Builder<ServiceCommissionsDTO, String>()
				.width(32)
				.fixed()
				.cell(new ZIconButtonCell.Builder<ServiceCommissionsDTO, String>()
					.gridStore(gridStore)
					.tooltip(Mes.get("view"))
					.icon(FAIconsProvider.getIcons().eye())
					.clickHandler(new GridClickHandler<ServiceCommissionsDTO>() {
						@Override
						public void onClick(Cell.Context context, final ServiceCommissionsDTO dto) {
							new ServiceCommissionsWindow(dto, serviceDTOs, ActionMode.VIEW) {
								@Override
								public void onSave(ServiceCommissionsDTO dto) {
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
		if (service.getValue() != null) {
			service.setValue(null);
		}
	}
}
