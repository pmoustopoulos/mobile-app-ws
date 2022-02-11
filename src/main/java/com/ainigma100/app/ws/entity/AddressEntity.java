package com.ainigma100.app.ws.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "addresses")
public class AddressEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // public address id that will be sent back contained in the response
    @Column(nullable = false)
    private String addressId;

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


    @ManyToOne
    // provide the name of the foreign key that will hold this relationship. TABLE-NAME_FIELD-NAME
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;

}
