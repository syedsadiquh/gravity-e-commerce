package com.gravityer.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String email;

    @Override
    public String toString() {
        return "{"+this.id+", "+this.username+", "+this.email+"}\n";
    }
}
