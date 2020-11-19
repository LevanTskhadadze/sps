package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.payment.PaymentInfoDto;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;

public class PaymentWindow extends ZWindow {

	private final TabPanel tabPanel = new TabPanel();

	public PaymentWindow(PaymentInfoDto dto) {
		super();

		tabPanel.add(new PaymentInfoTab(dto.getPaymentDto()), Mes.get("payment"));
		tabPanel.add(new PaymentClientInfoTab(dto.getPaymentDto()), Mes.get("client"));
		tabPanel.add(new PaymentTransactionsTab(dto.getPaymentDto(), dto.getPrincipal(), dto.getCommission()), Mes.get("transactions"));
		tabPanel.add(new PaymentStatusChangeTab(dto.getChanges()), Mes.get("paymentStatuses"));

		add(tabPanel, new MarginData(0));

		setHeight("550px");
		setWidth("700px");
		setHeadingText(Mes.get("paymentInfo"));
		showInCenter();

	}

}
