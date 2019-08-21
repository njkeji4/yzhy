package com.shicha.yzmgt.dao;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import com.shicha.yzmgt.bean.ErrorRecord;

public interface IErroRecord extends JpaRepository<ErrorRecord, String>{

	public ErrorRecord findByCardNo(String cardNo);
	
	@Transactional
	@Modifying
	@Query(value="delete from error_record where card_no=:cardNo",nativeQuery=true)
	public void removeErrorRecord(@Param("cardNo") String cardNo);
}
