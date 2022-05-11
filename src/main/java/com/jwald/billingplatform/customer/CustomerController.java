package com.jwald.billingplatform.customer;

import com.jwald.billingplatform.subscription.SubscriptionController;
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
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerModelAssembler customerModelAssembler;

    @GetMapping("/customers")
    public CollectionModel<EntityModel<Customer>> getAllCustomers() {
        List<EntityModel<Customer>> customers = customerRepository.findAll().stream()
                .map(customerModelAssembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(customers,
                linkTo(methodOn(CustomerController.class).getAllCustomers()).withSelfRel(),
                linkTo(methodOn(SubscriptionController.class).getAllSubscriptions()).withRel("subscriptions"),
                linkTo(methodOn(UserController.class).listAllUsers()).withRel("users")
                );
    }

    @PostMapping("/customers")
    public ResponseEntity<EntityModel<Customer>> createNewCustomer(@RequestBody Customer customer) {
        Customer newCustomer = customerRepository.save(customer);

        return ResponseEntity
                .created(linkTo(methodOn(CustomerController.class).getCustomerById(newCustomer.getId())).toUri())
                .body(customerModelAssembler.toModel(newCustomer));
    }

    @GetMapping("/customers/{id}")
    public EntityModel<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with the id " + id));

        return customerModelAssembler.toModel(customer);
    }

    @PutMapping("/customers/{id}")
    public ResponseEntity<?> replaceCustomer(@RequestBody Customer newCustomer, @PathVariable Long id) {
        Customer updatedCustomer = customerRepository.findById(id)
                .map(customer -> {
                    customer.setName(newCustomer.getName());
                    return customerRepository.save(customer);
                })
                .orElseGet(() -> {
                    newCustomer.setId(id);
                    return customerRepository.save(newCustomer);
                });

        EntityModel<Customer> entityModel = customerModelAssembler.toModel(updatedCustomer);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }


    @DeleteMapping("/customers/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        customerRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
