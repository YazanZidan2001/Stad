package com.example.stad;

import com.example.stad.Common.Entities.User;
import com.example.stad.Common.Enums.Role;
import com.example.stad.WebApi.Exceptions.UserNotFoundException;

public class SessionManagement {

    // Validate if the logged-in user is ADMIN
    public void validateLoggedInAdmin(User user) throws UserNotFoundException {
        if (user.getRole() != Role.ADMIN) {
            throw new UserNotFoundException("You are not authorized to perform this operation");
        }
    }

    // Validate if the logged-in user is OWNER
    public void validateLoggedInOwner(User user) throws UserNotFoundException {
        if (user.getRole() != Role.OWNER) {
            throw new UserNotFoundException("You are not authorized to perform this operation");
        }
    }

    // Validate if the logged-in user is CUSTOMER
    public void validateLoggedInCustomer(User user) throws UserNotFoundException {
        if (user.getRole() != Role.CUSTOMER) {
            throw new UserNotFoundException("You are not authorized to perform this operation");
        }
    }

    // Validate if the logged-in user is either CUSTOMER or OWNER
    public void validateLoggedInCustomerAndOwner(User user) throws UserNotFoundException {
        if (user.getRole() != Role.CUSTOMER && user.getRole() != Role.OWNER) {
            throw new UserNotFoundException("You are not authorized to perform this operation");
        }
    }

    // Validate if the logged-in user is either OWNER or ADMIN
    public void validateLoggedInOwnerOrAdmin(User user) throws UserNotFoundException {
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.OWNER) {
            throw new UserNotFoundException("You are not authorized to perform this operation");
        }
    }

    // Validate if the logged-in user can perform check-in or check-out
    public void validateLoggedInCheckInOut(User user) throws UserNotFoundException {
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.OWNER) {
            throw new UserNotFoundException("You are not authorized to perform this operation");
        }
    }

    // Validate if the logged-in user is any valid role
    public void validateLoggedInAllUser(User user) throws UserNotFoundException {
        if (user.getRole() != Role.ADMIN && user.getRole() != Role.OWNER && user.getRole() != Role.CUSTOMER) {
            throw new UserNotFoundException("You are not authorized to perform this operation");
        }
    }
}
