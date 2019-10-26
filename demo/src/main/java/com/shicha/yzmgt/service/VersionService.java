package com.shicha.yzmgt.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceGroup;
import com.shicha.yzmgt.bean.Version;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IDeviceGroupDao;
import com.shicha.yzmgt.dao.IUserDao;
import com.shicha.yzmgt.dao.IVersionDao;
import com.shicha.yzmgt.domain.AdvResponse;
import com.shicha.yzmgt.domain.GetVersionReq;

@Service
public class VersionService {

	private static final Logger log = LoggerFactory.getLogger(VersionService.class);
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IVersionDao versionDao;
	
	@Autowired
	IDeviceGroupDao deviceGroupDao;
	
	
	public void addVersion(Version version, String[]groups) {
	
		for(String id : groups) {
			DeviceGroup group = deviceGroupDao.findByGroupId(id);
			if(group == null)continue;
			
			version.addGroup(group);
		}
		
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
		
		log.info("device:"+req.getDeviceNo() + " request version");
		
		Optional<Device>op = deviceDao.findByDeviceNo(req.getDeviceNo());
		if(!op.isPresent()) {
			log.info("device is not registed yet");
			return null;
		}
		if(op.get().getGroupId() == null) {
			log.info("device does not belong to any group");
			return null;
		}
		
		DeviceGroup group = deviceGroupDao.findByGroupId(op.get().getGroupId());
		
		List<Version> versions = getAll();		
		for(Version v : versions) {			
			if(
					v.getOldVersion1().compareTo(req.getVersionNo()) <=0 &&
					v.getOldVersion2().compareTo(req.getVersionNo()) >=0 &&
					inGroup(group, v.getGroups())
					)
			{				
				return v;
			}
		}
		
		return null;
	}
	
	private boolean inGroup(DeviceGroup group, List<DeviceGroup>groups) {
		if(groups == null)
			return true;
		
		for(DeviceGroup g : groups)
			if(group.getGroupName().equals(g.getGroupName())) {
				return true;
			}
		
		return false;
	}
}
