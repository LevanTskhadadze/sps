package com.azry.sps.common.model.payment;

import com.azry.sps.common.model.client.Client;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class PaymentList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private Client client;

	@OneToMany(mappedBy = "paymentList")
	private List<PaymentListEntry> entries;

}
