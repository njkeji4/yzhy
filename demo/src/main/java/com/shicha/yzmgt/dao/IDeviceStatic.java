package com.shicha.yzmgt.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shicha.yzmgt.bean.CheckData;

import com.shicha.yzmgt.bean.DeviceStat;

public interface IDeviceStatic extends JpaRepository<DeviceStat, String> {

	
	DeviceStat findFirstByStatDateAndDeviceNoAndStatType(Long statDate, String deviceNo, int deviceType);
}
