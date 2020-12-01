package com.azry.sps.server.services.channel;

import com.azry.sps.common.exception.SPSException;
import com.azry.sps.common.model.channels.Channel;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ChannelManager {

	List<Channel> getChannels();

	Channel getChannel(long id);

	List<Channel> getFilteredChannels(String name, Boolean isActive);

	Channel updateChannel(Channel channel) throws SPSException;

	void deleteChannel(long id) throws SPSException;
}
