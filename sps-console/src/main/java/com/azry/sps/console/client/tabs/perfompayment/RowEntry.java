package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZNumberField;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.NumberFormatUtils;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.paymentList.PaymentListEntryDTO;
import com.azry.sps.console.shared.dto.providerintegration.AbonentInfoDTO;
import com.azry.sps.console.shared.dto.providerintegration.GetInfoStatusDTO;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
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
	private ServiceDTO serviceDTO;
	private final PaymentListTable paymentListTable;


	private Boolean loaded;
	private boolean commissionVerified;

	public RowEntry(FlexTable table, PaymentListEntryDTO paymentListEntryDTO, int row, PaymentListTable paymentListTable) {
		this.row = row;
		loaded = false;
		commissionVerified = false;
		abonentInfo = "";
		commission = new BigDecimal("0.00");
		serviceDTO = paymentListEntryDTO.getService();
		this.paymentListTable = paymentListTable;
		this.paymentListEntryDTO = paymentListEntryDTO;
		this.table = table;
		buildAmountField();
		buildDeleteButton();
		buildRow();
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

		ServicesFactory.getProviderIntegrationService().getAbonent(serviceDTO.getServiceDebtCode(), paymentListEntryDTO.getAbonentCode(),
			new ServiceCallback<AbonentInfoDTO>(false) {
			@Override
			public void onFailure(Throwable throwable) {
				setAbonentCells(Mes.get("spConnectionFailed"), "?", "payment-table-connection-error", ALIGN_CENTER, false);
				amountF.disable();
				loaded = false;
			}

			@Override
			public void onServiceSuccess(AbonentInfoDTO abonentInfoDTO) {
				abonentInfo = abonentInfoDTO.getAbonentInfo();
				debt = abonentInfoDTO.getDebt();
				updateRow(abonentInfoDTO.getStatus());
			}
		});
		ServicesFactory.getClientCommissionsService().calculateCommission(serviceDTO.getId(), amountF.getValue(),
			new ServiceCallback<BigDecimal>(false) {
			@Override
			public void onServiceSuccess(BigDecimal commission) {
				onCommissionLoad(commission);
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
		if (serviceDTO.isActive()) {
			TableUtils.setCell(table, row, 0, new ServiceCellWidget(serviceDTO), null,  null, ALIGN_LEFT, false);
			amountF.enable();
		} else {
			TableUtils.setCell(table, row, 0, new ServiceCellWidget(serviceDTO), null,  "payment-table-connection-error", ALIGN_LEFT, false);
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
					onAmountFValueChange();
				}
				else if (amountF.getValue().compareTo(BigDecimal.ZERO) > 0) {
					checkMinMaxAmount();
				}
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
		if (amountF.getValue().compareTo(serviceDTO.getMinAmount()) < 0) {
			amountF.setValue(serviceDTO.getMinAmount());
			amountF.markInvalid(Mes.get("minAmount") + ": " + NumberFormatUtils.format(serviceDTO.getMinAmount()));
			paymentListTable.updateAggregatePaymentAmount();
		}
		if (amountF.getValue().compareTo(serviceDTO.getMaxAmount()) > 0) {
			amountF.setValue(serviceDTO.getMaxAmount());
			amountF.markInvalid(Mes.get("maxAmount") + ": " + NumberFormatUtils.format(serviceDTO.getMaxAmount()));
			paymentListTable.updateAggregatePaymentAmount();
		}
	}

	private void onAmountFValueChange() {
		if (isCommissionVerified()) {
			paymentListTable.decrementReadyPaymentEntryCount();
		}
		if (amountF.getCurrentValue() == null || NumberFormatUtils.equalsWithFormat(amountF.getCurrentValue(), BigDecimal.ZERO)) {
			setCommissionCell(new Label(NumberFormatUtils.format(BigDecimal.ZERO)), "payment-table-loaded", ALIGN_RIGHT, false);
			paymentListTable.updateAggregateCommission();
			paymentListTable.updateAggregatePaymentAmount();
			if (paymentListTable.getReadyPaymentEntryCount() == 0) {
				paymentListTable.getPayB().disable();
				paymentListTable.updateAggregateTotalAmount();
			}
		} else {
			setCommissionCell(new Label("?"), "payment-table-loading", ALIGN_CENTER, false);
			paymentListTable.getPayB().disable();
			paymentListTable.updateAggregatePaymentAmount();
		}
		commissionVerified = false;
	}

	private void buildDeleteButton() {
		deleteB = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().trash())
			.tooltip(Mes.get("delete"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					new ZConfirmDialog(Mes.get("confirm"), Mes.get("objectDeleteConfirmation")) {
						@Override
						public void onConfirm() {
							onDelete();
						}
					}.show();
				}
			})
			.build();

		deleteB.addStyleName("x-toolbar-mark");
	}

	private void onDelete() {
		ServicesFactory.getPaymentListService().deletePaymentListEntry(paymentListEntryDTO.getId(),
			new ServiceCallback<Void>() {
				@Override
				public void onServiceSuccess(Void result) {
					table.removeRow(row);
					paymentListTable.getTableRows().remove(getEntry());
					paymentListTable.getPaymentListDTO().getEntries().remove(paymentListEntryDTO);
					if (paymentListTable.getTableRows().isEmpty()) {
						paymentListTable.addEmptyRow();
					} else {
						for (RowEntry entry: paymentListTable.getTableRows()) {
							if (entry.getRow() > row) {
								entry.decrementRow();
							}
						}
					}
					paymentListTable.setAggregatesValues();
				}
			});
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
		ServicesFactory.getClientCommissionsService().calculateCommission(serviceDTO.getId(), amountF.getValue(),
			new ServiceCallback<BigDecimal>(false) {
			@Override
			public void onServiceSuccess(BigDecimal commission) {
				onCommissionLoad(commission);
			}
		});
	}

	private void onCommissionLoad(BigDecimal commission) {
		if (commission == null) {
			setCommissionCell(getCommissionCellErrorWidget(), "payment-table-loaded-not-found", ALIGN_CENTER, false);
			amountF.disable();
			if (isCommissionVerified()) {
				commissionVerified = false;
				setCommission(BigDecimal.ZERO);
				paymentListTable.decrementReadyPaymentEntryCount();
				if (paymentListTable.getReadyPaymentEntryCount() == 0 ) {
					paymentListTable.getPayB().disable();
				}
			}
			amountF.setValue(BigDecimal.ZERO);
			paymentListTable.updateAggregatePaymentAmount();
			paymentListTable.updateAggregateCommission();
		} else {
			setCommission(commission);
			setCommissionCell(new Label(NumberFormatUtils.format(commission)), "payment-table-loaded", ALIGN_RIGHT, false);
			if (!isCommissionVerified()) {
				paymentListTable.incrementReadyPaymentEntryCount();
			}
			commissionVerified = true;
			amountF.enable();
			paymentListTable.updateAggregateCommission();
			paymentListTable.updateAggregateTotalAmount();
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

	public ServiceDTO getServiceDTO() {
		return serviceDTO;
	}

	public void clearAmountF() {
		amountF.setValue(BigDecimal.ZERO);
		amountF.removeToolTip();
	}

	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}

	public int getRow() {
		return row;
	}

	public void decrementRow() {
		this.row = row - 1;
	}

	public void setCommissionVerified(boolean commissionVerified) {
		this.commissionVerified = commissionVerified;
	}
}
