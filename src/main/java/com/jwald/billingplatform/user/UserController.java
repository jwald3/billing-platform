package com.jwald.billingplatform.user;

import com.jwald.billingplatform.customer.CustomerController;
import com.jwald.billingplatform.subscription.SubscriptionController;
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
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserModelAssembler userModelAssembler;

    @GetMapping("/users")
    public CollectionModel<EntityModel<User>> listAllUsers() {
        List<EntityModel<User>> users = userRepository.findAll().stream()
                .map(userModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).listAllUsers()).withSelfRel(),
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withRel("customers"),
                linkTo(methodOn(SubscriptionController.class).getAllSubscriptions()).withRel("subscriptions")
        );
    }

    @PostMapping("/users")
    public ResponseEntity<EntityModel<User>> newUser(@RequestBody User user) {
        User newUser = userRepository.save(user);

        return ResponseEntity
                .created(linkTo(methodOn(UserController.class).getUserById(newUser.getId())).toUri())
                .body(userModelAssembler.toModel(newUser));
    }

    @GetMapping("/users/{id}")
    public EntityModel<User> getUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with the id " + id));

        return userModelAssembler.toModel(user);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> replaceUser(@RequestBody User newUser, @PathVariable Long id) {
        User updatedUser = userRepository.findById(id)
                .map(user -> {
                    user.setName(newUser.getName());
                    user.setSubscriptionStart(newUser.getSubscriptionStart());
                    user.setSubscriptionEnd(newUser.getSubscriptionEnd());
                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return userRepository.save(newUser);
                });

        EntityModel<User> entityModel = userModelAssembler.toModel(updatedUser);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
