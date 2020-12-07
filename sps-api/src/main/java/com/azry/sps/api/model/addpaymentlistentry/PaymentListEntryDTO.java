package com.azry.sps.api.model.addpaymentlistentry;

import com.azry.sps.common.model.paymentlist.PaymentListEntry;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "PaymentListEntry", propOrder = {"serviceId", "abonentCode"})
public class PaymentListEntryDTO {

	private long id;

	private long serviceId;

	private String abonentCode;


	public void setId(long id) {
		this.id = id;
	}

	@XmlElement(name = "serviceId")
	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	@XmlElement(name = "abonentCode")
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

}
