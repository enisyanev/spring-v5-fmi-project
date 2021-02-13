package com.project.spring.digitalwallet.utils;

import java.util.Arrays;
import java.util.List;

public enum UserPermission {

    CAN_DEPOSIT, CAN_WITHDRAW, CAN_DONATE, CAN_CREATE_GROUP, CAN_SEND_MONEY, CAN_CREATE_ACCOUNT, CAN_USE_RECCURINGS,
    CAN_SEE_TR_HISTORY, CAN_USE_USER_MANAGEMENT;

    public static List<UserPermission> getAllPermissions() {
        return Arrays.asList(CAN_DEPOSIT, CAN_WITHDRAW, CAN_DONATE, CAN_CREATE_GROUP, CAN_SEND_MONEY,
                CAN_CREATE_ACCOUNT, CAN_USE_RECCURINGS, CAN_SEE_TR_HISTORY, CAN_USE_USER_MANAGEMENT);
    }
}
