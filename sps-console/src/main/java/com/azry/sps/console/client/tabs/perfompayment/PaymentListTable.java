package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZConfirmDialog;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.DialogUtils;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.NumberFormatUtils;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.bankclient.AccountDTO;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListEntryDTO;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.google.gwt.user.client.ui.FlexTable;
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
import java.util.List;

import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_CENTER;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_LEFT;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_RIGHT;

public class PaymentListTable extends Composite {

	private ZToolBar toolBar;
	private FlexTable paymentListTable;

	private ZButton add;
	private ZButton debtCommissionsB;
	private ZButton payB;

	private ZSimpleComboBox<AccountDTO> clientAccountsComboBox;

	private List<RowEntry> tableRows = new ArrayList<>();

	private ClientDTO clientDTO;

	private PaymentListDTO paymentListDTO;

	int readyPaymentEntryCount = 0;

	public PaymentListTable(ClientDTO clientDTO, PaymentListDTO paymentListDTO, ZSimpleComboBox<AccountDTO> clientAccountsComboBox) {
		this.clientAccountsComboBox = clientAccountsComboBox;
		this.clientDTO = clientDTO;
		this.paymentListDTO = paymentListDTO;
		initToolBar();
		initFlexTable();
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
						public void onSave(PaymentListEntryDTO dto, final ServiceDTO serviceDto) {
							if (validPaymentListEntry(dto)) {
								ServicesFactory.getPaymentListService().addPaymentListEntry(clientDTO,
									dto, new ServiceCallback<PaymentListEntryDTO>() {
									@Override
									public void onServiceSuccess(PaymentListEntryDTO result) {
										result.setService(serviceDto);
										if (tableRows.isEmpty()) {
											paymentListTable.removeRow(1);
										}
										paymentListTable.insertRow(tableRows.size() + 1);
										addPaymentListEntry(result);
									}
								});
							} else {
								DialogUtils.showWarning(Mes.get("clientPaymentListEntryAlreadyExists"));
							}
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

	private boolean validPaymentListEntry(PaymentListEntryDTO dto) {
		for (PaymentListEntryDTO entry : paymentListDTO.getEntries()) {
			if (dto.getServiceId() == entry.getServiceId() || dto.getAbonentCode().equals(entry.getAbonentCode())) {
				return false;
			}
		}
		return true;
	}

	private void onPayBClicked() {
		if (isClientAccountsComboBoxValid()) {
			new ZConfirmDialog(Mes.get("confirm"), Mes.get("createPaymentConfirmation")) {
				@Override
				public void onConfirm() {
					final List<PaymentDTO> paymentList = new ArrayList<>();
					for (RowEntry entry : tableRows) {
						PaymentDTO payment = new PaymentDTO();
						if (entry.isCommissionVerified()) {
							payment.setServiceId(entry.getPaymentListEntryDTO().getServiceId());
							payment.setAbonentCode(entry.getPaymentListEntryDTO().getAbonentCode());
							payment.setAmount(entry.getPaymentAmount());
							payment.setClCommission(entry.getCommission());
							payment.setClient(clientDTO);
							payment.setSourceAccountBan(clientAccountsComboBox.getValue().getIban());

							paymentList.add(payment);
						}
					}
					ServicesFactory.getPaymentService().addPayments(paymentList, new ServiceCallback<List<PaymentDTO>>(this) {
						@Override
						public void onServiceSuccess(List<PaymentDTO> result) {
							new CreatedPaymentsWindow(result).showInCenter();
							for (RowEntry entry : tableRows) {
								entry.clearAmountF();
								entry.updateCommissionValue();
								entry.setCommissionVerified(false);
							}
							readyPaymentEntryCount = 0;
							payB.disable();
							setAggregatesValues();
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
			} else {
				row.updateCommissionValue();
			}
			if (!NumberFormatUtils.equalsWithFormat(row.getPaymentAmount(), BigDecimal.ZERO)) {
				payB.enable();
			}
		}
	}

	private void initFlexTable() {
		paymentListTable = new FlexTable();
		paymentListTable.addStyleName("payment-list-table");

		addTableHeader();
		if (paymentListDTO != null && !paymentListDTO.getEntries().isEmpty()) {
			addPaymentList(paymentListDTO);
		} else {
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

	protected void addEmptyRow() {
		if (paymentListTable.getRowCount() == 1) {
			TableUtils.setCell(paymentListTable, 1, 0, new Label(Mes.get("emptyTableText")), null, "empty-table", ALIGN_LEFT, false);
			paymentListTable.getFlexCellFormatter().setColSpan(1, 0, paymentListTable.getCellCount(0));
		} else {
			paymentListTable.insertRow(1);
			TableUtils.setCell(paymentListTable, 1, 0, new Label(Mes.get("emptyTableText")), null, "empty-table", ALIGN_LEFT, false);
			paymentListTable.getFlexCellFormatter().setColSpan(1, 0, paymentListTable.getCellCount(0));
		}
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



	protected void setAggregatesValues() {
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
		Label label = new Label(Mes.get("totalAmount")+ ": " + NumberFormatUtils.format(total));
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

	public int getReadyPaymentEntryCount() {
		return readyPaymentEntryCount;
	}

	public void incrementReadyPaymentEntryCount() {
		readyPaymentEntryCount++;
	}

	public void decrementReadyPaymentEntryCount() {
		readyPaymentEntryCount--;
	}

	public PaymentListDTO getPaymentListDTO() {
		return paymentListDTO;
	}
}
