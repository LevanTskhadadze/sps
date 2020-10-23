package com.azry.sps.console.shared.channel;

import com.azry.sps.console.shared.dto.channel.ChannelDTO;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface ChannelServiceAsync {

	void getFilteredChannels(String name, Boolean isActive, AsyncCallback<List<ChannelDTO>> async);

	void updateChannel(ChannelDTO dto, AsyncCallback<ChannelDTO> async);

	void deleteChannel(long id, AsyncCallback<Void> async);
}
