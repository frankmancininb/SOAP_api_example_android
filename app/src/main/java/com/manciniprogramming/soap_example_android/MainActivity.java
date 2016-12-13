package com.manciniprogramming.soap_example_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    String[] country = { "British", "European", "American", "Indian" };

    String[] currency = { "Pounds ", "Euro", "Dollor", "Rupee" };
    String[] currency_abr = { "GBP", "EUR", "USD", "INR" };
    String to_curr, from_curr;

    // int arr_images[] = { R.drawable.pound, R.drawable.euro, R.drawable.dollor,
    //         R.drawable.rupee };
    TextView converted;
    Spinner from_currency;
    Spinner to_currency;
    EditText input;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        to_curr = "INR";
        from_curr = "USD";
        setContentView(R.layout.activity_main);
        converted = (TextView) findViewById(R.id.textView2);
        input = (EditText) findViewById(R.id.editText1);
        converted.setText("");

        from_currency = (Spinner) findViewById(R.id.spinner1);
        from_currency.setAdapter(new MyAdapter(MainActivity.this,
                R.layout.row, country));
        from_currency.setSelection(2);
        to_currency = (Spinner) findViewById(R.id.spinner2);
        to_currency.setAdapter(new MyAdapter(MainActivity.this,
                R.layout.row, country));
        to_currency.setSelection(3);
        from_currency.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View v,
                                       int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "" + currency_abr[position], Toast.LENGTH_LONG).show();
                converted.setHint(currency[position]);
                from_curr = currency_abr[position];

            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        to_currency.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parentView, View v,
                                       int position, long id) {
                Toast.makeText(getApplicationContext(),
                        "" + currency_abr[position], Toast.LENGTH_LONG).show();
                converted.setHint(currency[position]);
                to_curr = currency_abr[position];

            }

            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new DownloadData().execute(to_curr, from_curr, input.getText()
                        .toString());
            }
        });

        converted = (TextView) findViewById(R.id.textView2);
    }

    class DownloadData extends AsyncTask<String, String, String> {

        ProgressDialog pd = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setTitle("Converting...");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String theResult = "";
            String to_curr = params[0];
            String from_curr = params[1];
            String value = params[2];

            try {
                theResult = new SOAPClass().remotereq(to_curr, from_curr, value);

            }

            catch (Exception e) {
                e.printStackTrace();
            }

            return theResult;
        }

        @Override
        protected void onPostExecute(String theResult) {
            super.onPostExecute(theResult);
            pd.dismiss();
            double x;
            try {
                x = Double.parseDouble(theResult);
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                x = 1;
                input.setText("");
            }
            converted.setText(new Double(x).toString());
        }
    }

    public class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int textViewResourceId,
                         String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView,
                                  ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.row, parent, false);
            TextView label = (TextView) row.findViewById(R.id.country);
            label.setText(country[position]);

            TextView sub = (TextView) row.findViewById(R.id.currency);
            sub.setText(currency[position]);

            ImageView icon = (ImageView) row.findViewById(R.id.image);
            //icon.setImageResource(arr_images[position]);

            return row;
        }
    }
}