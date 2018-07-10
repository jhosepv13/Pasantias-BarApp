package pe.bazan.jhosep.com.barapp.Service;

import java.util.List;

import pe.bazan.jhosep.com.barapp.Models.Category;
import pe.bazan.jhosep.com.barapp.Models.Product;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Sr. Doge on 22/06/2018.
 */

public interface ApiService {

    String API_BASE_URL = "http://192.168.0.4/";

    @GET("/api/getCategories")
    Call<List<Category>> getCategories();

    @GET("/api/getProducts")
    Call<List<Product>> getProducts();


}
