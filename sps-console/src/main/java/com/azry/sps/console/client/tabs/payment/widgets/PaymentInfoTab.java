package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.gxt.client.zcomp.ZDateField;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;

import java.math.BigDecimal;


public class PaymentInfoTab extends VerticalLayoutContainer {
	
	private static final int FIELD_WIDTH = 290;

	private ZTextField statusField;

	private final PaymentDTO dto;

	private final ChannelDTO channelDTO;

	private final ServiceDTO serviceDTO;


	public PaymentInfoTab(PaymentDTO dto, ServiceDTO serviceDTO, ChannelDTO channelDTO) {
		super();
		this.dto = dto;

		if (channelDTO == null) {
			this.channelDTO = new ChannelDTO();
		} else {
			this.channelDTO = channelDTO;
		}

		if (serviceDTO == null) {
			this.serviceDTO = new ServiceDTO();
		} else {
			this.serviceDTO = serviceDTO;
		}

		setStyleName("editForm");
		add(constructForm(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));

	}
	
	private FlexTable constructForm() {
		final FlexTable formTable = new FlexTable();
		formTable.getElement().getStyle().setMarginTop(15, com.google.gwt.dom.client.Style.Unit.PX);
		formTable.getElement().getStyle().setMarginLeft(10, com.google.gwt.dom.client.Style.Unit.PX);


		formTable.setWidget(0, 0, new HTML(Mes.get("id") + ":"));
		formTable.setWidget(1, 0, new HTML(Mes.get("agentPaymentId") + ":"));
		formTable.setWidget(2, 0, new HTML(Mes.get("service") + ":"));
		formTable.setWidget(3, 0, new HTML(Mes.get("channel") + ":"));
		formTable.setWidget(4, 0, new HTML(Mes.get("abonentCode") + ":"));
		formTable.setWidget(5, 0, new HTML(Mes.get("status") + ":"));
		formTable.setWidget(6, 0, new HTML(Mes.get("statusMessage") + ":"));
		formTable.setWidget(7, 0, new HTML(Mes.get("amount") + ":"));
		formTable.setWidget(8, 0, new HTML(Mes.get("serviceCommission") + ":"));
		formTable.setWidget(9, 0, new HTML(Mes.get("clientCommission") + ":"));
		formTable.setWidget(10, 0, new HTML(Mes.get("createTime") + ":"));
		formTable.setWidget(11, 0, new HTML(Mes.get("lastUpdateTime") + ":"));
		formTable.getWidget(1, 0).setWidth("220px");

		formTable.setWidget(0, 1, getIdField());
		formTable.setWidget(1, 1, getAgentPaymentIdField());
		formTable.setWidget(2, 1, getServiceField());
		formTable.setWidget(3, 1, getChannelField());
		formTable.setWidget(4, 1, getAbonentCodeField());
		formTable.setWidget(5, 1, getStatusField());
		formTable.setWidget(6, 1, getStatusMessageField());
		formTable.setWidget(7, 1, getAmountField());
		formTable.setWidget(8, 1, getServiceCommissionField());
		formTable.setWidget(9, 1, getClientCommissionField());
		formTable.setWidget(10, 1, getCreateTimeField());
		formTable.setWidget(11, 1, getLastChangeTimeField());
		formTable.setCellSpacing(4);

		return formTable;
	}

	private ZTextField getIdField() {
		ZTextField idField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		idField.setValue(String.valueOf(dto.getId()));
		return idField;
	}

	private ZTextField getAgentPaymentIdField() {
		ZTextField AgentPaymentIdField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		AgentPaymentIdField.setValue(dto.getAgentPaymentId() == null ? "" : dto.getAgentPaymentId());
		return AgentPaymentIdField;
	}

	private ZTextField getChannelField() {
		ZTextField channelField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		channelField.setValue(channelDTO.getName() == null ? "" : channelDTO.getName());
		return channelField;
	}


	private ZTextField getServiceField() {
		ZTextField serviceField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		serviceField.setValue(serviceDTO.getName() == null ? "" : serviceDTO.getName());
		return serviceField;
	}


	private ZTextField getAbonentCodeField() {
		ZTextField abonentCodeField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		abonentCodeField.setValue(dto.getAbonentCode());
		return abonentCodeField;
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

	private BigDecimalField getServiceCommissionField() {
		BigDecimalField serviceCommissionField = new BigDecimalField();
		serviceCommissionField.setWidth(FIELD_WIDTH);
		serviceCommissionField.setValue(dto.getSvcCommission() == null ? new BigDecimal(0) : dto.getClCommission());
		serviceCommissionField.setEnabled(false);
		return serviceCommissionField;
	}

	private ZDateField getCreateTimeField() {
		ZDateField createTimeField = new ZDateField.Builder()
			.width(FIELD_WIDTH)
			.enabled(false)
			.pattern(PaymentDTO.DATE_PATTERN)
			.build();
		createTimeField.setValue(dto.getCreateTime());
		return createTimeField;
	}

	private ZDateField getLastChangeTimeField() {
		ZDateField lastChangeTimeField = new ZDateField.Builder()
			.width(FIELD_WIDTH)
			.pattern(PaymentDTO.DATE_PATTERN)
			.enabled(false)
			.build();
		lastChangeTimeField.setValue(dto.getStatusChangeTime());
		return lastChangeTimeField;
	}

}
