package jung.api.erp.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jung.api.erp.customer.CreateCustomerRequest;
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
    @Operation(summary = "고객 생성", description = "신규 고객 생성하는 API 입니다.")
    public List<String> createCustomer(@Valid @RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer();
    }

}
