package viasistemasweb.com.tulio.professor;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import viasistemasweb.com.tulio.JSONParser;
import viasistemasweb.com.tulio.R;

public class ConfirmaCadAviso extends ActionBarActivity {
    private String avi_desc, avi_data_hora, avi_tur_id;
    private String desc, data_hora, tur_id;
    //instancia da JSONParser
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    public static String URL_JSON_AVISOS = "http://tulioappweb.herokuapp.com/api/v1/avisos/store";
    public static String TAG_SUCCESS = "success";
    public static String TAG_MESSAGE = "message";
    public static String TAG_STATUS = "status";

    private TextView labelSucessoAvisos;
    private ProgressBar progressbar;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //teste
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma_cad_aviso);

        labelSucessoAvisos = (TextView)findViewById(R.id.labelAvisoSucesso);
        progressbar = (ProgressBar)findViewById(R.id.progressBar);


        setSupportProgressBarIndeterminateVisibility(true);

        Intent i = getIntent();
        if (i != null) {
            Bundle b = i.getExtras();
            if (b != null) {
                avi_desc = removerAcentos(i.getStringExtra("avi_desc"));
                avi_data_hora = i.getStringExtra("avi_data_hora");
                avi_tur_id = i.getStringExtra("avi_tur_id");

                //chama o asynctask
                new CarregarStatusAviso().execute(avi_desc, avi_data_hora, avi_tur_id);
            }
        }else{
            Toast.makeText(ConfirmaCadAviso.this, "Dados não passados", Toast.LENGTH_SHORT).show();
        }
    }

    class CarregarStatusAviso extends AsyncTask<String, Void, JSONObject>{
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.headerProgress);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            linearLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            // Building Parameters
            List<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("avi_desc", params[0]));
            parametros.add(new BasicNameValuePair("avi_data_hora", params[1]));
            parametros.add(new BasicNameValuePair("avi_tur_id", params[2]));
            Log.d("avi_desc", avi_desc);
            Log.d("avi_data_hora", avi_data_hora);
            Log.d("avi_tur_id", avi_tur_id);
            /** envia ao servidor que vai processar o envio por CURL para todos os dispositivos
             * usando a API do Pushbots
             */
            JSONObject json = jsonParser.makeHttpRequest(
                    URL_JSON_AVISOS,
                    "POST",
                    parametros
            );
            String jsonRetornado = json.toString();
            Log.d("json", jsonRetornado);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            /** {"success":"1","message":[{"statu":"atividade cadastrada com sucesso"}]} --*/
            try {
                jsonObject.getString(TAG_SUCCESS);
                Integer retorno = Integer.parseInt(jsonObject.getString(TAG_SUCCESS));
                if(retorno > 0){
                    JSONObject mensagemRetornada = jsonObject.getJSONObject(TAG_MESSAGE);
                    Log.d("mensagem ret = ", mensagemRetornada.toString());
                    String status = mensagemRetornada.getString(TAG_STATUS);
                    /** escreve na tela a mensagem de sucesso*/
                    labelSucessoAvisos.setText(status);
                    linearLayout.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                Log.e("err json = ", e.getMessage());
                /** escreve na tela a mensagem de FALHA*/
                labelSucessoAvisos.setText(e.getMessage());
                linearLayout.setVisibility(View.GONE);
            }

        }


    }



    /**
     *
     * @param str
     * @return string without acentos
     */
    public static String removerAcentos(String str) {
        return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

}
