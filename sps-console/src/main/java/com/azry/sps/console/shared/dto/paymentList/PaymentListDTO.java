package com.azry.sps.console.shared.dto.paymentList;

import com.azry.sps.common.model.paymentlist.PaymentList;
import com.azry.sps.console.shared.dto.bankclient.ClientDTO;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class PaymentListDTO implements IsSerializable {

	private long id;

	private ClientDTO client;

	private List<PaymentListEntryDTO> entries = new ArrayList<>();

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ClientDTO getClient() {
		return client;
	}

	public void setClient(ClientDTO client) {
		this.client = client;
	}

	public List<PaymentListEntryDTO> getEntries() {
		return entries;
	}

	public void setEntries(List<PaymentListEntryDTO> entries) {
		this.entries = entries;
	}

	@GwtIncompatible
	public static PaymentListDTO toDTO(PaymentList entity) {
		if (entity != null) {
			PaymentListDTO dto = new PaymentListDTO();
			dto.setId(entity.getId());
			dto.setClient(ClientDTO.toDTO(entity.getClient()));
			dto.setEntries(PaymentListEntryDTO.toDTOs(entity.getEntries()));

			return dto;
		}
		return null;
	}

	@GwtIncompatible
	public PaymentList fromDTO() {
		PaymentList entity = new PaymentList();
		entity.setId(this.getId());
		entity.setClient(this.getClient().entityFromDTO());
		entity.setEntries(PaymentListEntryDTO.fromDTOs(this.getEntries()));

		return entity;
	}
}
