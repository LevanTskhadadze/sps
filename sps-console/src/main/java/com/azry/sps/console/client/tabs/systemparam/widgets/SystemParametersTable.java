package com.azry.sps.console.client.tabs.systemparam.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZIconButtonCell;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.gxt.client.zcomp.helper.GridClickHandler;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDTO;
import com.google.gwt.cell.client.Cell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

public class SystemParametersTable {


	private static final ListStore<SystemParameterDTO> store = new ListStore<>(new ModelKeyProvider<SystemParameterDTO>() {
		@Override
		public String getKey(SystemParameterDTO systemParameterDTO) {
			return String.valueOf(systemParameterDTO.getId());
		}
	});


	public static ListStore<SystemParameterDTO> setListStore(List<SystemParameterDTO> result) {
		store.clear();
		store.addAll(result);

		return store;
	}

	public static ListStore<SystemParameterDTO> getListStore() {
		return store;
	}


	public static ColumnModel<SystemParameterDTO> getMyColumnModel(boolean isManage) {

		List<ColumnConfig<SystemParameterDTO, ?>> columns = new ArrayList<>();
		columns.add(new ZColumnConfig.Builder<SystemParameterDTO, String>()
				.header("id")
				.width(20)
				.valueProvider(new ZStringProvider<SystemParameterDTO>() {
					@Override
					public String getProperty(SystemParameterDTO systemParameterDTO) {return String.valueOf(systemParameterDTO.getId());
					}
				})
				.build());

		columns.add(new ZColumnConfig.Builder<SystemParameterDTO, String>()
			.header(Mes.get("code"))
			.width(100)
			.valueProvider(new ZStringProvider<SystemParameterDTO>() {
				@Override
				public String getProperty(SystemParameterDTO systemParameterDTO) {
					return String.valueOf(systemParameterDTO.getCode());
				}
			})
			.build());


		columns.add(new ZColumnConfig.Builder<SystemParameterDTO, String>()
				.header(Mes.get("value"))
				.width(100)
				.valueProvider(new ZStringProvider<SystemParameterDTO>() {
					@Override
					public String getProperty(SystemParameterDTO systemParameterDTO) {
						return String.valueOf(systemParameterDTO.getValue());
					}
				})
				.build());

		columns.add(new ZColumnConfig.Builder<SystemParameterDTO, String>()
				.header(Mes.get("description"))
				.width(100)
				.valueProvider(new ZStringProvider<SystemParameterDTO>() {
					@Override
					public String getProperty(SystemParameterDTO systemParameterDTO) {
						if (systemParameterDTO.getDescription() == null) {
							return "";
						}
						return String.valueOf(systemParameterDTO.getDescription());
					}
				})
				.build());


		if (isManage) {
			columns.add(new ZColumnConfig.Builder<SystemParameterDTO, String>()
				.width(32)
				.cell(new ZIconButtonCell.Builder<SystemParameterDTO, String>()
					.gridStore(store)
					.tooltip("edit")
					.icon(FAIconsProvider.getIcons().pencil())
					.clickHandler(new GridClickHandler<SystemParameterDTO>() {
						@Override
						public void onClick(Cell.Context context, final SystemParameterDTO systemParameterDTO) {
							new SystemParametersModifyWindow(systemParameterDTO, store, ActionMode.EDIT);
						}
					})
					.build()
				)
				.fixed()
				.build());


			columns.add(new ZColumnConfig.Builder<SystemParameterDTO, String>()
				.width(32)
				.cell(new ZIconButtonCell.Builder<SystemParameterDTO, String>()
					.gridStore(store)
					.tooltip(Mes.get("delete"))
					.icon(FAIconsProvider.getIcons().trash())
					.clickHandler(new GridClickHandler<SystemParameterDTO>() {
						@Override
						public void onClick(Cell.Context context, final SystemParameterDTO systemParameterDTO) {
							new ZConfirmDialog(Mes.get("confirm"), Mes.get("deleteConfirmMessage")) {
								@Override
								public void onConfirm() {
									ServicesFactory.getSystemParameterService().removeParameter(systemParameterDTO.getId(),
										new ServiceCallback<Void>(this) {

											@Override
											public void onServiceSuccess(Void unused) {
												store.remove(systemParameterDTO);
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
		} else {
			columns.add(new ZColumnConfig.Builder<SystemParameterDTO, String>()
				.width(32)
				.cell(new ZIconButtonCell.Builder<SystemParameterDTO, String>()
					.gridStore(store)
					.tooltip(Mes.get("view"))
					.icon(FAIconsProvider.getIcons().eye())
					.clickHandler(new GridClickHandler<SystemParameterDTO>() {
						@Override
						public void onClick(Cell.Context context, final SystemParameterDTO systemParameterDTO) {
							new SystemParametersModifyWindow(systemParameterDTO, store, ActionMode.VIEW);
						}
					})
					.build()
				)
				.fixed()
				.build());
		}

		return new ColumnModel<>(columns);
	}

}
