package com.azry.sps.console.shared.dto.commission.servicecommission;

import com.azry.sps.common.model.commission.CommissionRateType;
import com.azry.sps.common.model.commission.ServiceCommissions;
import com.azry.sps.console.client.utils.StringUtil;
import com.azry.sps.console.shared.dto.ConfigurableDTO;
import com.azry.sps.console.shared.dto.commission.CommissionRateTypeDTO;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServiceCommissionsDto extends ConfigurableDTO implements IsSerializable {

	private long id;

	private boolean allServices;

	private List<String> servicesIds = new ArrayList<>();

	private CommissionRateTypeDTO rateType;

	private BigDecimal minCommission;

	private BigDecimal maxCommission;

	private BigDecimal Commission;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isAllServices() {
		return allServices;
	}

	public void setAllServices(boolean allServices) {
		this.allServices = allServices;
	}

	public List<String> getServicesIds() {
		return servicesIds;
	}

	public void setServicesIds(List<String> servicesIds) {
		this.servicesIds = servicesIds;
	}

	public CommissionRateTypeDTO getRateType() {
		return rateType;
	}

	public void setRateType(CommissionRateTypeDTO rateType) {
		this.rateType = rateType;
	}

	public BigDecimal getMinCommission() {
		return minCommission;
	}

	public void setMinCommission(BigDecimal minCommission) {
		this.minCommission = minCommission;
	}

	public BigDecimal getMaxCommission() {
		return maxCommission;
	}

	public void setMaxCommission(BigDecimal maxCommission) {
		this.maxCommission = maxCommission;
	}

	public BigDecimal getCommission() {
		return Commission;
	}

	public void setCommission(BigDecimal commission) {
		Commission = commission;
	}

	@GwtIncompatible
	public static ServiceCommissionsDto toDTO(ServiceCommissions serviceCommissions) {
		if (serviceCommissions != null) {
			ServiceCommissionsDto dto = new ServiceCommissionsDto();
			dto.setId(serviceCommissions.getId());
			dto.setAllServices(serviceCommissions.isAllServices());
			if (!serviceCommissions.getServicesIds().isEmpty())  {
				dto.setServicesIds(Arrays.asList(serviceCommissions.getServicesIds().split(",")));
			}
			dto.setRateType(CommissionRateTypeDTO.valueOf(serviceCommissions.getRateType().name()));
			dto.setMinCommission(serviceCommissions.getMinCommission());
			dto.setMaxCommission(serviceCommissions.getMaxCommission());
			dto.setCommission(serviceCommissions.getCommission());
			dto.setCreateTime(serviceCommissions.getCreateTime());
			dto.setLastUpdateTime(serviceCommissions.getLastUpdateTime());
			dto.setVersion(serviceCommissions.getVersion());

			return dto;
		}
		return null;
	}

	@GwtIncompatible
	public static List<ServiceCommissionsDto> toDTOs(List<ServiceCommissions> serviceCommissions) {
		if (serviceCommissions != null) {
			List<ServiceCommissionsDto> dtos = new ArrayList<>();
			for (ServiceCommissions entity : serviceCommissions) {
				dtos.add(toDTO(entity));
			}
			return dtos;
		}
		return null;
	}

	@GwtIncompatible
	public ServiceCommissions fromDTO() {
		ServiceCommissions cc = new ServiceCommissions();
		cc.setId(this.getId());
		cc.setAllServices(this.isAllServices());
		cc.setServicesIds(StringUtil.join(this.getServicesIds(), ","));
		cc.setRateType(CommissionRateType.valueOf(this.getRateType().name()));
		cc.setMinCommission(this.getMinCommission());
		cc.setMaxCommission(this.getMaxCommission());
		cc.setCommission(this.getCommission());
		cc.setCreateTime(this.getCreateTime());
		cc.setLastUpdateTime(this.getLastUpdateTime());
		cc.setVersion(this.getVersion());

		return cc;
	}

}
