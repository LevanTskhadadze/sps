package com.azry.sps.console.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class ConsoleEntryPoint implements EntryPoint {

	public static final Viewport VIEWPORT = new Viewport();

	@Override
	public void onModuleLoad() {
		RootPanel.get().add(VIEWPORT);
		VIEWPORT.add(new HTML("Console page"));
	}
}