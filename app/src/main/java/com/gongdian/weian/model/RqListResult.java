package com.gongdian.weian.model;

import com.ab.model.AbResult;

import java.util.List;

/**
 * 
 *
 */
public class RqListResult extends AbResult {

	private List<Rq> items;

	public List<Rq> getItems() {
		return items;
	}

	public void setItems(List<Rq> items) {
		this.items = items;
	}

}
