package com.chatservice.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.Date;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


@NamedEntityGraph(name = "user.chats",
        attributeNodes = {@NamedAttributeNode("chats"),@NamedAttributeNode(value ="grantedAuths")}
)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
@Table(name = "users")
@Builder
@ToString(of = "ID")
@EqualsAndHashCode(of="ID")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;


    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.REFRESH})
    private Set<UserAuthority> grantedAuths;

    @NotBlank
    @Email(message = "Email is not valid",regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @Column(unique = true,nullable = false)
    private String email;
    @Size(min = 8, message = "{validation.name.size.too_short}")
    @Size(max = 200, message = "{validation.name.size.too_long}")
    @NotBlank
    private String password;


    private Date creationDate;


    @Column(name = "is_enabled",nullable = false)
    private boolean isEnabled;

//    @JsonIgnore
    @ManyToMany(mappedBy = "members")
    private Set<Chat> chats;

    @Column(name = "active")
    Boolean active;


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getGrantedAuths()==null
                ? Collections.emptyList()
                : getGrantedAuths().stream().map(t->new SimpleGrantedAuthority(t.getPermission())).collect(Collectors.toSet());
    }


    @PrePersist
    private void newUserAttempt(){
        setCreationDate(new Date(java.util.Date.from(Instant.now()).getTime()));
    }


    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Set<UserAuthority> getGrantedAuths() {
        return grantedAuths;
    }

    public void setGrantedAuths(Set<UserAuthority> grantedAuths) {
        this.grantedAuths = grantedAuths;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public Set<Chat> getChats() {
        return chats;
    }

    public void setChats(Set<Chat> chats) {
        this.chats = chats;
    }
}
