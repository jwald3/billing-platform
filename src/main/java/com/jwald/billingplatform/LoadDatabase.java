package com.jwald.billingplatform;

import com.jwald.billingplatform.customer.Customer;
import com.jwald.billingplatform.customer.CustomerRepository;
import com.jwald.billingplatform.subscription.Subscription;
import com.jwald.billingplatform.subscription.SubscriptionRepository;
import com.jwald.billingplatform.user.User;
import com.jwald.billingplatform.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, SubscriptionRepository subscriptionRepository, CustomerRepository customerRepository) {
        return args -> {
            userRepository.save(new User("User #1", 1L, LocalDate.of(2022, 1, 1), null));
            userRepository.save(new User("User #2", 1L, LocalDate.of(2022, 1, 1), null));
            userRepository.save(new User("User #3", 1L, LocalDate.of(2022, 1, 1), null));

            userRepository.findAll().forEach(user -> log.info("Preloaded " + user));

            Subscription sub1 = new Subscription(BigDecimal.valueOf(2.49));
            Subscription sub2 = new Subscription(BigDecimal.valueOf(3.99));
            Subscription sub3 = new Subscription(BigDecimal.valueOf(1.99));
            Subscription sub4 = new Subscription(BigDecimal.valueOf(4.99));

            Customer customer1 = new Customer("Customer One");
            Customer customer2 = new Customer("Customer Two");
            Customer customer3 = new Customer("Customer Three");

            sub1.setCustomer(customer1);
            sub2.setCustomer(customer2);
            sub3.setCustomer(customer3);

            customer1.setSubscription(sub1);
            customer2.setSubscription(sub2);
            customer3.setSubscription(sub3);

            subscriptionRepository.save(sub1);
            subscriptionRepository.save(sub2);
            subscriptionRepository.save(sub3);
            subscriptionRepository.save(sub4);

            subscriptionRepository.findAll().forEach(sub -> log.info("Preloaded " + sub));

            customerRepository.save(customer1);
            customerRepository.save(customer2);
            customerRepository.save(customer3);

            customerRepository.findAll().forEach(customer -> log.info("Preloaded " + customer));
        };
    }
}
