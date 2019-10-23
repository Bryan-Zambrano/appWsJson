package com.example.appwsjson;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import WebServices.Asynchtask;
import WebServices.WebService;

public class MainActivity extends AppCompatActivity implements Asynchtask {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Configuracion de la barra scroll de desplazamaiento para el control TextView
        TextView tvDatos = findViewById(R.id.tvDatos);
        tvDatos.setMovementMethod(new ScrollingMovementMethod());
    }

    //Metodo que hace el llamado al web service
    public void ejecutarWebService(View dt){
        Map<String, String> datos = new HashMap<String, String>();
        WebService ws= new WebService("https://api.androidhive.info/contacts/", datos,
                MainActivity.this, MainActivity.this);
        ws.execute("");
    }
    TextView txtSaludo;
    @Override
    public void processFinish(String result) throws JSONException {
        Log.i("processFinish", result);
        txtSaludo = (TextView)findViewById(R.id.tvDatos);
        ArrayList<HashMap<String, String>>  datosJSON=obtenerinfoJSON(result);
        String info=parsearInfoJSON(datosJSON);
        txtSaludo.setText(info);

    }

    //Metodo que obtiene nuestro arraylist del JSON
    public ArrayList<HashMap<String, String>>  obtenerinfoJSON( String jsonStr) throws JSONException {
        ArrayList<HashMap<String, String>>  contactList = new ArrayList<>();
        JSONObject jsonObj= new JSONObject(jsonStr);
        JSONArray contacts=jsonObj.getJSONArray("contacts");
        for(int i=0;i<contacts.length();i++){
            JSONObject c= contacts.getJSONObject(i);
            String id= c.getString("id");
            String name= c.getString("name");
            String email= c.getString("email");
            String address= c.getString("address");
            String gender= c.getString("gender");

            JSONObject phone= c.getJSONObject("phone");
            String mobile= phone.getString("mobile");
            String home= phone.getString("home");
            String office= phone.getString("office");

            HashMap<String,String>contact=new HashMap<>();
            contact.put("id",id);
            contact.put("name",name);
            contact.put("email",email);
            contact.put("mobile",mobile);
            contactList.add(contact);
        }
        //Object dt =contactList.get(1);
        //String info=contactList.get(1).get("id")+contactList.get(1).get("name");
        //txtSaludo.setText(info);
        //txtSaludo.setText((CharSequence) contactList.get(1).get("email"));
        //txtSaludo.setText((CharSequence) contactList.get(1).get("mobile"));
        return contactList;
    }


    //Conversion de la info de nuestro arraylist para enviar al control
    public String parsearInfoJSON( ArrayList<HashMap<String, String>>  contactList){
        String info="";
        for(int x=0;x<contactList.size();x++){
            HashMap<String,String> dato=contactList.get(x);
            for(Map.Entry<String,String> z:dato.entrySet()){
                info=info+""+z.getKey()+": "+z.getValue()+"\n";
            }
            info=info+"\n";
        }
        return info;
    }
}
