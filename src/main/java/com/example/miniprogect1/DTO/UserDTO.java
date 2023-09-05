package com.example.miniprogect1.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;
    private Long amount;
    private Long dotori;
    private String userRegDate;
    private String userRegDateTime;
}
