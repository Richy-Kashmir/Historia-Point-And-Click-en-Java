package Graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import control.Raton;
import herramientas.CargadorRecursos;
import herramientas.Sonido;
import herramientas.TransformadorImagenes;

public class Dibujo extends Canvas {

    private static final long serialVersionUID = 1L;
    private BufferStrategy buffer;
    private Graphics graficos;
    private BufferedImage imagen;
    private Raton raton;
    private BufferedImage imagenInicial;
    private BufferedImage imagenSecundaria;
    private BufferedImage imagenActual;
    public boolean cambioRealizado = false;
	private long tiempoInicio;
	private Sonido musicaFondo;
    

    public Dibujo(int ancho, int alto, long tiempoInicio) {
        setPreferredSize(new Dimension(ancho, alto));
        this.tiempoInicio = tiempoInicio;
        
        
        imagenInicial = CargadorRecursos.cargarImagen("recursos/imagenes/Presentacion.jpg");
        if (imagenInicial != null) {
            imagenInicial = TransformadorImagenes.escalarImagen(imagenInicial, 0.5);
        }

        imagenSecundaria = CargadorRecursos.cargarImagen("recursos/imagenes/Menu.jpg");
        if (imagenSecundaria != null) {
            imagenSecundaria = TransformadorImagenes.escalarImagen(imagenSecundaria, 0.5);
        }
        imagenActual = imagenInicial; // Comienza con la imagen inicial
        musicaFondo = new Sonido("recursos/musica/Custodes Abyssi.wav"); // Carga la música de fondo
        raton = new Raton(this); // Inicializa el objeto ratón para rastrear la posición del cursor
    }

    public void actualizar() {
    	raton.actualizar(this); //
    	
	}
    
    public void cambiarAImagenSecundaria() {
        imagenActual = imagenSecundaria;
        cambioRealizado = true;
        
        if (musicaFondo != null) { // Reproduce la música de fondo en loop infinito
            musicaFondo.reproducir(true);  // true = loop infinito

        }
    }
    
    public long getTiempoInicio() {
        return tiempoInicio;
    }
    
    
    public void detenerMusica() {
        if (musicaFondo != null) {
            musicaFondo.detener();
        }
    }
    
    public void dibujar() {
        buffer = getBufferStrategy();
        if (buffer == null) {
            createBufferStrategy(3);
            return;
        }

        graficos = buffer.getDrawGraphics();
        // primero el fondo
        graficos.setColor(Color.BLACK);
        graficos.fillRect(0, 0, 800, 600);

        // luego la imagen
        if (imagenActual != null) {
            graficos.drawImage(imagenActual, 0, 0, null);
        }
       
        raton.dibujar(graficos); // Dibuja la posición del ratón en la pantalla
        graficos.dispose();
        buffer.show();
    }
}