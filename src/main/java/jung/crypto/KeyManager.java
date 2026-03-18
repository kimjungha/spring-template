package jung.crypto;

import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

@Component
public class KeyManager {

    public static String secp256r1 = "secp256r1";

    static {
        try {
            if (Security.getProperty(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(BouncyCastleProviderSingleton.getInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static KeyPair createKeypair(){
        KeyPair keyPair = null;
        try {
            keyPair = KeyManager.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
        generator.initialize(new ECGenParameterSpec(secp256r1), new SecureRandom());
        return generator.generateKeyPair();
    }

    public static byte[] toBytes(PublicKey key) {
        ECPublicKey ecPublicKey = (ECPublicKey) key;
        return ecPublicKey.getQ().getEncoded(false);
    }

    public static byte[] toBytes(PrivateKey key) {
        ECPrivateKey ecPrivateKey = (ECPrivateKey) key;
        return ecPrivateKey.getD().toByteArray();
    }

    public static PublicKey loadPublicKey(byte[] x, byte[] y) throws Exception {
        ECParameterSpec params = ECNamedCurveTable.getParameterSpec(secp256r1);
        BigInteger bx = new BigInteger(1, x);
        BigInteger by = new BigInteger(1, y);
        ECPublicKeySpec pubKey = new ECPublicKeySpec(params.getCurve().createPoint(bx, by), params);
        KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
        return kf.generatePublic(pubKey);
    }

    /**
     * 압축되지 않은 공개키는 04 prefix 가 붙고, x 와 y 는 각각 32바이트이다.
     * @param pubkey 서버의 공개키
     * @return
     * @throws Exception
     */
    public static PublicKey loadPublicKey(String pubkey)  throws Exception {
        String x = pubkey.substring(2, 66);
        String y = pubkey.substring(66);
        return loadPublicKey(Hex.decode(x), Hex.decode(y));
    }

    public static PublicKey loadPublicKey(byte[] data) throws Exception {
        ECParameterSpec params = ECNamedCurveTable.getParameterSpec(secp256r1);
        ECPublicKeySpec pubKey = new ECPublicKeySpec(params.getCurve().decodePoint(data), params);
        KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
        return kf.generatePublic(pubKey);
    }

    public static PrivateKey loadPrivateKey(byte[] data) throws Exception {
        ECParameterSpec params = ECNamedCurveTable.getParameterSpec(secp256r1);
        ECPrivateKeySpec prvkey = new ECPrivateKeySpec(new BigInteger(data), params);
        KeyFactory kf = KeyFactory.getInstance("ECDH", "BC");
        return kf.generatePrivate(prvkey);
    }
}
