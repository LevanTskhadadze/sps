package com.azry.sps.common;

import java.io.Serializable;
import java.util.List;

public class ListResult<T> implements Serializable {

	private int resultCount;
	
	private List<T> resultList;
	
	public ListResult() {
	}

	public ListResult(List<T> resultList, int count) {
		this.resultCount = count;
		this.resultList = resultList;
	}
	
	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
}