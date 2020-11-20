package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.gxt.client.zcomp.ZDateField;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.payment.PaymentDto;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;

import java.math.BigDecimal;


public class PaymentInfoTab extends VerticalLayoutContainer {
	
	private static final int FIELD_WIDTH = 290;

	private ZTextField statusField;

	private final PaymentDto dto;


	public PaymentInfoTab(PaymentDto dto) {
		super();
		this.dto = dto;

		setStyleName("editForm");
		add(constructForm(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));

	}
	
	private FlexTable constructForm() {
		final FlexTable formTable = new FlexTable();
		formTable.getElement().getStyle().setMarginTop(15, com.google.gwt.dom.client.Style.Unit.PX);
		formTable.getElement().getStyle().setMarginLeft(10, com.google.gwt.dom.client.Style.Unit.PX);

		formTable.setWidget(0, 0, new HTML(Mes.get("status") + ":"));
		formTable.setWidget(1, 0, new HTML(Mes.get("statusMessage") + ":"));
		formTable.setWidget(2, 0, new HTML(Mes.get("amount") + ":"));
		formTable.setWidget(3, 0, new HTML(Mes.get("clientCommission") + ":"));
		formTable.setWidget(4, 0, new HTML(Mes.get("createTime") + ":"));
		formTable.setWidget(5, 0, new HTML(Mes.get("lastUpdateTime") + ":"));
		formTable.getWidget(1, 0).setWidth("220px");

		formTable.setWidget(0, 1, getStatusField());
		formTable.setWidget(1, 1, getStatusMessageField());
		formTable.setWidget(2, 1, getAmountField());
		formTable.setWidget(3, 1, getClientCommissionField());
		formTable.setWidget(4, 1, getCreateTimeField());
		formTable.setWidget(5, 1, getLastChangeTimeField());
		formTable.setCellSpacing(4);

		return formTable;
	}


	private ZTextField getStatusField() {
		statusField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		statusField.setValue(Mes.get("PAYMENT_" + dto.getStatus().name()));
		return statusField;
	}

	private ZTextField getStatusMessageField() {
		ZTextField statusMessageField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		statusMessageField.setValue(dto.getStatusMessage());
		return statusMessageField;
	}


	private BigDecimalField getAmountField() {
		BigDecimalField amountField = new BigDecimalField();
		amountField.setWidth(FIELD_WIDTH);
		amountField.setValue(dto.getAmount());
		amountField.setEnabled(false);
		return amountField;
	}

	private BigDecimalField getClientCommissionField() {
		BigDecimalField clientCommissionField = new BigDecimalField();
		clientCommissionField.setWidth(FIELD_WIDTH);
		clientCommissionField.setValue(dto.getClCommission() == null ? new BigDecimal(0) : dto.getClCommission());
		clientCommissionField.setEnabled(false);
		return clientCommissionField;
	}

	private ZDateField getCreateTimeField() {
		ZDateField createTimeField = new ZDateField.Builder()
			.width(FIELD_WIDTH)
			.enabled(false)
			.pattern(PaymentDto.DATE_PATTERN)
			.build();
		createTimeField.setValue(dto.getCreateTime());
		return createTimeField;
	}

	private ZDateField getLastChangeTimeField() {
		ZDateField lastChangeTimeField = new ZDateField.Builder()
			.width(FIELD_WIDTH)
			.pattern(PaymentDto.DATE_PATTERN)
			.enabled(false)
			.build();
		lastChangeTimeField.setValue(dto.getStatusChangeTime());
		return lastChangeTimeField;
	}

}
