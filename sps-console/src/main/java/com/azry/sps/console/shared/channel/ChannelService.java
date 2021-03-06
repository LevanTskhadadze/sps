package com.azry.sps.console.shared.channel;

import com.azry.sps.console.shared.clientexception.SPSConsoleException;
import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.List;

@RemoteServiceRelativePath("servlet/Channel")
public interface ChannelService extends RemoteService {

	List<ChannelDTO> getChannels();

	List<ChannelDTO> getFilteredChannels(String name, Boolean isActive);

	ChannelDTO updateChannel(ChannelDTO dto) throws SPSConsoleException;

	void deleteChannel(long id) throws SPSConsoleException;
}
