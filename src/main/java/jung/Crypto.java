package jung;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.util.encoders.Hex;

@Slf4j
public class Crypto {
    public static String secp256r1 = "secp256r1";

    static {
        try {
            if (Security.getProperty(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(BouncyCastleProviderSingleton.getInstance());
            }
        } catch (Exception e) {
           log.error("암호학 에러");
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Start Crypto");
        // 1. key 생성
        KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        generator.initialize(new ECGenParameterSpec(secp256r1), new SecureRandom());
        KeyPair keyPair = generator.generateKeyPair();

        System.out.println("공개키:"+ Hex.toHexString(toBytes(keyPair.getPublic())));
        System.out.println("비밀키:"+keyPair.getPrivate());

        // 2. 보안채널 생성
        String channelId = "1e3eb9e3693843a2a2c3aad416d86d43";
        PublicKey serverPubKey = loadPublicKey("04029b84e3255aa428ad3ebee731dbcd798422d5e829c6368b3b64aa4e710a9abb17e1f39b8b42a4cfe5243eb9982143e32d2cfa9a79b76176b23a313222eeb905");
        System.out.println("서버 공개키 :"+Hex.toHexString(toBytes(serverPubKey)));


    }

    /**
     * 압축되지 않은 공개키는 04 prefix 가 붙고, x 와 y 는 각각 32바이트이다.
     * @param pubkey 서버의 공개키
     * @throws Exception
     */
    public static PublicKey loadPublicKey(String pubkey)  throws Exception {
        String x = pubkey.substring(2, 66);
        String y = pubkey.substring(66);
        return loadPublicKey(Hex.decode(x), Hex.decode(y));
    }

    public static PublicKey loadPublicKey(byte[] x, byte[] y) throws Exception {
        ECParameterSpec params = ECNamedCurveTable.getParameterSpec(secp256r1);
        BigInteger bx = new BigInteger(1, x);
        BigInteger by = new BigInteger(1, y);
        ECPublicKeySpec pubKey = new ECPublicKeySpec(params.getCurve().createPoint(bx, by), params);
        KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
        return kf.generatePublic(pubKey);
    }

    public static byte[] toBytes(PublicKey key) {
        ECPublicKey ecPublicKey = (ECPublicKey) key;
        return ecPublicKey.getQ().getEncoded(false);
    }




}
