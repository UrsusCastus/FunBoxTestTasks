package com.example.ecommerceapp;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class ProductDataBase {
    private static final String TAG_PRODUCT_DATA_BASE = "ProductDataBase";

    private static ProductDataBase mInstanceDataBase;

    private final String csvFileName = "data.csv";
    private Context mContext;
    private List<ProductDataStructure> mProductList = new ArrayList<ProductDataStructure>();
    private List<ProductDataStructure> mBuyProductList = new ArrayList<ProductDataStructure>();
    public PublishSubject mPublishSubject = PublishSubject.create();

    private ProductDataBase(Context context) {
        mContext = context;
        getData(mContext);
    }

    public static ProductDataBase getInstanceDataBase(Context context) {
        if (mInstanceDataBase == null) {
            mInstanceDataBase = new ProductDataBase(context);
        }
        return mInstanceDataBase;
    }

    private void getData(Context context) {
        Observable.fromCallable(() -> getDataFromCsvFile(context))
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            mProductList = result;
                            mPublishSubject.onNext(mProductList);
                        },
                        error -> error.printStackTrace());
    }

    public void writeData(Context context) {
        Observable.fromRunnable(() -> writeDataToCsvFileOnInternalStorage(context))
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void removeQuotesAndSpaceOut(StringBuilder stringBuilder) {
        if (stringBuilder.charAt(0) == ' ') {
            stringBuilder.deleteCharAt(0);
        }

        if (stringBuilder.charAt(0) == '"' && stringBuilder.charAt(stringBuilder.length() - 1) == '"') {
            stringBuilder.deleteCharAt(0);
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
    }

    private List<ProductDataStructure> getDataFromCsvFile(Context context) {
        Log.e(TAG_PRODUCT_DATA_BASE, "Thread - " + Thread.currentThread().getName());
        List<ProductDataStructure> productList = new ArrayList<ProductDataStructure>();
        String path = context.getFilesDir().getAbsolutePath() + "/" + csvFileName;
        File file = new File(path);
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        try {
            if (file.exists()) {
                //файл присутствует - получаем данные из internalStorage
                inputStreamReader = new InputStreamReader(context.openFileInput(csvFileName));
            } else {
                //файл отсутствует - получаем данные из assets
                inputStream = mContext.getAssets().open(csvFileName);
                inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            }
            bufferedReader = new BufferedReader(inputStreamReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] row = line.split(mContext.getString(R.string.csvVirguleSplit));
                StringBuilder nameProduct = new StringBuilder(row[0]);
                StringBuilder priceProduct = new StringBuilder(row[1]);
                StringBuilder quantityProduct = new StringBuilder(row[2]);

                removeQuotesAndSpaceOut(nameProduct);
                removeQuotesAndSpaceOut(priceProduct);
                removeQuotesAndSpaceOut(quantityProduct);

                try {
                    int quantity = Integer.parseInt(quantityProduct.toString());
                    ProductDataStructure product = new ProductDataStructure(nameProduct.toString(),
                            priceProduct.toString(), quantity);
                    productList.add(product);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return productList;
    }

    public int getProductCount() {
        return mProductList.size();
    }

    public ProductDataStructure getProductFromDataBase(int position) {
        return mProductList.get(position);
    }

    public void addProduct(ProductDataStructure product) {
        mProductList.add(product);
    }

    public void setProduct(int position, ProductDataStructure product) {
        mProductList.set(position, product);
    }

    public void setBuyProduct(int position, ProductDataStructure product) {
        mBuyProductList.set(position, product);
    }

    public void updateBuyProductList() {
        mBuyProductList.clear();
        for (int i = 0; i < mProductList.size(); i++) {
            if (mProductList.get(i).getQuantityProduct() > 0) {
                mBuyProductList.add(mProductList.get(i));
            }
        }
    }

    public int getBuyProductCount() {
        return mBuyProductList.size();
    }

    public ProductDataStructure getBuyProductFromDataBase(int index) {
        return mBuyProductList.get(index);
    }

    private void writeDataToCsvFileOnInternalStorage(Context context) {
        Log.e(TAG_PRODUCT_DATA_BASE, "Thread - " + Thread.currentThread().getName());
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(context
                    .openFileOutput(csvFileName, Context.MODE_PRIVATE)));
            for (int i = 0; i < mProductList.size(); i++) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append('"').append(mProductList.get(i).getNameProduct()).append('"');
                stringBuilder.append(", ");
                stringBuilder.append('"').append(mProductList.get(i).getPriceProduct()).append('"');
                stringBuilder.append(", ").append('"').append(mProductList.get(i).getQuantityProduct()).append('"');
                bufferedWriter.write(stringBuilder.toString());
                if (i != mProductList.size() - 1) {
                    bufferedWriter.write('\n');
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
