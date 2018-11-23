package com.github.eitraz.avanza.model.account;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Account {
    private String accountType;
    private Double interestRate;
    private Boolean depositable;
    private Boolean active;
    private String accountId;
    private Boolean accountPartlyOwned;
    private Boolean attorney;
    private Boolean tradable;
    private Double totalBalance;
    private Double totalBalanceDue;
    private Double ownCapital;
    private Double buyingPower;
    private Double totalProfitPercent;
    private Double performance;
    private Double performancePercent;
    private Double totalProfit;
    private String name;
}
