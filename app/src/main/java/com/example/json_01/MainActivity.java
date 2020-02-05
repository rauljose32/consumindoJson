/*https://stackoverflow.com/questions/58889465/json-parsing-error-value-jsonstr-of-type-java-lang-string-cannot-be-converted-t*/
package com.example.json_01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.json_01.util.Auxiliar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private static final String URL = "https://api.androidhive.info/contacts/";
    private List<HashMap<String, String>> listacontatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listacontatos = new ArrayList();
        lv = findViewById(R.id.listView);


        ObtencaoDeContatos odc = new ObtencaoDeContatos();
        odc.execute();
    }//onCreate

    private class ObtencaoDeContatos extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Download JSON", Toast.LENGTH_SHORT).show();

        }//method

        @Override
        protected Void doInBackground(Void... voids) {
            Auxiliar aux = new Auxiliar();
            String jsonString = aux.conectar(URL);

            if (jsonString != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray jsonArray = jsonObject.getJSONArray("contacts");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        String cod = c.getString("id");
                        String nome = c.getString("name");
                        String email = c.getString("email");
                        String endereco = c.getString("address");
                        String genero = c.getString("gender");

                        JSONObject fone = c.getJSONObject("phone");
                        String movel = fone.getString("mobile");
                        String casa = fone.getString("home");
                        String trabalho = fone.getString("office");

                        HashMap<String, String> contatos = new HashMap<>();
                        contatos.put("nome", nome);
                        contatos.put("email", email);
                        contatos.put("endereco", endereco);
                        contatos.put("movel", movel);

                        listacontatos.add(contatos);

                    }//for
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }//if

            return null;

        }//method

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this,
                    listacontatos,
                    R.layout.item_list,
                    new String[]{"nome", "email", "endereco", "movel"},
                    new int[]{R.id.textViewNome, R.id.textViewEmail, R.id.textViewEndereco, R.id.textViewMovel});
            lv.setAdapter(adapter);
        }
    }//innerClass

}//class
