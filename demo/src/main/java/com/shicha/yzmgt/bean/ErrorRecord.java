package com.shicha.yzmgt.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="error_record")
public class ErrorRecord {

	@Id
	String cardNo;
	
	Integer count;
	
	public ErrorRecord() {}
	
	public ErrorRecord(String cardNo, Integer count) {
		this.cardNo = cardNo;
		this.count = count;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
