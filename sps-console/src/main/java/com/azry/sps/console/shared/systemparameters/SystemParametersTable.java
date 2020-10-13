package com.azry.sps.console.shared.systemparameters;

import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.sps.console.client.utils.Mes;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

public class SystemParametersTable {





	public static ListStore<SystemParameterDto> getListStore(List<SystemParameterDto> result){

		ListStore<SystemParameterDto> store = new ListStore<>(new ModelKeyProvider<SystemParameterDto>() {
			@Override
			public String getKey(SystemParameterDto systemParameterDto) {
				return String.valueOf(systemParameterDto.getId());
			}
		});
		store.addAll(result);

		return store;
	}

	public static ColumnModel<SystemParameterDto> getMyColumnModel(){

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
				.header(Mes.get("value"))
				.width(100)
				.valueProvider(new ZStringProvider<SystemParameterDto>() {
					@Override
					public String getProperty(SystemParameterDto systemParameterDto) {return String.valueOf(systemParameterDto.getValue());
					}
				})
				.build());

		columns.add(new ZColumnConfig.Builder<SystemParameterDto, String>()
				.header(Mes.get("code"))
				.width(100)
				.valueProvider(new ZStringProvider<SystemParameterDto>() {
					@Override
					public String getProperty(SystemParameterDto systemParameterDto) {return String.valueOf(systemParameterDto.getCode());
					}
				})
				.build());


		return new ColumnModel<>(columns);
	}

}
