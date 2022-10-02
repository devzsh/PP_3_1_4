package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService implements UserDetailsService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserRepository userDAO;
    private final RoleRepository roleDAO;

    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userDAO, RoleRepository roleDAO) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    @Transactional
    public User findUserById(Long userId) {
        Optional<User> userFromDb = userDAO.findById(userId);
        return userFromDb.orElse(new User());
    }

    @Transactional
    public List<User> allUsers() {
        return userDAO.findAll();
    }


    public boolean checkUser(User user) {
        if ((userDAO.findByUsername(user.getUsername()) == null)
                || (userDAO.findByUsername(user.getUsername()).getId() == user.getId()))  {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public void addUser(User user) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userDAO.save(user);
    }

    public void editUser(User user) {

        userDAO.save(user);
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }


    @Transactional
    public boolean saveUser(User user, Errors err) {


        if (userDAO.findByUsername(user.getUsername()) == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userDAO.save(user);
            return true;
        } else {
            err.rejectValue("username", "", "User already exists");
            return false;
        }


    }

    @Transactional
    public boolean deleteUser(Long id) {
        if (userDAO.findById(id).isPresent()) {
            userDAO.deleteById(id);
            return true;
        }
        return false;
    }


    @PostConstruct
    public void addDefaultUser() {

        roleDAO.save(new Role(1L,"ROLE_ADMIN"));
        roleDAO.save(new Role(2L,"ROLE_USER"));
        Set<Role> set1 = new HashSet<>();
        set1.add( new  Role(1L));
        User user1 = new User(1L,"admin","admin","admin",100,"2@2", set1);
        addUser(user1);

    }



}