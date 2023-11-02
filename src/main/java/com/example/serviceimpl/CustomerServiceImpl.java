package com.example.serviceimpl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.example.entity.ERole;
import com.example.entity.Roles;
import com.example.jwt.SignUpRequest;
import com.example.repository.RoleRepository;
import com.example.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.entity.Customer;
import com.example.exception.CustomerNotFoundException;
import com.example.repository.ICustomerRepo;
import com.example.service.ICustomerService;

import javax.management.relation.RoleNotFoundException;

@Service
public class CustomerServiceImpl implements ICustomerService {

    @Autowired
    private ICustomerRepo iCustomerRepo;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JWTUtility jwtUtils;

    @Override
    public List<Customer> getCustomerList() {
        return iCustomerRepo.findAll();
    }

    @Override
    public Customer updateCustomerDetail(Integer customerNumber, Customer c) {
        Optional<Customer> customer1 = iCustomerRepo.findById(customerNumber);
        if (customer1.isPresent()) {
            Customer customer = customer1.get();
            customer.setCustomerFirstName(c.getCustomerFirstName());
            customer.setCustomerLastName(c.getCustomerLastName());
            customer.setAddressLine1(c.getAddressLine1());
            customer.setAddressLine2(c.getAddressLine2());
            customer.setCity(c.getCity());
            customer.setCountry(c.getCountry());
            customer.setPostalCode(c.getPostalCode());
            customer.setState(c.getState());
            customer.setPhone(c.getPhone());
            return iCustomerRepo.save(customer);
        } else {
            throw new CustomerNotFoundException("Id does Not Exist");
        }
    }

    @Override
    public Customer getCustomerById(Integer customerNumber) {


        return iCustomerRepo.findById(customerNumber).orElseThrow(() ->
                new CustomerNotFoundException("Customer cannot be created: " + customerNumber));

    }

    @Override
    public Customer registerCustomer(Customer customerNumber) {
        return iCustomerRepo.save(customerNumber);
    }

    @Override
    public Customer registerCustomerSignUp( SignUpRequest signUpRequest) {
        try {
            if (iCustomerRepo.existsByUsername(signUpRequest.getUsername())) {
                throw new RoleNotFoundException("Error: Username is already taken!");
            }
            if (iCustomerRepo.existsByEmail(signUpRequest.getEmail())) {
                throw new RoleNotFoundException("Error: Email is already in use!");
            }
            //Create new user's account
            //Customer user = new Customer(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
            Customer user = new Customer(signUpRequest.getCustomerFirstName(), signUpRequest.getCustomerLastName(), signUpRequest.getPhone(),
                    signUpRequest.getAddressLine1(), signUpRequest.getAddressLine2(),
                    signUpRequest.getCity(),
                    signUpRequest.getState(),
                    signUpRequest.getPostalCode(),
                    signUpRequest.getCountry(),
                    signUpRequest.getUsername(), signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword())
            );

            Set<String> strRoles = signUpRequest.getRoles();
            Set<Roles> roles = new HashSet<>();
            if (strRoles == null) {
                Roles userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    switch (role) {
                        case "admin":
                            Roles adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        case "super_admin":
                            Roles modRole = roleRepository.findByName(ERole.ROLE_SUPER_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(modRole);

                            break;
                        case "delivery_agent":
                            Roles deliveryRole = roleRepository.findByName(ERole.ROLE_DELIVERY_AGENT)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(deliveryRole);

                            break;
                        default:
                            Roles userRole = roleRepository.findByName(ERole.ROLE_CUSTOMER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
                    }
                });
            }
            user.setRoles(roles);
            Customer customer = iCustomerRepo.save(user);
            return customer;
        } catch (RoleNotFoundException runtimeException) {
            throw new CustomerNotFoundException("customer not registered");
        }
    }

    @Override
    public String deleteCustomer(Integer customerNumber) {
        try {
            if (customerNumber != null) {
                iCustomerRepo.deleteById(customerNumber);
            }
            return "One Customer deleted " + customerNumber;
        } catch (RuntimeException e) {
            throw new CustomerNotFoundException("Id does Not Exist");
        }
    }

    @Override
    public Customer findBycustomerFirstName(String customerFirstName) {

        return iCustomerRepo.findBycustomerFirstName(customerFirstName);
    }

    @Override
    public Customer findBycustomerLastName(String customerLasstName) {

        return iCustomerRepo.findBycustomerLastName(customerLasstName);
    }

}
