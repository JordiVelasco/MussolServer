import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

public class ServidorXat {
	public static ArrayList<Socket> socols = new ArrayList<Socket>(); 
	public static void main(String[] args) {

		try {
			int i = 1;
			ServerSocket socolServidor = new ServerSocket(8189);

			while (true) {
				Socket socolEntrant = socolServidor.accept();
				socols.add(socolEntrant);
				System.out.println("client " + i);
				Runnable r = new FilXat(socolEntrant,"client " + i);
				Thread fil = new Thread(r);
				fil.start();
				i++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class FilXat implements Runnable {
	private Socket socolEntrant;
	private String nom;

	public FilXat(Socket i, String aNom) {
		socolEntrant = i;
		nom=aNom;
	}

	public void run() {
		XifratgeSimetric xsDES = null;
		try {
			xsDES = new XifratgeSimetric();
			try {
				InputStream inStream = socolEntrant.getInputStream();
				Scanner entrada = new Scanner(inStream);
				boolean fet = false;
				while (!fet && entrada.hasNextLine()) {
					String missatge = entrada.nextLine();
					byte[] textXifrat = xsDES.xifratgeSimetric(missatge);
					String text = textXifrat.toString();
					for(Socket item: ServidorXat.socols){
						OutputStream out = item.getOutputStream();
						PrintWriter sor = new PrintWriter(out, true /* autoFlush */);
						String textDesxifrat = xsDES.desxifraSimetric(textXifrat);
						sor.println(textDesxifrat + "text desxifrat");
					}
					if (missatge.trim().equals("FINAL"))
						fet = true;
				}
			} catch (BadPaddingException e) {
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} finally {
				socolEntrant.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}


}