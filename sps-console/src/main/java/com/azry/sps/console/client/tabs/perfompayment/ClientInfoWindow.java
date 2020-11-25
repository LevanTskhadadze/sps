package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZFieldLabel;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.utils.FormatDate;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

public class ClientInfoWindow extends ZWindow {

	private static final String WINDOW_BOTTOM_BORDER_STYLE = "1px solid #3291D6";

	private final VerticalLayoutContainer container = new VerticalLayoutContainer();

	ClientDTO clientDTO;

	private ZTextField personalNumberF;
	private ZTextField nameF;
	private ZTextField surnameF;
	private ZTextField birthDateF;
	private ZTextField birtPlaceF;

	ZButton closeB;


	public ClientInfoWindow(ClientDTO clientDTO) {
		super(Mes.get("clientInfo"), -1, -1, false);

		this.clientDTO = clientDTO;
		initFields();
		setFieldValues();
		addBottomHorizontalLine();
		add(container);
	}

	private void initFields() {
		personalNumberF = new ZTextField.Builder()
			.enable(false)
			.maxLength(350)
			.build();

		nameF = new ZTextField.Builder()
			.enable(false)
			.maxLength(350)
			.build();

		surnameF = new ZTextField.Builder()
			.enable(false)
			.maxLength(350)
			.build();

		birthDateF = new ZTextField.Builder()
			.enable(false)
			.maxLength(350)
			.build();

		birtPlaceF = new ZTextField.Builder()
			.enable(false)
			.maxLength(350)
			.build();

		closeB = new ZButton.Builder()
			.text(Mes.get("close"))
			.icon(FAIconsProvider.getIcons().ban_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					hide();
				}
			})
			.build();

		container.add(getFieldLabel(personalNumberF, "personalNumber"));
		container.add(getFieldLabel(nameF, "name"));
		container.add(getFieldLabel(surnameF, "surname"));
		container.add(getFieldLabel(birthDateF, "birthDate"));
		container.add(getFieldLabel(birtPlaceF, "birthPlace"));

		addButton(closeB);
	}

	private void setFieldValues() {
		personalNumberF.setValue(clientDTO.getPersonalNumber());
		nameF.setValue(clientDTO.getFirstName());
		surnameF.setValue(clientDTO.getLastName());
		birthDateF.setValue(FormatDate.formatDate(clientDTO.getBirthDate()));
		birtPlaceF.setValue(clientDTO.getBirthPlace());
	}

	private ZFieldLabel getFieldLabel(IsWidget field, String labelKey) {
		return new ZFieldLabel.Builder()
			.labelWidth(170)
			.label(Mes.get(labelKey))
			.field(field)
			.build();
	}

	private void addBottomHorizontalLine() {
		getButtonBar().getElement().getStyle().setProperty("borderTop", WINDOW_BOTTOM_BORDER_STYLE);
	}
}
