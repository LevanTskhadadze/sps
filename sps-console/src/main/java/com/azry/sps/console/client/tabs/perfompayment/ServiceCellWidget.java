package com.azry.sps.console.client.tabs.perfompayment;

import com.azry.faicons.client.faicons.FAIconsProvider;
import com.azry.gxt.client.zcomp.ZURLBuilder;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.shared.dto.services.ServiceDto;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;

public class ServiceCellWidget extends Composite {


	String imageUrl;

	public ServiceCellWidget(ServiceDto serviceDto) {

		imageUrl = ZURLBuilder.create(GWT.getHostPageBaseURL(), "sps/servlet/iconDownload")
			.param("id", serviceDto.getId())
			.param("t", 1)
			.build();

		Image serviceIcon = new Image();
		serviceIcon.setUrl(imageUrl);
		serviceIcon.setPixelSize(32,32);

		HBoxLayoutContainer h = new HBoxLayoutContainer();
		h.add(serviceIcon);
		h.add(new Label(serviceDto.getName()), new BoxLayoutContainer.BoxLayoutData(new Margins(5)));

		if (!serviceDto.isActive()) {
			Image infoIcon = new Image();
			String infoIconUrl = FAIconsProvider.getIcons().info_circle().getSafeUri().asString();
			infoIcon.setPixelSize(20,20);
			infoIcon.setTitle(Mes.get("serviceNotActive"));
			infoIcon.setUrl(infoIconUrl);
			h.add(infoIcon);
		}

		h.setHBoxLayoutAlign(HBoxLayoutContainer.HBoxLayoutAlign.MIDDLE);
		initWidget(h);

	}


}
