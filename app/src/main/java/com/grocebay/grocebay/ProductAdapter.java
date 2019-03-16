package com.grocebay.grocebay;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.grocebay.grocebay.interfaces.AddorRemoveCallbacks;
import com.grocebay.grocebay.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    Context mCtx;
    private List<Product> productList;

    public ProductAdapter(Context mCtx, List<Product> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.product_row, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.price.setText(product.getPrice());
        holder.desc.setText(product.getDescription());
        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = holder.elegantNumberButton.getNumber();
                int number = Integer.parseInt(num);
                int k = productList.get(position).getCount();
                productList.get(position).setCount(number);
                if (number - k > 0) {
                    ((AddorRemoveCallbacks) mCtx).onAddProduct(number - k);
                } else if (number - k < 0)
                    ((AddorRemoveCallbacks) mCtx).onRemoveProduct(k - number);

            }
        });
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
        private TextView name, price, desc;
        private Button addToCart;
        private ElegantNumberButton elegantNumberButton;
        private ImageView veg, nonveg;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.prod_name);
            price = itemView.findViewById(R.id.prod_price);
            elegantNumberButton = itemView.findViewById(R.id.qty_button);
            addToCart = itemView.findViewById(R.id.add_to_cart);
            veg = itemView.findViewById(R.id.veg);
            nonveg = itemView.findViewById(R.id.nonveg);
            desc = itemView.findViewById(R.id.prod_desc);
        }

    }
}
