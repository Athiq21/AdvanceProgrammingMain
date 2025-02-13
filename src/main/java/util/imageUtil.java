package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
public class imageUtil {

    private static final String IMAGE_PATH = "/Users/athiq/Downloads/images";  // Specify your image storage path

    // Method to save image from Base64 string
    public String saveImage(String imageBase64) throws IOException {
        // Ensure image data is not null or empty
        if (imageBase64 == null || imageBase64.isEmpty()) {
            throw new IllegalArgumentException("Image data is required");
        }

        // Handle image in Base64 format, typically of the form: "data:image/png;base64,ABC123=="
        if (imageBase64.startsWith("data:image")) {
            imageBase64 = imageBase64.split(",")[1]; // Remove data URL prefix
        }

        // Decode the Base64 string into a byte array
        byte[] imageBytes = java.util.Base64.getDecoder().decode(imageBase64);

        // Create a unique file name
        String fileName = "offer_" + System.currentTimeMillis() + ".png";

        // Define the target file path
        Path targetPath = Paths.get(IMAGE_PATH, fileName);

        // Save the image to the specified path
        try (OutputStream outputStream = Files.newOutputStream(targetPath)) {
            outputStream.write(imageBytes);
        }

        return fileName;  // Return the filename to be stored in the database
    }
}
