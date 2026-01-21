package com.alessandro.astages.api;

import com.alessandro.astages.AStages;
import com.alessandro.astages.api.nullability.NotNullParamsAndMethodsReturn;
import com.alessandro.astages.api.nullability.Nullable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@NotNullParamsAndMethodsReturn
public class AFileIOUtils {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

    public static final String JSON_EXTENSION = ".json";

    public static boolean fileExists(Path file) {
        return Files.exists(file);
    }

    public static void createDirectory(Path dir) {
        if (Files.notExists(dir)) {
            try {
                Files.createDirectories(dir);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }
    }

    public static Path getOrCreateFile(Path file) {
        if (Files.notExists(file)) {
            try {
                // Create file only if DOESN'T exist
                Files.createFile(file);
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }

        return file;
    }

    public static <E> List<E> readListOrDefault(Path file, Class<E> elementClazz) {
        var content = readList(file, elementClazz);
        return content == null ? new ArrayList<>() : content;
    }

    public static <E> @Nullable List<E> readList(Path file, Class<E> elementClazz) {
        return readFileContent(file, TypeToken.getParameterized(List.class, elementClazz).getType());
    }

    public static <E> HashSet<E> readHashSetOrDefault(Path file, Class<E> elementClazz) {
        var content = readHashSet(file, elementClazz);
        return content == null ? new HashSet<>() : content;
    }

    public static <E> @Nullable HashSet<E> readHashSet(Path file, Class<E> elementClazz) {
        return readFileContent(file, TypeToken.getParameterized(HashSet.class, elementClazz).getType());
    }

    public static <K, V> Map<K, V> readMapOrDefault(Path file, Class<K> keyClazz, Class<V> valueClazz) {
        var content = readMap(file, keyClazz, valueClazz);
        return content == null ? new HashMap<>() : content;
    }

    public static <K, V> @Nullable Map<K, V> readMap(Path file, Class<K> keyClazz, Class<V> valueClazz) {
        return readFileContent(file, TypeToken.getParameterized(Map.class, keyClazz, valueClazz).getType());
    }

    public static <T> @Nullable T readFileContent(Path file, Type type) {
        try (var fileReader = Files.newBufferedReader(file)) {
            return GSON.fromJson(fileReader, type);
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }

        return null;
    }

    public static <T> void writeFileContent(Path file, T content) {
        try (var fileWriter = Files.newBufferedWriter(file)) {
            GSON.toJson(content, fileWriter);
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }
    }

    public static void deleteFile(Path file) {
        try {
            Files.deleteIfExists(file);
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }
    }

    public static void deleteDirectory(Path directory) {
        deleteFile(directory);
    }

    public static boolean directoryHasNoFilesWithExtension(Path directory, String extension) {
        if (!Files.isDirectory(directory)) return true;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory, "*" + extension)) {
            return !stream.iterator().hasNext();
        } catch (IOException exception) {
            AStages.LOGGER.error(exception.getLocalizedMessage());
        }

        return false;
    }

    public static CompoundTag readFileNbt(Path file) {
        return Legacy.readFileNbt(file.toFile());
    }

    public static void writeFileNbt(CompoundTag tag, Path file) {
        Legacy.writeFileNbt(tag, file.toFile());
    }

    public static class Legacy {
        public static void createDirectory(File dir) {
            if (!dir.exists()) {
                boolean created = dir.mkdirs();
                if (!created) {
                    AStages.LOGGER.error("Impossible to make directory: {} ", dir.getAbsolutePath());
                }
            }
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        public static File getOrCreateFile(File file) {
            if (!file.exists()) {
                try {
                    // Create file only if DOESN'T exist
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            return file;
        }

        public static <E> List<E> readListOrDefault(File file, Class<E> elementClazz) {
            var content = readList(file, elementClazz);
            return content == null ? new ArrayList<>() : content;
        }

        public static <E> @Nullable List<E> readList(File file, Class<E> elementClazz) {
            return readFileContent(file, TypeToken.getParameterized(List.class, elementClazz).getType());
        }

        public static <K, V> Map<K, V> readMapOrDefault(File file, Class<K> keyClazz, Class<V> valueClazz) {
            var content = readMap(file, keyClazz, valueClazz);
            return content == null ? new HashMap<>() : content;
        }

        public static <K, V> @Nullable Map<K, V> readMap(File file, Class<K> keyClazz, Class<V> valueClazz) {
            return readFileContent(file, TypeToken.getParameterized(Map.class, keyClazz, valueClazz).getType());
        }

        public static <T> @Nullable T readFileContent(File file, Type type) {
            try (var fileReader = new FileReader(file)) {
                return GSON.fromJson(fileReader, type);
            } catch (IOException exception) {
                AStages.LOGGER.error(exception.getLocalizedMessage());
            }

            return null;
        }

        public static <T> void writeFileContent(File file, T content) {
            try (var fileWriter = new FileWriter(file)) {
                GSON.toJson(content, fileWriter);
            } catch (IOException exception) {
                AStages.LOGGER.error(exception.getLocalizedMessage());
            }
        }

        @SuppressWarnings("ResultOfMethodCallIgnored")
        public static void deleteFile(File file) {
            file.delete();
        }

        public static CompoundTag readFileNbt(File file) {
            CompoundTag tag = null;
            try { tag = NbtIo.read(file.toPath()); } catch (IOException ignoredException) { }
            if (tag == null) { tag = new CompoundTag(); }

            return tag;
        }

        public static void writeFileNbt(CompoundTag tag, File file) {
            try {
                NbtIo.write(tag, file.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
