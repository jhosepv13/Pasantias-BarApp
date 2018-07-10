package pe.bazan.jhosep.com.barapp.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pe.bazan.jhosep.com.barapp.Models.Category;
import pe.bazan.jhosep.com.barapp.R;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private List<Category> categories;

    public CategoriesAdapter(){
        this.categories = new ArrayList<>();
    }

    public void setCategories(List<Category> categories){
        this.categories = categories;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imagenCatView;
        public TextView nombreCatText;

        public ViewHolder(View itemView) {
            super(itemView);
            //imagenView = itemView.findViewById(R.id.picture_image);
            nombreCatText = itemView.findViewById(R.id.name_cat_text);
        }
    }


    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories_products,
                parent, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(CategoriesAdapter.ViewHolder viewHolder, int position) {
        Category category = this.categories.get(position);

        viewHolder.nombreCatText.setText(category.getName());
        //viewHolder.precioText.setText("S/. " + product.getPrice());
        //viewHolder.descripcionText.setText(product.getDescription());

        //String url = ApiService.API_BASE_URL + "/images/" + product.getImage();
        //Picasso.with(viewHolder.itemView.getContext()).load(url).into(viewHolder.imagenView);

    }

    @Override
    public int getItemCount() {
        return this.categories.size();
    }
}
