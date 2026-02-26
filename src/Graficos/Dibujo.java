package Graficos;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Dibujo extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; // Un ID de serie para la clase Dibujo, utilizado para la serialización de objetos
	private BufferStrategy buffer; // Es una clase que se utiliza para gestionar el almacenamiento, es decir liberar recursos
	private Graphics graficos; // Es una clase que permite dibujar formas, texto e imágenes en componentes gráficos
	
	// Constructor
	
	public Dibujo(int ancho, int alto) { // Recibe el ancho y alto en el "lienzo" de dibujo
		setPreferredSize(new Dimension(ancho, alto)); // Establece el tamaño de dibujo
	}
	
	public void dibujar() {
		buffer = getBufferStrategy(); // Obtiene el almacenamiento en canvas
		
		if (buffer == null) { // Si el buffer no tiene valor, se crea un nuevo buffer con 3 capas
			createBufferStrategy(3);
			return;
		}
		
		graficos = buffer.getDrawGraphics(); // Obtiene el objeto para dibujar en él
		graficos.dispose(); // Libera los recursos del objeto 
		buffer.show(); // Muestra el contenido del buffer en la pantalla
	}
}
