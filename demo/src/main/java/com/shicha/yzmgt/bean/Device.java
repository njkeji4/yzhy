package com.shicha.yzmgt.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="device")
@Table(indexes={
			@Index(name="latestHeartBeat_Index",columnList="latestHeartBeat"), 
			@Index(name="todaySuccCount_Index",columnList="todaySuccCount"),
			@Index(name="totalSuccCount_Index",columnList="totalSuccCount"),
			@Index(name="todayAlarm_Index",columnList="todayAlarm"),
			@Index(name="totalAlarm_Index",columnList="totalAlarm")
		})
public class Device {

	@Id
	String deviceNo;
	
	Integer deviceType;
	
	String versionNo;
	
	String sysVer;
	String model;
	String firm;
	String ipAddr;
	String sysIccid;
	
	String name;
	Long latestHeartBeat;
	Long firstLogin;
	
	String groupId;
		
	String address;
	
	@Column(nullable=false, columnDefinition="INT default 0")
	Integer status = 0;
	
	@Column(nullable=false, columnDefinition="BIGINT default 0")
	Long totalSuccCount =0l;
	
	@Column(nullable=false, columnDefinition="BIGINT default 0")
	Long totalFailCount =0l;
	
	@Column(nullable=false, columnDefinition="BIGINT default 0")
	Long todaySuccCount =0l;
	
	@Column(nullable=false, columnDefinition="BIGINT default 0")
	Long todayFailCount =0l;
	
	@Column(nullable=false, columnDefinition="BIGINT default 0")
	Long totalAlarm = 0l;
	
	@Column(nullable=false, columnDefinition="BIGINT default 0")
	Long todayAlarm = 0l;

	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	
	public Long getTotalSuccCount() {
		return totalSuccCount;
	}

	public void setTotalSuccCount(Long totalSuccCount) {
		this.totalSuccCount = totalSuccCount;
	}

	public Long getTotalFailCount() {
		return totalFailCount;
	}

	public void setTotalFailCount(Long totalFailCount) {
		this.totalFailCount = totalFailCount;
	}

	public Long getTodaySuccCount() {
		return todaySuccCount;
	}

	public void setTodaySuccCount(Long todaySuccCount) {
		this.todaySuccCount = todaySuccCount;
	}

	public Long getTodayFailCount() {
		return todayFailCount;
	}

	public void setTodayFailCount(Long todayFailCount) {
		this.todayFailCount = todayFailCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Device() {}
	
	public void syncDevice(Device d) {
		this.deviceType = d.getDeviceType();
		this.versionNo = (d.getVersionNo() != null && d.getVersionNo().length() > 0) ? d.getVersionNo() : versionNo;
		this.sysVer = d.getSysVer() != null ? d.getSysVer() : sysVer;
		this.model = d.getModel() != null ? d.getModel() : model;
		this.firm = d.getFirm() != null ? d.getFirm() : firm;
		this.ipAddr = d.getIpAddr() != null ? d.getIpAddr() : ipAddr;
		this.sysIccid = d.getSysIccid() != null ? d.getSysIccid() : sysIccid;
		this.latestHeartBeat = d.getLatestHeartBeat() != null ? d.getLatestHeartBeat() : latestHeartBeat;
	}
	
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public Integer getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(Integer deviceType) {
		this.deviceType = deviceType;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	public String getSysVer() {
		return sysVer;
	}
	public void setSysVer(String sysVer) {
		this.sysVer = sysVer;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	
	public String getFirm() {
		return firm;
	}
	public void setFirm(String firm) {
		this.firm = firm;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public String getSysIccid() {
		return sysIccid;
	}
	public void setSysIccid(String sysIccid) {
		this.sysIccid = sysIccid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getLatestHeartBeat() {
		return latestHeartBeat;
	}
	public void setLatestHeartBeat(Long latestHeartBeat) {
		this.latestHeartBeat = latestHeartBeat;
	}

	public Long getFirstLogin() {
		return firstLogin;
	}

	public void setFirstLogin(Long firstLogin) {
		this.firstLogin = firstLogin;
	}

	public Long getTotalAlarm() {
		return totalAlarm;
	}

	public void setTotalAlarm(Long totalAlarm) {
		this.totalAlarm = totalAlarm;
	}

	public Long getTodayAlarm() {
		return todayAlarm;
	}

	public void setTodayAlarm(Long todayAlarm) {
		this.todayAlarm = todayAlarm;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
