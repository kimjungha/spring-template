package jung.api.erp.customer.controller;

import jung.api.erp.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/erp/customer")
public class CustomerCreateController {

    private final CustomerService customerService;

    @PostMapping("/create")
    public List<String> createCustomer(@RequestBody String request) {
        return customerService.createCustomer();
    }

}
