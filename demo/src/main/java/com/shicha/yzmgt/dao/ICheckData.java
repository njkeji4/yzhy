package com.shicha.yzmgt.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shicha.yzmgt.bean.CheckData;
import com.shicha.yzmgt.bean.Device;

public interface ICheckData extends JpaRepository<CheckData, String>,JpaSpecificationExecutor<CheckData>{

	Page<CheckData> findByName(String name, Pageable pageable);
	
	Page<CheckData> findByNameContainingAndDeviceNameContainingAndCardNoContainingAndCompareDateBetween(
			String name, String deviceName, String cardNo, Long start, Long end, Pageable pageable);	
	
	Page<CheckData> findByResultAndNameContainingAndDeviceNameContainingAndCardNoContainingAndCompareDateBetween(
			Integer result, String name, String deviceName, String cardNo, Long start, Long end, Pageable pageable);
	
	Page<CheckData> findByResultAndNameContainingAndDeviceNameContainingAndCardNoContaining(
			Integer result, String name, String deviceName, String cardNo, Pageable pageable);
	
	Page<CheckData> findByResultLessThan(Integer result, Pageable pageable);
	
	CheckData findFirstByCompareDateAndCardNo(Long compareDate, String cardNo);
	
	
	@Query(value="select count(*) from check_data where result = 1 && compare_date >= :start && compare_date <= :end",nativeQuery=true)
	int getSuccessData(@Param("start") long start, @Param("end") long end);
	
	@Query(value="select count(*) from check_data where result != 1 && compare_date >= :start && compare_date <= :end",nativeQuery=true)
	int getFailData(@Param("start") long start, @Param("end") long end);
	
	@Query(value="select * from check_data where compare_date >= :start && compare_date <= :end",nativeQuery=true)
	List<CheckData> getCheckData(@Param("start") long start, @Param("end") long end);
	
	
	@Transactional
	@Modifying
	@Query(value="update check_data set device_name=:deviceName, group_id=:groupId where device_no=:deviceNo",nativeQuery=true)
	public void updateCheckata(@Param("deviceName") String deviceName, @Param("groupId") String groupId,@Param("deviceNo") String deviceNo);
	

	CheckData findFirstByCompareDateBetweenAndDeviceNo(Long start, Long end, String deviceNo);
}
