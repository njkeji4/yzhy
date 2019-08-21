package com.shicha.yzmgt.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.shicha.yzmgt.bean.AdResult;
import com.shicha.yzmgt.bean.CheckData;
import com.shicha.yzmgt.bean.Device;
//import com.shicha.yzmgt.bean.DeviceLog;

import com.shicha.yzmgt.bean.Version;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.AdvRequest;
import com.shicha.yzmgt.domain.AdvResponse;
import com.shicha.yzmgt.domain.DeviceRespond;
import com.shicha.yzmgt.domain.GetVersionReq;
import com.shicha.yzmgt.domain.PersonIncrement;
import com.shicha.yzmgt.service.AdverService;
import com.shicha.yzmgt.service.CheckDataService;
//import com.shicha.yzmgt.service.DeviceLogService;
import com.shicha.yzmgt.service.DeviceService;

import com.shicha.yzmgt.service.VersionService;
import com.shicha.yzmgt.util.Util;

@RestController
@RequestMapping("/terminal")
public class TerminalController {

	private static final Logger log = LoggerFactory.getLogger(TerminalController.class);
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	VersionService versionService;
	
	@Autowired
	AdverService advService;
	
	@Autowired
	CheckDataService checkDataService;
	
	@RequestMapping(value="/regDevice", method=RequestMethod.POST)
	public DeviceRespond registerDevice(
			@RequestBody Device device,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		log.info("device regDevice:"+device.getDeviceNo());
		
		int ret = 0;
		String msg = "成功";		
		
		deviceService.registerDevice(device);		
		
		return new DeviceRespond(ret, msg);
	}
	
	@RequestMapping(value="/keepHeart", method=RequestMethod.POST)
	public DeviceRespond heartBeat(
			@RequestBody Device device,
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
		
		int ret = 0;
		String msg = "成功";		
		if(deviceService.deviceExisted(device.getDeviceNo())) {
			deviceService.heartBeat(device.getDeviceNo(), device.getVersionNo());
			return new DeviceRespond(ret, msg);
		}
		
		log.info("device is not registered:"+device.getDeviceNo());
		
		ret = 1001;
		HashMap<String, String>data = new HashMap<String,String>();		
		data.put("sysTime", Util.formatDate());
		return new DeviceRespond(ret, msg, data);
	}
	
	@RequestMapping(value="/uploadCmpRecord", method=RequestMethod.POST)
	public DeviceRespond uploadCmpRecord(
			@RequestBody CheckData checkData,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		log.info("device uploadCmpRecord");
		
		int ret = 0;
		String msg = "成功";		
		
		checkDataService.saveCmpRecord(checkData);

		return new DeviceRespond(ret, msg, null);
	}
	
	@RequestMapping(value="/getVersion", method=RequestMethod.POST)
	public DeviceRespond getVersion(
			@RequestBody GetVersionReq versionReq,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		log.info("device send get version request");
		
		int ret = 0;
		String msg = "成功";		
		
		Version version = versionService.getVersion(versionReq);
		if(version == null) {
			ret = 1004;
			return new DeviceRespond(ret, msg, null);
		}		
		
		HashMap<String,String> data = new HashMap<String,String>();
		data.put("downloadAddr", version.getDownloadAddr());
		data.put("updateDate", Util.formatDate(version.getUpdateDate()));
		data.put("versionNo", version.getVersionNo());
		
		return new DeviceRespond(ret, msg, data);
	}	
	
	@RequestMapping(value="/data/ad", method=RequestMethod.POST)
	public AdvResponse getAd(
			@RequestBody AdvRequest advReq,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
			log.info("" + advReq.getTime_stamp());
			log.info("" + advReq.getTimestamp());
		
			return advService.getAds(advReq);
	}
	
	@RequestMapping(value="/ad/feedback", method=RequestMethod.POST)
	public APIResult adFeedback(
			@RequestBody AdResult result,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
			log.info("request:"+JSON.toJSONString(result));
		
			return advService.reportResult(result);
			
			//return new APIResult(0);
	}
}
