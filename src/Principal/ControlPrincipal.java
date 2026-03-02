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
		crearVentana(800, 600, "Detective Misterioso"); // Crea una nueva ventana con el título "Point Click" y la superficie de dibujo creada anteriormente
	}
	
	private static void crearVentana(int ancho, int alto, String nombre) {
		Dibujo dibujo = new Dibujo(ancho, alto); // Crea una nueva superficie de dibujo con el tamaño especificado
		Ventana ventana = new Ventana(nombre, dibujo); // Crea una nueva ventana con el título y la superficie de dibujo
		
	}
	
	private static void actualizar() {
		
	}
	
	private static void disegnar() {
	
	}
}
