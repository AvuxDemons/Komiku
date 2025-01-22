package com.avux.komiku;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EpisodeViewerActivity extends AppCompatActivity {

    private WebView webView;
    private FrameLayout fullscreenContainer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_viewer);

        webView = findViewById(R.id.web_view);
        fullscreenContainer = findViewById(R.id.fullscreen_container);

        // If the WebView was previously created and we have a saved state, restore it
        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            String animeTitle = getIntent().getStringExtra("anime_title");
            String embedHtml = getIntent().getStringExtra("embed_url");
            int episodeNumber = getIntent().getIntExtra("episode_number", 0);
            String animeDescription = getIntent().getStringExtra("anime_description");

            TextView titleTextView = findViewById(R.id.anime_title);
            titleTextView.setText(animeTitle);

            TextView episodeTextView = findViewById(R.id.episode_title);
            episodeTextView.setText("Episode " + episodeNumber);
            TextView descriptionTextView = findViewById(R.id.anime_description);
            descriptionTextView.setText(animeDescription);

            String responsiveHtml = "<html><style> iframe { position: absolute !important;\n" +
                    "  top: 0;\n" +
                    "  left: 0;\n" +
                    "  width: 100%;\n" +
                    "  height: 100%;\n" +
                    "  z-index: 2 }" +
                    "</style><body style='margin:0; padding:0;'>"
                    + "<div style='display: relative;'>"
                    + embedHtml
                    + "</div></body></html>";

            // Configure WebView settings
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(false);

            // Set WebViewClient to handle URLs inside the WebView
            webView.setWebViewClient(new WebViewClient());

            // Set WebChromeClient for fullscreen support
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onShowCustomView(View view, CustomViewCallback callback) {
                    // Enter fullscreen
                    customView = view;
                    customViewCallback = callback;
                    fullscreenContainer.addView(view);
                    fullscreenContainer.setVisibility(View.VISIBLE);
                    webView.setVisibility(View.GONE);

                    // Hide the system navigation bar in fullscreen mode
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                    decorView.setSystemUiVisibility(uiOptions);
                }

                @Override
                public void onHideCustomView() {
                    // Exit fullscreen
                    fullscreenContainer.removeView(customView);
                    customView = null;
                    fullscreenContainer.setVisibility(View.GONE);
                    webView.setVisibility(View.VISIBLE);

                    // Show the system navigation bar when exiting fullscreen
                    View decorView = getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    customViewCallback.onCustomViewHidden();
                }
            });

            // Load the responsive HTML into the WebView
            webView.loadData(responsiveHtml, "text/html", "UTF-8");
        }
    }

    @Override
    public void onBackPressed() {
        if (customView != null) {
            // Exit fullscreen if active
            customViewCallback.onCustomViewHidden();
        } else if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the WebView state before the activity is paused or destroyed
        webView.saveState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the WebView state after the activity is recreated
        webView.restoreState(savedInstanceState);
    }

}
