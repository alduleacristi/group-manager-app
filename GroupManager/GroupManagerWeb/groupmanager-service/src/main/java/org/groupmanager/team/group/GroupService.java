package org.groupmanager.team.group;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.groupmanager.team.model.Group;
import org.groupmanager.team.model.User;
import org.groupmanager.team.user.UserService;

@Stateless
public class GroupService {
	@PersistenceContext
	private EntityManager em;

	@Inject
	private UserService userService;

	public Long addGroup(Group group) {
		em.persist(group);

		return group.getId();
	}

	public List<User> getUserForGroup(long idGroup) {
		Group group = (Group) em
				.createQuery("select g from Group g where g.id = :idGroup")
				.setParameter("idGroup", idGroup).getSingleResult();

		return group.getUsers();
	}

	public List<Group> getGroupForUser(String email) {
		User user = userService.getUserByEmail(email);
		return user.getGroups();
	}

}
