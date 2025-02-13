package jung.api.erp.customer.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    @Override
    public String findCustomer(Long userId) {
        log.info("1명 Customer 고객 조회 ==> masterDB 로 연결 ");
        log.info("@Transactional 애노테이션 달지 않으면,setDefault DB로 연결");
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findCustomerList() {
        log.info("Customer 고객리스트 조회 ==> slaveDB 로 연결 ");
        return null;
    }

    @Override
    @Transactional
    public List<String> createCustomer() {
        log.info("Customer 고객리스트 신규 저장  ==> masterDB 로 연결 ");
        log.info("@Transactional  ==> readOnly 작성안하게 된다면, 기본값 false이므로 ==> masterDB로 연결 ");
        return null;
    }
}
