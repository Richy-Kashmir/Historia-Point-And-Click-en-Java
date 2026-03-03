package herramientas;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
public class TransformadorImagenes {

	public static BufferedImage escalarImagen(BufferedImage imagenOriginal, double proporcionX, double proporcionY) {
		
		int nuevoAncho = imagenOriginal.getWidth() / 2; // Reduce el ancho a la mitad
		int nuevoAlto = imagenOriginal.getHeight() / 2; // Reduce el alto a la mitad
		
	    BufferedImage imagenEscalada = new BufferedImage(nuevoAncho, nuevoAlto, BufferedImage.TYPE_INT_ARGB); // Crea una nueva imagen con el nuevo tamaño
	    
	    AffineTransform transformador = new AffineTransform();
	    transformador.scale(proporcionX, proporcionY); // Aplica la escala a la transformación
	    
	    AffineTransformOp operacion = new AffineTransformOp(transformador, AffineTransformOp.TYPE_BILINEAR); // Crea un operador de transformación con la transformación definida
	    
	    imagenEscalada = operacion.filter(imagenOriginal, imagenEscalada); // Aplica la transformación a la imagen original y guarda el resultado en la nueva imagen
	    	    
	    return imagenEscalada; // Devuelve la imagen escalada
		
	}
	
	
	// Sobrecarga del método para usar la misma proporción en ambos ejes
	
public static BufferedImage escalarImagen(BufferedImage imagenOriginal, double proporcion) {
		
		int nuevoAncho = imagenOriginal.getWidth() / 2; // Reduce el ancho a la mitad
		int nuevoAlto = imagenOriginal.getHeight() / 2; // Reduce el alto a la mitad
		
	    BufferedImage imagenEscalada = new BufferedImage(nuevoAncho, nuevoAlto, BufferedImage.TYPE_INT_ARGB); // Crea una nueva imagen con el nuevo tamaño
	    
	    AffineTransform transformador = new AffineTransform();
	    transformador.scale(proporcion, proporcion); // Aplica la escala a la transformación
	    
	    AffineTransformOp operacion = new AffineTransformOp(transformador, AffineTransformOp.TYPE_BILINEAR); // Crea un operador de transformación con la transformación definida
	    
	    imagenEscalada = operacion.filter(imagenOriginal, imagenEscalada); // Aplica la transformación a la imagen original y guarda el resultado en la nueva imagen
	    	    
	    return imagenEscalada; // Devuelve la imagen escalada
		
	}
	
}
