package com.yrybiak.foodadditives;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** Lists the additives of one category. */
public class AdditiveListActivity extends BaseActivity {

    private static final String EXTRA_CATEGORY = "category";

    static Intent newIntent(Context context, int categoryIndex) {
        return new Intent(context, AdditiveListActivity.class)
                .putExtra(EXTRA_CATEGORY, categoryIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int categoryIndex = getIntent().getIntExtra(EXTRA_CATEGORY, -1);
        Category category = Category.at(categoryIndex);
        if (category == null) {
            finish();
            return;
        }

        setUpScreen(R.layout.activity_list, true);
        setTitle(category.title);

        List<String> labels = new ArrayList<>();
        for (Category.Additive additive : category.loadAdditives(getResources())) {
            labels.add(getString(R.string.additive_label, additive.code, additive.name));
        }
        RecyclerView list = findViewById(R.id.content);
        list.setAdapter(new RowAdapter(labels, position ->
                startActivity(DetailActivity.newAdditiveIntent(this, categoryIndex, position))));
    }
}
