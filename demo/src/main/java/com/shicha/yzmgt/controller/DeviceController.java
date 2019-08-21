package com.shicha.yzmgt.controller;



import java.io.IOException;
import java.security.Principal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shicha.yzmgt.bean.CheckData;
import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.DeviceRespond;
import com.shicha.yzmgt.domain.SearchDevice;
import com.shicha.yzmgt.domain.TodayData;
import com.shicha.yzmgt.service.CheckDataService;
import com.shicha.yzmgt.service.DeviceService;
import com.shicha.yzmgt.util.Util;

@RestController
@RequestMapping("/device")
public class DeviceController {

	private static final Logger log = LoggerFactory.getLogger(DeviceController.class);
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	CheckDataService checkDataService;
	
	@RequestMapping(value="/searchDevice", method=RequestMethod.POST)
	public APIResult searchDevice(
			@RequestBody SearchDevice search,
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
			
			String userName = (String)req.getSession().getAttribute("userName");
		
			Page<Device>devices = deviceService.searchDevice(search, userName);
		
			return new APIResult(0, "", devices);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public APIResult deleteDevice(
			@RequestBody String[]	devices,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		for(String device : devices) {
			deviceService.deleteDevice(device);		
		}
		
		return new APIResult(0);
	}
	
	@RequestMapping(value="/update", method=RequestMethod.POST)
	public APIResult updateDevice(
			@RequestBody Device	device,
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
		
		if(deviceService.nameExisted(device.getName(), device.getDeviceNo())) {
			return new APIResult(1, "设备名字已经存在");
		}
		
		deviceService.updateDevice(device);
		
//		if(deviceService.updateDevice(device)) {		
//		
//			//deviceService.updateExistedData(device);
//		}
		
		return new APIResult(0);
	}
	
	@RequestMapping(value="/todayData", method=RequestMethod.GET)
	public APIResult getStatis(HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		String userName = (String)req.getSession().getAttribute("userName");
		
		TodayData data = checkDataService.getTodayData(userName);
		
		return new APIResult(0, "", data);
	}
			
}
