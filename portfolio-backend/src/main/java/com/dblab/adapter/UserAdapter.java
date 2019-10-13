package com.dblab.adapter;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class UserAdapter extends User {

    private com.dblab.domain.User user;

    public UserAdapter(com.dblab.domain.User user) {
        super(user.getUsername(), user.getPassword(), authorites());
        this.user = user;
    }

    private static Collection<? extends GrantedAuthority> authorites() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return grantedAuthorities;
    }
}
