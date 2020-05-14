import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
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
		try {
			try {
				InputStream inStream = socolEntrant.getInputStream();
				Scanner entrada = new Scanner(inStream);
				OutputStream outStream = socolEntrant.getOutputStream();
				PrintWriter sortida = new PrintWriter(outStream, true /* autoFlush */);
				sortida.println("sdafweqfefewffewfewf");
				sortida.println("Hola, asdfas");
				sortida.println("sadas per acabar");
				sortida.println("aeqerqw");
				sortida.println("a");


				// echo client input
				boolean fet = false;
				while (!fet && entrada.hasNextLine()) {
					String linia = entrada.nextLine();
					for(Socket item: ServidorXat.socols){
						OutputStream out = item.getOutputStream();
						PrintWriter sor = new PrintWriter(out, true /* autoFlush */);
						sor.println(linia);
					}
				
					if (linia.trim().equals("FINAL"))
						fet = true;
				}
			} finally {
				socolEntrant.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
}