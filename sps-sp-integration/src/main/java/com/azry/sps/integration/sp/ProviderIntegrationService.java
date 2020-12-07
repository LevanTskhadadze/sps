package com.azry.sps.integration.sp;


import com.azry.sps.integration.sp.dto.AbonentInfo;
import com.azry.sps.integration.sp.dto.PayResponse;
import com.azry.sps.integration.sp.exception.SpConnectivityException;
import com.azry.sps.integration.sp.exception.SpIntegrationException;

import javax.ejb.Local;
import java.math.BigDecimal;

@Local
public interface ProviderIntegrationService {

	AbonentInfo getInfo(String serviceCode, String abonentCode) throws SpConnectivityException;

	PayResponse pay(String serviceCode, String agentPaymentId, String abonentCode, BigDecimal amount) throws SpIntegrationException, SpConnectivityException;
}
