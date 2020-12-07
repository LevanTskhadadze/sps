package com.azry.sps.console.client.tabs.services.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.servicegroup.ServiceGroupDTO;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;

import java.util.List;


public class ServiceModifyWindow extends ZWindow {
	
	private static final int FIELD_WIDTH = 290;

	private boolean redactMode = false;

	private ZTextField nameField;

	private ZSimpleComboBox<ServiceGroupDTO> serviceGroupField;

	private BigDecimalField minAmountField;

	private BigDecimalField maxAmountField;

	private ZTextField debtCodeField;

	private ZTextField payCodeField;

	private ZTextField IBANField;

	private ServiceDTO dto;

	private final TabPanel tabPanel;

	private final VerticalLayoutContainer formContainer = new VerticalLayoutContainer();

	private final ServiceChannelWindow channelContainer;

	private final ListStore<ServiceDTO> store;

	ActionMode actionMode;

	public ServiceModifyWindow(ServiceDTO dto, ListStore<ServiceDTO> store, ActionMode actionMode) {
		super(Mes.get("ofService") + " " + Mes.get(actionMode.name().toLowerCase()), 700, 500, false);

		this.actionMode = actionMode;
		if (dto != null) {
			redactMode = true;
			this.dto = dto;
		}
		else {
			this.dto = new ServiceDTO();
			this.dto.setName("");
			this.dto.setActive(false);
			this.dto.setId(0);
		}
		channelContainer = new ServiceChannelWindow(this.dto.getChannels(), this.dto.isAllChannels(), actionMode);
		tabPanel = new TabPanel();

		this.store = store;


		ZButton confirmButton = getConfirmButton();
		ZButton cancelButton = getCancelButton();
		buttonBar.setMinButtonWidth(75);
		getButtonBar().getElement().getStyle().setProperty("borderTop", "1px solid #3291D6");

		addButton(confirmButton);
		addButton(cancelButton);

		formContainer.setStyleName("editForm");

		constructFormTab();

		add(tabPanel, new MarginData(0));

		showInCenter();

		if (ActionMode.VIEW.equals(actionMode)) {
			disableFields();
		}
	}

	private String getRequiredFieldNotification() {
		return "<span style='color: red;'>*</span>";
	}

	private void constructFormTab(){
		formContainer.add(constructForm(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));

		tabPanel.add(formContainer, Mes.get("serviceInfo"));
		tabPanel.add(channelContainer, Mes.get("serviceInfo"));
	}

	private FlexTable constructForm() {
		final FlexTable formTable = new FlexTable();
		formTable.getElement().getStyle().setMarginTop(15, com.google.gwt.dom.client.Style.Unit.PX);
		formTable.getElement().getStyle().setMarginLeft(10, com.google.gwt.dom.client.Style.Unit.PX);

		formTable.setWidget(0, 0, new HTML(Mes.get("name") + getRequiredFieldNotification() + ":"));
		formTable.setWidget(1, 0, new HTML(Mes.get("serviceGroup") + getRequiredFieldNotification() + ":"));
		formTable.setWidget(2, 0, new HTML(Mes.get("minAmount") + getRequiredFieldNotification() + ":"));
		formTable.setWidget(3, 0, new HTML(Mes.get("maxAmount") + getRequiredFieldNotification() + ":"));
		formTable.setWidget(4, 0, new HTML(Mes.get("serviceDebtCode") + getRequiredFieldNotification() + ":"));
		formTable.setWidget(5, 0, new HTML(Mes.get("servicePayCode") + getRequiredFieldNotification() + ":"));
		formTable.setWidget(6, 0, new HTML(Mes.get("providerAccountIban") + getRequiredFieldNotification() + ":"));
		formTable.getWidget(1, 0).setWidth("220px");

		formTable.setWidget(0, 1, getNameField());
		ServicesFactory.getServiceGroupService().getFilteredServiceGroups("", new ServiceCallback<List<ServiceGroupDTO>>(this) {
			@Override
			public void onServiceSuccess(List<ServiceGroupDTO> result) {
				serviceGroupField.addAll(result);
				if (redactMode) {
					for(ServiceGroupDTO serviceGroup : result) {

						if (dto.getGroupId() == serviceGroup.getId()) {
							serviceGroupField.setValue(serviceGroup);
						}
					}
				}
			}
		});
		formTable.setWidget(1, 1, getServiceGroupField());
		formTable.setWidget(2, 1, getMinAmountField());
		formTable.setWidget(3, 1, getMaxAmountField());
		formTable.setWidget(4, 1, getDebtCodeField());
		formTable.setWidget(5, 1, getPayCodeField());
		formTable.setWidget(6, 1, getIBANField());
		formTable.setCellSpacing(4);

		return formTable;
	}


	private ZTextField getNameField() {
		nameField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.build();
		if (redactMode) {
			nameField.setValue(dto.getName());
		}
		return nameField;
	}

	private ZSimpleComboBox<ServiceGroupDTO> getServiceGroupField() {
		 serviceGroupField = new ZSimpleComboBox.Builder<ServiceGroupDTO>()
			 .keyProvider(new ModelKeyProvider<ServiceGroupDTO>() {
				 @Override
				 public String getKey(ServiceGroupDTO item) {
					 return String.valueOf(item.getId());
				 }
			 })
			 .labelProvider(new LabelProvider<ServiceGroupDTO>() {
				 @Override
				 public String getLabel(ServiceGroupDTO item) {
					 return item.getName();
				 }
			 })
			 .width(FIELD_WIDTH)
			 .noSelectionLabel("")
			 .build();

		return serviceGroupField;
	}

	private BigDecimalField getMinAmountField() {
		minAmountField = new BigDecimalField();
		minAmountField.setWidth(FIELD_WIDTH);
		if (redactMode) {
			minAmountField.setValue(dto.getMinAmount());
		}
		return minAmountField;
	}

	private BigDecimalField getMaxAmountField() {
		maxAmountField = new BigDecimalField();
		maxAmountField.setWidth(FIELD_WIDTH);
		if (redactMode) {
			maxAmountField.setValue(dto.getMaxAmount());
		}
		return maxAmountField;
	}

	private ZTextField getDebtCodeField() {
		debtCodeField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.build();
		if (redactMode) {
			debtCodeField.setValue(dto.getServiceDebtCode());
		}
		return debtCodeField;
	}

	private ZTextField getPayCodeField() {
		payCodeField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.build();
		if (redactMode) {
			payCodeField.setValue(dto.getServicePayCode());
		}
		return payCodeField;
	}

	private ZTextField getIBANField() {
		IBANField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.build();
		if (redactMode) {
			IBANField.setValue(dto.getProviderAccountIBAN());
		}
		return IBANField;
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

	private boolean validate() {
		boolean good = true;

		if (nameField.getCurrentValue() == null || nameField.getCurrentValue().equals("")) {
			good = false;
			nameField.markInvalid(Mes.get("requiredField"));
		}


		if (minAmountField.getCurrentValue() == null) {
			good = false;
			minAmountField.markInvalid(Mes.get("requiredField"));
		}

		if (serviceGroupField.getCurrentValue() == null || serviceGroupField.getCurrentValue().equals("")) {
			good = false;
			serviceGroupField.markInvalid(Mes.get("requiredField"));
		}

		if (debtCodeField.getCurrentValue() == null || debtCodeField.getCurrentValue().equals("")) {
			good = false;
			debtCodeField.markInvalid(Mes.get("requiredField"));
		}

		if (maxAmountField.getCurrentValue() == null) {
			good = false;
			maxAmountField.markInvalid(Mes.get("requiredField"));
		}

		if (payCodeField.getCurrentValue() == null || payCodeField.getCurrentValue().equals("")) {
			good = false;
			payCodeField.markInvalid(Mes.get("requiredField"));
		}

		if (IBANField.getCurrentValue() == null || IBANField.getCurrentValue().equals("")) {
			good = false;
			IBANField.markInvalid(Mes.get("requiredField"));
		}


		if (good && minAmountField.getCurrentValue().compareTo(maxAmountField.getCurrentValue()) > 0) {
			minAmountField.markInvalid(Mes.get("invalidAmountMinMaxValues"));
			maxAmountField.markInvalid(Mes.get("invalidAmountMinMaxValues"));
			good = false;
		}
		if (!good) {
			tabPanel.setActiveWidget(formContainer);
		}
		if (good && !channelContainer.isValid()) {
			tabPanel.setActiveWidget(channelContainer);
		}
		return good && channelContainer.isValid();
	}

	private void retrieveInfo(ServiceDTO dto, ServiceChannelWindow.ChannelInfo info) {
		dto.setName(nameField.getCurrentValue());
		dto.setProviderAccountIBAN(IBANField.getCurrentValue());
		dto.setServicePayCode(payCodeField.getCurrentValue());
		dto.setServiceDebtCode(debtCodeField.getCurrentValue());
		dto.setMinAmount(minAmountField.getCurrentValue());
		dto.setMaxAmount(maxAmountField.getCurrentValue());
		dto.setAllChannels(info.isAllChannels());
		dto.setGroupId(serviceGroupField.getCurrentValue().getId());
		dto.setChannels(info.getChannels());

	}
	private boolean doRedact() {
		if(!validate()) return false;

		ServiceChannelWindow.ChannelInfo info = channelContainer.getChannelInfos();

		retrieveInfo(dto, info);

		ServicesFactory.getServiceTabService().editService(dto,
			new ServiceCallback<ServiceDTO>(this) {

				@Override
				public void onServiceSuccess(ServiceDTO newDTO) {
					dto = newDTO;
					store.update(dto);
				}
			});
		return true;
	}

	private boolean doAdd() {
		if(!validate()) {
			return false;
		}

		ServiceChannelWindow.ChannelInfo info = channelContainer.getChannelInfos();
		dto = new ServiceDTO();
		retrieveInfo(dto, info);

		ServicesFactory.getServiceTabService().editService(
			dto,
			new ServiceCallback<ServiceDTO>(this) {
				@Override
				public void onServiceSuccess(ServiceDTO ServiceDTO) {
					store.add(ServiceDTO);
				}
			});

		return true;
	}


	private ZButton getConfirmButton() {
		return new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().floppy_o_white())
			.visible(!ActionMode.VIEW.equals(actionMode))
			.text(Mes.get("save"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					if (redactMode) {
						if(!doRedact()) return;
					}
					else{
						if(!doAdd()) return;
					}

					hide();
				}
			})
			.build();
	}

	private void disableFields() {
		nameField.disable();
		serviceGroupField.disable();
		minAmountField.disable();
		maxAmountField.disable();
		debtCodeField.disable();
		payCodeField.disable();
		IBANField.disable();
	}
}
