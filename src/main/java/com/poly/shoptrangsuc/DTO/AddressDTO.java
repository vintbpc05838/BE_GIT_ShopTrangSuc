package com.poly.shoptrangsuc.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Integer addressId;
    private String address;
    private String city;
    private String district;
    private Boolean isDefault;

    

}
