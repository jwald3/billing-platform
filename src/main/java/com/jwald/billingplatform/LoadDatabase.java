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

            subscriptionRepository.save(new Subscription(1L, BigDecimal.valueOf(2.49)));
            subscriptionRepository.save(new Subscription(2L, BigDecimal.valueOf(3.99)));
            subscriptionRepository.save(new Subscription(3L, BigDecimal.valueOf(1.99)));

            subscriptionRepository.findAll().forEach(sub -> log.info("Preloaded " + sub));

            customerRepository.save(new Customer());
            customerRepository.save(new Customer());
            customerRepository.save(new Customer());

            customerRepository.findAll().forEach(customer -> log.info("Preloaded " + customer));
        };
    }
}
