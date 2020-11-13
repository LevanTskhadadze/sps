package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZFieldLabel;
import com.azry.gxt.client.zcomp.ZNumberField;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.bankclient.AccountDTO;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.azry.sps.console.shared.dto.paymentList.PaymentListDTO;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.theme.neptune.client.base.button.Css3ButtonCellAppearance;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;

import java.util.List;

public class PerformPaymentsTab extends Composite {

	private VerticalLayoutContainer container;
	private TabPanel customerTabPanel;
	private ZToolBar customerInfoBar;

	private boolean flag;

	private ZFieldLabel idNLabel;
	private ZNumberField<Long> idNField;
	private ZButton searchButton;
	private ZButton clientInfoButton;
	private ZSimpleComboBox<AccountDTO> clientAccountsComboBox;
	private ZButton clearButton;

	PaymentTable paymentTable;



	public PerformPaymentsTab() {
		flag = false;
		container = new VerticalLayoutContainer();
		container.add(buildCustomerTabPanel(), new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10)));
		initWidget(container);
	}

	private TabPanel buildCustomerTabPanel() {
		customerInfoBar = new ZToolBar();
		BoxLayoutContainer.BoxLayoutData flex = new BoxLayoutContainer.BoxLayoutData();
		flex.setFlex(1);

		customerTabPanel = new TabPanel();

		idNLabel = new ZFieldLabel.Builder()
			.labelWidth(100)
			.label(Mes.get("idNumber"))
			.field(idNField)
			.requiredSignColor("red")
			.build();

		idNField = new ZNumberField.Builder<>(new NumberPropertyEditor.LongPropertyEditor()).width(250).required().build();

		searchButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().search())
			.text(Mes.get("search"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					ServicesFactory.getBankIntegrationService().findClient(String.valueOf(idNField.getCurrentValue()), new ServiceCallback<ClientDTO>() {
						@Override
						public void onServiceSuccess(final ClientDTO dto) {

							initClientInfo(dto);

							ServicesFactory.getPaymentListService().getPaymentList(dto.getPersonalNumber(), new ServiceCallback<PaymentListDTO>() {
								@Override
								public void onServiceSuccess(PaymentListDTO result) {
									if (paymentTable == null) {
										paymentTable = new PaymentTable(dto, result);
										container.add(paymentTable, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10)));
										container.forceLayout();
									}
								}
							});

							ServicesFactory.getBankIntegrationService().getClientAccounts(dto.getId(), new ServiceCallback<List<AccountDTO>>() {
								@Override
								public void onServiceSuccess(List<AccountDTO> result) {
									clientAccountsComboBox.replaceAll(result);
								}
							});
						}
					});
				}
			})
			.build();

		clearButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().eraser())
			.text(Mes.get("clear"))
			.tooltip(Mes.get("clearFilter"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					clearClientInfo();
				}
			})
			.build();


		customerInfoBar.add(idNLabel);
		customerInfoBar.add(idNField);
		customerInfoBar.add(searchButton);
		customerInfoBar.add(new Label(), flex);
		customerInfoBar.add(clearButton);

		customerInfoBar.setBorders(true);
		customerTabPanel.add(customerInfoBar, new TabItemConfig(Mes.get("client")));
		customerTabPanel.addStyleName("payments-page-tab-panel");
		return customerTabPanel;
	}

	private void initClientInfo(final ClientDTO dto) {
		if (flag == true) {
			clearClientInfo();
		}
		clientInfoButton = new ZButton.Builder()
			.text(dto.getFirstName() + " " + dto.getLastName())
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler((new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					new ClientInfoWindow(dto).showInCenter();
				}
			}))
			.build();

		clientAccountsComboBox = new ZSimpleComboBox.Builder<AccountDTO>()
			.keyProvider(new ModelKeyProvider<AccountDTO>() {
				@Override
				public String getKey(AccountDTO dto) {
					return String.valueOf(dto.getIban());
				}
			})
			.labelProvider(new LabelProvider<AccountDTO>() {
				@Override
				public String getLabel(AccountDTO dto) {
					return dto.getName() + " - " + dto.getIban();
				}
			})
			.noSelectionLabel(Mes.get("clientIban"))
			.enableSorting(false)
			.editable(false)
			.width(350)
			.build();


		customerInfoBar.insert(clientInfoButton, 3);
		customerInfoBar.insert(clientAccountsComboBox, 4);
		int height = customerInfoBar.getOffsetHeight();
		customerInfoBar.forceLayout();
		customerInfoBar.setHeight(height);
		flag = true;
	}

	private void clearClientInfo() {
		if (flag == true) {
			customerInfoBar.remove(clientInfoButton);
			customerInfoBar.remove(clientAccountsComboBox);
			container.remove(paymentTable);
			paymentTable = null;
			flag = false;
		}
	}
}
