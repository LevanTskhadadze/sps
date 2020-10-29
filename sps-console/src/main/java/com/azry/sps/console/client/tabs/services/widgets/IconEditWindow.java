package com.azry.sps.console.client.tabs.services.widgets;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZFieldLabel;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsonUtils;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.PagingLoadResult;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.info.Info;

public abstract class  IconEditWindow extends ZWindow {

	private final static String WINDOW_BOTTOM_BORDER_STYLE = "1px solid #3291D6";

	private long id;

	private VerticalLayoutContainer vlc;
	private FileUploadField upload;
	private FormPanel formPanel;
	String type;
	ListStore<ServiceDto> store;

	public IconEditWindow(String type, long id, ListStore<ServiceDto> store) {
		super(Mes.get("uploadIcon"), 300, 120, false);
		this.type = type;
		this.id = id;
		this.store = store;

		initFields();
		initForm();
		initButtons();
		addBottomHorizontalLine();

		add(formPanel);
		setHeight("150px");
	}

	private void initFields() {
		vlc = new VerticalLayoutContainer();

		upload = new FileUploadField();
		upload.setName(id + "-services");
		vlc.add(new ZFieldLabel.Builder()
				.label(Mes.get("icon"))
				.field(upload)
				.labelWidth(50)
				.build(), new VerticalLayoutContainer.VerticalLayoutData(1, -1));

	}

	private void initForm() {
		formPanel = new FormPanel();
		formPanel.setAction(GWT.getModuleBaseURL() + "servlet/iconUpload");
		formPanel.setEncoding(FormPanel.Encoding.MULTIPART);
		formPanel.setMethod(FormPanel.Method.POST);
		formPanel.addSubmitCompleteHandler(new SubmitCompleteEvent.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				ServicesFactory.getServiceTabService().getService(id, new ServiceCallback<ServiceDto>() {
					@Override
					public void onServiceSuccess(ServiceDto result) {
						store.update(result);
						hide();
					}
				});
/*				JavaScriptObject response = JsonUtils.safeEval(event.getResults());

				if ("OK".equals(response.toString())) {
					onSave();
					hide();
					Info.display(Mes.get("note"), Mes.get("iconSuccessfullyUploaded"));
				}/* else {
					DialogUtils.showWarning(response.getInfo());
				}*/
			}
		});
		formPanel.add(vlc);
	}

	private void initButtons() {
		addButton(new ZButton.Builder()
				.text(Mes.get("upload"))
				.icon(FAIconsProvider.getIcons().floppy_o_white())
				.handler(new SelectEvent.SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						if (!upload.getValue().isEmpty()) {
							formPanel.submit();
						}
					}
				})
				.build());

		addButton(new ZButton.Builder()
				.icon(FAIconsProvider.getIcons().ban_white())
				.text(Mes.get("close"))
				.handler(new SelectEvent.SelectHandler() {
					@Override
					public void onSelect(SelectEvent event) {
						hide();
					}
				})
				.build());
	}

	private void addBottomHorizontalLine() {
		getButtonBar().getElement().getStyle().setProperty("borderTop", WINDOW_BOTTOM_BORDER_STYLE);
	}

	public abstract void onSave();
}