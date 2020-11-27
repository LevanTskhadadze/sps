package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZIconButtonCell;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.gxt.client.zcomp.helper.BooleanStateSelector;
import com.azry.gxt.client.zcomp.helper.GridClickHandler;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.payment.PaymentDto;
import com.azry.sps.console.shared.dto.payment.PaymentInfoDto;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDto;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

public class PaymentTable {


	private static final ListStore<PaymentDto> store = new ListStore<>(new ModelKeyProvider<PaymentDto>() {
		@Override
		public String getKey(PaymentDto paymentDto) {
			return String.valueOf(paymentDto.getId());
		}
	});

	public static ListStore<PaymentDto> getListStore() {
		return store;
	}

	public static ColumnModel<PaymentDto> getMyColumnModel(final List<ServiceDto> services, final List<ChannelDTO> channels) {

		List<ColumnConfig<PaymentDto, ?>> columns = new ArrayList<>();

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("id"))
			.width(80)
			.fixed()
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					return String.valueOf(paymentDto.getId());
				}
			})
			.build());



		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("agentPaymentId"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					return paymentDto.getAgentPaymentId() == null ?
						"" : paymentDto.getAgentPaymentId();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("channel"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					for (ChannelDTO channel : channels) {
						if (channel.getId() == paymentDto.getChannelId()) {
							return channel.getName();
						}
					}

					return Mes.get("channelUnavailable");
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("service"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					for (ServiceDto service : services) {
						if (service.getId() == paymentDto.getServiceId()) {
							return service.getName();
						}
					}

					return Mes.get("serviceUnavailable");
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("abonentCode"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					return paymentDto.getAbonentCode() == null ?
						"" : paymentDto.getAbonentCode();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("amount"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					return paymentDto.getAmount() == null ?
						"" : paymentDto.getAmount().toString();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("clientCommission"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					return paymentDto.getClCommission() == null ?
						"" : paymentDto.getClCommission().toString();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("serviceCommission"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					return paymentDto.getSvcCommission() == null ?
						"" : paymentDto.getSvcCommission().toString();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("status"))
			.width(100)
			.cell(new AbstractCell<String>() {
				@Override
				public void render(Context context, String value, SafeHtmlBuilder sb) {

					PaymentDto dto = store.get(context.getIndex());
					String str = dto.getStatus() == null ?
						"" : Mes.get("PAYMENT_" + dto.getStatus().name());
					sb.appendHtmlConstant("<div style=\"font-weight: bold; color: "+ dto.getStatus().getColor() + "\">" + str + "</div>");
				}
			})
			.build());


		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header("")
			.width(32)
			.fixed()
			.cell(new ZIconButtonCell.Builder<PaymentDto, String>()
				.gridStore(store)
				.icon(FAIconsProvider.getIcons().play())
				.dynamicVisibility(new BooleanStateSelector<PaymentDto>() {
					@Override
					public boolean check(Cell.Context context, PaymentDto paymentDto) {
						return paymentDto.getStatus() == PaymentStatusDto.REJECTED;
					}
				})

				.clickHandler(new GridClickHandler<PaymentDto>() {
					@Override
					public void onClick(Cell.Context context, final PaymentDto paymentDto) {
					}
				})
				.build()
			)

			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header("")
			.width(32)
			.fixed()
			.cell(new ZIconButtonCell.Builder<PaymentDto, String>()
				.gridStore(store)
				.icon(FAIconsProvider.getIcons().eye())
				.clickHandler(new GridClickHandler<PaymentDto>() {
					@Override
					public void onClick(Cell.Context context, final PaymentDto paymentDto) {
						ServicesFactory.getPaymentService().getPaymentInfo(paymentDto.getAgentPaymentId(), paymentDto.getId(), new ServiceCallback<PaymentInfoDto>() {
							@Override
							public void onServiceSuccess(PaymentInfoDto result) {
								result.setPaymentDto(paymentDto);
								new PaymentWindow(result);
							}
						});
					}
				})
				.build()
			)

			.build());

		return new ColumnModel<>(columns);
	}

}
