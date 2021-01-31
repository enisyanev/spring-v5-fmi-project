package com.project.spring.digitalwallet.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.spring.digitalwallet.dao.GroupRepository;
import com.project.spring.digitalwallet.exception.InvalidEntityDataException;
import com.project.spring.digitalwallet.model.Account;
import com.project.spring.digitalwallet.model.group.Group;
import com.project.spring.digitalwallet.model.user.User;

@Service
public class GroupService {

	private GroupRepository groupRepository;
	private AccountService accountService;
	private UserService userService;

	@Autowired
	public GroupService(GroupRepository groupRepository, AccountService accountService, UserService userService) {
		this.groupRepository = groupRepository;
		this.accountService = accountService;
		this.userService = userService;
	}

	public List<Group> getAllGroups() {
		return groupRepository.findAll();
	}

	public Group createGroup(Group group) {
		group.setCurrentMoney(0);
		return groupRepository.save(group);
	}

	public String updateTagetMoneyGroup(String groupName, int money, String currency, String username) {
		User user = userService.getUserByUsername(username);
		Account sender = accountService.getByCurrencyAndWalletId(currency, user.getWalletId());
		validate(sender, BigDecimal.valueOf(money));
		Group group = groupRepository.findByGroupName(groupName);
		if (group.getCurrentMoney() >= group.getTargetMoney()) {
			return "Target already acomplished!";
		} else if (group.getCurrentMoney() + money >= group.getTargetMoney()) {
			int rest = group.getCurrentMoney() + money - group.getTargetMoney();
			accountService.updateBalance(sender.getId(),
					BigDecimal.valueOf(money).subtract(BigDecimal.valueOf(rest)).negate());
			group.setCurrentMoney(group.getTargetMoney());
			groupRepository.save(group);
			return "Target acomplished!Well done!";
		} else {
			group.setCurrentMoney(group.getCurrentMoney() + money);
			accountService.updateBalance(sender.getId(), BigDecimal.valueOf(money).negate());
			groupRepository.save(group);
			return "Donation succesful!Well done!";
		}

	}

	private void validate(Account account, BigDecimal amount) {
		if (account.getBalance().compareTo(amount) < 0) {
			throw new InvalidEntityDataException("Insufficient balance!");
		}
	}
}
