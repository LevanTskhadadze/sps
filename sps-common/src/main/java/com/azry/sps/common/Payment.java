package com.azry.sps.common;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Payment {

	private long id;

	private String agentPaymentId;

	private long serviceId;

	private long channelId;

	private String abonentCode;

	private BigDecimal amount;

	private BigDecimal clCommission;

	private BigDecimal svcCommission;

	private PaymentStatus status;

	private Date statusChangeTime;

	private String statusMessage;

	private Date createTime;

	private Client client;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(length = 50)
	public String getAgentPaymentId() {
		return agentPaymentId;
	}

	public void setAgentPaymentId(String agentPaymentId) {
		this.agentPaymentId = agentPaymentId;
	}

	public long getServiceId() {
		return serviceId;
	}

	public void setServiceId(long serviceId) {
		this.serviceId = serviceId;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	@Column(length = 100)
	public String getAbonentCode() {
		return abonentCode;
	}

	public void setAbonentCode(String abonentCode) {
		this.abonentCode = abonentCode;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getClCommission() {
		return clCommission;
	}

	public void setClCommission(BigDecimal clCommission) {
		this.clCommission = clCommission;
	}

	@Column(precision = 10, scale = 2)
	public BigDecimal getSvcCommission() {
		return svcCommission;
	}

	public void setSvcCommission(BigDecimal svcCommission) {
		this.svcCommission = svcCommission;
	}

	@Column(length = 50)
	@Enumerated(EnumType.STRING)
	public PaymentStatus getStatus() {
		return status;
	}

	public void setStatus(PaymentStatus status) {
		this.status = status;
	}

	public Date getStatusChangeTime() {
		return statusChangeTime;
	}

	public void setStatusChangeTime(Date statusChangeTime) {
		this.statusChangeTime = statusChangeTime;
	}

	@Column(length = 500)
	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Embedded
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}