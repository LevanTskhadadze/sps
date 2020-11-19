package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.payment.PaymentDto;
import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDto;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldSet;

public class PaymentTransactionsTab extends VerticalLayoutContainer {

	private static final int FIELD_WIDTH = 290;

	private PaymentDto paymentDto;

	public PaymentTransactionsTab(PaymentDto paymentDto, TransactionOrderDto principal, TransactionOrderDto commission) {
		super();
		this.paymentDto = paymentDto;
		FieldSet principalFieldSet = new FieldSet();
		principalFieldSet.setHeadingText(Mes.get("principalAmount"));
		principalFieldSet.add(getForm(principal), new MarginData(0));

		FieldSet clientCommissionFieldSet = new FieldSet();
		clientCommissionFieldSet.setHeadingText(Mes.get("clientCommissionAmount"));
		clientCommissionFieldSet.add(getForm(principal),  new MarginData(0));

		clientCommissionFieldSet.setStyleName("fieldSet");

		principalFieldSet.setStyleName("fieldSet");
		add(principalFieldSet, new VerticalLayoutData(0.98, -1));
		add(clientCommissionFieldSet, new VerticalLayoutData(0.98, -1));

	}

	private FlexTable getForm(TransactionOrderDto dto) {
		final FlexTable formTable = new FlexTable();
		formTable.getElement().getStyle().setMarginTop(15, com.google.gwt.dom.client.Style.Unit.PX);
		formTable.getElement().getStyle().setMarginLeft(10, com.google.gwt.dom.client.Style.Unit.PX);

		formTable.setWidget(0, 0, new HTML(Mes.get("fromAccount") + ":"));
		formTable.setWidget(1, 0, new HTML(Mes.get("recipient")  + ":"));
		formTable.setWidget(2, 0, new HTML(Mes.get("purpose") + ":"));
		formTable.setWidget(3, 0, new HTML(Mes.get("status") + ":"));
		formTable.setWidget(4, 0, new HTML(Mes.get("statusMessage") + ":"));
		formTable.setWidget(5, 0, new HTML(Mes.get("statusChangeTime") + ":"));
		formTable.setWidget(6, 0, new HTML(Mes.get("createTime") + ":"));
		formTable.getWidget(1, 0).setWidth("220px");

		formTable.setWidget(0, 1, getSenderAccountField(dto));
		formTable.setWidget(1, 1, getRecipientField(dto));
		formTable.setWidget(2, 1, getPurposeField(dto));
		formTable.setWidget(3, 1, getStatusField());
		formTable.setWidget(4, 1, getStatusMessageField(dto));
		formTable.setWidget(5, 1, getStatusChangeTime());
		formTable.setWidget(6, 1, getCreateTime());
		formTable.setCellSpacing(4);
		formTable.setStyleName("editForm");

		return formTable;

	}

	private ZTextField getPurposeField(TransactionOrderDto dto) {
		ZTextField purposeField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		if (dto.getPurpose() != null) {
			purposeField.setValue(dto.getPurpose());
		}
		return purposeField;
	}

	private ZTextField getStatusField() {
		ZTextField statusField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		if (paymentDto.getStatus() != null) {
			statusField.setValue(Mes.get("PAYMENT_" + paymentDto.getStatus().name()));
		}
		return statusField;
	}

	private ZTextField getStatusMessageField(TransactionOrderDto dto) {
		ZTextField firstNameField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		if (dto.getSourceAccountBAN() != null) {
			firstNameField.setValue(dto.getSourceAccountBAN());
		}
		return firstNameField;
	}

	private ZTextField getStatusChangeTime() {
		ZTextField statusChangeTime = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		if (paymentDto.getStatusChangeTime() != null) {
			statusChangeTime.setValue(DateTimeFormat.getFormat(PaymentDto.DATE_PATTERN).format(paymentDto.getStatusChangeTime()));
		}
		return statusChangeTime;
	}

	private ZTextField getCreateTime() {
		ZTextField createTimeField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();

		if (paymentDto.getCreateTime() != null) {
			createTimeField.setValue(DateTimeFormat.getFormat(PaymentDto.DATE_PATTERN).format(paymentDto.getCreateTime()));
		}
		return createTimeField;
	}

	private ZTextField getSenderAccountField(TransactionOrderDto dto) {
		ZTextField senderField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();

		if (dto.getSourceAccountBAN() != null) {
			senderField.setValue(dto.getSourceAccountBAN());
		}
		return senderField;
	}

	private ZTextField getRecipientField(TransactionOrderDto dto) {
		ZTextField recipientField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		if (dto.getDestinationAccountBAN() != null) {
			recipientField.setValue(dto.getDestinationAccountBAN());
		}
		return recipientField;
	}

}
