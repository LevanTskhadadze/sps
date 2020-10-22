package com.azry.sps.console.client.tabs.users;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZPagingToolBar;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.users.widgets.UsersModifyWindow;
import com.azry.sps.console.client.tabs.users.widgets.UsersTable;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;

import com.azry.sps.console.shared.dto.usergroup.UserGroupDto;
import com.azry.sps.console.shared.dto.users.SystemUserDTO;
import com.google.gwt.dom.client.Style;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.data.shared.loader.PagingLoadConfig;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.data.shared.loader.PagingLoader;
import com.sencha.gxt.theme.neptune.client.base.button.Css3ButtonCellAppearance;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsersTab extends Composite {
	private static final int DEFAULT_PAGE_SIZE = 50;

	private final VerticalLayoutContainer content;

	private  ZTextField usernameField;

	private ZTextField nameField;

	private ZTextField emailField;

	private ZSimpleComboBox<UserGroupDto> groupComboBox;

	private ZSimpleComboBox<String> activeComboBox;

	private ZPagingToolBar pager;

	private PagingLoader<PagingLoadConfig, PagingLoadResult<SystemUserDTO>> pagingLoader;

	ZGrid<SystemUserDTO> grid;

	public UsersTab(List<UserGroupDto> result){
		super();
		content = new VerticalLayoutContainer();
		buildGroupField(result);

		initToolbar();

		initWidget(content);

		assembleContent();

		initPaging();



	}

	private void initPaging() {
		RpcProxy<PagingLoadConfig, PagingLoadResult<SystemUserDTO>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<SystemUserDTO>>() {
			@Override
			public void load(PagingLoadConfig loadConfig, final AsyncCallback<PagingLoadResult<SystemUserDTO>> callback) {
				Map<String, String> params = new HashMap<>();
				params.put("username", usernameField.getValue());
				params.put("name", nameField.getValue());
				params.put("email", emailField.getValue());
				params.put("active", activeComboBox.getValue());
				if(groupComboBox.getValue() != null) {
					params.put("groups", groupComboBox.getValue().getName());
				}
				//params.put("active", usernameField.getValue());
				ServicesFactory.getUserTabService().getUsers(loadConfig.getOffset(), loadConfig.getLimit(), params, new ServiceCallback<PagingLoadResult<SystemUserDTO>>() {
					@Override
					public void onServiceSuccess(PagingLoadResult<SystemUserDTO> result) {
						callback.onSuccess(result);
					}
				});

			}
		};
		pagingLoader = new PagingLoader<>(proxy);
		pagingLoader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, SystemUserDTO, PagingLoadResult<SystemUserDTO>>(UsersTable.getListStore()));
		grid.setLoader(pagingLoader);
		pager = new ZPagingToolBar.Builder<>(pagingLoader).build();
		content.add(pager);
		pagingLoader.load();
	}

	private void initToolbar() {
		usernameField = new ZTextField.Builder()
			.emptyText(Mes.get("username"))
			.build();

		nameField = new ZTextField.Builder()
			.emptyText(Mes.get("name"))
			.build();

		emailField = new ZTextField.Builder()
			.emptyText(Mes.get("email"))
			.build();

		buildActiveComboBox();
	}

	private void buildActiveComboBox() {
		List<String> values = new ArrayList<>();
		values.add("1");
		values.add("0");
		activeComboBox = new ZSimpleComboBox.Builder<String>()
			.keyProvider(new ModelKeyProvider<String>() {
				@Override
				public String getKey(String item) {
					return item;
				}
			})
			.labelProvider(new LabelProvider<String>() {
				@Override
				public String getLabel(String item) {
					return (item.equals("1") ? Mes.get("active") : Mes.get("inactive"));
				}
			})
			.values(values)
			.editable(false)
			.width(200)
			.listWidth(400)
			.noSelectionLabel(Mes.get("active") + "/" + Mes.get("inactive"))
			.build();
	}

	private void buildGroupField(List<UserGroupDto> entries){
		groupComboBox = new ZSimpleComboBox.Builder<UserGroupDto>()
			.emptyText(Mes.get("valueEmptyText"))
			.keyProvider(new ModelKeyProvider<UserGroupDto>() {
				@Override
				public String getKey(UserGroupDto item) {
					return String.valueOf(item.getId());
				}
			})
			.labelProvider(new LabelProvider<UserGroupDto>() {
				@Override
				public String getLabel(UserGroupDto item) {
					return item.getName();
				}
			})
			.values(entries)
			.editable(false)
			.width(200)
			.noSelectionLabel(Mes.get("allGroups"))
			.listWidth(400)
			.enableSorting(false)
			.build();

		groupComboBox.getElement().getStyle().setOverflow(Style.Overflow.HIDDEN);
	}


	private void assembleContent(){

		content.add(getToolbar());
		grid = new ZGrid<>(UsersTable.setListStore(new ArrayList<SystemUserDTO>()), UsersTable.getMyColumnModel());
		grid.setColumnResize(false);
		grid.getView().setForceFit(true);
		grid.getView().setColumnLines(true);
		content.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));

	}

	private ZToolBar getToolbar() {
		ZToolBar toolbar = new ZToolBar();
		toolbar.setEnableOverflow(false);
		toolbar.add(usernameField);
		toolbar.add(nameField);
		toolbar.add(emailField);
		toolbar.add(groupComboBox);
		toolbar.add(activeComboBox);

		toolbar.add(getSearchButton());
		toolbar.add(getClearButton());

		ZButton addButton = new ZButton.Builder()
			.appearance (new Css3ButtonCellAppearance<String>())
			.icon (FAIconsProvider.getIcons().plus())
			.text (Mes.get("addEntry"))
			.handler (new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					new UsersModifyWindow(null, UsersTable.getListStore());
				}
			})
			.build();

		addButton.getElement().getStyle().setColor("white");
		toolbar.add(addButton);

		toolbar.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
		toolbar.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
		toolbar.getElement().getStyle().setHeight(2, Style.Unit.EM);
		toolbar.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);
		return toolbar;
	}


	private ZButton getSearchButton(){
		return new ZButton.Builder()
			.appearance(new Css3ButtonCellAppearance<String>())
			.icon(FAIconsProvider.getIcons().search())
			.text(Mes.get("searchEntry"))
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					pagingLoader.load();

				}
			})
			.build();
	}


	private ZButton getClearButton(){
		return new ZButton.Builder()
			.appearance(new Css3ButtonCellAppearance<String>())
			.icon(FAIconsProvider.getIcons().eraser())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					usernameField.setText(null);
					emailField.setText(null);
					nameField.setText(null);
					groupComboBox.setValue(null);

				}
			} )
			.build();
	}


}
