package com.azry.sps.console.client.tabs.usergroup;

import java.util.ArrayList;
import java.util.List;

public class PermissionGroupTreeModel extends PermissionTreeModel{

	private List<PermissionTreeModel> children = new ArrayList<>();

	public PermissionGroupTreeModel(String name) {
		super(name);
	}

	@Override
	public List<PermissionTreeModel> getChildren() {
		return children;
	}

	public void setChildren(List<PermissionTreeModel> children) {
		this.children = children;
	}

	public void addChild(PermissionTreeModel child) {
		children.add(child);
	}

}
