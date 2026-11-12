package com.yrybiak.foodadditives;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/** Lists the additive categories. */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpScreen(R.layout.activity_list, false);

        List<String> labels = new ArrayList<>();
        int[] icons = new int[Category.ALL.length];
        for (int i = 0; i < Category.ALL.length; i++) {
            Category category = Category.ALL[i];
            labels.add(getString(R.string.category_label, category.range, getString(category.title)));
            icons[i] = category.icon;
        }
        RecyclerView list = findViewById(R.id.content);
        list.setAdapter(new RowAdapter(labels, icons, position ->
                startActivity(AdditiveListActivity.newIntent(this, position))));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.dark_theme) {
            ThemeSettings.setDark(this, true);
            return true;
        } else if (id == R.id.light_theme) {
            ThemeSettings.setDark(this, false);
            return true;
        } else if (id == R.id.about) {
            startActivity(DetailActivity.newAboutIntent(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
