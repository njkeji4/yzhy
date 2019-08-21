package com.shicha.yzmgt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.shicha.yzmgt.bean.AdResult;
import com.shicha.yzmgt.bean.Advertise;

public interface IAdResultDao extends JpaRepository<AdResult, String>, JpaSpecificationExecutor<AdResult>{

	AdResult findByDeviceNoAndAdId(String deviceNo, String adId);
}
