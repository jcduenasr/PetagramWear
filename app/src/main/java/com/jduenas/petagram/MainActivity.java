package com.jduenas.petagram;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.jduenas.petagram.adapter.MascotaAdaptador;
import com.jduenas.petagram.adapter.PageAdapter;
import com.jduenas.petagram.fragment.PerfilFragment;
import com.jduenas.petagram.fragment.RecyclerViewFragment;
import com.jduenas.petagram.pojo.Mascota;
import com.jduenas.petagram.restApi.EndpointsApi;
import com.jduenas.petagram.restApi.adapter.RestApiAdapter;
import com.jduenas.petagram.restApi.model.UsuarioResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<Mascota> mascotas;
    private RecyclerView listaMascotas;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String var="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar miActionBar = (Toolbar) findViewById(R.id.miActionBar);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar!=null){
            setSupportActionBar(toolbar);
        }
        tabLayout= (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        /*Bundle b = getIntent().getExtras();
        if (b!=null){
            var = b.getString("flag");
        }*/
        setUpViewPager();
        viewPager.setCurrentItem(1);

    }

    private ArrayList<Fragment> agregarFragments(){
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new RecyclerViewFragment());
        fragments.add(new PerfilFragment());
        return fragments;
    }

    private void setUpViewPager(){
        viewPager.setAdapter(new PageAdapter(getSupportFragmentManager(),agregarFragments()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_dog_house);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_dog);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mAbout:
                //Toast.makeText(this,"Menu About",Toast.LENGTH_SHORT).show();
                Intent intentAbout = new Intent(this,About.class);
                startActivity(intentAbout);
                break;
            case R.id.mContacto:
                //Toast.makeText(this,"Menu Settings",Toast.LENGTH_SHORT).show();
                Intent intentContacto = new Intent(this,Contacto.class);
                startActivity(intentContacto);
                break;
            case R.id.mAccount:
                Intent iaccount = new Intent(this,ConfigureAccount.class);
                startActivity(iaccount);
                break;
            case R.id.mFavoritos:
                Intent intent = new Intent(this,ListadoMascotas.class);
                startActivity(intent);
                break;
            case R.id.mNotifications:
                enviarToken();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void enviarToken(){
        SharedPreferences sharedPref = this.getSharedPreferences("ConfigureAccount",this.MODE_PRIVATE);
        String accountName = "";
        try{
            accountName = sharedPref.getString(getString(R.string.account_name), "");
        }catch (Exception e){
            accountName = "";
        }
        if (!accountName.equals("")) {
            String token = FirebaseInstanceId.getInstance().getToken();
            Log.d("TOKEN", token);
            sendRegistrationToServer(token, accountName);
        }else{
            Toast.makeText(this, "Primero debe configurar una cuenta de instragram para recibir notificaciones", Toast.LENGTH_LONG).show();
        }
    }

    private void sendRegistrationToServer(String token, String accountName) {

        Log.d("TOKEN", "sendRegistrationToServer: " + token);
        RestApiAdapter restApiAdapter = new RestApiAdapter();
        EndpointsApi endPoints = restApiAdapter.establecerConexionRestAPIHeroku();
        Call<UsuarioResponse> usuarioResponseCall = endPoints.registrarTokenID(token,accountName);

        usuarioResponseCall.enqueue(new Callback<UsuarioResponse>() {
            @Override
            public void onResponse(Call<UsuarioResponse> call, Response<UsuarioResponse> response) {
                UsuarioResponse usuarioResponse = response.body();
                //Log.d("usuarioresponse", usuarioResponse.getId_dispositivo());
                //Log.d("USUARIO_FIREBASE", usuarioResponse.getId_usuario_instagram());
            }

            @Override
            public void onFailure(Call<UsuarioResponse> call, Throwable t) {

            }
        });
    }


}
