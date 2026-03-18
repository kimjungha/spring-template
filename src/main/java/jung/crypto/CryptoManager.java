package jung.crypto;

import com.nimbusds.jose.crypto.bc.BouncyCastleProviderSingleton;
import jung.crypto.symmetric.AES;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import javax.crypto.KeyAgreement;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;

@Component
public class CryptoManager {

    public static String secp256r1 = "secp256r1";

    private static CryptoManager instance = null;

    static {
        try {
            if (Security.getProperty(BouncyCastleProvider.PROVIDER_NAME) == null) {
                Security.addProvider(BouncyCastleProviderSingleton.getInstance());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CryptoManager getInstance() {
        if(instance == null) {
            instance = new CryptoManager();
        }

        return instance;
    }

    public byte[] getSharedSecretWithECDH(PrivateKey privateKey, PublicKey publicKey) throws Exception {
        byte[] prikey = KeyManager.toBytes(privateKey);
        byte[] pubkey = KeyManager.toBytes(publicKey);

        return getSharedSecretWithECDH(prikey, pubkey);
    }

    public byte[] getSharedSecretWithECDH(byte[] dataPrv, byte[] dataPub) throws Exception {
        KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME);

        PrivateKey privateKey = KeyManager.loadPrivateKey(dataPrv);
        PublicKey publicKey = KeyManager.loadPublicKey(dataPub);

        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey, true);

        byte[] secret = keyAgreement.generateSecret();

        return secret;
    }

    public String encryptWithAES(byte[] sharedSecret, String plainText) throws Exception {
        AES aes = new AES(sharedSecret);
        byte[] encrypted = aes.encrypt(plainText.getBytes());
        byte[] encoded = Base64.encode(encrypted);

        return new String(encoded);
    }

    public String decryptWithAES(byte[] sharedSecret, String encrypted) throws Exception {
        AES aes = new AES(sharedSecret);
        byte[] decrypted = aes.decrypt(Base64.decode(encrypted));

        return new String(decrypted);
    }

    public String encryptSecret(String serverPubKey, KeyPair keyPair, String secret){
        String x = serverPubKey.substring(2, 66);
        String y = serverPubKey.substring(66);
        PublicKey publicKey;
        try {
            publicKey = KeyManager.loadPublicKey(Hex.decode(x), Hex.decode(y));
        } catch (Exception e) {
            throw new RuntimeException(e);
//            throw new BusinessException(ErrorCode.SECURE_CHANNEL_ENCRYPT_ERROR);
        }
        byte[] sharedSecret;
        try {
            sharedSecret = this.getSharedSecretWithECDH(keyPair.getPrivate(), publicKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
//            throw new BusinessException(ErrorCode.SECURE_CHANNEL_ENCRYPT_ERROR);
        }
        try {
            return this.encryptWithAES(sharedSecret, secret);
        } catch (Exception e) {
            throw new RuntimeException(e);
//            throw new BusinessException(ErrorCode.SECURE_CHANNEL_ENCRYPT_ERROR);
        }
    }
}
