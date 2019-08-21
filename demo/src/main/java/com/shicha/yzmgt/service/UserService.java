package com.shicha.yzmgt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.shicha.yzmgt.bean.DeviceGroup;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IDeviceGroupDao;
import com.shicha.yzmgt.dao.IUserDao;
import com.shicha.yzmgt.domain.ChangePassword;
import com.shicha.yzmgt.domain.UserDomain;

@Service
public class UserService {

	private static final Logger log = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	IDeviceGroupDao groupDao;
	
	
	@Transactional
	public User addUser(UserDomain userDomain) {
		
		if(userDomain.getPassword() == null || userDomain.getPassword().length() == 0) {
			userDomain.setPassword("123456");
		}
		String password = DigestUtils.md5DigestAsHex(userDomain.getPassword().getBytes());
		
		User user = new User();
		user.setName(userDomain.getName());
		user.setRole(userDomain.getRole());
		user.setPassword(password);
		
		for(String id : userDomain.getGroupIds()) {
			DeviceGroup dg = groupDao.findByGroupId(id);
			if(user.getGroups() == null) {
				user.setGroups(new ArrayList<DeviceGroup>());
			}
			user.getGroups().add(dg);
		}
		
		User newUser = userDao.save(user);
		
		return newUser;
	}
	
	@Transactional
	public void updateUser(User user) {
		
		if(!userDao.findById(user.getId()).isPresent()) {
			return;
		}
		
		User existedUser = userDao.findById(user.getId()).get();
		if(existedUser.getRole().equals("ROLE_ADMIN")) {
			return;
		}
		
		existedUser.setGroups(user.getGroups());
		userDao.save(existedUser);
	}
	
	public User getUser(String name) {
		User user = userDao.findByName(name);
		
		return user;
	}
	
	@Transactional
	public boolean changePassword(ChangePassword domain) {
		
		User user = userDao.findByName(domain.getUsername());
		if(user == null) {
			return false;
		}
		if(!user.getPassword().equals(domain.getPassword())) {
			return false;
		}
		
		user.setPassword(domain.getNewPassword());
		
		userDao.save(user);
		
		return true;	
		
	}
	
	public void delUsers(String[] ids) {
		for(String id : ids) {
			userDao.deleteById(id);
		}
	}
	
	public List<User>getAllUser(){
		List<User> users= userDao.findAll();
		
		for(User user: users) {
			user.setPassword(null);
		}
		return users;
	}
}
