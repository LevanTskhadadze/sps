package com.azry.sps.console.client.tabs.commissions;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.event.StoreAddEvent;
import com.sencha.gxt.data.shared.event.StoreClearEvent;
import com.sencha.gxt.data.shared.event.StoreDataChangeEvent;
import com.sencha.gxt.data.shared.event.StoreRemoveEvent;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DualListWidget extends Composite {

	private final ListStore<DualListWidgetItem> selectedItems;

	interface Properties extends PropertyAccess<DualListWidgetItem> {
		ModelKeyProvider<DualListWidgetItem> id();

		@Editor.Path("name")
		ValueProvider<DualListWidgetItem, String> nameProp();
	}

	public DualListWidget(List<DualListWidgetItem> leftItems, List<DualListWidgetItem> rightItems, final ListElementsChangeListener listener) {
		VerticalLayoutContainer container = new VerticalLayoutContainer();

		Properties props = GWT.create(Properties.class);
		ListStore<DualListWidgetItem> allItems = new ListStore<>(props.id());
		allItems.addSortInfo(new Store.StoreSortInfo<>(new Comparator<DualListWidgetItem>() {
			@Override
			public int compare(DualListWidgetItem o1, DualListWidgetItem o2) {
				return o1.getName().compareTo(o2.getName());
			}
		}, SortDir.ASC));
		allItems.addAll(leftItems);

		selectedItems = new ListStore<>(props.id());
		selectedItems.addAll(rightItems);

		DualListField<DualListWidgetItem, String> field = new DualListField<>(allItems, selectedItems, props.nameProp(), new TextCell());

		field.addValidator(new EmptyValidator<List<DualListWidgetItem>>());
		field.setEnableDnd(true);
		field.setMode(DualListField.Mode.APPEND);
		field.getToStore().addStoreDataChangeHandler(new StoreDataChangeEvent.StoreDataChangeHandler<DualListWidgetItem>() {
			@Override
			public void onDataChange(StoreDataChangeEvent<DualListWidgetItem> event) {
				listener.onListItemsChanged();
			}
		});
		field.getToStore().addStoreAddHandler(new StoreAddEvent.StoreAddHandler<DualListWidgetItem>() {
			@Override
			public void onAdd(StoreAddEvent<DualListWidgetItem> event) {
				listener.onListItemsChanged();
			}
		});
		field.getToStore().addStoreRemoveHandler(new StoreRemoveEvent.StoreRemoveHandler<DualListWidgetItem>() {
			@Override
			public void onRemove(StoreRemoveEvent<DualListWidgetItem> event) {
				listener.onListItemsChanged();
			}
		});

		field.getToStore().addStoreClearHandler(new StoreClearEvent.StoreClearHandler<DualListWidgetItem>() {
			@Override
			public void onClear(StoreClearEvent<DualListWidgetItem> event) {
				listener.onListItemsChanged();
			}
		});

		container.add(field, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
		initWidget(container);
	}

	public List<String> getSelectedItems() {
		List<String> res = new ArrayList<>();
		for (DualListWidgetItem selectedItem : selectedItems.getAll()) {
			res.add(selectedItem.getId());
		}
		return res;
	}

	public interface ListElementsChangeListener {
		void onListItemsChanged();
	}
}
