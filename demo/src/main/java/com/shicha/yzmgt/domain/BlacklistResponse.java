package com.shicha.yzmgt.domain;

import java.util.ArrayList;
import java.util.List;

import com.shicha.yzmgt.bean.BlackList;

public class BlacklistResponse {

	int code;
	String message;
	
	List<Black>newList;
	List<Black>removedList;

	public BlacklistResponse() {}
	
	public BlacklistResponse(List<BlackList> list) {
		
		if(list == null || list.size() == 0) {
			code = 1;
			message = "no black list existed";
			return;
		}
		
		code = 0;
		
		for(BlackList b: list) {
			
			if(b.getStatus() == BlackList.STATUS_DELETED) {
				remove(b);
			}else {
				addNew(b);
			}
		}
	}
	
	private void addNew(BlackList b) {
		if(newList == null)
			newList = new ArrayList<Black>();
		
		newList.add(new Black(b));
	}
	private void remove(BlackList b) {
		if(removedList == null)
			removedList = new ArrayList<Black>();
		
		removedList.add(new Black(b));
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Black> getNewList() {
		return newList;
	}

	public void setNewList(List<Black> newList) {
		this.newList = newList;
	}

	public List<Black> getRemovedList() {
		return removedList;
	}

	public void setRemovedList(List<Black> removedList) {
		this.removedList = removedList;
	}
	
}

class Black{
	String cardNo;
	String name;
	int sex;
	
	public Black() {}
	
	public Black(BlackList b) {
		cardNo = b.getCardNo();
		name = b.getName();
		sex = b.getSex();
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

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}
	
	
}
