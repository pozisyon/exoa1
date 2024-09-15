package com.brenn.firstapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.digicelgroup.moncash.APIContext;
import com.digicelgroup.moncash.exception.MonCashRestException;
import com.digicelgroup.moncash.http.Constants;
import com.digicelgroup.moncash.payments.Payment;
import com.digicelgroup.moncash.payments.PaymentCreator;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpStatus;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
                                                  URL url = new URL("http://192.168.0.54:8080/WebSAndroid/resources/jakartaee10");

                                                      urlConnection = (HttpURLConnection) url.openConnection();

                                                  urlConnection.setRequestMethod("GET");
                                                  Log.i("apres connexion","suspens :"+url.getContent());
                                                  InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                                                  Scanner scaner = new Scanner(in);
                                                      Personne msg =new Gson().fromJson(scaner.nextLine(), Personne.class);
                                                  //Personne msg = new Genson().deserialize(scaner.nextLine(), Personne.class);
                                                  Log.i("exchanhe json","result: "+msg);

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
    public String getLien() {
        String result = null;
        try {
            // Jsonb jsonb = JsonbBuilder.create();
            APIContext apiContext = new APIContext("5838bd68e8fabe53507fa92da46b0014", "oHrr4tbnB1PH0uz6VQNUvRPZyja7WAWuVqtmgxKtXLJcjqCCldeIbPY2BpptZc76", Constants.SANDBOX);

            PaymentCreator paymentCreator = new PaymentCreator();
            Payment payment = new Payment();
            payment.setOrderId(System.currentTimeMillis() + "");
            payment.setAmount(50);
            Log.i("msg","TEST AVANT PAYMENT CREATOR avant possible ERREUR");
            PaymentCreator creator = paymentCreator.execute(apiContext, PaymentCreator.class, payment);
            Log.i("msg"," "+"test avant possible ERREUR");
            if (creator.getStatus() != null && creator.getStatus().compareTo(HttpStatus.SC_ACCEPTED + "") == 0) {
                Log.i("msg"," "+"redirect to the link below");
//creator.redirectUri() method return the payment gateway url
                Log.i("msg","LIEN DE REDIRECTION : " + creator.redirectUri());
                result = creator.redirectUri();

                /// webDrive.get(result);

                // Making thread to sleep for 2 seconds


            } else if (creator.getStatus() == null) {
                Log.i("msg","Error");
               // System.out.println(creator.getError());
                Log.i("msg"," "+creator.getError_description());
            } else {
                Log.i("msg","Error");
                Log.i("msg"," "+creator.getStatus());
                Log.i("msg"," "+creator.getError());
                Log.i("msg"," "+creator.getMessage());
                Log.i("msg"," "+creator.getPath());
            }


        } catch(MonCashRestException ex){ Log.i("msg"," "+"ERREUR*************************: "+ex.getMessage());}


        return result;
    }


}