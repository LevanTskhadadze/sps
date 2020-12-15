package com.azry.sps.api.model.addpaymentlistentry;

import com.azry.sps.common.model.paymentlist.PaymentListEntry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PaymentListEntry", propOrder = {"serviceId", "abonentCode"})
public class PaymentListEntryDTO {

	private Long id;

	private Long serviceId;

	private String abonentCode;

	@XmlTransient
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@XmlElement(name = "serviceId", required = true)
	public Long getServiceId() {
		return serviceId;
	}

	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}

	@XmlElement(name = "abonentCode", required = true)
	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}

	public static PaymentListEntryDTO listEntryToDTO(PaymentListEntry entry) {
		PaymentListEntryDTO info = new PaymentListEntryDTO();
		info.setAbonentCode(entry.getAbonentCode());
		info.setId(entry.getId());
		info.setServiceId(entry.getServiceId());

		return info;
	}

	public static PaymentListEntry dtoToListEntry(PaymentListEntryDTO info) {
		PaymentListEntry entry = new PaymentListEntry();
		entry.setAbonentCode(info.getAbonentCode());
		entry.setServiceId(info.getServiceId());

		return entry;
	}

	@Override
	public String toString() {
		return "ID: " + id +
			"\nService ID: " + serviceId +
			"\nAbonent code: " + abonentCode + "\n";
	}
}
