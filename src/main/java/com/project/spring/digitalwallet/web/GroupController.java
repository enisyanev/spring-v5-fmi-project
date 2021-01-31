package com.project.spring.digitalwallet.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.spring.digitalwallet.model.group.Group;
import com.project.spring.digitalwallet.service.GroupService;

@RestController
@RequestMapping("/api/group")
public class GroupController {

	@Autowired
	private GroupService groupService;

	@PostMapping("/create")
	public void createGroup(@RequestBody Group group) {
		groupService.createGroup(group);
	}

	@PostMapping("/donate")
	public Map<String, String> donateToGroup(@RequestBody Map<String, String> json, Authentication authentication) {
		Map<String, String> resp = new HashMap<>();
		String message = groupService.updateTagetMoneyGroup(json.get("groupName"), Integer.parseInt(json.get("money")),
				json.get("currency"), authentication.getName());
		resp.put("message", message);
		return resp;
	}

	@GetMapping
	public List<Group> getAllGroups() {
		return groupService.getAllGroups();
	}
}
