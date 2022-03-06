package com.ainigma100.app.ws.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class AddressEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;


    @Column(nullable = false, length = 15)
    private String city;

    @Column(nullable = false, length = 15)
    private String country;

    @Column(nullable = false, length = 100)
    private String streetName;

    @Column(nullable = false, length = 8)
    private String postalCode;

    @Column(nullable = false, length = 10)
    private String type;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


    @ManyToOne
    // provide the name of the foreign key that will hold this relationship. TABLE-NAME_FIELD-NAME
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;

}
