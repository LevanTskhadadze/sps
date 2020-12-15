package com.azry.sps.console.client.tabs.systemparam.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDTO;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDTOType;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SystemParametersModifyWindow extends ZWindow {

	private boolean redactMode = false;

	private ZTextField codeField;

	private ZSimpleComboBox<SystemParameterDTOType> typeField;

	private Widget valueField;

	private TextArea descField;

	private final SystemParameterDTO dto;

	private final FlexTable formContainer = new FlexTable();

	private final ListStore<SystemParameterDTO> store;

	ActionMode actionMode;

	public SystemParametersModifyWindow(SystemParameterDTO dto, ListStore<SystemParameterDTO> store, ActionMode actionMode) {
		super(Mes.get("systemParam") + " " + Mes.get(actionMode.name().toLowerCase()), 490, -1, false);
		this.store = store;
		this.actionMode = actionMode;
		if (dto != null) {
			redactMode = true;
			this.dto = dto;
		} else {
			this.dto = new SystemParameterDTO();
			this.dto.setType(SystemParameterDTOType.STRING);
			this.dto.setDescription("");
			this.dto.setCode("");
			this.dto.setValue("");
		}
		this.setShadow(false);
		VerticalLayoutContainer mainContainer = new VerticalLayoutContainer();

		ZButton confirmButton = getConfirmButton();
		ZButton cancelButton = getCancelButton();
		buttonBar.setMinButtonWidth(75);
		getButtonBar().getElement().getStyle().setProperty("borderTop", "1px solid #3291D6");
		addButton(confirmButton);
		addButton(cancelButton);

		formContainer.setStyleName("editForm");

		constructForm(formContainer);

		mainContainer.add(formContainer);

		add(mainContainer);

		showInCenter();

		if (ActionMode.VIEW.equals(actionMode)) {
			disableFields();
		}


	}

	private String getRequiredFieldNotification() {
		return "<span style='color: red;'>*</span>";

	}

	private void constructForm(FlexTable formContainer) {
		formContainer.setWidget(0, 0, new HTML(Mes.get("code") + getRequiredFieldNotification() + ":"));
		formContainer.setWidget(1, 0, new HTML(Mes.get("type") + getRequiredFieldNotification() + ":"));
		formContainer.setWidget(2, 0, new HTML(Mes.get("value") + getRequiredFieldNotification() + ":"));
		formContainer.setWidget(3, 0, new HTML(Mes.get("description") + ":"));
		formContainer.getWidget(0, 0).setWidth("150px");

		formContainer.setWidget(0, 1, getCodeField());
		formContainer.setWidget(1, 1, getTypeField());
		formContainer.setWidget(2, 1, getValueField());
		formContainer.setWidget(3, 1, getDescriptionField());
	}

	private ZTextField getCodeField() {
		codeField = new ZTextField.Builder()
			.enable(!redactMode)
			.width(305)
			.build();
		codeField.setValue(dto.getCode());

		return codeField;
	}

	private ZSimpleComboBox<SystemParameterDTOType> getTypeField() {
		List<String> types = new ArrayList<>();
		types.add("string");
		types.add("integer");
		types.add("bool");

		String cur = types.get(dto.getType().ordinal());
		types.remove(cur);
		types.add(0, cur);

		typeField = new ZSimpleComboBox.Builder<SystemParameterDTOType>()
			.labelProvider(new LabelProvider<SystemParameterDTOType>() {
				@Override
				public String getLabel(SystemParameterDTOType item) {
					return Mes.get(item.name());
				}
			})
			.keyProvider(new ModelKeyProvider<SystemParameterDTOType>() {
				@Override
				public String getKey(SystemParameterDTOType item) {
					return item.name();
				}
			})
			.enableSorting(false)
			.values(Arrays.asList(SystemParameterDTOType.values()))
			.width(305)
			.editable(false)
			.build();

		typeField.addSelectionHandler(new ZSimpleComboBox.SelectionHandler<SystemParameterDTOType>() {
			@Override
			public void onSelection(SystemParameterDTOType s) {
				formContainer.setWidget(2, 1, getValueField());
			}
		});
		return typeField;
	}

	private IsWidget getValueField() {
		switch (typeField.getCurrentValue()) {
			case STRING:
				return getValueTextField();
			case INTEGER:
				return getValueNumberField();
			case BOOLEAN:
				return getValueCheckBox();
		}
		return null;
	}

	private IsWidget getValueCheckBox() {
		CheckBox checkBox = new CheckBox();
		if (dto.getType().equals(SystemParameterDTOType.BOOLEAN)) {
			checkBox.setValue(Boolean.parseBoolean(dto.getValue()));
		}
		if (ActionMode.VIEW.equals(actionMode)) {
			checkBox.setEnabled(false);
		}
		valueField = checkBox;
		return valueField;
	}

	private IsWidget getValueTextField() {
		TextArea textArea = new TextArea();
		if (dto.getType().equals(SystemParameterDTOType.STRING)) {
			textArea.setValue(dto.getValue());
		}

		textArea.setWidth("305px");
		textArea.setHeight("150px");
		if (ActionMode.VIEW.equals(actionMode)) {
			textArea.disable();
		}
		valueField = textArea;

		return valueField;
	}

	private IsWidget getValueNumberField() {
		IntegerField integerField = new IntegerField();
		if (dto.getType().equals(SystemParameterDTOType.INTEGER)) {
			integerField.setValue(Integer.parseInt(dto.getValue()));
		}
		integerField.setWidth("305px");
		if (ActionMode.VIEW.equals(actionMode)) {
			integerField.disable();
		}
		valueField = integerField;
		return valueField;
	}


	private TextArea getDescriptionField() {
		descField = new TextArea();
		descField.setValue(dto.getDescription());
		descField.setWidth("305px");
		descField.setHeight("50px");
		return descField;
	}

	private ZButton getCancelButton() {
		return new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().ban_white())
			.text(Mes.get("discard"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					hide();
				}
			})
			.build();
	}

	private String getValueFieldValue() {
		switch (typeField.getCurrentValue()) {
			case BOOLEAN:
				return ((CheckBox)valueField).getValue().toString();
			case INTEGER:
				return ((IntegerField)valueField).getCurrentValue() == null ? "" : ((IntegerField)valueField).getCurrentValue().toString();
			case STRING:
				return ((TextArea)valueField).getCurrentValue() == null ? "" : ((TextArea)valueField).getCurrentValue();
		}
		return null;
	}

	private void markValueField() {


		switch (typeField.getCurrentValue()) {
			case BOOLEAN:
				return;
			case INTEGER:
				((IntegerField)valueField).markInvalid("invalid");
				break;
			case STRING:
				((TextArea)valueField).markInvalid("invalid");
				break;
		}
	}

	private void retreiveFieldValues() {
		dto.setCode(codeField.getCurrentValue());
		dto.setDescription(descField.getCurrentValue());
		dto.setType(typeField.getCurrentValue());
		dto.setValue(getValueFieldValue());
	}

	private void doRedact() {
		retreiveFieldValues();
		ServicesFactory.getSystemParameterService().editParameter(dto,
			new ServiceCallback<Void>(this) {

				@Override
				public void onServiceSuccess(Void unused) {
					store.update(dto);
					hide();
				}
			});
	}
	private void doAdd() {
		retreiveFieldValues();
		ServicesFactory.getSystemParameterService().addParameter(dto,
			new ServiceCallback<SystemParameterDTO>(this) {

				@Override
				public void onServiceSuccess(SystemParameterDTO systemParameterDTO) {
					store.add(0, systemParameterDTO);
					hide();
				}
			});
	}


	private ZButton getConfirmButton() {
		return new ZButton.Builder()
			.visible(!ActionMode.VIEW.equals(actionMode))
			.icon(FAIconsProvider.getIcons().floppy_o_white())
			.text(Mes.get("save"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					boolean validation = true;
					if (getValueFieldValue() == null || getValueFieldValue().equals("")) {
						markValueField();
						validation = false;
					}

					if (codeField.getCurrentValue() == null || codeField.getCurrentValue().equals("")) {
						codeField.markInvalid("invalid");
						validation = false;
					}
					if (!validation) return;
					if (redactMode) {
						doRedact();
					}
					else{
						doAdd();
					}

				}
			})
			.build();
	}

	private void disableFields() {
		codeField.disable();
		typeField.disable();
		descField.disable();
	}
}
