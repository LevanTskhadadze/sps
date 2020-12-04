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
import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.azry.sps.console.shared.dto.payment.PaymentStatusDTO;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.azry.sps.console.shared.payment.PaymentParamDTO;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.util.DateWrapper;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class PaymentTab extends Composite {

	private final static int TOOLBAR_FIELD_WIDTH = 150;

	private final VerticalLayoutContainer content;

	private ZNumberField<Long> idField;

	private ZTextField agentPaymentIdField;

	private ZDateField creationStartTimeField;

	private ZDateField creationEndTimeField;

	private ZSimpleComboBox<ServiceDTO> serviceComboBox;

	private ZSimpleComboBox<ChannelDTO> channelComboBox;

	private ZMultiSelectComboBox<PaymentStatusDTO> paymentStatusComboBox;

	private PagingLoader<PagingLoadConfig, PagingLoadResult<PaymentDTO>> pagingLoader;

	ZGrid<PaymentDTO> grid;

	public PaymentTab(List<ServiceDTO> services, List<ChannelDTO> channels){
		super();
		content = new VerticalLayoutContainer();

		initToolbar(services, channels);

		initWidget(content);

		assembleContent(services, channels);

		initPaging();

	}

	private void initPaging() {
		RpcProxy<PagingLoadConfig, PagingLoadResult<PaymentDTO>> proxy = new RpcProxy<PagingLoadConfig, PagingLoadResult<PaymentDTO>>() {
			@Override
			public void load(PagingLoadConfig loadConfig, final AsyncCallback<PagingLoadResult<PaymentDTO>> callback) {

				PaymentParamDTO params = new PaymentParamDTO();

				params.setId( idField.getCurrentValue());
				params.setAgentPaymentId(agentPaymentIdField.getCurrentValue());
				params.setCreationEndTime(creationStartTimeField.getCurrentValue());
				params.setCreationEndTime(creationEndTimeField.getCurrentValue());
				if (serviceComboBox.getCurrentValue() != null) {
					params.setServiceId(serviceComboBox.getCurrentValue().getId());
				}
				if (channelComboBox.getCurrentValue() != null) {
					params.setChannelId(channelComboBox.getCurrentValue().getId());
				}


				ServicesFactory.getPaymentService().getPayments(loadConfig.getOffset(), loadConfig.getLimit(), params, paymentStatusComboBox.getValues(),
					new ServiceCallback<PagingLoadResult<PaymentDTO>>(PaymentTab.this) {
					@Override
					public void onServiceSuccess(PagingLoadResult<PaymentDTO> result) {
						callback.onSuccess(result);
					}
				});

			}
		};
		pagingLoader = new PagingLoader<>(proxy);

		pagingLoader.addLoadHandler(new LoadResultListStoreBinding<PagingLoadConfig, PaymentDTO, PagingLoadResult<PaymentDTO>>(PaymentTable.getListStore()));
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

	private void initToolbar(List<ServiceDTO> services, List<ChannelDTO> channels) {
		idField = new ZNumberField.Builder<>(new NumberPropertyEditor.LongPropertyEditor())
			.emptyText(Mes.get("id"))
			.build();

		agentPaymentIdField = new ZTextField.Builder()
			.emptyText(Mes.get("agentPaymentId"))
			.width(TOOLBAR_FIELD_WIDTH)
			.build();

		creationStartTimeField = new ZDateField.Builder()
			.pattern(PaymentDTO.DATE_PATTERN)
			.width(TOOLBAR_FIELD_WIDTH)
			.emptyText(Mes.get("leastTime"))
			.build();
		DateWrapper dateWrapper = new DateWrapper(new Date());
		dateWrapper = dateWrapper.addHours(-dateWrapper.getHours());
		dateWrapper = dateWrapper.addMinutes(-dateWrapper.getMinutes());
		creationStartTimeField.setValue(dateWrapper.asDate());

		creationEndTimeField = new ZDateField.Builder()
			.pattern(PaymentDTO.DATE_PATTERN)
			.width(TOOLBAR_FIELD_WIDTH)
			.emptyText(Mes.get("atMostTime"))
			.build();

		serviceComboBox = new ZSimpleComboBox.Builder<ServiceDTO>()
			.keyProvider(new ModelKeyProvider<ServiceDTO>() {
				@Override
				public String getKey(ServiceDTO item) {
					return String.valueOf(item.getId());
				}
			})
			.labelProvider(new LabelProvider<ServiceDTO>() {
				@Override
				public String getLabel(ServiceDTO item) {
					return item.getName();
				}
			})
			.noSelectionLabel(Mes.get("chooseService"))
			.editable(false)
			.values(services)
			.width(TOOLBAR_FIELD_WIDTH)
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
			.editable(false)
			.values(channels)
			.width(TOOLBAR_FIELD_WIDTH)
			.build();

		paymentStatusComboBox = new ZMultiSelectComboBox.Builder<PaymentStatusDTO>()
			.keyProvider(new ModelKeyProvider<PaymentStatusDTO>() {
				@Override
				public String getKey(PaymentStatusDTO item) {
					return item == null ? null : item.name();
				}
			})
			.width(2 * TOOLBAR_FIELD_WIDTH + 6)
			.noSelectionLabel(Mes.get("paymentStatus"))
			.values(Arrays.asList(PaymentStatusDTO.values()))
			.labelProvider(new LabelProvider<PaymentStatusDTO>() {
				@Override
				public String getLabel(PaymentStatusDTO item) {
					return "<div style=\"color: " + item.getColor() + "\">" + Mes.get("PAYMENT_" + item.name()) + "</div>";
				}
			})
			.build();


	}

	private void assembleContent(List<ServiceDTO> services, List<ChannelDTO> channels){

		content.add(getToolbar());
		grid = new ZGrid<>(PaymentTable.getListStore(), PaymentTable.getMyColumnModel(services, channels));
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
		upperToolbar.add(paymentStatusComboBox);

		lowerToolbar.add(serviceComboBox);
		lowerToolbar.add(channelComboBox);
		lowerToolbar.add(creationStartTimeField);
		lowerToolbar.add(creationEndTimeField);

		upperToolbar.add(getSearchButton());
		upperToolbar.add(getClearButton());

		container.add(upperToolbar);
		upperToolbar.addStyleName("toolbar");
		container.add(lowerToolbar);

		container.addStyleName("toolbar-container");
		container.getElement().getStyle().setFontSize(13, Style.Unit.PX);
		container.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
		container.getElement().getStyle().setMarginBottom(5, Style.Unit.PX);
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
					paymentStatusComboBox.setValues(new ArrayList<PaymentStatusDTO>());
				}
			} )
			.build();
	}
}
