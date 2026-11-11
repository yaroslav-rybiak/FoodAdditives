package com.yrybyak.foodadditives;

import android.content.res.Resources;

import androidx.annotation.RawRes;
import androidx.annotation.StringRes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** A range of E numbers (e.g. E100–E199) and the additives listed in it. */
final class Category {

    /** All categories, in the order they are shown on the main screen. */
    static final Category[] ALL = {
            new Category("E100–E199", R.string.category_e100, R.raw.additives_e100),
            new Category("E200–E299", R.string.category_e200, R.raw.additives_e200),
            new Category("E300–E399", R.string.category_e300, R.raw.additives_e300),
            new Category("E400–E499", R.string.category_e400, R.raw.additives_e400),
            new Category("E500–E599", R.string.category_e500, R.raw.additives_e500),
            new Category("E600–E699", R.string.category_e600, R.raw.additives_e600),
            new Category("E700–E799", R.string.category_e700, R.raw.additives_e700),
            new Category("E900–E999", R.string.category_e900, R.raw.additives_e900),
            new Category("E1000–E1599", R.string.category_e1000, R.raw.additives_e1000),
    };

    /** The range of E numbers, e.g. "E100–E199". */
    final String range;

    /** What the additives in the range are for, e.g. "Colours". */
    @StringRes
    final int title;

    /**
     * JSON array of {@code {"code", "name", "text"}} objects. The file is translated through
     * resource qualifiers (res/raw, res/raw-ru), like any other resource.
     */
    @RawRes
    private final int additives;

    private Category(String range, @StringRes int title, @RawRes int additives) {
        this.range = range;
        this.title = title;
        this.additives = additives;
    }

    /** Returns the category at {@code index}, or null if there is no such category. */
    static Category at(int index) {
        return index >= 0 && index < ALL.length ? ALL[index] : null;
    }

    /** Reads the category's additives in the current language. */
    List<Additive> loadAdditives(Resources resources) {
        try (InputStream input = resources.openRawResource(additives)) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int read;
            while ((read = input.read(buffer)) != -1) {
                bytes.write(buffer, 0, read);
            }
            JSONArray array = new JSONArray(new String(bytes.toByteArray(), StandardCharsets.UTF_8));
            List<Additive> result = new ArrayList<>(array.length());
            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);
                result.add(new Additive(
                        item.getString("code"), item.getString("name"), item.getString("text")));
            }
            return result;
        } catch (IOException | JSONException e) {
            // The files are bundled with the app, so this is a packaging bug, not a runtime state.
            throw new IllegalStateException("Cannot read additives of " + range, e);
        }
    }

    static final class Additive {

        /** E number, e.g. "E100". */
        final String code;

        /** Common name, e.g. "Curcumin". */
        final String name;

        /** Short description: plain text, paragraphs separated by blank lines. */
        final String text;

        Additive(String code, String name, String text) {
            this.code = code;
            this.name = name;
            this.text = text;
        }
    }
}
