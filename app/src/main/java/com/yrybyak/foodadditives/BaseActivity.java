package com.yrybyak.foodadditives;

import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.LayoutRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;

/** Common screen setup: edge-to-edge layout with a toolbar on top of scrolling content. */
abstract class BaseActivity extends AppCompatActivity {

    /**
     * Inflates {@code layout}, which must contain the views {@code root}, {@code toolbar} and
     * {@code content}, and keeps them clear of the system bars.
     *
     * @param showUp whether the toolbar shows a back arrow
     */
    protected void setUpScreen(@LayoutRes int layout, boolean showUp) {
        EdgeToEdge.enable(this);
        setContentView(layout);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(showUp);
        }

        // The app bar pads itself below the status bar (fitsSystemWindows). The sides are
        // handled on the root and the bottom on the scrolling content, so that the content
        // scrolls behind the navigation bar but can always be scrolled clear of it.
        View content = findViewById(R.id.content);
        int contentPaddingBottom = content.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root), (root, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.displayCutout());
            root.setPadding(bars.left, 0, bars.right, 0);
            content.setPadding(content.getPaddingLeft(), content.getPaddingTop(),
                    content.getPaddingRight(), contentPaddingBottom + bars.bottom);
            return windowInsets;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
