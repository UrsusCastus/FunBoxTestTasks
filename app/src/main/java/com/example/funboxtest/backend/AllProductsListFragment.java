package com.example.funboxtest.backend;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.funboxtest.database.ProductDataBase;
import com.example.funboxtest.R;

public class AllProductsListFragment extends Fragment {
    private ProductDataBase mProductDataBase;
    private AllProductsListAdapter mAllProductsListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mProductDataBase = ProductDataBase.getInstanceDataBase(getContext());
        return inflater.inflate(R.layout.back_end_fragment_list_of_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView allProductsListRecyclerView = view
                .findViewById(R.id.fragment_list_of_products_recycler_view_back_end);
        mAllProductsListAdapter = new AllProductsListAdapter(getContext(), mProductDataBase);
        allProductsListRecyclerView.setAdapter(mAllProductsListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        allProductsListRecyclerView.setLayoutManager(linearLayoutManager);
        allProductsListRecyclerView.setHasFixedSize(true);

        allProductsListRecyclerView.addItemDecoration(
                new DividerItemDecoration(allProductsListRecyclerView.getContext(),
                        linearLayoutManager.getOrientation()) {
                    @Override
                    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                               RecyclerView.State state) {
                        int position = parent.getChildAdapterPosition(view);
                        if (position == parent.getAdapter().getItemCount() - 1) {
                            outRect.setEmpty();
                        } else {
                            super.getItemOffsets(outRect, view, parent, state);
                        }
                    }
                }
        );
    }

    @Override
    public void onStart() {
        super.onStart();
        mAllProductsListAdapter.notifyDataSetChanged();
    }
}
