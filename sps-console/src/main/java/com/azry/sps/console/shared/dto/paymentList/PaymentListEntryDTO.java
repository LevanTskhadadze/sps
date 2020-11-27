package com.azry.sps.console.shared.dto.paymentList;

import com.azry.sps.common.model.paymentlist.PaymentListEntry;
import com.azry.sps.console.shared.dto.services.ServiceDTO;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class PaymentListEntryDTO implements IsSerializable {

	private long id;

	private PaymentListDTO paymentList;

	private long serviceId;

	private ServiceDTO service;

	private String abonentCode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public PaymentListDTO getPaymentList() {
		return paymentList;
	}

	public void setPaymentList(PaymentListDTO paymentList) {
		this.paymentList = paymentList;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}

	public ServiceDTO getService() {
		return service;
	}

	public void setService(ServiceDTO service) {
		this.service = service;
	}

	@GwtIncompatible
	public static PaymentListEntryDTO toDTO(PaymentListEntry entity) {
		if (entity != null) {
			PaymentListEntryDTO dto = new PaymentListEntryDTO();
			dto.setId(entity.getId());
			dto.setAbonentCode(entity.getAbonentCode());
			dto.setServiceId(entity.getServiceId());

			return dto;
		}
		return null;
	}

	@GwtIncompatible
	public static List<PaymentListEntryDTO> toDTOs(List<PaymentListEntry> entities) {
		if (entities != null) {
			List<PaymentListEntryDTO> dtos = new ArrayList<>();

			for (PaymentListEntry channel : entities) {
				dtos.add(toDTO(channel));

			}
			return dtos;
		}
		return null;
	}

	@GwtIncompatible
	public PaymentListEntry fromDTO() {
		PaymentListEntry entity = new PaymentListEntry();
		entity.setId(this.getId());
		entity.setServiceId(this.getServiceId());
		entity.setAbonentCode(this.getAbonentCode());

		return entity;
	}

	@GwtIncompatible
	public static List<PaymentListEntry> fromDTOs(List<PaymentListEntryDTO> dtos) {
		List<PaymentListEntry> entities = new ArrayList<>();
		for (PaymentListEntryDTO entry: dtos) {
			entities.add(entry.fromDTO());
		}
		return entities;
	}

}
