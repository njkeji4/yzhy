package com.shicha.yzmgt.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceGroup;
import com.shicha.yzmgt.domain.StatGroup;
import com.shicha.yzmgt.domain.TodayData;

@Repository
public interface IDeviceDao extends JpaRepository<Device, String>,JpaSpecificationExecutor<Device>{
	
	Device findByName(String name);
	
	Device findByNameAndDeviceNoNot(String name, String deviceNo);
	
	@Transactional
	@Modifying
	@Query(value="delete from device where device_no=:deviceNo",nativeQuery=true)
	void removeDeviceByDeviceNo(@Param("deviceNo") String deviceNo);
	
	Optional<Device> findByDeviceNo(String deviceNo);
	
	List<Device> findByStatusIs(int status);
	
	@Transactional
	@Modifying
	@Query(value="select distinct version_no from device order by version_no",nativeQuery=true)
	List<String> getExistedVersion();
	
	
	@Transactional
	@Modifying
	@Query(value="update device set today_succ_count = 0, today_fail_count = 0,today_alarm=0",nativeQuery=true)
	void clearTodayStatistics();
	
	
	@Transactional
	@Query(value="select new com.shicha.yzmgt.domain.TodayData("		
		+ "sum(todaySuccCount), sum(todayFailCount), sum(totalSuccCount), sum(totalFailCount),"
		+ "count(status) -  sum(status), sum(status), sum(todayAlarm), sum(totalAlarm)"
		+ ") from device")
	TodayData selectTodayStatistics();
	
	@Transactional
	@Query(value="select new com.shicha.yzmgt.domain.StatGroup(groupId,sum(totalSuccCount),"
			+ "sum(totalFailCount),sum(totalAlarm)) from device group by groupId")
	List<StatGroup> selectGroupStatistics();

	@Transactional
	@Query(value="select new com.shicha.yzmgt.domain.TodayData("		
		+ "sum(todaySuccCount), sum(todayFailCount), sum(totalSuccCount), sum(totalFailCount),"
		+ "count(status) - sum(status), sum(status), sum(todayAlarm), sum(totalAlarm)"
		+ ") from device where groupId in ?1")
	TodayData selectTodayStatistics(List<String>groupIds);
	
	
	@Query(value="select * from device where group_id in ?1",nativeQuery=true)
	List<Device> getAll(List<String>groupIds);

	
	@Transactional
	@Query(value="select new com.shicha.yzmgt.bean.DeviceGroup("		
		+ "count(*) as deviceCount, groupId"
		+ ") from device group by groupId")
	List<DeviceGroup> getDeviceCountByGroup();
}



