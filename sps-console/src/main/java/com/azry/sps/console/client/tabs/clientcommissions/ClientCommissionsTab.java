package com.azry.sps.console.client.tabs.clientcommissions;

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
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.commission.ClientCommissionsDTO;
import com.azry.sps.console.shared.dto.commission.CommissionRateTypeDTO;
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
import java.util.Date;
import java.util.List;

public class ClientCommissionsTab extends Composite {

	private List<ChannelDTO> channelDTOs;

	private List<ServiceEntityDto> serviceEntityDTOs;

	private final VerticalLayoutContainer verticalLayoutContainer;

	private ZGrid<ClientCommissionsDTO> grid;

	private ZToolBar toolBar;

	private ZPagingToolBar pagingToolBar;

	private ZSimpleComboBox<ServiceEntityDto> service;

	private ZSimpleComboBox<ChannelDTO> channel;


	private final ListStore<ClientCommissionsDTO> gridStore = new ListStore<>(new ModelKeyProvider<ClientCommissionsDTO>() {
		@Override
		public String getKey(ClientCommissionsDTO dto) {
			return String.valueOf(dto.getId());
		}
	});

	Store.StoreSortInfo<ClientCommissionsDTO> storeSortInfo;

	private PagingLoader<PagingLoadConfig, PagingLoadResult<ClientCommissionsDTO>> loader;


	public ClientCommissionsTab() {
		initToolbar();
		getData();
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

	private void getData() {
		ServicesFactory.getServiceTabService().getAllServices(new ServiceCallback<List<ServiceEntityDto>>() {
			@Override
			public void onServiceSuccess(List<ServiceEntityDto> result) {
				serviceEntityDTOs = result;
				initServiceComboboxData(serviceEntityDTOs);
			}
		});
		ServicesFactory.getChannelService().getChannels(new ServiceCallback<List<ChannelDTO>>() {
			@Override
			public void onServiceSuccess(List<ChannelDTO> result) {
				channelDTOs = result;
				initChannelComboboxData(channelDTOs);
			}
		});
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

		channel = new ZSimpleComboBox.Builder<ChannelDTO>()
			.keyProvider(new ModelKeyProvider<ChannelDTO>() {
				@Override
				public String getKey(ChannelDTO dto) {
					return String.valueOf(dto.getId());
				}
			})
			.labelProvider(new LabelProvider<ChannelDTO>() {
				@Override
				public String getLabel(ChannelDTO dto) {
					return dto.getName();
				}
			})
			.noSelectionLabel(Mes.get("allChannels"))
			.template(new AbstractSafeHtmlRenderer<ChannelDTO>() {
				@Override
				public SafeHtml render(ChannelDTO object) {
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
					new ClientCommissionsWindow(null, serviceEntityDTOs, channelDTOs, ActionMode.ADD) {
						@Override
						public void onSave(ClientCommissionsDTO dto) {
							gridStore.add(dto);
						}
					}.showInCenter();
				}
			})
			.build();

		new EnterKeyBinder.Builder(searchButton)
			.add(service)
			.add(channel)
			.bind();


		toolBar = new ZToolBar(1, -1);
		toolBar.setEnableOverflow(false);


		toolBar.add(service);
		toolBar.add(channel);
		toolBar.add(searchButton);
		toolBar.add(clearFiltersButton);
		toolBar.add(addButton);
	}

	private void initChannelComboboxData(List<ChannelDTO> dtos) {

		channel.add(new ChannelDTO(-1, Mes.get("allChannelsSelected")));
		channel.addAll(dtos);
	}

	private void initServiceComboboxData(List<ServiceEntityDto> dtos) {

		service.add(new ServiceEntityDto(-1, Mes.get("allServicesSelected")));
		service.addAll(dtos);
	}


	private String getChannelIdForFilter() {
		if (channel != null) {
			ChannelDTO dto = channel.getValue();
			return dto == null ? null : String.valueOf(dto.getId());
		}
		return null;
	}

	private String getServiceIdForFilter() {
		if (service != null) {
			ServiceEntityDto dto = service.getValue();
			return dto == null ? null : String.valueOf(dto.getId());
		}
		return null;
	}

	private void initGrid() {

		RpcProxy<PagingLoadConfig, PagingLoadResult<ClientCommissionsDTO>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<ClientCommissionsDTO>>() {
			@Override
			public void load(PagingLoadConfig loadConfig, final AsyncCallback<PagingLoadResult<ClientCommissionsDTO>> callback) {
				ServicesFactory.getClientCommissionsService().getFilteredClientCommissions(
						getServiceIdForFilter(),
						getChannelIdForFilter(),
						loadConfig,
						new ServiceCallback<PagingLoadResult<ClientCommissionsDTO>>() {
					@Override
					public void onServiceSuccess(PagingLoadResult<ClientCommissionsDTO> result) {
						callback.onSuccess(result);
					}
				});
			}
		};

		loader = new PagingLoader<>(proxy);
		loader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, ClientCommissionsDTO, PagingLoadResult<ClientCommissionsDTO>>(gridStore));

		storeSortInfo = new Store.StoreSortInfo<>(new ValueProvider<ClientCommissionsDTO, Date>() {
			@Override
			public Date getValue(ClientCommissionsDTO dto) {
				return dto.getLastUpdateTime();
			}

			@Override
			public void setValue(ClientCommissionsDTO o, Date o2) { }

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

		grid = new ZGrid<>(gridStore, getColumns(), new ZGridView<ClientCommissionsDTO>());
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


	private ColumnModel<ClientCommissionsDTO> getColumns() {
		List<ColumnConfig<ClientCommissionsDTO, ?>> columns = new ArrayList<>();

		columns.add(new ZColumnConfig.Builder<ClientCommissionsDTO, String>()
			.width(250)
//			.fixed()
			.valueProvider(new ZStringProvider<ClientCommissionsDTO>() {
				@Override
				public String getProperty(ClientCommissionsDTO dto) {
					return dto.isAllServices() ? Mes.get("allServices") : Mes.get("services") + ":" + dto.getServicesIds().size();
				}
			})
			.header(Mes.get("services"))
			.build());

		columns.add(new ZColumnConfig.Builder<ClientCommissionsDTO, String>()
			.width(250)
//			.fixed()
			.header(Mes.get("channels"))
			.valueProvider(new ZStringProvider<ClientCommissionsDTO>() {
				@Override
				public String getProperty(ClientCommissionsDTO dto) {
					return dto.isAllChannels() ? Mes.get("allChannels") : Mes.get("channels") + ":" + dto.getChannelsIds().size();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ClientCommissionsDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("minCommission"))
			.valueProvider(new ZStringProvider<ClientCommissionsDTO>() {
				@Override
				public String getProperty(ClientCommissionsDTO dto) {
					String pSign = dto.getRateType() == CommissionRateTypeDTO.PERCENT ? "%" : "";
					return dto.getMinCommission() == null ? Mes.get("notDefined") : (dto.getMinCommission() + pSign);
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ClientCommissionsDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("maxCommission"))
			.valueProvider(new ZStringProvider<ClientCommissionsDTO>() {
				@Override
				public String getProperty(ClientCommissionsDTO dto) {
					String pSign = dto.getRateType() == CommissionRateTypeDTO.PERCENT ? "%" : "";
					return dto.getMaxCommission() == null ? Mes.get("notDefined") : (dto.getMaxCommission() + pSign);
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ClientCommissionsDTO, String>()
			.width(100)
			.fixed()
			.header(Mes.get("commission"))
			.valueProvider(new ZStringProvider<ClientCommissionsDTO>() {
				@Override
				public String getProperty(ClientCommissionsDTO dto) {
					String pSign = dto.getRateType() == CommissionRateTypeDTO.PERCENT ? "%" : "";
					return dto.getCommission() + pSign;
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ClientCommissionsDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("createTime"))
			.valueProvider(new ZStringProvider<ClientCommissionsDTO>() {
				@Override
				public String getProperty(ClientCommissionsDTO dto) {
					return FormatDate.formatDateTime(dto.getCreateTime());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ClientCommissionsDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("lastUpdateTime"))
			.valueProvider(new ZStringProvider<ClientCommissionsDTO>() {
				@Override
				public String getProperty(ClientCommissionsDTO dto) {
					return FormatDate.formatDateTime(dto.getLastUpdateTime());
				}
			})
			.build());

//		if (canEdit()) {
		columns.add(new ZColumnConfig.Builder<ClientCommissionsDTO, String>()
			.width(32)
			.fixed()
			.cell(new ZIconButtonCell.Builder<ClientCommissionsDTO, String>()
				.gridStore(gridStore)
				.tooltip(Mes.get("edit"))
				.icon(FAIconsProvider.getIcons().pencil())
				.clickHandler(new GridClickHandler<ClientCommissionsDTO>() {
					@Override
					public void onClick(Cell.Context context, final ClientCommissionsDTO dto) {
						new ClientCommissionsWindow(dto, serviceEntityDTOs, channelDTOs, ActionMode.EDIT) {
							@Override
							public void onSave(ClientCommissionsDTO dto) {
								gridStore.update(dto);
								gridStore.applySort(false);
							}
						}.showInCenter();
					}
				})
				.build())
			.build());

//		}

		columns.add(new ZColumnConfig.Builder<ClientCommissionsDTO, String>()
			.width(32)
			.fixed()
			.cell(new ZIconButtonCell.Builder<ClientCommissionsDTO, String>()
				.gridStore(gridStore)
				.icon(FAIconsProvider.getIcons().trash())
				.tooltip(Mes.get("delete"))
				.clickHandler(new GridClickHandler<ClientCommissionsDTO>() {
					@Override
					public void onClick(Cell.Context context, final ClientCommissionsDTO dto) {
						new ZConfirmDialog(Mes.get("confirm"), Mes.get("objectDeleteConfirmation")) {
							@Override
							public void onConfirm() {
								ServicesFactory.getClientCommissionsService().deleteClientCommissions(dto.getId(), new ServiceCallback<Void>() {
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
		channel.setValue(null);
		loader.load();
	}
}
