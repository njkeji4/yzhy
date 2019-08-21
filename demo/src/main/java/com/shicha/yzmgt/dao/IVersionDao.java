package com.shicha.yzmgt.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.bean.Version;

public interface IVersionDao extends JpaRepository<Version, String>{

}
