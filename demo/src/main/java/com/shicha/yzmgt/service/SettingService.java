package com.shicha.yzmgt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shicha.yzmgt.bean.Setting;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.dao.IDeviceDao;

import com.shicha.yzmgt.dao.ISettingDao;
import com.shicha.yzmgt.dao.IUserDao;

@Service
public class SettingService {
	private static final Logger log = LoggerFactory.getLogger(SettingService.class);	
	
	@Autowired
	ISettingDao settingDao;
	
	@Autowired
	IUserDao userDao;
	
	public void updateSetting(Setting setting, String userName) {
		
		User user = userDao.findByName(userName);
		
		List<Setting>settings = settingDao.findAll();
		if(settings == null || settings.size() == 0) {
			settingDao.save(setting);
		}else {
			Setting old = settings.get(0);
			old.setAlarmPhone(setting.getAlarmPhone());
			old.setErrorTimes(setting.getErrorTimes());
			settingDao.save(old);
		}
		
		if(user != null && setting.getAlarmPhone() != null) {
			user.setPhone(setting.getAlarmPhone());
			userDao.save(user);
		}
	}
	
	public Setting getSetting(String userName) {
		
		User user = userDao.findByName(userName);
		List<Setting>settings = settingDao.findAll();
		
		if(settings == null || settings.size() == 0) {
			return null;
		}else {
			if(user != null && user.getPhone() != null)
				settings.get(0).setAlarmPhone(user.getPhone());
			return settings.get(0);
		}
	}
	
	public Setting getSetting() {
		
		
		List<Setting>settings = settingDao.findAll();
		
		if(settings == null || settings.size() == 0) {
			return null;
		}else {
			return settings.get(0);
		}
	}
	
}
