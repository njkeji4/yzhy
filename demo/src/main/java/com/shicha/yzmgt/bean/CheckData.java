package com.shicha.yzmgt.bean;

import java.util.HashMap;

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

import com.shicha.yzmgt.domain.SearchCheckData;
import com.shicha.yzmgt.util.Util;

@Entity(name="check_data")
public class CheckData {

	@Id
	@Column(name="id", nullable=false, length=36)
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	String deviceName;
	
	String deviceNo;
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
	
	String groupId;
	
	
	String roomNumber;
	Long checkInDate;
	Long checkOutDate;
	
	public CheckData() {}
	
	
	private static HashMap<String,String>nativeMap = new HashMap<String,String>(){{
		put("compareDate","compare_date");
		put("deviceName","device_name");
		put("deviceNo","device_no");
		put("name","name");
		put("result","result");
		put("cardNo","card_no");
	}};
	
	public static String toNative(String f) {
		if(nativeMap.containsKey(f))
			return nativeMap.get(f);
		return f;
	}
	
	public CheckData(SearchCheckData data) {
		this.deviceName = data.getDeviceName();
		this.deviceNo = data.getDeviceNo();
		this.name = data.getName();
		this.cardNo = data.getCardNo();
		this.result = data.getResult();
		
		this.folk = data.getFolk();
		this.birthday = data.getBirthday();
		this.address = data.getAddress();
		this.authority = data.getAuthority();
		this.compareDate = data.getCompareDate();
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
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
	
	
	
	public String getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(String roomNumber) {
		this.roomNumber = roomNumber;
	}

	public Long getCheckInDate() {
		return checkInDate;
	}

	public void setCheckInDate(Long checkInDate) {
		this.checkInDate = checkInDate;
	}

	public Long getCheckOutDate() {
		return checkOutDate;
	}

	public void setCheckOutDate(Long checkOutDate) {
		this.checkOutDate = checkOutDate;
	}

	public static String Title() {
		return "设备名称,设备编号,姓名,身份证,性别,比对结果,比对日期\r\n";
	}
	@Override
	public String toString() {
		
		return deviceName + ",#" + deviceNo + "," + 
				name + ",#" + cardNo + "," + (sex==1?"男":"女") + "," 
				+ (result == 1?"通过":"不通过") + "," + Util.formatDate(compareDate)+"\r\n";
		
	}
	
}
