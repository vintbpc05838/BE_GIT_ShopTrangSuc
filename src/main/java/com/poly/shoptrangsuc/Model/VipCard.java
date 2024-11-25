package com.poly.shoptrangsuc.Model;
import jakarta.persistence.*;

import java.sql.Date;

@Entity
@Table(name = "VIPCard")
public class VipCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VIPCardID")
    private Integer vipCardId;

    @Column(name = "CardholderName")
    private String cardholderName;

    @Column(name = "ExpirationDate")
    private java.sql.Date expirationDate;

    @Column(name = "DiscountPercentage")
    private Integer discountPercentage;

    @OneToOne
    @JoinColumn(name = "AccountID")
    private Account account;

    public Integer getVipCardId() {
        return vipCardId;
    }

    public void setVipCardId(Integer vipCardId) {
        this.vipCardId = vipCardId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

}
