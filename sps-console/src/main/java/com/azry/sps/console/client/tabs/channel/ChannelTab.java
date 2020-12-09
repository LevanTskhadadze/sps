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
import com.azry.gxt.client.zcomp.helper.StringStateSelector;
import com.azry.gxt.client.zcomp.helper.TooltipSelector;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.FormatDate;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.LabelProvider;
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

public class ChannelTab extends Composite {

	private static final String OPACITY_STYLE = "opacity: 0.3";

	private final VerticalLayoutContainer verticalLayoutContainer;

	private ZGrid<ChannelDTO> grid;
	private ZToolBar toolBar;

	private ZTextField nameField;
	private ZSimpleComboBox<Boolean> activeCombo;

	private final ListStore<ChannelDTO> gridStore = new ListStore<>(new ModelKeyProvider<ChannelDTO>() {
		@Override
		public String getKey(ChannelDTO dto) {
			return String.valueOf(dto.getId());
		}
	});

	private ListLoader<ListLoadConfig, ListLoadResult<ChannelDTO>> loader;

	private List<Boolean> booleans = new ArrayList<>();

	private boolean isManage;

	public ChannelTab(boolean isManage) {
		this.isManage = isManage;
		booleans.add(true);
		booleans.add(false);
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
		nameField = new ZTextField.Builder()
			.width(200)
			.emptyText(Mes.get("name"))
			.build();


		activeCombo = new ZSimpleComboBox.Builder<Boolean>()
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
					new ChannelWindow(null, ActionMode.ADD) {
						@Override
						public void onSave(ChannelDTO dto) {
							gridStore.add(dto);
						}
					}.showInCenter();
				}
			})
			.build();

		new EnterKeyBinder.Builder(searchButton)
			.add(nameField)
			.add(activeCombo)
			.bind();


		toolBar = new ZToolBar(1, -1);
		toolBar.setEnableOverflow(false);

		toolBar.add(nameField);
		toolBar.add(activeCombo);
		toolBar.add(searchButton);
		toolBar.add(clearFiltersButton);
		toolBar.add(addButton);
	}

	private void initGrid() {

		RpcProxy<ListLoadConfig, ListLoadResult<ChannelDTO>> proxy = new RpcProxy<ListLoadConfig, ListLoadResult<ChannelDTO>>() {
			@Override
			public void load(ListLoadConfig loadConfig, final AsyncCallback<ListLoadResult<ChannelDTO>> callback) {
				ServicesFactory.getChannelService().getFilteredChannels(nameField.getCurrentValue(), activeCombo.getCurrentValue(),
					new ServiceCallback<List<ChannelDTO>>(ChannelTab.this) {
					@Override
					public void onServiceSuccess(List<ChannelDTO> result) {
						callback.onSuccess(new ListLoadResultBean<>(result));
					}
				});
			}
		};

		loader = new ListLoader<>(proxy);
		loader.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, ChannelDTO, ListLoadResult<ChannelDTO>>(gridStore));

		grid = new ZGrid<>(gridStore, getColumns(), new ZGridView<ChannelDTO>());
		grid.getView().setColumnLines(true);
		grid.getView().setAutoFill(true);
		grid.getView().setForceFit(true);
		grid.getView().setStripeRows(true);

		grid.setLoader(loader);
		new QuickTip(grid);
		loader.load();
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
			.width(400)
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
				.dynamicEnabling(new BooleanStateSelector<ChannelDTO>() {
						@Override
						public boolean check(Cell.Context context, ChannelDTO dto) {
							if (isManage) {
								return true;
							}
							return false;
						}
					})
				.dynamicStyle(new StringStateSelector<ChannelDTO>() {
					@Override
					public String getStyle(Cell.Context context, ChannelDTO channelDTO) {
						if (!isManage) {
							return OPACITY_STYLE;
						}
						return "";
					}
				})
				.clickHandler(new GridClickHandler<ChannelDTO>() {
					@Override
					public void onClick(Cell.Context context, final ChannelDTO dto) {
						new ZConfirmDialog(Mes.get("confirm"),
							dto.isActive() ? Mes.get("deactivateConfigurableConfirmation") : Mes.get("activateConfigurableConfirmation")) {
							@Override
							public void onConfirm() {
								dto.setActive(!dto.isActive());
								ServicesFactory.getChannelService().updateChannel(dto, new ServiceCallback<ChannelDTO>(this) {
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

		if (isManage) {
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
								}
							}.showInCenter();
						}
					})
					.build())
				.build());

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
									ServicesFactory.getChannelService().deleteChannel(dto.getId(), new ServiceCallback<Void>(this) {
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
			columns.add(new ZColumnConfig.Builder<ChannelDTO, String>()
				.width(32)
				.fixed()
				.cell(new ZIconButtonCell.Builder<ChannelDTO, String>()
					.gridStore(gridStore)
					.tooltip(Mes.get("view"))
					.icon(FAIconsProvider.getIcons().eye())
					.clickHandler(new GridClickHandler<ChannelDTO>() {
						@Override
						public void onClick(Cell.Context context, final ChannelDTO dto) {
							new ChannelWindow(dto, ActionMode.VIEW) {
								@Override
								public void onSave(ChannelDTO dto) {
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
		nameField.setValue(null);
		activeCombo.setValue(null);
	}
}
