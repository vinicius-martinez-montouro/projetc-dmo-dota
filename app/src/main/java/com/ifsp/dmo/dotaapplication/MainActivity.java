package com.ifsp.dmo.dotaapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ifsp.dmo.dotaapplication.model.BaseEntity;
import com.ifsp.dmo.dotaapplication.model.Team;
import com.ifsp.dmo.dotaapplication.service.HeroService;
import com.ifsp.dmo.dotaapplication.service.ProPlayerService;
import com.ifsp.dmo.dotaapplication.service.TeamService;
import com.ifsp.dmo.dotaapplication.view.adapter.ItemEntityAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author vinicius.montouro
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PERMISSION = 64;

    private static final String BASE_URL = "https://api.opendota.com/api/";

    private Button searchTeamButton;

    private Button searchProPlayerButton;

    private Button searchHeroButton;

    private RecyclerView entitiesRecyclerView;

    private ItemEntityAdapter itemEntityAdapter;

    private List<BaseEntity> entities;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchTeamButton = findViewById(R.id.teams_btn);
        searchHeroButton = findViewById(R.id.heroes_btn);
        searchProPlayerButton = findViewById(R.id.pro_player_btn);

        entitiesRecyclerView = findViewById(R.id.entities_recycler_view);
        entities = new ArrayList<>();

        searchProPlayerButton.setOnClickListener(this);
        searchHeroButton.setOnClickListener(this);
        searchTeamButton.setOnClickListener(this);

        itemEntityAdapter = new ItemEntityAdapter(entities);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        entitiesRecyclerView.setLayoutManager(layoutManager);
        entitiesRecyclerView.setAdapter(itemEntityAdapter);
    }

    @Override
    public void onClick(View v) {
        if(!hasPermission())
            requestPermission();
        else{
            buildRetrofit();
            switch (v.getId()){
                case R.id.teams_btn:
                    searchTeams();
                    break;
                case R.id.pro_player_btn:
                    searchProPlayers();
                    break;
                case R.id.heroes_btn:
                    searchHeros();
                    break;
            }
        }
    }


    private void buildRetrofit(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void searchTeams(){
        final TeamService retrofitService = retrofit.create(TeamService.class);
        Call<List<BaseEntity>> call = retrofitService.findTeams();
        search(call);
    }

    private void searchProPlayers(){
        final ProPlayerService retrofitService = retrofit.create(ProPlayerService.class);
        Call<List<BaseEntity>> call = retrofitService.findProPlayers();
        search(call);
    }

    private void searchHeros(){
        final HeroService retrofitService = retrofit.create(HeroService.class);
        Call<List<BaseEntity>> call = retrofitService.findHeroes();
        search(call);
    }

    private void search(Call<List<BaseEntity>> call) {

        call.enqueue(new Callback<List<BaseEntity>>() {
            @Override
            public void onResponse(Call<List<BaseEntity>> call, Response<List<BaseEntity>> response) {
                if (response.isSuccessful()) {
                    List<BaseEntity> list = response.body();
                    if (list != null){
                        entitiesRecyclerView.setVisibility(View.VISIBLE);
                        entities.clear();
                        entities.addAll(list);
                        itemEntityAdapter.notifyDataSetChanged();
                    } else {
                        entitiesRecyclerView.setVisibility(View.GONE);
                        showMessage(getString(R.string.data_not_found));
                    }
                } else {
                    entitiesRecyclerView.setVisibility(View.GONE);
                    showMessage(getString(R.string.data_not_found));
                }
            }

            @Override
            public void onFailure(Call<List<BaseEntity>> call, Throwable t) {
                entitiesRecyclerView.setVisibility(View.GONE);
                showMessage(getString(R.string.api_error_msg));
            }
        });
    }

    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equalsIgnoreCase(Manifest.permission.INTERNET) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    searchTeams();
                }

            }
        }
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
            final Activity activity = this;
            new AlertDialog.Builder(this)
                    .setMessage(R.string.resquest_permission_msg)
                    .setPositiveButton(R.string.request_permission_ok_msg, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET}, REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton(getString(R.string.request_permission_not_msg), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.INTERNET
                    },
                    REQUEST_PERMISSION);
        }
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}