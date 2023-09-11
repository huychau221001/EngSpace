//package com.example.engspace;
//
//import android.os.Bundle;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//public class SearchTest extends AppCompatActivity {
//
//    private AutoCompleteTextView textCountry;
//
//    private String[] countries = {"Vietnam", "England", "Canada", "France", "Australia"};
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        textCountry = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
//
//        ArrayAdapter adapterCountries
//                = new ArrayAdapter(this, android.R.layout.simple_list_item_1, countries);
//
//        textCountry.setAdapter(adapterCountries);
//
//        // Set the minimum number of characters, to show suggestions
//        textCountry.setThreshold(1);
//    }
//}
