package com.grocebay.grocebay.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.grocebay.grocebay.R;
import com.grocebay.grocebay.model.Products;

import java.util.List;

/**
 * Created by aakas on 3/8/2018.
 */

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Products> productsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, description, count;
        public ImageView image;
        Button add, subtract;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            image = (ImageView) view.findViewById(R.id.image);
            description = (TextView) view.findViewById(R.id.description);
            count = (TextView) view.findViewById(R.id.count);
            add = (Button) view.findViewById(R.id.add);
            subtract = (Button) view.findViewById(R.id.subtract);
        }
    }


    public ProductsAdapter(Context mContext, List<Products> productsList) {
        this.mContext = mContext;
        this.productsList = productsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.products_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Products product = productsList.get(position);
        holder.name.setText(product.getName());
        holder.description.setText(product.getDescription());
        holder.price.setText(product.getPrice());
        holder.image.setImageResource(product.getImage_url());
        holder.count.setText(Integer.toString(product.getCount()));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int no = product.getCount() + 1;
                product.setCount(no);
                holder.count.setText(Integer.toString(product.getCount()));
            }
        });
        holder.subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getCount() != 0) {
                    int no = product.getCount() - 1;
                    product.setCount(no);
                    holder.count.setText(Integer.toString(product.getCount()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productsList.size();
    }
}

