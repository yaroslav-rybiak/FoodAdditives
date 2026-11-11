package com.yrybyak.foodadditives;

import android.content.res.Resources;
import android.content.res.TypedArray;

import androidx.annotation.ArrayRes;
import androidx.annotation.StringRes;

import java.util.ArrayList;
import java.util.List;

/** A range of E numbers (e.g. E100 - E199) and the additives listed in it. */
final class Category {

    /** All categories, in the order they are shown on the main screen. */
    static final Category[] ALL = {
            new Category(R.string.e100_e199_label, R.string.e100_e199_meaning, R.array.additives_e100_e199),
            new Category(R.string.e200_e299_label, R.string.e200_e299_meaning, R.array.additives_e200_e299),
            new Category(R.string.e300_e399_label, R.string.e300_e399_meaning, R.array.additives_e300_e399),
            new Category(R.string.e400_e499_label, R.string.e400_e499_meaning, R.array.additives_e400_e499),
            new Category(R.string.e500_e599_label, R.string.e500_e599_meaning, R.array.additives_e500_e599),
            new Category(R.string.e600_e699_label, R.string.e600_e699_meaning, R.array.additives_e600_e699),
            new Category(R.string.e900_e999_label, R.string.e900_e999_meaning, R.array.additives_e900_e999),
            new Category(R.string.e1000_e1999_label, R.string.e1000_e1999_meaning, R.array.additives_e1000_e1999),
    };

    /** Name shown on the main screen, e.g. "E100 - E199 Красители". */
    @StringRes
    final int label;

    /** Title of the category's own screen, e.g. "Красители". */
    @StringRes
    final int title;

    /** Array of (label, text) string pairs, see arrays.xml. */
    @ArrayRes
    private final int additives;

    private Category(@StringRes int label, @StringRes int title, @ArrayRes int additives) {
        this.label = label;
        this.title = title;
        this.additives = additives;
    }

    /** Returns the category at {@code index}, or null if there is no such category. */
    static Category at(int index) {
        return index >= 0 && index < ALL.length ? ALL[index] : null;
    }

    List<Additive> loadAdditives(Resources resources) {
        List<Additive> result = new ArrayList<>();
        TypedArray pairs = resources.obtainTypedArray(additives);
        try {
            for (int i = 0; i + 1 < pairs.length(); i += 2) {
                result.add(new Additive(pairs.getResourceId(i, 0), pairs.getResourceId(i + 1, 0)));
            }
        } finally {
            pairs.recycle();
        }
        return result;
    }

    static final class Additive {

        /** Short name, e.g. "E100". */
        @StringRes
        final int label;

        /** Full description as HTML. */
        @StringRes
        final int text;

        Additive(@StringRes int label, @StringRes int text) {
            this.label = label;
            this.text = text;
        }
    }
}
