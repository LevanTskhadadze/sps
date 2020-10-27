package com.azry.sps.console.client.tabs.services.widgets;

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
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;


import java.util.ArrayList;
import java.util.List;

public class ServiceTable {


	private static final ListStore<ServiceDto> store = new ListStore<>(new ModelKeyProvider<ServiceDto>() {
		@Override
		public String getKey(ServiceDto serviceDto) {
			return String.valueOf(serviceDto.getId());
		}
	});


	public static ListStore<ServiceDto> setListStore(List<ServiceDto> result) {
		store.clear();
		store.addAll(result);

		return store;
	}

	public static ListStore<ServiceDto> getListStore() {
		return store;
	}


	public static ColumnModel<ServiceDto> getMyColumnModel() {

		List<ColumnConfig<ServiceDto, ?>> columns = new ArrayList<>();
		columns.add(new ZColumnConfig.Builder<ServiceDto, String>()
			.header("id")
			.width(20)
			.valueProvider(new ZStringProvider<ServiceDto>() {
				@Override
				public String getProperty(ServiceDto ServiceDto) {return String.valueOf(ServiceDto.getId());
				}
			})
			.build());
		columns.add(new ZColumnConfig.Builder<ServiceDto, String>()
			.header(Mes.get("name"))
			.width(100)
			.valueProvider(new ZStringProvider<ServiceDto>() {
				@Override
				public String getProperty(ServiceDto ServiceDto) {
					return ServiceDto.getName();
				}
			})
			.build());



		columns.add(new ZColumnConfig.Builder<ServiceDto, String>()
			.header(Mes.get("createTime"))
			.width(100)
			.valueProvider(new ZStringProvider<ServiceDto>() {
				@Override
				public String getProperty(ServiceDto ServiceDto) {
					return ServiceDto.getFormattedCreateTime();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceDto, String>()
			.header(Mes.get("lastUpdateTime"))
			.width(100)
			.valueProvider(new ZStringProvider<ServiceDto>() {
				@Override
				public String getProperty(ServiceDto ServiceDto) {
					return ServiceDto.getFormattedLastUpdateTime();
				}
			})
			.build());


		columns.add(new ZColumnConfig.Builder<ServiceDto, String>()
			.header("")
			.width(32)
			.cell(new ZIconButtonCell.Builder<ServiceDto, String>()
				.gridStore(store)
				.dynamicIcon(new IconSelector<ServiceDto>() {
					@Override
					public ImageResource selectIcon(Cell.Context context, ServiceDto ServiceDto) {
						if(ServiceDto.isActive()) return FAIconsProvider.getIcons().green();
						return FAIconsProvider.getIcons().red();
					}
				})
				.dynamicTooltip(new TooltipSelector<ServiceDto>() {
					@Override
					public String selectTooltip(Cell.Context context, ServiceDto serviceDto) {
						if(serviceDto.isActive()) return Mes.get("deactivation");
						return Mes.get("activation");
					}
				})
				.clickHandler(new GridClickHandler<ServiceDto>() {
					@Override
					public void onClick(Cell.Context context, final ServiceDto serviceDto) {
						ServicesFactory.getServiceTabService().changeActivation(serviceDto.getId(), new ServiceCallback<Void>() {
							@Override
							public void onServiceSuccess(Void result) {
								serviceDto.setActive(!serviceDto.isActive());
								store.update(serviceDto);
							}
						});

					}
				})
				.build()

			)
			.fixed()
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceDto, String>()
			.header("")
			.width(32)
			.cell(new ZIconButtonCell.Builder<ServiceDto, String>()
				.gridStore(store)
				.icon(FAIconsProvider.getIcons().pencil())
				.clickHandler(new GridClickHandler<ServiceDto>() {
					@Override
					public void onClick(Cell.Context context, final ServiceDto ServiceDto) {
						ServicesFactory.getChannelService().getFilteredChannels("", null, new ServiceCallback<List<ChannelDTO>>() {
							@Override
							public void onServiceSuccess(List<ChannelDTO> result) {
								new ServiceModifyWindow(ServiceDto, store, result);
							}
						});

					}
				})
				.build()

			)
			.fixed()
			.build());


		columns.add(new ZColumnConfig.Builder<ServiceDto, String>()
			.header("")
			.width(32)
			.cell(new ZIconButtonCell.Builder<ServiceDto, String>()
				.gridStore(store)
				.icon(FAIconsProvider.getIcons().trash())
				.clickHandler(new GridClickHandler<ServiceDto>() {
					@Override
					public void onClick(Cell.Context context, final ServiceDto ServiceDto) {

						new ZConfirmDialog(Mes.get("confirm"), Mes.get("deleteConfirmMessage")) {
							@Override
							public void onConfirm() {
								ServicesFactory.getServiceTabService().removeService(ServiceDto.getId(), new ServiceCallback<Void>() {

									@Override
									public void onServiceSuccess(Void unused) {
										store.remove(ServiceDto);
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
