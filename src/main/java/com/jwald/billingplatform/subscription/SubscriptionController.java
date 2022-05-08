package com.jwald.billingplatform.subscription;

import com.jwald.billingplatform.customer.CustomerController;
import com.jwald.billingplatform.user.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class SubscriptionController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private SubscriptionModelAssembler subscriptionModelAssembler;

    @GetMapping("/subscriptions")
    public CollectionModel<EntityModel<Subscription>> getAllSubscriptions() {
        List<EntityModel<Subscription>> subscriptions = subscriptionRepository.findAll().stream()
                .map(subscriptionModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(subscriptions,
                linkTo(methodOn(SubscriptionController.class).getAllSubscriptions()).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"),
                linkTo(methodOn(UserController.class).listAllUsers()).withRel("users")
        );
    }

    @GetMapping("/subscriptions/{id}")
    public EntityModel<Subscription> getSubscriptionById(@PathVariable Long id) {

        Subscription subscription = subscriptionRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Subscription not found with the id " + id));

        return subscriptionModelAssembler.toModel(subscription);
    }
}
