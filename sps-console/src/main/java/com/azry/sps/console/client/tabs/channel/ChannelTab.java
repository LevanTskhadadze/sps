package com.azry.sps.console.client.tabs.channel;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.EnterKeyBinder;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZGridView;
import com.azry.gxt.client.zcomp.ZIconButtonCell;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.gxt.client.zcomp.helper.BooleanStateSelector;
import com.azry.gxt.client.zcomp.helper.GridClickHandler;
import com.azry.gxt.client.zcomp.helper.IconSelector;
import com.azry.gxt.client.zcomp.helper.TooltipSelector;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.FormatDate;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.LabelProvider;
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
import java.util.Date;
import java.util.List;

public class ChannelTab extends Composite {

	private VerticalLayoutContainer verticalLayoutContainer;

	private ZGrid<ChannelDTO> grid;

	private ZToolBar toolBar;

	private ZTextField name;

	private ZSimpleComboBox<Boolean> active;

	private ZButton searchButton;

	private ZButton clearFiltersButton;

	private ZButton addButton;

	private final ListStore<ChannelDTO> gridStore = new ListStore<>(new ModelKeyProvider<ChannelDTO>() {
		@Override
		public String getKey(ChannelDTO dto) {
			return String.valueOf(dto.getId());
		}
	});

	Store.StoreSortInfo<ChannelDTO> storeSortInfo;

	private ListLoader<ListLoadConfig, ListLoadResult<ChannelDTO>> loader;

	List<Boolean> booleans = new ArrayList<>();

	public ChannelTab() {
		booleans.add(true);
		booleans.add(false);
		verticalLayoutContainer = new VerticalLayoutContainer();
		initToolbar();
		initGrid();
		initWidget(verticalLayoutContainer);
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


		active = new ZSimpleComboBox.Builder<Boolean>()
			.keyProvider(new ModelKeyProvider<Boolean>() {
				@Override
				public String getKey(Boolean bool) {
					return String.valueOf(booleans.indexOf(bool));
				}
			})
			.labelProvider(new LabelProvider<Boolean>() {
				@Override
				public String getLabel(Boolean bool) {
					return Mes.get("active_" + bool.toString());
				}
			})
			.values(booleans)
			.noSelectionLabel(Mes.get("isActive"))
			.enableSorting(false)
			.editable(false)
			.width(200)
			.build();


		searchButton = new ZButton.Builder()
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
					new ChannelWindow(null, ActionMode.ADD){
						@Override
						public void onSave(ChannelDTO dto) {
							gridStore.add(dto);
						}
					}.showInCenter();
				}
			})
			.build();

		new EnterKeyBinder.Builder(searchButton)
			.add(name)
			.add(active)
			.bind();


		toolBar = new ZToolBar(1, -1);
		toolBar.setEnableOverflow(false);

		toolBar.add(name);
		toolBar.add(active);
		toolBar.add(searchButton);
		toolBar.add(clearFiltersButton);
		toolBar.add(addButton);
	}

	private void initGrid() {

		RpcProxy<ListLoadConfig, ListLoadResult<ChannelDTO>> proxy = new RpcProxy<ListLoadConfig, ListLoadResult<ChannelDTO>>() {
			@Override
			public void load(ListLoadConfig loadConfig, final AsyncCallback<ListLoadResult<ChannelDTO>> callback) {
				ServicesFactory.getChannelService().getFilteredChannels(name.getValue(), active.getCurrentValue(), new ServiceCallback<List<ChannelDTO>>() {
					@Override
					public void onServiceSuccess(List<ChannelDTO> result) {
						callback.onSuccess(new ListLoadResultBean<>(result));
					}
				});
			}
		};

		loader = new ListLoader<>(proxy);
		loader.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, ChannelDTO, ListLoadResult<ChannelDTO>>(gridStore));

		storeSortInfo = new Store.StoreSortInfo<>(new ValueProvider<ChannelDTO, Date>() {
			@Override
			public Date getValue(ChannelDTO dto) {
				return dto.getLastUpdateTime();
			}

			@Override
			public void setValue(ChannelDTO o, Date o2) { }

			@Override
			public String getPath() {
				return null;
			}
		}, SortDir.DESC);

		gridStore.addSortInfo(storeSortInfo);

		grid = new ZGrid<>(gridStore, getColumns(), new ZGridView<ChannelDTO>());
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


	private ColumnModel<ChannelDTO> getColumns() {
		List<ColumnConfig<ChannelDTO, ?>> columns = new ArrayList<>();

		columns.add(new ZColumnConfig.Builder<ChannelDTO, String>()
			.width(120)
			.fixed()
			.valueProvider(new ZStringProvider<ChannelDTO>() {
				@Override
				public String getProperty(ChannelDTO dto) {
					return String.valueOf(dto.getId());
				}
			})
			.header(Mes.get("id"))
			.build());

		columns.add(new ZColumnConfig.Builder<ChannelDTO, String>()
			.width(500)
			.fixed()
			.header(Mes.get("name"))
			.valueProvider(new ZStringProvider<ChannelDTO>() {
				@Override
				public String getProperty(ChannelDTO dto) {
					return dto.getName();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ChannelDTO, String>()
			.header(Mes.get("fiServiceUnavailabilityAction"))
			.valueProvider(new ZStringProvider<ChannelDTO>() {
				@Override
				public String getProperty(ChannelDTO dto) {
					return Mes.get(dto.getFiServiceUnavailabilityAction().name());
				}
			})
			.build());


		columns.add(new ZColumnConfig.Builder<ChannelDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("createTime"))
			.valueProvider(new ZStringProvider<ChannelDTO>() {
				@Override
				public String getProperty(ChannelDTO dto) {
					return FormatDate.formatDateTime(dto.getCreateTime());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ChannelDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("lastUpdateTime"))
			.valueProvider(new ZStringProvider<ChannelDTO>() {
				@Override
				public String getProperty(ChannelDTO dto) {
					return FormatDate.formatDateTime(dto.getLastUpdateTime());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ChannelDTO, Boolean>()
			.cell(new ZIconButtonCell.Builder<ChannelDTO, Boolean>()
				.gridStore(gridStore)
				.dynamicIcon(new IconSelector<ChannelDTO>() {
					@Override
					public ImageResource selectIcon(Cell.Context context, ChannelDTO value) {
						if (value.isActive()) {
							return FAIconsProvider.getIcons().green();
						}
						return FAIconsProvider.getIcons().red();
					}
				})
				.dynamicTooltip(new TooltipSelector<ChannelDTO>() {
					@Override
					public String selectTooltip(Cell.Context context, ChannelDTO dto) {
						if (dto.isActive()) {
							return Mes.get("deactivate");
						}
						return Mes.get("activate");
					}
				})
//				.dynamicStyle(new StringStateSelector<channelDTO>() {
//					@Override
//					public String getStyle(Cell.Context context, channelDTO dto) {
//						return OPACITY_STYLE;
//					}
//				})
				.dynamicEnabling(new BooleanStateSelector<ChannelDTO>() {
					@Override
					public boolean check(Cell.Context context, ChannelDTO dto) {
						return true;
					}
				})
				.clickHandler(new GridClickHandler<ChannelDTO>() {
					@Override
					public void onClick(Cell.Context context, final ChannelDTO dto) {
						new ZConfirmDialog(Mes.get("confirm"), dto.isActive() ? Mes.get("deactivateConfigurableConfirmation") : Mes.get("activateConfigurableConfirmation")) {
							@Override
							public void onConfirm() {
								dto.setActive(!dto.isActive());
								ServicesFactory.getChannelService().updateChannel(dto, new ServiceCallback<ChannelDTO>() {
									@Override
									public void onServiceSuccess(ChannelDTO result) {
										gridStore.update(result);
									}
								});
							}
						}.show();
					}
				})
				.build())
			.fixed()
			.width(32)
			.build());

//		if (canEdit()) {
		columns.add(new ZColumnConfig.Builder<ChannelDTO, String>()
			.width(32)
			.fixed()
			.cell(new ZIconButtonCell.Builder<ChannelDTO, String>()
				.gridStore(gridStore)
				.tooltip(Mes.get("edit"))
				.icon(FAIconsProvider.getIcons().pencil())
				.clickHandler(new GridClickHandler<ChannelDTO>() {
					@Override
					public void onClick(Cell.Context context, final ChannelDTO dto) {
						new ChannelWindow(dto, ActionMode.EDIT) {
							@Override
							public void onSave(ChannelDTO dto) {
								gridStore.update(dto);
								gridStore.applySort(false);
							}
						}.showInCenter();
					}
				})
				.build())
			.build());

//		}

		columns.add(new ZColumnConfig.Builder<ChannelDTO, String>()
			.width(32)
			.fixed()
			.cell(new ZIconButtonCell.Builder<ChannelDTO, String>()
				.gridStore(gridStore)
				.icon(FAIconsProvider.getIcons().trash())
				.tooltip(Mes.get("delete"))
				.clickHandler(new GridClickHandler<ChannelDTO>() {
					@Override
					public void onClick(Cell.Context context, final ChannelDTO dto) {
						new ZConfirmDialog(Mes.get("confirm"), Mes.get("objectDeleteConfirmation")) {
							@Override
							public void onConfirm() {
								ServicesFactory.getChannelService().deleteChannel(dto.getId(), new ServiceCallback<Void>() {
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
		active.setValue(null);
	}
}
