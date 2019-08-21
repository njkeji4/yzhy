package com.shicha.yzmgt.security;

import java.util.ArrayList;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import com.shicha.yzmgt.bean.User;
import com.shicha.yzmgt.dao.IUserDao;

@Component
public class LoginAuthenticationProvider implements AuthenticationProvider{

	@Autowired
	IUserDao userDao;
	
	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		// TODO Auto-generated method stub
		String userName = (String)auth.getPrincipal();
		String password = (String)auth.getCredentials();
		
		LoginAuthenticationDetails details = (LoginAuthenticationDetails)auth.getDetails();
		
		User user = userDao.findByNameAndPassword(userName, password);
		if(user == null) {
			throw new AccountExpiredException("invalid");
		}
		
		List<SimpleGrantedAuthority>authorities = new ArrayList<SimpleGrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(user.getRole()));
//		List<Group>groups =user.getGroups();
//		if(groups!=null && groups.size() > 0) {
//			for(Group g : groups) {
//				List<Role>roles = g.getRoles();
//				if(roles == null || roles.size() == 0)continue;
//				for(Role role : roles) {
//					authorities.add(new SimpleGrantedAuthority(role.getName()));
//				}
//			}
//		}
		
		return new UsernamePasswordAuthenticationToken(userName, password,authorities);
	}

	@Override
	public boolean supports(Class<?> auth) {
		// TODO Auto-generated method stub
		return auth.equals(UsernamePasswordAuthenticationToken.class);
	}

}
