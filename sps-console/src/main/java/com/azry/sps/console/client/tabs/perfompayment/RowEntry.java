package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZNumberField;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.NumberFormatUtils;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.commission.CommissionRateTypeDTO;
import com.azry.sps.console.shared.dto.commission.clientcommission.ClientCommissionsDto;
import com.azry.sps.console.shared.dto.paymentList.PaymentListEntryDTO;
import com.azry.sps.console.shared.dto.providerintegration.AbonentInfoDTO;
import com.azry.sps.console.shared.dto.providerintegration.GetInfoStatusDTO;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.theme.neptune.client.base.button.Css3ButtonCellAppearance;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_CENTER;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_LEFT;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_RIGHT;

public class RowEntry {

	private int row;

	private final FlexTable table;

	private String abonentInfo;
	private BigDecimal debt;
	private BigDecimal commission;
	private ZNumberField<BigDecimal> amountF;
	private ZButton deleteB;


	private final PaymentListEntryDTO paymentListEntryDTO;
	private ServiceDto serviceDto;
	private ClientCommissionsDto clientCommissionsDto;
	private final PaymentListTable paymentListTable;


	private Boolean loaded;
	private boolean commissionVerified;

	public RowEntry(FlexTable table, PaymentListEntryDTO paymentListEntryDTO, int row, PaymentListTable paymentListTable) {
		this.row = row;
		loaded = false;
		commissionVerified = false;
		abonentInfo = "";
		commission = new BigDecimal("0.00");
		this.paymentListTable = paymentListTable;
		this.paymentListEntryDTO = paymentListEntryDTO;
		this.table = table;
		buildAmountField();
		buildDeleteButton();
		ServicesFactory.getServiceTabService().getService(paymentListEntryDTO.getServiceId(), new ServiceCallback<ServiceDto>() {
				@Override
				public void onServiceSuccess(ServiceDto result) {
					serviceDto = result;
					buildRow();
				}
			});
		table.getRowFormatter().setStyleName(row,"payment-entry-list-row");
	}

	public void buildRow(){
		loadCellData();
		setCommissionCell(new Label(""), "payment-table-loading", ALIGN_RIGHT, true);
		TableUtils.setCell(table, row, 5, amountF, null,  null, null, false);
		TableUtils.setCell(table, row, 6, deleteB, null,  null, null, false);
	}

	public void loadCellData() {
		setServiceCell();
		setAbonentCells(abonentInfo, "","payment-table-loading", ALIGN_CENTER, true);

		ServicesFactory.getProviderIntegrationService().getAbonent(serviceDto.getServiceDebtCode(), Long.valueOf(paymentListEntryDTO.getAbonentCode()), new AsyncCallback<AbonentInfoDTO>() {
			@Override
			public void onFailure(Throwable throwable) {
				setAbonentCells(Mes.get("noProviderConnection"), "?", "payment-table-connection-error", ALIGN_CENTER, false);
				amountF.disable();
				loaded = false;
			}

			@Override
			public void onSuccess(AbonentInfoDTO abonentInfoDTO) {
				abonentInfo = abonentInfoDTO.getAbonentInfo();
				debt = abonentInfoDTO.getDebt();
				updateRow(abonentInfoDTO.getStatus());
			}
		});
		ServicesFactory.getClientCommissionsService().getClientCommission(serviceDto.getId(), new AsyncCallback<ClientCommissionsDto>() {
			@Override
			public void onFailure(Throwable throwable) {
				setCommissionCell(getCommissionCellErrorWidget(), "payment-table-loaded-not-found", ALIGN_CENTER, false);
				amountF.disable();
			}

			@Override
			public void onSuccess(ClientCommissionsDto dto) {
				clientCommissionsDto = dto;
				setCommissionCell(new Label(NumberFormatUtils.format(commission)), "payment-table-loaded", ALIGN_RIGHT, false);
			}
		});
	}

	private void updateRow(GetInfoStatusDTO status) {
		switch(status) {
			case SUCCESS:
				setAbonentCells(abonentInfo, NumberFormatUtils.format(debt) ,"payment-table-loaded", ALIGN_RIGHT, false);
				loaded = true;
				paymentListTable.updateAggregateDebt();
				break;
			case BAD_REQUEST:
			case ABONENT_NOT_FOUND:
				setAbonentCells(Mes.get("abonentNotFound"), "?", "payment-table-loaded-not-found", ALIGN_CENTER, false);
				amountF.disable();
				loaded = false;
				break;
		}
	}

	private void setServiceCell() {
		if (serviceDto.isActive()) {
			TableUtils.setCell(table, row, 0, new ServiceCellWidget(serviceDto), null,  null, ALIGN_LEFT, false);
			amountF.enable();
		}
		else {
			TableUtils.setCell(table, row, 0, new ServiceCellWidget(serviceDto), null,  "payment-table-connection-error", ALIGN_LEFT, false);
			amountF.disable();
		}
	}

	public void setAbonentCells(String abonentInfo, String debt, String style, HasHorizontalAlignment.HorizontalAlignmentConstant h, boolean loading) {
		TableUtils.setCell(table, row, 1, new Label(paymentListEntryDTO.getAbonentCode()), null,  style, ALIGN_LEFT, false);
		TableUtils.setCell(table, row, 2, new Label(abonentInfo), null,  style, ALIGN_LEFT, loading);
		TableUtils.setCell(table, row, 3, new Label(debt), null,  style, h, loading);
	}

	private void setCommissionCell(Widget commission, String style, HasHorizontalAlignment.HorizontalAlignmentConstant h, boolean loading) {
		TableUtils.setCell(table, row, 4, commission, null,  style, h, loading);
	}

	private void buildAmountField() {
		amountF = new ZNumberField.Builder<>(new NumberPropertyEditor.BigDecimalPropertyEditor(NumberFormatUtils.AMOUNT_NUMBER_FORMAT))
			.allowNegative(false)
			.build();
		amountF.setValue(BigDecimal.ZERO);

		amountF.addBlurHandler(new BlurEvent.BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				if (amountF.getCurrentValue() == (null)) {
					amountF.setValue(BigDecimal.ZERO, true);
				}
				else if (amountF.getValue().compareTo(BigDecimal.ZERO) > 0) {
					checkMinMaxAmount();
				}
			}
		});

		amountF.addValueChangeHandler(new ValueChangeHandler<BigDecimal>() {
			@Override
			public void onValueChange(ValueChangeEvent<BigDecimal> valueChangeEvent) {
				onAmountFValueChange();
			}
		});
		amountF.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				int code = event.getNativeKeyCode();
				if ((code >= KeyCodes.KEY_ZERO && code <= KeyCodes.KEY_NINE) || (code >= KeyCodes.KEY_NUM_ZERO && code <= KeyCodes.KEY_NUM_NINE)) {
					onAmountFValueChange();
				}
			}
		});
	}

	private void checkMinMaxAmount() {
		if (amountF.getValue().compareTo(serviceDto.getMinAmount()) < 0) {
			amountF.setValue(serviceDto.getMinAmount());
			amountF.markInvalid(Mes.get("minAmount") + ": " + NumberFormatUtils.format(serviceDto.getMinAmount()));
			paymentListTable.updateAggregatePaymentAmount();
		}
		if (amountF.getValue().compareTo(serviceDto.getMaxAmount()) > 0) {
			amountF.setValue(serviceDto.getMaxAmount());
			amountF.markInvalid(Mes.get("maxAmount") + ": " + NumberFormatUtils.format(serviceDto.getMinAmount()));
			paymentListTable.updateAggregatePaymentAmount();
		}
	}

	private void onAmountFValueChange() {
		commissionVerified = false;
		if (amountF.getValue() != null) {
			paymentListTable.updateAggregatePaymentAmount();
			paymentListTable.getPayB().disable();
		}
		if (clientCommissionsDto != null) {
			if (amountF.getCurrentValue() == null || NumberFormatUtils.equalsWithFormat(amountF.getCurrentValue(), BigDecimal.ZERO)) {
				setCommissionCell(new Label(NumberFormatUtils.format(BigDecimal.ZERO)), "payment-table-loaded", ALIGN_RIGHT, false);
			} else {
				setCommissionCell(new Label("?"), "payment-table-loading", ALIGN_CENTER, false);
			}
		}
	}

	private void buildDeleteButton() {
		deleteB = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().trash())
			.tooltip(Mes.get("delete"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					ServicesFactory.getPaymentListService().deletePaymentListEntry(paymentListEntryDTO.getId(), new ServiceCallback<Void>() {
						@Override
						public void onServiceSuccess(Void result) {
							table.removeRow(row);
							paymentListTable.getTableRows().remove(getEntry());
							if (paymentListTable.getTableRows().isEmpty()) {
								paymentListTable.addEmptyRow();
							}
							else {
								for (RowEntry entry: paymentListTable.getTableRows()) {
									if (entry.getRow() > row) {
										entry.decrementRow();
									}
								}
							}
						}
					});
				}
			})
			.build();

		deleteB.addStyleName("x-toolbar-mark");
	}

	private Widget getCommissionCellErrorWidget() {
		HBoxLayoutContainer container =  new HBoxLayoutContainer();
		Image infoIcon = new Image();
		String infoIconUrl = FAIconsProvider.getIcons().info_circle().getSafeUri().asString();
		infoIcon.setPixelSize(20,20);
		infoIcon.setTitle(Mes.get("commissionNotConfigured"));
		infoIcon.setUrl(infoIconUrl);
		container.add(new Label("?"));
		container.add(infoIcon);
		container.setHBoxLayoutAlign(HBoxLayoutContainer.HBoxLayoutAlign.MIDDLE);
		return container.asWidget();
	}

	public void updateCommissionValue() {
		if (clientCommissionsDto !=  null){
			setCommissionCell(new Label(calculateCommission()), "payment-table-loaded", ALIGN_RIGHT, false);
		}
	}


	private String calculateCommission() {
		if (amountF.getValue().compareTo(BigDecimal.ZERO) > 0) {
			if (clientCommissionsDto.getRateType() == CommissionRateTypeDTO.FIXED) {
				commissionVerified = true;
				commission = clientCommissionsDto.getCommission();
				return NumberFormatUtils.format(commission);
			} else {
				commissionVerified = true;
				commission = amountF.getValue().multiply(clientCommissionsDto.getCommission().divide(new BigDecimal(100), RoundingMode.UP));
				return NumberFormatUtils.format(commission);
			}
		}
		else {
			commissionVerified = false;
			return String.valueOf(BigDecimal.ZERO);
		}
	}

	public BigDecimal getDebt() {
		return debt;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public BigDecimal getPaymentAmount(){return amountF.getCurrentValue();}

	public RowEntry getEntry() {return this;}

	public Boolean isLoaded() {
		return loaded;
	}

	public boolean isCommissionVerified() {
		return commissionVerified;
	}

	public PaymentListEntryDTO getPaymentListEntryDTO() {
		return paymentListEntryDTO;
	}

	public ServiceDto getServiceDto() {
		return serviceDto;
	}

	public void clearAmountF() {
		amountF.setValue(BigDecimal.ZERO);
		amountF.removeToolTip();
	}

	public int getRow() {
		return row;
	}

	public void decrementRow() {
		this.row = row - 1;
	}
}
