package com.jwald.billingplatform.subscription;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Subscription {
    @Id
    @GeneratedValue
    private Long id;
    private Long customer_id;
    private BigDecimal dailyRate;

    public Subscription() {
    }

    public Subscription(Long customer_id, BigDecimal dailyRate) {
        this.customer_id = customer_id;
        this.dailyRate = dailyRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Long customer_id) {
        this.customer_id = customer_id;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(id, that.id) && Objects.equals(customer_id, that.customer_id) && Objects.equals(dailyRate, that.dailyRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, customer_id, dailyRate);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", customer_id=" + customer_id +
                ", dailyRate=" + dailyRate +
                '}';
    }
}
