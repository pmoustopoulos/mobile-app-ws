package com.ainigma100.app.ws.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;


    // It is mapped by 'roles' property which exists in UserEntity class
    @ManyToMany(mappedBy = "roles")
    private Collection<UserEntity> users;

    // Persist: If a user is deleted, we do not want to cascade and delete the role as well
    // Eager: when user details are read from the db, we want the roles to be retrieved right away
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    // @JoinTable is used to specify the mapping of a many-to-many table relationship
    // name: the name of the link table called 'roles_authorities'
    // joinColumns: a column called 'roles_id' that references the primary key of 'roles' table
    // inverseJoinColumns: a column called 'authorities_id' that references the primary key of 'authorities' table
    @JoinTable(name = "roles_authorities",
            joinColumns = @JoinColumn(name = "roles_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "authorities_id", referencedColumnName = "id"))
    private Collection<AuthorityEntity> authorities;
}
