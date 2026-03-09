package Graficos;

import java.awt.BorderLayout;
import java.awt.Canvas;

import javax.swing.JFrame;

public class Ventana extends JFrame {

	private static final long serialVersionUID = 1L;

	public Ventana(String nombre, Canvas superficieDibujo) {        // Canvas es una clase especializada en recibir dibujos y crear graficos  
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);       // Para cerra  la ventana
		this.setTitle(nombre);                                    // Da titulo a la ventana
		this.setResizable(false);                                // Para que la pantalla no cambie de tamaño
		this.setLayout(new BorderLayout());                     // Es ordenar los componentes dentro de la ventana
		if (superficieDibujo != null) {                       
            this.add(superficieDibujo, BorderLayout.CENTER);   // Condición para permitir null inicialmente
        }                     
		this.pack();                                          // Ajusta el tamaño de la ventana al tamaño de su contenido
		this.setLocationRelativeTo(null);                    // Centra la ventana en la pantalla
		this.setVisible(true);                              // Hace que la ventana sea visible
	}

}