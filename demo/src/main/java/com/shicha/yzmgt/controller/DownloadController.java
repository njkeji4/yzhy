package com.shicha.yzmgt.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



//@Controller
//@RequestMapping("/download")
public class DownloadController {

	@Value("${version.upload:./upload}")
	String uploadFolder;
	
	@RequestMapping(value="/version/{filename}", method=RequestMethod.GET)
	public void groups(	
			@PathVariable(name="filename") String filename,
			HttpServletRequest req, HttpServletResponse response) throws IOException{
		
		try {
			
			response.addHeader("Content-Disposition", "attachment;fileName"+filename);
			response.setContentType("multipart/form-data");
			
			String filePath = uploadFolder + "/" + filename;
			File file = new File(filePath);
			//response.setHeader("Content-Length")
			response.setContentLength((int)file.length());
			
			InputStream fis = new BufferedInputStream(new FileInputStream(file));
			OutputStream fos = new BufferedOutputStream(response.getOutputStream());
			byte[] buffer = new byte[10240];
			int len = 0;
			while((len = fis.read(buffer)) != -1) {
				fos.write(buffer,0,len);
			}
			
			fis.close();
			fos.close();		
			
			
		}catch(Exception ex) {
			
		}
		
	}
}
