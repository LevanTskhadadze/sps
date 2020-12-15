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
import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.azry.sps.console.shared.dto.payment.PaymentInfoDTO;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDTO;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
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


	private static final ListStore<PaymentDTO> store = new ListStore<>(new ModelKeyProvider<PaymentDTO>() {
		@Override
		public String getKey(PaymentDTO paymentDTO) {
			return String.valueOf(paymentDTO.getId());
		}
	});


	public static ListStore<PaymentDTO> getListStore() {
		return store;
	}

	public static ColumnModel<PaymentDTO> getMyColumnModel(final List<ServiceDTO> services, final List<ChannelDTO> channels) {

		List<ColumnConfig<PaymentDTO, ?>> columns = new ArrayList<>();

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.header(Mes.get("id"))
			.width(80)
			.fixed()
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO paymentDTO) {
					return String.valueOf(paymentDTO.getId());
				}
			})
			.build());



		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.header(Mes.get("agentPaymentId"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO paymentDTO) {
					return paymentDTO.getAgentPaymentId() == null ?
						"" : paymentDTO.getAgentPaymentId();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.header(Mes.get("channel"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO paymentDTO) {
					for (ChannelDTO channel : channels) {
						if (channel.getId() == paymentDTO.getChannelId()) {
							return channel.getName();
						}
					}

					return Mes.get("channelUnavailable");
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.header(Mes.get("service"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO paymentDTO) {
					for (ServiceDTO service : services) {
						if (service.getId() == paymentDTO.getServiceId()) {
							return service.getName();
						}
					}

					return Mes.get("serviceUnavailable");
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.header(Mes.get("abonentCode"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO paymentDTO) {
					return paymentDTO.getAbonentCode() == null ?
						"" : paymentDTO.getAbonentCode();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.header(Mes.get("amount"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO paymentDTO) {
					return paymentDTO.getAmount() == null ?
						"" : paymentDTO.getAmount().toString();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.header(Mes.get("clientCommission"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO paymentDTO) {
					return paymentDTO.getClCommission() == null ?
						"" : paymentDTO.getClCommission().toString();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.header(Mes.get("serviceCommission"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO paymentDTO) {
					return paymentDTO.getSvcCommission() == null ?
						"" : paymentDTO.getSvcCommission().toString();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.header(Mes.get("status"))
			.width(100)
			.cell(new AbstractCell<String>() {
				@Override
				public void render(Context context, String value, SafeHtmlBuilder sb) {

					PaymentDTO dto = store.get(context.getIndex());
					String str = dto.getStatus() == null ?
						"" : Mes.get("PAYMENT_" + dto.getStatus().name());
					sb.appendHtmlConstant("<div class = \"tooltip\" style=\"font-weight: bold; color: "+ dto.getStatus().getColor() + "\">" +
						str + "  <span class=\"tooltiptext\">"+dto.getStatusMessage()+"</span>\n" +
						  "</div>");
				}
			})
			.build());


		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.header("")
			.width(32)
			.fixed()
			.cell(new ZIconButtonCell.Builder<PaymentDTO, String>()
				.gridStore(store)
				.icon(FAIconsProvider.getIcons().play())
				.dynamicVisibility(new BooleanStateSelector<PaymentDTO>() {
					@Override
					public boolean check(Cell.Context context, PaymentDTO paymentDTO) {
						return paymentDTO.getStatus() == PaymentStatusDTO.REJECTED;
					}
				})

				.clickHandler(new GridClickHandler<PaymentDTO>() {
					@Override
					public void onClick(final Cell.Context context, final PaymentDTO paymentDTO) {
						ServicesFactory.getPaymentService().retryPayment(paymentDTO.getId(), new ServiceCallback<PaymentStatusDTO>() {
							@Override
							public void onServiceSuccess(PaymentStatusDTO result) {
								paymentDTO.setStatus(result);
								store.update(paymentDTO);
							}
						});
					}
				})
				.build()
			)

			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.header("")
			.width(32)
			.fixed()
			.cell(new ZIconButtonCell.Builder<PaymentDTO, String>()
				.gridStore(store)
				.icon(FAIconsProvider.getIcons().eye())
				.clickHandler(new GridClickHandler<PaymentDTO>() {
					@Override
					public void onClick(Cell.Context context, final PaymentDTO paymentDTO) {
						ServicesFactory.getPaymentService().getPaymentInfo(paymentDTO, new ServiceCallback<PaymentInfoDTO>() {
							@Override
							public void onServiceSuccess(PaymentInfoDTO result) {
								result.setPaymentDTO(paymentDTO);
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
