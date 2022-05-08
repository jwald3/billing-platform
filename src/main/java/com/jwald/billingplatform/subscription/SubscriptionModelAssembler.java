package com.jwald.billingplatform.subscription;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SubscriptionModelAssembler implements RepresentationModelAssembler<Subscription, EntityModel<Subscription>> {

    @Override
    public EntityModel<Subscription> toModel(Subscription subscription) {
        return EntityModel.of(subscription,
                linkTo(methodOn(SubscriptionController.class).getSubscriptionById(subscription.getId())).withSelfRel(),
                linkTo(methodOn(SubscriptionController.class).getAllSubscriptions()).withRel("subscriptions")
        );
    }
}
