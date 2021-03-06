import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class XifratgeSimetric {
    KeyGenerator keygenerator = KeyGenerator.getInstance("DES");
    SecretKey clauDES = keygenerator.generateKey();
    Cipher xifradorDES = Cipher.getInstance("DES/ECB/PKCS5Padding");
    public XifratgeSimetric() throws NoSuchAlgorithmException, NoSuchPaddingException {
    }

    public byte[] xifratgeSimetric(String sms) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] missatge = sms.getBytes();
        xifradorDES.init(Cipher.ENCRYPT_MODE, clauDES);
         // xifratge del missatge
        byte[] missatgeXifrat = xifradorDES.doFinal(missatge);
        return missatgeXifrat;
    }

    public String desxifraSimetric(byte[] sms)  throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        xifradorDES.init(Cipher.DECRYPT_MODE, clauDES);
        byte[] missatgeDes = xifradorDES.doFinal(sms);
        return new String(missatgeDes);
    }
}
