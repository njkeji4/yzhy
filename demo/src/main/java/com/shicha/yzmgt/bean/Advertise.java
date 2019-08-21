package com.shicha.yzmgt.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="advertise")
@Table(indexes={
		@Index(name="create_time",columnList="createTime")
	})
public class Advertise {

	public static int status_new = 0;
	public static int status_del = 1;
	
	public static int advtype_image = 1;
	public static int advtype_video = 2;
	
	public static int requested_no = 0;
	public static int requested_yes = 1;
	
	@Id
	@Column(name="id", nullable=false, length=36)
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	String url;
	String title;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="adv_group", joinColumns=@JoinColumn(name="adv_id", referencedColumnName="id"), 
		inverseJoinColumns = @JoinColumn(name="group_id", referencedColumnName="group_id"))	
	List<DeviceGroup> groups;
	
	@Column(nullable=false, columnDefinition="INT default 0")
	int status = 0;
	
	int advType;
	
	@Column(nullable=false, columnDefinition="INT default 0")
	int requested = 0;
	
	Long createTime;
	Long updateTime;
	Long expireDate;
	
	Long reqTime;

	String createUser;
	
	public Advertise() {
	}
	
	public int getAdvType() {
		return advType;
	}

	public void setAdvType(int advType) {
		this.advType = advType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getReqTime() {
		return reqTime;
	}

	public void setReqTime(Long reqTime) {
		this.reqTime = reqTime;
	}

	public int getRequested() {
		return requested;
	}

	public void setRequested(int requested) {
		this.requested = requested;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<DeviceGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<DeviceGroup> groups) {
		this.groups = groups;
	}
	
	public void addGroup(DeviceGroup group) {
		if(groups == null)
			groups = new ArrayList<DeviceGroup>();
		
		groups.add(group);
	}

	public Long getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Long expireDate) {
		this.expireDate = expireDate;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	
}
