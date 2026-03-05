package control;
import java.awt.event.MouseAdapter;

import javax.swing.SwingUtilities;

import Graficos.Dibujo;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.MouseInfo;
public class Raton  extends MouseAdapter {
	
	private static Point posicion;
	
	public Raton(final Dibujo sd) {
		posicion = new Point(); // Inicializa la posición del ratón en (0, 0)
		
		actualizarPosicion(sd); // Llama al método para actualizar la posición del ratón con la posición actual del cursor
	}
	
	public void actualizar(final Dibujo sd) {
		actualizarPosicion(sd); // Actualiza la posición del ratón cada vez que se llama a este método
	}
	
	public void dibujar(Graphics g) {
		g.setColor(java.awt.Color.RED); // Establece el color de dibujo a rojo
		g.drawString("X: " + getPosicion().getX(),10,10 ); // Dibuja un círculo rojo en la posición del ratón
		g.drawString("Y: " + getPosicion().getX(),10,20 );
	}
	
	
	// Este método se llamará para actualizar la posición del ratón
	private static void actualizarPosicion(final Dibujo sd) {
		final Point posicionInicial = MouseInfo.getPointerInfo().getLocation(); // Obtiene la posición actual del ratón							

		SwingUtilities.convertPointFromScreen(posicionInicial, sd);
		
		posicion.setLocation(posicionInicial.getX(), posicionInicial.getY()); // Actualiza la posición del ratón con la nueva posición obtenida
	}
	
	public Point getPosicion() {
		return posicion; // Devuelve la posición actual del ratón
	}
}
