package jung.api.erp.customer.controller;

import io.swagger.v3.oas.annotations.Operation;
import jung.api.erp.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/erp/read/customer")
public class CustomerReadController {

    private final CustomerService customerService;
    @GetMapping("/list")
    @Operation(summary = "고객 조회", description = "고객 정보 조회 API 입니다.")
    public List<String> findCustomerList() {
        return customerService.findCustomerList();
    }

}
