package Principal;
import Graficos.Dibujo;
import Graficos.Ventana;

public class ControlPrincipal {
	private static boolean enfuncionamiento = false; // Variable para controlar el estado del programa
	private static Dibujo dibujo; // Variable para almacenar la superficie de dibujo
	private  static int aps = 0; // Variable para contar las actualizaciones por segundo
	private static int fps = 0; // Variable para contar los fotogramas por segundo
	
	
	static void iniciar() {
		enfuncionamiento = true;
		inicializar();
	}
	
	@SuppressWarnings("unused")
	private static void detener() {
		
		if (dibujo != null) {
	        dibujo.detenerMusica();
	    }
		enfuncionamiento = false;
		System.exit(0); // Termina el programa
	}
	
	private static void inicializar() {
		long tiempoActual = System.currentTimeMillis();
	    dibujo = new Dibujo(800, 600, tiempoActual); // ahora sí asigna a la variable estática
	    new Ventana("El Cuervo", dibujo);

	    // Forzar creación del BufferStrategy después de que la ventana sea visible
	    dibujo.createBufferStrategy(3);
	}
	
	private static void actualizar() {
	
		dibujo.actualizar(); // Llama al método actualizar de la superficie de dibujo para realizar las operaciones de actualización
		
		if (!dibujo.cambioRealizado) {
            long tiempoTranscurrido = System.currentTimeMillis() - dibujo.getTiempoInicio();
            if (tiempoTranscurrido >= 6000) { // 6000 ms = 6 segundos
                dibujo.cambiarAImagenSecundaria();
            }
        }
		aps++; // Incrementa el contador de actualizaciones por segundo
		
	}
	
	private static void dibujar() {
		// Aquí se pueden agregar las operaciones de dibujo necesarias para el programa
		dibujo.dibujar(); // Llama al método dibujar de la superficie de dibujo para realizar las operaciones de dibujo
		fps++; // Incrementa el contador de fotogramas por segundo
	}
	static void ejecutar() {

	    final int NS_POR_SEGUNDO = 1000000000; 
	    final int APS_OBJETIVO = 60; // objetivo de actualizaciones por segundo
	    final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

	    long referenciaActualizacion = System.nanoTime(); 
	    long referenciaContador = System.nanoTime();

	    double delta = 0; 

	    aps = 0;
	    fps = 0;

	    while (enfuncionamiento) {

	        long tiempoActual = System.nanoTime();
	        delta += (tiempoActual - referenciaActualizacion) / NS_POR_ACTUALIZACION;
	        referenciaActualizacion = tiempoActual;

	        // SOLO actualizamos si delta >= 1, garantizando máximo 60 APS
	        while (delta >= 1) {
	            actualizar(); // APS limitado
	            delta--;
	        }

	        dibujar(); // dibuja tanto como pueda, no afecta APS

	        // Contador de FPS/APS por segundo
	        if (System.nanoTime() - referenciaContador >= NS_POR_SEGUNDO) {
	            System.out.println("APS: " + aps + " | FPS: " + fps);
	            aps = 0; // reinicia contador cada segundo
	            fps = 0;
	            referenciaContador = System.nanoTime();
	        }
	    }
	}
}