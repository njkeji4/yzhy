package com.shicha.yzmgt.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;

import com.shicha.yzmgt.util.Util;

@Entity(name="black_list")
public class BlackList {
	
	public static int STATUS_DELETED = 1;
	
	@Id
	String cardNo;	
	String name;
	
	String folk;
	Integer sex;
	String birthday;
	String address;
	
	Long createTime;
	Long deleteTime;
	
	@Column(nullable=false, columnDefinition="INT default 0")
	Integer deleted = 0;
	
	String userName;
	
	public BlackList() {
	}
	
	public static String title() {
		return "姓名,卡号,性别,创建时间,删除时间\r\n";
	}
	@Override
	public String toString() {
		return name + ",#" + cardNo + "," + (sex == 1?"男" : "女") + "," + (createTime == null? "":Util.formatDate(createTime)) + 
				"," + (deleteTime == null? "":Util.formatDate(deleteTime)) + "\r\n";
	}
	
	public BlackList(String cardNo, String name, int sex) {
		this.cardNo = cardNo;
		this.name = name;
		this.sex = sex;
	}
	
	public BlackList(String cardNo, String name, int sex, String userName) {
		this.cardNo = cardNo;
		this.name = name;
		this.sex = sex;
		this.userName = userName;
		this.createTime = System.currentTimeMillis();
	}
	
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Long deleteTime) {
		this.deleteTime = deleteTime;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
