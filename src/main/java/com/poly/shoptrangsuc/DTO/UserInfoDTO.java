package com.poly.shoptrangsuc.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoDTO {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String address;
    private String city;
    private String district;
}
