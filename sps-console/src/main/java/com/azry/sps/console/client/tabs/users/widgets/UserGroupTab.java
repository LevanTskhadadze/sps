package com.azry.sps.console.client.tabs.users.widgets;

import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDTO;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;

import java.util.ArrayList;
import java.util.List;

public class UserGroupTab extends FlexTable {

	private final CheckBox selectAllBox;

	private final List<CheckBox> checkBoxes;

	private final List<UserGroupDTO> groups;

	public UserGroupTab(List<UserGroupDTO> entries, List<UserGroupDTO> initialGroups, ActionMode actionMode){
		super();
		groups = entries;
		checkBoxes = new ArrayList<>();
		selectAllBox = new CheckBox();
		selectAllBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				for(CheckBox checkBox : checkBoxes){
					checkBox.setValue(selectAllBox.getValue());
				}
			}
		});

		setStyleName("userGroupTable");

		HTML header = new HTML(Mes.get("userGroups"));
		header.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
		header.getElement().getStyle().setColor("#666666");

		setWidget(0, 0, selectAllBox);
		setWidget(0, 1, header);

		addEntries(initialGroups);
		setBorderWidth(1);
		getWidget(0, 0).setWidth("10px");
		getColumnFormatter().setWidth(0, "10px");
		getColumnFormatter().setWidth(1, "100%");

		if (ActionMode.VIEW.equals(actionMode)) {
			disableCheckboxes();
		}
	}

	private void addEntries(List<UserGroupDTO> initialGroups) {
		int i = 1;


		for(UserGroupDTO dto : groups){
			CheckBox curr = new CheckBox();
			checkBoxes.add(curr);

			if (initialGroups.contains(dto)) {
				curr.setValue(true);
			}

			setWidget(i, 0, curr);
			setWidget(i, 1, new HTML(dto.getName()));
			i ++;
		}
	}

	public boolean validate() {
		for (CheckBox box : checkBoxes){
			if (box.getValue()) return true;
		}
		return false;
	}

	public List<UserGroupDTO> getAllSelectedGroups(){
		List<UserGroupDTO> res = new ArrayList<>();
		for (int i = 0; i < checkBoxes.size(); i ++){
			if (checkBoxes.get(i).getValue()) {
				res.add(groups.get(i));
			}
		}
		return res;
	}

	private void disableCheckboxes() {
		selectAllBox.setEnabled(false);
		for (CheckBox checkbox : checkBoxes) {
			checkbox.setEnabled(false);
		}
	}
}
