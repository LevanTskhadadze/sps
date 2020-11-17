package com.azry.sps.console.client.tabs.perfompayment;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.user.client.ui.HasVerticalAlignment.ALIGN_MIDDLE;

public class TableUtils {

	public static void setCell(FlexTable table, int row, int column,
							   Widget widget, String width, String style,
							   HasHorizontalAlignment.HorizontalAlignmentConstant h,
							   boolean loading) {

		table.setWidget(row, column, widget);
		table.getFlexCellFormatter().setVerticalAlignment(row, column, ALIGN_MIDDLE);
		if (width != null) {
			table.getFlexCellFormatter().setWidth(row, column, width);
		}
		if (style != null) {
			table.getFlexCellFormatter().setStyleName(row, column, style);
		}
		if (loading) {
			widget.setStyleName("loader");
		}
		if (h != null) {
			table.getFlexCellFormatter().setHorizontalAlignment(row, column, h);
		}
	}

}