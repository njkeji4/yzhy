package com.shicha.yzmgt.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity(name="user")
public class User {
	
	public static String ROLE_ADMIN="ROLE_ADMIN";
	public static String ROLE_NORMAL="ROLE_NORMAL";
	public static String ROLE_="ROLE_AD";

	public static int USER_STATUS_OK = 0;	
	public static int USER_STATUS_LOCK = 1;
	public static int USER_STATUS_ERRORPWD = 2;
	
	public static int USER_STATUS_NOTEXISTED = 100;
	public static int USER_STATUS_UNKNOWN = 101;
	
	
	public static int USER_MAX_ERROR_TIMES = 5;
	
	
	@Id
	@Column(name="id", nullable=false, length=36)
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	String name;
	String password;	
	String role;
	
	String phone;
	
	@Column(nullable=false, columnDefinition="INT default 0")
	int status = 0;
	
	@Column(nullable=false, columnDefinition="INT default 0")
	int errorTimes = 0;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="group_user", joinColumns=@JoinColumn(name="user_id", referencedColumnName="id"), inverseJoinColumns = @JoinColumn(name="group_id", referencedColumnName="group_id"))	
	List<DeviceGroup> groups;
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public void addGroup(DeviceGroup group) {
		if(groups == null) {
			groups = new ArrayList<DeviceGroup>();
		}
		groups.add(group);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<DeviceGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<DeviceGroup> groups) {
		this.groups = groups;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getErrorTimes() {
		return errorTimes;
	}

	public void setErrorTimes(int errorTimes) {
		this.errorTimes = errorTimes;
	}
	
}
