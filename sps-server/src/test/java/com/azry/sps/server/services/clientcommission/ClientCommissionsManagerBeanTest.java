package com.azry.sps.server.services.clientcommission;

import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.common.model.commission.CommissionRateType;
import com.azry.sps.server.caching.CachedConfigurationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.when;

public class ClientCommissionsManagerBeanTest {

	@Mock
	private CachedConfigurationService cachedConfigurationService;

	@InjectMocks
	private ClientCommissionsManager clientCommissionsManager;

	ClientCommissions clientCommissions;
	BigDecimal amount;
	BigDecimal commission;

	@Before
	public void create() {
		clientCommissionsManager = new ClientCommissionsManagerBean();
		clientCommissions = new ClientCommissions();
		amount = new BigDecimal(50);
		commission = new BigDecimal(0);
		MockitoAnnotations.initMocks(this);
		when(cachedConfigurationService.getClientCommissionByServiceId(contains("1"))).thenReturn(clientCommissions);
	}


	@Test
	public void calculateCommissionShouldCalculatePercentWhenRateTypeIsPercent() {
		clientCommissions.setRateType(CommissionRateType.PERCENT);
		clientCommissions.setCommission(new BigDecimal(10));

		commission = clientCommissionsManager.calculateCommission(1, null, amount);

		assertThat(commission, equalTo(new BigDecimal(5).setScale(2)));
	}

	@Test
	public void calculateCommissionShouldCalculateFixedCommissionWhenRateTypeIsPercent() {
		clientCommissions.setRateType(CommissionRateType.FIXED);
		clientCommissions.setCommission(new BigDecimal(10));

		commission = clientCommissionsManager.calculateCommission(1, null, amount);

		assertThat(commission, equalTo(new BigDecimal(10)));
	}

	@Test
	public void calculateCommissionShouldSetCommissionToMaxValueIfCommissionExceedsIt() {
		clientCommissions.setRateType(CommissionRateType.PERCENT);
		clientCommissions.setCommission(new BigDecimal(100));
		clientCommissions.setMaxCommission(new BigDecimal(5));

		commission = clientCommissionsManager.calculateCommission(1, null, amount);

		assertThat(commission, equalTo(new BigDecimal(5)));
	}

	@Test
	public void calculateCommissionShouldSetCommissionToMinValueIfCommissionIsLess() {
		clientCommissions.setRateType(CommissionRateType.PERCENT);
		clientCommissions.setCommission(new BigDecimal(1));
		clientCommissions.setMinCommission(new BigDecimal(10));

		commission = clientCommissionsManager.calculateCommission(1, null, amount);

		assertThat(commission, equalTo(new BigDecimal(10)));
	}
}