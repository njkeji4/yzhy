package com.shicha.yzmgt.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.shicha.yzmgt.bean.BlackList;
import com.shicha.yzmgt.bean.Device;
import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.dao.IBlaclistDao;
import com.shicha.yzmgt.dao.ICheckData;
import com.shicha.yzmgt.dao.IUserDao;
import com.shicha.yzmgt.domain.SearchBlacklist;

@Service
public class BlacklistService {

	private static final Logger log = LoggerFactory.getLogger(BlacklistService.class);
	
	@Autowired
	IBlaclistDao blackListDao;
	
	@Autowired
	IUserDao userDao;
	
	public void save(BlackList blacklist) {
		blacklist.setCreateTime(System.currentTimeMillis());
		blackListDao.save(blacklist);
	}
	
	public void remove(String[] ids, String userName) {
		
		for(String id: ids) {
			blackListDao.deletebyCardNo(id, userName);
		}
	}
	
	public List<BlackList> getall(){
		return blackListDao.findAll();
	}
	
	public List<BlackList> getall(String userName){
		
		User user = userDao.findByName(userName);
		if(user == null || user.getRole().equals(User.ROLE_ADMIN))
			return blackListDao.findAll();
		
		return blackListDao.findByUserName(userName);
	}
	
	public Page<BlackList> searchList(SearchBlacklist filter, String userName){
		
		User user = userDao.findByName(userName);
		
		Direction orderBy  = (filter.getOrder() == null || filter.getOrder().equals("asc")) ? Direction.ASC : Direction.DESC;		
		Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(orderBy, filter.getSort()));	
		
		BlackList blackList = new BlackList();
		blackList.setName(filter.getName());
		blackList.setAddress(filter.getAddress());
		blackList.setBirthday(filter.getBirthday());
		blackList.setCardNo(filter.getCardNo());
		blackList.setSex(filter.getSex());
		blackList.setFolk(filter.getFolk());	
		if(user != null && !user.getRole().equals(User.ROLE_ADMIN)) {
			blackList.setUserName(userName);
		}
		
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues()
									.withStringMatcher(StringMatcher.CONTAINING);								
		
		Example<BlackList> example = Example.of(blackList, matcher);
		
		return blackListDao.findAll(example, pageable);
	}
	
	public boolean importBlacklistFromFile(MultipartFile file, String userName) {
		
		String fname = file.getOriginalFilename();
		if(fname.length() <= 3) {
			return false;
		}
		
		Workbook wb = null;		
		String last3str = fname.substring(fname.length() - 3);
		try {
			if(last3str.equals("xls")) {	//<=excel2003
				wb = new HSSFWorkbook(file.getInputStream());
			}else {	//excel 2007
				wb = new XSSFWorkbook(file.getInputStream());
			}
			Sheet sheet = wb.getSheetAt(0);
			
			List<BlackList> bl = new ArrayList<BlackList>();
			
			for(int i = 1; i < sheet.getLastRowNum(); i++) {
				Row row = sheet.getRow(i);
				
				String name = row.getCell(0).getStringCellValue();
				if(name == null || name.length() == 0) {
					break;
				}
				String cardNo = row.getCell(1).getStringCellValue();
				String sexStr = row.getCell(2).getStringCellValue();
				
				int sex = 1;
				if(sexStr.equals("女")) {
					sex = 2;
				}
				bl.add(new BlackList(cardNo, name, sex, userName));
			}
			
			blackListDao.saveAll(bl);
			
			wb.close();
			
			return true;
			
		}catch(Exception ex) {
			ex.printStackTrace();
			return false;
		}		
	}
	
}
