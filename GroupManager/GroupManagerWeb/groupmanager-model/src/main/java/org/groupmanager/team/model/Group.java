package org.groupmanager.team.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.IndexColumn;

@Entity
@Table(name = "gm_group")
public class Group {
	private Long id;
	private String name;
	private List<User> users;
	private Set<User> pendingUsers;
	private User owner;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "gm_user_group_map", joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@OneToOne
	@JoinColumn(name = "user_id", nullable = false)
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "gm_user_group_pending", joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"))
	public Set<User> getPendingUsers() {
		return pendingUsers;
	}

	public void setPendingUsers(Set<User> pendingUsers) {
		this.pendingUsers = pendingUsers;
	}

	public void addUserToGroup(User user) {
		if (users == null) {
			users = new ArrayList<User>();
		}
		boolean ok = true;
		for (User userL : users)
			if (userL.equals(user))
				ok = false;

		if (ok) {
			users.add(user);
			user.getGroups().add(this);
		}
	}

	public void removeUserFromGroup(User user) {
		if (users != null) {
			for (int i = 0; i < users.size(); i++) {
				if (users.get(i).equals(user)) {
					users.remove(i);
					user.getGroups().add(this);
					i--;
				}
			}
		}
	}
	
	/*public void addUserToPendingGroup(User user) {
		if (pendingUsers == null) {
			pendingUsers = new ArrayList<User>();
		}
		boolean ok = true;
		for (User userL : pendingUsers)
			if (userL.equals(user))
				ok = false;

		if (ok) {
			pendingUsers.add(user);
			user.getPendingGroups().add(this);
		}
	}
	
	public void removeUserFromPendingGroup(User user) {
		if (pendingUsers != null) {
			for (int i = 0; i < pendingUsers.size(); i++) {
				if (pendingUsers.get(i).equals(user)) {
					pendingUsers.remove(i);
					user.getPendingGroups().add(this);
					i--;
				}
			}
		}
	}*/

}
