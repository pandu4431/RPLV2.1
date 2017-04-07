package com.androidbelieve.tubesrpl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidbelieve.tubesrpl.Upload_Download.Uploader;
import com.androidbelieve.tubesrpl.adapterView.RecyclerAdapter;
import com.androidbelieve.tubesrpl.setter_getter.isiMateri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static com.androidbelieve.tubesrpl.newLogin.USER_TYPE;

/**
 * Created by Ratan on 7/29/2015.
 */
public class TabMateri extends Fragment {
    public static String ID_MATERI;
    FloatingActionButton fabs;
    RecyclerView recyclerView;
    //RecyclerView.LayoutManager layoutManager;
    //RecyclerView.Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.tab_materi, container, false);

        fabs = (FloatingActionButton) ll.findViewById(R.id.fab);

        if (USER_TYPE.equals("mahasiswa")) {
            fabs.setVisibility(View.GONE);
        } else if (USER_TYPE.equals("dosen")) {
            fabs.setVisibility(View.VISIBLE);
        } else {
            fabs.setVisibility(View.GONE);
        }
        fabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (USER_TYPE.equals("mahasiswa")) {
                    Snackbar.make(view, "Mahasiswa", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                } else if (USER_TYPE.equals("dosen")) {
                    Snackbar.make(view, "Dosen", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    Intent upload = new Intent(getActivity(), Uploader.class);
                    startActivity(upload);
                } else {
                    fabs.setVisibility(View.GONE);
                }
            }
        });
        recyclerView = (RecyclerView) ll.findViewById(R.id.recycler_view);
        doLoad();
        return ll;
    }


    public void doLoad() {
        new BackgroundTask(getActivity(), recyclerView).execute();
    }

    class BackgroundTask extends AsyncTask<Void, isiMateri, Void> {

        Context ctx;
        //Activity activity;
        RecyclerView recyclerView;
        RecyclerView.Adapter adapter;
        RecyclerView.LayoutManager layoutManager;
        ArrayList<isiMateri> arrayList = new ArrayList<>();
        String URLdata = "http://pandumalik.esy.es/UserRegistration/materi.php?type=materi";

        public BackgroundTask(Activity ctx, RecyclerView rview) {
            this.ctx = ctx;
            this.recyclerView = rview;
        }

        @Override
        protected void onPreExecute() {
            recyclerView = (RecyclerView) recyclerView.findViewById(R.id.recycler_view);
            layoutManager = new LinearLayoutManager(ctx);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            adapter = new RecyclerAdapter(arrayList);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onProgressUpdate(isiMateri... values) {
            arrayList.add(values[0]);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url = new URL(URLdata);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");

                }

                httpURLConnection.disconnect();
                String json_string = stringBuilder.toString().trim();

                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.optJSONArray("server_response");
                int count = 0;
                while (count < jsonArray.length()) {
                    JSONObject JO = jsonArray.getJSONObject(count);
                    count++;
                    isiMateri isiMateris = new isiMateri(JO.getString("title"), JO.getString("description"));
                    publishProgress(isiMateris);
                    ID_MATERI = JO.getString("idmateri");
                }
                Log.d("JSON STRING", json_string);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
