package com.company.coworking.management.service;

import com.company.coworking.management.entity.User;

public interface CurrentUserService {
    User getCurrentUser();

    boolean isAdmin();

    Long getCurrentUserId();
}
