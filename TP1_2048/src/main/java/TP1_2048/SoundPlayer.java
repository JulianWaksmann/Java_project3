package TP1_2048;

import javax.sound.sampled.*;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {
    // HashMap para mantener los clips cargados con nombres como clave
    private static Map<String, Clip> clipMap = new HashMap<>();

    // Método para cargar un Clip y agregarlo al HashMap
    public static void addClip(String resourceName, String clipName) {
        try {
            // Cargar el archivo de sonido desde el recurso de la aplicación
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResourceAsStream(resourceName));

            // Obtener el formato del audio
            AudioFormat format = audioInputStream.getFormat();

            // Crear un DataLine.Info para reproducir el audio
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            // Obtener el Clip y abrirlo
            Clip clip = (Clip) AudioSystem.getLine(info);
            clip.open(audioInputStream);

            // Agregar el Clip al HashMap con el nombre proporcionado
            clipMap.put(clipName, clip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para reproducir un Clip por su nombre
    public static void playClip(String clipName) {
        Clip clip = clipMap.get(clipName);
        if (clip != null) {
            clip.setFramePosition(0); // Reiniciar la posición del clip
            clip.start(); // Iniciar la reproducción
        }
    }
    
    public static void stopClip(String clipName) {
        Clip clip = clipMap.get(clipName);
        if (clip != null) {
            clip.stop(); // Detener la reproducción del clip
        }
    }
}
