package com.jwald.billingplatform.subscription;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jwald.billingplatform.customer.Customer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Subscription {
    @Id
    @GeneratedValue
    private Long id;
    private BigDecimal dailyRate;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="fk_customer")
    private Customer customer;

    public Subscription() {
    }

    public Subscription(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return Objects.equals(id, that.id) && Objects.equals(dailyRate, that.dailyRate) && Objects.equals(customer, that.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dailyRate, customer);
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", dailyRate=" + dailyRate +
                ", customer=" + customer +
                '}';
    }
}
