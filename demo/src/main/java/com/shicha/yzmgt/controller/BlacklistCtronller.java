package com.shicha.yzmgt.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.shicha.yzmgt.bean.BlackList;
import com.shicha.yzmgt.bean.Version;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.SearchBlacklist;
import com.shicha.yzmgt.service.BlacklistService;
import com.shicha.yzmgt.service.DeviceService;
import com.shicha.yzmgt.service.VersionService;
import com.shicha.yzmgt.util.Util;

@RestController
@RequestMapping("/blacklist")
public class BlacklistCtronller {

	private static final Logger log = LoggerFactory.getLogger(BlacklistCtronller.class);
	
	@Autowired
	DeviceService deviceService;
	
	@Autowired
	BlacklistService blackListService;
	
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public APIResult addUser(
			@RequestBody BlackList blacklist,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		blacklist.setUserName((String)req.getSession().getAttribute("userName"));
		
		blackListService.save(blacklist);

		return new APIResult(0);
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.POST)
	public APIResult delUser(
			@RequestBody String[] ids,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		blackListService.remove(ids, (String)req.getSession().getAttribute("userName"));

		return new APIResult(0);
	}
	
//	@RequestMapping(value="/list", method=RequestMethod.GET)
//	public List<BlackList> getVersions(			
//			HttpServletRequest req, HttpServletResponse response) throws IOException{
//		
//		List<BlackList> list= blackListService.getall();
//		
//		return list;
//	}
	
//	@RequestMapping(value="/export", method=RequestMethod.GET)
//	public APIResult getVersions(			
//			HttpServletRequest req, HttpServletResponse response) throws IOException{
//		
//		String filename = "black_list_export_" + Util.formatDate() + ".csv";
//		
//		return new APIResult(0,"",filename);
//	}
	
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public void groups(
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		try {
			String filename = "black_list_export_" + Util.formatDate2() + ".csv";
			
			List<BlackList> bls = blackListService.getall((String)req.getSession().getAttribute("userName"));
			
			response.addHeader("Content-Disposition", "attachment;fileName="+filename);
			response.setContentType("multipart/form-data");
			
			OutputStream fos = new BufferedOutputStream(response.getOutputStream());
			
			byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
			fos.write(uft8bom);
			fos.write(BlackList.title().getBytes("UTF-8"));
			
			for(BlackList bl : bls) {
				String buffer = bl.toString();
				fos.write(buffer.getBytes("UTF-8"));
			}
			
			fos.close();
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	@RequestMapping(value="/list", method=RequestMethod.POST)
	public APIResult searchBlackList(	
			@RequestBody SearchBlacklist filter,
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
		
		Page<BlackList> blackList = blackListService.searchList(filter,(String)req.getSession().getAttribute("userName"));
		
		return new APIResult(0, "", blackList);		
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public APIResult uploadBlacklist(
			@RequestParam("uploadFile") MultipartFile file,			
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		boolean result = blackListService.importBlacklistFromFile(file, (String)req.getSession().getAttribute("userName"));
		
		if(result)
			return new APIResult(0);
		else
			return new APIResult(1, "导入文件失败");
	}
}
