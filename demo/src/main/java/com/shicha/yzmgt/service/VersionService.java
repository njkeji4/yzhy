package com.shicha.yzmgt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shicha.yzmgt.bean.Version;
import com.shicha.yzmgt.dao.IDeviceDao;

import com.shicha.yzmgt.dao.IUserDao;
import com.shicha.yzmgt.dao.IVersionDao;
import com.shicha.yzmgt.domain.GetVersionReq;

@Service
public class VersionService {

	private static final Logger log = LoggerFactory.getLogger(VersionService.class);
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IVersionDao versionDao;
	
	
	public void addVersion(Version version) {
	
		versionDao.save(version);
	}
	
	public void removeVersion(String[] ids) {
		for(String id : ids) {
			versionDao.deleteById(id);
		}
	}
	
	public List<Version> getAll(){
		return versionDao.findAll();
	}
	
	public List<String> existedVersion(){
		return deviceDao.getExistedVersion();
	}
	
	public Version getVersion(GetVersionReq req) {
		
		List<Version> versions = getAll();
		for(Version v : versions) {
			if(v.getOldVersion1().compareTo(req.getVersionNo()) <=0 &&
					v.getOldVersion2().compareTo(req.getVersionNo()) >=0)
			{
				return v;
			}
		}
		
		return null;
	}
	
}
