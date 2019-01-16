package com.atguigu.atcrowdfunding.manager.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.controller.BaseController;
import com.atguigu.atcrowdfunding.manager.service.RoleService;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.util.AjaxResult;
import com.atguigu.atcrowdfunding.util.Page;
import com.atguigu.atcrowdfunding.util.StringUtil;
import com.atguigu.atcrowdfunding.vo.Data;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController{

	@Autowired
	private UserService userService ;
	
	@Autowired
	private RoleService roleService ;
	
	@RequestMapping("/index")
	public String index() {
		return "user/index";
	}
	
	@RequestMapping("/toAdd")
	public String toAdd() {
		return "user/add";
	}
	
	
	@ResponseBody
	@RequestMapping("/doAssignRole")
	public Object doAssignRole(Integer userid,Data ds) {
		AjaxResult result = new AjaxResult();
		try {
			
			roleService.doAssignRoleByUserid(userid, ds.getIds());
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("分配角色失败!");
		}		
		return result;
	}
	
/*	@ResponseBody
	@RequestMapping("/doUnAssignRole")
	public Object doUnAssignRole(Integer userid , Data ds) {
		AjaxResult result = new AjaxResult();
		try {
			
			roleService.doUnAssignRoleByUserid(userid, ds.getIds());
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("取消分配角色失败!");
		}		
		return result;
	}*/
	
	
	@ResponseBody
	@RequestMapping("/doUnAssignRole")
	public Object doUnAssignRole(Integer userid , Data ds) {
		
		start();
		try {			
			roleService.doUnAssignRoleByUserid(userid, ds.getIds());
			success(true);
		} catch (Exception e) {
			e.printStackTrace();
			success(false);
			error("取消分配角色失败!");
		}		
		return end();
	}
	
	
	@RequestMapping("/toAssgnRole")
	public String toAssgnRole(Integer userid,Map<String,Object> map) {
		
		//1.查询所有角色		
		List<Role> allRole = roleService.queryAllRole();
		
		//2.查询用户所拥有的角色
		List<Integer> myRoleidList = roleService.queryRoleidByUserid(userid);
		
		//3.将已经分配的角色存放到右边集合中,将未分配的角色存放到左边集合中
		List<Role> unAssignList = new ArrayList<Role>(); //左边集合:未分配角色
		List<Role> assignList = new ArrayList<Role>(); //右边集合:已分配角色
		
		for(Role role : allRole) {
			if(myRoleidList.contains(role.getId())) {
				assignList.add(role);
			}else {
				unAssignList.add(role);
			}
		}
		
		map.put("assignList", assignList);
		map.put("unAssignList", unAssignList);
		
		
		return "user/assgnRole";
	}
	
	// ?name=zhangsan&age=22   =>> user
	// ?ids[0]=22&ids[1]=23
	@ResponseBody
	@RequestMapping("/doDeleteBatch")
	public Object doDeleteBatch(Data ds) {
		AjaxResult result = new AjaxResult();
		try {
			
			int i = userService.deleteBatchUserByData(ds);
			
			if(i==ds.getUserList().size()) {
				result.setSuccess(true);
			}else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("删除用户失败!");
		}		
		return result;
	}
	
	/*@ResponseBody
	@RequestMapping("/doDeleteBatch")
	public Object doDeleteBatch(Integer[] id) {
		AjaxResult result = new AjaxResult();
		try {
			
			int i = userService.deleteBatchUser(id);
			
			if(i==id.length) {
				result.setSuccess(true);
			}else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("删除用户失败!");
		}		
		return result;
	}*/
	
	@ResponseBody
	@RequestMapping("/doDelete")
	public Object doDelete(Integer id) {
		AjaxResult result = new AjaxResult();
		try {
			
			int i = userService.deleteUser(id);
			
			if(i==1) {
				result.setSuccess(true);
			}else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("删除用户失败!");
		}		
		return result;
	}
	
	@RequestMapping("/toUpdate")
	public String toUpdate(Integer id,Map<String,Object> map) {
		//查询数据进行回显
		User user = userService.getUserById(id);
		map.put("user", user);
		return "user/update";
	}
	
	@ResponseBody
	@RequestMapping("/doUpdate")
	public Object doUpdate(User user) {
		AjaxResult result = new AjaxResult();
		try {
			
			int i = userService.updateUser(user);
			
			if(i==1) {
				result.setSuccess(true);
			}else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("修改用户失败!");
		}		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/doSave")
	public Object save(User user) {
		AjaxResult result = new AjaxResult();
		try {
			
			int i = userService.saveUser(user);
			
			if(i==1) {
				result.setSuccess(true);
			}else {
				result.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("保存用户失败!");
		}		
		return result;
	}
	
	//异步查询 - 条件查询
	@ResponseBody
	@RequestMapping("queryPage")  
	public Object queryPage(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
			@RequestParam(value="pagesize",required=false,defaultValue="1")  Integer pagesize,
			String queryText) {
		
		
		AjaxResult result = new AjaxResult();
		try {
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pageno", pageno);
			paramMap.put("pagesize", pagesize);
			
			if(StringUtil.isNotEmpty(queryText)) {
				if(queryText.contains("%")) {
					//  想把% 替换成 \% 用replaceAll方法，一个\得用4个\\\\
					queryText = queryText.replaceAll("%", "\\\\%");
				}
			}
			
			paramMap.put("queryText", queryText);
			
			
			Page<User> page = userService.queryPage(paramMap);
			
			result.setPage(page);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("分页数据查询失败!");
		}		
		
		return result;
	}
	
	//异步请求
	/*@ResponseBody
	@RequestMapping("queryPage")
	public Object queryPage(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
			@RequestParam(value="pagesize",required=false,defaultValue="1")  Integer pagesize) {
		AjaxResult result = new AjaxResult();
		try {
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("pageno", pageno);
			paramMap.put("pagesize", pagesize);
			
			Page<User> page = userService.queryPage(paramMap);
			
			result.setPage(page);
			result.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			result.setSuccess(false);
			result.setMessage("分页数据查询失败!");
		}		
		
		return result;
	}*/
	
	
	//同步查询
	/*@RequestMapping("queryPage")
	public String queryPage(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
			@RequestParam(value="pagesize",required=false,defaultValue="1")  Integer pagesize,
			Map<String,Object> map) {
		
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("pageno", pageno);
		paramMap.put("pagesize", pagesize);
		
		Page<User> page = userService.queryPage(paramMap);
		
		map.put("page", page);		
		
		return "user/index";
	}*/
	
}
