package com.azry.sps.console.server;

import com.azry.sps.common.exception.SPSException;
import com.azry.sps.console.shared.channel.ChannelService;
import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.azry.sps.server.services.channel.ChannelManager;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import javax.inject.Inject;
import javax.servlet.annotation.WebServlet;
import java.util.List;

@WebServlet("sps/servlet/Channel")
public class ChannelServiceImpl extends RemoteServiceServlet implements ChannelService {

	@Inject
	ChannelManager channelManager;

	@Override
	public List<ChannelDTO> getChannels() {
		return ChannelDTO.toDTOs(channelManager.getChannels());
	}

	@Override
	public List<ChannelDTO> getFilteredChannels(String name, Boolean isActive) {
		return ChannelDTO.toDTOs(channelManager.getFilteredChannels(name, isActive));
	}

	@Override
	public ChannelDTO updateChannel(ChannelDTO dto) throws SPSConsoleException {
		try {
			return ChannelDTO.toDTO(channelManager.updateChannel(dto.fromDTO()));
		}
		catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}

	@Override
	public void deleteChannel(long id) throws SPSConsoleException {
		try {
			channelManager.deleteChannel(id);
		} catch (SPSException ex) {
			throw new SPSConsoleException(ex);
		}
	}
}
