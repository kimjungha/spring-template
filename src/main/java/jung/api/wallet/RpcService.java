package jung.api.wallet;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RpcService {

    private final String USDC_MINT;
    private final RestClient restClient;

    public RpcService(
            @Value("${solana.usdc-mint}") String usdcMint,
            @Value("${solana.rpc.url}") String rpcUrl
    ) {
        USDC_MINT = usdcMint;
        this.restClient = RestClient.builder()
                .baseUrl(rpcUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }


    /**
     *  Solana - 트랜잭션 검증(Receipt 확인)
     *  fromAddress -> toAddress 에 amountRaw 만큼 수신 확인
     */
    public void verifyInboundTransfer(
            String txHash, String fromAddress, String toAddress, long amountRaw
    ) {
        Map<String, Object> body = Map.of(
                "jsonrpc", "2.0",
                "id", 1,
                "method", "getTransaction",
                "params", List.of(txHash, Map.of(
                        "encoding", "jsonParsed",
                        "commitment", "confirmed",  // 블록에 쌓이는 기준을 설정할 수 있다.
                        "maxSupportedTransactionVersion", 0
                ))
        );

        JsonNode root = restClient.post().body(body).retrieve().body(JsonNode.class);
        JsonNode result = root == null ? null : root.path("result");

        // tx 성공 확인
        if (result == null || result.isNull() || result.isMissingNode()) {
            log.error("{} : Receipt 조회 실패 - tx 없음 또는 미확인", txHash);
            throw new RuntimeException("찾을수 없습니다.");
        }
        if (!result.path("meta").path("err").isNull()) {
            log.error("{} : 온체인 tx 실패", txHash);
            throw new RuntimeException("찾을수 없습니다.");
        }

        JsonNode preBalances = result.path("meta").path("preTokenBalances");
        JsonNode postBalances = result.path("meta").path("postTokenBalances");

        Map<String, Long> preMap  = toUsdcBalanceMap(preBalances);  // 트랜잭션 실행 전
        Map<String, Long> postMap = toUsdcBalanceMap(postBalances); // 트랜잭션 실행 후

        // sender 검증: externalWallet 이 맞는가
        if (!preMap.containsKey(fromAddress)) {
            log.error("{} : 송신 지갑 불일치 - fromAddress={}", txHash, fromAddress);
            throw new RuntimeException("찾을수 없습니다.");
        }

        // recipient 검증: hdWallet 이 맞는가
        if (!postMap.containsKey(toAddress)) {
            log.error("{} : 수신 지갑 불일치 - toAddress={}", txHash, toAddress);
            throw new RuntimeException("찾을수 없습니다.");
        }

        // amountRaw 만큼 증가했는지 검증
        long pre  = preMap.getOrDefault(toAddress, 0L);
        long post = postMap.getOrDefault(toAddress, -1L);

        if ((post - pre) != amountRaw) {
            log.error("{} : 금액 불일치 - toAddress={}", txHash, toAddress);
            throw new RuntimeException("찾을수 없습니다.");
        }

        log.info("txHash ({}) 금액 일치: {} -> {}", txHash, pre, post);
    }

    private Map<String, Long> toUsdcBalanceMap(JsonNode balances) {
        Map<String, Long> map = new HashMap<>();
        for (JsonNode entry : balances) {
            if (entry.path("mint").asText().equals(USDC_MINT)) {
                map.put(
                        entry.path("owner").asText(),
                        entry.path("uiTokenAmount").path("amount").asLong()
                );
            }
        }
        return map;
    }

    /**
     * Solana USDC ATA(Associated Token Account) 주소 조회
     */
    public String getUsdcTokenAccountAddress(String walletAddress) {
        Map<String, Object> body = Map.of(
                "jsonrpc", "2.0",
                "id", 1,
                "method", "getTokenAccountsByOwner",
                "params", List.of(walletAddress, Map.of("mint", USDC_MINT), Map.of("encoding", "jsonParsed"))
        );

        JsonNode root = restClient.post().body(body).retrieve().body(JsonNode.class);
        JsonNode value = root.path("result").path("value");
        if (value.isMissingNode() || value.isEmpty()) return null;
        return value.get(0).path("pubkey").asText();
    }
}
