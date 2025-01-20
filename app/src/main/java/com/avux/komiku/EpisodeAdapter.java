package com.avux.komiku;

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

    public EpisodeAdapter(Context context, List<JSONObject> episodeList) {
        this.context = context;
        this.episodeList = episodeList;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_episode, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        try {
            JSONObject episode = episodeList.get(position);
            int episodeNumber = episode.getInt("episodeNumber");
            String episodeTitle = episode.getString("title");

            // Ambil URL dari stream
            String embedUrl = episode.getJSONArray("streams")
                    .getJSONObject(0)
                    .getString("url");

            holder.episodeNumber.setText("Episode " + episodeNumber);
            holder.episodeTitle.setText(episodeTitle);

            holder.itemView.setOnClickListener(v -> {
                // Buat intent untuk membuka EpisodeViewerActivity
                Intent intent = new Intent(context, EpisodeViewerActivity.class);
                intent.putExtra("embed_url", embedUrl); // Kirim URL sebagai extra
                context.startActivity(intent); // Jalankan Activity
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

