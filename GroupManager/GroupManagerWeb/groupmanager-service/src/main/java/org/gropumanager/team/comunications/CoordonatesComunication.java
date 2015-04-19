package org.gropumanager.team.comunications;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.groupmanager.team.model.Position;

@Stateless
public class CoordonatesComunication {
	@PersistenceContext()
	private EntityManager em;

	public void updateUserLocation(Position position) {
		em.merge(position);
		em.flush();
	}

}
