package com.example.emotion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmotionActivity extends AppCompatActivity  {
    TextView userName,email;
    ImageView happy,unHappy,normal;
    String name,emailUser;
    List<Product> products= new ArrayList<>();;
    ListViewAdapter listViewAdapter;
    ListView listView;
    Button createBtn;

    String url = "https://60b084751f26610017ffe50d.mockapi.io/product";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);
        userName = findViewById(R.id.username);
        email = findViewById(R.id.email);

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(currentFirebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        name = snapshot.child("name").getValue().toString();
                        emailUser = snapshot.child("email").getValue().toString();
                        userName.setText(name);
                        email.setText(emailUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
      //  products =
         this.GetArrayJson(url);
      //  Log.d("Object","Product"+products);

        createBtn = findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(getApplicationContext(),create.class);
                startActivity(intent);
            }
        });


    }

    private List<Product> GetArrayJson(String url){
        List<Product> list =new ArrayList<>();
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(url,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i=0; i<response.length(); i++){
                                    try {

                                        JSONObject object = (JSONObject) response.get(i);
                                        String id = object.getString("id");
                                        String name = object.getString("name");
                                        String brand = object.getString("brand");
                                        Product product = new Product(id,name,brand);
                                        Log.d("Object","Product"+product);
                                        list.add(product);
                                     //   Log.d(null,"Litst 1"+list);
                                        products.add(product);
                                        listViewAdapter = new ListViewAdapter(products,getApplicationContext(),R.layout.item_row);
                                        listView = findViewById(R.id.productList);
                                        listView.setAdapter(listViewAdapter);
                                      //  Log.d(null,"Litst 2"+list);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmotionActivity.this, "Error by get Json Array!", Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
      //  Log.d(null,"List"+list);
        return list;
    }

    private void PutApi(String url){
        StringRequest stringRequest = new StringRequest(
                Request.Method.PUT, url + '/' + 28, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(EmotionActivity.this, "Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EmotionActivity.this, "Error by Post data!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("name", "Lâm");
                params.put("age", "30");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}