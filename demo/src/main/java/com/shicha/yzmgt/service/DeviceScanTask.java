package com.shicha.yzmgt.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceStat;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IDeviceStatic;
import com.shicha.yzmgt.util.Util;

@Component
public class DeviceScanTask {
	
	private static final Logger log = LoggerFactory.getLogger(DeviceScanTask.class);
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IDeviceStatic statDao;
	
	@Value("${device.heartbeat:10}")
	int heartBeat;
	
	@Scheduled(fixedRate = 1000 * 60 * 5)
	public void scanDevice() {		
		try {
			List<Device> list = deviceDao.findByStatusIs(0);//deviceDao.findAll();
			if(list == null || list.size() == 0) {
				return;
			}
			
			long current = System.currentTimeMillis();
			for(Device d : list) {
				long interval = current - d.getLatestHeartBeat();
				
				if(interval >= heartBeat * 60 * 1000) {
					d.setStatus(1);
					deviceDao.save(d);
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	//every day 
	@Scheduled(cron = "0 0 0 * * ?")
	public void resetTodayStatitics() {
		try {			
			List<Device> devices = deviceDao.findAll();
			
			Long day = Util.getPreviousDay(System.currentTimeMillis());
			Long week = Util.getWeek(day);
			Long month = Util.getMonth(day);
			
			for(Device device : devices) {
				if(device.getTodaySuccCount() == 0 && device.getTodayFailCount() == 0)
					continue;
				
				DeviceStat statDay = statDao.findFirstByStatDateAndDeviceNoAndStatType(day, device.getDeviceNo(), DeviceStat.stat_type_day);
				DeviceStat statWeek = statDao.findFirstByStatDateAndDeviceNoAndStatType(week, device.getDeviceNo(),DeviceStat.stat_type_week);
				DeviceStat statMonth = statDao.findFirstByStatDateAndDeviceNoAndStatType(month, device.getDeviceNo(),DeviceStat.stat_type_month);
				if(statDay == null) {
					statDay = new DeviceStat();
					statDay.setStatDate(day);
					statDay.setStatType(DeviceStat.stat_type_day);
					statDay.setDeviceName(device.getName());
					statDay.setDeviceNo(device.getDeviceNo());
				}
				
				if(statWeek == null) {
					statWeek = new DeviceStat();
					statWeek.setStatDate(week);
					statWeek.setStatType(DeviceStat.stat_type_week);
					statWeek.setDeviceName(device.getName());
					statWeek.setDeviceNo(device.getDeviceNo());
				}
				
				if(statMonth == null) {
					statMonth = new DeviceStat();
					statMonth.setStatDate(month);
					statMonth.setStatType(DeviceStat.stat_type_month);
					statMonth.setDeviceName(device.getName());
					statMonth.setDeviceNo(device.getDeviceNo());
				}
				
				statDay.setSuccessCountByDay(statDay.getSuccessCountByDay() + device.getTodaySuccCount());
				statDay.setFailCountByDay(statDay.getFailCountByDay()+ device.getTodayFailCount());
				
				
				statDao.save(statDay);
				
				statWeek.setSuccessCountByWeek(statWeek.getSuccessCountByWeek() +  device.getTodaySuccCount());
				statWeek.setFailCountByWeek(statWeek.getFailCountByWeek() + device.getTodayFailCount());
				
				statDao.save(statWeek);
				
				statMonth.setSuccessCountByMonth(statMonth.getSuccessCountByMonth() + device.getTodaySuccCount());
				statMonth.setFailCountByMonth(statMonth.getFailCountByMonth() + device.getTodayFailCount());
				
				statDao.save(statMonth);
			}
			
			deviceDao.clearTodayStatistics();	
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
}
