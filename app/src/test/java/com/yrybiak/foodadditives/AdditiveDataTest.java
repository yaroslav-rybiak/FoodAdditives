package com.yrybiak.foodadditives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
 * Checks the bundled additive data: every file parses, every entry is complete, and each
 * translation lists exactly the same additives in the same order as the default language.
 * Screens address an additive by its position in the file, so the order must match.
 */
public class AdditiveDataTest {

    // Gradle runs unit tests with the module directory as the working directory.
    private static final File DEFAULT_DIR = new File("src/main/res/raw");
    private static final List<File> TRANSLATION_DIRS = Arrays.asList(new File("src/main/res/raw-ru"));

    private static final Pattern CODE = Pattern.compile("E\\d{3,4}[a-z]?");
    private static final Set<String> KEYS = new HashSet<>(Arrays.asList("code", "name", "text"));

    @Test
    public void defaultLanguageFilesAreComplete() throws IOException, JSONException {
        Set<String> seenCodes = new HashSet<>();
        for (String fileName : dataFileNames(DEFAULT_DIR)) {
            for (String code : checkedCodes(new File(DEFAULT_DIR, fileName))) {
                assertTrue(code + " appears more than once", seenCodes.add(code));
            }
        }
        assertFalse("No additives found in " + DEFAULT_DIR, seenCodes.isEmpty());
    }

    @Test
    public void translationsMatchDefaultLanguage() throws IOException, JSONException {
        for (File translationDir : TRANSLATION_DIRS) {
            assertEquals("Files in " + translationDir,
                    dataFileNames(DEFAULT_DIR), dataFileNames(translationDir));
            for (String fileName : dataFileNames(DEFAULT_DIR)) {
                assertEquals("Codes in " + new File(translationDir, fileName),
                        checkedCodes(new File(DEFAULT_DIR, fileName)),
                        checkedCodes(new File(translationDir, fileName)));
            }
        }
    }

    private static Set<String> dataFileNames(File dir) {
        String[] names = dir.list((d, name) -> name.startsWith("additives_") && name.endsWith(".json"));
        assertTrue("No data files in " + dir.getAbsolutePath(), names != null && names.length > 0);
        return new TreeSet<>(Arrays.asList(names));
    }

    /** Validates every entry of {@code file} and returns the codes in file order. */
    private static List<String> checkedCodes(File file) throws IOException, JSONException {
        JSONArray entries = new JSONArray(
                new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8));
        List<String> codes = new ArrayList<>();
        for (int i = 0; i < entries.length(); i++) {
            JSONObject entry = entries.getJSONObject(i);
            String where = file + " entry " + i;
            Set<String> keys = new HashSet<>();
            entry.keys().forEachRemaining(keys::add);
            assertEquals(where, KEYS, keys);
            String code = entry.getString("code");
            assertTrue(where + ": bad code " + code, CODE.matcher(code).matches());
            assertFalse(where + ": empty name", entry.getString("name").trim().isEmpty());
            assertFalse(where + ": empty text", entry.getString("text").trim().isEmpty());
            codes.add(code);
        }
        assertFalse(file + " has no entries", codes.isEmpty());
        return codes;
    }
}
