package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.payment.PaymentDto;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

public class PaymentStatusChangeTab extends VerticalLayoutContainer {


	private final ListStore<PaymentDto> store = new ListStore<>(new ModelKeyProvider<PaymentDto>() {
		@Override
		public String getKey(PaymentDto paymentDto) {
			return String.valueOf(paymentDto.getId());
		}
	});

	public PaymentStatusChangeTab(List<PaymentDto> changes) {
		super();
		store.addAll(changes);

		ZGrid<PaymentDto> grid = new ZGrid<>(store, initColumnModel());
		grid.setColumnResize(false);
		grid.getView().setForceFit(true);
		grid.getView().setColumnLines(true);
		add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
	}

	private ColumnModel<PaymentDto> initColumnModel() {
		List<ColumnConfig<PaymentDto, ?>> columns = new ArrayList<>();

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("status"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					return paymentDto.getSvcCommission() == null ?
						"" : Mes.get("PAYMENT_" + paymentDto.getStatus().name());
				}
			})
			.build());



		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("statusChangeTime"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					return paymentDto.getStatusChangeTime() == null ?
						"" : DateTimeFormat.getFormat(PaymentDto.DATE_PATTERN).format(paymentDto.getStatusChangeTime());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDto, String>()
			.header(Mes.get("statusMessage"))
			.width(200)
			.valueProvider(new ZStringProvider<PaymentDto>() {
				@Override
				public String getProperty(PaymentDto paymentDto) {
					return paymentDto.getStatusMessage() == null ?
						"" : paymentDto.getStatusMessage();
				}
			})
			.build());

		return new ColumnModel<>(columns);

	}

}
