package com.example.serviceimpl;

import com.example.entity.Customer;
import com.example.repository.ICustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private ICustomerRepo customerRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(customer);
    }


    /*@Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
     // Logic to get the user form the Database
        return new User("admin","password",new ArrayList<>());
    }*/
}