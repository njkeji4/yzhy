package com.shicha.yzmgt.bean;

import java.io.Serializable;
import java.util.Objects;

public class BlackListId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	String cardNo;
	String groupId;
	
	public BlackListId() {}
	
	public BlackListId(String cardNo, String groupId) {
		this.cardNo= cardNo;
		this.groupId=groupId;
	}
	
	@Override  
    public int hashCode() {  
		return Objects.hash(cardNo, groupId);
    }  
	
	@Override  
    public boolean equals(Object obj) {  
        if(this == obj) return true;  
        if(obj == null) return false;  
        if(!(obj instanceof BlackListId)) return false;  
        
        BlackListId objKey = (BlackListId)obj;
        return objKey.getCardNo().equals(cardNo) && objKey.getGroupId().equals(groupId);
    }

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}  
}
