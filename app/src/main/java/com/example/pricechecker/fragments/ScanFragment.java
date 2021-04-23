package com.example.pricechecker.fragments;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.JsonReader;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.pricechecker.MainActivity;
import com.example.pricechecker.MainActivityViewModel;
import com.example.pricechecker.MainActivityViewModelFactory;
import com.example.pricechecker.ProductActivity;
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
    ListView itemsListView;
    CustomListAdapter adapter;
    ArrayList<Item> itemsArrayList = new ArrayList<Item>() {
    };

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
        result_head = (TextView) view.findViewById(R.id.result_head);
        button = (Button) view.findViewById(R.id.btn_fragment);
        next = (Button) view.findViewById(R.id.next_fragment);
        itemsListView = (ListView) view.findViewById(R.id.listview_flavor);
        adapter = new CustomListAdapter(getContext(), itemsArrayList);
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
                Intent intent = new Intent(getActivity(), ProductActivity.class);
                intent.putExtra("itemTitle", "A product");
                intent.putExtra("itemPrice", "worthless");
                intent.putExtra("itemSource", "from nowhere");
                intent.putExtra("thumbnailUrl", "https://imgix.bustle.com/uploads/image/2021/1/14/f1499ba8-d5f9-41b0-8a21-61ba6cec190d-happy-baby-yoda.jpeg?w=1200&h=630&fit=crop&crop=faces&fm=jpg");
                startActivity(intent);
            }
        });
        scanFragmentSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.scan_fragment_SwipeRefreshLayout);
        scanFragmentSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("LOG_TAG", "onRefresh called from SwipeRefreshLayout");
                scanFragmentSwipeRefreshLayout.setRefreshing(false);
                onScanned(globalBarcode);
            }
        });
        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Repository.Companion.getInstance().getLiveProgress().setValue(itemsArrayList.get(position).getItemTitle());
                ((MainActivity) getActivity()).swipeRight(2);
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
        result_head.setText("Searching for: " + barcode.displayValue);
        scanFragmentSwipeRefreshLayout.setRefreshing(true);
        println(barcode.displayValue);
        Repository repository = new Repository();
        MainActivityViewModelFactory viewModelFactory = new MainActivityViewModelFactory(repository);
        MainActivityViewModel viewModel = new ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel.class);
        HashMap<String, String> options = new HashMap();
        options.put("google_domain", "google.co.uk");
        Map<String, String> parameters = new HashMap<>();
        parameters.put("q", barcode.displayValue);
        parameters.put("tbm", "shop");
        parameters.put("location", "Coventry, England, United Kingdom");
        parameters.put("google_domain", "google.co.uk");
        parameters.put("gl", "uk");
        parameters.put("hl", "en");
        parameters.put("no_cache", "true");
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
                    Log.e("Ddddsdad", response.toString());
                    Gson gson = new Gson();
                    Type type = new TypeToken<BodyResponse>() {
                    }.getType();
                    BodyResponse search_response = gson.fromJson(response.body(), type);
                    try {
                        for (ShoppingResult result : search_response.getShopping_results()) {
                            itemsArrayList.add(new Item(result.getResultTitle(), result.getResultPrice(), result.getResultSource(), result.getResultThumbnail()));
                            result_head.setText("Success! Found: " + search_response.getShopping_results().get(0).getResultTitle());
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), "Sorry, couldn't recognize the code. Please try again or use manual search", Toast.LENGTH_SHORT).show();
                    }
                }
                scanFragmentSwipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Click on an item to get more information about it.", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("TAG Error", t.getMessage());
            }
        });
        adapter.notifyDataSetChanged();
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

