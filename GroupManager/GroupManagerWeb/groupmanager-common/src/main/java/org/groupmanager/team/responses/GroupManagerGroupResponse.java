package org.groupmanager.team.responses;

import java.util.List;

import org.groupmanager.team.dto.GroupDTO;
import org.groupmanager.team.dto.PositionDTO;

public class GroupManagerGroupResponse extends GroupManagerResponse {
	private Long groupId;
	private List<PositionDTO> positions;
	private List<GroupDTO> groups;

	public List<PositionDTO> getPositions() {
		return positions;
	}

	public void setPositions(List<PositionDTO> positions) {
		this.positions = positions;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public List<GroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<GroupDTO> groups) {
		this.groups = groups;
	}
}
