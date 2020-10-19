package com.azry.sps.console.client.tabs.SystemParameter.table;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZIconButtonCell;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.gxt.client.zcomp.helper.GridClickHandler;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.google.gwt.cell.client.Cell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

public class SystemParametersTable {


	private static final ListStore<SystemParameterDto> store = new ListStore<>(new ModelKeyProvider<SystemParameterDto>() {
		@Override
		public String getKey(SystemParameterDto systemParameterDto) {
			return String.valueOf(systemParameterDto.getId());
		}
	});


	public static ListStore<SystemParameterDto> setListStore(List<SystemParameterDto> result) {
		store.clear();
		store.addAll(result);

		return store;
	}

	public static ListStore<SystemParameterDto> getListStore() {
		return store;
	}


	public static ColumnModel<SystemParameterDto> getMyColumnModel() {

		List<ColumnConfig<SystemParameterDto, ?>> columns = new ArrayList<>();
		columns.add(new ZColumnConfig.Builder<SystemParameterDto, String>()
				.header("id")
				.width(20)
				.valueProvider(new ZStringProvider<SystemParameterDto>() {
					@Override
					public String getProperty(SystemParameterDto systemParameterDto) {return String.valueOf(systemParameterDto.getId());
					}
				})
				.build());
		columns.add(new ZColumnConfig.Builder<SystemParameterDto, String>()
			.header(Mes.get("code"))
			.width(100)
			.valueProvider(new ZStringProvider<SystemParameterDto>() {
				@Override
				public String getProperty(SystemParameterDto systemParameterDto) {
					return String.valueOf(systemParameterDto.getCode());
				}
			})
			.build());


		columns.add(new ZColumnConfig.Builder<SystemParameterDto, String>()
				.header(Mes.get("value"))
				.width(100)
				.valueProvider(new ZStringProvider<SystemParameterDto>() {
					@Override
					public String getProperty(SystemParameterDto systemParameterDto) {
						return String.valueOf(systemParameterDto.getValue());
					}
				})
				.build());

		columns.add(new ZColumnConfig.Builder<SystemParameterDto, String>()
				.header(Mes.get("description"))
				.width(100)
				.valueProvider(new ZStringProvider<SystemParameterDto>() {
					@Override
					public String getProperty(SystemParameterDto systemParameterDto) {
						return String.valueOf(systemParameterDto.getDescription());
					}
				})
				.build());


		columns.add(new ZColumnConfig.Builder<SystemParameterDto, String>()
				.header("")
				.width(32)
				.cell(new ZIconButtonCell.Builder<SystemParameterDto, String>()
						.gridStore(store)
						.icon(FAIconsProvider.getIcons().pencil())
						.clickHandler(new GridClickHandler<SystemParameterDto>() {
							@Override
							public void onClick(Cell.Context context, final SystemParameterDto systemParameterDto) {
									new SystemParametersModifyWindow(systemParameterDto, store);

							}
						})
						.build()

				)
				.fixed()
				.build());


		columns.add(new ZColumnConfig.Builder<SystemParameterDto, String>()
			.header("")
			.width(32)
			.cell(new ZIconButtonCell.Builder<SystemParameterDto, String>()
				.gridStore(store)
				.icon(FAIconsProvider.getIcons().trash())
				.clickHandler(new GridClickHandler<SystemParameterDto>() {
					@Override
					public void onClick(Cell.Context context, final SystemParameterDto systemParameterDto) {

						new ZConfirmDialog(Mes.get("confirm"), Mes.get("deleteConfirmMessage")) {
							@Override
							public void onConfirm() {
								ServicesFactory.getSystemParameterService().removeParameter(systemParameterDto.getId(), new ServiceCallback<Void>() {

									@Override
									public void onServiceSuccess(Void unused) {
										store.remove(systemParameterDto);
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
