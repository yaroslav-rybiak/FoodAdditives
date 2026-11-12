package com.yrybiak.foodadditives;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.widget.TextViewCompat;

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

        if (!getIntent().hasExtra(EXTRA_CATEGORY)) {
            showAbout();
            return;
        }

        Category category = Category.at(getIntent().getIntExtra(EXTRA_CATEGORY, -1));
        int additiveIndex = getIntent().getIntExtra(EXTRA_ADDITIVE, -1);
        List<Category.Additive> additives = category == null
                ? Collections.emptyList()
                : category.loadAdditives(getResources());
        if (additiveIndex < 0 || additiveIndex >= additives.size()) {
            finish();
            return;
        }
        showAdditive(additives.get(additiveIndex));
    }

    private void showAdditive(Category.Additive additive) {
        setUpScreen(R.layout.activity_detail, true);
        setTitle(additive.code);
        TextView nameView = findViewById(R.id.name);
        nameView.setText(additive.name);
        TextView statusView = findViewById(R.id.status);
        statusView.setText(additive.euAuthorised
                ? R.string.eu_authorised : R.string.eu_not_authorised);
        // A green check for authorised additives; a neutral grey mark for the others, since
        // "not authorised" often just means obsolete or never submitted, not dangerous.
        statusView.setCompoundDrawablesRelativeWithIntrinsicBounds(additive.euAuthorised
                ? R.drawable.ic_status_authorised : R.drawable.ic_status_not_authorised, 0, 0, 0);
        TextViewCompat.setCompoundDrawableTintList(statusView, additive.euAuthorised
                ? ContextCompat.getColorStateList(this, R.color.status_authorised)
                : statusView.getTextColors());
        TextView textView = findViewById(R.id.text);
        textView.setText(additive.text);
    }

    private void showAbout() {
        setUpScreen(R.layout.activity_detail, true);
        setTitle(R.string.about_label);
        findViewById(R.id.name).setVisibility(View.GONE);
        findViewById(R.id.status).setVisibility(View.GONE);
        TextView textView = findViewById(R.id.text);
        textView.setText(HtmlCompat.fromHtml(
                getString(R.string.about_text), HtmlCompat.FROM_HTML_MODE_LEGACY));
        // Makes the e-mail link clickable.
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
