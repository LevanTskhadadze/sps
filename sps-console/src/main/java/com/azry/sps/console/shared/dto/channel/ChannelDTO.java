package com.azry.sps.console.shared.dto.channel;

import com.azry.sps.common.model.channels.Channel;
import com.azry.sps.common.model.channels.FiServiceUnavailabilityAction;
import com.azry.sps.console.shared.dto.ConfigurableDTO;
import com.google.gwt.core.shared.GwtIncompatible;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class ChannelDTO extends ConfigurableDTO implements IsSerializable {

	private long id;

	private String name;

	private FiServiceUnavailabilityActionDTO fiServiceUnavailabilityAction;

	private boolean active;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FiServiceUnavailabilityActionDTO getFiServiceUnavailabilityAction() {
		return fiServiceUnavailabilityAction;
	}

	public void setFiServiceUnavailabilityAction(FiServiceUnavailabilityActionDTO fiServiceUnavailabilityAction) {
		this.fiServiceUnavailabilityAction = fiServiceUnavailabilityAction;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@GwtIncompatible
	public static ChannelDTO toDTO(Channel channel) {
		if (channel != null) {
			ChannelDTO dto = new ChannelDTO();
			dto.setId(channel.getId());
			dto.setName(channel.getName());
			dto.setFiServiceUnavailabilityAction(FiServiceUnavailabilityActionDTO.valueOf(channel.getFiServiceUnavailabilityAction().name()));
			dto.setActive(channel.isActive());
			dto.setCreateTime(channel.getCreateTime());
			dto.setLastUpdateTime(channel.getLastUpdateTime());
			dto.setVersion(channel.getVersion());

			return dto;
		}
		return null;
	}

	@GwtIncompatible
	public static List<ChannelDTO> toDTOs(List<Channel> channels) {
		if (channels != null) {
			List<ChannelDTO> dtos = new ArrayList<>();

			for (Channel channel : channels) {
				dtos.add(toDTO(channel));

			}
			return dtos;
		}
		return null;
	}


	@GwtIncompatible
	public Channel fromDTO() {
		Channel channel = new Channel();
		channel.setId(this.getId());
		channel.setName(this.getName());
		channel.setFiServiceUnavailabilityAction(FiServiceUnavailabilityAction.valueOf(this.getFiServiceUnavailabilityAction().name()));
		channel.setActive(this.isActive());
		channel.setCreateTime(this.getCreateTime());
		channel.setLastUpdateTime(this.getLastUpdateTime());
		channel.setVersion(this.getVersion());

		return channel;
	}
}


