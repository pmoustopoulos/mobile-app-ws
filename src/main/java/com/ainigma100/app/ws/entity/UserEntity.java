package com.ainigma100.app.ws.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 120, unique = true)
    private String email;

    @Column(nullable = false)
    private String encryptedPassword;

    private String emailVerificationToken;

    // set 'false' as a default value
    @Column(nullable = false)
    private Boolean emailVerificationStatus = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // specify the name of the field that owns the relationship in the AddressEntity class
    @OneToMany(mappedBy = "userDetails", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<AddressEntity> addresses;


    // Persist: If a user is deleted, we do not want to cascade and delete the role as well
    // Eager: when user details are read from the db, we want the roles to be retrieved right away
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    // @JoinTable is used to specify the mapping of a many-to-many table relationship
    // name: the name of the link table called 'users_roles'
    // joinColumns: a column called 'users_id' that references the primary key of 'users' table
    // inverseJoinColumns: a column called 'roles_id' that references the primary key of 'roles' table
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "users_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"))
    private Collection<RoleEntity> roles;

}
