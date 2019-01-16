package com.atguigu.atcrowdfunding.manager.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.dao.UserMapper;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.util.Const;
import com.atguigu.atcrowdfunding.util.MD5Util;
import com.atguigu.atcrowdfunding.util.Page;
import com.atguigu.atcrowdfunding.vo.Data;

@Service
//@Scope("prototype")
//@Scope("singleton")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	@Override
	public User queryUserByLogin(Map<String, Object> paramMap) {
		
		String userpswd = (String)paramMap.get("userpswd");
		
		String md5userpswd = MD5Util.digest(userpswd);
		
		paramMap.put("userpswd", md5userpswd);
		
		User user = userMapper.queryUserByLogin(paramMap);
		/*if(user==null) {
			throw new LoginFailureException("用户名称或密码不对,请重新登录!");
		}*/
		return user;
	}

	@Override
	public Page<User> queryPage(Map<String, Object> paramMap) {
		Integer pageno = (Integer)paramMap.get("pageno");
		Integer pagesize = (Integer)paramMap.get("pagesize");
		
		Page<User> page = new Page<User>(pageno,pagesize);	
		
		int startIndex = page.getStartIndex();
		paramMap.put("startIndex", startIndex);
		
		List<User> datas = userMapper.queryPageList(paramMap);
		
		int totalsize = userMapper.count(paramMap);
		
		page.setDatas(datas);
		
		page.setTotalsize(totalsize);
		
		return page;
	}

	@Override
	public int saveUser(User user) {	
		user.setUserpswd(MD5Util.digest(Const.USERPSWD));
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String createTime = sdf.format(new Date());
		
		user.setCreatetime(createTime);
		return userMapper.insert(user);
	}

	@Override
	public User getUserById(Integer id) {
		return userMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateUser(User user) {
		return userMapper.updateByPrimaryKey(user);
	}

	@Override
	public int deleteUser(Integer id) {
		return userMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int deleteBatchUser(Integer[] id) {
		int count = 0 ;
		for (Integer userid : id) {
			int i = userMapper.deleteByPrimaryKey(userid);
			count += i ;
		}
		return count;
	}

	@Override
	public int deleteBatchUserByData(Data ds) {
		List<User> userList = ds.getUserList();
		int i = userMapper.deleteBatchUserByData(ds.getUserList());
		return i;
	}

	@Override
	public List<Permission> queryPermissionSelf(Integer id) {
		return userMapper.queryPermissionSelf(id);
	}

}
