package org.ruoland.dictionary.dictionary.dictionary.developer.category;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Nullable;
import org.ruoland.dictionary.Dictionary;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public interface Data {

    Path DIRECTORY_PATH = Paths.get("./dictionary");
    Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Nullable
    static <T> Object readJson(Path path, Class<T> tC) {
        try {
            String content = Files.readString(path);
            return GSON.fromJson(content, tC);
        } catch (Exception e) {
            Dictionary.LOGGER.error("Error reading file: {}, {}", path, e);
            throw new RuntimeException(e);
        }

    }

    static <T> void saveJson(Path path, Object obj) {
        try {
            String json = GSON.toJson(obj);
            Files.writeString(path, json);
        } catch (IOException e) {
            Dictionary.LOGGER.error("Error writing file: {}", path, e);
            throw new RuntimeException(e);
        }
    }
    default void init() {
        try {
            Files.createDirectories(DIRECTORY_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void createFolder(Path createFile) {
        try {
            Files.createDirectories(DIRECTORY_PATH.resolve(createFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default void createFile(Path createFile) {
        try {
            Path file =DIRECTORY_PATH.resolve(createFile);
            if(!file.toFile().isFile())
                Files.createFile(DIRECTORY_PATH.resolve(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
