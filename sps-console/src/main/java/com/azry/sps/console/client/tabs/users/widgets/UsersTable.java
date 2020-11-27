package com.azry.sps.console.client.tabs.users.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZIconButtonCell;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.gxt.client.zcomp.helper.GridClickHandler;
import com.azry.gxt.client.zcomp.helper.IconSelector;
import com.azry.gxt.client.zcomp.helper.TooltipSelector;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDTO;
import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

public class UsersTable {


	private static final ListStore<SystemUserDTO> store = new ListStore<>(new ModelKeyProvider<SystemUserDTO>() {
		@Override
		public String getKey(SystemUserDTO systemUserDTO) {
			return String.valueOf(systemUserDTO.getId());
		}
	});

	static Store.StoreSortInfo<SystemUserDTO> storeSortInfo = new Store.StoreSortInfo<>(new ValueProvider<SystemUserDTO, String>() {
		@Override
		public String getValue(SystemUserDTO dto) {
			return dto.getName().toLowerCase();
		}

		@Override
		public void setValue(SystemUserDTO o, String o2) { }

		@Override
		public String getPath() {
			return null;
		}
	}, SortDir.ASC);

	public static ListStore<SystemUserDTO> getListStore() {
		store.addSortInfo(storeSortInfo);
		return store;
	}


	public static ColumnModel<SystemUserDTO> getMyColumnModel() {

		List<ColumnConfig<SystemUserDTO, ?>> columns = new ArrayList<>();
		columns.add(new ZColumnConfig.Builder<SystemUserDTO, String>()
				.header("id")
				.width(20)
				.valueProvider(new ZStringProvider<SystemUserDTO>() {
					@Override
					public String getProperty(SystemUserDTO systemUserDTO) {return String.valueOf(systemUserDTO.getId());
					}
				})
				.build());
		columns.add(new ZColumnConfig.Builder<SystemUserDTO, String>()
			.header(Mes.get("username"))
			.width(100)
			.valueProvider(new ZStringProvider<SystemUserDTO>() {
				@Override
				public String getProperty(SystemUserDTO systemUserDTO) {
					return systemUserDTO.getUsername();
				}
			})
			.build());


		columns.add(new ZColumnConfig.Builder<SystemUserDTO, String>()
				.header(Mes.get("name"))
				.width(100)
				.valueProvider(new ZStringProvider<SystemUserDTO>() {
					@Override
					public String getProperty(SystemUserDTO systemUserDTO) {
						return systemUserDTO.getName();
					}
				})
				.build());

		columns.add(new ZColumnConfig.Builder<SystemUserDTO, String>()
				.header(Mes.get("email"))
				.width(100)
				.valueProvider(new ZStringProvider<SystemUserDTO>() {
					@Override
					public String getProperty(SystemUserDTO systemUserDTO) {
						String res = systemUserDTO.getEmail();
						if(res == null) res = "";
						return res;
					}
				})
				.build());

		columns.add(new ZColumnConfig.Builder<SystemUserDTO, String>()
				.header(Mes.get("userGroups"))
				.width(100)
				.valueProvider(new ZStringProvider<SystemUserDTO>() {
					@Override
					public String getProperty(SystemUserDTO systemUserDTO) {
						StringBuilder prop = new StringBuilder();
						String comma = "";
						
						for(UserGroupDTO dto : systemUserDTO.getGroups()) {
							prop.append(comma).append(dto.getName());
							comma = ", ";
						}
						return prop.toString();
					}
				})
				.build());


		columns.add(new ZColumnConfig.Builder<SystemUserDTO, String>()
				.header(Mes.get("createTime"))
				.width(100)
				.valueProvider(new ZStringProvider<SystemUserDTO>() {
					@Override
					public String getProperty(SystemUserDTO systemUserDTO) {
						return systemUserDTO.getFormattedCreateTime();
					}
				})
				.build());

		columns.add(new ZColumnConfig.Builder<SystemUserDTO, String>()
				.header(Mes.get("lastUpdateTime"))
				.width(100)
				.valueProvider(new ZStringProvider<SystemUserDTO>() {
					@Override
					public String getProperty(SystemUserDTO systemUserDTO) {
						return systemUserDTO.getFormattedLastUpdateTime();
					}
				})
				.build());


		columns.add(new ZColumnConfig.Builder<SystemUserDTO, String>()
				.header("")
				.width(32)
				.cell(new ZIconButtonCell.Builder<SystemUserDTO, String>()
						.gridStore(store)
						.dynamicIcon(new IconSelector<SystemUserDTO>() {
							@Override
							public ImageResource selectIcon(Cell.Context context, SystemUserDTO systemUserDTO) {
								if(systemUserDTO.isActive()) return FAIconsProvider.getIcons().green();
								return FAIconsProvider.getIcons().red();
							}
						})
						.dynamicTooltip(new TooltipSelector<SystemUserDTO>() {
							@Override
							public String selectTooltip(Cell.Context context, SystemUserDTO systemUserDTO) {
								if(systemUserDTO.isActive()) return Mes.get("deactivation");
								return Mes.get("activation");
							}
						})
						.clickHandler(new GridClickHandler<SystemUserDTO>() {
							@Override
							public void onClick(Cell.Context context, final SystemUserDTO dto) {
								new ZConfirmDialog(Mes.get("confirm"), dto.isActive() ? Mes.get("deactivateConfigurableConfirmation") : Mes.get("activateConfigurableConfirmation")) {
									@Override
									public void onConfirm() {
										dto.setActive(!dto.isActive());
										ServicesFactory.getUserTabService().changeActivation(dto.getId(), dto.getVersion(),
											new ServiceCallback<Void>(this) {
											@Override
											public void onServiceSuccess(Void ignored) {
												dto.setVersion(dto.getVersion() + 1);
												store.update(dto);
											}
										});
									}
								}.show();
							}
						})
						.build()

				)
				.fixed()
				.build());

		columns.add(new ZColumnConfig.Builder<SystemUserDTO, String>()
				.header("")
				.width(32)
				.cell(new ZIconButtonCell.Builder<SystemUserDTO, String>()
						.gridStore(store)
						.icon(FAIconsProvider.getIcons().pencil())
						.clickHandler(new GridClickHandler<SystemUserDTO>() {
							@Override
							public void onClick(Cell.Context context, final SystemUserDTO SystemUserDTO) {
									new UsersModifyWindow(SystemUserDTO, store);
							}
						})
						.build()

				)
				.fixed()
				.build());


		columns.add(new ZColumnConfig.Builder<SystemUserDTO, String>()
			.header("")
			.width(32)
			.cell(new ZIconButtonCell.Builder<SystemUserDTO, String>()
				.gridStore(store)
				.icon(FAIconsProvider.getIcons().trash())
				.clickHandler(new GridClickHandler<SystemUserDTO>() {
					@Override
					public void onClick(Cell.Context context, final SystemUserDTO SystemUserDTO) {

						new ZConfirmDialog(Mes.get("confirm"), Mes.get("deleteConfirmMessage")) {
							@Override
							public void onConfirm() {
								ServicesFactory.getUserTabService().removeParameter(SystemUserDTO.getId(), new ServiceCallback<Void>(this) {

									@Override
									public void onServiceSuccess(Void unused) {
										store.remove(SystemUserDTO);
									}
								});
							}
						}.show();
					}
				})
				.build()
			)
			.fixed()
			.build());

		return new ColumnModel<>(columns);
	}

}
