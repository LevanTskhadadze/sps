package com.azry.sps.console.client.tabs.systemparam.table;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDto;
import com.azry.sps.console.shared.dto.systemparameter.SystemParameterDtoType;
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
import com.azry.sps.console.client.utils.Mes;
import com.sencha.gxt.widget.core.client.form.IntegerField;
import com.sencha.gxt.widget.core.client.form.TextArea;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SystemParametersModifyWindow extends ZWindow {

	private boolean redactMode = false;

	private ZTextField codeField;

	private ZSimpleComboBox<SystemParameterDtoType> typeField;

	private Widget valueField;

	private TextArea descField;

	private final SystemParameterDto dto;

	private final FlexTable formContainer = new FlexTable();

	private final ListStore<SystemParameterDto> store;

	public SystemParametersModifyWindow(SystemParameterDto dto, ListStore<SystemParameterDto> store) {
		super();
		this.store = store;

		if (dto != null) {
			redactMode = true;
			this.dto = dto;
		}
		else {
			this.dto = new SystemParameterDto();
			this.dto.setType(SystemParameterDtoType.STRING);
			this.dto.setDescription("");
			this.dto.setCode("");
			this.dto.setValue("");
		}

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

		setHeight("400px");
		setWidth("490px");
		String header = Mes.get("systemParam") + " " + (redactMode ? Mes.get("redact") : Mes.get("addEntry"));
		setHeadingText(header);
		showInCenter();
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
			.emptyText(dto.getCode()==null ? "" : dto.getValue())
			.enable(!redactMode)
			.width(305)
			.build();
		codeField.setValue(dto.getCode());

		return codeField;
	}

	private ZSimpleComboBox<SystemParameterDtoType> getTypeField() {
		List<String> types = new ArrayList<>();
		types.add("string");
		types.add("integer");
		types.add("bool");

		String cur = types.get(dto.getType().ordinal());
		types.remove(cur);
		types.add(0, cur);

		typeField = new ZSimpleComboBox.Builder<SystemParameterDtoType>()
			.labelProvider(new LabelProvider<SystemParameterDtoType>() {
				@Override
				public String getLabel(SystemParameterDtoType item) {
					return Mes.get(item.name());
				}
			})
			.keyProvider(new ModelKeyProvider<SystemParameterDtoType>() {
				@Override
				public String getKey(SystemParameterDtoType item) {
					return item.name();
				}
			})
			.enableSorting(false)
			.values(Arrays.asList(SystemParameterDtoType.values()))
			.width(305)
			.editable(false)
			.build();

		typeField.addSelectionHandler(new ZSimpleComboBox.SelectionHandler<SystemParameterDtoType>() {
			@Override
			public void onSelection(SystemParameterDtoType s) {
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
		if (dto.getType().equals(SystemParameterDtoType.BOOLEAN)) {
			checkBox.setValue(Boolean.parseBoolean(dto.getValue()));
		}

		valueField = checkBox;
		return valueField;
	}

	private IsWidget getValueTextField() {
		TextArea textArea = new TextArea();
		if (dto.getType().equals(SystemParameterDtoType.STRING)) {
			textArea.setValue(dto.getValue());
		}

		textArea.setWidth("305px");
		textArea.setHeight("150px");
		valueField = textArea;
		return valueField;
	}

	private IsWidget getValueNumberField() {
		IntegerField integerField = new IntegerField();
		if (dto.getType().equals(SystemParameterDtoType.INTEGER)) {
			integerField.setValue(Integer.parseInt(dto.getValue()));
		}
		integerField.setWidth("305px");
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
				return ((IntegerField)valueField).getCurrentValue().toString();
			case STRING:
				return ((TextArea)valueField).getCurrentValue();
		}
		return null;
	}

	private void markValueField() {
		switch (typeField.getCurrentValue()) {
			case BOOLEAN:
				return;
			case INTEGER:
				((IntegerField)valueField).markInvalid("invalid");
			case STRING:
				((TextArea)valueField).markInvalid("invalid");
		}
	}

	private void retreiveFieldValues() {
		dto.setCode(codeField.getCurrentValue());
		dto.setDescription(descField.getCurrentValue());
		dto.setType(typeField.getCurrentValue());
		dto.setValue(getValueFieldValue());
	}
	private void doRedact() {

		ServicesFactory.getSystemParameterService().editParameter(dto,
			new ServiceCallback<Void>() {

				@Override
				public void onServiceSuccess(Void unused) {
					final int index = store.indexOf(dto);
					store.update(dto);
				}
			});
	}

	private void doAdd() {
		retreiveFieldValues();
		ServicesFactory.getSystemParameterService().addParameter(dto,
			new ServiceCallback<SystemParameterDto>() {

				@Override
				public void onServiceSuccess(SystemParameterDto systemParameterDto) {
					store.add(systemParameterDto);
				}
			});
	}


	private ZButton getConfirmButton() {
		return new ZButton.Builder()
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
					if(!validation) return;
					if (redactMode) {
						doRedact();
					}
					else{
						doAdd();
					}

					hide();
				}
			})
			.build();
	}
}
