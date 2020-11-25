package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.EnterKeyBinder;
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

public class PerformPaymentsTab extends Composite {

	private final VerticalLayoutContainer container;
	private ZToolBar customerInfoBar;

	private boolean clientInfoLoaded;

	private ZNumberField<Long> idNField;
	private ZButton clientInfoButton;
	private ZSimpleComboBox<AccountDTO> clientAccountsComboBox;

	PaymentListTable paymentTable;

	public PerformPaymentsTab() {
		clientInfoLoaded = false;
		container = new VerticalLayoutContainer();
		container.add(buildCustomerTabPanel(), new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10)));
		initWidget(container);
	}

	private TabPanel buildCustomerTabPanel() {
		customerInfoBar = new ZToolBar();
		BoxLayoutContainer.BoxLayoutData flex = new BoxLayoutContainer.BoxLayoutData();
		flex.setFlex(1);

		TabPanel customerTabPanel = new TabPanel();

		final ZFieldLabel idNLabel = new ZFieldLabel.Builder()
			.labelWidth(100)
			.label(Mes.get("idNumber"))
			.field(idNField)
			.requiredSignColor("red")
			.build();

		idNField = new ZNumberField.Builder<>(new NumberPropertyEditor.LongPropertyEditor()).width(250).required().build();

		ZButton searchButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().search())
			.text(Mes.get("search"))
			.appearance(new Css3ButtonCellAppearance<String>())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					if (isValid()) {
						ServicesFactory.getBankIntegrationService().getBankClientWithPaymentList(String.valueOf(idNField.getCurrentValue()),
							new ServiceCallback<ClientDTO>(PerformPaymentsTab.this) {
								@Override
								public void onServiceSuccess(ClientDTO result) {
									initClientInfo(result);

									if (paymentTable == null) {
										paymentTable = new PaymentListTable(result, result.getPaymentListDTO(), clientAccountsComboBox);
										container.add(paymentTable, new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(10)));
										container.forceLayout();
									}
								}
							});
					}
				}
			})
			.build();

		ZButton clearButton = new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().eraser())
			.text(Mes.get("clear"))
			.tooltip(Mes.get("clearFilter"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					clearClientInfo();
					clearIdField();
				}
			})
			.build();

		new EnterKeyBinder.Builder(searchButton)
			.add(idNField)
			.bind();

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

	private boolean isValid() {
		if (idNField.getCurrentValue() == null) {
			idNField.markInvalid(Mes.get("inputIdNumber"));
			return false;
		}
		else if (String.valueOf(idNField.getCurrentValue()).length() != 11) {
			idNField.markInvalid(Mes.get("idNumberInvalidLength"));
			return false;
		}
		return true;
	}

	private void initClientInfo(final ClientDTO dto) {
		if (clientInfoLoaded) {
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
			.values(dto.getAccountDTOs())
			.enableSorting(false)
			.editable(false)
			.width(350)
			.build();
		clientAccountsComboBox.setTooltipErrorHandler();


		customerInfoBar.insert(clientInfoButton, 3);
		customerInfoBar.insert(clientAccountsComboBox, 4);
		int height = customerInfoBar.getOffsetHeight();
		customerInfoBar.forceLayout();
		customerInfoBar.setHeight(height);
		clientInfoLoaded = true;
	}

	private void clearClientInfo() {
		if (clientInfoLoaded) {
			customerInfoBar.remove(clientInfoButton);
			customerInfoBar.remove(clientAccountsComboBox);
			container.remove(paymentTable);
			paymentTable = null;
			clientInfoLoaded = false;
		}
	}

	private void clearIdField() {
		idNField.setValue(null);
	}
}
