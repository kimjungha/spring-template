package jung.api.erp.customer.controller;

import jung.api.erp.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequiredArgsConstructor //생성자 직접 작성 없이 주입 자동으로 설정가능
@RequestMapping("/erp/read/customer")
public class CustomerReadController {

    private final CustomerService customerService;
    @GetMapping("/list")
    public List<String> findCustomerList() {
        return customerService.findCustomerList();
    }

}
