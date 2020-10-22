package com.azry.sps.console.client.tabs.servicegroup;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZFieldLabel;
import com.azry.gxt.client.zcomp.ZNumberField;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.servicegroup.ServiceGroupDTO;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;

public abstract class ServiceGroupWindow extends ZWindow {

	private static final String WINDOW_BOTTOM_BORDER_STYLE = "1px solid #3291D6";

	private ServiceGroupDTO serviceGroupDTO;

	private final VerticalLayoutContainer container = new VerticalLayoutContainer();

	private ZTextField nameField;

	private ZNumberField<Long> priorityField;

	ZButton saveButton;

	ZButton cancelButton;

	public ServiceGroupWindow(ServiceGroupDTO serviceGroupDTO, ActionMode actionMode) {
		super(Mes.get("ofServiceGroup") + " " + Mes.get("ActionMode_" + actionMode), 500, -1, false);
		this.serviceGroupDTO = serviceGroupDTO;

		initFields();
		if (serviceGroupDTO != null) {
			setFieldValues();
		}

		add(container);

		initButtons();

		addBottomHorizontalLine();

	}


	private void initFields() {

		nameField = new ZTextField.Builder()
			.maxLength(300)
			.required(true)
			.build();

		priorityField = new ZNumberField.Builder<>(new NumberPropertyEditor.LongPropertyEditor()).required().build();
		VerticalLayoutContainer.VerticalLayoutData layoutData = new VerticalLayoutContainer.VerticalLayoutData(1, -1);

		container.add(getFieldLabel(nameField, "name", true), layoutData);
		container.add(getFieldLabel(priorityField, "priority", true), layoutData);
	}



	private void setFieldValues() {
		nameField.setValue(serviceGroupDTO.getName());
		priorityField.setValue(serviceGroupDTO.getPriority());
	}

	protected  void initButtons(){
		saveButton = new ZButton.Builder()
			.text(Mes.get("save"))
			.icon(FAIconsProvider.getIcons().floppy_o_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					if (isValid()) {
						ServicesFactory.getServiceGroupService().updateServiceGroup(getServiceGroupForUpdate(), new ServiceCallback<ServiceGroupDTO>(ServiceGroupWindow.this) {
							@Override
							public void onServiceSuccess(ServiceGroupDTO result) {
								hide();
								onSave(result);
							}
						});
					}
				}
			})
			.build();

		cancelButton = new ZButton.Builder()
			.text(Mes.get("cancel"))
			.icon(FAIconsProvider.getIcons().ban_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					hide();
				}
			})
			.build();

		addButton(saveButton);
		addButton(cancelButton);
	}

	private void addBottomHorizontalLine() {
		getButtonBar().getElement().getStyle().setProperty("borderTop", WINDOW_BOTTOM_BORDER_STYLE);
	}



	private ZFieldLabel getFieldLabel(IsWidget field, String labelKey, boolean required) {
		return new ZFieldLabel.Builder()
			.labelWidth(150)
			.label(Mes.get(labelKey))
			.field(field)
			.required(required)
			.requiredSignColor("red")
			.build();
	}

	private boolean isValid() {
		boolean result = nameField.isValid();
		result = priorityField.isValid() && result;
		return result;
	}

	private ServiceGroupDTO getServiceGroupForUpdate() {
		if (serviceGroupDTO == null) {
			serviceGroupDTO = new ServiceGroupDTO();
		}
		serviceGroupDTO.setName(nameField.getValue());
		serviceGroupDTO.setPriority(priorityField.getValue());
		return serviceGroupDTO;
	}

	public abstract void onSave(ServiceGroupDTO dto);
}
