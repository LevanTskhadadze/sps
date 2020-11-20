package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.payment.PaymentInfoDto;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

public class PaymentWindow extends ZWindow {

	private static final String WINDOW_BOTTOM_BORDER_STYLE = "1px solid #3291D6";

	private final TabPanel tabPanel = new TabPanel();

	public PaymentWindow(PaymentInfoDto dto) {
		super();

		tabPanel.add(new PaymentInfoTab(dto.getPaymentDto()), Mes.get("payment"));
		tabPanel.add(new PaymentClientInfoTab(dto.getPaymentDto()), Mes.get("client"));
		tabPanel.add(new PaymentTransactionsTab(dto.getPaymentDto(), dto.getPrincipal(), dto.getCommission()), Mes.get("transactions"));
		tabPanel.add(new PaymentStatusChangeTab(dto.getChanges()), Mes.get("paymentStatuses"));

		add(tabPanel, new MarginData(0));

		getButtonBar().getElement().getStyle().setProperty("borderTop", WINDOW_BOTTOM_BORDER_STYLE);
		buttonBar.setMinButtonWidth(75);
		addButton(getCancelButton());

		setHeight("590px");
		setWidth("570px");
		setHeadingText(Mes.get("paymentInfo"));
		showInCenter();


	}

	ZButton getCancelButton() {
		return new ZButton.Builder()
			.text(Mes.get("quit"))
			.icon(FAIconsProvider.getIcons().ban_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					hide();
				}
			})
			.build();
	}


}
