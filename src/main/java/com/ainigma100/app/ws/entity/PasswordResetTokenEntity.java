package com.ainigma100.app.ws.entity;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetTokenEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    // @Lob specifies that the object is very large
    @Lob
    private String token;

    @OneToOne
    // provide the name of the foreign key that will hold this relationship. TABLE-NAME_FIELD-NAME
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;
    
}
