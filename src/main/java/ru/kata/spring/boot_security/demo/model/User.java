package ru.kata.spring.boot_security.demo.model;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;


    private String username;


    private String password;


    private String lastName;


    private Integer age;

    private String email;


    @ManyToMany (fetch = FetchType.LAZY)
            @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    private Set<Role> roles;



    public Long getId() {
        return id;
    }


    public User(String username, String password, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }


    public User(String username, String password, String lastName, Integer age, String email) {
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
    }


    public User(Long id, String username, String password, String lastName, Integer age, String email, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.roles = roles;
    }

    public User() {
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles (Set<Role> roles) {
        this.roles = roles;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public void setUsername(String userName) {
        this.username = userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
