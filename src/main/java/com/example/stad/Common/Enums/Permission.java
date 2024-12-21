package com.example.stad.Common.Enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    //ADMIN
    ADMIN_READ("Admin:read"),
    ADMIN_UPDATE("Admin:update"),
    ADMIN_CREATE("Admin:create"),
    ADMIN_DELETE("Admin:delete"),
    //OWNER
    OWNER_READ("OWNER:read"),
    OWNER_UPDATE("OWNER:update"),
    OWNER_CREATE("OWNER:create"),
    OWNER_DELETE("OWNER:delete"),
    //CUSTOMER
    CUSTOMER_READ("CUSTOMER:read"),
    CUSTOMER_UPDATE("CUSTOMER:update"),
    CUSTOMER_CREATE("CUSTOMER:create"),
    CUSTOMER_DELETE("CUSTOMER:delete"),

    SUPER_ADMIN_READ("SUPER_ADMIN:read"),
    SUPER_ADMIN_UPDATE("SUPER_ADMIN:update"),
    SUPER_ADMIN_CREATE("SUPER_ADMIN:create"),
    SUPER_ADMIN_DELETE("SUPER_ADMIN:delete");

    private final String permission;
}
