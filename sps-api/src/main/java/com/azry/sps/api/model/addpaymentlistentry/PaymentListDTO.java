package com.azry.sps.api.model.addpaymentlistentry;

import com.azry.sps.common.model.paymentlist.PaymentListEntry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PaymentList", propOrder = {"id", "serviceId", "abonentCode"})
public class PaymentListDTO {

	private long id;

	private long serviceId;

	private String abonentCode;

	@XmlElement(name = "id", required = true)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@XmlElement(name = "serviceId", required = true)
	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	@XmlElement(name = "abonentCode", required = true)
	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}

	public static PaymentListDTO listEntryToDTO(PaymentListEntry entry) {
		PaymentListDTO info = new PaymentListDTO();
		info.setAbonentCode(entry.getAbonentCode());
		info.setId(entry.getId());
		info.setServiceId(entry.getServiceId());

		return info;
	}

	public static PaymentListEntry dtoToListEntry(PaymentListDTO info) {
		PaymentListEntry entry = new PaymentListEntry();
		entry.setAbonentCode(info.getAbonentCode());
		entry.setId(info.getId());
		entry.setServiceId(info.getServiceId());

		return entry;
	}

}
