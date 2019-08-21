package com.shicha.yzmgt.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shicha.yzmgt.bean.AlarmData;
import com.shicha.yzmgt.bean.BlackList;
import com.shicha.yzmgt.bean.CheckData;

public interface IAlarmDataDao extends JpaRepository<AlarmData, String>, JpaSpecificationExecutor<AlarmData>{

	@Query(value="select count(*) from alarm_data where alarm_time >= :start && alarm_time <= :end",nativeQuery=true)
	int getAlarmCount(@Param("start") long start, @Param("end") long end);
	
	
	@Transactional
	@Modifying
	@Query(value="update alarm_data set device_name=:deviceName, group_id=:groupId where device_no=:deviceNo",nativeQuery=true)
	public void updateAlarmData(@Param("deviceName") String deviceName, @Param("groupId") String groupId, @Param("deviceNo") String deviceNo);
}
