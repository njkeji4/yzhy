package com.shicha.yzmgt.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="device_stat")
public class DeviceStat {
	
	public static int stat_type_day = 1;
	public static int stat_type_week = 2;
	public static int stat_type_month = 3;
	
	@Id
	@Column(name="id", nullable=false, length=36)
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	String deviceNo;	
	String deviceName;
	
	int statType = 1;
	
	public int getStatType() {
		return statType;
	}
	public void setStatType(int statType) {
		this.statType = statType;
	}
	Long statDate;
	
	Long successCountByDay = 0l;
	Long failCountByDay = 0l;
	
	Long successCountByWeek = 0l;
	Long failCountByWeek = 0l;
	
	Long successCountByMonth = 0l;
	Long failCountByMonth = 0l;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Long getStatDate() {
		return statDate;
	}
	public void setStatDate(Long statDate) {
		this.statDate = statDate;
	}
	public Long getSuccessCountByDay() {
		return successCountByDay;
	}
	public void setSuccessCountByDay(Long successCountByDay) {
		this.successCountByDay = successCountByDay;
	}
	public Long getFailCountByDay() {
		return failCountByDay;
	}
	public void setFailCountByDay(Long failCountByDay) {
		this.failCountByDay = failCountByDay;
	}
	public Long getSuccessCountByWeek() {
		return successCountByWeek;
	}
	public void setSuccessCountByWeek(Long successCountByWeek) {
		this.successCountByWeek = successCountByWeek;
	}
	public Long getFailCountByWeek() {
		return failCountByWeek;
	}
	public void setFailCountByWeek(Long failCountByWeek) {
		this.failCountByWeek = failCountByWeek;
	}
	public Long getSuccessCountByMonth() {
		return successCountByMonth;
	}
	public void setSuccessCountByMonth(Long successCountByMonth) {
		this.successCountByMonth = successCountByMonth;
	}
	public Long getFailCountByMonth() {
		return failCountByMonth;
	}
	public void setFailCountByMonth(Long failCountByMonth) {
		this.failCountByMonth = failCountByMonth;
	}
}
