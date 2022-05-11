package com.jwald.billingplatform.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jwald.billingplatform.subscription.Subscription;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Customer {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToOne
    @JoinColumn(name="subscription_id", nullable = true)
    @JsonIgnoreProperties("customer")
    private Subscription subscription;


    public Customer() {
    }

    public Customer(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(subscription, customer.subscription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, subscription);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
