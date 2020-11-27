package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZFieldLabel;
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
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.error.ToolTipErrorHandler;

import java.util.Comparator;
import java.util.List;

public abstract class AddPaymentListEntryWindow extends ZWindow {

	private static final String WINDOW_BOTTOM_BORDER_STYLE = "1px solid #3291D6";

	private final VerticalLayoutContainer container;

	private ZSimpleComboBox<ServiceGroupDTO> serviceGroupsCB;
	private ZSimpleComboBox<ServiceDTO> serviceCB;
	private ZTextField abonentCodeF;
	private ZTextField abonenCodeInfo;

	private ZButton saveB;
	ZButton closeB;


	public AddPaymentListEntryWindow() {
		super(Mes.get("addTemplate"), 500, 220, false);
		container = new VerticalLayoutContainer();
		initFields();
		getData();
		initButtons();
		addBottomHorizontalLine();
		add(container);
		saveB.disable();
	}

	private void getData() {
		ServicesFactory.getServiceGroupService().getFilteredServiceGroups(null, new ServiceCallback<List<ServiceGroupDTO>>(this) {
			@Override
			public void onServiceSuccess(List<ServiceGroupDTO> result) {
				serviceGroupsCB.addAll(result);
			}
		});

		ServicesFactory.getServiceTabService().getAllActiveServices(new ServiceCallback<List<ServiceDTO>>(this) {
			@Override
			public void onServiceSuccess(List<ServiceDTO> result) {
				serviceCB.replaceAll(result);
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
			.noSelectionLabel(Mes.get("allServiceGroups"))
			.enableSorting(true)
			.comparator(new Comparator<ServiceGroupDTO>() {
				@Override
				public int compare(ServiceGroupDTO o1, ServiceGroupDTO o2) {
					return Long.compare(o1.getPriority(), o2.getPriority());
				}
			})
			.editable(false)
			.width(280)
			.build();

		serviceGroupsCB.addSelectionHandler(new ZSimpleComboBox.SelectionHandler<ServiceGroupDTO>() {
			@Override
			public void onSelection(ServiceGroupDTO dto) {
				onServiceGroupSelect();
			}
		});

		serviceCB = new ZSimpleComboBox.Builder<ServiceDTO>()
			.keyProvider(new ModelKeyProvider<ServiceDTO>() {
				@Override
				public String getKey(ServiceDTO dto) {
					return String.valueOf(dto.getId());
				}
			})
			.labelProvider(new LabelProvider<ServiceDTO>() {
				@Override
				public String getLabel(ServiceDTO dto) {
					return dto.getName();
				}
			})
			.enableSorting(false)
			.editable(false)
			.width(280)
			.required(true)
			.build();
		serviceCB.getCombo().getLoader().setReuseLoadConfig(false);


		abonentCodeF = new ZTextField.Builder()
			.width(255)
			.required(true)
			.keyPressHandler(new KeyPressHandler() {
				@Override
				public void onKeyPress(KeyPressEvent keyPressEvent) {
					if (saveB.isEnabled()) {
						saveB.disable();
						abonenCodeInfo.setValue(null);
						abonenCodeInfo.removeStyleName(abonenCodeInfo.getStyleName());
					}
				}
			})
			.build();

		abonentCodeF.setErrorSupport(new ToolTipErrorHandler(abonentCodeF));

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

		verifyAbonentCodeB.addStyleName("verify-abonent-code-button");

		VerticalLayoutContainer vLayoutWrapper = new VerticalLayoutContainer();
		HorizontalLayoutContainer abonentCodeHorizontalWrapper = new HorizontalLayoutContainer();
		vLayoutWrapper.add(getFieldLabel(abonentCodeF, "abonentCode", true));
		abonentCodeHorizontalWrapper.add(vLayoutWrapper);
		abonentCodeHorizontalWrapper.add(verifyAbonentCodeB);
		vLayoutWrapper.add(getVerifyInfoField());

		container.add(getFieldLabel(serviceGroupsCB, "serviceGroup", false));
		container.add(getFieldLabel(serviceCB, "service", true));
		container.add(abonentCodeHorizontalWrapper);
	}

	private void onServiceGroupSelect() {
		if (serviceGroupsCB.getCurrentValue() == null) {
			ServicesFactory.getServiceTabService().getAllActiveServices(new ServiceCallback<List<ServiceDTO>>(this) {
				@Override
				public void onServiceSuccess(List<ServiceDTO> result) {
					serviceCB.replaceAll(result);
				}
			});
		} else {
			ServicesFactory.getServiceTabService().getServicesByServiceGroup(serviceGroupsCB.getCurrentValue().getId(), new ServiceCallback<List<ServiceDTO>>(this) {
				@Override
				public void onServiceSuccess(List<ServiceDTO> result) {
					serviceCB.replaceAll(result);
				}
			});
		}
	}

	private void initButtons() {
		saveB = new ZButton.Builder()
			.text(Mes.get("save"))
			.icon(FAIconsProvider.getIcons().floppy_o_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					if (isValid()) {
						hide();
						onSave(getPaymentListEntryDTO(), serviceCB.getValue());
					}
				}
			})
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

		addButton(saveB);
		addButton(closeB);
	}

	private boolean isValid() {
		return serviceCB.isValid()
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
		abonenCodeInfo.addStyleName("abonent-info");
		Label emptyLabel = new Label("");
		emptyLabel.setWidth("175");
		wrapper.add(emptyLabel);
		wrapper.add(abonenCodeInfo);
		return wrapper;
	}

	public void verifyAbonentCode() {

		abonenCodeInfo.setStyleName("abonent-info-loading");
		abonenCodeInfo.setValue(" ");

		ServicesFactory.getProviderIntegrationService().getAbonent(serviceCB.getValue().getServiceDebtCode(), abonentCodeF.getValue(), new ServiceCallback<AbonentInfoDTO>(false) {
			@Override
			public void onServiceSuccess(AbonentInfoDTO result) {
				if (result.getStatus().equals(GetInfoStatusDTO.SUCCESS)) {
					abonenCodeInfo.setStyleName("abonent-info-success");
					abonenCodeInfo.addStyleName("abonent-info");

					abonenCodeInfo.setValue(result.getAbonentInfo());
					saveB.enable();
				}
				else {
					abonenCodeInfo.setStyleName("abonent-info-failure");
					abonenCodeInfo.setValue(Mes.get("abonentNotFound"));
				}
			}

			@Override
			public void onFailure(Throwable th) {
				abonenCodeInfo.setStyleName("abonent-info-connection-error");
				abonenCodeInfo.setValue(Mes.get("noProviderConnection"));
			}
		});

	}

	private PaymentListEntryDTO getPaymentListEntryDTO() {
		PaymentListEntryDTO dto = new PaymentListEntryDTO();
		dto.setServiceId(serviceCB.getValue().getId());
		dto.setAbonentCode(String.valueOf(abonentCodeF.getValue()));
		return dto;
	}

	public abstract void onSave(PaymentListEntryDTO dto, ServiceDTO serviceDto);
}
