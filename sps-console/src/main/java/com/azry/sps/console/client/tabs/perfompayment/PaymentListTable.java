package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.NumberFormatUtils;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.bankclient.AccountDTO;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.azry.sps.console.shared.dto.payment.PaymentDto;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDto;
import com.azry.sps.console.shared.dto.paymentList.PaymentListDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListEntryDTO;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.theme.neptune.client.base.button.Css3ButtonCellAppearance;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_CENTER;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_LEFT;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_RIGHT;

public class PaymentListTable extends Composite {

	private ZToolBar toolBar;
	private FlexTable paymentListTable;

	ZButton add;
	ZButton debtCommissionsB;
	ZButton payB;

	ZSimpleComboBox<AccountDTO> clientAccountsComboBox;

	List<RowEntry> tableRows = new ArrayList<>();

	ClientDTO clientDTO;

	public PaymentListTable(ClientDTO clientDTO, PaymentListDTO paymentListDTO, ZSimpleComboBox<AccountDTO> clientAccountsComboBox) {
		this.clientAccountsComboBox = clientAccountsComboBox;
		this.clientDTO = clientDTO;
		initToolBar();
		initFlexTable(paymentListDTO);
		buildDisplay();
	}

	private void buildDisplay() {
		VerticalLayoutContainer vContainer = new VerticalLayoutContainer();
		TabPanel tabPanel = new TabPanel();
		vContainer.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
		vContainer.add(paymentListTable, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
		vContainer.setBorders(true);
		vContainer.addStyleName("payment-table-container");
		tabPanel.add(vContainer, new TabItemConfig(Mes.get("payments")));
		tabPanel.addStyleName("payments-page-tab-panel");
		initWidget(tabPanel);
	}

	private void initToolBar() {
		toolBar = new ZToolBar();
		BoxLayoutContainer.BoxLayoutData flex = new BoxLayoutContainer.BoxLayoutData();
		flex.setFlex(1);

		add = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().plus())
			.text(Mes.get("add"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					new AddPaymentListEntryWindow() {
						@Override
						public void onSave(PaymentListEntryDTO dto) {
							ServicesFactory.getPaymentListService().addPaymentListEntry(clientDTO, dto, new ServiceCallback<PaymentListEntryDTO>() {
								@Override
								public void onServiceSuccess(PaymentListEntryDTO result) {
									if (tableRows.isEmpty()) {
										paymentListTable.removeRow(1);
									}
									paymentListTable.insertRow(tableRows.size()+1);
									addPaymentListEntry(result);
								}
							});
						}
					}.showInCenter();
				}
			})
			.build();

		debtCommissionsB = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().refresh())
			.text(Mes.get("debtCommission"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					debtCommissionsBClicked();
				}
			})
			.build();

		payB = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().play())
			.text(Mes.get("payment"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					onPayBClicked();
				}
			})
			.build();

		payB.disable();

		toolBar.add(new Label(), flex);
		toolBar.add(add);
		toolBar.add(debtCommissionsB);
		toolBar.add(payB);
	}

	private void onPayBClicked() {
		if (isClientAccountsComboBoxValid()) {
			GWT.log(String.valueOf(clientAccountsComboBox.getValue()));
			new ZConfirmDialog(Mes.get("confirm"), Mes.get("createPaymentConfirmation")) {
				@Override
				public void onConfirm() {
					final List<PaymentDto> paymentList = new ArrayList<>();
					for (RowEntry entry : tableRows) {
						PaymentDto payment = new PaymentDto();
						if (entry.isCommissionVerified()) {
//							payment.setAgentPaymentId();
							payment.setServiceId(entry.getPaymentListEntryDTO().getServiceId());
//							payment.setChannelId();
							payment.setAbonentCode(entry.getPaymentListEntryDTO().getAbonentCode());
							payment.setAmount(entry.getPaymentAmount());
							payment.setClCommission(entry.getCommission());
//							payment.setSvcCommission();
							payment.setStatus(PaymentStatusDto.CREATED);
//							payment.statusChangeTime;
//							payment.statusMessage;
							payment.setCreateTime(new Date());
							payment.setClient(clientDTO);

							paymentList.add(payment);
						}
						entry.clearAmountF();
					}

					ServicesFactory.getPaymentService().addPayments(paymentList, new ServiceCallback<Void>() {
						@Override
						public void onServiceSuccess(Void result) {
							new CreatedPaymentWindow(paymentList).showInCenter();
						}
					});
				}
			}.show();
		}
	}

	private void debtCommissionsBClicked() {
		for (RowEntry row: tableRows) {
			if (!row.isLoaded()) {
				row.loadCellData();
			}
			if (!NumberFormatUtils.equalsWithFormat(row.getPaymentAmount(), BigDecimal.ZERO)) {
				row.updateCommissionValue();
				payB.enable();
			}
			updateAggregateCommission();
			updateAggregateTotalAmount();
		}
	}

	private void initFlexTable(PaymentListDTO paymentListDTO) {
		paymentListTable = new FlexTable();
		paymentListTable.addStyleName("payment-list-table");

		addTableHeader();
		if (paymentListDTO != null) {
			addPaymentList(paymentListDTO);
		}
		else {
			addEmptyRow();
		}
		setAggregatesValues();
	}

	private void addTableHeader() {
		int row = 0;
		int column = 0;
		TableUtils.setCell(paymentListTable, row, column++, new Label(Mes.get("service")), "300px",  null, ALIGN_CENTER, false);
		TableUtils.setCell(paymentListTable, row, column++, new Label(Mes.get("paymentIdentifierShort")), "150px",  null, ALIGN_CENTER, false);
		TableUtils.setCell(paymentListTable, row, column++, new Label(Mes.get("abonentInfo")), null,  null, ALIGN_CENTER, false);
		TableUtils.setCell(paymentListTable, row, column++, new Label(Mes.get("debt")), "100px",  null, ALIGN_CENTER, false);
		TableUtils.setCell(paymentListTable, row, column++, new Label(Mes.get("clientCommissionShort")), "100px",  null, ALIGN_CENTER, false);
		TableUtils.setCell(paymentListTable, row, column++, new Label(Mes.get("amount")), "150px",  null, ALIGN_CENTER, false);
		TableUtils.setCell(paymentListTable, row, column, new Label(""), "30px",  null, ALIGN_CENTER, false);
	}

	private void addEmptyRow() {
		TableUtils.setCell(paymentListTable, 1, 0, new HTML("<div class='empty-table'>" + Mes.get("emptyTableText") + "</div>"), "100%", null, ALIGN_LEFT, false);
		paymentListTable.getFlexCellFormatter().setColSpan(1, 0, paymentListTable.getCellCount(0));
	}

	private void addPaymentList(PaymentListDTO paymentList) {
		for (PaymentListEntryDTO entry: paymentList.getEntries()) {
			addPaymentListEntry(entry);
		}
	}

	private void addPaymentListEntry(final PaymentListEntryDTO entry) {
		int row  = tableRows.size() == 0 ? 1 : tableRows.size() + 1;
		tableRows.add(new RowEntry(paymentListTable, entry, row, this));
	}



	private void setAggregatesValues() {
		updateAggregateCommission();
		updateAggregateDebt();
		updateAggregatePaymentAmount();
		updateAggregateTotalAmount();
	}

	void updateAggregateDebt() {
		int row  = tableRows.size() == 0 ? 2 : tableRows.size() + 1;
		BigDecimal total = BigDecimal.ZERO;
		for (RowEntry entry : tableRows) {
			if (entry.isLoaded()) {
				total = total.add(entry.getDebt());
			}
		}
		TableUtils.setCell(paymentListTable, row, 3, new Label(NumberFormatUtils.format(total)),null, null, ALIGN_RIGHT, false);
	}

	void updateAggregateCommission() {
		int row  = tableRows.size() == 0 ? 2 : tableRows.size() + 1;
		BigDecimal total = BigDecimal.ZERO;
		for (RowEntry entry : tableRows) {
			if (entry.isCommissionVerified()) {
				total = total.add(entry.getCommission());
			}
		}
		TableUtils.setCell(paymentListTable, row, 4, new Label(NumberFormatUtils.format(total)),null, null, ALIGN_RIGHT, false);
	}

	void updateAggregatePaymentAmount() {
		int row  = tableRows.size() == 0 ? 2 : tableRows.size() + 1;
		BigDecimal total = BigDecimal.ZERO;
		for (RowEntry entry : tableRows) {
			if (entry.isLoaded()) {
				total = total.add(entry.getPaymentAmount());
			}
		}
		TableUtils.setCell(paymentListTable, row, 5, new Label(NumberFormatUtils.format(total)),null, null, ALIGN_RIGHT, false);
		TableUtils.setCell(paymentListTable, row, 6, new Label(""),null, null, null, false);
	}

	void updateAggregateTotalAmount() {
		int row  = tableRows.size() == 0 ? 3 : tableRows.size() + 2;
		int column = paymentListTable.getCellCount(0);
		BigDecimal total = BigDecimal.ZERO;
		for (RowEntry entry : tableRows) {
			if (entry.isCommissionVerified()) {
				total = total.add(entry.getCommission());
				total = total.add(entry.getPaymentAmount());
			}
		}
		Label label = new Label(Mes.get("totalAmount: ") + NumberFormatUtils.format(total));
		label.setStyleName("payment-list-total-amount");
		TableUtils.setCell(paymentListTable, row, 0, label, null, null, ALIGN_RIGHT, false);
		paymentListTable.getRowFormatter().setStyleName(row, "payment-list-table-total-amount-row");
		paymentListTable.getFlexCellFormatter().setColSpan(row, 0, column);
	}

	List<RowEntry> getTableRows() {
		return tableRows;
	}

	public ZButton getPayB() {
		return payB;
	}

	private boolean isClientAccountsComboBoxValid() {
		if (clientAccountsComboBox.getValue() == null) {
			clientAccountsComboBox.markInvalid(Mes.get("chooseAccount"));
			return false;
		}
		return true;
	}
}