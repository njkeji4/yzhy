package com.shicha.yzmgt.bean;

import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;

@Entity(name="spot_img")
public class SpotImg {

	@Id
	@Column(name="id", nullable=false, length=36)	
	String id;
	
	@Lob @Basic(fetch=FetchType.LAZY)
	byte[] spotImg;

	
	public SpotImg() {}
	
	public SpotImg(byte[] spotImg) {
		
		this.spotImg = Arrays.copyOf(spotImg, spotImg.length);		
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getSpotImg() {
		return spotImg;
	}

	public void setSpotImg(byte[] spotImg) {
		this.spotImg = spotImg;
	}
	
}
