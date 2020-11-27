package com.azry.sps.integration.sp;


import com.azry.sps.common.exception.SPSException;
import com.azry.sps.integration.sp.dto.AbonentInfo;
import com.azry.sps.integration.sp.dto.SpResponseStatus;

import java.math.BigDecimal;

public interface ServiceProviderIntegrationService {

	AbonentInfo getInfo(String serviceCode, String id) throws SPSException;

	SpResponseStatus pay(String serviceCode, long agentPaymentId, String abonentCode, BigDecimal amount) throws SPSException;
}
