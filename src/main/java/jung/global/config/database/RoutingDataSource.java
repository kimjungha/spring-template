package jung.global.config.database;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {
    /**
     * 동적 DB source 변경위해, AbstractRoutingDataSource.setTargetDataSources() 설정이 필요함
     * 스프링은 트랜잭션에 진입순간 DB connection을 가져오기에, 트랜잭션 진입 이후에 DB source 를 분기하는것은 불가
     * 트랜잭션이 시작될 때 메서드 호출하여 데이터 소스 결정된다.
     */
    @Override
    protected Object determineCurrentLookupKey() {
        boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        //@Transaction(readOnly = true) 이면 slave DB
        if (readOnly) {
            return "slave";
        } else {
            return "master";
        }
    }
}
