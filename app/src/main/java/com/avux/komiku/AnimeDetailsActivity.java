package com.avux.komiku;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AnimeDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anime_details_activity);

        ImageView thumbnail = findViewById(R.id.thumbnail);
        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);
        TextView release = findViewById(R.id.release_date);
        TextView rating = findViewById(R.id.rating_anime);
        TextView status = findViewById(R.id.status_anime);
        TextView studio = findViewById(R.id.studio_anime);
        TextView genresTextView = findViewById(R.id.genre_anime);
        RecyclerView episodeRecyclerView = findViewById(R.id.episode_list);

        // Dapatkan data dari Intent
        String animeTitle = getIntent().getStringExtra("title");
        String animeThumbnail = getIntent().getStringExtra("thumbnail");
        String animeDescription = getIntent().getStringExtra("description");
        String animeRelease = getIntent().getStringExtra("release");
        String animeRating = getIntent().getStringExtra("rating");
        String animeStatus = getIntent().getStringExtra("status");
        String animeStudio = getIntent().getStringExtra("studio");
        String genresJson = getIntent().getStringExtra("genres");

        StringBuilder genresText = new StringBuilder("");
        try {
            JSONArray genresArray = new JSONArray(genresJson);
            for (int i = 0; i < genresArray.length(); i++) {
                JSONObject genre = genresArray.getJSONObject(i);
                genresText.append(genre.getString("name"));
                if (i < genresArray.length() - 1) {
                    genresText.append(", ");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Atur data ke views
        title.setText(animeTitle);
        description.setText(animeDescription);
        release.setText(animeRelease);
        rating.setText(animeRating);
        status.setText(animeStatus);
        studio.setText(animeStudio);
        genresTextView.setText(genresText.toString());

        episodeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            String episodeListJson = getIntent().getStringExtra("episodeList");
            JSONArray episodeArray = new JSONArray(episodeListJson);
            List<JSONObject> episodeList = new ArrayList<>();

            for (int i = 0; i < episodeArray.length(); i++) {
                episodeList.add(episodeArray.getJSONObject(i));
            }

            // Buat adapter dengan judul anime
            EpisodeAdapter adapter = new EpisodeAdapter(this, episodeList, animeTitle, animeDescription);
            episodeRecyclerView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Glide.with(this)
                .load(animeThumbnail)
                .placeholder(R.drawable.placeholder_image)
                .into(thumbnail);
    }
}
