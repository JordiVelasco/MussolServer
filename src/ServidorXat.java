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
			System.out.println("Servidor Individual o Grupal");
			Scanner sn = new Scanner(System.in);
			String Server = sn.nextLine();
			if (Server.contains("Grupal")){
				System.out.println("Benvingut al servidor de xat grupal");
				while (true) {
					Socket socolEntrant = socolServidor.accept();
					socols.add(socolEntrant);
					System.out.println("client " + i);
					Runnable r = new FilXatGrupal(socolEntrant,"User " + i);
					Thread fil = new Thread(r);
					fil.start();
					i++;
				}
			} else if (Server.contains("Individual")){
				System.out.println("Benvingut al servidor de xat individual");
				socols.clear();
				while (true) {
					int size = socols.size();
					if (socolServidor.isClosed()) socolServidor = new ServerSocket(8189);
					Socket socolEntrant = socolServidor.accept();
					if (size <= 1) {
						socols.add(socolEntrant);
						System.out.println("client " + i);
						Runnable r = new FilXatIndividual(socolEntrant, i);
						Thread fil = new Thread(r);
						fil.start();
						i++;
					} else {
						socols.remove(socolEntrant);
						Runnable r = new ServidorPle(socolEntrant, i);
						Thread fil = new Thread(r);
						fil.start();
						System.out.println("hola");
					}
				}
			} else {
				System.out.println("No has introduit un servidor existent");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
class ServidorPle implements  Runnable{
	private Socket socolEntrant;
	private int num;

	public ServidorPle(Socket i, int aNom) {
		socolEntrant = i;
		num = aNom;
		System.out.println(socolEntrant + "socol");
	}

	@Override
	public void run() {
		OutputStream out = null;
		try {
			out = socolEntrant.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter sor = new PrintWriter(out, true /* autoFlush */);
		sor.println("SERVIDOR PLE");
		try {
			socolEntrant.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class FilXatIndividual implements Runnable {
	private Socket socolEntrant;
	private int num;

	public FilXatIndividual(Socket i, int aNom) {
		socolEntrant = i;
		num = aNom;
		System.out.println(socolEntrant + "socol");
	}

	public void run() {
		XifratgeSimetric xsDES;
		try {
			xsDES = new XifratgeSimetric();
			try {
				InputStream inStream = socolEntrant.getInputStream();
				Scanner entrada = new Scanner(inStream);
				boolean fet = false;
				while (!fet && entrada.hasNextLine()) {
					String missatge = entrada.nextLine();
					byte[] textXifrat = xsDES.xifratgeSimetric(missatge);
					textXifrat.toString();
					for(Socket item: ServidorXat.socols){
						OutputStream out = item.getOutputStream();
						PrintWriter sor = new PrintWriter(out, true /* autoFlush */);
						String textDesxifrat = xsDES.desxifraSimetric(textXifrat);
						sor.println("Usuari " + num + ": "+ textDesxifrat);
					}
					if (missatge.trim().equals("FINAL")){
						fet = true;
						ServidorXat.socols.remove(socolEntrant);
						num--;
					}
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

class FilXatGrupal implements Runnable {
	private Socket socolEntrant;
	private String nom;

	public FilXatGrupal(Socket i, String aNom) {
		socolEntrant = i;
		nom = aNom;
		System.out.println(socolEntrant + "socol");
	}

	public void run() {
		XifratgeSimetric xsDES;
		try {
			xsDES = new XifratgeSimetric();
			try {
				InputStream inStream = socolEntrant.getInputStream();
				Scanner entrada = new Scanner(inStream);
				boolean fet = false;
				while (!fet && entrada.hasNextLine()) {
					String missatge = entrada.nextLine();
					byte[] textXifrat = xsDES.xifratgeSimetric(missatge);
					textXifrat.toString();
					for (Socket item : ServidorXat.socols) {
						OutputStream out = item.getOutputStream();
						PrintWriter sor = new PrintWriter(out, true /* autoFlush */);
						String textDesxifrat = xsDES.desxifraSimetric(textXifrat);
						sor.println(nom + ": " + textDesxifrat);
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
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}
}