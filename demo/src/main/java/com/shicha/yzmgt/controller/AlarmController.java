package com.shicha.yzmgt.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shicha.yzmgt.bean.AlarmData;
import com.shicha.yzmgt.bean.BlackList;
import com.shicha.yzmgt.bean.CheckData;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.SearchAlarmData;
import com.shicha.yzmgt.domain.SearchCheckData;
import com.shicha.yzmgt.service.BlacklistService;
import com.shicha.yzmgt.service.CheckDataService;
import com.shicha.yzmgt.service.DeviceService;
import com.shicha.yzmgt.util.Util;

@RestController
@RequestMapping("/alarm")
public class AlarmController {

	private static final Logger log = LoggerFactory.getLogger(AlarmController.class);
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	CheckDataService checkDataService;	
	
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public List<AlarmData> getVersions(			
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		List<AlarmData> list= checkDataService.getAlamrs();
		
		return list;
	}
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public APIResult searchDevice(
			@RequestBody SearchAlarmData search,
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
		
			Page<AlarmData>datas = checkDataService.searchAlarmData(search,(String)req.getSession().getAttribute("userName"));
		
			return new APIResult(0, "", datas);
	}
	
	@Value("${export.maxcount:10000}")
	int export_max;
	
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public void searchDataToExport(
			@RequestParam(name="deviceName", required=false)String deviceName,
			@RequestParam(name="name", required=false)String name,
			@RequestParam(name="alarmType", required=false)Integer alarmType,
			@RequestParam(name="cardNo", required=false)String cardNo,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
			SearchAlarmData search = new SearchAlarmData(); 
			search.setSize(100);
			search.setDeviceName(deviceName);
			search.setAlarmType(alarmType);
			search.setName(name);
			search.setCardNo(cardNo);
			
			String filename = "alarm_record_export_" + Util.formatDate2() + ".csv";
			
			response.addHeader("Content-Disposition", "attachment;fileName="+filename);
			response.setContentType("multipart/form-data");
			
			OutputStream fos = new BufferedOutputStream(response.getOutputStream());
			
			byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
			fos.write(uft8bom);
			fos.write(CheckData.Title().getBytes("UTF-8"));
			
			Page<AlarmData>datas = checkDataService.searchAlarmData(search, (String)req.getSession().getAttribute("userName"));
			
			int count = 0;
			int page = 0;
			while(page < datas.getTotalPages()) {
				for(AlarmData cd: datas.getContent()) {
					String buffer = cd.toString();
					fos.write(buffer.getBytes("UTF-8"));
					count++;
				}
				if(count > export_max)
					break;
				
				search.setPage(++page);
				datas = checkDataService.searchAlarmData(search,(String)req.getSession().getAttribute("userName"));
			}
			
			fos.close();
	}
}
