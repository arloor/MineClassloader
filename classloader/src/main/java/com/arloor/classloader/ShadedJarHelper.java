package com.arloor.classloader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * 从resource中拿到shaded jar
 */
public class ShadedJarHelper {
    public static File prepareTempJar() {
        final String resourceName = "InnerModule.jar.resource";
        final InputStream inputStream = ShadedJarHelper.class.getClassLoader().getResourceAsStream(resourceName);
        if (inputStream != null) {
            try {
                File file = File.createTempFile("InnerModule-", ".jar");
                System.err.println(String.format("copy shaded.jar from %s to %s", resourceName, file.getAbsolutePath()));
                Files.copy(inputStream, file.toPath(), REPLACE_EXISTING);
                return file;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.err.println("error close input stream of shaded.jar resource");
                }
            }
        }
        return null;
    }
}
