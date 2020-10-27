package com.azry.sps.console.client.tabs.services.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.users.widgets.UserGroupTab;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.servicegroup.ServiceGroupDTO;
import com.azry.sps.console.shared.dto.services.ServiceDto;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


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

	private ServiceDto dto;

	private final TabPanel tabPanel;

	private final VerticalLayoutContainer formContainer = new VerticalLayoutContainer();

	private final ServiceChannelWindow channelContainer;

	private final ListStore<ServiceDto> store;



	public ServiceModifyWindow(ServiceDto dto, ListStore<ServiceDto> store) {
		super();
		channelContainer = new ServiceChannelWindow(dto.getChannels(), new ArrayList<ChannelDTO>());
		tabPanel = new TabPanel();

		this.store = store;

		if (dto != null) {
			redactMode = true;
			this.dto = dto;
		}
		else {
			this.dto = new ServiceDto();
			this.dto.setName("");
			this.dto.setActive(false);
			this.dto.setId(0);
		}



		ZButton confirmButton = getConfirmButton();
		ZButton cancelButton = getCancelButton();
		buttonBar.setMinButtonWidth(75);
		getButtonBar().getElement().getStyle().setProperty("borderTop", "1px solid #3291D6");

		addButton(confirmButton);
		addButton(cancelButton);

		formContainer.setStyleName("editForm");

		constructFormTab();

		add(tabPanel, new MarginData(0));

		setHeight("500px");
		setWidth("550px");
		String header = Mes.get("systemParam") + " " + (redactMode ? Mes.get("redact") : Mes.get("addEntry"));
		setHeadingText(header);
		showInCenter();
	}

	private String getRequiredFieldNotification() {
		return "<span style='color: red;'>*</span>";
	}

	private void constructFormTab(){
		formContainer.add(constructForm());

		tabPanel.add(formContainer, Mes.get("serviceInfo"));
		tabPanel.add(formContainer, Mes.get("serviceInfo"));


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
		ServicesFactory.getServiceGroupService().getFilteredServiceGroups("", new ServiceCallback<List<ServiceGroupDTO>>() {
			@Override
			public void onServiceSuccess(List<ServiceGroupDTO> result) {
				formTable.setWidget(1, 1, getServiceGroupField(result));
			}
		});
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

	private ZSimpleComboBox<ServiceGroupDTO> getServiceGroupField(List<ServiceGroupDTO> serviceGroups) {
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
			 .values(serviceGroups)
			 .width(FIELD_WIDTH)
			 .noSelectionLabel("")
			 .build();

		if (redactMode) {
			for(ServiceGroupDTO serviceGroup : serviceGroups) {

				if (dto.getGroupId() == serviceGroup.getId()) {
					serviceGroupField.setValue(serviceGroup);
				}
			}
		}
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

		if(nameField.getValue() == null || nameField.getValue().equals("")) {
			good = false;
			nameField.markInvalid(Mes.get("requiredField"));
		}

		if(serviceGroupField.getValue() == null || serviceGroupField.getValue().equals("")) {
			good = false;
			serviceGroupField.markInvalid(Mes.get("requiredField"));
		}

		if(minAmountField.getValue() == null || minAmountField.getValue().equals("")) {
			good = false;
			minAmountField.markInvalid(Mes.get("requiredField"));
		}

		if(maxAmountField.getValue() == null || maxAmountField.getValue().equals("")) {
			good = false;
			maxAmountField.markInvalid(Mes.get("requiredField"));
		}

		if(debtCodeField.getValue() == null || debtCodeField.getValue().equals("")) {
			good = false;
			debtCodeField.markInvalid(Mes.get("requiredField"));
		}

		if(payCodeField.getValue() == null || payCodeField.getValue().equals("")) {
			good = false;
			payCodeField.markInvalid(Mes.get("requiredField"));
		}

		if(IBANField.getValue() == null || IBANField.getValue().equals("")) {
			good = false;
			IBANField.markInvalid(Mes.get("requiredField"));
		}


		return good;
	}

	private boolean doRedact() {
		if(!validate()) return false;

		dto.setName(nameField.getValue());
		dto.setProviderAccountIBAN(IBANField.getValue());
		dto.setServicePayCode(payCodeField.getValue());
		dto.setServiceDebtCode(debtCodeField.getValue());
		dto.setMinAmount(minAmountField.getValue());
		dto.setMaxAmount(maxAmountField.getValue());
		dto.setGroupId(serviceGroupField.getValue().getId());
		dto.setLastUpdateTime(new Date());

		Logger logger = java.util.logging.Logger.getLogger("NameOfYourLogger");
		logger.log(Level.SEVERE, "" + dto.getVersion());
		ServicesFactory.getServiceTabService().editService(dto,
			new ServiceCallback<ServiceDto>() {

				@Override
				public void onServiceSuccess(ServiceDto newDto) {
					dto = newDto;
					store.update(dto);
				}
			});
		return true;
	}

	private boolean doAdd() {
		if(!validate()) {
			return false;
		}
		dto = new ServiceDto();
		dto.setName(nameField.getValue());
		dto.setGroupId(serviceGroupField.getValue().getId());
		dto.setMinAmount(minAmountField.getValue());
		dto.setMaxAmount(maxAmountField.getValue());
		dto.setServiceDebtCode(debtCodeField.getValue());
		dto.setServicePayCode(payCodeField.getValue());
		dto.setProviderAccountIBAN(IBANField.getValue());
		dto.setLastUpdateTime(new Date());
		dto.setCreateTime(new Date());

		ServicesFactory.getServiceTabService().editService(
			dto,
			new ServiceCallback<ServiceDto>() {
				@Override
				public void onServiceSuccess(ServiceDto ServiceDto) {
					store.add(ServiceDto);
				}
			});

		return true;
	}


	private ZButton getConfirmButton() {
		return new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().floppy_o_white())
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
}
