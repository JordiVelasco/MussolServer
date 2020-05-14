import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * NOTES
 * -manca definir un nick per cada client
 * -manca controlar el tancament del socol de lectura
 */
public class ClientXat {

	public static void main(String[] args) {

		InetAddress adrecaHost = null;
		if (args.length == 0) {
			try {
				adrecaHost = InetAddress.getLocalHost();

			} catch (UnknownHostException e) {
				System.out.println(" no es troba host");
			}

		} else if (args.length == 1) {
			byte[] IP = new byte[4];
			String[] ipString = args[0].split("\\.");
			for (int i = 0; i < 4; i++) {
				IP[i] = (byte) Integer.parseInt(ipString[i]);
			}

			try {
				adrecaHost = InetAddress.getByAddress(IP);

			} catch (UnknownHostException e) {
				System.out.println(" no es troba host");
			}
		} else {
			System.out.println("nombre de parametres incorrecte");
			System.exit(1);
		}

		LecturaFil lectura = new LecturaFil(adrecaHost);
		lectura.start();
		EscripturaFil escriptura = new EscripturaFil(adrecaHost);
		escriptura.start();

	}
}

class LecturaFil extends Thread {
	private Socket socol = null;
	public LecturaFil(InetAddress adrecaHost) {
		socol = new Socket();
		try {
			socol.connect(new InetSocketAddress(adrecaHost.getHostAddress(),
					8189), 2000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {

		try {

			InputStream inStream = socol.getInputStream();
			Scanner entrada = new Scanner(inStream);
			while (true) {
				String resposta = entrada.nextLine();
				System.out.println("\nSERVER> " + resposta);

			}

		} catch (UnknownHostException e) {
			System.out.println("host desconegut");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("problemes E/S");
		}

	}

}

class EscripturaFil extends Thread {
	private Socket socol = null;

	public EscripturaFil(InetAddress adrecaHost) {
		socol = new Socket();
		try {
			socol.connect(new InetSocketAddress(adrecaHost.getHostAddress(),
					8189), 2000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		//FuncioResumMD5Fitxer hashMsg = new FuncioResumMD5Fitxer();
		XifratgeSimetric xsDES = null;
		try {
			xsDES = new XifratgeSimetric();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
		try {
			OutputStream outStream = socol.getOutputStream();
			PrintWriter sortida = new PrintWriter(outStream, true);
			Scanner entradaTeclat = new Scanner(System.in);
			String missatge = "";
			sortida.write(missatge);
			do {
				System.out.print("Entra missatge: ");
				missatge = entradaTeclat.nextLine();
				byte[] textXifrat = xsDES.xifratgeSimetric(missatge);
				//hashMsg.generarMD5(a);
				String text = textXifrat.toString();
				sortida.println(text);
				System.out.println(text);
				String textDesxifrat = xsDES.desxifraSimetric(textXifrat);
				sortida.println(textDesxifrat);
				System.out.println(textDesxifrat);
			} while (!missatge.equals("FINAL"));
			socol.close();
		} catch (UnknownHostException e) {
			System.out.println("host desconegut");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("problemes E/S");
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}

	}

}