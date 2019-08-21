package com.shicha.yzmgt.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="version")
public class Version {

	@Id
	@Column(name="id", nullable=false, length=36)
	@GenericGenerator(name="system-uuid", strategy="uuid2")
	@GeneratedValue(generator="system-uuid")
	String id;
	
	String downloadAddr;
	Long updateDate;
	String versionNo;
	
	String oldVersion1;
	String oldVersion2;	
	
	public String getOldVersion1() {
		return oldVersion1;
	}
	public void setOldVersion1(String oldVersion1) {
		this.oldVersion1 = oldVersion1;
	}
	public String getOldVersion2() {
		return oldVersion2;
	}
	public void setOldVersion2(String oldVersion2) {
		this.oldVersion2 = oldVersion2;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDownloadAddr() {
		return downloadAddr;
	}
	public void setDownloadAddr(String downloadAddr) {
		this.downloadAddr = downloadAddr;
	}
	public Long getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Long updateDate) {
		this.updateDate = updateDate;
	}
	public String getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	
}
