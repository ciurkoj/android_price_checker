package com.example.pricechecker.fragments;

import android.os.Bundle;

import android.util.JsonReader;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.pricechecker.MainActivity;
import com.example.pricechecker.MainActivityViewModel;
import com.example.pricechecker.MainActivityViewModelFactory;
import com.example.pricechecker.R;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.example.pricechecker.repository.Repository;
import com.google.android.gms.vision.barcode.Barcode;
import com.example.pricechecker.barcode_reader.BarcodeReaderFragment;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.sql.DriverManager.println;
import static java.sql.DriverManager.setLogWriter;

public class ScanFragment extends Fragment implements BarcodeReaderFragment.BarcodeReaderListener {
    private static final String TAG = ScanFragment.class.getSimpleName();

    private BarcodeReaderFragment barcodeReader;
    private static TextView result_head;
    private static TextView result;
    private Button button;
    private Boolean buttonClicked = false;
    private Button next;
    private SwipeRefreshLayout scanFragmentSwipeRefreshLayout;
    private Barcode globalBarcode;
    ListView itemsListView ;
    CustomListAdapter adapter;
    ArrayList<Item> itemsArrayList = new ArrayList<Item>() {};



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        barcodeReader = (BarcodeReaderFragment) getChildFragmentManager().findFragmentById(R.id.scan_fragment);
        barcodeReader.setListener(this);
        barcodeReader.onPause();
//        result_head = setText(barcode.displayValue);
        result_head = (TextView) view.findViewById(R.id.result_head);
        button = (Button) view.findViewById(R.id.btn_fragment);
        next = (Button) view.findViewById(R.id.next_fragment);

        itemsListView = (ListView) view.findViewById(R.id.listview_flavor);
//        itemsArrayList.add(new Item("DUPA", "shops.getSource()", "https://serpapi.com/searches/60764f6bfe41d0397861024e/images/a9ca48b553ff76feabe0d90a9236741ee7754990456048e8b221ad36fc66fe0c.webp"));
        adapter = new CustomListAdapter(this, itemsArrayList);
        //set custom adapter as adapter to our list view
        itemsListView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (buttonClicked == true) {
                    barcodeReader.onResume();
                    buttonClicked ^= true;


                } else {
                    barcodeReader.onPause();
                    buttonClicked ^= true;
                }

            }
        });
        next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Yes
                ((MainActivity) getActivity()).swipeRight(2);
            }
        });
//
        scanFragmentSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.scan_fragment_SwipeRefreshLayout);
        scanFragmentSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("LOG_TAG", "onRefresh called from SwipeRefreshLayout");
                scanFragmentSwipeRefreshLayout.setRefreshing(false);
                onScanned(globalBarcode);
            }
        });
        return view;
    }


    @Override
    public void onScanned(final Barcode barcode) {
        globalBarcode = barcode;
        itemsArrayList.clear();
        Log.e(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();
        result_head.setText(barcode.displayValue);
        scanFragmentSwipeRefreshLayout.setRefreshing(true);

        println(barcode.displayValue);
        Repository repository = new Repository();
        MainActivityViewModelFactory viewModelFactory = new MainActivityViewModelFactory(repository);
        MainActivityViewModel viewModel = new ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel.class);
        HashMap<String, String> options = new HashMap();
        options.put("google_domain", "google.co.uk");


        Toast.makeText(getActivity(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();

//        ListView listView = (ListView) getView().findViewById(R.id.listview_flavor);

        // Get a reference to the ListView, and attach this adapter to it.





        //create adapter object


        Map<String, String> parameters = new HashMap<>();
        parameters.put("q", barcode.displayValue);
        parameters.put("location", "Greater London, England, United Kingdom");
        parameters.put("google_domain", "google.co.uk");
//        parameters.put("device", "mobile");
        parameters.put("gl", "uk");
        parameters.put("hl", "en");
        parameters.put("tbm", "shop");
//        parameters.put("filter", "0");
        parameters.put("no_cache", "true");
        Log.e("Ddddsdad","scan before");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://serpapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);
        Call<JsonObject> call = jsonPlaceHolderApi.getPosts(parameters);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    result_head.setText("Code: " + response.code());
                    Log.e("TAG1", response.toString());

                }
                Gson gson = new Gson();
                Type type = new TypeToken<BodyResponse>() {}.getType();
                BodyResponse posts = gson.fromJson(response.body().toString(), type);

//                Log.e("sssssssssssssssssSSSSSSSSS", String.valueOf(posts.getShopping_results().size()));
                try{
                    for(ShoppingResult result : posts.getShopping_results()){
                        Log.e("sss", result.getResultTitle());
                        Log.e("sss", result.getResultSource());
                        Log.e("sss", result.getResultPrice());
                        Log.e("sss", result.getResultThumbnail());
                        itemsArrayList.add(new Item(result.getResultTitle(), result.getResultPrice(), result.getResultSource(), result.getResultThumbnail()));

                    }
                }catch(Exception e){
                    Toast.makeText(getActivity(), "Sorry, couldn't recognize the code. Please try again or use manual search", Toast.LENGTH_SHORT).show();
                }

//                ShoppingResult shops = posts.getShopping_results().get(0);
//                result_head.setText("Code: " + response.code());
//                itemsArrayList.add(new Item(shops.getTitle(), shops.getSource(), shops.getThumbnail()));
                scanFragmentSwipeRefreshLayout.setRefreshing(false);
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG Error", t.getMessage());
            }
        });

//        adapter = new CustomListAdapter(this, itemsArrayList);
        //set custom adapter as adapter to our list view
//        itemsListView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//        itemsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        Log.e("Ddddsdad","scan after");
//        listView.setAdapter(adapter);
        barcodeReader.onPause();
        buttonClicked ^= true;
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
        Toast.makeText(getActivity(), "Please scan only one barcode at once ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String errorMessage) {
        Log.e(TAG, "onScanError: " + errorMessage);
    }

    @Override
    public void onCameraPermissionDenied() {
        Toast.makeText(getActivity(), "Camera permission denied!", Toast.LENGTH_LONG).show();
    }
}

