package com.alumni.token;

import com.alumni.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@Data

public class ConfirmationToken {


    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence"
            ,allocationSize = 1
    )

    @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "confirmation_token_sequence"
    )
    @Id
    private Long id;


    @NotNull
    private String token;

    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime expiredAt;


    private LocalDateTime confirmedAt;

    @ManyToOne  
    @JoinColumn(nullable = false,
    name = "user_id")
    @Cascade(CascadeType.DELETE)
    private User user;

    public ConfirmationToken(String token,
                             LocalDateTime createdAt,
                             LocalDateTime expiredAt,User user) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.user=user;
    }
}
