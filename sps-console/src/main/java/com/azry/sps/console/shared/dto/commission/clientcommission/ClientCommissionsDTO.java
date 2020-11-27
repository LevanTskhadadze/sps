package com.azry.sps.console.shared.dto.commission.clientcommission;

import com.azry.sps.common.model.commission.ClientCommissions;
import com.azry.sps.common.model.commission.CommissionRateType;
import com.azry.sps.console.client.utils.StringUtil;
import com.azry.sps.console.shared.dto.ConfigurableDTO;
import com.azry.sps.console.shared.dto.commission.CommissionRateTypeDTO;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClientCommissionsDTO extends ConfigurableDTO implements IsSerializable {

	private long id;

	private long priority;

	private boolean allServices;

	private List<String> servicesIds = new ArrayList<>();

	private boolean allChannels;

	private List<String> channelsIds = new ArrayList<>();

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

	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
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

	public boolean isAllChannels() {
		return allChannels;
	}

	public void setAllChannels(boolean allChannels) {
		this.allChannels = allChannels;
	}

	public List<String> getChannelsIds() {
		return channelsIds;
	}

	public void setChannelsIds(List<String> channelsIds) {
		this.channelsIds = channelsIds;
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
	public static ClientCommissionsDTO toDTO(ClientCommissions clientCommissions) {
		if (clientCommissions != null) {
			ClientCommissionsDTO dto = new ClientCommissionsDTO();
			dto.setId(clientCommissions.getId());
			dto.setPriority(clientCommissions.getPriority());
			dto.setAllServices(clientCommissions.isAllServices());
			if (!clientCommissions.getServicesIds().isEmpty())  {
				dto.setServicesIds(Arrays.asList(clientCommissions.getServicesIds().split(",")));
			}
			dto.setAllChannels(clientCommissions.isAllChannels());
			if (!clientCommissions.getChannelsIds().isEmpty()) {
				dto.setChannelsIds(Arrays.asList(clientCommissions.getChannelsIds().split(",")));
			}
			dto.setRateType(CommissionRateTypeDTO.valueOf(clientCommissions.getRateType().name()));
			dto.setMinCommission(clientCommissions.getMinCommission());
			dto.setMaxCommission(clientCommissions.getMaxCommission());
			dto.setCommission(clientCommissions.getCommission());
			dto.setCreateTime(clientCommissions.getCreateTime());
			dto.setLastUpdateTime(clientCommissions.getLastUpdateTime());
			dto.setVersion(clientCommissions.getVersion());

			return dto;
		}
		return null;
	}

	@GwtIncompatible
	public static List<ClientCommissionsDTO> toDTOs(List<ClientCommissions> clientCommissions) {
		if (clientCommissions != null) {
			List<ClientCommissionsDTO> dtos = new ArrayList<>();

			for (ClientCommissions cc : clientCommissions) {
				dtos.add(toDTO(cc));

			}
			return dtos;
		}
		return null;
	}

	@GwtIncompatible
	public ClientCommissions fromDTO() {
		ClientCommissions cc = new ClientCommissions();
		cc.setId(this.getId());
		cc.setPriority(this.getPriority());
		cc.setAllServices(this.isAllServices());
		cc.setServicesIds(StringUtil.joinEndWithDel(this.getServicesIds(), ","));
		cc.setAllChannels(this.isAllChannels());
		cc.setChannelsIds(StringUtil.joinEndWithDel(this.getChannelsIds(), ","));
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
