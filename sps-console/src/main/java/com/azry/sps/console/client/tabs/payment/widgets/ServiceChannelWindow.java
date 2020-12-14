package com.azry.sps.console.client.tabs.payment.widgets;

import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.services.ServiceChannelInfoDTO;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;

import java.util.ArrayList;
import java.util.List;

public class ServiceChannelWindow extends Composite {

	private final ToggleButton selectAllButton;

	private final FlexTable table;

	List<ServiceChannelInfoDTO> channelInfos;

	List<ChannelInfoRow> rows;

	VerticalLayoutContainer container;

	public ServiceChannelWindow(List<ServiceChannelInfoDTO> entries, List<ChannelDTO> channels, boolean allSelected){
		super();
		container = new VerticalLayoutContainer();
		table = new FlexTable();
		channelInfos = new ArrayList<>();
		rows = new ArrayList<>();
		if (entries == null) {
			entries = new ArrayList<>();
		}
		ZToolBar toolbar = new ZToolBar();
		toolbar.setEnableOverflow(false);
		toolbar.setHeight(40);
		selectAllButton = new ToggleButton(Mes.get("allChannels"));
		selectAllButton.setValue(allSelected);
		selectAllButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				toggleCheckboxes(!event.getValue());
			}
		});
		table.setStyleName("ServiceChannelTable");

		toolbar.add(selectAllButton);
		toolbar.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
		toolbar.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
		container.add(toolbar);

		table.setWidget(0, 0, new HTML(Mes.get("active")));
		table.setWidget(0, 1, new HTML(Mes.get("channel")));
		table.setWidget(0, 2, new HTML(Mes.get("minAmountShort")));
		table.setWidget(0, 3, new HTML(Mes.get("maxAmountShort")));

		addEntries(entries, channels);
		table.getColumnFormatter().setWidth(0, "32px");

		table.getColumnFormatter().setWidth(1, "100%");
		container.add(table);
		toggleCheckboxes(!allSelected);
		initWidget(container);
	}

	private void toggleCheckboxes(boolean value) {
		for(ChannelInfoRow row : rows){
			row.getActiveBox().setEnabled(value);
		}
	}

	private void addEntries(List<ServiceChannelInfoDTO> infos, List<ChannelDTO> channels) {
		int i = 1;
		if (infos == null) {
			infos = new ArrayList<>();
		}
		for(ChannelDTO dto : channels) {
			ServiceChannelInfoDTO newInfo = getNewInfo(dto.getId());
			if (infos.contains(newInfo)) {
				newInfo = infos.get(infos.indexOf(newInfo));
			}
			channelInfos.add(newInfo);

			CheckBox activeBox = new CheckBox();

			if (newInfo.isActive()) {
				activeBox.setValue(true);
			}

			BigDecimalField minAmountField = new BigDecimalField();
			minAmountField.setWidth(140);
			if (newInfo.getMinAmount() != null) {
				minAmountField.setValue(newInfo.getMinAmount());
			}

			BigDecimalField maxAmountField = new BigDecimalField();
			maxAmountField.setWidth(140);
			if (newInfo.getMaxAmount() != null) {
				maxAmountField.setValue(newInfo.getMaxAmount());
			}

			rows.add(new ChannelInfoRow(activeBox, minAmountField, maxAmountField));

			table.setWidget(i, 0, activeBox);
			table.setWidget(i, 1, new HTML(dto.getName()));
			table.setWidget(i, 2, minAmountField);
			table.setWidget(i, 3, maxAmountField);
			i++;
		}
	}

	private ServiceChannelInfoDTO getNewInfo(long id) {
		ServiceChannelInfoDTO info = new ServiceChannelInfoDTO();
		info.setChannelId(id);
		return info;
	}

	public boolean isValid() {
		//Logger logger = java.util.logging.Logger.getLogger("NameOfYourLogger");
		boolean valid = true;
		for(ChannelInfoRow row : rows) {
			//logger.log(Level.SEVERE, "!!!");
			if (row.getMinAmountField().getValue() != null &&
				row.getMaxAmountField().getValue() != null &&
				row.getMinAmountField().getValue().compareTo(row.getMaxAmountField().getValue()) > 0) {
					row.getMinAmountField().markInvalid(Mes.get("invalidAmountMinMaxValues"));
					row.getMaxAmountField().markInvalid(Mes.get("invalidAmountMinMaxValues"));
					valid = false;
			}
		}
		return valid;
	}

	public ChannelInfo getChannelInfos(){
		for (int i = 0; i < rows.size(); i ++){
			channelInfos.get(i).setActive(rows.get(i).getActiveBox().getValue());
			channelInfos.get(i).setMinAmount(rows.get(i).getMinAmountField().getValue());
			channelInfos.get(i).setMaxAmount(rows.get(i).getMaxAmountField().getValue());
		}

		return new ChannelInfo(channelInfos, selectAllButton.getValue());
	}

	private static class ChannelInfoRow {

		private final CheckBox activeBox;

		private final BigDecimalField minAmountField;

		private final BigDecimalField maxAmountField;

		public ChannelInfoRow(CheckBox activeBox, BigDecimalField minAmountField, BigDecimalField maxAmountField) {
			this.activeBox = activeBox;
			this.minAmountField = minAmountField;
			this.maxAmountField = maxAmountField;
		}

		public CheckBox getActiveBox() {
			return activeBox;
		}

		public BigDecimalField getMinAmountField() {
			return minAmountField;
		}

		public BigDecimalField getMaxAmountField() {
			return maxAmountField;
		}
	}

	static class ChannelInfo {

		List<ServiceChannelInfoDTO> channels;

		boolean allChannels;

		ChannelInfo(List<ServiceChannelInfoDTO> ch, boolean allChannels) {
			this.channels = ch;
			this.allChannels = allChannels;
		}

		public List<ServiceChannelInfoDTO> getChannels() {
			return channels;
		}

		public boolean isAllChannels() {
			return allChannels;
		}
	}

}
