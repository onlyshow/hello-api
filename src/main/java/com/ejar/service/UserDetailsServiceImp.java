package com.ejar.service;

import com.ejar.dao.UserDetailsDao;
import com.ejar.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserDetailsDao userDetailsDao;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDetailsDao.findUserByUsername(username);
        UserBuilder userBuilder;

        if (user != null) {
            userBuilder = org.springframework.security.core.userdetails.User.withUsername(username);
            userBuilder.disabled(!user.getEnabled());
            userBuilder.password(user.getPassword());

            String[] authorities = user.getAuthorities().stream().map(a -> a.getAuthority()).toArray(String[]::new);

            userBuilder.authorities(authorities);
        } else {
            throw new UsernameNotFoundException("Username Not Found");
        }

        return userBuilder.build();
    }
}
