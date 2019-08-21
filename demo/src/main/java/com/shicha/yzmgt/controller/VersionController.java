package com.shicha.yzmgt.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.bean.Version;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.service.DeviceService;
import com.shicha.yzmgt.service.VersionService;

@RestController
@RequestMapping("/version")
public class VersionController {
	
	private static final Logger log = LoggerFactory.getLogger(VersionController.class);
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	VersionService versionService;
	
	@Value("${version.upload:./upload}")
	String uploadFolder;
	
	@Value("${host.url:http://localhost:8300}")
	String hostUrl;
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public APIResult addUser(
			@RequestBody Version version,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		versionService.addVersion(version);

		return new APIResult(0);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public APIResult delUser(
			@RequestBody String[] ids,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		versionService.removeVersion(ids);

		return new APIResult(0);
	}
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public List<Version> getVersions(			
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		List<Version> versions= versionService.getAll();
		
		return versions;
	}
	
	@RequestMapping(value="/existed", method=RequestMethod.GET)
	public List<String> getExistedVersions(			
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		List<String> versions = versionService.existedVersion();
		
		return versions;
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public APIResult uploadVersion(
			@RequestParam("uploadFile") MultipartFile file,
			@RequestParam("minVersion") String minVersion,
			@RequestParam("maxVersion") String maxVersion,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String filename = UUID.randomUUID().toString() + file.getOriginalFilename();
		String filePath = uploadFolder + "/" + filename; 	
		
		File folder = new File(uploadFolder);
		if(!folder.exists()) {
			folder.mkdirs();
		}
		
		String downloadaddr = hostUrl + "/download/version/" + filename;
		
		Version version = new Version();
		version.setDownloadAddr(downloadaddr);
		version.setOldVersion1(minVersion);
		version.setOldVersion2(maxVersion);
		
		try{
			String abc=file.getOriginalFilename();
			abc=abc.substring(0,abc.length()-4);
			String[]tmp=abc.split("-");
			String vstr=tmp[tmp.length-1];
			version.setVersionNo(vstr);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		version.setUpdateDate(System.currentTimeMillis());
		
        try{
        	file.transferTo(new File(filePath)); 
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
        
		versionService.addVersion(version);

		return new APIResult(0);
	}

}
