package com.github.eitraz.avanza.model.account;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Overview {
    private List<Account> accounts;
    private Long numberOfOrders;
    private Long numberOfDeals;
    private Long numberOfTransfers;
    private Long numberOfIntradayTransfers;
    private Double totalPerformancePercent;
    private Double totalPerformance;
    private Double totalBalance;
    private Double totalBuyingPower;
    private Double totalOwnCapital;
}
