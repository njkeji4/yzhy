package com.shicha.yzmgt.domain;

import java.util.ArrayList;
import java.util.List;

import com.shicha.yzmgt.bean.CheckData;
import com.shicha.yzmgt.bean.Device;

public class TodayData {

	long successCount;
	long failCount;
	
	long online;
	long offline;
	
	long alarmCount;	
	
	long totalSuccess;
	long totalFail;
	
	long totalAlarmCount;
	
	List<Device>devices;
	List<StatGroup>deviceGroups;
	
	public TodayData() {}
	
	public TodayData(long succ) {
		this.successCount = succ;
	}
	
	public TodayData(long succ, long fail, long totalSucc, long totalFail, long online, long offline, long todayAlarm, long totalAlarm) {
		this.successCount = succ;
		this.failCount = fail;
		this.online = online;
		this.offline = offline;
		this.totalSuccess = totalSucc;
		this.totalFail = totalFail;
		
		this.alarmCount = todayAlarm;
		this.totalAlarmCount = totalAlarm;
	}
	
	
	public long getSuccessCount() {
		return successCount;
	}
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	public long getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public long getOnline() {
		return online;
	}
	public void setOnline(int online) {
		this.online = online;
	}
	public long getOffline() {
		return offline;
	}
	public void setOffline(int offline) {
		this.offline = offline;
	}
	public long getAlarmCount() {
		return alarmCount;
	}
	public void setAlarmCount(int alarmCount) {
		this.alarmCount = alarmCount;
	}
	public long getTotalSuccess() {
		return totalSuccess;
	}
	public void setTotalSuccess(int totalSuccess) {
		this.totalSuccess = totalSuccess;
	}
	public long getTotalFail() {
		return totalFail;
	}
	public void setTotalFail(int totalFail) {
		this.totalFail = totalFail;
	}
	
	public long getTotalAlarmCount() {
		return totalAlarmCount;
	}
	public void setTotalAlarmCount(int totalAlarmCount) {
		this.totalAlarmCount = totalAlarmCount;
	}

	public List<StatGroup> getDeviceGroups() {
		return deviceGroups;
	}

	public void setDeviceGroups(List<StatGroup> deviceGroups) {
		this.deviceGroups = deviceGroups;
	}
	

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
	
}

