package org.groupmanager.team.user;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.groupmanager.team.model.User;

@Singleton
@Startup
public class GroupManagerSession {
	private Map<String, User> authenticatedUsers;

	@PostConstruct
	private void initialize() {
		authenticatedUsers = new HashMap<String, User>();
	}

	public void addUser(String key, User user) {
		authenticatedUsers.put(key, user);
		System.out.println(authenticatedUsers.size());
	}

	public User getUserByKey(String key) {
		return authenticatedUsers.get(key);
	}
	
	public void removeUser(String key){
		authenticatedUsers.remove(key);
		System.out.println(authenticatedUsers.size());
	}
}
