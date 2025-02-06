package jung.api.ERP.Customer;

import jung.api.ERP.Customer.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor //생성자 직접 작성 없이 주입 자동으로 설정가능
@RequestMapping("/erp/customer")
public class CustomerController {

    private final CustomerService customerService;
    @GetMapping("/list")
    public List<String> findCustomerList() {
        return customerService.findCustomerList();
    }

    @PostMapping("/create")
    public List<String> createCustomer(@RequestBody String request) {
        return customerService.createCustomer();
    }

}
