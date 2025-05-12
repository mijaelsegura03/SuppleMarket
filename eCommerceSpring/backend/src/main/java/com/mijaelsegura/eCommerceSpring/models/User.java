package com.mijaelsegura.eCommerceSpring.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.mijaelsegura.eCommerceSpring.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "users")
public class User implements UserDetails {
    @Id
    private Long DNI;
    @Column(name = "name")
    private String name;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    @Enumerated(EnumType.ORDINAL)
    private Role role;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Purchase> purchases;

    @OneToOne(mappedBy = "user")
    @JsonManagedReference
    private Cart cart;

    public User(Long DNI, String name, String lastName, LocalDate birthDate, String username, String password) {
        this.DNI = DNI;
        this.name = name;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.username = username;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return String.valueOf(DNI); //tipo de identificación que queremos que tenga nuestro usuario, tiene que ser única.
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

    public String getUsernameString() {
        return username;
    }
}
