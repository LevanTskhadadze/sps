package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListEntryDTO;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.theme.neptune.client.base.button.Css3ButtonCellAppearance;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.HashMap;
import java.util.Map;

import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_CENTER;
import static com.google.gwt.user.client.ui.HasHorizontalAlignment.ALIGN_LEFT;
import static com.google.gwt.user.client.ui.HasVerticalAlignment.ALIGN_MIDDLE;
import static com.google.gwt.user.client.ui.HasVerticalAlignment.ALIGN_TOP;

public class PaymentTable extends Composite {

	private TabPanel tabPanel;
	private VerticalLayoutContainer vContainer;
	private ZToolBar toolBar;
	private FlexTable paymentListTable;
	private int row = 0;
	private int column = 0;

	ZButton add;
	ZButton reload;
	ZButton pay;

	Map<Integer, RowEntry> tableRows = new HashMap<>();

	ClientDTO clientDTO;
//	PaymentListDTO paymentListDTO;

	public PaymentTable(ClientDTO clientDTO, PaymentListDTO paymentListDTO) {
		this.clientDTO = clientDTO;
//		this.paymentListDTO = paymentListDTO;
		initToolBar();
		initFlexTable(paymentListDTO);
		buildDisplay();
	}

	private void buildDisplay() {
		vContainer = new VerticalLayoutContainer();
		tabPanel = new TabPanel();
		vContainer.add(toolBar, new VerticalLayoutContainer.VerticalLayoutData(1, -1));
		vContainer.add(paymentListTable, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(5)));
		vContainer.setBorders(true);
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
							ServicesFactory.getPaymentListService().addPaymentListEntry(clientDTO, dto, new ServiceCallback<PaymentListDTO>() {
								@Override
								public void onServiceSuccess(PaymentListDTO result) {

								}
							});
						}
					}.showInCenter();
				}
			})
			.build();

		reload = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().refresh())
			.text(Mes.get("reload"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {

				}
			})
			.build();

		pay = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().play())
			.text(Mes.get("payment"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {

				}
			})
			.build();

		toolBar.add(new Label(), flex);
		toolBar.add(add);
		toolBar.add(reload);
		toolBar.add(pay);
	}

	private void initFlexTable(PaymentListDTO paymentListDTO) {
		paymentListTable = new FlexTable();
		paymentListTable.addStyleName("payment-list-table");

		addTableHeader(paymentListTable);
		if (paymentListDTO != null) {
			addPaymentListEntries(paymentListDTO);
		}
		else {
			addEmptyRow(paymentListTable);
		}
		addSummedAmountedCommissionRow();
//		addTotalAmountRow();
	}

	private void addSummedAmountedCommissionRow() {

	}

	private void addTableHeader(FlexTable table) {
		setCell(new Label(Mes.get("service")), "300px");
		setCell(new Label(Mes.get("paymentIdentifierShort")), "150px");
		setCell(new Label(Mes.get("abonentInfo")), "null");
		setCell(new Label(Mes.get("debt")), "100px");
		setCell(new Label(Mes.get("clientCommissionShort")), "100px");
		setCell(new Label(Mes.get("amount")), "150px");
		setCell(new Label(""), "30px");
		column=0;
		row++;
	}

	private void addEmptyRow(FlexTable table) {
		TableUtils.setCell(table, 1, 0, new HTML("<div class='empty-table'>" + Mes.get("emptyTableText") + "</div>"), "100%", null, ALIGN_TOP, ALIGN_LEFT);
		table.getFlexCellFormatter().setColSpan(1, 0, table.getCellCount(0));
	}

	private void addPaymentListEntries(PaymentListDTO paymentList) {

		for (final PaymentListEntryDTO entry: paymentList.getEntries()) {
			ServicesFactory.getServiceTabService().getService(entry.getServiceId(), new ServiceCallback<ServiceDto>() {
				@Override
				public void onServiceSuccess(ServiceDto result) {
					tableRows.put(row, new RowEntry(paymentListTable, entry, result, row));
					row++;
				}
			});
		}
	}


	public void setCell(Widget widget, String width) {
		paymentListTable.setWidget(row, column, widget);
		paymentListTable.getFlexCellFormatter().setWidth(row, column, width);
		paymentListTable.getFlexCellFormatter().setVerticalAlignment(row, column, ALIGN_MIDDLE);
		paymentListTable.getFlexCellFormatter().setHorizontalAlignment(row, column, ALIGN_CENTER);
		column++;
	}

}
