package com.example.ecommerceapp.storefront;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.ProductDataBase;
import com.example.ecommerceapp.ProductDataStructure;
import com.example.ecommerceapp.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class BuyProductsListAdapter extends RecyclerView
        .Adapter<BuyProductsListAdapter.ProductInformationViewHolder> {
    private Context mContext;
    private ProductDataBase mProductDataBase;

    public BuyProductsListAdapter(Context context, ProductDataBase productDataBase) {
        mContext = context;
        mProductDataBase = productDataBase;
    }

    @NonNull
    @Override
    public ProductInformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.store_front_item_product_information, parent, false);
        return new ProductInformationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductInformationViewHolder holder, int position) {
        ProductDataStructure product = mProductDataBase.getBuyProductFromDataBase(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return mProductDataBase.getBuyProductCount();
    }

    class ProductInformationViewHolder extends RecyclerView.ViewHolder {
        private final String currency = mContext.getString(R.string.currencyRuble);
        private final String numberUnit = mContext.getString(R.string.numberUnit);
        private TextView mNameTextView;
        private TextView mPriceTextView;
        private TextView mNumberTextView;

        public ProductInformationViewHolder(@NonNull View itemView) {
            super(itemView);
            mNameTextView = itemView.findViewById(R.id.item_product_buy_name_product);
            mPriceTextView = itemView.findViewById(R.id.item_product_buy_price_product);
            mNumberTextView = itemView.findViewById(R.id.item_product_buy_number_product);
        }

        public void bind(ProductDataStructure product) {
            double price = 0.0;
            String priceFormat = formatPrice(price);
            try {
                price = Double.parseDouble(product.getPriceProduct());
            } catch (NumberFormatException e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            if (!Double.isNaN(price)) {
                priceFormat = formatPrice(price) + " " + currency;
            }

            int quantityProduct = product.getQuantityProduct();
            String numberFormat = quantityProduct + " " + numberUnit;

            mNameTextView.setText(product.getNameProduct());
            mPriceTextView.setText(priceFormat);
            mNumberTextView.setText(numberFormat);
        }
    }

    private String formatPrice(double price) {
        DecimalFormat decimalFormat = new DecimalFormat();
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator(' ');
        decimalFormat.setDecimalFormatSymbols(symbols);
        return decimalFormat.format(price);
    }
}
