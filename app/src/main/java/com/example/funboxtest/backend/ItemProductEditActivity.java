package com.example.funboxtest.backend;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.funboxtest.database.ProductDataBase;
import com.example.funboxtest.database.ProductDataStructure;
import com.example.funboxtest.R;

public class ItemProductEditActivity extends AppCompatActivity {
    private final int defaultPositionItem = -1;
    private ProductDataBase mProductDataBase;
    private Button mButtonCancel;
    private Button mButtonSave;
    private int mPositionItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_product_edit);
        mButtonCancel = findViewById(R.id.toolbar_cancel_save_button_cancel);
        mButtonSave = findViewById(R.id.toolbar_cancel_save_button_save);
        mButtonCancel.setVisibility(View.GONE);
        mButtonSave.setVisibility(View.GONE);

        EditText editTextName = findViewById(R.id.activity_item_product_edit_name_edit_text);
        EditText editTextPrice = findViewById(R.id.activity_item_product_edit_price_edit_text);
        EditText editTextQuantity = findViewById(R.id.activity_item_product_edit_quantity_edit_text);

        mPositionItem = getIntent().getIntExtra("productPosition", defaultPositionItem);
        mProductDataBase = ProductDataBase.getInstanceDataBase(getApplicationContext());

        if (mPositionItem > defaultPositionItem) {
            editTextName.setText(mProductDataBase.getProductFromDataBase(mPositionItem).getNameProduct());
            editTextPrice.setText(mProductDataBase.getProductFromDataBase(mPositionItem).getPriceProduct());
            editTextQuantity.setText(String.valueOf(mProductDataBase.getProductFromDataBase(mPositionItem).getQuantityProduct()));
        } else {
            editTextName.setText("");
            editTextPrice.setText("");
            editTextQuantity.setText("");
        }

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mButtonCancel.setVisibility(View.VISIBLE);
                mButtonSave.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        editTextName.addTextChangedListener(textWatcher);
        editTextPrice.addTextChangedListener(textWatcher);
        editTextQuantity.addTextChangedListener(textWatcher);

        mButtonCancel.setOnClickListener(view -> {
            View focusView = this.getCurrentFocus();
            if (focusView != null) {
                hideKeyboard(getApplicationContext(), focusView);
            }
            showAlertDialogCancel();
        });

        mButtonSave.setOnClickListener(view -> {
            if ((editTextName.length() != 0) && (editTextPrice.length() != 0) && (editTextQuantity.length() != 0)) {
                String nameProduct = editTextName.getText().toString();
                String priceProduct = editTextPrice.getText().toString();
                String quantityProduct = editTextQuantity.getText().toString();

                int quantity = 0;
                try {
                    quantity = Integer.parseInt(quantityProduct);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), R.string.toastMessageQuantityIsZero, Toast.LENGTH_SHORT).show();
                }
                ProductDataStructure product = new ProductDataStructure(nameProduct, priceProduct,
                        quantity);

                if (mPositionItem != defaultPositionItem) {
                    mProductDataBase.setProduct(mPositionItem, product);
                } else {
                    mProductDataBase.addProduct(product);
                }

                mProductDataBase.writeData(getApplicationContext());
                mButtonCancel.setVisibility(View.GONE);
                mButtonSave.setVisibility(View.GONE);
                finish();
                Toast.makeText(getApplicationContext(), R.string.dataSaved, Toast.LENGTH_SHORT).show();
            } else {
                View focusView = this.getCurrentFocus();
                if (focusView != null) {
                    hideKeyboard(getApplicationContext(), focusView);
                }
                showAlertDialogFillAllFields();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mButtonSave.getVisibility() == View.VISIBLE && mButtonCancel.getVisibility() == View.VISIBLE) {
            showAlertDialogCancel();
        } else {
            finish();
        }
    }

    private void showAlertDialogCancel() {
        AlertDialog.Builder alertDialogBuilderCancel = new AlertDialog.Builder(this);
        alertDialogBuilderCancel
                .setMessage(R.string.unsavedDataMessage)
                .setCancelable(false)
                .setPositiveButton(R.string.buttonYes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.buttonNo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialogCancel = alertDialogBuilderCancel.create();
        alertDialogCancel.show();
    }

    private void showAlertDialogFillAllFields() {
        AlertDialog.Builder alertDialogBuilderCancel = new AlertDialog.Builder(this);
        alertDialogBuilderCancel
                .setMessage(R.string.fillFieldsOfProducts)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialogCancel = alertDialogBuilderCancel.create();
        alertDialogCancel.show();
    }

    private static void hideKeyboard(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
