package dev.pratishtha.project.userService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@Entity
public class Session extends BaseModel{

    private String token;
    private Date expiryAt;

    @Enumerated(value = EnumType.ORDINAL)
    private SessionStatus sessionStatus;

    @ManyToOne
    private User user;
}
