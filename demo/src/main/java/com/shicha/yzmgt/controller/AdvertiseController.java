package com.shicha.yzmgt.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

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
import org.springframework.web.multipart.MultipartFile;

import com.shicha.yzmgt.bean.AdResult;
import com.shicha.yzmgt.bean.Advertise;
import com.shicha.yzmgt.bean.AlarmData;
import com.shicha.yzmgt.bean.Version;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.AdvRequest;
import com.shicha.yzmgt.domain.AdvResponse;
import com.shicha.yzmgt.domain.SearchAdv;
import com.shicha.yzmgt.domain.SearchAdvResult;
import com.shicha.yzmgt.domain.SearchAlarmData;
import com.shicha.yzmgt.service.AdverService;
import com.shicha.yzmgt.service.DeviceService;

@RestController
@RequestMapping("/advertisement")
public class AdvertiseController {

	private static final Logger log = LoggerFactory.getLogger(AdvertiseController.class);
	
	@Autowired
	AdverService advService;
	
	@Value("${version.upload:./upload}")
	String uploadFolder;
	
	@Value("${host.url:http://localhost:8300}")
	String hostUrl;
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public APIResult uploadVersion(
			@RequestParam("uploadFile") MultipartFile file,
			@RequestParam("adType") int adType,
			@RequestParam("title") String title,
			@RequestParam("groups") String[] groups,
			@RequestParam("expire") Long expire,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String filename = UUID.randomUUID().toString() + file.getOriginalFilename();
		String filePath = uploadFolder + "/ad/" + filename; 	
		
		File folder = new File(uploadFolder+"/ad");
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
		String downloadaddr = hostUrl + "/download/ad/" + filename;
		
		Advertise adv = new Advertise();
		
		adv.setTitle(title);
		adv.setUrl(downloadaddr);
		adv.setCreateTime(System.currentTimeMillis());
		adv.setUpdateTime(adv.getCreateTime());
		adv.setStatus(Advertise.status_new);
		adv.setAdvType(adType);
		adv.setExpireDate(expire);
		
        try{
        	file.transferTo(new File(filePath)); 
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
        
        String userName = (String)request.getSession().getAttribute("userName");
        
		advService.addAdv(adv, groups, userName);

		return new APIResult(0);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public APIResult deleteAdv(
			@RequestBody String[] ids,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
			 advService.delAdv(ids);
			 
			 return new APIResult(0);
	}
	
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public APIResult searchAdv(
			@RequestBody SearchAdv adv,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
			Page<Advertise> ads = advService.searchAdv(adv);
			
			return new APIResult(0,"",ads);
	}
	
	@RequestMapping(value="/result", method=RequestMethod.POST)
	public APIResult searchAdvResult(
			@RequestBody SearchAdvResult adv,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
			Page<AdResult> ads = advService.searchAdResult(adv);
			
			return new APIResult(0,"",ads);
	}
}
