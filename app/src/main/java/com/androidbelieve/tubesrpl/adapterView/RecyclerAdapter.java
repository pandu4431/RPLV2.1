package com.androidbelieve.tubesrpl.adapterView;

/**
 * Created by pandu on 25/03/17.
 */


import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.androidbelieve.tubesrpl.R;
import com.androidbelieve.tubesrpl.setter_getter.isiMateri;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.androidbelieve.tubesrpl.TabMateri.ID_MATERI;
import static com.androidbelieve.tubesrpl.newLogin.ID_USER;

/**
 * Created by galan on 23/03/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private static final int TYPE_HEAD = 0;
    private static final String REGISTER_URL = "http://pandumalik.esy.es/UserRegistration/favorite.php";
    private static final int TYPE_LIST = 1;
    ArrayList<isiMateri> arrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<isiMateri> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        isiMateri isiMateris = arrayList.get(position);
        holder.title.setText(isiMateris.getTitle());
        holder.description.setText(isiMateris.getDescription());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEAD;
        return TYPE_LIST;

    }

    private void insertfavorite(int position) {
        isiMateri isiMateris = arrayList.get(position);
        String namaMateri = isiMateris.getTitle();
        String idmateri = isiMateris.getIdmateri();
        String iduser = ID_USER;
        register(idmateri, iduser, namaMateri);
    }

    private void register(String idmateri, String iduser, String namaMateri) {
        String urlSuffix = "?idmateri=" + idmateri + "&iduser=" + iduser +"&title=" +namaMateri;
        class RegisterUser extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferReader = null;
                try {
                    URL url = new URL(REGISTER_URL + s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String result;
                    result = bufferReader.readLine();
                    return result;

                } catch (Exception e) {
                    return null;
                }
            }
        }
        RegisterUser ur = new RegisterUser();
        ur.execute(urlSuffix);
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView title, description;
        CardView cardView;
        Button mfav;
        Resources res;

        public RecyclerViewHolder(final View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.item_title);
            description = (TextView) view.findViewById(R.id.item_detail);
            cardView = (CardView) view.findViewById(R.id.card_view);
            mfav = (Button) view.findViewById(R.id.favorite);
            res = view.getResources();

            mfav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(view, "Materi berhasil ditambahkan ke Favorite Anda", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                    insertfavorite(getAdapterPosition());
                }
            });
        }
    }


}

