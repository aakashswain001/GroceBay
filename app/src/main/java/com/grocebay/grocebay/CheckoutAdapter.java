package com.grocebay.grocebay;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.grocebay.grocebay.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {
    Context mCtx;
    private List<Product> productList;

    public CheckoutAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.checkout_row, parent, false);
        return new CheckoutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.price.setText(Integer.toString(Integer.parseInt(product.getPrice()) * product.getCount()));
        holder.quantity.setText(Integer.toString(product.getCount()));
        Picasso.with(mCtx).load(product.getImage()).into(holder.prod);
        if (product.getVegtype().equals("veg")) {
            holder.veg.setVisibility(View.VISIBLE);
        } else {
            holder.nonveg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, price, quantity;
        private ImageView veg, nonveg, prod;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_menuname);
            price = itemView.findViewById(R.id.tv_price);
            quantity = itemView.findViewById(R.id.tv_quantity);
            veg = itemView.findViewById(R.id.veg);
            nonveg = itemView.findViewById(R.id.nonveg);
            prod = itemView.findViewById(R.id.prod_image);
        }

    }
}
