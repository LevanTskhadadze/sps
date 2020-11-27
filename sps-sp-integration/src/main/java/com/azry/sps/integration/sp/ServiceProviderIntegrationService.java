package com.azry.sps.integration.sp;

import com.azry.sps.common.exception.SPSException;
import com.azry.sps.integration.sp.dto.AbonentInfo;
import com.azry.sps.integration.sp.dto.PayResponse;

import javax.ejb.Local;
import java.math.BigDecimal;

@Local
public interface ServiceProviderIntegrationService {

	AbonentInfo getInfo(String serviceCode, String id) throws SPSException;

	PayResponse pay(String serviceCode, long agentPaymentId, String abonentCode, BigDecimal amount) throws SPSException;
}
