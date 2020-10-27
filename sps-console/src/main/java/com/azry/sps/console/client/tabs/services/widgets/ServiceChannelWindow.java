package com.azry.sps.console.client.tabs.services.widgets;

import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.services.ServiceChannelInfoDto;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.BigDecimalField;

import java.util.ArrayList;
import java.util.List;

public class ServiceChannelWindow extends VerticalLayoutContainer {

	private final ToggleButton selectAllButton;

	private final FlexTable table;

	List<ServiceChannelInfoDto> channelInfos;

	List<ChannelInfoRow> rows;

	public ServiceChannelWindow(List<ServiceChannelInfoDto> entries, List<ChannelDTO> channels){
		super();
		table = new FlexTable();
		channelInfos = new ArrayList<>();
		rows = new ArrayList<>();

		selectAllButton = new ToggleButton(Mes.get("allChannels"));
		selectAllButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				for(ChannelInfoRow row : rows){
					row.getActiveBox().setEnabled(!selectAllButton.isEnabled());
				}
			}
		});

		setStyleName("ServiceChannelTable");


		add(selectAllButton);

		table.setWidget(0, 0, new HTML(Mes.get("active")));
		table.setWidget(0, 1, new HTML(Mes.get("channel")));
		table.setWidget(0, 2, new HTML(Mes.get("minAmountShort")));
		table.setWidget(0, 3, new HTML(Mes.get("maxAmountShort")));

		addEntries(entries, channels);
		table.getWidget(0, 0).setWidth("10px");
		table.getColumnFormatter().setWidth(0, "10px");
		table.getColumnFormatter().setWidth(1, "100%");
		add(table);
	}

	private void addEntries(List<ServiceChannelInfoDto> infos, List<ChannelDTO> channels) {
		int i = 1;
		if (infos == null) {
			infos = new ArrayList<>();
		}
		for(ChannelDTO dto : channels) {
			ServiceChannelInfoDto newInfo = getNewInfo(dto.getId());
			if (infos.contains(newInfo)) {
				newInfo = infos.get(infos.indexOf(newInfo));
			}
			channelInfos.add(newInfo);

			CheckBox activeBox = new CheckBox();

			if (newInfo.isActive()) {
				activeBox.setValue(true);
			}

			BigDecimalField minAmountField = new BigDecimalField();
			BigDecimalField maxAmountField = new BigDecimalField();

			rows.add(new ChannelInfoRow(activeBox, minAmountField, maxAmountField));

			table.setWidget(i, 0, activeBox);
			table.setWidget(i, 1, new HTML(dto.getName()));
			table.setWidget(i, 2, minAmountField);
			table.setWidget(i, 3, maxAmountField);
			i++;
		}
	}

	private ServiceChannelInfoDto getNewInfo(long id) {
		ServiceChannelInfoDto info = new ServiceChannelInfoDto();
		info.setChannelId(id);
		return info;
	}

	public List<ServiceChannelInfoDto> getChannelInfos(){
		for (int i = 0; i < rows.size(); i ++){
			if (!selectAllButton.isEnabled()) {
				channelInfos.get(i).setActive(true);
			}
			else {
				channelInfos.get(i).setActive(rows.get(i).getActiveBox().getValue());
			}

			if (rows.get(i).getMinAmountField().getValue() != null) {
				channelInfos.get(i).setMinAmount(rows.get(i).getMinAmountField().getValue());
			}

			if (rows.get(i).getMaxAmountField().getValue() != null) {
				channelInfos.get(i).setMaxAmount(rows.get(i).getMaxAmountField().getValue());
			}
		}

		return channelInfos;
	}

	private class ChannelInfoRow {

		private CheckBox activeBox;

		private BigDecimalField minAmountField;

		private BigDecimalField maxAmountField;

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

}
