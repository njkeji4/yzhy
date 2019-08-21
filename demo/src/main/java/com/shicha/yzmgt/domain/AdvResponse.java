package com.shicha.yzmgt.domain;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.shicha.yzmgt.bean.Advertise;

public class AdvResponse {

	public static String code_ok = "00000000";
	public static String code_fail = "00000001";
	
	String code;
	String message;
	String reserveInfo;
	String salt;
	Long timestamp;
	String signature;	
	
	String data;

	
	public AdvResponse() {}
	
	public AdvResponse(List<Advertise>ads, String keyValue) {
		code = code_ok;
		timestamp = System.currentTimeMillis();
		Data d = new Data(ads);
		data = JSON.toJSONString(d);
		try {
			byte[] byteSalt = new byte[12];
			SecureRandom.getInstance("SHA1PRNG").nextBytes(byteSalt);
			salt = Base64.getEncoder().encodeToString(byteSalt);
			
			byte[] sig = getSignature(data, byteSalt, String.valueOf(timestamp).getBytes(), keyValue.getBytes());
			signature = Base64.getEncoder().encodeToString(sig);
			
		}catch(Exception ex) {
			code = code_fail;
			message = ex.getMessage();
		}
	}
	
	private byte[] getSignature(String data, byte[]salt, byte[] timestamp, byte[] key) {
		
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("SHA-256");
			byte[] textBytes = data.getBytes(Charset.forName("UTF-8"));
			
			md.update(salt);
			md.update(timestamp);
			md.update(textBytes);
			md.update(key);
			
			return md.digest();
			
		} catch (NoSuchAlgorithmException e) {
			
			e.printStackTrace();
			
			return null;
		}
		
	}
	
	public AdvResponse(String message) {
		code = code_fail;
		this.message = message;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getReserveInfo() {
		return reserveInfo;
	}
	public void setReserveInfo(String reserveInfo) {
		this.reserveInfo = reserveInfo;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
}

class Data {
	List<Pic_ad> pic_ad;
	List<Video_ad>video_ad;
	
	public Data() {};
	
	public Data(List<Advertise> ads) {
		for(Advertise ad : ads) {
			if(ad.getAdvType() == Advertise.advtype_image) {
				addPicAd(new Pic_ad(ad));
			}
			if(ad.getAdvType() == Advertise.advtype_video) {
				addVideoAd(new Video_ad(ad));
			}
		}	
	}
	
	public void addPicAd(Pic_ad ad) {
		if(pic_ad == null)
			pic_ad = new ArrayList<Pic_ad>();
			
		pic_ad.add(ad);
	}
	public void addVideoAd(Video_ad ad) {
		if(video_ad == null)
			video_ad = new ArrayList<Video_ad>();
			
		video_ad.add(ad);
	}
	public void addPicAd(List<Pic_ad> ad) {
		if(pic_ad == null)
			pic_ad = new ArrayList<Pic_ad>();
			
		pic_ad.addAll(ad);
	}
	public void addVideoAd(List<Video_ad> ad) {
		if(video_ad == null)
			video_ad = new ArrayList<Video_ad>();
			
		video_ad.addAll(ad);
	}

	public List<Pic_ad> getPic_ad() {
		return pic_ad;
	}

	public void setPic_ad(List<Pic_ad> pic_ad) {
		this.pic_ad = pic_ad;
	}

	public List<Video_ad> getVideo_ad() {
		return video_ad;
	}

	public void setVideo_ad(List<Video_ad> video_ad) {
		this.video_ad = video_ad;
	}
	
}

class Pic_ad{
	
	String pic_id;
	String pic_url;
	Long create_time;
	Long expire_time;
	int status;
	
	public Pic_ad() {
	}
	
	public Pic_ad(Advertise ad) {
		this.pic_id = ad.getId();
		pic_url = ad.getUrl();
		status = ad.getStatus();
		create_time = ad.getUpdateTime();
		expire_time=ad.getExpireDate();
	}
	
	public Pic_ad(String id, String url, int status) {
		pic_id = id;
		pic_url = url;
		this.status = status;
	}

	public String getPic_id() {
		return pic_id;
	}

	public void setPic_id(String pic_id) {
		this.pic_id = pic_id;
	}

	public String getPic_url() {
		return pic_url;
	}

	public void setPic_url(String pic_url) {
		this.pic_url = pic_url;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public Long getExpire_time() {
		return expire_time;
	}

	public void setExpire_time(Long expire_time) {
		this.expire_time = expire_time;
	}
	
}

class Video_ad{
	String video_id;
	String video_url;
	int status;
	Long create_time;
	Long expire_time;
	
	public Video_ad() {
		
	}
	public Video_ad(Advertise ad) {
		video_id = ad.getId();
		video_url=ad.getUrl();
		status=ad.getStatus();
		create_time = ad.getUpdateTime();
		expire_time=ad.getExpireDate();
	}
	
	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	
	public Video_ad(String id, String url, int status) {
		video_id = id;
		video_url = url;
		this.status = status;
	}
	public String getVideo_id() {
		return video_id;
	}
	public void setVideo_id(String video_id) {
		this.video_id = video_id;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Long getExpire_time() {
		return expire_time;
	}
	public void setExpire_time(Long expire_time) {
		this.expire_time = expire_time;
	}
	
}
