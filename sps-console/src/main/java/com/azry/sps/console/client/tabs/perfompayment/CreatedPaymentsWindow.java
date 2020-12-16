package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZButton;
import com.azry.gxt.client.zcomp.ZColumnConfig;
import com.azry.gxt.client.zcomp.ZGrid;
import com.azry.gxt.client.zcomp.ZGridView;
import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.gxt.client.zcomp.ZWindow;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.NumberFormatUtils;
import com.azry.sps.console.shared.dto.payment.PaymentDTO;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.loader.ListLoadConfig;
import com.sencha.gxt.data.shared.loader.ListLoadResult;
import com.sencha.gxt.data.shared.loader.ListLoadResultBean;
import com.sencha.gxt.data.shared.loader.ListLoader;
import com.sencha.gxt.data.shared.loader.LoadResultListStoreBinding;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.tips.QuickTip;

import java.util.ArrayList;
import java.util.List;

public class CreatedPaymentsWindow extends ZWindow {

	private static final String WINDOW_BOTTOM_BORDER_STYLE = "1px solid #3291D6";

	VerticalLayoutContainer container = new VerticalLayoutContainer();

	private ZGrid<PaymentDTO> grid;

	private final ListStore<PaymentDTO> gridStore = new ListStore<>(new ModelKeyProvider<PaymentDTO>() {
		@Override
		public String getKey(PaymentDTO dto) {
			return String.valueOf(dto.getAbonentCode());
		}
	});

	private ListLoader<ListLoadConfig, ListLoadResult<PaymentDTO>> loader;

	List<PaymentDTO> payments;

	ZButton closeB;

	public CreatedPaymentsWindow(List<PaymentDTO> payments) {
		super(Mes.get("paymentsInfo"), 800, 500, false);
		this.payments = payments;
		initGrid();
		container.add(grid, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
		addBottomHorizontalLine();
		initButton();
		add(container, new MarginData(0));
	}

	private void initButton() {
		closeB = new ZButton.Builder()
			.text(Mes.get("close"))
			.icon(FAIconsProvider.getIcons().ban_white())
			.handler(new SelectEvent.SelectHandler() {
				@Override
				public void onSelect(SelectEvent selectEvent) {
					hide();
				}
			})
			.build();

		addButton(closeB);
		add(container);
	}

	private void initGrid() {

		RpcProxy<ListLoadConfig, ListLoadResult<PaymentDTO>> proxy = new RpcProxy<ListLoadConfig, ListLoadResult<PaymentDTO>>() {
			@Override
			public void load(ListLoadConfig loadConfig, final AsyncCallback<ListLoadResult<PaymentDTO>> callback) {
				callback.onSuccess(new ListLoadResultBean<>(payments));
			}
		};

		loader = new ListLoader<>(proxy);
		loader.addLoadHandler(new LoadResultListStoreBinding<ListLoadConfig, PaymentDTO, ListLoadResult<PaymentDTO>>(gridStore));


		grid = new ZGrid<>(gridStore, getColumns(), new ZGridView<PaymentDTO>());
		grid.getView().setColumnLines(true);
		grid.getView().setAutoFill(true);
		grid.getView().setForceFit(true);
		grid.getView().setStripeRows(true);
		grid.getView().getHeader().setDisableSortIcon(true);

		grid.setLoader(loader);
		new QuickTip(grid);
		grid.addAttachHandler(new AttachEvent.Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent attachEvent) {
				if (attachEvent.isAttached()) {
					loader.load();
				}
			}
		});
	}

	private ColumnModel<PaymentDTO> getColumns() {
		List<ColumnConfig<PaymentDTO, ?>> columns = new ArrayList<>();

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO dto) {
					return dto.getServiceDTO().getName();
				}
			})
			.header(Mes.get("service"))
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.fixed()
			.width(140)
			.header(Mes.get("abonentCode"))
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO dto) {
					return dto.getAbonentCode();
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.fixed()
			.header(Mes.get("amount"))
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO dto) {
					return NumberFormatUtils.format(dto.getAmount());
				}
			})
			.build());


		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.fixed()
			.header(Mes.get("commission"))
			.valueProvider(new ZStringProvider<PaymentDTO>() {
				@Override
				public String getProperty(PaymentDTO dto) {
					return NumberFormatUtils.format(dto.getClCommission());
				}
			})
			.build());

		columns.add(new ZColumnConfig.Builder<PaymentDTO, String>()
			.width(250)
			.fixed()
			.header(Mes.get("paymentStatus"))
			.cell(new AbstractCell<String>() {
				@Override
				public void render(Context context, String s, SafeHtmlBuilder sb) {
					PaymentDTO dto = gridStore.get(context.getIndex());
					String str = dto.getStatus() == null ?
						"" : Mes.get("PAYMENT_" + dto.getStatus().name());
					sb.appendHtmlConstant("<div style=\"font-weight: bold; color: "+ dto.getStatus().getColor() + "\">" + str + "</div>");
				}
			})
			.build());

		return new ColumnModel<>(columns);
	}

	private void addBottomHorizontalLine() {
		getButtonBar().getElement().getStyle().setProperty("borderTop", WINDOW_BOTTOM_BORDER_STYLE);
	}

}
