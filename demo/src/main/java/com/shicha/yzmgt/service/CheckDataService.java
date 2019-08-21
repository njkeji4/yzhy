package com.shicha.yzmgt.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.shicha.yzmgt.bean.AlarmData;
import com.shicha.yzmgt.bean.BlackList;
import com.shicha.yzmgt.bean.CheckData;
import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.DeviceGroup;
import com.shicha.yzmgt.bean.ErrorRecord;
import com.shicha.yzmgt.bean.Setting;
import com.shicha.yzmgt.bean.SpotImg;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.dao.IAlarmDataDao;
import com.shicha.yzmgt.dao.IBlaclistDao;
import com.shicha.yzmgt.dao.ICheckData;
import com.shicha.yzmgt.dao.IDeviceDao;
import com.shicha.yzmgt.dao.IErroRecord;

import com.shicha.yzmgt.dao.ISettingDao;
import com.shicha.yzmgt.dao.ISpotImgDao;
import com.shicha.yzmgt.dao.IUserDao;
import com.shicha.yzmgt.domain.SearchAlarmData;
import com.shicha.yzmgt.domain.SearchCheckData;
import com.shicha.yzmgt.domain.SearchDevice;
import com.shicha.yzmgt.domain.TodayData;
import com.shicha.yzmgt.util.Util;

import net.coobird.thumbnailator.Thumbnails;

@Service
public class CheckDataService {

	private static final Logger log = LoggerFactory.getLogger(CheckDataService.class);
	
	@Autowired
	ICheckData checkDataDao;
	
	@Autowired
	ISpotImgDao spotImgDao;
	
	@Autowired
	IDeviceDao deviceDao;
	
	@Autowired
	IAlarmDataDao alarmDao;
	
	@Autowired
	IBlaclistDao blacklistDao;
	
	@Autowired
	IErroRecord errorDao;
	
	@Autowired
	IUserDao userDao;
	
	@Autowired
	SettingService settingService;
	
	@Autowired
	private SimpMessagingTemplate webSocket;
	
	@PersistenceContext
    private EntityManager em;
	
	//these devices should only have one compare records in one day
//	@Value("${devices}")
//	List<String>devices;
	
	/**this function is here because a special requirement:
	 * some devices(in the deivces list above),should only use once for one person a day.
	 * if a person use the device 2 or more times, send alarm
	*/
//	public boolean checkRepeatCompare(CheckData data) {
//		if(data.getResult() != 1)return false;
//		
//		boolean inlist=false;
//		log.info("no-repeat list length="+devices.size());
//		for(String d : devices) {			
//			if(d.equals(data.getDeviceNo())) {
//				inlist=true;
//				break;
//			}
//		}
//		
//		if(!inlist) {
//			log.info("not in no-repeat list");
//			return false;
//		}
//		
//		log.info("device in the no-repeat list:"+data.getDeviceNo()+ " " + Util.begin()+" and " + (data.getCompareDate()-5*60*1000));
//		
//		CheckData op=checkDataDao.findFirstByCompareDateBetweenAndDeviceNo(Util.begin(), data.getCompareDate()-5*60*1000, data.getDeviceNo());
//		if(op == null)
//		{
//			
//			log.info("there is no repeat records");
//			return false;
//		}
//		
//		AlarmData alarmData = new AlarmData(data);
//		alarmData.setAlarmTime(System.currentTimeMillis());
//		alarmData.setAlarmType(3);//repeat compare
//		
//		alarmDao.save(alarmData);
//		
//		sendAlarm(data);
//		
//		return true;
//		
//	}
	
	public void saveCmpRecord(CheckData data) {
		
		//repeat records
		CheckData old = checkDataDao.findFirstByCompareDateAndCardNo(data.getCompareDate(), data.getCardNo());
		if(old != null) {
			log.info("repeat record:"+data.getCardNo() + " : " + data.getCompareDate());
			return;
		}
		
		//check if device existed
		Optional<Device> optional = deviceDao.findByDeviceNo(data.getDeviceNo());
		if(!optional.isPresent()) {
			log.info("device is not registered yet:" + data.getCardNo());
			return ;
		}
		
		Device tmpDevice = optional.get();
		
		/////////////
		data.setDeviceName(tmpDevice.getName());
		data.setGroupId(tmpDevice.getGroupId());
		
		SpotImg spotImg = null;
		if(data.getSpotImg() != null) {
			spotImg = new SpotImg(data.getSpotImg());		
			convert(data);			
		}
		
		CheckData newdata = checkDataDao.save(data);
		
		if(spotImg != null) {
			spotImg.setId(newdata.getId());	
			spotImgDao.save(spotImg);
		}
		
		//statistics
		if(data.getResult() == 1) {
			tmpDevice.setTotalSuccCount(tmpDevice.getTotalSuccCount() +1);
			if(data.getCompareDate() >= Util.begin() && data.getCompareDate() <= Util.end())
				tmpDevice.setTodaySuccCount(tmpDevice.getTodaySuccCount() + 1);
		}else {
			tmpDevice.setTotalFailCount(tmpDevice.getTotalFailCount()+1);
			if(data.getCompareDate() >= Util.begin() && data.getCompareDate() <= Util.end())
				tmpDevice.setTodayFailCount(tmpDevice.getTodayFailCount()+1);
		}
		
		data.setId(newdata.getId());
		
		//for some device, if there are 2 compare records in one day,send alarm 
		//boolean isrepeat= checkRepeatCompare(data);
		
		if(checkAlarm(data)) {//is alarm data
			
			tmpDevice.setTotalAlarm(tmpDevice.getTotalAlarm() + 1);
			if(data.getCompareDate() >= Util.begin() && data.getCompareDate() <= Util.end())
				tmpDevice.setTodayAlarm(tmpDevice.getTodayAlarm() + 1);
		}
		
		deviceDao.save(tmpDevice);
	}	
	
	public SpotImg getSpotImg(String id) {
		
		Optional<SpotImg> optional =  spotImgDao.findById(id);
		if(optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	public void convert(CheckData data) {
		try {
			
			byte[] bytes=data.getSpotImg();	
			if(bytes == null || bytes.length == 0) {
				return;
			}
			ByteArrayOutputStream os=new ByteArrayOutputStream();
			Thumbnails.of(new ByteArrayInputStream(bytes)).scale(0.1).toOutputStream(os);			
			data.setSpotImg(os.toByteArray());
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public boolean checkAlarm(CheckData data) {		
		
		String cardNo = data.getCardNo();
		BlackList bl = blacklistDao.findByCardNoAndDeleted(cardNo, 0);		
		if(bl != null) {	//the user is in blacklist
			AlarmData alarmData = new AlarmData(data);
			alarmData.setAlarmTime(System.currentTimeMillis());
			alarmData.setAlarmType(1);
			
			alarmDao.save(alarmData);
			
			sendAlarm(data);
			
			return true;
		}
		
		if(data.getResult() == 1) {	//compare success			
			errorDao.removeErrorRecord(data.getCardNo());			
			return false;
		}
		
		//compare failed, check if add to alarm
		ErrorRecord records = errorDao.findByCardNo(data.getCardNo());
		if(records == null) {
			records = new ErrorRecord(data.getCardNo(), 0);
		}
		records.setCount(records.getCount()+1);
		errorDao.save(records);
		
		Setting setting = settingService.getSetting();
		if(setting == null) {			
			return false;
		}
		
		if(records.getCount() >= setting.getErrorTimes()) {
			AlarmData alarmData = new AlarmData(data);
			alarmData.setAlarmTime(System.currentTimeMillis());
			alarmData.setAlarmType(2);
			
			alarmDao.save(alarmData);
			
			sendAlarm(data);
			
			return true;
		}
		return false;
	}
	
	public void sendAlarm(CheckData data) {
		//get user for the data
		List<User>user = userDao.findByRole(User.ROLE_ADMIN);
		if(data.getGroupId() != null) {
			DeviceGroup g = new DeviceGroup();
			g.setGroupId(data.getGroupId());
			List<User>user2 = userDao.findByGroups(g);
			if(user2 != null && user.size() > 0)
				user.addAll(user2);
		}
		
		if(user == null || user.size() == 0) {
			log.info("no need to send alarm");
			return;
		}
		
		for(User u: user) {
			String target = "/topic/greetings/" + u.getName();
			webSocket.convertAndSend(target, "alarm");
		}
		
		//webSocket.convertAndSend("/topic/greetings", "alarm");
	}
	
	public List<AlarmData> getAlamrs(){
		return alarmDao.findAll();
	}
	
	public List<String> getUserGroupIds(User user){
		List<String> ids = new ArrayList<String>();
		for(DeviceGroup g : user.getGroups()) {
			ids.add(g.getGroupId());
		}
		return ids;
	}
	
	public TodayData getTodayData(String userName) {
		
		final User user = userDao.findByName(userName);
		
		try{
			if(user == null || user.getRole().equals(User.ROLE_ADMIN))
				return deviceDao.selectTodayStatistics();
			
			if(user.getGroups().size() == 0) {
				return new TodayData();
			}
			
			return deviceDao.selectTodayStatistics(getUserGroupIds(user));
			
		}catch(Exception ex) {
			ex.printStackTrace();
			return new TodayData();
		}
	}
	
	public Page<CheckData> searchCheckData(SearchCheckData search, String userName) {
		
		final User user = userDao.findByName(userName);
		if(user != null && !user.getRole().equals(User.ROLE_ADMIN) && (
				user.getGroups() == null || user.getGroups().size() == 0)) {
			return null;
		}
		
		Direction orderBy  = (search.getOrder() == null || search.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(orderBy, search.getSort()));
		
		//if(search.getDeviceName() == null)search.setDeviceName("");
		if(search.getBetrween1() == null) {
			search.setBetrween1(0l);
		}
		if(search.getBetrween2() == null) {
			search.setBetrween2(System.currentTimeMillis());
		}		
		
		return checkDataDao.findAll(new Specification<CheckData>() {

			@Override
			public Predicate toPredicate(Root<CheckData> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<>();				
				
				if(user != null && !user.getRole().equals(User.ROLE_ADMIN)) {
					CriteriaBuilder.In<Object> in = builder.in(root.get("groupId"));
					for(DeviceGroup g : user.getGroups())
						in.value(g.getGroupId());
					
					predicatesList.add(builder.and(
							builder.and(in)
							));
				}				
				
				if(search.getDeviceName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("deviceName"), "%" + search.getDeviceName() + "%")
							));
				}
				if(search.getName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("name"), "%" + search.getName() + "%")
							));
				}
				
				if(search.getCardNo() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("cardNo"), "%" + search.getCardNo() + "%")
							));
				}
				
				if(search.getResult() != null) {
					predicatesList.add(builder.and(
							builder.equal(root.get("result"), search.getResult())
							));
				}
				if(search.getBetrween1() != null) {
					predicatesList.add(builder.and(
							builder.ge(root.get("compareDate"), search.getBetrween1())
							));
				}
				if(search.getBetrween2() != null) {
					predicatesList.add(builder.and(
							builder.le(root.get("compareDate"), search.getBetrween2())
							));
				}				
				
				return builder.and(
	                    predicatesList.toArray(new Predicate[predicatesList.size()]));
				
			}
			
		}, pageable);
	}
	
	public Page<AlarmData> searchAlarmData(SearchAlarmData search, String userName) {
		
		final User user = userDao.findByName(userName);
		if(user != null && !user.getRole().equals(User.ROLE_ADMIN) && (
				user.getGroups() == null || user.getGroups().size() == 0)) {
			return null;
		}
		
		Direction orderBy  = (search.getOrder() == null || search.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(orderBy, search.getSort()));	
		
		if(search.getBetrween1() == null) {
			search.setBetrween1(0l);
		}
		if(search.getBetrween2() == null) {
			search.setBetrween2(System.currentTimeMillis());
		}
		
		return alarmDao.findAll(new Specification<AlarmData>() {

			@Override
			public Predicate toPredicate(Root<AlarmData> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicatesList = new ArrayList<>();
				
				if(user != null && !user.getRole().equals(User.ROLE_ADMIN)) {
					CriteriaBuilder.In<Object> in = builder.in(root.get("groupId"));
					for(DeviceGroup g : user.getGroups())
						in.value(g.getGroupId());
					
					predicatesList.add(builder.and(
							builder.and(in)
							));
				}				
				
				if(search.getDeviceName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("deviceName"), "%" + search.getDeviceName() + "%")
							));
				}
				if(search.getName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("name"), "%" + search.getName() + "%")
							));
				}
				
				if(search.getCardNo() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("cardNo"), "%" + search.getCardNo() + "%")
							));
				}
				
				if(search.getAlarmType() != null) {
					predicatesList.add(builder.and(
							builder.equal(root.get("alarmType"), search.getAlarmType())
							));
				}
				if(search.getBetrween1() != null) {
					predicatesList.add(builder.and(
							builder.ge(root.get("compareDate"), search.getBetrween1())
							));
				}
				if(search.getBetrween2() != null) {
					predicatesList.add(builder.and(
							builder.le(root.get("compareDate"), search.getBetrween2())
							));
				}				
				
				return builder.and(
	                    predicatesList.toArray(new Predicate[predicatesList.size()]));
				
			}
			
		}, pageable);
	}
	
	public List<CheckData> searchCheckData2(SearchCheckData search, String userName) {
		
		final User user = userDao.findByName(userName);
		if(user != null && !user.getRole().equals(User.ROLE_ADMIN) && (
				user.getGroups() == null || user.getGroups().size() == 0)) {
			return null;
		}
		
		Direction orderBy  = (search.getOrder() == null || search.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		Pageable pageable = PageRequest.of(search.getPage(), search.getSize(), Sort.by(orderBy, search.getSort()));
		
		if(search.getBetrween1() == null) {
			search.setBetrween1(0l);
		}
		if(search.getBetrween2() == null) {
			search.setBetrween2(System.currentTimeMillis());
		}		
		
		//return checkDataDao.findAll(new Specification<CheckData>() {

			//@Override
			//public Predicate toPredicate(Root<CheckData> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();		
		CriteriaQuery<CheckData> query = builder.createQuery(CheckData.class);//builder.createQuery();
		//CriteriaQuery<Tuple>  query = builder.createTupleQuery();
		Root<CheckData> root = query.from(CheckData.class);
		
		root.alias("check_data_0");
		
				List<Predicate> predicatesList = new ArrayList<>();				
				
				if(user != null && !user.getRole().equals(User.ROLE_ADMIN)) {
					CriteriaBuilder.In<Object> in = builder.in(root.get("groupId"));
					for(DeviceGroup g : user.getGroups())
						in.value(g.getGroupId());
					
					predicatesList.add(builder.and(
							builder.and(in)
							));
				}				
				
				if(search.getDeviceName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("deviceName"), "%" + search.getDeviceName() + "%")
							));
				}
				if(search.getName() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("name"), "%" + search.getName() + "%")
							));
				}
				
				if(search.getCardNo() != null) {
					predicatesList.add(builder.and(
							builder.like(root.get("cardNo"), "%" + search.getCardNo() + "%")
							));
				}
				
				if(search.getResult() != null) {
					predicatesList.add(builder.and(
							builder.equal(root.get("result"), search.getResult())
							));
				}
				if(search.getBetrween1() != null) {
					predicatesList.add(builder.and(
							builder.ge(root.get("compareDate"), search.getBetrween1())
							));
				}
				if(search.getBetrween2() != null) {
					predicatesList.add(builder.and(
							builder.le(root.get("compareDate"), search.getBetrween2())
							));
				}				
				
				
			
				query.select(root.get("id")).where(builder.and(
			                    predicatesList.toArray(new Predicate[predicatesList.size()])));
				
				
				System.out.println("page="+search.getPage());
				
				int offset = search.getPage() * search.getSize();
				int max= search.getSize();
							
				
				
				/////////////////////////////				
				CriteriaQuery<Long> query2 = builder.createQuery(Long.class);//builder.createQuery();
				Root<CheckData>root2 = query2.from(query.getResultType());//.class);
				
				root2.alias("check_data_0");
				query2.select(builder.count(root2));
				query2.where(query.getRestriction());
				
				System.out.println("==================");
				long count = em.createQuery(query2).getSingleResult();
				System.out.println("count="+count);
				
				List<CheckData>list= em.createQuery(query).setFirstResult(offset).setMaxResults(max).getResultList();	
				System.out.println("list.size="+list.size());
				
				return list;
				//return builder.and(
	            //        predicatesList.toArray(new Predicate[predicatesList.size()]));
				
			//}
			
		//}, pageable);
	}
	
	public HashMap searchCheckData3(SearchCheckData search, String userName) {
		
		final User user = userDao.findByName(userName);
		if(user != null && !user.getRole().equals(User.ROLE_ADMIN) && (
				user.getGroups() == null || user.getGroups().size() == 0)) {
			return null;
		}		
		
		if(search.getBetrween1() == null) {
			search.setBetrween1(0l);
		}
		if(search.getBetrween2() == null) {
			search.setBetrween2(System.currentTimeMillis());
		}		
			
		String fromsql=" from check_data where compare_date >= " + search.getBetrween1() + " and compare_date <= "+ search.getBetrween2();
				
		List<String>in=new ArrayList<String>();
		if(user != null && !user.getRole().equals(User.ROLE_ADMIN) && user.getGroups().size() > 0) {
			
			for(DeviceGroup g : user.getGroups())
				in.add(g.getGroupId());
			
			fromsql += " and group_id in :groups ";
		}				
		
		if(search.getDeviceName() != null) {
			fromsql += " and device_name like '%" + search.getDeviceName() +"%'";
		}
		
		if(search.getName() != null) {
			fromsql += " and name like '%" + search.getName() +"%' ";
		}
		
		if(search.getCardNo() != null) {
			fromsql += " and card_no like '%" + search.getCardNo() +"%' ";
		}
		
		if(search.getResult() != null) {
			fromsql += " and result=" + search.getResult();
		}			
		
		String countSql = "select count(id) " + fromsql;
		////
		int offset = search.getPage() * search.getSize();		
		fromsql += " order by " + CheckData.toNative(search.getSort()) + " " + search.getOrder();
		fromsql += " limit " + offset +  ", " +search.getSize();
		
		String select = "select id ";
		String sql2= "select * from check_data a join ( " +  select + fromsql + ") b on a.id=b.id";
		
		Query query= em.createNativeQuery(sql2, CheckData.class);						
		Query countQuery = em.createNativeQuery(countSql);		
		
		////set parameters
		if(in.size() > 0) {
			query.setParameter("groups", in);
			countQuery.setParameter("groups", in);
		}
		
		List<CheckData>list = query.getResultList();
		
		BigInteger  count = (BigInteger)countQuery.getSingleResult();
		
		
		HashMap<String,Object>result= new HashMap<String,Object>();
		result.put("content", list);
		result.put("totalElements",count);
		
		
		return result;
				
	}
}
