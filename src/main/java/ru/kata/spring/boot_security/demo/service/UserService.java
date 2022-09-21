package ru.kata.spring.boot_security.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleRepository;
import ru.kata.spring.boot_security.demo.dao.UserRepository;
import ru.kata.spring.boot_security.demo.model.User;
import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @PersistenceContext
    private EntityManager em;
    @Autowired
    UserRepository userDAO;
    @Autowired
    RoleRepository roleDAO;



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

    public boolean addUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
        return true;
    }


    public boolean saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
        return true;
    }

    public boolean deleteUser(Long userId) {
        if (userDAO.findById(userId).isPresent()) {
            userDAO.deleteById(userId);
            return true;
        }
        return false;
    }


}