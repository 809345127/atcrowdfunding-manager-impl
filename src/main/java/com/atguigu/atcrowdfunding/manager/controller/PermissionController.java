package com.atguigu.atcrowdfunding.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.util.AjaxResult;

@Controller
@RequestMapping("/permission")
public class PermissionController {

	@Autowired
	private PermissionService permissionService ;
	
	@RequestMapping("/index")
	public String index() {
		return "permission/index";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd() {
		return "permission/add";
	}
	
	
	
	
	@RequestMapping("/toUpdate")
	public String toUpdate(Integer id,Map<String,Object> map) {
		Permission permission = permissionService.getPermissionById(id);
		map.put("permission", permission);
		return "permission/update";
	}
	
	@ResponseBody
	@RequestMapping("/doDelete")
	public Object doDelete(Integer id) {
		AjaxResult result = new AjaxResult();
		try {
			
			int i = permissionService.deletePermission(id);
			
			if(i==1) {
				result.setSuccess(true);
			}else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("删除许可失败!");
		}		
		return result;
	}
	
	
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Object doUpdate(Permission permission) {
		AjaxResult result = new AjaxResult();
		try {
			
			int i = permissionService.updatePermission(permission);
			
			if(i==1) {
				result.setSuccess(true);
			}else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("修改许可失败!");
		}		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/doSave")
	public Object save(Permission permission) {
		AjaxResult result = new AjaxResult();
		try {
			
			int i = permissionService.savePermission(permission);
			
			if(i==1) {
				result.setSuccess(true);
			}else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("保存许可失败!");
		}		
		return result;
	}
	
	//Demo 6 异步加载树的数据
	@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData() {		
			
		List<Permission> root = new ArrayList<Permission>();			
		
		Permission parent = null ;
		
		List<Permission> allPermissionList = permissionService.queryAllPermission();

		
		Map<Integer,Permission> allPermissionMap = new HashMap<Integer,Permission>();
		for (Permission permission : allPermissionList) {
			allPermissionMap.put(permission.getId(), permission);
		}
		
		for (Permission permission : allPermissionList) {
			Permission child = permission;
			if(permission.getPid()==0) { //根
				parent = permission ;
			}else {						
				Permission permissionObj = allPermissionMap.get(child.getPid());					
				permissionObj.getChildren().add(child);
			}
		}
		
		root.add(parent);
		
		return root;
	}
	
	
	//Demo 5 解决双层for循环性能问题.
	/*@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData() {
		
		AjaxResult result = new AjaxResult();
		
		try {
			
			List<Permission> root = new ArrayList<Permission>();			
			
			Permission parent = null ;
			
			List<Permission> allPermissionList = permissionService.queryAllPermission();

			
			Map<Integer,Permission> allPermissionMap = new HashMap<Integer,Permission>();
			for (Permission permission : allPermissionList) {
				allPermissionMap.put(permission.getId(), permission);
			}
			
			for (Permission permission : allPermissionList) {
				Permission child = permission;
				if(permission.getPid()==0) { //根
					parent = permission ;
				}else {						
					Permission permissionObj = allPermissionMap.get(child.getPid());					
					permissionObj.getChildren().add(child);
				}
			}
			
			root.add(parent);
			result.setData(root);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}*/
	
	// Demo4 - 改进查询语句的条数,进行一次加载,在内存中组合数据关系.
	/*@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData() {
		
		AjaxResult result = new AjaxResult();
		
		try {
			
			List<Permission> root = new ArrayList<Permission>();			
			
			Permission parent = null ;
			
			List<Permission> allPermissionList = permissionService.queryAllPermission();

			//a : 
			for (Permission permission : allPermissionList) {
				Permission child = permission;
				if(permission.getPid()==0) { //根
					parent = permission ;
				}else {					
					for (Permission permissionObj : allPermissionList) {
						if(child.getPid() == permissionObj.getId()) { //通过子来 查找父
							permissionObj.getChildren().add(child); //组合父与子的关系.
							//break a;
							break ;
						}						
					}					
				}
			}
			
			root.add(parent);
			result.setData(root);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}*/
	
	//Demo 3 递归
	/*@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData() {
		
		AjaxResult result = new AjaxResult();
		
		try {
			
			List<Permission> root = new ArrayList<Permission>();			
			
			Permission parent = permissionService.getPermissionRoot();	 //根节点		
			
			relationPermission(parent);

			root.add(parent);
			
			result.setData(root);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}
	
	private void relationPermission(Permission permission) {
		List<Permission> children = permissionService.getPermissionByPid(permission.getId()); 
		permission.setChildren(children);
		
		if(children.size()>0) {
			for (Permission permissionObj : children) {
				relationPermission(permissionObj);
			}
		}
	}*/
	
	//Demo 2 循环
	/*@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData() {
		
		AjaxResult result = new AjaxResult();
		
		try {
			
			List<Permission> root = new ArrayList<Permission>();			
			
			Permission parent = permissionService.getPermissionRoot();	 //根节点		
			
			List<Permission> children = permissionService.getPermissionByPid(parent.getId()); //子节点: 分支节点
			
			parent.setChildren(children);
			
			for (Permission permission : children) {
				List<Permission> childrenList = permissionService.getPermissionByPid(permission.getId()); // 子节点: 叶子节点 
				
				permission.setChildren(childrenList);				
			}
			
			root.add(parent);
			
			result.setData(root);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}*/
	
	//Demo1
	/*@ResponseBody
	@RequestMapping("/loadData")
	public Object loadData() {
		
		AjaxResult result = new AjaxResult();
		
		try {
			
			List<Permission> root = new ArrayList<Permission>();
			
			Permission parent = new Permission(); //真正的根节点.
			parent.setName("系统权限菜单");
			
			List<Permission> children = new ArrayList<Permission>();
			
			Permission child1 = new Permission();
			child1.setName("控制面板");

			Permission child2 = new Permission();
			child2.setName("权限管理");
			
			children.add(child1);
			children.add(child2);
			
			
			parent.setChildren(children);
			
			root.add(parent);

			result.setData(root);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
		}
		
		return result;
	}*/
	
	
}
