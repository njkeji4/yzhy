package com.shicha.yzmgt.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.shicha.yzmgt.domain.SearchAlarmData;
import com.shicha.yzmgt.util.Util;

@Entity(name="alarm_data")
@Table(indexes={
		@Index(name="compare_time",columnList="compareDate")
	})
public class AlarmData {

	@Id
	@Column(name="id", nullable=false, length=36)
	//@GenericGenerator(name="system-uuid", strategy="uuid2")
	//@GeneratedValue(generator="system-uuid")
	String id;
	
	String deviceName;
	String deviceNo;
	String groupId;
	
	Integer alarmType;		//1 black list, 2 too many compare error times
	Long alarmTime;
	
	String name;
	String cardNo;	
	
	@Lob @Basic(fetch=FetchType.LAZY)
	byte[] cardImg;
	
	@Lob @Basic(fetch=FetchType.LAZY)
	byte[] spotImg;
	
	Double score;
	Double threshold;
	Integer result;			//1 ok,2 fail, 3 undetected face 4 undetect fingerprint
	String folk;
	Integer sex;
	String birthday;
	String address;
	String validdate1; //card start date
	String validdate2;
	String authority;
	Long compareDate;
	
	public AlarmData() {}
	
	public AlarmData(SearchAlarmData data) {
		deviceName = data.getDeviceName();
		deviceNo = data.getDeviceNo();;
		name = data.getName();
		cardNo = data.getCardNo();
		
		alarmType = data.getAlarmType();
		alarmTime = data.getAlarmTime();
	}
	
	public AlarmData(CheckData data) {
		deviceName = data.getDeviceName();
		deviceNo = data.getDeviceNo();;
		name = data.getName();
		cardNo = data.getCardNo();
		cardImg = data.getCardImg();
		spotImg = data.getSpotImg();
		score = data.getScore();
		threshold = data.getThreshold();
		result = data.getResult();
		folk = data.getFolk();
		sex = data.getSex();
		birthday = data.getBirthday();
		address = data.getAddress();
		validdate1 = data.getValiddate1();
		validdate2 = data.getValiddate2();
		authority = data.getAuthority();
		compareDate = data.getCompareDate();
		groupId=data.getGroupId();
		id = data.getId();
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public Integer getAlarmType() {
		return alarmType;
	}
	public void setAlarmType(Integer alarmType) {
		this.alarmType = alarmType;
	}
	public Long getAlarmTime() {
		return alarmTime;
	}
	public void setAlarmTime(Long alarmTime) {
		this.alarmTime = alarmTime;
	}
	public String getDeviceNo() {
		return deviceNo;
	}
	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public byte[] getCardImg() {
		return cardImg;
	}
	public void setCardImg(byte[] cardImg) {
		this.cardImg = cardImg;
	}
	public byte[] getSpotImg() {
		return spotImg;
	}
	public void setSpotImg(byte[] spotImg) {
		this.spotImg = spotImg;
	}
	public Double getScore() {
		return score;
	}
	public void setScore(Double score) {
		this.score = score;
	}
	public Double getThreshold() {
		return threshold;
	}
	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}
	public Integer getResult() {
		return result;
	}
	public void setResult(Integer result) {
		this.result = result;
	}
	public String getFolk() {
		return folk;
	}
	public void setFolk(String folk) {
		this.folk = folk;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getValiddate1() {
		return validdate1;
	}
	public void setValiddate1(String validdate1) {
		this.validdate1 = validdate1;
	}
	public String getValiddate2() {
		return validdate2;
	}
	public void setValiddate2(String validdate2) {
		this.validdate2 = validdate2;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	public Long getCompareDate() {
		return compareDate;
	}
	public void setCompareDate(Long compareDate) {
		this.compareDate = compareDate;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
	public static String Title() {
		return "设备名称,设备编号,姓名,身份证,性别,比对结果,告警日期\r\n";
	}
	@Override
	public String toString() {
		
		return deviceName + ",#" + deviceNo + "," + 
				name + ",#" + cardNo + "," + (sex==1?"男":"女") + "," 
				+ (result == 1?"通过":"不通过") + "," + Util.formatDate(alarmTime)+"\r\n";
		
	}
}
