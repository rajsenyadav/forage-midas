package com.jpmc.midascore.controller;

import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class BalanceController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping(value = "/balance", produces = MediaType.APPLICATION_JSON_VALUE)
    public Balance getBalance(@RequestParam("userId") Long userId) {
        // Convert Long to match repository findById parameter type
        Optional<UserRecord> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            UserRecord user = userOpt.get();
            return new Balance(user.getBalance());
        } else {
            // Return balance of 0 if user doesn't exist
            return new Balance(0.0f);
        }
    }
}
