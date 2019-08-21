package com.shicha.yzmgt.service;

//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceGroup;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.bean.Version;
import com.shicha.yzmgt.controller.DeviceController;
import com.shicha.yzmgt.dao.IAlarmDataDao;
import com.shicha.yzmgt.dao.ICheckData;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IUserDao;
import com.shicha.yzmgt.domain.GetVersionReq;
import com.shicha.yzmgt.domain.SearchDevice;

@Service
public class DeviceService {
	private static final Logger log = LoggerFactory.getLogger(DeviceController.class);
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	IAlarmDataDao alarmDao;
	
	@Autowired
	ICheckData checkDataDao;
	
	
	public Page<Device> searchDevice(final SearchDevice device, String userName) {
		
		final User user = userDao.findByName(userName);
		
		Direction orderBy  = (device.getOrder() == null || device.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		Pageable pageable = PageRequest.of(device.getPage(), device.getSize(), Sort.by(orderBy, device.getSort()));	
		
		return deviceDao.findAll(new Specification<Device>() {

			@Override
			public Predicate toPredicate(Root<Device> root, CriteriaQuery<?> criteria, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<>();
				
				if(user != null && !user.getRole().equals(User.ROLE_ADMIN) && 
						user.getGroups() != null && user.getGroups().size() > 0) {
					CriteriaBuilder.In<Object> in = builder.in(root.get("groupId"));
					for(DeviceGroup g : user.getGroups())
						in.value(g.getGroupId());
					
					predicatesList.add(builder.and(
							builder.and(in)
							));
				}
				
				if(device.getName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("name"), "%" + device.getName() + "%")
							));
				}
				if(device.getDeviceNo() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("deviceNo"), "%" + device.getDeviceNo() + "%")
							));
				}
				if(device.getVersionNo() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("versionNo"), "%" + device.getVersionNo() + "%")
							));
				}
				if(device.getStatus() != null) {
					predicatesList.add(builder.and(
							builder.equal(root.get("status"), device.getStatus())
							));
				}
				
				return builder.and(
	                    predicatesList.toArray(new Predicate[predicatesList.size()]));
			}}, pageable);
	}
	
	public void deleteDevice(String deviceNo) {
		
		deviceDao.removeDeviceByDeviceNo(deviceNo);
	}
	
	public boolean nameExisted(String name, String deviceNo) {
		
		Device tmp = deviceDao.findByNameAndDeviceNoNot(name, deviceNo);
		
		return tmp != null;
	}
	
	
	@Transactional
	public boolean updateDevice(Device device) {
		
		Optional<Device> optional = deviceDao.findByDeviceNo(device.getDeviceNo());
		
		if(optional.isPresent()) {
			Device oldDevice = optional.get();			
			
			oldDevice.setName(device.getName());
			oldDevice.setGroupId(device.getGroupId());
			
			deviceDao.save(oldDevice);
			
			return true;
		}		
		return false;
	}	
	
	@Async
	public void updateExistedData(Device device) {
		
		log.info("begin to update alarm and compared record because of change device name and group id");
		
		//alarmDao.updateAlarmData(device.getName(), device.getGroupId(), device.getDeviceNo());		
		
		//checkDataDao.updateCheckata(device.getName(), device.getGroupId(), device.getDeviceNo());
		
		log.info("done");
	}

	public void registerDevice(Device device) {		
		
		device.setLatestHeartBeat(System.currentTimeMillis());
		device.setFirstLogin(System.currentTimeMillis());
		
		Optional<Device> optional = deviceDao.findByDeviceNo(device.getDeviceNo());
		if(optional.isPresent()) {
			Device oldDevice = optional.get();
			
			oldDevice.syncDevice(device);
			
			deviceDao.save(oldDevice);
			
			return;
		}
		
		deviceDao.save(device);		
	}
	
	public boolean deviceExisted(String deviceNo) {
		return deviceDao.findByDeviceNo(deviceNo).isPresent();
	}
	
	public void heartBeat(String deviceNo, String versionNo) {
		Optional<Device> optional = deviceDao.findByDeviceNo(deviceNo);		
		if(!optional.isPresent()) {
			log.info("device is not existed:" + deviceNo);
			return;
		}
		Device d = optional.get();
		
		d.setLatestHeartBeat(System.currentTimeMillis());
		if(versionNo != null && versionNo.length() > 0) {
			d.setVersionNo(versionNo);
		}
		d.setStatus(0);
		deviceDao.save(d);
	}
}
