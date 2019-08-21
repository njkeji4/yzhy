package com.shicha.yzmgt.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shicha.yzmgt.bean.Advertise;
import com.shicha.yzmgt.bean.AlarmData;
import com.shicha.yzmgt.bean.DeviceGroup;

public interface IAdvDao extends JpaRepository<Advertise, String>, JpaSpecificationExecutor<Advertise>{

	List<Advertise> findAllByUpdateTimeGreaterThanEqual(Long timestamp);
	
	List<Advertise> findAllByGroupsAndUpdateTimeGreaterThanEqual(DeviceGroup group, Long timestamp);
	
	List<Advertise> findAllByGroupsAndUpdateTimeGreaterThanEqualAndExpireDateGreaterThan(DeviceGroup group, Long timestamp, Long expire);
	
	List<Advertise> findAllByStatusAndGroupsAndUpdateTimeGreaterThanEqualAndExpireDateGreaterThan(int status,DeviceGroup group, Long timestamp, Long expire);
	
}
