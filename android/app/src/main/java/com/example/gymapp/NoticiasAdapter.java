package com.example.gymapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/*
    NOTICIAS ADAPTER
    ================
    Adaptador per al RecyclerView que mostra la llista de notícies.

    @author ImperiumGym
    @version 2.0
*/
public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.ViewHolder> {

    private List<Noticia> llistaNoticies;

    public NoticiasAdapter(List<Noticia> llistaNoticies) {
        this.llistaNoticies = llistaNoticies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_noticia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Noticia noticia = llistaNoticies.get(position);

        // TÍTOL
        if (noticia.getTitle() != null && noticia.getTitle().getRendered() != null) {
            String titol = android.text.Html.fromHtml(
                    noticia.getTitle().getRendered(),
                    android.text.Html.FROM_HTML_MODE_LEGACY
            ).toString();
            holder.titol.setText(titol);
        }

        // DESCRIPCIÓ (camp ACF)
        if (noticia.getAcf() != null && noticia.getAcf().getDescripcio() != null) {
            holder.descripcio.setText(noticia.getAcf().getDescripcio());
        }

        // IMATGE
        try {
            if (noticia.getEmbedded() != null &&
                    noticia.getEmbedded().getFeaturedmedia() != null &&
                    !noticia.getEmbedded().getFeaturedmedia().isEmpty()) {

                String urlImatge = noticia.getEmbedded().getFeaturedmedia().get(0).getSource_url();

                RequestOptions requestOptions = new RequestOptions()
                        .transform(new RoundedCorners(16));

                Glide.with(holder.itemView.getContext())
                        .load(urlImatge)
                        .apply(requestOptions)
                        .placeholder(R.drawable.logo)
                        .error(R.drawable.logo)
                        .into(holder.imatge);
            } else {
                holder.imatge.setImageResource(R.drawable.logo);
            }
        } catch (Exception e) {
            holder.imatge.setImageResource(R.drawable.logo);
        }
    }

    @Override
    public int getItemCount() {
        return llistaNoticies != null ? llistaNoticies.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titol, descripcio;
        ImageView imatge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titol = itemView.findViewById(R.id.txtTitulo);
            descripcio = itemView.findViewById(R.id.txtDescripcio);
            imatge = itemView.findViewById(R.id.imgNoticia);
        }
    }
}