package viasistemasweb.com.tulio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class AtividadeBD extends ActionBarActivity {

    JSONParser jsonParser = new JSONParser();
    private JSONParserFromUrl parserFromUrl = new JSONParserFromUrl();
    // JSON Node names
    private static final String TAG_SUCCESS     = "success";//se 1 = OK, se 0 = Erro
    private static final String TAG_RETORNO     = "retorno";//mensagem string retornada
    private static final String TAG_NOME_TURMA  = "nome_tur";
    private static final String TAG_ATIV_DESC   = "desc";//descrição da atividade
    private static final String TAG_DIS_COD     = "dis_cod";
    private static final String TAG_DT_ENTREGA  = "dt_entrega";
    private static final String TAG_DIS_NOME    = "dis_nome";
    private static final String TAG_COD_TURMA   = "cod_tur";

    //private static final int[] refLayoutId = {R.id.txtAtivTituloBD, R.id.txtAtivDescBD, R.id.txtAtivDataBD, R.id.imageAtivBD};
    private static final int[] refLayoutId = {R.id.txtAtivTituloBD, R.id.txtAtivDescBD, R.id.txtAtivDataBD, R.id.imageAtivBD};

    ProgressDialog pDialog;


    // url to enviar os dados
    private static String url_pegar_json = "http://www.viasistemasweb.com.br/tulio/resposta_atividades_json.php";

    ProgressDialog barraDeProgresso;

    ListView listaAtividades;
    String[] disciplina ;
    String[] atividade;
    String[] dataEntrega;
    int[] imagemAtividade = new int[]{
            R.drawable.icone_de_matematica,
            R.drawable.icone_de_portugues,
            R.drawable.icone_de_artes,
            R.drawable.icone_de_ciencias,
            R.drawable.icone_de_geografia,
            R.drawable.icone_de_historia,
            R.drawable.icone_de_religiao,
    };

    // Hashmap for ListView
    ArrayList<HashMap<String, String>> atividadesList = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividade_bd);
        /** *
         * Exibe a Atividade Vinda do Banco de Dados
         * */

        /**
         * Crio uma Thread para fazer o processamento em background e mostra um Dialog ao usuário
         * 1 - vai na internet, pega as últimas atividades cadastradas no banco de dados
         * 2 - exibe em um listview
         */
        /** -- Criando a Thread --- */
        new GerarListaDeAtividades().execute();

        /**
         * Populando o ListView com imagens
         */
        /*ListaAtividades adapter = new ListaAtividades(AtividadeBD.this, disciplina, atividade, dataEntrega, imagemAtividade);
        listaAtividades=(ListView)findViewById(R.id.listaAtividades);
        listaAtividades.setAdapter(adapter);
        listaAtividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

            }
        });*/
    }

    @Override public void onPause(){
        super.onPause();
        if(pDialog != null) {
            pDialog.dismiss();
        }
    }

    /**
     * CLASSE ASYNC TASK PROCESSA EM BACKGROUND A INSER??O DA ATIVIDADE
     *
     * Backgrounda async taks
     */
    class GerarListaDeAtividades extends AsyncTask<String, String, ArrayList<HashMap<String, String>>> {

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> atividadesList = new ArrayList<HashMap<String, String>>();


        /**
         * Antes de iniciar backgroun crio um thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pDialog = ProgressDialog.show(context, context.getString(R.string.app_name),context.getString(R.string.aguarde));
            pDialog = new ProgressDialog(AtividadeBD.this);//ConfirmaCadAtividade.this
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
            //convertendo int para String
            String id  = "2";

            List<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("idDaTurma", id ));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_pegar_json, "GET", parametros);
            //JSONObject json = parserFromUrl.getJSONFromUrl(url_pegar_json,parametros);
            // check log cat fro response
//            Log.i("Resposta do JSON", json.toString());

            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // fez a busca no banco de dados e retornou a lista de avisos com sucesso
                    // Obtem o retorno com a listagem de avisos
                    JSONArray listagemDeAtividades = json.getJSONArray(TAG_RETORNO);
                    /* Fa?o um loop para retornar um Array com todos os avisos encontrados*/

                    for(int i=0; i<listagemDeAtividades.length(); i++){
                        JSONObject listaRetornada = listagemDeAtividades.getJSONObject(i);
                        //agora armazeno cada item da lista de atividade em uma vari?vel
                        String turma     = listaRetornada.getString(TAG_NOME_TURMA);
                        String desc_ativ = listaRetornada.getString(TAG_ATIV_DESC);
                        String disCod    = listaRetornada.getString(TAG_DIS_COD);
                        String dataEntrega = listaRetornada.getString(TAG_DT_ENTREGA);
                        String disNome   = listaRetornada.getString(TAG_DIS_NOME);

                        //Crio um HashMap para mapear os dados vindos da internet
                        HashMap<String, String> mapDeAtividades = new HashMap<String, String>();
                        //Adiciono os n?s do HashMap no tipo Chave => valor
                        mapDeAtividades.put("dis_nome", disNome);
                        mapDeAtividades.put("desc", desc_ativ);
                        mapDeAtividades.put("dt_entrega", dataEntrega);
                        mapDeAtividades.put("dis_cod", Integer.toString(imagemAtividade[i]));
                        //Adiciono o HashMap ao ArrayList
                        atividadesList.add(mapDeAtividades);
                    }
                    //retorno o arraylist de avisos
                    return atividadesList;
                }else if(success == 0) {
                    // falha ao retornar a lisatem de avisos
                    HashMap<String,String> erroMapeamento = new HashMap<String, String>();
                    erroMapeamento.put("nomeDisciplina", "ERRO");
                    erroMapeamento.put("descAtividade", "Nenhuma atividade econtrada");
                    erroMapeamento.put("dataEntrega", "00/00/0000");
                    atividadesList.add(erroMapeamento);
                    return atividadesList;
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
            //String codigoDisciplina = (String) obj.get(0).get("dis_cod");
            String codigoDisciplina = (String) obj.get(0).get("dis_cod");
            Log.i("codigo", codigoDisciplina);
            //new SimpleAdapter(contexto, lista, layout, arrayChavesHashMap, arrayReferenciaLayout)
            //chaves usadas no hashmap
            final String[] from = new String[]{TAG_DIS_NOME, TAG_ATIV_DESC, TAG_DT_ENTREGA, TAG_DIS_COD};
            //ID´s das views do item_atividade_bd layout do item


            //atualiza a UI em uma Thread separada
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new SimpleAdapter(AtividadeBD.this, atividadesList, R.layout.item_atividade_bd, from , refLayoutId);
                    ListView listviewDeAtividades = (ListView) findViewById(R.id.listaAtividadesBD);
                    listviewDeAtividades.setAdapter(adapter);
                    listviewDeAtividades.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String codigoDisciplina = (String) obj.get(0).get("dis_cod");
                            Log.i("codigo", codigoDisciplina);
                            Toast.makeText(getApplicationContext(), codigoDisciplina, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                }
            });

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_atividades, menu);
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

    /*public class ListaAtividades extends ArrayAdapter<String> {
        private final Activity contexto;
        private final String[] tituloAtividade;
        private final String[] descricaoAtividade;
        private final String[] dataAtividade;
        private final Integer[] imagemAtividade;
        //construtor
        public ListaAtividades(Activity contexto, String[] tituloAtividade, String[] descricaoAtividade, String[] dataAtividade, Integer[] imagemAtividade ){
            super(contexto, R.layout.item_atividade, tituloAtividade);
            this.contexto = contexto;
            this.tituloAtividade = tituloAtividade;
            this.descricaoAtividade = descricaoAtividade;
            this.dataAtividade = dataAtividade;
            this.imagemAtividade = imagemAtividade;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent){
            LayoutInflater inflater = contexto.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.item_atividade, null, true);
            TextView txtTituloAtiv = (TextView) rowView.findViewById(R.id.txtAtivTitulo);
            TextView txtDescAtiv = (TextView) rowView.findViewById(R.id.txtAtivDesc);
            TextView txtDataAtiv = (TextView) rowView.findViewById(R.id.txtAtivData);
            ImageView imagemProduto = (ImageView) rowView.findViewById(R.id.imageAtiv);
            txtTituloAtiv.setText(tituloAtividade[position]);
            txtDescAtiv.setText(descricaoAtividade[position]);
            txtDataAtiv.setText(dataAtividade[position]);
            imagemProduto.setImageResource(this.imagemAtividade[position]);
            return rowView;
        }
    }*/

    /**
     * Created by Frank on 19/05/2015.
     */
    public static class AndroidUtils {
        protected static final String TAG = "livroandroid";
        public static boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        public static void alertDialog(final Context context, final String mensagem) {
            try {
                AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
                        context.getString(R.string.app_name)).setMessage(mensagem)
                        .create();
                dialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dialog.show();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        // Retorna se e Android 3.x "honeycomb" ou superior (API Level 11)
        public static boolean isAndroid_3() {
            int apiLevel = Build.VERSION.SDK_INT;
            if (apiLevel >= 11) {
                return true;
            }
            return false;
        }
        // Retorna se a tela  large ou xlarge
        public static boolean isTablet(Context context) {
            return (context.getResources().getConfiguration().screenLayout
                    & Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        }
        // Retona se � um tablet com Android 3.x
        public static boolean isAndroid_3_Tablet(Context context) {
            return isAndroid_3() && isTablet(context);
        }
        // Fecha o teclado virtual se aberto (view com foque)
        public static boolean closeVirtualKeyboard(Context context, View view) {
            // Fecha o teclado virtual
            InputMethodManager imm = (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
            if(imm != null) {
                boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return b;
            }
            return false;
        }
    }
}
