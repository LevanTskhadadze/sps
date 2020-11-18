package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZFieldLabel;
import com.azry.gxt.client.zcomp.ZNumberField;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.paymentList.PaymentListEntryDTO;
import com.azry.sps.console.shared.dto.providerintegration.AbonentInfoDTO;
import com.azry.sps.console.shared.dto.providerintegration.GetInfoStatusDTO;
import com.azry.sps.console.shared.dto.servicegroup.ServiceGroupDTO;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.error.ToolTipErrorHandler;

import java.util.Comparator;
import java.util.List;

public abstract class AddPaymentListEntryWindow extends ZWindow {

	private static final String WINDOW_BOTTOM_BORDER_STYLE = "1px solid #3291D6";

	private final VerticalLayoutContainer container;

	private ZSimpleComboBox<ServiceGroupDTO> serviceGroupsCB;

	private ZSimpleComboBox<ServiceDto> serviceCB;

	private ZNumberField<Long> abonentCodeF;

	private ZTextField abonenCodeInfo;

	private ZButton saveButton;


	public AddPaymentListEntryWindow() {
		super(Mes.get("addTemplate"), 500, 220, false);
		container = new VerticalLayoutContainer();
		initFields();
		getServiceGroups();
		initButtons();
		addBottomHorizontalLine();
		add(container);
		serviceCB.disable();
		saveButton.disable();
	}

	private void getServiceGroups() {
		ServicesFactory.getServiceGroupService().getFilteredServiceGroups(null, new ServiceCallback<List<ServiceGroupDTO>>() {
			@Override
			public void onServiceSuccess(List<ServiceGroupDTO> result) {
				ServiceGroupDTO allServiceGroups = new ServiceGroupDTO();
				allServiceGroups.setId(-1);
				allServiceGroups.setName(Mes.get("allServiceGroups"));
				serviceGroupsCB.add(allServiceGroups);
				serviceGroupsCB.addAll(result);
			}
		});
	}


	private void initFields() {
		serviceGroupsCB = new ZSimpleComboBox.Builder<ServiceGroupDTO>()
			.keyProvider(new ModelKeyProvider<ServiceGroupDTO>() {
				@Override
				public String getKey(ServiceGroupDTO dto) {
					return String.valueOf(dto.getId());
				}
			})
			.labelProvider(new LabelProvider<ServiceGroupDTO>() {
				@Override
				public String getLabel(ServiceGroupDTO dto) {
					return dto.getName();
				}
			})
			.emptyText(Mes.get("chooseServicesGroups"))
			.enableSorting(true)
			.comparator(new Comparator<ServiceGroupDTO>() {
				@Override
				public int compare(ServiceGroupDTO o1, ServiceGroupDTO o2) {
					return Long.compare(o1.getPriority(), o2.getPriority());
				}
			})
			.editable(false)
			.width(280)
			.required(true)
			.build();

		serviceGroupsCB.addSelectionHandler(new ZSimpleComboBox.SelectionHandler<ServiceGroupDTO>() {
			@Override
			public void onSelection(ServiceGroupDTO dto) {
				if (dto.getId() == -1) {
					ServicesFactory.getServiceTabService().getAllServices(new ServiceCallback<List<ServiceDto>>() {
						@Override
						public void onServiceSuccess(List<ServiceDto> result) {
							serviceCB.replaceAll(result);
						}
					});
				}
				else {
					ServicesFactory.getServiceTabService().getServicesByServiceGroup(dto.getId(), new ServiceCallback<List<ServiceDto>>() {
						@Override
						public void onServiceSuccess(List<ServiceDto> result) {

							serviceCB.replaceAll(result);
						}
					});
				}
				if(!serviceCB.isEnabled()) {
					serviceCB.enable();
				}
			}
		});

		serviceCB = new ZSimpleComboBox.Builder<ServiceDto>()
			.keyProvider(new ModelKeyProvider<ServiceDto>() {
				@Override
				public String getKey(ServiceDto dto) {
					return String.valueOf(dto.getId());
				}
			})
			.labelProvider(new LabelProvider<ServiceDto>() {
				@Override
				public String getLabel(ServiceDto dto) {
					return dto.getName();
				}
			})
			.enableSorting(false)
			.editable(false)
			.width(280)
			.required(true)
			.build();
		serviceCB.getCombo().getLoader().setReuseLoadConfig(false);


		abonentCodeF = new ZNumberField.Builder<>(new NumberPropertyEditor.LongPropertyEditor()).width(255).required().build();

		ZButton verifyAbonentCodeB = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().search())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					if (isValid()) {
						verifyAbonentCode();
					}
				}
			})
			.build();
		abonentCodeF.setErrorSupport(new ToolTipErrorHandler(abonentCodeF));

		verifyAbonentCodeB.addStyleName("verify-abonent-code-button");

		VerticalLayoutContainer vLayoutWrapper = new VerticalLayoutContainer();
		HorizontalLayoutContainer abonentCodeHorizontalWrapper = new HorizontalLayoutContainer();
		vLayoutWrapper.add(getFieldLabel(abonentCodeF, "abonentCode", true));
		abonentCodeHorizontalWrapper.add(vLayoutWrapper);
		abonentCodeHorizontalWrapper.add(verifyAbonentCodeB);
		vLayoutWrapper.add(getVerifyInfoField());

		container.add(getFieldLabel(serviceGroupsCB, "serviceGroup", true));
		container.add(getFieldLabel(serviceCB, "service", true));
		container.add(abonentCodeHorizontalWrapper);
	}

	private void initButtons() {
		saveButton = new ZButton.Builder()
			.text(Mes.get("save"))
			.icon(FAIconsProvider.getIcons().floppy_o_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					if (isValid()) {
						hide();
						onSave(getPaymentListEntryDTO());
					}
				}
			})
			.build();

		ZButton cancelButton = new ZButton.Builder()
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

	private boolean isValid() {
		return serviceGroupsCB.isValid()
			&& serviceCB.isValid()
			&& abonentCodeF.isValid();
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

	public Widget getVerifyInfoField() {
		HorizontalLayoutContainer wrapper = new HorizontalLayoutContainer();
		abonenCodeInfo = new ZTextField.Builder()
			.emptyText(Mes.get("abonentInfo"))
			.width(280)
			.build();
		abonenCodeInfo.disable();
		Label emptyLabel = new Label("");
		emptyLabel.setWidth("175");
		wrapper.add(emptyLabel);
		wrapper.add(abonenCodeInfo);
		return wrapper;
	}

	public void verifyAbonentCode() {

		ServicesFactory.getProviderIntegrationService().getAbonent(serviceCB.getValue().getServiceDebtCode(), abonentCodeF.getValue(), new ServiceCallback<AbonentInfoDTO>(this) {
			@Override
			public void onServiceSuccess(AbonentInfoDTO result) {
				if (result.getStatus().equals(GetInfoStatusDTO.SUCCESS)) {
					abonenCodeInfo.setStyleName("abonent-info-success");
					abonenCodeInfo.setText(result.getAbonentInfo());
					saveButton.enable();
				}
				else if (result.getStatus().equals(GetInfoStatusDTO.ABONENT_NOT_FOUND)) {
					abonenCodeInfo.setStyleName("abonent-info-failure");
					abonenCodeInfo.setText(Mes.get(GetInfoStatusDTO.ABONENT_NOT_FOUND.name()));
				}
				else {
					abonenCodeInfo.setStyleName("abonent-info-failure");
					abonenCodeInfo.setText(Mes.get(GetInfoStatusDTO.BAD_REQUEST.name()));
				}
			}
		});

	}

	private PaymentListEntryDTO getPaymentListEntryDTO() {
		PaymentListEntryDTO dto = new PaymentListEntryDTO();
		dto.setServiceId(serviceCB.getValue().getId());
		dto.setAbonentCode(String.valueOf(abonentCodeF.getValue()));
		return dto;
	}

	public abstract void onSave(PaymentListEntryDTO dto);
}
