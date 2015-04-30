package org.groupmanager.team.group;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.groupmanager.team.model.Group;
import org.groupmanager.team.model.User;
import org.groupmanager.team.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class GroupService {
	private Logger logger = LoggerFactory.getLogger(GroupService.class);

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

	public void addUsersToGroup(Group group, List<User> users) {
		for (User user : users)
			group.addUserToGroup(user);
		em.merge(group);
		em.flush();
	}

	public void removeUsersFromGroup(Group group, List<User> users) {
		for (User user : users)
			group.removeUserFromGroup(user);
		em.merge(group);
		em.flush();
	}

	public void addUsersToPendingGroup(Group group, List<User> users) {
		Group newGroup = em.find(Group.class, group.getId());
		for (User user : users) {
			newGroup.getPendingUsers().add(user);
			user.getGroups().add(newGroup);
		}
		em.merge(newGroup);
		em.flush();
	}

	public void acceptPendingGroup(List<Group> groups, User user) {
		User newUser = userService.getUserByEmail(user.getEmail());
		for (Group group : groups) {
			Group newGroup = em.find(Group.class, group.getId());
			//newGroup.getUsers().add(newUser);
			//newUser.getGroups().add(newGroup);
			newGroup.addUserToGroup(newUser);
			
			newGroup.getPendingUsers().remove(newUser);
			newUser.getPendingGroups().remove(newGroup);
			
			em.merge(newGroup);
			logger.info("Dupa merge");
		}

		em.flush();

	}

	public Group getGroupById(long id) {
		return em.find(Group.class, id);
	}

	public Group getGroupByName(String name) {
		@SuppressWarnings("unchecked")
		List<Group> groups = em
				.createQuery("select g from Group g where g.name = :name")
				.setParameter("name", name).getResultList();

		if (groups != null && groups.size() > 0)
			return groups.get(0);
		return null;
	}

}
