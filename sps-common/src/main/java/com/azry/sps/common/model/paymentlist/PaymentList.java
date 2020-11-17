package com.azry.sps.common.model.paymentlist;

import com.azry.sps.common.model.client.Client;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class PaymentList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private Client client;

	@OneToMany(mappedBy = "paymentList", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PaymentListEntry> entries = new ArrayList<>();

}
