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

    Funcionalitat:
    - Mostra imatge, títol i descripció per cada notícia
    - Utilitza Glide per carregar imatges desde URL
    - Aplica cantonades arrodonides a les imatges

    @author ImperiumGym
    @version 1.0
*/
public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.ViewHolder> {

    private List<Noticia> llistaNoticies;

    // Constructor: rep la llista de notícies a mostrar
    public NoticiasAdapter(List<Noticia> llistaNoticies) {
        this.llistaNoticies = llistaNoticies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout de cada element de la llista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_noticia, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Noticia noticia = llistaNoticies.get(position);

        // Estableix el títol (neteja tags HTML si n'hi ha)
        String titol = noticia.getTitle().getRendered();
        if (titol != null) {
            titol = android.text.Html.fromHtml(titol, android.text.Html.FROM_HTML_MODE_LEGACY).toString();
            holder.titol.setText(titol);
        }

        // Estableix la descripció
        String descripcio = noticia.getAcf().getDescripcio();
        if (descripcio != null) {
            holder.descripcio.setText(descripcio);
        }

        // Carrega la imatge destacada amb Glide
        try {
            String urlImatge = noticia.getEmbedded()
                    .getFeaturedmedia()
                    .get(0)
                    .getSource_url();

            // Aplica cantonades arrodonides a la imatge
            RequestOptions requestOptions = new RequestOptions()
                    .transform(new RoundedCorners(16));

            Glide.with(holder.itemView.getContext())
                    .load(urlImatge)
                    .apply(requestOptions)
                    .placeholder(R.drawable.logo) // Imatge mentre carrega
                    .error(R.drawable.logo)       // Imatge si hi ha error
                    .into(holder.imatge);

        } catch (Exception e) {
            // Si no hi ha imatge, mostrem el logo per defecte
            Glide.with(holder.itemView.getContext())
                    .load(R.drawable.logo)
                    .into(holder.imatge);
        }
    }

    @Override
    public int getItemCount() {
        return llistaNoticies != null ? llistaNoticies.size() : 0;
    }

    /*
        MÈTODE PER ACTUALITZAR LA LLISTA DE NOTÍCIES
        ============================================
        Actualitza les dades i notifica al RecyclerView que hi ha canvis.
    */
    public void updateData(List<Noticia> novesNoticies) {
        this.llistaNoticies = novesNoticies;
        notifyDataSetChanged();
    }

    // ==================== VIEWHOLDER ====================
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