package com.example.stad;

import com.example.stad.Common.Entities.User;
import com.example.stad.Common.Enums.Role;
import com.example.stad.WebApi.Exceptions.UserNotFoundException;

import java.util.EnumSet;

public class SessionManagement {

    // General method to validate a user against allowed roles
    private void validateUserRole(User user, EnumSet<Role> allowedRoles) throws UserNotFoundException {
        if (user == null || !allowedRoles.contains(user.getRoles())) {
            throw new UserNotFoundException("You are not authorized to perform this operation");
        }
    }

    // Specific validations using the general method
    public void validateLoggedInAdmin(User user) throws UserNotFoundException {
        validateUserRole(user, EnumSet.of(Role.ADMIN));
    }

    public void validateLoggedInOwner(User user) throws UserNotFoundException {
        validateUserRole(user, EnumSet.of(Role.OWNER));
    }

    public void validateLoggedInCustomer(User user) throws UserNotFoundException {
        validateUserRole(user, EnumSet.of(Role.CUSTOMER));
    }

    public void validateLoggedInCustomerAndOwner(User user) throws UserNotFoundException {
        validateUserRole(user, EnumSet.of(Role.CUSTOMER, Role.OWNER));
    }

    public void validateLoggedInOwnerOrAdmin(User user) throws UserNotFoundException {
        validateUserRole(user, EnumSet.of(Role.ADMIN, Role.OWNER));
    }

    public void validateLoggedInCheckInOut(User user) throws UserNotFoundException {
        validateUserRole(user, EnumSet.of(Role.ADMIN, Role.OWNER));
    }

    public void validateLoggedInAllUser(User user) throws UserNotFoundException {
        validateUserRole(user, EnumSet.of(Role.ADMIN, Role.OWNER, Role.CUSTOMER));
    }
}
