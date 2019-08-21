package com.shicha.yzmgt.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shicha.yzmgt.bean.BlackList;
import com.shicha.yzmgt.bean.CheckData;

public interface IBlaclistDao extends JpaRepository<BlackList, String>{

	public BlackList findByCardNo(String cardNo); 
	
	List<BlackList> findByUserName(String userName);
	
	public BlackList findByCardNoAndDeleted(String cardNo, int deleted);
	
	
	@Transactional
	@Modifying
	@Query(value="update black_list set deleted = 1, userName=:userName where card_no=:cardNo",nativeQuery=true)
	public void deletebyCardNo(@Param("cardNo") String cardNo, @Param("userName") String userName);
}
