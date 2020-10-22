package com.azry.sps.console.client.tabs.usergroup;

import com.azry.sps.console.shared.dto.usergroup.PermissionsDTO;
import com.sencha.gxt.data.shared.TreeStore;

import java.io.Serializable;
import java.util.List;

public class PermissionTreeModel implements Serializable, TreeStore.TreeNode<PermissionTreeModel> {

	String permissionName;

	PermissionsDTO permissionsDTO;

	public PermissionTreeModel(PermissionsDTO dto) {
		permissionName = dto.name();
		permissionsDTO = dto;
	}

	public PermissionTreeModel(String permissionName) {
		this.permissionName = permissionName;
	}

	public String getPermissionName() {
		return permissionName;
	}

	public void setPermissionName(String permissionName) {
		this.permissionName = permissionName;
	}

	public PermissionsDTO getPermissionsDTO() {
		return permissionsDTO;
	}

	public void setPermissionsDTO(PermissionsDTO permissionsDTO) {
		this.permissionsDTO = permissionsDTO;
	}

	@Override
	public List<? extends TreeStore.TreeNode<PermissionTreeModel>> getChildren() {
		return null;
	}

	@Override
	public PermissionTreeModel getData() {
		return this;
	}
}
