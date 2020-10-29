package com.azry.sps.console.client.tabs.clientcommissions;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZFieldLabel;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.commission.ClientCommissionsDTO;
import com.azry.sps.console.shared.dto.commission.CommissionRateTypeDTO;
import com.azry.sps.console.shared.dto.services.ServiceEntityDto;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.BigDecimalSpinnerField;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ClientCommissionsWindow extends ZWindow implements DualListWidget.ListElementsChangeListener {

	private static final String WINDOW_BOTTOM_BORDER_STYLE = "1px solid #3291D6";

	private static final VerticalLayoutContainer.VerticalLayoutData DUAL_LIST_LAYOUT_DATA = new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(8, 16, 16, 16));

	private static final VerticalLayoutContainer.VerticalLayoutData CHECK_BOX_LAYOUT_DATA = new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(8, 16, 0, 13));

	private static final VerticalLayoutContainer.VerticalLayoutData FIELD_LAYOUT_DATA = new VerticalLayoutContainer.VerticalLayoutData(1, -1);

	private final ClientCommissionsDTO clientCommissionsDTO;

	private final List<DualListWidgetItem> allServicesWidgetList = new ArrayList<>();

	private final List<DualListWidgetItem> allChannelsWidgetList = new ArrayList<>();

	private DualListWidget servicesDualListWidget;

	private DualListWidget channelsDualListWidget;

	private final VerticalLayoutContainer container = new VerticalLayoutContainer();

	private TabPanel tabPanel;

	private BigDecimalSpinnerField commission;

	private ZSimpleComboBox<CommissionRateTypeDTO> rateType;

	private BigDecimalSpinnerField minCommission;

	private BigDecimalSpinnerField maxCommission;

	private ToggleButton allServices;

	private ToggleButton allChannels;

	private ZButton saveButton;

	private ZButton cancelButton;

	public ClientCommissionsWindow(ClientCommissionsDTO dto, List<ServiceEntityDto> serviceEntityDTOs, List<ChannelDTO> channelDTOS, ActionMode actionMode) {
		super(Mes.get("ofClientCommissions") + " " + Mes.get("ActionMode_" + actionMode), 1000, 800, false);

		initWidgetLists(serviceEntityDTOs, channelDTOS);

		initFields();

		if (dto != null) {
			clientCommissionsDTO = dto;
			setFieldValues();
		}
		else {
			clientCommissionsDTO = new ClientCommissionsDTO();
		}


		add(container, new MarginData(0));

		initButtons();

		buildDisplay();
	}

	private void buildDisplay() {
		tabPanel.add(getServicesTab(),
					new TabItemConfig(getHeaderValue("services",
						clientCommissionsDTO.isAllServices(),
						clientCommissionsDTO.getServicesIds().size()),
						false));

		tabPanel.add(getChannelsTab(),
			new TabItemConfig(getHeaderValue("channels",
				clientCommissionsDTO.isAllChannels(),
				clientCommissionsDTO.getChannelsIds().size()),
				false));

		container.add(tabPanel, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
		container.add(getParametersPanel(), new VerticalLayoutContainer.VerticalLayoutData(1, -1, new Margins(0, 16, 8, 16)));

		addBottomHorizontalLine();

		addButton(saveButton);
		addButton(cancelButton);
	}


	private void initFields() {

		tabPanel = new TabPanel();

		commission = getBigDecimalField(true);

		rateType = new ZSimpleComboBox.Builder<CommissionRateTypeDTO>()
			.keyProvider(new ModelKeyProvider<CommissionRateTypeDTO>() {
				@Override
				public String getKey(CommissionRateTypeDTO dto) {
					return dto.name();
				}
			})
			.labelProvider(new LabelProvider<CommissionRateTypeDTO>() {
				@Override
				public String getLabel(CommissionRateTypeDTO dto) {
					return Mes.get(dto.name());
				}
			})
			.values(Arrays.asList(CommissionRateTypeDTO.values()))
			.enableSorting(false)
			.editable(false)
			.width(200)
			.required(true)
			.build();

		minCommission = getBigDecimalField(false);

		maxCommission = getBigDecimalField(false);

	}

	private BigDecimalSpinnerField getBigDecimalField(boolean required) {
		BigDecimalSpinnerField field = new BigDecimalSpinnerField();
		field.setAllowNegative(false);
		field.getPropertyEditor().setFormat(NumberFormat.getDecimalFormat());
		field.setIncrement(new BigDecimal(0.01));
		field.setAllowBlank(!required);
		field.getPropertyEditor().getFormat().overrideFractionDigits(0, 10);
		return field;
	}

	private IsWidget getParametersPanel() {
		VerticalLayoutContainer paramContainer = new VerticalLayoutContainer();
		paramContainer.add(getFieldLabel(rateType, "rateType", true), FIELD_LAYOUT_DATA);
		paramContainer.add(getFieldLabel(commission, "commission", true), FIELD_LAYOUT_DATA);
		paramContainer.add(getFieldLabel(minCommission, "minCommission", false), FIELD_LAYOUT_DATA);
		paramContainer.add(getFieldLabel(maxCommission, "maxCommission", false), FIELD_LAYOUT_DATA);
		return paramContainer;
	}

	private void setFieldValues() {
		rateType.setValue(clientCommissionsDTO.getRateType());
		commission.setValue(clientCommissionsDTO.getCommission());
		minCommission.setValue(clientCommissionsDTO.getMinCommission());
		maxCommission.setValue(clientCommissionsDTO.getMaxCommission());
	}

	private void initWidgetLists(List<ServiceEntityDto> serviceEntityDTOs, List<ChannelDTO> channelDTOs) {

		for (ServiceEntityDto service: serviceEntityDTOs) {
			allServicesWidgetList.add(new DualListWidgetItem(String.valueOf(service.getId()), service.getName()));
		}

		for (ChannelDTO channel: channelDTOs) {
			allChannelsWidgetList.add(new DualListWidgetItem(String.valueOf(channel.getId()), channel.getName()));
		}

	}

	private   void initButtons(){
		saveButton = new ZButton.Builder()
			.text(Mes.get("save"))
			.icon(FAIconsProvider.getIcons().floppy_o_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					if (isValid()) {
						ServicesFactory.getClientCommissionsService().updateClientCommissions(getClientCommissionsForUpdate(), new ServiceCallback<ClientCommissionsDTO>(ClientCommissionsWindow.this) {
							@Override
							public void onServiceSuccess(ClientCommissionsDTO result) {
								hide();
								onSave(result);
							}
						});
					}
				}
			})
			.build();

		cancelButton = new ZButton.Builder()
			.text(Mes.get("cancel"))
			.icon(FAIconsProvider.getIcons().ban_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					hide();
				}
			})
			.build();

		allServices = getToggleButtonWithListener("allServices");
		allChannels = getToggleButtonWithListener("allChannels");

	}

	private void addBottomHorizontalLine() {
		getButtonBar().getElement().getStyle().setProperty("borderTop", WINDOW_BOTTOM_BORDER_STYLE);
	}

	private IsWidget getServicesTab() {
		VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.add(getToggleButton(allServices), CHECK_BOX_LAYOUT_DATA);
		servicesDualListWidget = getDualListWidget(clientCommissionsDTO.getServicesIds(), allServicesWidgetList);
		container.add(servicesDualListWidget, DUAL_LIST_LAYOUT_DATA);
		return container;
	}

	private IsWidget getChannelsTab() {
		VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.add(getToggleButton(allChannels), CHECK_BOX_LAYOUT_DATA);
		channelsDualListWidget = getDualListWidget(clientCommissionsDTO.getChannelsIds(), allChannelsWidgetList);
		container.add(channelsDualListWidget, DUAL_LIST_LAYOUT_DATA);
		return container;
	}


	private DualListWidget getDualListWidget(List<String> items, List<DualListWidgetItem> WidgetList) {
		List<DualListWidgetItem> leftItems = new ArrayList<>();
		List<DualListWidgetItem> rightItems = new ArrayList<>();
		for (DualListWidgetItem entry : WidgetList) {
			if (!items.contains(entry.getId())) {
				leftItems.add(entry);
			} else {
				rightItems.add(entry);
			}
		}
		return new DualListWidget(leftItems, rightItems, this);
	}

	private ToolBar getToggleButton(ToggleButton toggleButton) {
		ToolBar toolBar = new ToolBar();
		toolBar.setEnableOverflow(false);

		toolBar.setPadding(new Padding(0));
		toolBar.setSpacing(0);
		toolBar.add(toggleButton);
		return toolBar;
	}


	private ZFieldLabel getFieldLabel(IsWidget field, String labelKey, boolean required) {
		return new ZFieldLabel.Builder()
			.labelWidth(150)
			.label(Mes.get(labelKey))
			.field(field)
			.required(required)
			.requiredSignColor("red")
			.build();
	}

	private String getHeaderValue(String tabName, Boolean all, int size) {
		String number = all ? Mes.get("all") : "" + size;
		return Mes.get(tabName) + ": " + number;
	}

	private ToggleButton getToggleButtonWithListener(String text) {
		ToggleButton toggleButton = new ToggleButton(Mes.get(text));
		toggleButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				onListItemsChanged();
			}
		});
		return toggleButton;
	}


	private boolean isValid() {
		return rateType.isValid() &&
			commission.isValid() &&
			validateAmountLimits();
	}

	private boolean validateAmountLimits() {
		BigDecimal com = commission.getCurrentValue();
		BigDecimal min = minCommission.getCurrentValue();
		BigDecimal max = maxCommission.getCurrentValue();

		if ((com != null) && com.compareTo(max) > 0) {
			commission.markInvalid("invalidCommissionMaxValue");
			return false;

		} else if ((com != null) && com.compareTo(min) < 0) {
			commission.markInvalid("invalidCommissionMinValue");
			return false;
		}

		if ((min != null && max != null) && min.compareTo(max) >= 0) {
			minCommission.markInvalid(Mes.get("invalidAmountMinMaxValues"));
			return false;
		}

		return true;
	}

	private ClientCommissionsDTO getClientCommissionsForUpdate() {
		clientCommissionsDTO.setAllServices(allServices.getValue());
		clientCommissionsDTO.setServicesIds(servicesDualListWidget.getSelectedItems());
		clientCommissionsDTO.setAllChannels(allChannels.getValue());
		clientCommissionsDTO.setChannelsIds(channelsDualListWidget.getSelectedItems());
		clientCommissionsDTO.setRateType(rateType.getValue());
		clientCommissionsDTO.setCommission(commission.getValue().setScale(2, BigDecimal.ROUND_FLOOR));
		if (minCommission.getValue() != null) {
			clientCommissionsDTO.setMinCommission(minCommission.getValue().setScale(2, BigDecimal.ROUND_FLOOR));
		}
		if (maxCommission.getValue() != null) {
			clientCommissionsDTO.setMaxCommission(maxCommission.getValue().setScale(2, BigDecimal.ROUND_FLOOR));
		}
		return clientCommissionsDTO;
	}


	private void setTabHeader(int index, DualListWidget listWidget, String tabName, Boolean all) {
		TabItemConfig config = tabPanel.getConfig(tabPanel.getWidget(index));
		config.setText(getHeaderValue(tabName, all, listWidget.getSelectedItems().size()));
		tabPanel.update(tabPanel.getWidget(index), config);
	}


	@Override
	public void onListItemsChanged() {
		setTabHeader(0, servicesDualListWidget, "services", allServices.getValue());
		setTabHeader(1, channelsDualListWidget, "channels", allChannels.getValue());
	}


	public abstract void onSave(ClientCommissionsDTO dto);
}
