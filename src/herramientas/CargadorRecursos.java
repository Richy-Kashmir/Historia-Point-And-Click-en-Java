package herramientas;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class CargadorRecursos {

    public static BufferedImage cargarImagen(String ruta) {
        BufferedImage imagenTemporal = null;

        try {
            // Ruta relativa a tu proyecto
            imagenTemporal = ImageIO.read(new File(ruta));
            if (imagenTemporal == null) {
                System.err.println("No se pudo cargar la imagen: revisar la ruta o el nombre del archivo");
            }
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: ");
            e.printStackTrace();
        }

        return imagenTemporal;
    }
}