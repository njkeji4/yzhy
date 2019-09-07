package com.shicha.yzmgt.bean;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;

import org.hibernate.annotations.GenericGenerator;

import com.shicha.yzmgt.util.Util;

@Entity(name="black_list")
@IdClass(BlackListId.class)
public class BlackList {
	
	public static int STATUS_DELETED = 1;
	
	@Id
	String cardNo;
	
	@Id
	String groupId;
	
	String name;
	Integer sex;
	
	String folk;
	String birthday;
	String address;
	
	Long createTime;
	
	@Column(nullable=false, columnDefinition="INT default 0")
	Integer status = 0;
	
	String userName;
	String groupName;
	
	public BlackList() {
	}
	
	public static String title() {
		return "姓名,卡号,性别,时间,状态,所属组\r\n";
	}
	@Override
	public String toString() {
		return name + ",#" + cardNo + "," + (sex == 1?"男" : "女") + "," + (createTime == null? "":Util.formatDate(createTime)) + 
				"," + (status == 0? "创建":"删除") + ","+ groupName + "\r\n";
	}
	
	public BlackList(String cardNo, String name, int sex) {
		this.cardNo = cardNo;
		this.name = name;
		this.sex = sex;
	}
	
	public BlackList(String cardNo, String name, int sex, String userName, String groupId, String groupName) {
		this.cardNo = cardNo;
		this.name = name;
		this.sex = sex;
		this.userName = userName;
		this.createTime = System.currentTimeMillis();
		
		this.groupId = groupId;
		this.groupName = groupName;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}	

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
}
