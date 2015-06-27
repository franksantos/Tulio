package viasistemasweb.com.tulio;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class AtividadeBD extends ActionBarActivity {

    JSONParser jsonParser = new JSONParser();
    // JSON Node names
    private static final String TAG_SUCCESS     = "success";//se 1 = OK, se 0 = Erro
    private static final String TAG_RETORNO     = "retorno";//mensagem string retornada
    private static final String TAG_ATIV_DESC   = "ativ_desc";//descrição da atividade
    private static final String TAG_DT_ENTREGA  = "ativ_dt_entrega";
    private static final String TAG_DIS_NOME    = "dis_nome";

    private static final int[] refLayoutId = {R.id.txtAtivTituloBD, R.id.txtAtivDescBD, R.id.imageAtivBD};


    // url to enviar os dados
    private static String url_pegar_json = "http://www.viasistemasweb.com.br/tulio/resposta_atividades_json.php";

    ProgressDialog barraDeProgresso;

    ListView listaAtividades;
    String[] disciplina ;
    String[] atividade;
    String[] dataEntrega;
    Integer[] imagemAtividade = {
            R.drawable.icone_de_portugues,
            R.drawable.icone_de_matematica,
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
        mostraProgresso();

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

    public ArrayList<HashMap<String, String>> mostraProgresso(){
        /** ----------- buscando a lista de atividades no banco de dados --------- */
        new Thread(){
            public void run(){
                String idDaTurma = "2";
                //faz o processamento em background
                List<NameValuePair> parametros = new ArrayList<NameValuePair>();
                parametros.add(new BasicNameValuePair("idDaTurma",idDaTurma));
                final JSONObject json = jsonParser.makeHttpRequest(url_pegar_json, "GET", parametros);
                Log.d("Resposta do JSON", json.toString());

                //atualiza a interface gráfica
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            AlertDialog.Builder alerta = new AlertDialog.Builder(AtividadeBD.this);
                            alerta.setTitle("Carregando Atividades");
                            alerta.setMessage("As atividades estão sendo preparadas para serem exibidas");
                            int success = json.getInt(TAG_SUCCESS);
                            if(success==1){
                                // Obtem o retorno com a listagem das atividades
                                JSONArray retornoListaAtiv = json.getJSONArray(TAG_RETORNO);
                                /* Fa?o um loop para retornar um Array com todas as atividades encontrados*/
                                for(int i=0; i<retornoListaAtiv.length(); i++){
                                    JSONObject listaRetornada = retornoListaAtiv.getJSONObject(i);
                                    //agora armazeno cada item da lista de atividades em uma vari?vel
                                    String ativDesc      = listaRetornada.getString(TAG_ATIV_DESC);
                                    String ativDtEntrega = listaRetornada.getString(TAG_DT_ENTREGA);
                                    String nomeDisciplina= listaRetornada.getString(TAG_DIS_NOME);
                                    //Crio um HashMap para mapear os dados vindos da internet
                                    HashMap<String, String> mapeamentoDeAtivid = new HashMap<String, String>();
                                    //Adiciono os n?s do HashMap no tipo Chave => valor
                                    mapeamentoDeAtivid.put("desc", ativDesc);
                                    mapeamentoDeAtivid.put("dtEntrega", ativDtEntrega);
                                    mapeamentoDeAtivid.put("disciplina", nomeDisciplina);
                                    //Adiciono o HashMap ao ArrayList
                                    atividadesList.add(mapeamentoDeAtivid);

                                }
                                barraDeProgresso.dismiss();
                            }
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        }

                    }
                });
            }
        }.start();

        return atividadesList;
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
