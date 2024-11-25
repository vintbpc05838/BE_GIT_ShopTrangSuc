package com.poly.shoptrangsuc.Model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SearchHistory")
public class SearchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SearchHistoryID")
    private Integer searchHistoryId;

    @ManyToOne
    @JoinColumn(name = "AccountID") // Establishing the relationship with Account
    private Account account;

    @Column(name = "SearchKeyword")
    private String searchKeyword;

    @Column(name = "SearchTime")
    private Timestamp searchTime;

    // Getters and setters
    public Integer getSearchHistoryId() {
        return searchHistoryId;
    }

    public void setSearchHistoryId(Integer searchHistoryId) {
        this.searchHistoryId = searchHistoryId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public Timestamp getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(Timestamp searchTime) {
        this.searchTime = searchTime;
    }
}