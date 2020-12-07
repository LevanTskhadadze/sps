package com.azry.sps.console.client.tabs.usergroup;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZFieldLabel;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDTO;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.tree.Tree;

public abstract class UserGroupWindow extends ZWindow {

	private final static String WINDOW_BOTTOM_BORDER_STYLE = "1px solid #3291D6";

	private final VerticalLayoutContainer container = new VerticalLayoutContainer();

	private UserGroupDTO userGroupDTO;

	private ZTextField nameField;

	private final Tree<PermissionTreeModel, String> tree;

	public UserGroupWindow(UserGroupDTO dto, ActionMode actionMode) {
		super(Mes.get("ofUserGroup") + " " + Mes.get("ActionMode_" + actionMode), 600, 500, false);
		tree = PermissionTree.createTree();
		userGroupDTO = dto;
		initFields();
		initButtons(actionMode);
		addBottomHorizontalLine();
		if (userGroupDTO != null) {
			setFieldValues();
		}
		add(container);
		if (ActionMode.VIEW.equals(actionMode)) {
			disableTreeNodes();
		}
	}

	private void initFields() {

		nameField = new ZTextField.Builder()
			.maxLength(300)
			.required(true)
			.build();
		VerticalLayoutContainer.VerticalLayoutData layoutData = new VerticalLayoutContainer.VerticalLayoutData(1, -1);

		container.add(getFieldLabel(nameField, "name", FormPanel.LabelAlign.LEFT,true), layoutData);
		container.add(getFieldLabel(tree, "permissions", FormPanel.LabelAlign.TOP, false));
	}

	private ZFieldLabel getFieldLabel(IsWidget field, String labelKey, FormPanel.LabelAlign labelAlign, boolean required) {
		return new ZFieldLabel.Builder()
			.labelWidth(100)
			.label(Mes.get(labelKey))
			.labelAlign(labelAlign)
			.field(field)
			.required(required)
			.requiredSignColor("red")
			.build();
	}


	protected  void initButtons(ActionMode actionMode){
		ZButton saveButton = new ZButton.Builder()
			.visible(!ActionMode.VIEW.equals(actionMode))
			.text(Mes.get("save"))
			.icon(FAIconsProvider.getIcons().floppy_o_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					if (isValid()) {
						ServicesFactory.getUserGroupService().updateUserGroup(getUserGroupForUpdate(),
							new ServiceCallback<UserGroupDTO>(UserGroupWindow.this) {
							@Override
							public void onServiceSuccess(UserGroupDTO result) {
								hide();
								onSave(result);
							}
						});
					}
				}
			})
			.build();

		ZButton closeButton = new ZButton.Builder()
			.text(Mes.get("quit"))
			.icon(FAIconsProvider.getIcons().ban_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					hide();
				}
			})
			.build();

		addButton(saveButton);
		addButton(closeButton);
	}

	private void setFieldValues() {
		nameField.setValue(userGroupDTO.getName());
		PermissionTree.setCheckedPermissions(tree, userGroupDTO.getPermissions());
	}


	private void addBottomHorizontalLine() {
		getButtonBar().getElement().getStyle().setProperty("borderTop", WINDOW_BOTTOM_BORDER_STYLE);
	}

	private boolean isValid() {
		return nameField.isValid();
	}

	private UserGroupDTO getUserGroupForUpdate(){
		if (userGroupDTO == null) {
			userGroupDTO = new UserGroupDTO();
			userGroupDTO.setActive(true);
		}
		userGroupDTO.setName(nameField.getValue());
		userGroupDTO.setPermissions(PermissionTree.getCheckedPermissions(tree));

		return userGroupDTO;
	}

	private void disableTreeNodes() {
		nameField.disable();
		tree.disable();
	}

	public abstract void onSave(UserGroupDTO dto);
}
