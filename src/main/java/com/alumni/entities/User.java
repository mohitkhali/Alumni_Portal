package com.alumni.entities;


import com.alumni.entities.role.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.intellij.lang.annotations.Pattern;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
public class User implements UserDetails {

    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence"
            ,allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    @Id
    private Long UId;


    
    private String fullName;

    private String email;

    private String userType;
    
    private String password;

    private Boolean enabled=false;

    private Boolean filledForm=false;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    private Boolean locked=false;
    
    private String resetPasswordToken;
    

	

    public User(String fullName, String email,String userType, String password, UserRole role) {
        this.fullName=fullName;
        this.email=email;
        this.userType=userType;
        this.password=password;
        this.role=role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority= new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(simpleGrantedAuthority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return enabled;
    }


	


	
}
