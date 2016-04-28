package com.gongdian.weian.model;

import com.ab.model.AbResult;

import java.util.List;

/**
 * 
 *
 */
public class ProjectListResult extends AbResult {

	private List<Project> items;

	public List<Project> getItems() {
		return items;
	}

	public void setItems(List<Project> items) {
		this.items = items;
	}

}
