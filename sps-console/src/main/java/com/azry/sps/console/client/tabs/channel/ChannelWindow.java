package com.azry.sps.console.client.tabs.channel;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZFieldLabel;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.channel.FiServiceUnavailabilityActionDTO;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.Arrays;

public abstract class ChannelWindow extends ZWindow {

	private static final String WINDOW_BOTTOM_BORDER_STYLE = "1px solid #3291D6";


	private final VerticalLayoutContainer container = new VerticalLayoutContainer();

	private ZTextField nameField;
	ZSimpleComboBox<FiServiceUnavailabilityActionDTO> fiServiceUnavailabilityActionCombo;

	private ZButton saveButton;
	private ZButton cancelButton;

	private ChannelDTO channelDTO;

	public ChannelWindow(ChannelDTO channelDTO, ActionMode actionMode) {
		super(Mes.get("ofChannel") + " " + Mes.get("ActionMode_" + actionMode), 600, -1, false);
		this.channelDTO = channelDTO;

		initFields();
		if (channelDTO != null) {
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

		fiServiceUnavailabilityActionCombo = new ZSimpleComboBox.Builder<FiServiceUnavailabilityActionDTO>()
			.keyProvider(new ModelKeyProvider<FiServiceUnavailabilityActionDTO>() {
				@Override
				public String getKey(FiServiceUnavailabilityActionDTO dto) {
					return dto.name();
				}
			})
			.labelProvider(new LabelProvider<FiServiceUnavailabilityActionDTO>() {
				@Override
				public String getLabel(FiServiceUnavailabilityActionDTO dto) {
					return Mes.get(dto.name());
				}
			})
			.values(Arrays.asList(FiServiceUnavailabilityActionDTO.values()))
			.enableSorting(false)
			.editable(false)
			.width(200)
			.required(true)
			.build();

		VerticalLayoutContainer.VerticalLayoutData layoutData = new VerticalLayoutContainer.VerticalLayoutData(1, -1);

		container.add(getFieldLabel(nameField, "name", true), layoutData);
		container.add(getFieldLabel(fiServiceUnavailabilityActionCombo, "fiServiceUnavailabilityAction", false), layoutData);
	}



	private void setFieldValues() {
		nameField.setValue(channelDTO.getName());
		fiServiceUnavailabilityActionCombo.setValue(channelDTO.getFiServiceUnavailabilityAction());
	}

	protected  void initButtons(){
		saveButton = new ZButton.Builder()
			.text(Mes.get("save"))
			.icon(FAIconsProvider.getIcons().floppy_o_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					if (isValid()) {
						ServicesFactory.getChannelService().updateChannel(getServiceGroupForUpdate(), new ServiceCallback<ChannelDTO>(ChannelWindow.this) {
							@Override
							public void onServiceSuccess(ChannelDTO result) {
								hide();
								onSave(result);
							}
						});
					}
				}
			})
			.build();

		cancelButton = new ZButton.Builder()
			.text(Mes.get("close"))
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
			.labelWidth(170)
			.label(Mes.get(labelKey))
			.field(field)
			.required(required)
			.requiredSignColor("red")
			.build();
	}

	private boolean isValid() {
		boolean result = nameField.isValid();
		result = fiServiceUnavailabilityActionCombo.isValid() && result;
		return result;
	}

	private ChannelDTO getServiceGroupForUpdate() {
		if (channelDTO == null) {
			channelDTO = new ChannelDTO();
			channelDTO.setActive(true);
		}
		channelDTO.setName(nameField.getCurrentValue());
		channelDTO.setFiServiceUnavailabilityAction(fiServiceUnavailabilityActionCombo.getCurrentValue());
		return channelDTO;
	}

	public abstract void onSave(ChannelDTO dto);
}

