package com.azry.sps.integration.sp;

import com.azry.sps.common.exception.SPSException;
import com.azry.sps.integration.sp.dto.AbonentInfo;
import com.azry.sps.integration.sp.dto.PayResponse;
import com.azry.sps.integration.sp.exception.SpIntegrationException;

import javax.ejb.Local;
import java.math.BigDecimal;

@Local
public interface ServiceProviderIntegrationService {

	AbonentInfo getInfo(String serviceCode, String abonentCode) throws SpIntegrationException;

	PayResponse pay(String serviceCode, long agentPaymentId, String abonentCode, BigDecimal amount) throws SpIntegrationException;
}
