package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.azry.sps.console.shared.dto.transactionorder.TransactionOrderDTO;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.FieldSet;

public class PaymentTransactionsTab extends VerticalLayoutContainer {

	private static final int FIELD_WIDTH = 290;

	private PaymentDTO paymentDTO;

	public PaymentTransactionsTab(PaymentDTO paymentDTO, TransactionOrderDTO principal, TransactionOrderDTO commission) {
		super();
		this.paymentDTO = paymentDTO;
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

	private FlexTable getForm(TransactionOrderDTO dto) {
		final FlexTable formTable = new FlexTable();
		formTable.getElement().getStyle().setMarginTop(15, com.google.gwt.dom.client.Style.Unit.PX);
		formTable.getElement().getStyle().setMarginLeft(10, com.google.gwt.dom.client.Style.Unit.PX);

		formTable.setWidget(0, 0, new HTML(Mes.get("fromAccount") + ":"));
		formTable.setWidget(1, 0, new HTML(Mes.get("recipient")  + ":"));
		formTable.setWidget(2, 0, new HTML(Mes.get("purpose") + ":"));
		formTable.setWidget(3, 0, new HTML(Mes.get("statusChangeTime") + ":"));
		formTable.setWidget(4, 0, new HTML(Mes.get("createTime") + ":"));
		formTable.getWidget(1, 0).setWidth("220px");

		formTable.setWidget(0, 1, getSenderAccountField(dto));
		formTable.setWidget(1, 1, getRecipientField(dto));
		formTable.setWidget(2, 1, getPurposeField(dto));
		formTable.setWidget(3, 1, getStatusChangeTime());
		formTable.setWidget(4, 1, getCreateTime());
		formTable.setCellSpacing(4);
		formTable.setStyleName("editForm");

		return formTable;

	}

	private ZTextField getPurposeField(TransactionOrderDTO dto) {
		ZTextField purposeField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		if (dto.getPurpose() != null) {
			purposeField.setValue(dto.getPurpose());
		}
		return purposeField;
	}

	private ZTextField getStatusChangeTime() {
		ZTextField statusChangeTime = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		if (paymentDTO.getStatusChangeTime() != null) {
			statusChangeTime.setValue(DateTimeFormat.getFormat(PaymentDTO.DATE_PATTERN).format(paymentDTO.getStatusChangeTime()));
		}
		return statusChangeTime;
	}

	private ZTextField getCreateTime() {
		ZTextField createTimeField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();

		if (paymentDTO.getCreateTime() != null) {
			createTimeField.setValue(DateTimeFormat.getFormat(PaymentDTO.DATE_PATTERN).format(paymentDTO.getCreateTime()));
		}
		return createTimeField;
	}

	private ZTextField getSenderAccountField(TransactionOrderDTO dto) {
		ZTextField senderField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();

		if (dto.getSourceAccountIBAN() != null) {
			senderField.setValue(dto.getSourceAccountIBAN());
		}
		return senderField;
	}

	private ZTextField getRecipientField(TransactionOrderDTO dto) {
		ZTextField recipientField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		if (dto.getDestinationAccountIBAN() != null) {
			recipientField.setValue(dto.getDestinationAccountIBAN());
		}
		return recipientField;
	}

}
