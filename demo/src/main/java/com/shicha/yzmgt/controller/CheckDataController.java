package com.shicha.yzmgt.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shicha.yzmgt.bean.BlackList;
import com.shicha.yzmgt.bean.CheckData;
import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.SpotImg;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.SearchCheckData;
import com.shicha.yzmgt.domain.SearchDevice;
import com.shicha.yzmgt.service.CheckDataService;
import com.shicha.yzmgt.service.DeviceService;
import com.shicha.yzmgt.util.Util;

@RestController
@RequestMapping("/checkdata")
public class CheckDataController {

	@Autowired
	DeviceService deviceService;
	
	@Autowired
	CheckDataService checkDataService;	
	
	@RequestMapping(value="/search", method=RequestMethod.POST)
	public APIResult searchData(
			@RequestBody SearchCheckData search,
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
		
			//Page<CheckData>datas = checkDataService.searchCheckData(search, (String)req.getSession().getAttribute("userName"));
			
			Map datas = checkDataService.searchCheckData3(search, (String)req.getSession().getAttribute("userName"));
		
			return new APIResult(0, "", datas);
	}
	
	@Value("${export.maxcount:10000}")
	int export_max;
	
	@RequestMapping(value="/export", method=RequestMethod.GET)
	public void searchDataToExport(
			@RequestParam(name="deviceName", required=false)String deviceName,
			@RequestParam(name="result", required=false)Integer result,
			@RequestParam(name="name", required=false)String name,
			@RequestParam(name="cardNo", required=false)String cardNo,
			@RequestParam(name="betrween1", required=false)Long betrween1,
			@RequestParam(name="betrween2", required=false)Long betrween2,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
			SearchCheckData search = new SearchCheckData(); 
			search.setSize(1000);
			search.setDeviceName(deviceName);
			search.setResult(result);
			search.setName(name);
			search.setCardNo(cardNo);
			search.setBetrween1(betrween1);
			search.setBetrween2(betrween2);			
			
			String filename = "compare_record_export_" + Util.formatDate2() + ".csv";
			
			response.addHeader("Content-Disposition", "attachment;fileName="+filename);
			response.setContentType("multipart/form-data");
			
			OutputStream fos = new BufferedOutputStream(response.getOutputStream());
			
			byte[] uft8bom={(byte)0xef,(byte)0xbb,(byte)0xbf};
			fos.write(uft8bom);
			fos.write(CheckData.Title().getBytes("UTF-8"));
			
			HashMap datas = checkDataService.searchCheckData3(search, (String)req.getSession().getAttribute("userName"));
			BigInteger total = (BigInteger)datas.get("totalElements");
			List<CheckData> list = (List<CheckData>)datas.get("content");
			//System.out.println("list.size"+list.size()+" toalcount="+total.intValue());
			int count = 0;
			int page = 0;
			while(count < total.intValue()) {
				for(CheckData cd: list) {
					String buffer = cd.toString();
					fos.write(buffer.getBytes("UTF-8"));
					count++;
				}
				if(count > export_max)
					break;
				
				search.setPage(++page);
				datas = checkDataService.searchCheckData3(search, (String)req.getSession().getAttribute("userName"));				
				list = (List<CheckData>)datas.get("content");
				if(list == null || list.size() == 0)
					break;
			}
			
			fos.close();
	}
	
	@RequestMapping(value="/spotimg", method=RequestMethod.GET)
	public APIResult getSpotImg(
			@RequestParam(name="id")String id,
			HttpServletRequest req, HttpServletResponse response) throws IOException{		
		
			SpotImg img = checkDataService.getSpotImg(id);
		
			return new APIResult(0, "", img);
	}
}
