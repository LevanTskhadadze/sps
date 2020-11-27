package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.azry.sps.console.shared.dto.payment.PaymentStatusLogDTO;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

import java.util.ArrayList;
import java.util.List;

public class PaymentStatusChangeTab extends VerticalLayoutContainer {


	private final ListStore<PaymentStatusLogDTO> store = new ListStore<>(new ModelKeyProvider<PaymentStatusLogDTO>() {
		@Override
		public String getKey(PaymentStatusLogDTO paymentStatusLogDTO) {
			return String.valueOf(paymentStatusLogDTO.getId());
		}
	});

	public PaymentStatusChangeTab(List<PaymentStatusLogDTO> changes) {
		super();
		store.addAll(changes);

		ZGrid<PaymentStatusLogDTO> grid = new ZGrid<>(store, initColumnModel());
		grid.setColumnResize(false);
		grid.getView().setForceFit(true);
		grid.getView().setColumnLines(true);
		add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
	}

	private ColumnModel<PaymentStatusLogDTO> initColumnModel() {
		List<ColumnConfig<PaymentStatusLogDTO, ?>> columns = new ArrayList<>();

		columns.add(new ZColumnConfig.Builder<PaymentStatusLogDTO, String>()
			.header(Mes.get("status"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentStatusLogDTO>() {
				@Override
				public String getProperty(PaymentStatusLogDTO paymentStatusLogDTO) {
					return paymentStatusLogDTO.getStatus() == null ?
						"" : Mes.get("PAYMENT_" + paymentStatusLogDTO.getStatus().name());
				}
			})
			.build());



		columns.add(new ZColumnConfig.Builder<PaymentStatusLogDTO, String>()
			.header(Mes.get("time"))
			.width(100)
			.valueProvider(new ZStringProvider<PaymentStatusLogDTO>() {
				@Override
				public String getProperty(PaymentStatusLogDTO paymentStatusLogDTO) {
					return paymentStatusLogDTO.getStatusTime() == null ?
						"" : DateTimeFormat.getFormat(PaymentDTO.DATE_PATTERN).format(paymentStatusLogDTO.getStatusTime());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentStatusLogDTO, String>()
			.header(Mes.get("message"))
			.width(200)
			.valueProvider(new ZStringProvider<PaymentStatusLogDTO>() {
				@Override
				public String getProperty(PaymentStatusLogDTO paymentStatusLogDTO) {
					return paymentStatusLogDTO.getStatusMessage() == null ?
						"" : paymentStatusLogDTO.getStatusMessage();
				}
			})
			.build());

		return new ColumnModel<>(columns);

	}

}
