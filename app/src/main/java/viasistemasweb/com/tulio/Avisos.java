package viasistemasweb.com.tulio;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import viasistemasweb.com.tulio.library.ChecaInternet;


public class Avisos extends ActionBarActivity {
    //propriedades
    Calendar c;
    //Parametro para o Progress Dialog
    private ProgressDialog pDialog;
    private Context context;
    //instancia da JSONParser
    // JSON parser class
    JSONParser jsonParser = new JSONParser();


    // JSON Node names
    private static final String TAG_SUCCESS     = "success";//se 1 = OK, se 0 = Erro
    private static final String TAG_RETORNO     = "listaDeAvisos";//mensagem string retornada
    private static final String TAG_MESSAGE     = "message";//mensagem string retornada
    private static final String TAG_DATA        = "data";
    private static final String TAG_NOME        = "nome";
    private static final String TAG_DESC        = "desc";
    private static final int[] arrayReferenciaLayout =  {R.id.tvDataAviso, R.id.tvNomeAviso};

    // url to enviar os dados
    private static String url_pegar_json = "http://www.viasistemasweb.com.br/tulio/resposta_avisos_json.php";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avisos);

            new GerarListaDeAvisos().execute();
    }

    /**
     * CLASSE ASYNC TASK PROCESSA EM BACKGROUND A INSER??O DA ATIVIDADE
     *
     * Backgrounda async taks
     */
    class GerarListaDeAvisos extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> avisosList = new ArrayList<HashMap<String, String>>();


        /**
         * Antes de iniciar backgroun crio um thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pDialog = ProgressDialog.show(context, context.getString(R.string.app_name),context.getString(R.string.aguarde));
            pDialog = new ProgressDialog(Avisos.this);//ConfirmaCadAtividade.this
            pDialog.setTitle("Por favor Aguarde...");
            pDialog.setMessage("Carregando Dados...");
            pDialog.setMax(5000);
            pDialog.setIcon(R.drawable.icone_avisos);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        //protected String doInBackground(String... args) {
        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... args) {


                // Building Parameters
                c = Calendar.getInstance();
                int Cano = c.get(Calendar.YEAR);//ano
                int Cmes = c.get(Calendar.MONTH)+1;//mes
                int Cdia = c.get(Calendar.DAY_OF_MONTH);
                //convertendo int para String
                String Sano  = String.valueOf(Cano);
                String Smes  = "0"+String.valueOf(Cmes);
                String Sdia  = String.valueOf(Cdia);

                List<NameValuePair> parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("ano", Sano ));
                parametros.add(new BasicNameValuePair("mes", Smes));
                parametros.add(new BasicNameValuePair("dia", Sdia));
                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_pegar_json, "GET", parametros);
                //JSONObject json = jsonParserFromUrl.getJSONFromUrl(url_pegar_json, parametros);

                // check log cat fro response
                Log.d("Resposta do JSON", json.toString());

                try {
                    int success = json.getInt(TAG_SUCCESS);
                    if (success == 1) {
                        // fez a busca no banco de dados e retornou a lista de avisos com sucesso
                        // Obtem o retorno com a listagem de avisos
                        JSONArray listagemDeAvisos = json.getJSONArray(TAG_RETORNO);
                    /* Fa?o um loop para retornar um Array com todos os avisos encontrados*/
                        for(int i=0; i<listagemDeAvisos.length(); i++){
                            JSONObject listaRetornada = listagemDeAvisos.getJSONObject(i);
                            //agora armazeno cada item da lista de avisos em uma vari?vel
                            String dataAviso = listaRetornada.getString(TAG_DATA);
                            String nomeAviso = listaRetornada.getString(TAG_NOME);
                            String descAviso = listaRetornada.getString(TAG_DESC);
                            //Crio um HashMap para mapear os dados vindos da internet
                            HashMap<String, String> mapeamentoDeavisos = new HashMap<String, String>();
                            //Adiciono os n?s do HashMap no tipo Chave => valor
                            mapeamentoDeavisos.put("data", dataAviso);
                            mapeamentoDeavisos.put("nome", nomeAviso);
                            mapeamentoDeavisos.put("desc", descAviso);
                            //Adiciono o HashMap ao ArrayList
                            avisosList.add(mapeamentoDeavisos);
                        }
                        //retorno o arraylist de avisos
                        return avisosList;
                    }else if(success == 0) {
                        // falha ao retornar a lisatem de avisos
                        HashMap<String,String> erroMapeamento = new HashMap<String, String>();
                        erroMapeamento.put("data", Sdia+"/"+Smes+"/"+Sano);
                        erroMapeamento.put("nome", "Nenhum Evento econtrado");
                        erroMapeamento.put("desc", "Erro ao procurar aviso");
                        avisosList.add(erroMapeamento);
                        return avisosList;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;


        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(ArrayList<HashMap<String, String>> param) {
            //fazendo o resultado acontecer
            final ArrayList<HashMap<String, String>> obj = param;
            // dismiss the dialog once done
            pDialog.dismiss();
            //teste

            //String nome = (String) obj.get(0).get("nome");
            //new SimpleAdapter(contexto, lista, layout, arrayChavesHashMap, arrayReferenciaLayout)
            //atualiza a UI em uma Thread separada
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(Avisos.this, avisosList, R.layout.item_avisos, new String[]{TAG_DATA, TAG_NOME}, arrayReferenciaLayout);
                    ListView listviewDeavisos = (ListView)findViewById(R.id.listviewDeAvisos);
                    listviewDeavisos.setAdapter(adapter);
                    listviewDeavisos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String desc = (String) obj.get(position).get("desc");
                            Intent i = new Intent(Avisos.this, AvisosDescricao.class );
                            Bundle b = new Bundle();
                            b.putString("desc", desc);
                            i.putExtras(b);
                            startActivity(i);
                        }
                    });

                }
            });

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_avisos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
