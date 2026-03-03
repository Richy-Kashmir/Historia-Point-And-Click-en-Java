package Graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import herramientas.TransformadorImagenes;
import herramientas.CargadorRecursos;
import control.Raton;
import control.raton;

public class Dibujo extends Canvas {

    private static final long serialVersionUID = 1L;
    private BufferStrategy buffer;
    private Graphics graficos;
    private BufferedImage imagen;
    private Raton raton;
    

    public Dibujo(int ancho, int alto) {
        setPreferredSize(new Dimension(ancho, alto));
        imagen = CargadorRecursos.cargarImagen("recursos/imagenes/detective.jpeg"); // carga tu rojo.png o Oficina.jpeg
   
        imagen = TransformadorImagenes.escalarImagen(imagen, 0.5); // Escala la imagen a la mitad de su tamaño original
    
        raton = new Raton(this); // Inicializa el objeto ratón para rastrear la posición del cursor
    }

    public void actualizar() {
    	raton.actualizar(this); //
    	
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
        if (imagen != null) {
            graficos.drawImage(imagen, 0, 0, null);
        }
       
        raton.dibujar(graficos); // Dibuja la posición del ratón en la pantalla
        graficos.dispose();
        buffer.show();
    }
}