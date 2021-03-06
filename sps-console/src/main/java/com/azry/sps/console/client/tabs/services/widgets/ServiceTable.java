package com.azry.sps.console.client.tabs.services.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZIconButtonCell;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.gxt.client.zcomp.ZURLBuilder;
import com.azry.gxt.client.zcomp.helper.BooleanStateSelector;
import com.azry.gxt.client.zcomp.helper.GridClickHandler;
import com.azry.gxt.client.zcomp.helper.IconSelector;
import com.azry.gxt.client.zcomp.helper.StringStateSelector;
import com.azry.gxt.client.zcomp.helper.TooltipSelector;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

public class ServiceTable {

	private static final String OPACITY_STYLE = "opacity: 0.3";

	private static final ListStore<ServiceDTO> store = new ListStore<>(new ModelKeyProvider<ServiceDTO>() {
		@Override
		public String getKey(ServiceDTO serviceDTO) {
			return String.valueOf(serviceDTO.getId());
		}
	});

	public static ListStore<ServiceDTO> getListStore() {
		return store;
	}

	@SuppressWarnings("unchecked")
	public static ColumnModel<ServiceDTO> getMyColumnModel(final boolean isManage) {

		List<ColumnConfig<ServiceDTO, ?>> columns = new ArrayList<>();
		columns.add(new ZColumnConfig.Builder<ServiceDTO, String>()
			.header(Mes.get("id"))
			.width(20)
			.valueProvider(new ZStringProvider<ServiceDTO>() {
				@Override
				public String getProperty(ServiceDTO ServiceDTO) {
					return String.valueOf(ServiceDTO.getId());
				}
			})
			.build());
		columns.add(new ZColumnConfig.Builder<ServiceDTO, String>()
			.header(Mes.get("name"))
			.width(100)
			.valueProvider(new ZStringProvider<ServiceDTO>() {
				@Override
				public String getProperty(ServiceDTO ServiceDTO) {
					return ServiceDTO.getName();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceDTO, String>()
			.header(Mes.get("createTime"))
			.width(100)
			.valueProvider(new ZStringProvider<ServiceDTO>() {
				@Override
				public String getProperty(ServiceDTO ServiceDTO) {
					return ServiceDTO.getFormattedCreateTime();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceDTO, String>()
			.headerHtml(Mes.get("lastUpdateTime") + "<i class=\"fa fa-sort\" style = \"padding-left: 3px;\"></i>")
			.width(100)
			.valueProvider(new ZStringProvider<ServiceDTO>() {
				@Override
				public String getProperty(ServiceDTO ServiceDTO) {
					return ServiceDTO.getFormattedLastUpdateTime();
				}
			})
			.sortable()
			.build());

		columns.add(new ZColumnConfig.Builder<ServiceDTO, String>()
			.header("")
			.width(32)
			.fixed()
			.cellClassName("grid-icon-column")
			.cell(new ZIconButtonCell.Builder<ServiceDTO, String>()
				.gridStore(store)
				.dynamicIcon(
					new IconSelector<ServiceDTO>() {
						@Override
						public ImageResource selectIcon(Cell.Context context, ServiceDTO dto) {
							final String url = ZURLBuilder.create(GWT.getHostPageBaseURL(), "sps/servlet/iconDownload")
								.param("id", dto.getId())
								.param("t", 1)
								.build();
							return new ImageResource() {
								@Override
								public int getHeight() {
									return 25;
								}

								@Override
								public int getLeft() {
									return 0;
								}

								@Override
								public SafeUri getSafeUri() {
									return new SafeUri() {
										@Override
										public String asString() {
											return url;
										}
									};
								}

								@Override
								public int getTop() {
									return 0;
								}

								@Override
								public String getURL() {
									return null;
								}

								@Override
								public int getWidth() {
									return 25;
								}

								@Override
								public boolean isAnimated() {
									return false;
								}

								@Override
								public String getName() {
									return null;
								}
							};
						}
					}
				)
				.dynamicEnabling(new BooleanStateSelector<ServiceDTO>() {
					@Override
					public boolean check(Cell.Context context, ServiceDTO dto) {
						if (isManage) {
							return true;
						}
						return false;
					}
				})
				.clickHandler(new GridClickHandler<ServiceDTO>() {
					@Override
					public void onClick(Cell.Context context, final ServiceDTO dto) {
						new IconEditWindow("service", dto.getId(), store) {
							@Override
							public void onSave() {
								store.update(dto);
							}
						}.showInCenter();
					}
				})
				.build()
			)
			.build());


		columns.add(new ZColumnConfig.Builder<ServiceDTO, String>()
			.width(32)
			.cell(new ZIconButtonCell.Builder<ServiceDTO, String>()
				.gridStore(store)
				.dynamicIcon(new IconSelector<ServiceDTO>() {
					@Override
					public ImageResource selectIcon(Cell.Context context, ServiceDTO ServiceDTO) {
						if (ServiceDTO.isActive()) return FAIconsProvider.getIcons().green();
						return FAIconsProvider.getIcons().red();
					}
				})
				.dynamicTooltip(new TooltipSelector<ServiceDTO>() {
					@Override
					public String selectTooltip(Cell.Context context, ServiceDTO serviceDTO) {
						if (serviceDTO.isActive()) return Mes.get("deactivation");
						return Mes.get("activation");
					}
				})
				.dynamicEnabling(new BooleanStateSelector<ServiceDTO>() {
					@Override
					public boolean check(Cell.Context context, ServiceDTO dto) {
						if (isManage) {
							return true;
						}
						return false;
					}
				})
				.dynamicStyle(new StringStateSelector<ServiceDTO>() {
					@Override
					public String getStyle(Cell.Context context, ServiceDTO dto) {
						if (!isManage) {
							return OPACITY_STYLE;
						}
						return "";
					}
				})
				.clickHandler(new GridClickHandler<ServiceDTO>() {
					@Override
					public void onClick(Cell.Context context, final ServiceDTO dto) {
						new ZConfirmDialog(Mes.get("confirm"), dto.isActive() ? Mes.get("deactivateConfigurableConfirmation") : Mes.get("activateConfigurableConfirmation")) {
							@Override
							public void onConfirm() {
								dto.setActive(!dto.isActive());
								ServicesFactory.getServiceTabService().changeActivation(dto.getId(), dto.getVersion(),
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

		if (isManage) {
			columns.add(new ZColumnConfig.Builder<ServiceDTO, String>()
				.width(32)
				.cell(new ZIconButtonCell.Builder<ServiceDTO, String>()
					.gridStore(store)
					.tooltip(Mes.get("edit"))
					.icon(FAIconsProvider.getIcons().pencil())
					.clickHandler(new GridClickHandler<ServiceDTO>() {
						@Override
						public void onClick(Cell.Context context, final ServiceDTO ServiceDTO) {
							new ServiceModifyWindow(ServiceDTO, store, ActionMode.EDIT);
						}
					})
					.build()

				)
				.fixed()
				.build());


			columns.add(new ZColumnConfig.Builder<ServiceDTO, String>()
				.width(32)
				.cell(new ZIconButtonCell.Builder<ServiceDTO, String>()
					.gridStore(store)
					.tooltip(Mes.get("delete"))
					.icon(FAIconsProvider.getIcons().trash())
					.clickHandler(new GridClickHandler<ServiceDTO>() {
						@Override
						public void onClick(Cell.Context context, final ServiceDTO serviceDTO) {
							new ZConfirmDialog(Mes.get("confirm"), Mes.get("deleteConfirmMessage")) {
								@Override
								public void onConfirm() {
									ServicesFactory.getServiceTabService().removeService(serviceDTO.getId(), new ServiceCallback<Void>() {

										@Override
										public void onServiceSuccess(Void unused) {
											store.remove(serviceDTO);
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
			columns.add(new ZColumnConfig.Builder<ServiceDTO, String>()
				.width(32)
				.cell(new ZIconButtonCell.Builder<ServiceDTO, String>()
					.gridStore(store)
					.tooltip(Mes.get("view"))
					.icon(FAIconsProvider.getIcons().eye())
					.clickHandler(new GridClickHandler<ServiceDTO>() {
						@Override
						public void onClick(Cell.Context context, final ServiceDTO ServiceDTO) {
							new ServiceModifyWindow(ServiceDTO, store, ActionMode.VIEW);
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
