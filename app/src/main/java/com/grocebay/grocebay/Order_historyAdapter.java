package com.grocebay.grocebay;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.grocebay.grocebay.utils.Order;

import java.util.List;

public class Order_historyAdapter extends RecyclerView.Adapter<Order_historyAdapter.OrderHistoryHolder> {

    private Context mCtx;
    private List<Order> OrderHistoryList;

    public Order_historyAdapter(Context mCtx, List<Order> OrderHistoryList) {
        this.mCtx = mCtx;
        this.OrderHistoryList = OrderHistoryList;
    }

    @NonNull
    @Override
    public Order_historyAdapter.OrderHistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.order_history_row, null);
        return new OrderHistoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Order_historyAdapter.OrderHistoryHolder holder, int position) {
        Order order = OrderHistoryList.get(position);

        holder.order_details.setText(order.getOrder_details());
        holder.order_date.setText(order.getOrder_date());
        holder.price.setText(String.valueOf(order.getPrice()));
        holder.status.setText(order.getStatus());

    }

    @Override
    public int getItemCount() {
        return OrderHistoryList.size();
    }

    class OrderHistoryHolder extends RecyclerView.ViewHolder {

        TextView order_details, order_date, price, status;

        public OrderHistoryHolder(View itemView) {
            super(itemView);

            order_details = itemView.findViewById(R.id.order_details);
            order_date = itemView.findViewById(R.id.order_date);
            price = itemView.findViewById(R.id.price);
            status = itemView.findViewById(R.id.order_status);
        }
    }
}
