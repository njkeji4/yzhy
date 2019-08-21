package com.shicha.yzmgt.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shicha.yzmgt.bean.Setting;
import com.shicha.yzmgt.bean.SpotImg;

public interface ISpotImgDao  extends JpaRepository<SpotImg, String>{

}
