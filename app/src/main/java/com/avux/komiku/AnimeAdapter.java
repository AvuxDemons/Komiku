package com.avux.komiku;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    private final Context context;
    private final JSONArray animeList;

    public AnimeAdapter(Context context, JSONArray animeList) {
        this.context = context;
        this.animeList = animeList;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_anime_card, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        try {
            JSONObject anime = animeList.getJSONObject(position);
            String title = anime.getString("title");
            String thumbnailUrl = anime.getString("thumbnail");
            String description = anime.getString("description");
            String release = anime.getString("releaseDate");
            String rating = anime.getString("rating");
            String status = anime.getString("status");

            JSONObject studioObject = anime.getJSONObject("studio");
            String studioName = studioObject.getString("name");

            JSONArray genresArray = anime.getJSONArray("genres");
            String genres = genresArray.toString();

            JSONArray episodeListArray = anime.getJSONArray("episodeList");
            String episodeList = episodeListArray.toString();

            holder.title.setText(title);
            Glide.with(context)
                    .load(thumbnailUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(holder.thumbnail);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, AnimeDetailsActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("thumbnail", thumbnailUrl);
                intent.putExtra("description", description);
                intent.putExtra("release", release);
                intent.putExtra("rating", rating);
                intent.putExtra("status", status);
                intent.putExtra("studio", studioName);
                intent.putExtra("genres", genres);
                intent.putExtra("episodeList", episodeList);
                context.startActivity(intent);
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return animeList.length();
    }

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;

        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.title);
        }
    }
}
