package com.shicha.yzmgt.service;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.shicha.yzmgt.bean.AdResult;
import com.shicha.yzmgt.bean.Advertise;
import com.shicha.yzmgt.bean.AlarmData;
import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceGroup;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.dao.IAdResultDao;
import com.shicha.yzmgt.dao.IAdvDao;
import com.shicha.yzmgt.dao.IBlaclistDao;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IDeviceGroupDao;
import com.shicha.yzmgt.dao.IUserDao;
import com.shicha.yzmgt.domain.APIResult;
import com.shicha.yzmgt.domain.AdvRequest;
import com.shicha.yzmgt.domain.AdvResponse;
import com.shicha.yzmgt.domain.SearchAdv;
import com.shicha.yzmgt.domain.SearchAdvResult;

@Service
public class AdverService {

	private static final Logger log = LoggerFactory.getLogger(AdverService.class);
	
	@Autowired
	IAdvDao advDao;
	
	@Autowired
	IAdResultDao adResultDao;
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	IDeviceGroupDao deviceGroupDao;
	
	
	@Value("${encrypt.key:xxxx}")
	String keyValue;
	
	public AdvResponse getAds(AdvRequest req) {
		
		try{
			
			log.info("device:"+req.getDeviceNo() + " request advertisement");
			
			Optional<Device>op = deviceDao.findByDeviceNo(req.getDeviceNo());
			if(!op.isPresent()) {
				log.info("device is not registed yet");
				return new 	AdvResponse("device is not registed yet");
			}
			if(op.get().getGroupId() == null) {
				log.info("device does not belong to any group");
				return new 	AdvResponse("device does not belong to any group");
			}
			
			DeviceGroup group = deviceGroupDao.findByGroupId(op.get().getGroupId());
			
			Long timestamp = req.getTime_stamp() == null ? req.getTimestamp() : req.getTime_stamp();
			List<Advertise> list;
			if(timestamp != 0)
				list = advDao.findAllByGroupsAndUpdateTimeGreaterThanEqualAndExpireDateGreaterThan(group, 
						timestamp, System.currentTimeMillis());
			else
				list = advDao.findAllByStatusAndGroupsAndUpdateTimeGreaterThanEqualAndExpireDateGreaterThan(0,group, 
						timestamp, System.currentTimeMillis());
			
			return new AdvResponse(list, keyValue);	
			
		}catch(Exception ex) {
			return new 	AdvResponse(ex.getMessage());
		}
	}
	
	
	public void addAdv(Advertise adv, String[] groupIds, String user) {
		
		for(String id : groupIds) {
			DeviceGroup group = deviceGroupDao.findByGroupId(id);
			if(group == null)continue;
			
			adv.addGroup(group);
		}
		
		adv.setCreateUser(user);
		
		advDao.save(adv);
	}
	
	
	public void delAdv(String[] ids) {	
		
		for(String id : ids) {
			Optional<Advertise> op = advDao.findById(id);
			if(op.isPresent()) {
				Advertise old = op.get();
				old.setUpdateTime(System.currentTimeMillis());
				old.setStatus(Advertise.status_del);
				advDao.save(old);
			}
		}
	}
	
	public Page<Advertise> searchAdv(SearchAdv adv) {
		
		Direction orderBy  = (adv.getOrder() == null || adv.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		Pageable pageable = PageRequest.of(adv.getPage(), adv.getSize(), Sort.by(orderBy, adv.getSort()));	
		
		if(adv.getBetrween1() == null) {
			adv.setBetrween1(0l);
		}
		if(adv.getBetrween2() == null) {
			adv.setBetrween2(System.currentTimeMillis());
		}
		
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userDao.findByName(userName);
		
		return advDao.findAll(new Specification<Advertise>() {

			@Override
			public Predicate toPredicate(Root<Advertise> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<>();
				
				predicatesList.add(builder.and(
						builder.equal(root.get("status"), Advertise.status_new)
						));
				
				if(user != null && !user.getRole().equals(User.ROLE_ADMIN)) {
					
					predicatesList.add(builder.and(
							builder.equal(root.get("createUser"), userName)
							));
				}
				
				if(adv.getAdvType() != null) {
					predicatesList.add(builder.and(
							builder.equal(root.get("advType"), adv.getAdvType())
							));
				}
				
				if(adv.getBetrween1() != null) {
					predicatesList.add(builder.and(
							builder.ge(root.get("createTime"), adv.getBetrween1())
							));
				}
				if(adv.getBetrween2() != null) {
					predicatesList.add(builder.and(
							builder.le(root.get("createTime"), adv.getBetrween2())
							));
				}				
				
				return builder.and(
	                    predicatesList.toArray(new Predicate[predicatesList.size()]));
				
			}
			
		}, pageable);
	}	
	
	//result
	public APIResult reportResult(AdResult result) {
		
		Optional<Device> opt = deviceDao.findByDeviceNo(result.getDeviceNo());
		if(!opt.isPresent()) {
			return new APIResult(1, "device is not exsited");
		}
		Optional<Advertise> opt2 = advDao.findById(result.getAdId());
		if(!opt2.isPresent()) {
			return new APIResult(1, "ad is not exsited");
		}
		
		if(adResultDao.findByDeviceNoAndAdId(result.getDeviceNo(), result.getAdId()) != null) {
			log.info("repeat report,ignore");
			return new APIResult(0);
		}
		
		Device device = opt.get();
		Advertise ad = opt2.get();
		String groupName="";
		for(DeviceGroup g : ad.getGroups()) {
			if(g.getGroupId().equals(device.getGroupId())){
				groupName = g.getGroupName();
				break;
			}
		}
		
		result.setDeviceName(device.getName());
		result.setGroupName(groupName);
		result.setAdTitle(ad.getTitle());
		result.setEndTime(ad.getExpireDate());
		result.setCreateUser(ad.getCreateUser());
		
		adResultDao.save(result);
		
		return new APIResult(0);
	}
	
	public Page<AdResult> searchAdResult(SearchAdvResult adv) {
		
		Direction orderBy  = (adv.getOrder() == null || adv.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		Pageable pageable = PageRequest.of(adv.getPage(), adv.getSize(), Sort.by(orderBy, adv.getSort()));	
		
		if(adv.getBetrween1() == null) {
			adv.setBetrween1(0l);
		}
		if(adv.getBetrween2() == null) {
			adv.setBetrween2(System.currentTimeMillis());
		}
		
		return adResultDao.findAll(new Specification<AdResult>() {

			@Override
			public Predicate toPredicate(Root<AdResult> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<>();
				
				if(adv.getBetrween1() != null) {
					predicatesList.add(builder.and(
							builder.ge(root.get("startTime"), adv.getBetrween1())
							));
				}
				if(adv.getBetrween2() != null) {
					predicatesList.add(builder.and(
							builder.le(root.get("startTime"), adv.getBetrween2())
							));
				}	
				if(adv.getAdTitle() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("adTitle"), "%" + adv.getAdTitle() + "%")
						));
				}
				if(adv.getDeviceName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("deviceName"), "%" + adv.getDeviceName()+ "%")
						));
				}
				
				if(adv.getGroupName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("groupName"), adv.getGroupName())
						));
				}
				
				if(adv.getCreateUser() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("createUser"), adv.getCreateUser())
						));
				}
				
				return builder.and(
	                    predicatesList.toArray(new Predicate[predicatesList.size()]));
				
			}
			
		}, pageable);
	}	
}
