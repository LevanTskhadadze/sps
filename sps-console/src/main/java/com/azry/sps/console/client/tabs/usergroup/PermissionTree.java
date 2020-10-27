package com.azry.sps.console.client.tabs.usergroup;

import com.azry.gxt.client.zcomp.ZStringProvider;
import com.azry.sps.console.client.utils.Mes;
import com.azry.sps.console.client.utils.StringUtil;
import com.azry.sps.console.shared.dto.usergroup.PermissionsDTO;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeSelectionModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PermissionTree {

	public static Tree<PermissionTreeModel, String> createTree () {

		TreeStore<PermissionTreeModel> store = new TreeStore<>(GetModelKeyProvider());

		List<PermissionGroupTreeModel> groupTreeList = getGroupTreeRootModels(getPermissionMap());


		Tree<PermissionTreeModel, String> tree = new Tree<>(store, getValueProvider());
		TreeSelectionModel<PermissionTreeModel> selectionModel = new TreeSelectionModel<>();
		selectionModel.setLocked(true);
		tree.setSelectionModel(selectionModel);
		tree.setHeight(320);
		tree.setCheckable(true);
		tree.setCheckStyle(Tree.CheckCascade.TRI);
		tree.setAutoLoad(true);
		for (PermissionGroupTreeModel treeModel : groupTreeList) {
			for (PermissionTreeModel model: treeModel.getChildren()) {
				tree.setData(model.getPermissionName(), model);
			}
			store.add(treeModel);
			store.addSubTree(treeModel, 0, treeModel.getChildren());
		}
		return tree;
	}


	private static ModelKeyProvider<PermissionTreeModel> GetModelKeyProvider() {
		return new ModelKeyProvider<PermissionTreeModel>() {
			@Override
			public String getKey(PermissionTreeModel model) {
				return model.getPermissionName();
			}
		};
	}

	private static Map<String, List<PermissionsDTO>> getPermissionMap() {
		Map<String, List<PermissionsDTO>> permissionMap = new HashMap<>();

		for (PermissionsDTO permission : PermissionsDTO.values()) {
			if (permissionMap.containsKey(permission.getPermissionType())) {
				permissionMap.get(permission.getPermissionType()).add(permission);
			}

			else {
				List<PermissionsDTO> permissions = new ArrayList<>();
				permissions.add(permission);
				permissionMap.put(permission.getPermissionType(), permissions);
			}
		}

		return permissionMap;
	}

	private static ZStringProvider<PermissionTreeModel> getValueProvider() {
		return new ZStringProvider<PermissionTreeModel>() {
			@Override
			public String getProperty(PermissionTreeModel model) {
				return Mes.get(model.getPermissionName());
			}
		};
	}

	private static List<PermissionGroupTreeModel> getGroupTreeRootModels(Map<String, List<PermissionsDTO>> map) {
		List<PermissionGroupTreeModel> treeModels = new ArrayList<>();

		PermissionGroupTreeModel groupTree;

		for (Map.Entry<String, List<PermissionsDTO>> entry : map.entrySet()) {

			groupTree = new PermissionGroupTreeModel(entry.getKey());

			for (PermissionsDTO perm : entry.getValue()) {
				groupTree.addChild(new PermissionTreeModel(perm));
			}
			treeModels.add(groupTree);
		}
		return treeModels;
	}

	public static String getCheckedPermissions(Tree<PermissionTreeModel, String> tree) {
			List<String> permissions = new ArrayList<>();

			for (PermissionTreeModel model : tree.getCheckedSelection()) {
				if (model.getPermissionsDTO() != null) {
					permissions.add(model.getPermissionsDTO().name());
				}
			}
			return StringUtil.join(permissions, ",");
	}

	public static void setCheckedPermissions(Tree<PermissionTreeModel, String> tree, String permissions) {
			for (String permission : permissions.split(",")) {
				PermissionTreeModel model = tree.getData(permission);
				Tree.TreeNode<PermissionTreeModel> node = tree.findNode(model);
				if (node != null) {
					tree.setChecked(model, Tree.CheckState.CHECKED);
				}
			}
		}
	}