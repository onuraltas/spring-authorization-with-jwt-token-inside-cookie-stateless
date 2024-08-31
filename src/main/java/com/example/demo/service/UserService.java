package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.util.Utils;

@Service
public class UserService implements UserDetailsService {

    private static final Map<String, User> userMap = new HashMap<String, User>();

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = null;
        if (Utils.isValidEmail(username)) {
            user = userMap.get(username);
        }

        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        return new CustomUserDetails(user.email(), user.passwordEncrypted(), new ArrayList<>(), true);
    }

    public void createUser(String email, String name, String password) {
        User user = new User(email, name, bCryptPasswordEncoder.encode(password));

        userMap.put(email, user);

        System.out.println(user.email());
        System.out.println(user.name());
        System.out.println(user.passwordEncrypted());
    }

    public User getUserByEmail(String email) {
        return userMap.get(email);
    }

}
