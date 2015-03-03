package org.groupmanager.team.user;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.groupmanager.team.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class UserService {
	 private Logger logger = LoggerFactory
			 .getLogger(UserService.class);
	
	@PersistenceContext(name = "groupmanager-sql")
	private EntityManager em;
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void addUser(User user){
		em.persist(user);
		em.flush();
		logger.info("User with email {} was added succesfully",user.getEmail());
	}
}
