package com.azry.sps.console.client.tabs.usergroup;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.EnterKeyBinder;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZDynamicComboBox;
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
import com.azry.sps.console.shared.dto.usergroup.PermissionsDTO;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDTO;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserGroupTab extends Composite {

	private static final String OPACITY_STYLE = "opacity: 0.3";

	private final VerticalLayoutContainer verticalLayoutContainer;

	private ZGrid<UserGroupDTO> grid;

	private ZToolBar toolBar;

	private ZTextField nameField;
	private ZDynamicComboBox<PermissionsDTO> permissionCombo;
	private ZSimpleComboBox<Boolean> activeCombo;

	private final List<PermissionsDTO> permissions;

	private final ListStore<UserGroupDTO> gridStore = new ListStore<>(new ModelKeyProvider<UserGroupDTO>() {
		@Override
		public String getKey(UserGroupDTO groupDTO) {
			return String.valueOf(groupDTO.getId());
		}
	});

	private final List<Boolean> booleans = new ArrayList<>();

	private Store.StoreSortInfo<UserGroupDTO> storeSortInfo;

	private ListLoader<ListLoadConfig, ListLoadResult<UserGroupDTO>> loader;

	private boolean isManage;

	public UserGroupTab(boolean isManage) {
		this.isManage = isManage;
		booleans.add(true);
		booleans.add(false);
		permissions = Arrays.asList(PermissionsDTO.values());
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

		permissionCombo = new ZDynamicComboBox.Builder<PermissionsDTO>()
			.emptyText(Mes.get("allPermissions"))
			.listWidth(400)
			.minChars(1)
			.width(300)
			.keyProvider(new ModelKeyProvider<PermissionsDTO>() {
				@Override
				public String getKey(PermissionsDTO permission) {
					return permission.name();
				}
			})
			.labelProvider(new LabelProvider<PermissionsDTO>() {
				@Override
				public String getLabel(PermissionsDTO permission) {
					return Mes.get(permission.name());
				}
			})
			.dataCallback(new ZDynamicComboBox.Builder.DataCallBack<PermissionsDTO>() {
				@Override
				public void getData(String query, int i, AsyncCallback<List<PermissionsDTO>> asyncCallback) {
					List<PermissionsDTO> perms = new ArrayList<>();
					String q = query != null ? query.trim() : "";
					for (PermissionsDTO perm : permissions) {
						if (Mes.get(perm.name()).contains(q)) {
							perms.add(perm);
						}
					}
					asyncCallback.onSuccess(perms);
				}
			})
			.build();

		permissionCombo.setTriggerAction(ComboBoxCell.TriggerAction.ALL);

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

		//			.visible(isManage)
		ZButton addButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().plus())
			.text(Mes.get("add"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.visible(isManage)
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					new UserGroupWindow(null, ActionMode.ADD) {
						@Override
						public void onSave(UserGroupDTO dto) {
							gridStore.add(dto);
						}
					}.showInCenter();
				}
			})
			.build();

		new EnterKeyBinder.Builder(searchButton)
			.add(nameField)
			.add(permissionCombo)
			.add(activeCombo)
			.bind();


		toolBar = new ZToolBar(1, -1);
		toolBar.setEnableOverflow(false);

		toolBar.add(nameField);
		toolBar.add(permissionCombo);
		toolBar.add(activeCombo);
		toolBar.add(searchButton);
		toolBar.add(clearFiltersButton);
		toolBar.add(addButton);
	}

	private void initGrid() {

		RpcProxy<ListLoadConfig, ListLoadResult<UserGroupDTO>> proxy = new RpcProxy<ListLoadConfig, ListLoadResult<UserGroupDTO>>() {
			@Override
			public void load(ListLoadConfig loadConfig, final AsyncCallback<ListLoadResult<UserGroupDTO>> callback) {
				ServicesFactory.getUserGroupService().getFilteredUserGroups(nameField.getValue(), permissionCombo.getCurrentValue(), activeCombo.getCurrentValue(),
					new ServiceCallback<List<UserGroupDTO>>(UserGroupTab.this) {
					@Override
					public void onServiceSuccess(List<UserGroupDTO> result) {
						callback.onSuccess(new ListLoadResultBean<>(result));
					}
				});
			}
		};

		loader = new ListLoader<>(proxy);
		loader.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, UserGroupDTO, ListLoadResult<UserGroupDTO>>(gridStore));

		storeSortInfo = new Store.StoreSortInfo<>(new ValueProvider<UserGroupDTO, Date>() {
			@Override
			public Date getValue(UserGroupDTO groupDTO) {
				return groupDTO.getLastUpdateTime();
			}

			@Override
			public void setValue(UserGroupDTO o, Date o2) { }

			@Override
			public String getPath() {
				return null;
			}
		}, SortDir.DESC);

		gridStore.addSortInfo(storeSortInfo);

		grid = new ZGrid<>(gridStore, getColumns(), new ZGridView<UserGroupDTO>());
		grid.getView().setColumnLines(true);
		grid.getView().setAutoFill(true);
		grid.getView().setForceFit(true);
		grid.getView().setStripeRows(true);
		grid.getView().getHeader().setDisableSortIcon(true);

		grid.setLoader(loader);
		new QuickTip(grid);
		loader.load();
	}


	private ColumnModel<UserGroupDTO> getColumns() {
		List<ColumnConfig<UserGroupDTO, ?>> columns = new ArrayList<>();

		columns.add(new ZColumnConfig.Builder<UserGroupDTO, String>()
			.width(120)
			.fixed()
			.valueProvider(new ZStringProvider<UserGroupDTO>() {
				@Override
				public String getProperty(UserGroupDTO groupDTO) {
					return String.valueOf(groupDTO.getId());
				}
			})
			.header(Mes.get("id"))
			.build());

		columns.add(new ZColumnConfig.Builder<UserGroupDTO, String>()
			.width(200)
			.fixed()
			.header(Mes.get("name"))
			.valueProvider(new ZStringProvider<UserGroupDTO>() {
				@Override
				public String getProperty(UserGroupDTO groupDTO) {
					return groupDTO.getName();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<UserGroupDTO, String>()
			.header(Mes.get("userPermissions"))
			.valueProvider(new ZStringProvider<UserGroupDTO>() {
				@Override
				public String getProperty(UserGroupDTO groupDTO) {
					return groupDTO.toString();
				}
			})
			.build());


		columns.add(new ZColumnConfig.Builder<UserGroupDTO, String>()
			.width(150)
			.fixed()
			.header(Mes.get("createTime"))
			.valueProvider(new ZStringProvider<UserGroupDTO>() {
				@Override
				public String getProperty(UserGroupDTO groupDTO) {
					return FormatDate.formatDateTime(groupDTO.getCreateTime());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<UserGroupDTO, String>()
			.width(150)
			.fixed()
			.header(Mes.get("lastUpdateTime"))
			.valueProvider(new ZStringProvider<UserGroupDTO>() {
				@Override
				public String getProperty(UserGroupDTO groupDTO) {
					return FormatDate.formatDateTime(groupDTO.getLastUpdateTime());
				}
			})
			.build());


		columns.add(new ZColumnConfig.Builder<UserGroupDTO, Boolean>()
			.cell(new ZIconButtonCell.Builder<UserGroupDTO, Boolean>()
				.gridStore(gridStore)
				.dynamicIcon(new IconSelector<UserGroupDTO>() {
					@Override
					public ImageResource selectIcon(Cell.Context context, UserGroupDTO value) {
						if (value.isActive()) {
							return FAIconsProvider.getIcons().green();
						}
						return FAIconsProvider.getIcons().red();
					}
				})
				.dynamicTooltip(new TooltipSelector<UserGroupDTO>() {
					@Override
					public String selectTooltip(Cell.Context context, UserGroupDTO dto) {
						if (dto.isActive()) {
							return Mes.get("deactivate");
						}
						return Mes.get("activate");
					}
				})
				.dynamicEnabling(new BooleanStateSelector<UserGroupDTO>() {
					@Override
					public boolean check(Cell.Context context, UserGroupDTO dto) {
						if (isManage) {
							return true;
						}
						return false;
					}
				})
				.dynamicStyle(new StringStateSelector<UserGroupDTO>() {
					@Override
					public String getStyle(Cell.Context context, UserGroupDTO dto) {
						if (!isManage) {
							return OPACITY_STYLE;
						}
						return "";
					}
				})
				.clickHandler(new GridClickHandler<UserGroupDTO>() {
					@Override
					public void onClick(Cell.Context context, final UserGroupDTO dto) {
						new ZConfirmDialog(Mes.get("confirm"), dto.isActive() ?
							Mes.get("deactivateConfigurableConfirmation") : Mes.get("activateConfigurableConfirmation")) {
							@Override
							public void onConfirm() {
								dto.setActive(!dto.isActive());
								ServicesFactory.getUserGroupService().updateUserGroup(dto, new ServiceCallback<UserGroupDTO>(this) {
									@Override
									public void onServiceSuccess(UserGroupDTO result) {
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
			columns.add(new ZColumnConfig.Builder<UserGroupDTO, String>()
				.width(32)
				.fixed()
				.cell(new ZIconButtonCell.Builder<UserGroupDTO, String>()
					.gridStore(gridStore)
					.tooltip(Mes.get("edit"))
					.icon(FAIconsProvider.getIcons().pencil())
					.clickHandler(new GridClickHandler<UserGroupDTO>() {
						@Override
						public void onClick(Cell.Context context, final UserGroupDTO dto) {
							new UserGroupWindow(dto, ActionMode.EDIT) {
								@Override
								public void onSave(UserGroupDTO dto) {
									gridStore.update(dto);
									gridStore.applySort(false);
								}
							}.showInCenter();
						}
					})
					.build())
				.build());

			columns.add(new ZColumnConfig.Builder<UserGroupDTO, String>()
				.width(32)
				.fixed()
				.cell(new ZIconButtonCell.Builder<UserGroupDTO, String>()
					.gridStore(gridStore)
					.icon(FAIconsProvider.getIcons().trash())
					.tooltip(Mes.get("delete"))
					.clickHandler(new GridClickHandler<UserGroupDTO>() {
						@Override
						public void onClick(Cell.Context context, final UserGroupDTO dto) {
							new ZConfirmDialog(Mes.get("confirm"), Mes.get("objectDeleteConfirmation")) {
								@Override
								public void onConfirm() {
									ServicesFactory.getUserGroupService().deleteUserGroup(dto.getId(), new ServiceCallback<Void>(this) {
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
			columns.add(new ZColumnConfig.Builder<UserGroupDTO, String>()
				.width(32)
				.fixed()
				.cell(new ZIconButtonCell.Builder<UserGroupDTO, String>()
					.gridStore(gridStore)
					.tooltip(Mes.get("view"))
					.icon(FAIconsProvider.getIcons().eye())
					.clickHandler(new GridClickHandler<UserGroupDTO>() {
						@Override
						public void onClick(Cell.Context context, final UserGroupDTO dto) {
							new UserGroupWindow(dto, ActionMode.VIEW) {
								@Override
								public void onSave(UserGroupDTO dto) {
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
		permissionCombo.setValue(null);
		activeCombo.setValue(null);
	}
}
