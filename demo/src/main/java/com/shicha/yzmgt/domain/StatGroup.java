package com.shicha.yzmgt.domain;

public class StatGroup {

	String name;
	long succ;
	long fail;
	long alarm;
	
	public StatGroup() {}
	
	public StatGroup(String name, long succ, long fail, long alarm) {
		this.name = name;
		this.succ=succ;
		this.fail=fail;
		this.alarm=alarm;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getSucc() {
		return succ;
	}
	public void setSucc(long succ) {
		this.succ = succ;
	}
	public long getFail() {
		return fail;
	}
	public void setFail(long fail) {
		this.fail = fail;
	}
	public long getAlarm() {
		return alarm;
	}
	public void setAlarm(long alarm) {
		this.alarm = alarm;
	}
	
	
}
