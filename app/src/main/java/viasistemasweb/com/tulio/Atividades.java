package viasistemasweb.com.tulio;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import viasistemasweb.com.tulio.library.DataBaseHandler;
import viasistemasweb.com.tulio.library.HttpHelper;
import viasistemasweb.com.tulio.library.SessionManager;


public class Atividades extends ActionBarActivity {
    private ProgressDialog pDialog;
    private Calendar c;
    private ListView listaAtividades;
    private static final String URLGET = "http://www.fegv.com.br/tulio_api/salva_cpf_dthora_recebeu_notificacao.php";
    private String envia;
    URL u;
    private String hora;


    String[] tituloAtividade;
            /*= {
            "Português",
            "Matemática",
            "Ciências",
            "Geografia",
            "História",
            "Artes",
            "Religião"
    } ;*/
    String[] descricaoAtividade;
                    /*= {
            "Folha de leitura página 7",
            "Exercício no caderno de atividades, memorização de soma, subtração e multiplicação",
            "Caderno de matemática, caderno de regligião, caderno de ciências",
            "Estudar a revião de Português para a prova",
            "Atividade do livro página 308",
            "Tarefa na folha de leitura",
            "Pintar os desenhos e colar o que se pede no caderno",
    } ;*/
    String[] dataAtividade;
                            /*= {
            "22/04/2015",
            "23/04/2015",
            "24/04/2015",
            "25/04/2015",
            "26/04/2015",
            "28/04/2015",
            "29/04/2015",
    } ;*/
    Integer[] imagemAtividade = {
            R.drawable.icone_de_portugues
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades);

        /** *
         * Dados vindos da notificação do pushbots
         * */
        Bundle extras = getIntent().getExtras();
        if(null !=extras && getIntent().getExtras().containsKey("txtDescAtiv") && getIntent().getExtras().containsKey("DisciplinaSelecionada") && getIntent().getExtras().containsKey("txtDataEntrega")){
            //código executado caso receba a notificação do PushBots com sucesso
            //DAdos vindos do Pushbots
            tituloAtividade = new String[] {extras.getString("DisciplinaSelecionada")};
            descricaoAtividade = new String[]{extras.getString("txtDescAtiv")};
            dataAtividade = new String[]{extras.getString("txtDataEntrega")};
            /*Passo ao servidor os dados do telefone e do usuário que chegou a esta tela, ou seja, que leu
                a notificação enviada pelo professor com as atividade
             */
            new ConfirmaRecebimento().execute();

        }else{
            //Toast.makeText(this, "ERRO. \nNão foi possível receber a notificação", Toast.LENGTH_SHORT).show();
            Intent prox = new Intent(Atividades.this, AtividadeBD.class);
            startActivity(prox);
            /*ArrayList<ItemAtividade> listaDeAtividades = new ArrayList<ItemAtividade>();
            ItemAtividade obj = new ItemAtividade(R.drawable.icone_de_portugues, "Sem Atividade", "Nenhuma Atividade para este aluno", "28-04-2015");
            listaDeAtividades.add(obj);
            ListaAtividadesAdapter adapter = new ListaAtividadesAdapter(this, listaDeAtividades);
            ListView listView = (ListView)findViewById(R.id.listaAtividades);
            listView.setAdapter(adapter);*/

        }

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Atividades.this, MainActivity.class);
        super.onBackPressed();
        startActivity(intent);
        finish();
    }

    /**
     * CLASSE ASYNC TASK SALVA NO BANCO DE DADOS A CONFIRMAÇÃO DE LEITURA DA NOTIFICAÇÃO DE ATIVIDADE
     *
     * Backgrounda async taks
     */
    class ConfirmaRecebimento extends AsyncTask<Void, Void, Void> {
        /**
         * Antes de iniciar backgroun crio um thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pDialog = ProgressDialog.show(context, context.getString(R.string.app_name),context.getString(R.string.aguarde));
            pDialog = new ProgressDialog(Atividades.this);//ConfirmaCadAtividade.this
            pDialog.setTitle("Por favor Aguarde...");
            pDialog.setMessage("Carregando Dados...");
            pDialog.setMax(5000);
            pDialog.setIcon(R.drawable.icone_avisos);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            /*
                Vai pegar o CPF e o horário e vai salvar no banco de dados
            * */
            //pego a hora exata
            c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            hora = sdf.format(c.getTime());
            Log.d("hora", hora);//é para exibir: 2015-09-09 04:30:09

            /**°//
             * TESTE
             */
            DataBaseHandler db = new DataBaseHandler(getApplicationContext());
            //pega o cpf salvo no dispostivo no DB SQLite
            HashMap<String, String> teste = new HashMap<String, String>();
            teste = db.getUserDetails();
            String cc = teste.get("cpf");
            String cpfDoUsuario = cc;
            Log.d("cpfTESTE", cpfDoUsuario);
            /**
             * FIM DO TESTE
             */


            /*
            Salvando no banco de dados remoto
             */
            //codificando a URL
            try {
                hora = URLEncoder.encode(hora,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            connect(URLGET+"?cpf="+cpfDoUsuario+"&hora="+hora);

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * *
         */
        protected void onPostExecute(Void param) {
            // dismiss the dialog once done
            pDialog.setTitle("Informando o recebimento...");
            pDialog.setMessage("Aguarde estamos contactando o professor...");
            pDialog.setMax(5000);
            pDialog.setIcon(R.drawable.icone_avisos);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
            pDialog.dismiss();
            //fazendo o resultado acontecer na tela
            ArrayList<ItemAtividade> listaDeAtividades = new ArrayList<ItemAtividade>();
            ItemAtividade obj = new ItemAtividade();
            obj.setImagem(R.drawable.icone_de_portugues);
            obj.setTitulo(tituloAtividade[0]);
            obj.setDescricao(descricaoAtividade[0]);
            obj.setData(dataAtividade[0]);

            listaDeAtividades.add(obj);
            ListaAtividadesAdapter adapter = new ListaAtividadesAdapter(Atividades.this, listaDeAtividades);
            ListView listView = (ListView) findViewById(R.id.listaAtividades);
            listView.setAdapter(adapter);
            listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(Atividades.this, position, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ///Toast.makeText(Atividades.this, position, Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    public static void connect(String url)
    {

        HttpClient httpclient = new DefaultHttpClient();

        // Prepare a request object
        HttpGet httpget = new HttpGet(url);

        // Execute the request
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            // Examine the response status
            Log.i("Praeda",response.getStatusLine().toString());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release

            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();
            }


        } catch (Exception e) {}
    }

    private static String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
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

   /* public class ListaAtividades extends ArrayAdapter<String> {
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
}
