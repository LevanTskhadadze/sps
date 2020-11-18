package com.azry.sps.console.client.tabs.payment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZDateField;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZMultiSelectComboBox;
import com.azry.gxt.client.zcomp.ZNumberField;
import com.azry.gxt.client.zcomp.ZPagingToolBar;
import com.azry.gxt.client.zcomp.ZSimpleComboBox;
import com.azry.gxt.client.zcomp.ZTextField;
import com.azry.gxt.client.zcomp.ZToolBar;
import com.azry.sps.console.client.ServicesFactory;
import com.azry.sps.console.client.tabs.payment.widgets.PaymentTable;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.ServiceCallback;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.console.shared.dto.payment.PaymentDto;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDto;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
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
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentTab extends Composite {



	private final VerticalLayoutContainer content;

	private ZNumberField<Long> idField;

	private ZTextField agentPaymentIdField;

	private ZDateField creationStartTimeField;

	private ZDateField creationEndTimeField;

	private ZSimpleComboBox<ServiceDto> serviceComboBox;

	private ZSimpleComboBox<ChannelDTO> channelComboBox;

	private ZMultiSelectComboBox<PaymentStatusDto> paymentStatusComboBox;

	private PagingLoader<PagingLoadConfig, PagingLoadResult<PaymentDto>> pagingLoader;

	ZGrid<PaymentDto> grid;

	public PaymentTab(){
		super();
		content = new VerticalLayoutContainer();

		initToolbar();

		initWidget(content);

		assembleContent();

		initPaging();

	}

	private void initPaging() {
		RpcProxy<PagingLoadConfig, PagingLoadResult<PaymentDto>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<PaymentDto>>() {
			@Override
			public void load(PagingLoadConfig loadConfig, final AsyncCallback<PagingLoadResult<PaymentDto>> callback) {
				Map<String, Serializable> params = new HashMap<>();
				if (idField.getCurrentValue() != null) {
					params.put(PaymentDto.Columns.id.name(), idField.getCurrentValue());
				}
				if (agentPaymentIdField.getCurrentValue() != null) {
					params.put(PaymentDto.Columns.agentPaymentId.name(), agentPaymentIdField.getCurrentValue());
				}
				if (creationStartTimeField.getCurrentValue() != null) {
					params.put(PaymentDto.Columns.startTime.name(), creationStartTimeField.getCurrentValue());
				}
				if (creationEndTimeField.getCurrentValue() != null) {
					params.put(PaymentDto.Columns.endTime.name(), creationEndTimeField.getCurrentValue());
				}
				if (serviceComboBox.getCurrentValue() != null) {
					params.put(PaymentDto.Columns.serviceId.name(), serviceComboBox.getCurrentValue().getId());
				}
				if (channelComboBox.getCurrentValue() != null) {
					params.put(PaymentDto.Columns.channelId.name(), channelComboBox.getCurrentValue().getId());
				}

				ServicesFactory.getPaymentService().getPayments(loadConfig.getOffset(), loadConfig.getLimit(), params, paymentStatusComboBox.getValues(), new ServiceCallback<PagingLoadResult<PaymentDto>>() {
					@Override
					public void onServiceSuccess(PagingLoadResult<PaymentDto> result) {
						callback.onSuccess(result);
					}
				});

			}
		};
		pagingLoader = new PagingLoader<>(proxy);

		pagingLoader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, PaymentDto, PagingLoadResult<PaymentDto>>(PaymentTable.getListStore()));
		grid.setLoader(pagingLoader);
		List<Integer> pagingPossibleValues = new ArrayList<>();
		pagingPossibleValues.add(20);
		pagingPossibleValues.add(50);
		pagingPossibleValues.add(100);
		pagingPossibleValues.add(200);
		ZPagingToolBar pager = new ZPagingToolBar.Builder<>(pagingLoader)
			.possibleValue(pagingPossibleValues)
			.build();
		pager.getCombo();
		content.add(pager);
		pagingLoader.load();
	}

	private void initToolbar() {
		idField = new ZNumberField.Builder<>(new NumberPropertyEditor.LongPropertyEditor())
			.emptyText(Mes.get("id"))
			.build();

		agentPaymentIdField = new ZTextField.Builder()
			.emptyText(Mes.get("agentPaymentId"))
			.build();

		creationStartTimeField = new ZDateField.Builder()
			.pattern(PaymentDto.DATE_PATTERN)
			.value(new Date())
			.build();

		creationEndTimeField = new ZDateField.Builder()
			.pattern(PaymentDto.DATE_PATTERN)
			.value(new Date())
			.build();

		serviceComboBox = new ZSimpleComboBox.Builder<ServiceDto>()
			.keyProvider(new ModelKeyProvider<ServiceDto>() {
				@Override
				public String getKey(ServiceDto item) {
					return String.valueOf(item.getId());
				}
			})
			.labelProvider(new LabelProvider<ServiceDto>() {
				@Override
				public String getLabel(ServiceDto item) {
					return item.getName();
				}
			})
			.noSelectionLabel(Mes.get("chooseService"))
			.build();

		channelComboBox = new ZSimpleComboBox.Builder<ChannelDTO>()
			.keyProvider(new ModelKeyProvider<ChannelDTO>() {
				@Override
				public String getKey(ChannelDTO item) {
					return String.valueOf(item.getId());
				}
			})
			.labelProvider(new LabelProvider<ChannelDTO>() {
				@Override
				public String getLabel(ChannelDTO item) {
					return item.getName();
				}
			})
			.noSelectionLabel(Mes.get("chooseChannel"))
			.build();

		paymentStatusComboBox = new ZMultiSelectComboBox.Builder<PaymentStatusDto>()
			.keyProvider(new ModelKeyProvider<PaymentStatusDto>() {
				@Override
				public String getKey(PaymentStatusDto item) {
					return item == null ? null : item.name();
				}
			})
			.labelProvider(new LabelProvider<PaymentStatusDto>() {
				@Override
				public String getLabel(PaymentStatusDto item) {
					return item == null ? null : item.name();
				}
			})
			.width(200)
			.noSelectionLabel(Mes.get("paymentStatus"))
			.values(Arrays.asList(PaymentStatusDto.values()))
			.build();


	}

	private void assembleContent(){

		content.add(getToolbar());
		grid = new ZGrid<>(PaymentTable.setListStore(new ArrayList<PaymentDto>()), PaymentTable.getMyColumnModel());
		grid.setColumnResize(false);
		grid.getView().setForceFit(true);
		grid.getView().setColumnLines(true);
		content.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));

	}

	private VerticalLayoutContainer getToolbar() {
		ZToolBar upperToolbar = new ZToolBar();
		ZToolBar lowerToolbar = new ZToolBar();
		VerticalLayoutContainer container = new VerticalLayoutContainer();
		upperToolbar.setEnableOverflow(false);

		upperToolbar.add(idField);
		upperToolbar.add(agentPaymentIdField);

		upperToolbar.add(serviceComboBox);
		upperToolbar.add(channelComboBox);
		upperToolbar.add(paymentStatusComboBox);
		upperToolbar.getWidget(2).getParent().getElement().getStyle().setTop(0, Style.Unit.PX);

		lowerToolbar.add(new HTML(Mes.get("paymentCreationTime") + ": "));
		lowerToolbar.add(creationStartTimeField);
		lowerToolbar.add(new HTML(Mes.get("from")));
		lowerToolbar.add(creationEndTimeField);
		lowerToolbar.add(new HTML(Mes.get("to")));

		upperToolbar.add(getSearchButton());
		upperToolbar.add(getClearButton());

		container.add(upperToolbar);
		upperToolbar.addStyleName("toolbar");
		container.add(lowerToolbar);

		container.addStyleName("toolbar-container");
		container.getElement().getStyle().setFontSize(13, Style.Unit.PX);
		container.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
		container.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
//		container.getElement().getStyle().setHeight(2, Style.Unit.EM);
		container.getElement().getStyle().setTextAlign(Style.TextAlign.CENTER);

		return container;
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
					idField.setText(null);
					agentPaymentIdField.setText(null);
					creationStartTimeField.setText(null);
					creationEndTimeField.setText(null);
					serviceComboBox.setValue(null);
					channelComboBox.setValue(null);
					paymentStatusComboBox.setValue(null);
				}
			} )
			.build();
	}
}
