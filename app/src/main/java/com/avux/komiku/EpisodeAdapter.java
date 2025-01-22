package com.avux.komiku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

    private final List<JSONObject> episodeList;
    private final Context context;
    private final String animeTitle;
    private final String animeDescription;
    public EpisodeAdapter(Context context, List<JSONObject> episodeList, String animeTitle, String animeDescription) {
        this.context = context;
        this.episodeList = episodeList;
        this.animeTitle = animeTitle;
        this.animeDescription = animeDescription;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_episode, parent, false);
        return new EpisodeViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        try {
            JSONObject episode = episodeList.get(position);
            int episodeNumber = episode.getInt("episodeNumber");
            String episodeTitle = episode.getString("title");

            String embedUrl = episode.getJSONArray("streams")
                    .getJSONObject(0)
                    .getString("url");

            holder.episodeNumber.setText("Episode " + episodeNumber);
            holder.episodeTitle.setText(episodeTitle);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, EpisodeViewerActivity.class);
                intent.putExtra("embed_url", embedUrl);
                intent.putExtra("episode_number", episodeNumber);
                intent.putExtra("anime_title", animeTitle);
                intent.putExtra("anime_description", animeDescription);
                context.startActivity(intent);
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    static class EpisodeViewHolder extends RecyclerView.ViewHolder {

        TextView episodeNumber;
        TextView episodeTitle;

        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            episodeNumber = itemView.findViewById(R.id.episode_number);
            episodeTitle = itemView.findViewById(R.id.episode_title);
        }
    }
}

