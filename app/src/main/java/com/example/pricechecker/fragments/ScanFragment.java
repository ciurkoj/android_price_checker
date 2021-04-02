package com.example.pricechecker.fragments;

import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.pricechecker.R;
import com.google.android.gms.vision.barcode.Barcode;
import com.example.pricechecker.barcode_reader.BarcodeReaderFragment;

import java.util.List;

import static com.example.pricechecker.R.id.barcode_fragment;
import static com.example.pricechecker.R.id.result_head;

public class ScanFragment extends Fragment implements BarcodeReaderFragment.BarcodeReaderListener {
    private static final String TAG = ScanFragment.class.getSimpleName();

    private BarcodeReaderFragment barcodeReader;
    private static TextView result_head;
    private static TextView result;
    private Button button;
    private Boolean buttonClicked = false;

    public static ScanFragment newInstance() {
        Bundle args = new Bundle();
        ScanFragment fragment = new ScanFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        result = (TextView) view.findViewById(R.id.result);
        button = (Button) view.findViewById(R.id.btn_fragment);
        button.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                if(buttonClicked == true){
                    barcodeReader.onResume();
                    buttonClicked^= true;
                }else{
                    barcodeReader.onPause();
                    buttonClicked^= true;
                }

//                result.setText(buttonClicked.toString());
            }
        } );
//        result_head.setText("barcode.displayValue");
        return view;
    }

    @Override
    public void onScanned(final Barcode barcode) {
        Log.e(TAG, "onScanned: " + barcode.displayValue);
        barcodeReader.playBeep();
        result_head.setText(barcode.displayValue);
//        result_head.setTextColor(Color.WHITE);
        Toast.makeText(getActivity(), "Barcode: " + barcode.displayValue, Toast.LENGTH_SHORT).show();
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