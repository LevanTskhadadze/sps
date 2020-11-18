package com.azry.sps.server.caching;

import java.util.List;

public interface CachingService<Type, Key> {

	Type get(Key key);

	List<Type> getList();

	void syncData();
}