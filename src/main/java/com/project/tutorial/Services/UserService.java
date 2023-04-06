package com.project.tutorial.Services;

import com.project.tutorial.Models.User;
import com.project.tutorial.Repositories.UserFeedRepo;
import com.project.tutorial.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = {"UserLogin"})
public class UserService implements UserDetailsService{

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserFeedRepo userFeedRepo;
    @Override
    @Cacheable(key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepository.findById(email);
        if (foundUser == null){
            throw new UsernameNotFoundException(email);
        }
        String emailId = foundUser.get().getEmail();
        String password = foundUser.get().getPassword();
        return new org.springframework.security.core.userdetails.User(emailId,password,new ArrayList<>());
    }
    public void processOAuthPostLogin(String email,String fullname,String provider) {
        if(provider.equals("GOOGLE")) {
            if (!userRepository.existsById(email)) {
                User newUser = new User(email,fullname,"123456789","USER",null,null,null);
                newUser.setProvider("GOOGLE");
                newUser.setEnabled(true);
                userRepository.save(newUser);
            }
        }
    }
}
