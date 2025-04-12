package com.ecommerce.service.impl;

import com.ecommerce.entity.UserDemand;
import com.ecommerce.entity.UserDetail;
import com.ecommerce.repository.UserDemandRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserDemandService implements com.ecommerce.service.UserDemandService {

    private UserDemandRepository userDemandRepository;

    @Override
    public UserDemand addUserDemand(UserDemand userDemand) {
        return userDemandRepository.save(userDemand);
    }

    @Override
    public UserDetail updateUserDemand(String pid, int quantity) {
        return null;
    }

    @Override
    public List<UserDemand> getAllUserDemand(String pid) {
        return List.of();
    }
}
