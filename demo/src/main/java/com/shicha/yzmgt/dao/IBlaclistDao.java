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
	
	public BlackList findFirstByCardNoAndStatus(String cardNo, int status);
	
	
	List<BlackList> findAllByGroupIdAndCreateTimeGreaterThanEqual(String groupId, Long create);
	
	List<BlackList> findAllByGroupIdAndCreateTimeGreaterThanEqualAndStatus(String groupId, Long time, int status);
	
	
	@Transactional
	@Modifying
	@Query(value="update black_list set status = 1, user_name=:userName, create_time=:deleteTime where card_no=:cardNo and group_id=:groupId",nativeQuery=true)
	public void deletebyCardNoAndGroupId(@Param("cardNo") String cardNo, @Param("groupId") String groupId,
			@Param("deleteTime") Long deleteTime,@Param("userName") String userName);
}
