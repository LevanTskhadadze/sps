package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZNumberField;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.NumberFormatUtils;
import com.azry.sps.console.shared.dto.commission.CommissionRateTypeDTO;
import com.azry.sps.console.shared.dto.commission.clientcommission.ClientCommissionsDto;
import com.azry.sps.console.shared.dto.paymentList.PaymentListEntryDTO;
import com.azry.sps.console.shared.dto.providerintegration.AbonentInfoDTO;
import com.azry.sps.console.shared.dto.providerintegration.GetInfoStatusDTO;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.theme.neptune.client.base.button.Css3ButtonCellAppearance;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;

import java.math.BigDecimal;

import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_CENTER;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_LEFT;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_RIGHT;
import static com.google.gwt.user.client.ui.HasVerticalAlignment.ALIGN_MIDDLE;

public class RowEntry {

	private int row;

	private int column;

	private FlexTable table;

	private String abonentInfo;
	private BigDecimal debt;
	private BigDecimal commission;
	private ZNumberField<BigDecimal> amountF;
	private ZButton deleteB;


	private PaymentListEntryDTO paymentListEntryDTO;
	private ServiceDto serviceDto;
	ClientCommissionsDto clientCommissionsDto;



	public RowEntry(FlexTable table, PaymentListEntryDTO paymentListEntryDTO, ServiceDto serviceDto, int row) {
		this.row = row;
		column = 0;
		abonentInfo = "";
		this.paymentListEntryDTO = paymentListEntryDTO;
		this.serviceDto = serviceDto;
		this.table = table;
		buildAmountField();
		buildDeleteButton();
		buildRow();
		table.getRowFormatter().setStyleName(row,"payment-entry-list-row");
	}


	public void LoadData() {
		setLoadingCells(abonentInfo, "","table-cell-loading", true);
		ServicesFactory.getProviderIntegrationService().getAbonent(serviceDto.getServiceDebtCode(), Long.valueOf(paymentListEntryDTO.getAbonentCode()), new AsyncCallback<AbonentInfoDTO>() {
			@Override
			public void onFailure(Throwable throwable) {
				setLoadingCells(Mes.get("noIntegrationCOnneciton"), "?", "table-cell-loaded-error", false);
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

			}

			@Override
			public void onSuccess(ClientCommissionsDto dto) {

			}
		});
	}

	public void buildRow(){
		setCell(new Label(serviceDto.getName()), 0, ALIGN_LEFT, null, false);
		LoadData();
		setCommissionCell(NumberFormatUtils.format(commission), "table-cell-loaded");
		setCell(amountF, 5, null, null, false);
		setCell(deleteB, 6, null, null, false);
	}

	public void updateRow(GetInfoStatusDTO status) {
		switch(status) {
			case SUCCESS:
				setLoadingCells(abonentInfo, NumberFormatUtils.format(debt) ,"table-cell-loaded", false);
				break;
			case BAD_REQUEST:
				setLoadingCells(Mes.get("BAD_REQUEST"), "?", "table-cell-loaded-not-found", false);
				break;
			case ABONENT_NOT_FOUND:
				setLoadingCells(Mes.get("ABONENT_NOT_FOUND"), "?", "table-cell-loaded-not-found", false);
				break;
		}
	}

	public void setLoadingCells(String abonentInfo, String debt,String style, boolean loading) {
		setCell(new Label(paymentListEntryDTO.getAbonentCode()), 1, ALIGN_LEFT, style, false);
		setCell(new Label(abonentInfo), 2, ALIGN_LEFT, style, loading);
		setCell(new Label(debt), 3, ALIGN_RIGHT, style, loading);
	}

	private void setCommissionCell(String commission, String style) {
		setCell(new Label(commission), 4, ALIGN_RIGHT, style, false);
	}

	private void setCell(Widget widget, int column, HasHorizontalAlignment.HorizontalAlignmentConstant h, String style, boolean loading) {
			table.setWidget(row, column, widget);
			table.getFlexCellFormatter().setVerticalAlignment(row, column, ALIGN_MIDDLE);
			table.getFlexCellFormatter().setHorizontalAlignment(row, column, ALIGN_CENTER);
			if (style != null) {
				table.getFlexCellFormatter().addStyleName(row, column, style);
			}
			if (loading) {
				widget.setStyleName("loader");
			}
		if (h != null) {
			table.getFlexCellFormatter().setHorizontalAlignment(row, column, h);
		}
	}

	private void buildAmountField() {
		amountF = new ZNumberField.Builder<>(new NumberPropertyEditor.BigDecimalPropertyEditor(NumberFormatUtils.AMOUNT_NUMBER_FORMAT))
			.allowNegative(false)
			.build();

		amountF.setValue(BigDecimal.ZERO);
		amountF.addValueChangeHandler(new ValueChangeHandler<BigDecimal>() {
			@Override
			public void onValueChange(ValueChangeEvent<BigDecimal> valueChangeEvent) {
				updateCommissionValue();
			}
		});
	}

	private void buildDeleteButton() {
		deleteB = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().trash())
			.tooltip(Mes.get("delete"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					table.removeRow(row);
				}
			})
			.build();

		deleteB.addStyleName("x-toolbar-mark");
	}

	private void updateCommissionValue() {
		calculateCommission();
		setCommissionCell(calculateCommission(), null);
	}


	private String calculateCommission() {
		if (clientCommissionsDto.getRateType() == CommissionRateTypeDTO.FIXED) {
			return NumberFormatUtils.format(clientCommissionsDto.getCommission());
		}
		else {
			return NumberFormatUtils.format(amountF.getCurrentValue().multiply(clientCommissionsDto.getCommission().divide(new BigDecimal(100))));
		}
	}

	public BigDecimal getDebt() {
		return debt;
	}

//	public BigDecimal getCommission() {
//		return ;
//	}
}
