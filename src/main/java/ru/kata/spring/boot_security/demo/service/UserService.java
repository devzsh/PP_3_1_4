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
import ru.kata.spring.boot_security.demo.model.User;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    public boolean addUser(User user) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userDAO.save(user);
            return true;


    }


    @Transactional
    public boolean saveUser(User user, Errors err) {



        if (userDAO.findByUsername(user.getUsername()) == null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userDAO.save(user);
            return true;
        } else {
            err.rejectValue("username", "", "User already exists");
            //throw new NonUniqueResultException("Пользователь с таким логином уже существует!");
            return false;
        }


    }

    @Transactional
    public boolean deleteUser(Long userId) {
        if (userDAO.findById(userId).isPresent()) {
            userDAO.deleteById(userId);
            return true;
        }
        return false;
    }


}