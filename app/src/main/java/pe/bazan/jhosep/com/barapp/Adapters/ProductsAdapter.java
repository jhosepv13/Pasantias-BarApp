package pe.bazan.jhosep.com.barapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import pe.bazan.jhosep.com.barapp.Models.Product;
import pe.bazan.jhosep.com.barapp.R;
import pe.bazan.jhosep.com.barapp.Service.ApiService;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private List<Product> products;

    public ProductsAdapter(){
        this.products = new ArrayList<>();
    }

    public void setProducts(List<Product> products){
        this.products = products;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imagenView;
        public TextView nombreText;
        public TextView descripcionText;
        public TextView precioText;

        public ViewHolder(View itemView) {
            super(itemView);
            //imagenView = itemView.findViewById(R.id.picture_image);
            nombreText = itemView.findViewById(R.id.name_text);
            descripcionText = itemView.findViewById(R.id.description_text);
            precioText = itemView.findViewById(R.id.price_text);
        }
    }


    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ProductsAdapter.ViewHolder viewHolder, int position) {
        Product product = this.products.get(position);

        viewHolder.nombreText.setText(product.getName());
        viewHolder.precioText.setText("S/. " + product.getPrice());
        viewHolder.descripcionText.setText(product.getDescription());

        //String url = ApiService.API_BASE_URL + "/images/" + product.getImage();
        //Picasso.with(viewHolder.itemView.getContext()).load(url).into(viewHolder.imagenView);

    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }
}
