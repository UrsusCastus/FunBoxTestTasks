package com.example.funboxtest.backend;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funboxtest.database.ProductDataBase;
import com.example.funboxtest.database.ProductDataStructure;
import com.example.funboxtest.R;

public class AllProductsListAdapter extends RecyclerView.Adapter<AllProductsListAdapter.ProductHolder> {
    private Context mContext;
    private ProductDataBase mProductDataBase;

    public AllProductsListAdapter(Context context, ProductDataBase productDataBase) {
        mContext = context;
        mProductDataBase = productDataBase;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.back_end_item_product, parent, false);
        int numberOfItemToDisplay = 3;
        view.getLayoutParams().height = parent.getHeight() / numberOfItemToDisplay;
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        ProductDataStructure product = mProductDataBase.getProductFromDataBase(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return mProductDataBase.getProductCount();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private final String numberUnit = mContext.getString(R.string.numberUnit);
        private TextView mNameTextView;
        private TextView mQuantityTextView;
        private ImageButton mEditItemImageButton;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.back_end_item_name_product);
            mQuantityTextView = itemView.findViewById(R.id.back_end_item_number_product);
            mEditItemImageButton = itemView.findViewById(R.id.back_end_item_arrow_right_button);
        }

        public void bind(ProductDataStructure product) {
            int quantityProduct = product.getQuantityProduct();
            String numberFormat = quantityProduct + " " + numberUnit;
            mNameTextView.setText(product.getNameProduct());
            mQuantityTextView.setText(numberFormat);
            mEditItemImageButton.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ItemProductEditActivity.class);
                int index = getAdapterPosition();
                intent.putExtra("productPosition", index);
                mContext.startActivity(intent);
            });
        }
    }
}
