package com.github.eitraz.avanza.model.account;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountOverview {
    private String courtageClass;
    private Boolean depositable;
    private String accountType;
    private Boolean withdrawable;
    private String clearingNumber;
    private Boolean instrumentTransferPossible;
    private Boolean internalTransferPossible;
    private Boolean jointlyOwned;
    private String accountId;
    private String accountTypeName;
    private Double interestRate;
    private Long numberOfOrders;
    private Long numberOfDeals;
    private Double performanceSinceOneWeek;
    private Double performanceSinceOneMonth;
    private Double performanceSinceThreeMonths;
    private Double performanceSinceSixMonths;
    private Double performanceSinceOneYear;
    private Double performanceSinceThreeYears;
    private Double performanceSinceOneWeekPercent;
    private Double performanceSinceOneMonthPercent;
    private Double performanceSinceThreeMonthsPercent;
    private Double performanceSinceSixMonthsPercent;
    private Double performanceSinceOneYearPercent;
    private Double performanceSinceThreeYearsPercent;
    private Boolean allowMonthlySaving;
    private Double totalProfit;
    private Double creditLimit;
    private List<CurrencyAccount> currencyAccounts;
    private Double forwardBalance;
    private Double reservedAmount;
    private Double totalCollateralValue;
    private Double totalPositionsValue;
    private Double buyingPower;
    private Double totalProfitPercent;
    private Double availableSuperLoanAmount;
    private Double performancePercent;
    private Double accruedInterest;
    private Boolean overMortgaged;
    private Double creditAfterInterest;
    private Boolean overdrawn;
    private Double performance;
    private Double totalBalance;
    private Double ownCapital;
    private Long numberOfTransfers;
    private Long numberOfIntradayTransfers;
    private Double standardDeviation;
    private Double sharpeRatio;
}
