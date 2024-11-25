package com.poly.shoptrangsuc.Repository;

import com.poly.shoptrangsuc.Model.Account;
import com.poly.shoptrangsuc.Model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    Address findByIsDefaultTrue();
    Address save(Address address);

    Address findByAddressAndCityAndDistrict(String address, String city, String district);

    List<Address> findByAccount(Account account);

    List<Address> findByAccount_accountId(Integer accountId);




    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.account.accountId = :accountId")
    void removeDefaultAddress(@Param("accountId") Integer accountId);





}
