package com.shicha.yzmgt.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shicha.yzmgt.bean.Setting;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.service.SettingService;
import com.shicha.yzmgt.service.UserService;

@RestController
@RequestMapping("/setting")
public class SettingController {

	private static final Logger log = LoggerFactory.getLogger(SettingController.class);
	
	@Autowired
	SettingService settingService;
	
	@RequestMapping(value="/get", method=RequestMethod.GET)
	public APIResult getSetting(HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		String userName = (String)req.getSession().getAttribute("userName");
		Setting setting = settingService.getSetting(userName);
		
		return new APIResult(0, "", setting);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public APIResult update(
			@RequestBody Setting setting,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		String userName = (String)req.getSession().getAttribute("userName");
		settingService.updateSetting(setting, userName);
		
		return new APIResult(0, null, null);
	}
	
}
