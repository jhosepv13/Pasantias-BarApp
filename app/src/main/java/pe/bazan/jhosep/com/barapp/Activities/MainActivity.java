package pe.bazan.jhosep.com.barapp.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import pe.bazan.jhosep.com.barapp.Adapters.CategoriesAdapter;
import pe.bazan.jhosep.com.barapp.Models.Category;
import pe.bazan.jhosep.com.barapp.R;
import pe.bazan.jhosep.com.barapp.Service.ApiService;
import pe.bazan.jhosep.com.barapp.Service.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView categoriesList;

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToogle;
    private TextView address;

    private Button btnwhisky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            btnwhisky = findViewById(R.id.btn_whisky);

            address = findViewById(R.id.address);

            mDrawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
            mToogle = new ActionBarDrawerToggle(this, mDrawer, R.string.Open_Nav, R.string.Close_Nav);
            mDrawer.addDrawerListener(mToogle);
            mToogle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);

            Bundle extras = getIntent().getExtras();
            if(extras != null){
                String data = extras.getString("fullAddress");
                Log.e(TAG, "Data: "+data);
                address.setText(data);
            }

            categoriesList = findViewById(R.id.recyclerCategoryView);
            categoriesList.setLayoutManager(new LinearLayoutManager(this));

            categoriesList.setAdapter(new CategoriesAdapter());

            initialize();

    }

    private void initialize() {

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Category>> call = service.getCategories();

        call.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Category> categories = response.body();
                        Log.d(TAG, "Categorias: " + categories);

                        CategoriesAdapter adapter = (CategoriesAdapter) categoriesList.getAdapter();
                        adapter.setCategories(categories);
                        adapter.notifyDataSetChanged();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }
            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }


    public void lookProducts(View view){
        Intent intent = new Intent(this, ListProductsActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToogle.onOptionsItemSelected(item)){

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ShowToast")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if( id == R.id.Login){
            Toast.makeText(this, "Cargando: Iniciar Sesión", Toast.LENGTH_SHORT).show();
        }

        if( id == R.id.Ubication){
            Toast.makeText(this, "Cargando: Ubicación", Toast.LENGTH_SHORT).show();
        }

        if( id == R.id.Categories){
            Toast.makeText(this, "Cargando: Categorias", Toast.LENGTH_SHORT).show();
        }

        if( id == R.id.Call_us){
            Toast.makeText(this, "Llamando...", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
