package org.fundamenta.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@XmlType(propOrder = {"name", "premium"})
public class SalesManager {

    private String name;

    private List<Deal> deals = new ArrayList<>();

    private double basicPremium;

    private double extraPremium;

    private double premium;

    private Map<String, Integer> sumOfSalesPerType = new HashMap<>();

    public SalesManager() {
    }

    public SalesManager(String name, Deal deal) {
        this.name = name;
        deals.add(deal);
    }

    @XmlAttribute
    public double getPremium() {
        return premium;
    }

    public void addDeal(Deal deal) {
        this.deals.add(deal);
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setBasicPremium(double basicPremium) {
        this.basicPremium = basicPremium;
    }

    public void setExtraPremium(double extraPremium) {
        this.extraPremium = extraPremium;
    }

    public void putSumOfSalesPerType(String type, int sum) {
        sumOfSalesPerType.put(type, sum);
    }

    public Map<String, Integer> getSumOfSalesPerType() {
        return sumOfSalesPerType;
    }

    public void calculatePremium() {
        premium = basicPremium + extraPremium;
    }
}
