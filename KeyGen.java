import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class KeyGen {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        
        PrivateKey priv = kp.getPrivate();
        PublicKey pub = kp.getPublic();
        
        String privPem = "-----BEGIN PRIVATE KEY-----\n" + 
                         Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(priv.getEncoded()) + 
                         "\n-----END PRIVATE KEY-----\n";
                         
        String pubPem = "-----BEGIN PUBLIC KEY-----\n" + 
                        Base64.getMimeEncoder(64, new byte[]{'\n'}).encodeToString(pub.getEncoded()) + 
                        "\n-----END PUBLIC KEY-----\n";
                        
        try (FileOutputStream fos = new FileOutputStream("src/main/resources/privateKey.pem")) {
            fos.write(privPem.getBytes());
        }
        
        try (FileOutputStream fos = new FileOutputStream("src/main/resources/publicKey.pem")) {
            fos.write(pubPem.getBytes());
        }
        
        System.out.println("Keys generated successfully.");
    }
}
