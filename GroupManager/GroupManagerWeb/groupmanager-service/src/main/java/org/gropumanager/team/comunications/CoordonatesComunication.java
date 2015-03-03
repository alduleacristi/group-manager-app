package org.gropumanager.team.comunications;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.groupmanager.team.model.Group;
import org.groupmanager.team.model.User;

@Stateless
public class CoordonatesComunication {
//	 private Logger logger = LoggerFactory
//	 .getLogger(CoordonatesComunication.class);
	@PersistenceContext(unitName = "groupmanager-sql")
	private EntityManager em;

	@PostConstruct
	public void construct() {
		// logger.info("***** PostConstruct CoordonatesComunication ******");
		// logger.info("**** PostConstruct of the CoordonatesComunication *****");
		System.out.println("**** PostConstruct ****");
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public String getMessage() {

		User user = new User();
		user.setEmail("emailTest@gmail.com");
		em.persist(user);
		em.flush();
//		List<User> users = new ArrayList<User>();
//		users.add(user);
//
//		Group group = new Group();
//		group.setName("FirstGroup");
//		group.setUsers(users);
//		em.persist(group);
//		em.flush();

		return "Hello world from EJB";
	}

}
