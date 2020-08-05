package com.example.ecommerceapp.storefront;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerceapp.ProductDataBase;
import com.example.ecommerceapp.ProductDataStructure;
import com.example.ecommerceapp.R;

public class BuyProductsListFragment extends Fragment {
    private static final String TAG_BUY_PRODUCT_LIST_FRAGMENT = "buyProductsListFragment";

    private ProductDataBase mProductDataBase;
    private BuyProductsListAdapter mBuyProductsListAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private Button mBuyButton;
    private TextView mNoProductsTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mProductDataBase = ProductDataBase.getInstanceDataBase(getContext());
        return inflater.inflate(R.layout.store_front_fragment_product_list_for_buy, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView buyProductsListRecyclerView = view
                .findViewById(R.id.fragment_product_list_for_buy_recycler_view_products_buy);
        mBuyButton = view.findViewById(R.id.fragment_product_list_for_buy_button_buy);
        mNoProductsTextView = view.findViewById(R.id.fragment_product_list_for_buy_text_view_no_products);

        mBuyProductsListAdapter = new BuyProductsListAdapter(getContext(), mProductDataBase);
        buyProductsListRecyclerView.setAdapter(mBuyProductsListAdapter);

        mLinearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);

        buyProductsListRecyclerView.setLayoutManager(mLinearLayoutManager);
        buyProductsListRecyclerView.setHasFixedSize(true);

        buyProductsListRecyclerView.addItemDecoration(new DividerItemDecoration(
                buyProductsListRecyclerView.getContext(), mLinearLayoutManager.getOrientation()));

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(buyProductsListRecyclerView);

        mBuyButton.setOnClickListener(v -> {
            onBuyProduct();
        });

        mProductDataBase.mPublishSubject.subscribe(o -> {
            mProductDataBase.updateBuyProductList();
            if (mProductDataBase.getBuyProductCount() == 0) {
                mNoProductsTextView.setVisibility(View.VISIBLE);
                mBuyButton.setVisibility(View.GONE);
            } else {
                mNoProductsTextView.setVisibility(View.GONE);
                mBuyButton.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mProductDataBase.updateBuyProductList();
        mBuyProductsListAdapter.notifyDataSetChanged();
        if (mProductDataBase.getBuyProductCount() == 0) {
            mNoProductsTextView.setVisibility(View.VISIBLE);
            mBuyButton.setVisibility(View.GONE);
        } else {
            mNoProductsTextView.setVisibility(View.GONE);
            mBuyButton.setVisibility(View.VISIBLE);
        }
    }

    private void onBuyProduct() {
        int position = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (position == -1 && mProductDataBase.getBuyProductCount() != 0) {
            //position -1 - если во время scroll item нажать на кнопку Buy
            Log.d(TAG_BUY_PRODUCT_LIST_FRAGMENT, "clickOnBuyButtonToTimeScroll");
        } else if (position == -1 && mProductDataBase.getBuyProductCount() == 0) {
            mNoProductsTextView.setVisibility(View.VISIBLE);
            mBuyButton.setVisibility(View.GONE);
        } else {
            ProductDataStructure product = mProductDataBase.getBuyProductFromDataBase(position);
            int currentQuantity = product.getQuantityProduct();
            product.setQuantityProduct(currentQuantity - 1);
            mProductDataBase.setBuyProduct(position, product);
            mBuyProductsListAdapter.notifyDataSetChanged();
            if (getContext() != null) {
                mProductDataBase.writeData(getContext());
            }
            mProductDataBase.updateBuyProductList();
        }
    }
}
