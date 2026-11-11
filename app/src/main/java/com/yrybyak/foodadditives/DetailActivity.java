package com.yrybyak.foodadditives;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.core.text.HtmlCompat;

import java.util.Collections;
import java.util.List;

/** Shows a page of text: the description of one additive, or the "about" page. */
public class DetailActivity extends BaseActivity {

    private static final String EXTRA_CATEGORY = "category";
    private static final String EXTRA_ADDITIVE = "additive";

    static Intent newAdditiveIntent(Context context, int categoryIndex, int additiveIndex) {
        return new Intent(context, DetailActivity.class)
                .putExtra(EXTRA_CATEGORY, categoryIndex)
                .putExtra(EXTRA_ADDITIVE, additiveIndex);
    }

    static Intent newAboutIntent(Context context) {
        return new Intent(context, DetailActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        @StringRes int title;
        @StringRes int text;
        if (getIntent().hasExtra(EXTRA_CATEGORY)) {
            Category category = Category.at(getIntent().getIntExtra(EXTRA_CATEGORY, -1));
            int additiveIndex = getIntent().getIntExtra(EXTRA_ADDITIVE, -1);
            List<Category.Additive> additives = category == null
                    ? Collections.emptyList()
                    : category.loadAdditives(getResources());
            if (additiveIndex < 0 || additiveIndex >= additives.size()) {
                finish();
                return;
            }
            title = additives.get(additiveIndex).label;
            text = additives.get(additiveIndex).text;
        } else {
            title = R.string.about_label;
            text = R.string.about_text;
        }

        setUpScreen(R.layout.activity_detail, true);
        setTitle(title);
        TextView textView = findViewById(R.id.text);
        textView.setText(HtmlCompat.fromHtml(getString(text), HtmlCompat.FROM_HTML_MODE_LEGACY));
        // Makes links in the text (the e-mail address on the "about" page) clickable.
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
