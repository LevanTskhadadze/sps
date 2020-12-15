package com.azry.sps.server.services.servicecommission;

import com.azry.sps.common.model.commission.CommissionRateType;
import com.azry.sps.common.model.commission.ServiceCommissions;
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

public class ServiceCommissionManagerBeanTest {

	@Mock
	private CachedConfigurationService cachedConfigurationService;

	@InjectMocks
	private ServiceCommissionsManager serviceCommissionsManager;

	ServiceCommissions serviceCommissions;
	BigDecimal amount;
	BigDecimal commission;

	@Before
	public void create() {
		serviceCommissionsManager = new ServiceCommissionsManagerBean();
		serviceCommissions = new ServiceCommissions();
		amount = new BigDecimal(50);
		commission = new BigDecimal(0);
		MockitoAnnotations.initMocks(this);
		when(cachedConfigurationService.getCommissionForService(contains("1"))).thenReturn(serviceCommissions);
	}


	@Test
	public void calculateCommissionShouldCalculatePercentWhenRateTypeIsPercent() {
		serviceCommissions.setRateType(CommissionRateType.PERCENT);
		serviceCommissions.setCommission(new BigDecimal(10));

		commission = serviceCommissionsManager.calculateCommission(1, amount);

		assertThat(commission, equalTo(new BigDecimal(5).setScale(2)));
	}

	@Test
	public void calculateCommissionShouldCalculateFixedCommissionWhenRateTypeIsPercent() {
		serviceCommissions.setRateType(CommissionRateType.FIXED);
		serviceCommissions.setCommission(new BigDecimal(10));

		commission = serviceCommissionsManager.calculateCommission(1, amount);

		assertThat(commission, equalTo(new BigDecimal(10)));
	}

	@Test
	public void calculateCommissionShouldSetCommissionToMaxValueIfCommissionExceedsIt() {
		serviceCommissions.setRateType(CommissionRateType.PERCENT);
		serviceCommissions.setCommission(new BigDecimal(100));
		serviceCommissions.setMaxCommission(new BigDecimal(5));

		commission = serviceCommissionsManager.calculateCommission(1, amount);

		assertThat(commission, equalTo(new BigDecimal(5)));
	}

	@Test
	public void calculateCommissionShouldSetCommissionToMinValueIfCommissionIsLess() {
		serviceCommissions.setRateType(CommissionRateType.PERCENT);
		serviceCommissions.setCommission(new BigDecimal(1));
		serviceCommissions.setMinCommission(new BigDecimal(10));

		commission = serviceCommissionsManager.calculateCommission(1, amount);

		assertThat(commission, equalTo(new BigDecimal(10)));
	}
}