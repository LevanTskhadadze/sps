package com.azry.sps.console.client.tabs.users.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
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
import java.util.logging.Level;
import java.util.logging.Logger;


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

	public UsersModifyWindow(SystemUserDTO dto, ListStore<SystemUserDTO> store) {
		super();

		tabPanel = new TabPanel();

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

		setHeight("500px");
		setWidth("550px");
		String header = Mes.get("systemParam") + " " + (redactMode ? Mes.get("redact") : Mes.get("addEntry"));
		setHeadingText(header);
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
		ServicesFactory.getUserGroupService().getUserGroups( new ServiceCallback<List<UserGroupDTO>>() {
			@Override
			public void onServiceSuccess(List<UserGroupDTO> result) {
				entries.addAll(result);
				userGroupTabContainer.add(userGroupTab = new UserGroupTab(entries, dto.getGroups()), new VerticalLayoutContainer.VerticalLayoutData(1, -1));

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
		if(usernameField.getValue() == null || usernameField.getValue().equals("")) {
			good = false;
			usernameField.markInvalid(Mes.get("requiredField"));
		}

		if(nameField.getValue() == null || nameField.getValue().equals("")) {
			good = false;
			nameField.markInvalid(Mes.get("requiredField"));
		}

		if(!redactMode){
			if(passwordField.getValue() == null || passwordField.getValue().equals("")) {
				good = false;
				passwordField.markInvalid(Mes.get("requiredField"));
			}
		}

		if(passwordField.getValue() != null && !passwordField.getValue().equals("")
			&& !passwordField.getValue().equals(repeatPasswordField.getValue())) {
			good = false;
			repeatPasswordField.markInvalid(Mes.get("passwordMismatch"));

		}
		boolean groupTabValid = userGroupTab.validate();
		if(!groupTabValid) {
			tabPanel.setActiveWidget(userGroupTabContainer);
		}

		return good & groupTabValid;
	}

	private boolean doRedact() {
		if(!validate()) return false;

		dto.setName(nameField.getValue());
		dto.setUsername(usernameField.getValue());
		if(passwordField.getValue() != null && !passwordField.getValue().equals("")) {
			dto.setPassword(passwordField.getValue());
		}
		if(emailField.getValue() != null && !emailField.getValue().equals("")) {
			dto.setEmail(emailField.getValue());
		}

		dto.setGroups(userGroupTab.getAllSelectedGroups());
		dto.setLastUpdateTime(new Date());

		ServicesFactory.getUserTabService().editParameter(dto,
			new ServiceCallback<SystemUserDTO>() {

				@Override
				public void onServiceSuccess(SystemUserDTO newDto) {
					dto = newDto;
					Logger logger = java.util.logging.Logger.getLogger("NameOfYourLogger");
					logger.log(Level.SEVERE, "" + dto.getVersion());

					store.update(dto);
				}
			});
		return true;
	}

	private boolean doAdd() {
		if(!validate()) {
			return false;
		}
		dto = new SystemUserDTO();
		dto.setName(nameField.getValue());
		dto.setUsername(usernameField.getValue());
		dto.setPassword(passwordField.getValue());
		dto.setEmail(emailField.getValue());
		dto.setGroups(userGroupTab.getAllSelectedGroups());
		dto.setLastUpdateTime(new Date());

		ServicesFactory.getUserTabService().addParameter(
			dto,
			new ServiceCallback<SystemUserDTO>() {

				@Override
				public void onServiceSuccess(SystemUserDTO SystemUserDTO) {
					store.add(SystemUserDTO);
				}
			});

		return true;
	}


	private ZButton getConfirmButton() {
		return new ZButton.Builder()
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
}
