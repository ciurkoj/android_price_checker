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

import com.example.pricechecker.fragments.adapters.NewsAdapter;
import com.example.pricechecker.model.NewsArticle;
import com.example.pricechecker.model.NewsViewModel;
import com.example.pricechecker.repository.Repository;
import com.google.android.gms.vision.barcode.Barcode;
import com.example.pricechecker.barcode_reader.BarcodeReaderFragment;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.google.protobuf.Any;

import org.json.JSONObject;

import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlinx.serialization.json.Json;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static java.sql.DriverManager.println;

public class ScanFragment extends Fragment implements BarcodeReaderFragment.BarcodeReaderListener {
    private static final String TAG = ScanFragment.class.getSimpleName();

    private BarcodeReaderFragment barcodeReader;
    private static TextView result_head;
    private static TextView result;
    private Button button;
    private Boolean buttonClicked = false;
    private Button next;
//    public static ScanFragment newInstance() {
//        Bundle args = new Bundle();
//        ScanFragment fragment = new ScanFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//    public  ListView listView;
//    public View view;
//    public CustomListAdapter adapter;

//    MainActivity activityName=(MainActivity)getActivity();
//    if(activityName!=null){
//        activityName.generateItemsList();
//    }
// =  generateItemsList();//new ArrayList<String>(5);

//    static {
//        itemsArrayList.add(0,"dupa");
//        itemsArrayList.add(1,"dupa");
//        itemsArrayList.add(2,"dupa");
//        itemsArrayList.add(3,"dupa");
//    }

    //    private ArrayList<Item> itemsList; //new ArrayList<Item>(5);
    private AndroidFlavorAdapter flavorAdapter;

    AndroidFlavor[] androidFlavors = {
            new AndroidFlavor("Cupcake", "1.5"),
            new AndroidFlavor("Donut", "1.6"),
            new AndroidFlavor("Eclair", "2.0-2.1"),
            new AndroidFlavor("Froyo", "2.2-2.2.3"),
            new AndroidFlavor("GingerBread", "2.3-2.3.7"),
            new AndroidFlavor("Honeycomb", "3.0-3.2.6"),
            new AndroidFlavor("Ice Cream Sandwich", "4.0-4.0.4"),
            new AndroidFlavor("Jelly Bean", "4.1-4.3.1"),
            new AndroidFlavor("KitKat", "4.4-4.4.4"),
            new AndroidFlavor("Lollipop", "5.0-5.1.1")
    };

    ArrayList<Item> itemsArrayList = new ArrayList<Item>() {
    };

    //    static {
//
//        itemsArrayList.add(0,"dupa");
//        itemsArrayList.add(1,"dupa");
//        itemsArrayList.add(2,"dupa");
//        itemsArrayList.add(3,"dupa");
//    }
    // calls function to get items list
//    private ArrayList<Item> generateItemsList() {
//        String itemNames[] = getResources().getStringArray(R.array.items_name);
//        String itemDescriptions[] = getResources().getStringArray(R.array.item_description);
//
//        ArrayList<Item> list = new ArrayList<>();
//
//        for (int i = 0; i < itemNames.length; i++) {
//            list.add(new Item(itemNames[i], itemDescriptions[i],itemDescriptions[i]));
//        }
//
//        return list;
//    }

    //    private ArrayList<Item> generateItemsList() {
//        ArrayList<Item> list = new ArrayList<>();
//        if(isAdded()) {
//            String itemNames[] = getResources().getStringArray(R.array.items_name);
//            String itemDescriptions[] = getResources().getStringArray(R.array.item_description);
//
//
//            for (int i = 0; i < itemNames.length; i++) {
//                list.add(new Item(itemNames[i], itemDescriptions[i]));
//            }
//            Log.e("Ssssssssssssssssssssssssssssss",String.valueOf(itemsArrayList));
//            return list;
//        }
//
//
//        println(String.valueOf(androidFlavors));
//        return list;
//    }
    ArrayList<NewsArticle> articleArrayList = new ArrayList<>();
    NewsViewModel newsViewModel;
    NewsAdapter newsAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        itemsArrayList = generateItemsList();
//        Log.e("Ssssssssssssssssssssssssssssss", String.valueOf(androidFlavors));
//        CustomListAdapter adapter = new CustomListAdapter(this, itemsArrayList);
//        itemsList = generateItemsList();
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        newsViewModel.init();
        newsAdapter = new NewsAdapter(this, articleArrayList);
        Log.e("Ssssssssssssssssssssssssssssss", "dupa1");
//        newsViewModel.getNewsRepository().observe(this, newsResponse -> {
//            List<NewsArticle> newsArticles = newsResponse.getArticles();
//            articleArrayList.addAll(newsArticles);
//            newsAdapter.notifyDataSetChanged();
//
//            Log.e("Ssssssssssssssssssssssssssssss", "dupa2");
//        });


    }
//    private Context mContext;
//    Activity activity = getActivity();
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        mContext = context;
//    }

    //    @Override
//    public void onDetach() {
//        super.onDetach();
//        mContext = null;
//    }
//    private FragmentAListener listener;
//
//    public interface FragmentAListener{
//        void onInputASent(CharSequence input);
//    }
//    @Override
//    public void onAttach(Context context){
//        super.onAttach(context);
//        if(context instanceof FragmentAListener){
//            listener = (FragmentAListener) context;
//        } else{
//            throw new RuntimeException(context.toString()
//            + " must implement FragmentAListener");
//        }
//    }
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        listener = null;
//    }
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
        ;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (buttonClicked == true) {
                    barcodeReader.onResume();
                    buttonClicked ^= true;


                } else {
                    barcodeReader.onPause();
                    buttonClicked ^= true;
                }

//                result.setText(buttonClicked.toString());
            }
        });
        next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //Yes
                ((MainActivity) getActivity()).swipeRight(2);
            }
        });
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnButtonClickListener.onButtonClicked(v);
//            }
//        });
//        result_head.setText("barcode.displayValue");
//        AndroidFlavorAdapter adapter = new AndroidFlavorAdapter(getActivity(), Arrays.asList(androidFlavors));
//        // Get a reference to the ListView, and attach this adapter to it.
//        ListView listView = (ListView) view.findViewById(R.id.listview_flavor);
//        listView.setAdapter(adapter);
//        ListView itemsListView  = (ListView)view.findViewById(R.id.listview_flavor);
//
//        //create adapter object
//        CustomListAdapter adapter = new CustomListAdapter(this, itemsArrayList);
//
//        //set custom adapter as adapter to our list view
//        itemsListView.setAdapter(adapter);

        return view;
    }


    @Override
    public void onScanned(final Barcode barcode) {
        Log.e(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();
        result_head.setText(barcode.displayValue);
        println(barcode.displayValue);
//        result_head.setTextColor(Color.WHITE);
        Repository repository = new Repository();
        MainActivityViewModelFactory viewModelFactory = new MainActivityViewModelFactory(repository);
        MainActivityViewModel viewModel = new ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel.class);
        HashMap<String, String> options = new HashMap();
        options.put("google_domain", "google.co.uk");

//        viewModel.getCustomQuery1("milk",options);
//        viewModel.myResponse1.observe(this, new Observer{ response ->
//            if (response.isSuccessful) {
//                mainResponse = response.body()
//                var mAdapter = MyCustomAdapter(this, mainResponse!!, swipeRefreshLayout)
//                listView?.adapter = mAdapter
//                mAdapter.notifyDataSetChanged()
//            } else {
//                Log.d("Response Error: ", response.errorBody().toString())
//            }
//        })
//        ListView listView = (ListView) view.findViewById(R.id.listview_flavor);

//        listView.setAdapter(flavorAdapter);
        Toast.makeText(getActivity(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();

        ListView listView = (ListView) getView().findViewById(R.id.listview_flavor);
//        AndroidFlavorAdapter adapter = new AndroidFlavorAdapter(getActivity(), Arrays.asList(androidFlavors));

        // Get a reference to the ListView, and attach this adapter to it.


        ListView itemsListView = (ListView) getView().findViewById(R.id.listview_flavor);


        //create adapter object

        TextView position_textview = getView().findViewById(R.id.result_head);
//        https://serpapi.com/search.json?engine=google&q=3574661098937&location=Coventry%2C+England%2C+United+Kingdom
//        &google_domain=google.co.uk&gl=uk&hl=en&tbm=shop&api_key=55eb0b1549172ab8e0ef83026fdc56fce225517f8006d98c78766c218f3217aa        TextView position_textview = getView().findViewById(R.id.result_head);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("q", barcode.displayValue);
        parameters.put("location", "Coventry, England, United Kingdom");
        parameters.put("google_domain", "google.co.uk");
//        parameters.put("device", "desktop");
        parameters.put("gl", "uk");
        parameters.put("hl", "en");
        parameters.put("tbm", "shop");
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
                    position_textview.setText("Code: " + response.code());
                    Log.e("TAG1", response.toString());



                }
//                List posts = response.body();
//                for (Post post : posts) {
//                    String content = "";
//                    content += "ID: " + post.getId() + "\n";
//                    content += "User ID: " + post.getUserId() + "\n";
//                    content += "Title: " + post.getTitle() + "\n";
//                    content += "Text: " + post.getText() + "\n\n";
//                    position_textview.append(content);
//                }
                Gson gsone = new Gson();
                Type typee = new TypeToken<BodyResponse>() {}.getType();
                BodyResponse posts = gsone.fromJson(response.body().toString(), typee);
//                BodyResponse posts =  (BodyResponse) response.body();

                    String content = "";
                    content += "ID: " + posts.getSearch_metadata() + "\n";
                    content += "User ID: " + posts.getSearch_parameters() + "\n";
                    content += "Title: " + posts.getSearch_information() + "\n";
                    content += "Text: " + posts.getShopping_results() + "\n\n";
                    position_textview.append(content);
//                    System.out.printf(content);
                Log.e("aaaaaaaaaaaaaaaaaaaaaa", posts.getShopping_results().toString());
                Gson gson = new Gson();
                String string = "{\"userId\": 1,\"id\": 1,\"title\": \"sunt aut facere repellat provident occaecati excepturi optio reprehenderit\",\"body\": \"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"}";
                Log.e("aaaaaaaaaaaaaaaaaaaaaa", string.toString());
                String string2 = "{\"position\": 1,\"title\": \"Neutrogena Norwegian Formula Deep Moisture Body Lotion 400Ml\",\"link\": \"https://www.google.co.uk/aclk?sa=L&ai=DChcSEwiQkbWr1PzvAhWY8OMHHWZ0A1UYABADGgJ5bQ&sig=AOD64_3kMD91rDnP9jQ4HnygfNyf3LIMzg&ctype=5&q=&ved=0ahUKEwjMmLGr1PzvAhUFUa0KHSgjB34Qg-UECGU&adurl=\",\"product_link\": \"https://google.com/shopping/product/6805029257449863576\",\"product_id\": \"6805029257449863576\",\"serpapi_product_api\": \"https://serpapi.com/search.json?device=desktop&engine=google_product&gl=uk&google_domain=google.co.uk&hl=en&location=Coventry%2C+England%2C+United+Kingdom&product_id=6805029257449863576\",\"source\": \"Tesco Groceries\",\"price\": \"£5.00\",\"extracted_price\": 5.0,\"rating\": 4.6,\"reviews\": 782,\"snippet\": \"Neutrogena · Neutrogena Deep Moisture · Body · For Dry Skin · SPF 25 · 400ml · Oil-free\",\"thumbnail\": \"https://serpapi.com/searches/60764f6bfe41d0397861024e/images/a9ca48b553ff76feabe0d90a9236741ee7754990456048e8b221ad36fc66fe0c.webp\"}";
//                Log.e("aaaaaaaaaaaaaaaaaaaaaa", string2.toString());

                Type type = new TypeToken<Post>() {}.getType();
                Post servDto = gson.fromJson(string, type);

                Type type1 = new TypeToken<ShoppingResult>() {}.getType();
                JsonReader reader = new JsonReader(new StringReader(String.valueOf(posts.getShopping_results())));
                reader.setLenient(true);
                Log.e("aaaaaaaaaaaaaaaaaaaaaa", response.body().toString());
                ShoppingResult shops = posts.getShopping_results().get(0);
                Log.e("aaaaaaaaaaaaaaaaaaaaaeeeeedsdada", posts.getShopping_results().get(0).getTitle().toString());
//                ShoppingResult shops1 = gson.fromJson(shops.toString(), type1);
//                List<ShoppingResult> name = stringToArray(String.valueOf(posts.getShopping_results().get(0)), ShoppingResult[].class);//.get(0).getTitle();

                Log.e("bbbbbbbbbbbbbbbbbbbbbbbbbbb", String.valueOf(posts.getShopping_results().get(0).getTitle().toString()));
                Log.e("bbbbbbbbbbbbbbbbbbbbbbbbbbb", shops.getTitle().toString());

                position_textview.setText("Code: " + response.code());

                itemsArrayList.add(new Item(shops.getTitle(), shops.getSource(), shops.getThumbnail()));

//                position_textview.setText("Codeee: " + response.code());

            }



            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
//                position_textview.setText(t.getMessage());
                Log.e("TAG Error", t.getMessage());
            }
        });

        CustomListAdapter adapter = new CustomListAdapter(this, itemsArrayList);
        //set custom adapter as adapter to our list view
        itemsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        barcodeReader.onPause();
        buttonClicked ^= true;
    }
    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }
    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
//        Log.e(TAG, "onScannedMultiple: " + barcodes.size());
//
//        String codes = "";
//        for (Barcode barcode : barcodes) {
//            codes += barcode.displayValue + ", ";
//        }
//
//        final String finalCodes = codes;
//        result_head.setText(finalCodes);
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

