package pe.bazan.jhosep.com.barapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.BackgroundDetector;

import java.util.List;

import pe.bazan.jhosep.com.barapp.Adapters.ProductsAdapter;
import pe.bazan.jhosep.com.barapp.Models.Product;
import pe.bazan.jhosep.com.barapp.R;
import pe.bazan.jhosep.com.barapp.Service.ApiService;
import pe.bazan.jhosep.com.barapp.Service.ApiServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProductsActivity extends AppCompatActivity {

    private static final String TAG = ListProductsActivity.class.getSimpleName();

    private RecyclerView productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_products);

        productsList = findViewById(R.id.recyclerView);
        productsList.setLayoutManager(new LinearLayoutManager(this));

        productsList.setAdapter(new ProductsAdapter());

        initialize();
    }

    private void initialize() {

        ApiService service = ApiServiceGenerator.createService(ApiService.class);

        Call<List<Product>> call = service.getProducts();

        call.enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                try {

                    int statusCode = response.code();
                    Log.d(TAG, "HTTP status code: " + statusCode);

                    if (response.isSuccessful()) {

                        List<Product> products = response.body();
                        Log.d(TAG, "Productos: " + products);

                        ProductsAdapter adapter = (ProductsAdapter) productsList.getAdapter();
                        adapter.setProducts(products);
                        adapter.notifyDataSetChanged();

                    } else {
                        Log.e(TAG, "onError: " + response.errorBody().string());
                        throw new Exception("Error en el servicio");
                    }

                } catch (Throwable t) {
                    try {
                        Log.e(TAG, "onThrowable: " + t.toString(), t);
                        Toast.makeText(ListProductsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }catch (Throwable x){}
                }
            }
            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e(TAG, "onFailure: " + t.toString());
                Toast.makeText(ListProductsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

        });
    }
}
