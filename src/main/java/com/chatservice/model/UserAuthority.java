package com.chatservice.model;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
@ToString(exclude = "userSecurityDetails")
@EqualsAndHashCode(of = "ID")
@Table(name = "auths")
public class UserAuthority {

    @Id
    Long ID;

    String permission;

    @JsonIgnore
    @ManyToMany(mappedBy = "grantedAuths",fetch = FetchType.LAZY)
    Set<User> userSecurityDetails;

}
