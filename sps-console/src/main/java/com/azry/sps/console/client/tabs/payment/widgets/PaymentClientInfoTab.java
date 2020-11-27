package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.gxt.client.zcomp.ZDateField;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;


public class PaymentClientInfoTab extends VerticalLayoutContainer {

	private static final int FIELD_WIDTH = 290;

	private final PaymentDTO dto;

	public PaymentClientInfoTab(PaymentDTO dto) {
		super();
		this.dto = dto;

		setStyleName("editForm");
		add(constructForm(), new VerticalLayoutData(1, -1));

	}
	
	private FlexTable constructForm() {
		final FlexTable formTable = new FlexTable();
		formTable.getElement().getStyle().setMarginTop(15, com.google.gwt.dom.client.Style.Unit.PX);
		formTable.getElement().getStyle().setMarginLeft(10, com.google.gwt.dom.client.Style.Unit.PX);
		
		formTable.setWidget(0, 0, new HTML(Mes.get("name") + ":"));
		formTable.setWidget(1, 0, new HTML(Mes.get("lastName")  + ":"));
		formTable.setWidget(2, 0, new HTML(Mes.get("personalNumber") + ":"));
		formTable.setWidget(3, 0, new HTML(Mes.get("birthPlace") + ":"));
		formTable.setWidget(4, 0, new HTML(Mes.get("birthDate") + ":"));
		formTable.getWidget(1, 0).setWidth("220px");

		formTable.setWidget(0, 1, getNameField());
		formTable.setWidget(1, 1, getLastNameField());
		formTable.setWidget(2, 1, getPersonalNumberField());
		formTable.setWidget(3, 1, getBirthPlaceField());
		formTable.setWidget(4, 1, getBirthDateField());
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

	private ZTextField getPersonalNumberField() {
		ZTextField personalNumberField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		personalNumberField.setValue(dto.getClient().getPersonalNumber());
		return personalNumberField;
	}

	private ZTextField getBirthPlaceField() {
		ZTextField birthPlaceField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.enable(false)
			.build();
		birthPlaceField.setValue(dto.getClient().getBirthPlace());
		return birthPlaceField;
	}


	private ZDateField getBirthDateField() {
		ZDateField birthDateField = new ZDateField.Builder()
			.width(FIELD_WIDTH)
			.pattern(PaymentDTO.DATE_PATTERN)
			.enabled(false)
			.build();
		birthDateField.setValue(dto.getClient().getBirthDate());
		return birthDateField;
	}

}
