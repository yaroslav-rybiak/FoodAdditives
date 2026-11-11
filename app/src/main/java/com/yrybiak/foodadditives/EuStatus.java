package com.yrybiak.foodadditives;

import android.content.res.Resources;

import androidx.annotation.RawRes;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * Which additives are on the EU's list of authorised food additives (Annex II, Part B of
 * Regulation (EC) No 1333/2008). The list lives in res/raw/eu_authorised.json and is the same
 * for every language.
 */
final class EuStatus {

    private static Set<String> authorised;

    private EuStatus() {
    }

    static synchronized boolean isAuthorised(Resources resources, String code) {
        if (authorised == null) {
            try {
                JSONArray codes = readJsonArray(resources, R.raw.eu_authorised);
                Set<String> set = new HashSet<>();
                for (int i = 0; i < codes.length(); i++) {
                    set.add(codes.getString(i));
                }
                authorised = set;
            } catch (IOException | JSONException e) {
                // The file is bundled with the app, so this is a packaging bug.
                throw new IllegalStateException("Cannot read the EU list", e);
            }
        }
        return authorised.contains(code);
    }

    /** Reads a bundled raw resource that contains a JSON array. */
    static JSONArray readJsonArray(Resources resources, @RawRes int id)
            throws IOException, JSONException {
        try (InputStream input = resources.openRawResource(id)) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) != -1) {
                bytes.write(buffer, 0, read);
            }
            return new JSONArray(new String(bytes.toByteArray(), StandardCharsets.UTF_8));
        }
    }
}
