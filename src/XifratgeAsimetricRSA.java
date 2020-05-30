import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Classe que fa xifra i desxifra amb criptorgrafia asimètrica un missatge
 * utilitzant l'algorisme RSA
 *
 * @author sergi grau
 * @version 1.0 15/01/2013
 */
public class XifratgeAsimetricRSA {

    public static void main(String[] args) {
        try {
            byte[] missatge = "Sergi Grau, Escola del Clot".getBytes();
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair parellaClaus = keyGen.generateKeyPair();
            Cipher xifrarRSA = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            System.out.println( "\n" + xifrarRSA.getProvider().getInfo() );
            // inicial. del xifrador. xifrem amb la clau pública
            xifrarRSA.init(Cipher.ENCRYPT_MODE, parellaClaus.getPublic());
             // xifratge del missatge
            byte[] missatgeXifrat = xifrarRSA.doFinal(missatge);
            System.out.println(new String(missatgeXifrat));
            
            //desxifratge amb clau privada
             xifrarRSA.init(Cipher.DECRYPT_MODE, parellaClaus.getPrivate());
             byte[] missatgeDes= xifrarRSA.doFinal(missatgeXifrat);

                System.out.println(new String(missatgeDes));
            


        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException e) {
        }

    }
}
