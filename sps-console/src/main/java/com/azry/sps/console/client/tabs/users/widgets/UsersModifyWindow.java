package com.azry.sps.console.client.tabs.users.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.ActionMode;
import com.azry.sps.console.client.utils.DialogUtils;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.usergroup.UserGroupDTO;
import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.PasswordField;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class UsersModifyWindow extends ZWindow {
	
	private static final int FIELD_WIDTH = 290;

	private boolean redactMode = false;

	private ZTextField usernameField;

	private ZTextField nameField;

	private ZTextField emailField;

	private PasswordField passwordField;

	private PasswordField repeatPasswordField;

	private SystemUserDTO dto;

	private final TabPanel tabPanel;

	private final VerticalLayoutContainer formContainer = new VerticalLayoutContainer();

	final VerticalLayoutContainer userGroupTabContainer = new VerticalLayoutContainer();

	private final ListStore<SystemUserDTO> store;

	private UserGroupTab userGroupTab;

	ActionMode actionMode;

	public UsersModifyWindow(SystemUserDTO dto, ListStore<SystemUserDTO> store, ActionMode actionMode) {
		super(Mes.get("ofUser") + " " + Mes.get(actionMode.name().toLowerCase()), 550, 500, false);

		tabPanel = new TabPanel();

		this.actionMode = actionMode;
		this.store = store;

		if (dto != null) {
			redactMode = true;
			this.dto = dto;
		}
		else {
			this.dto = new SystemUserDTO();
			this.dto.setPassword("");
			this.dto.setName("");
			this.dto.setEmail("");
			this.dto.setActive(false);
			this.dto.setGroups(new ArrayList<UserGroupDTO>());
			this.dto.setId(0);
		}

		ZButton confirmButton = getConfirmButton();
		ZButton cancelButton = getCancelButton();
		buttonBar.setMinButtonWidth(75);
		getButtonBar().getElement().getStyle().setProperty("borderTop", "1px solid #3291D6");

		addButton(confirmButton);
		addButton(cancelButton);

		formContainer.setStyleName("editForm");

		constructFormTab();
		constructUserGroupTab();

		add(tabPanel, new MarginData(0));

		if (ActionMode.VIEW.equals(actionMode)) {
			disableFields();
		}

		showInCenter();
	}

	private String getRequiredFieldNotification() {
		return "<span style='color: red;'>*</span>";
	}

	private void constructFormTab(){
		formContainer.add(constructForm());

		tabPanel.add(formContainer, Mes.get("user"));
	}

	private void constructUserGroupTab(){
		final List<UserGroupDTO> entries = new ArrayList<>();
		ServicesFactory.getUserGroupService().getUserGroups( new ServiceCallback<List<UserGroupDTO>>(this) {
			@Override
			public void onServiceSuccess(List<UserGroupDTO> result) {
				entries.addAll(result);
				userGroupTabContainer.add(userGroupTab = new UserGroupTab(entries, dto.getGroups(), actionMode), new VerticalLayoutContainer.VerticalLayoutData(1, -1));
			}
		});
		tabPanel.add(userGroupTabContainer, Mes.get("userGroups"));
	}

	private FlexTable constructForm() {
		FlexTable formTable = new FlexTable();
		formTable.getElement().getStyle().setMarginTop(15, com.google.gwt.dom.client.Style.Unit.PX);
		formTable.getElement().getStyle().setMarginLeft(10, com.google.gwt.dom.client.Style.Unit.PX);

		formTable.setWidget(0, 0, new HTML(Mes.get("username") + getRequiredFieldNotification() + ":"));
		formTable.setWidget(1, 0, new HTML(Mes.get("name") + getRequiredFieldNotification() + ":"));
		formTable.setWidget(2, 0, new HTML(Mes.get("email") + ":"));
		formTable.setWidget(3, 0, new HTML(Mes.get("password") + getRequiredFieldNotification() + ":"));
		formTable.setWidget(4, 0, new HTML(Mes.get("repeatPassword") + getRequiredFieldNotification()  + ":"));
		formTable.getWidget(0, 0).setWidth("220px");

		formTable.setWidget(0, 1, getUsernameField());
		formTable.setWidget(1, 1, getNameField());
		formTable.setWidget(2, 1, getEmailField());
		formTable.setWidget(3, 1, getPasswordField());
		formTable.setWidget(4, 1, getRepeatPasswordField());
		formTable.setCellSpacing(4);

		return formTable;
	}

	private ZTextField getEmailField() {
		emailField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.build();
		if (redactMode) {
			emailField.setValue(dto.getEmail());
		}
		return emailField;
	}

	private ZTextField getUsernameField() {
		usernameField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.build();
		if (redactMode) {
			usernameField.setValue(dto.getUsername());
		}
		return usernameField;
	}

	private ZTextField getNameField() {
		nameField = new ZTextField.Builder()
			.width(FIELD_WIDTH)
			.build();
		if (redactMode) {
			nameField.setValue(dto.getName());
		}
		return nameField;
	}

	private PasswordField getPasswordField() {
		passwordField = new PasswordField();
		passwordField.setWidth(FIELD_WIDTH);

		return passwordField;
	}

	private PasswordField getRepeatPasswordField() {
		repeatPasswordField = new PasswordField();
		repeatPasswordField.setWidth(FIELD_WIDTH);

		return repeatPasswordField;
	}


	private ZButton getCancelButton() {
		return new ZButton.Builder()
			.icon(FAIconsProvider.getIcons().ban_white())
			.text(Mes.get("discard"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					hide();
				}
			})
			.build();
	}

	private boolean validate() {
		boolean good = true;
		if(usernameField.getCurrentValue() == null || usernameField.getCurrentValue().equals("")) {
			good = false;
			usernameField.markInvalid(Mes.get("requiredField"));
		}

		if(nameField.getCurrentValue() == null || nameField.getCurrentValue().equals("")) {
			good = false;
			nameField.markInvalid(Mes.get("requiredField"));
		}

		if(!redactMode){
			if(passwordField.getCurrentValue() == null || passwordField.getCurrentValue().equals("")) {
				good = false;
				passwordField.markInvalid(Mes.get("requiredField"));
			}
		}

		if(passwordField.getCurrentValue() != null && !passwordField.getCurrentValue().equals("")
			&& !passwordField.getCurrentValue().equals(repeatPasswordField.getCurrentValue())) {
			good = false;
			repeatPasswordField.markInvalid(Mes.get("passwordMismatch"));

		}
		boolean groupTabValid = userGroupTab.validate();
		if (!good) {
			tabPanel.setActiveWidget(formContainer);
		} else if (!groupTabValid) {
			tabPanel.setActiveWidget(userGroupTabContainer);
			DialogUtils.showWarning(Mes.get("groupsRequired"));
		}
		return good & groupTabValid;
	}
	private boolean doRedact() {
		if(!validate()) return false;

		dto.setName(nameField.getCurrentValue());
		dto.setUsername(usernameField.getCurrentValue());
		if(passwordField.getCurrentValue() != null && !passwordField.getCurrentValue().equals("")) {
			dto.setPassword(passwordField.getCurrentValue());
		}
		if(emailField.getCurrentValue() != null && !emailField.getCurrentValue().equals("")) {
			dto.setEmail(emailField.getCurrentValue());
		}

		dto.setGroups(userGroupTab.getAllSelectedGroups());
		dto.setLastUpdateTime(new Date());

		ServicesFactory.getUserTabService().editParameter(dto,
			new ServiceCallback<SystemUserDTO>(this) {

				@Override
				public void onServiceSuccess(SystemUserDTO newDTO) {
					dto = newDTO;
					store.update(dto);
					store.applySort(false);
				}
			});
		return true;
	}

	private boolean doAdd() {
		if(!validate()) {
			return false;
		}
		dto = new SystemUserDTO();
		dto.setName(nameField.getCurrentValue());
		dto.setUsername(usernameField.getCurrentValue());
		dto.setPassword(passwordField.getCurrentValue());
		dto.setEmail(emailField.getCurrentValue());
		dto.setGroups(userGroupTab.getAllSelectedGroups());
		dto.setLastUpdateTime(new Date());
		dto.setCreateTime(new Date());

		ServicesFactory.getUserTabService().addParameter(
			dto,
			new ServiceCallback<SystemUserDTO>(this) {

				@Override
				public void onServiceSuccess(SystemUserDTO SystemUserDTO) {
					store.add(SystemUserDTO);
				}
			});

		return true;
	}


	private ZButton getConfirmButton() {
		return new ZButton.Builder()
			.visible(!ActionMode.VIEW.equals(actionMode))
			.icon(FAIconsProvider.getIcons().floppy_o_white())
			.text(Mes.get("save"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					if (redactMode) {
						if(!doRedact()) return;
					}
					else{
						if(!doAdd()) return;
					}

					hide();
				}
			})
			.build();
	}

	private void disableFields() {
		usernameField.disable();
		nameField.disable();
		emailField.disable();
		passwordField.disable();
		repeatPasswordField.disable();
	}
}
