package jung.api.ERP.Customer.Service;

import java.util.List;

public interface CustomerService {
    String findCustomer(Long userId);
    List<String> findCustomerList();

    List<String> createCustomer();
}
