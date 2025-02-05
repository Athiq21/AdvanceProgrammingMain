package util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Imageheadache {
    private static final String IMAGE_DIRECTORY = "webapp/images/";

    public String saveImage(InputStream imageInputStream, String imageName) throws IOException {
        File imagesDir = new File(IMAGE_DIRECTORY);
        if (!imagesDir.exists()) {
            imagesDir.mkdirs();
            String imagePath = IMAGE_DIRECTORY + imageName;
            Files.copy(imageInputStream, Paths.get(imagePath));

            return imagePath;
        }
return imagesDir.getAbsolutePath();
    }
}
