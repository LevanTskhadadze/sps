package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZIconButtonCell;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.gxt.client.zcomp.helper.GridClickHandler;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.payment.PaymentDto;
import com.azry.sps.console.shared.dto.payment.PaymentInfoDto;
import com.google.gwt.cell.client.Cell;
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


	public static ListStore<PaymentDto> setListStore(List<PaymentDto> result) {
		store.clear();
		store.addAll(result);

		return store;
	}

	public static ListStore<PaymentDto> getListStore() {
		return store;
	}

	@SuppressWarnings("unchecked")
	public static ColumnModel<PaymentDto> getMyColumnModel() {

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
					return String.valueOf(paymentDto.getChannelId());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("service"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					return String.valueOf(paymentDto.getServiceId());
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
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					return paymentDto.getStatus() == null ?
						"" : paymentDto.getStatus().name();
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
