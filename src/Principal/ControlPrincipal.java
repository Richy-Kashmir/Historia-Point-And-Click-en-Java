package Principal;
import Graficos.Dibujo;
import Graficos.Ventana;

public class ControlPrincipal {
	private static boolean enfuncionamiento = false; // Variable para controlar el estado del programa
	
	
	static void iniciar() {
		enfuncionamiento = true;
		inicializar();
	}
	
	private static void detener() {
		enfuncionamiento = false;
		System.exit(0); // Termina el programa
	}
	
	private static void inicializar() {
		Dibujo dibujo = new Dibujo(800, 600); // Crea un nuevo objeto de la clase Dibujo con un tamaño de 800x600 píxeles
		new Ventana("Detective Misterioso", dibujo); // Crea una nueva ventana con el título "Point Click" y la superficie de dibujo creada anteriormente
	}
}
