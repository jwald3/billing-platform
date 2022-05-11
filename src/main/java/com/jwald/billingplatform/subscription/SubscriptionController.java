package com.jwald.billingplatform.subscription;

import com.jwald.billingplatform.customer.CustomerController;
import com.jwald.billingplatform.user.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/subscriptions")
    public ResponseEntity<EntityModel<Subscription>> newSubscription(@RequestBody Subscription subscription) {
        Subscription newSubscription = subscriptionRepository.save(subscription);

        return ResponseEntity
                .created(linkTo(methodOn(SubscriptionController.class).getSubscriptionById(newSubscription.getId())).toUri())
                .body(subscriptionModelAssembler.toModel(newSubscription));
    }

    @GetMapping("/subscriptions/{id}")
    public EntityModel<Subscription> getSubscriptionById(@PathVariable Long id) {

        Subscription subscription = subscriptionRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Subscription not found with the id " + id));

        return subscriptionModelAssembler.toModel(subscription);
    }

    @PutMapping("/subscriptions/{id}")
    ResponseEntity<?> replaceSubscription(@RequestBody Subscription newSubscription, @PathVariable Long id) {
        Subscription updatedSubscription = subscriptionRepository.findById(id)
                .map(subscription -> {
                    subscription.setDailyRate(newSubscription.getDailyRate());
                    return subscriptionRepository.save(subscription);
                })
                .orElseGet(() -> {
                    newSubscription.setId(id);
                    return subscriptionRepository.save(newSubscription);
                });

        EntityModel<Subscription> entityModel = subscriptionModelAssembler.toModel(updatedSubscription);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/subscriptions/{id}")
    public ResponseEntity<?> deleteSubscription(@PathVariable Long id) {
        subscriptionRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
