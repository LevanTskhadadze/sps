package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZDateField;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.payment.PaymentDto;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;

import java.math.BigDecimal;
import java.util.List;


public class PaymentInfoTab extends TabPanel {
	
	private static final int FIELD_WIDTH = 290;

	private ZTextField statusField;

	private final PaymentDto dto;


	public PaymentInfoTab(PaymentDto dto) {
		super();
		this.dto = dto;

		VerticalLayoutContainer formContainer = new VerticalLayoutContainer();
		formContainer.setStyleName("editForm");
		formContainer.add(constructForm(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
		add(formContainer, Mes.get("paymentInfo"));

		/*add(tabPanel, new MarginData(0));

		setHeight("500px");
		setWidth("700px");
		String header = Mes.get("ofService") + " " + (redactMode ? Mes.get("redact") : Mes.get("addEntry"));
		setHeadingText(header);
		showInCenter();
		 */
	}
	
	private FlexTable constructForm() {
		final FlexTable formTable = new FlexTable();
		formTable.getElement().getStyle().setMarginTop(15, com.google.gwt.dom.client.Style.Unit.PX);
		formTable.getElement().getStyle().setMarginLeft(10, com.google.gwt.dom.client.Style.Unit.PX);
		
		formTable.setWidget(0, 0, new HTML(Mes.get("name") + ":"));
		formTable.setWidget(1, 0, new HTML(Mes.get("lastName")  + ":"));
		formTable.setWidget(2, 0, new HTML(Mes.get("status") + ":"));
		formTable.setWidget(3, 0, new HTML(Mes.get("statusMessage") + ":"));
		formTable.setWidget(4, 0, new HTML(Mes.get("amount") + ":"));
		formTable.setWidget(5, 0, new HTML(Mes.get("clientCommission") + ":"));
		formTable.setWidget(6, 0, new HTML(Mes.get("creationTime") + ":"));
		formTable.setWidget(7, 0, new HTML(Mes.get("lastUpdateTime") + ":"));
		formTable.getWidget(1, 0).setWidth("220px");

		formTable.setWidget(0, 1, getNameField());
		formTable.setWidget(1, 1, getLastNameField());
		formTable.setWidget(2, 1, getStatusField());
		formTable.setWidget(3, 1, getStatusMessageField());
		formTable.setWidget(4, 1, getAmountField());
		formTable.setWidget(5, 1, getClientCommissionField());
		formTable.setWidget(6, 1, getCreateTimeField());
		formTable.setWidget(7, 1, getLastChangeTimeField());
		formTable.setCellSpacing(4);

		return formTable;
	}


	private ZTextField getNameField() {
		ZTextField firstNameField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		firstNameField.setValue(dto.getClient().getFirstName());
		return firstNameField;
	}

	private ZTextField getLastNameField() {
		ZTextField lastNameField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		lastNameField.setValue(dto.getClient().getLastName());
		return lastNameField;
	}

	private ZTextField getStatusField() {
		statusField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		statusField.setValue(dto.getStatus().name());
		return statusField;
	}

	private ZTextField getStatusMessageField() {
		ZTextField statusMessageField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		statusField.setValue(dto.getStatusMessage());
		return statusMessageField;
	}


	private BigDecimalField getAmountField() {
		BigDecimalField amountField = new BigDecimalField();
		amountField.setWidth(FIELD_WIDTH);
		amountField.setValue(dto.getAmount());
		return amountField;
	}

	private BigDecimalField getClientCommissionField() {
		BigDecimalField clientCommissionField = new BigDecimalField();
		clientCommissionField.setWidth(FIELD_WIDTH);
		clientCommissionField.setValue(dto.getClCommission() == null ? new BigDecimal(0) : dto.getClCommission());
		return clientCommissionField;
	}

	private ZDateField getCreateTimeField() {
		ZDateField createTimeField = new ZDateField.Builder()
			.width(FIELD_WIDTH)
			.enabled(false)
			.build();
		createTimeField.setValue(dto.getCreateTime());
		return createTimeField;
	}

	private ZDateField getLastChangeTimeField() {
		ZDateField lastChangeTimeField = new ZDateField.Builder()
			.width(FIELD_WIDTH)
			.enabled(false)
			.build();
		lastChangeTimeField.setValue(dto.getCreateTime());
		return lastChangeTimeField;
	}

}
