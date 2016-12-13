package com.manciniprogramming.soap_example_android;

/**
 * Created by user on 12/13/2016.
 */


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.StrictMode;
import android.util.Log;

public class SOAPClass {
    private final String NAMESPACE = "http://www.webserviceX.NET/";
    private final String URL = "http://www.webservicex.net/CurrencyConvertor.asmx";
    private final String SOAP_ACTION = "http://www.webserviceX.NET/ConversionRate";
    private final String METHOD_NAME = "ConversionRate";

    public SOAPClass() {
    }

    public String remotereq(String to_curr, String from_curr, String value) {

        // Strict mode is defined because executing network operations in the
        // main
        // thread will give exception
        // Strict mode is available only above version 9
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Create the soap request object
        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

        // Convert from
        String fromUnit = from_curr;
        // Convert to
        String toUnit = to_curr;
        if (value == null) {
            value = "1";
        }

        // Set the property info for the to currency
        PropertyInfo FromCurrency = new PropertyInfo();
        FromCurrency.setName("FromCurrency");
        FromCurrency.setValue(fromUnit);
        FromCurrency.setType(String.class);
        request.addProperty(FromCurrency);

        PropertyInfo ToCurrency = new PropertyInfo();
        ToCurrency.setName("ToCurrency");
        ToCurrency.setValue(toUnit);
        ToCurrency.setType(String.class);
        request.addProperty(ToCurrency);

        // Create the envelop.Envelop will be used to send the request
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11);
        envelope.setOutputSoapObject(request);
        // Says that the soap webservice is a .Net service
        envelope.dotNet = true;

        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
        String s = "";
        Double x = 1.0;
        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);

            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
            // TextView v=(TextView) findViewById(R.id.currency);
            // v.setText("1" +fromUnit +"=" +response.toString()+toUnit);

            // Output to the log
            s = response.toString();
            x = Double.parseDouble(s) * Double.parseDouble(value);
            Log.d("Converter", response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return x.toString();

    }

}