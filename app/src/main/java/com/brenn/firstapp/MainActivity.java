package com.brenn.firstapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import entite.Personne;

public class MainActivity extends AppCompatActivity { private TextView welcome;
private EditText nom;
private EditText prenom;
private Button valider;
private Button vider;
ArrayList<Personne> listePersonne ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        welcome = findViewById(R.id.welcome);
        valider = findViewById(R.id.valider);
        vider = findViewById(R.id.vider);
        listePersonne = new ArrayList<>();
       valider.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          //  welcome.setText("Bienvenue : "+prenom.getText()+" "+nom.getText());
                                          ajouterPersonne(nom.getText().toString(), prenom.getText().toString());
                                          welcome.setText(listePersonne.size() + " personne(s) enregistree(s)!");
                                          // onResume();
                                          Log.i("lancement thread","suspens ");
                                          new Thread(new Runnable(){
                                              HttpURLConnection urlConnection;
                                              public void run(){
                                                  try {
                                                  URL url = new URL("http://192.168.0.56:8080/WSA/resources/assemblee/"+nom.getText());

                                                      urlConnection = (HttpURLConnection) url.openConnection();

                                                  urlConnection.setRequestMethod("GET");
                                                  Log.i("apres connexion","suspens :"+url.getContent());
                                                  InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                                  Scanner scaner = new Scanner(in);

                                                  //////
                                                 // Type listType = new TypeToken<ArrayList<Personne>>(){}.getType();
                                                     // List<Personne> assemblee = new Gson().fromJson(scaner.nextLine(), listType);
                                                   ////////
                                                      Personne msg =new Gson().fromJson(scaner.nextLine(), Personne.class);
                                                  //Personne msg = new Genson().deserialize(scaner.nextLine(), Personne.class);
                                                //  Log.i("exchanhe json","result: "+msg);

                                                  runOnUiThread(new Runnable() {
                                                      @Override
                                                      public void run() {
                                                          welcome.setText("Bienvenue! : "+msg.getNom()+ " "+msg.getPrenom());//+getLien());
                                                     nom.setText(msg.getNom()); prenom.setText(msg.getPrenom());
                                                      }
                                                  });

                                                  } catch (IOException e) {
                                                      throw new RuntimeException(e);
                                                  }
                                              }
                                          }).start();

                                      }
                                  });


       vider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nom.setText("");
                prenom.setText("");
                welcome.setText("");
            }
        });

        init();
       }
    public void ajouterPersonne(String n, String p){
        Personne pers1 = new Personne(n,p);
         listePersonne.add(pers1);
    }
    public void init(){
        nom.setText("");
        prenom.setText("");
    }
    /*protected void onResume(){
        super.onResume();
        new Thread(new Runnable(){
            public void run() {
                final String result =getLien();


                runOnUiThread(new Runnable(){
                 @Override
                 public void run() {
                  welcome.setText(result);
                    }
                });
            }
        }).start();

    }*/


}