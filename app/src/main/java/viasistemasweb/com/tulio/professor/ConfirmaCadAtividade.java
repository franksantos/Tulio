package viasistemasweb.com.tulio.professor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;

import viasistemasweb.com.tulio.R;

import viasistemasweb.com.tulio.JSONParser;


public class ConfirmaCadAtividade extends ActionBarActivity {
    //Parametro para o Progress Dialog
    private ProgressDialog pDialog;
    private Context context;
    private String txtDescAtiv;
    private String txtDataEntrega;
    private String disciplina, turma;
    private Button btnConfirmaRecebimento;
    private String msgRetorno;

    //constantes
    private static final String PAI      = "nome_pai";
    private static final String ALUNO    = "nome_aluno";
    private static final String DATAHORA = "not_data_hora";

    private static final int[] refLayoutId = {R.id.txtNomeAluno, R.id.txtNomePaiAluno, R.id.TracoConfirmacao};

    JSONObject listaDeRetorno;
    ArrayList<ItemConfirmacaoAtividade> myList = new ArrayList<ItemConfirmacaoAtividade>();

    //listview
    ListView lv;
    /*
    teste
     */
    // Hashmap for ListView
    ArrayList<HashMap<String, String>> confirmaAtividadesList = new ArrayList<HashMap<String, String>>();

    private String DisciplinaSelecionada, TurmaSelecionada;

    //instancia da JSONParser
    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // products JSONArray
    JSONArray smsEnviado = null;

    // JSON Node names
    private static final String TAG_SUCCESS     = "success";//se 1 = OK, se 0 = Erro
    //private static final String TAG_ERROR     = "Falhou";
    private static final String TAG_MESSAGE     = "message";//mensagem string retornada
    private static final String TAG_STATUS        = "status";
    private static final String TAG_RETORNO     = "retorno";//mensagem string retornada



    // url to enviar os dados
    private static String url_pegar_json = "http://www.fegv.com.br/tulio_api/resposta_json.php";
    // url lista usuários que leram as notificações JSON
    private static String url_lista_lidos_json = "http://www.fegv.com.br/tulio_api/lista_usuarios_leram_notificacao_json.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirma_cad_atividade);

        //Carrega a classe que tem a Thread que processa os dados
        new GerarStatusDaAtividade().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirma_cad_atividade, menu);
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

    /**
     * CLASSE ASYNC TASK PROCESSA EM BACKGROUND A INSERÇÃO DA ATIVIDADE
     *
     * Backgrounda async taks
     */
    class GerarStatusDaAtividade extends AsyncTask<String, Void, String[]> {

        //propriedades do textview
        private TextView status;
        private ProgressDialog pDialog;
        Context context;


        /**
         * Antes de iniciar backgroun crio um thread
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pDialog = ProgressDialog.show(context, context.getString(R.string.app_name),context.getString(R.string.aguarde));
            pDialog = new ProgressDialog(ConfirmaCadAtividade.this);//ConfirmaCadAtividade.this
            pDialog.setTitle("Por favor Aguarde...");
            pDialog.setMessage("Carregando Dados...");
            //pDialog.setMax(5000);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         */
        //protected String doInBackground(String... args) {
        @Override
        protected String[] doInBackground(String... args) {

            Intent i = getIntent();
            if (i != null) {
                Bundle b = i.getExtras();
                if (b != null) {
                    txtDescAtiv = i.getStringExtra("descricao");
                    txtDataEntrega = i.getStringExtra("data");
                    DisciplinaSelecionada = i.getStringExtra("disciplina");
                    TurmaSelecionada = i.getStringExtra("turma");

                }
            }else{
                Toast.makeText(ConfirmaCadAtividade.this, "Dados não passados", Toast.LENGTH_SHORT).show();
            }

            // Building Parameters
            List<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("txtDescAtiv", txtDescAtiv));
            parametros.add(new BasicNameValuePair("txtDataEntrega", txtDataEntrega));
            parametros.add(new BasicNameValuePair("DisciplinaSelecionada", DisciplinaSelecionada));
            parametros.add(new BasicNameValuePair("TurmaSelecionada", TurmaSelecionada));
            // getting JSON Object
            /** envia ao servidor que vai processar o envio por CURL para todos os dispositivos
             * usando a API do Pushbots
             */
            JSONObject json = jsonParser.makeHttpRequest(url_pegar_json,
                    "GET", parametros);
            // check log cat fro response
            Log.d("Resposta do JSON", json.toString());
            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // agendamento foi realizado com sucesso
                    // Obtem a mensagem de agendamento realizado com sucesso (Getting Array of Products)
                    JSONArray mensagemRetornada = json.getJSONArray(TAG_MESSAGE);
                    JSONObject mensagem = mensagemRetornada.getJSONObject(0);
                    //String mensagemRetornada = json.toString();
                    String[] msgRetorno = { mensagem.getString(TAG_STATUS) };
                    return msgRetorno;
                }else if(success == 0) {
                    // failed to create product
                    String[] msgRetorno = {"0"};
                    return msgRetorno;
                    //return carro;
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
        protected void onPostExecute(String[] param) {
            //se tiver alteração
            String resposta = param[0];
            Log.d("resposta", resposta.toString());

            if (resposta == "0") {
                //Falhou

                status = (TextView) findViewById(R.id.status);
                status.setText("Atenção\n. Houve um erro e a atividade não pôde ser cadastrada. Procure o administrador do sistema e informe este erro a ele.");
            } else {
                //deu certo
                //coloca o resultado  numa variável
                status = (TextView) findViewById(R.id.status);
                //exibindo o resultado na tela
                status.setText("A Atividade foi cadastrada com sucesso. \nOs pais foram avisados da atividade, em breve recebera a confirmacao de recebimento dos pais");

                /*
                Acessa o servidor e verifica quais os pais que leram a notificação e quais os pais que não leram a notificação
                Atualiza o ListView com verde para LEU ou cinza para NÃO LEU
                 */
                btnConfirmaRecebimento = (Button) findViewById(R.id.btnConfirmaRecebimento);
                btnConfirmaRecebimento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*
                        Cria um AssyncTask que vai no servidor e consome um json com a confirmação de quem leu e quem não leu a mensagem enviada
                        pelo túlio professor aos pais contendo a atividade

                        Ai preenche a lista de pessoas que leram a notificação e mostra no listview
                         */
                        new ChecaSeLeu().execute();
                    }
                });

            }
            // dismiss the dialog once done
            pDialog.dismiss();

        }


    }

    class ChecaSeLeu extends AsyncTask<String, String, ArrayList<HashMap<String, String>>>{

        // Hashmap for ListView
        ArrayList<HashMap<String, String>> confirmaPaisList = new ArrayList<HashMap<String, String>>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ConfirmaCadAtividade.this, "Aguarde. Trabalhando para mostrar a listagem", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected ArrayList<HashMap<String, String>> doInBackground(String... params) {
            // Building Parameters
            List<NameValuePair> parametros = new ArrayList<NameValuePair>();
            parametros.add(new BasicNameValuePair("cpf", "24453398348"));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json_listagem = jsonParser.makeHttpRequest(url_lista_lidos_json,
                    "GET", parametros);
            // check log cat fro response
            Log.d("Resposta do JSON", json_listagem.toString());
            // check for success tag
            try {
                int success = json_listagem.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // agendamento foi realizado com sucesso
                    // Obtem a mensagem de agendamento realizado com sucesso (Getting Array of Products)
                    JSONArray mensagemRetornada = json_listagem.getJSONArray(TAG_RETORNO);
                    int l = mensagemRetornada.length();
                    Log.d("itemRetornados", String.valueOf(l));
                    JSONObject listaRetornada1 = mensagemRetornada.getJSONObject(0);
                    String k = String.valueOf(listaRetornada1);
                    Log.d("ret1", k);

                    for(int i=0; i<mensagemRetornada.length(); i++){
                        //listaDeRetorno  = mensagemRetornada.getJSONObject(i);
                        JSONObject listaRetornada = mensagemRetornada.getJSONObject(i);


                        //agora armazeno cada item da lista de atividade em uma vari?vel
                        String nome_pai         = listaRetornada.getString(PAI);
                        String nome_aluno       = listaRetornada.getString(ALUNO);
                        String not_data_hora    = listaRetornada.getString(DATAHORA);

                        //Crio um HashMap para mapear os dados vindos da internet
                        HashMap<String, String> mapDeAtividades = new HashMap<String, String>();
                        //Adiciono os n?s do HashMap no tipo Chave => valor
                        mapDeAtividades.put(PAI, nome_pai);
                        mapDeAtividades.put(ALUNO, nome_aluno);
                        mapDeAtividades.put(DATAHORA, not_data_hora);

                        //Adiciono o HashMap ao ArrayList
                        confirmaPaisList.add(mapDeAtividades);
                        Log.d("ListaConfi", String.valueOf(confirmaPaisList.get(i)));
                    }

                    //String mensagemRetornada = json.toString();
                    //String[] msgRetorno = new String[]{listaDeRetorno.getString("nome_pai"), listaDeRetorno.getString("nome_aluno"), listaDeRetorno.getString("not_data_hora")};

                    return confirmaPaisList;
                }else if(success == 0) {
                    // failed to create product
                    // falha ao retornar a lisatem de avisos
                    HashMap<String,String> erroMapeamento = new HashMap<String, String>();
                    erroMapeamento.put(PAI, "ERRO");
                    erroMapeamento.put(ALUNO, "Nenhuma atividade econtrada");
                    erroMapeamento.put(DATAHORA, "00/00/0000");
                    confirmaPaisList.add(erroMapeamento);
                    return confirmaPaisList;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<HashMap<String, String>> params) {
            //lista as informações no ListView e fecha o Dialog
            //fazendo o resultado acontecer
            final ArrayList<HashMap<String, String>> obj = params;

            obj.get(0);
            HashMap<String, String> listaHash = new HashMap<String, String>();


            /*for(int i=0; i<obj.size(); i++){
                ItemConfirmacaoAtividade item = new ItemConfirmacaoAtividade();
                item.setNomePai(obj.get(i).get(i));
            }*/
            // dismiss the dialog once done
            //teste
            //chaves usadas no hashmap
            final String[] from = new String[]{PAI, ALUNO, DATAHORA};
            String[] f = from;
            //String[]teste = f;
            //atualiza a UI em uma Thread separada
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //ListAdapter adapter = new SimpleAdapter(ConfirmaCadAtividade.this, confirmaPaisList, R.layout.item_lista_pais_confirma, from , refLayoutId);
                    //ListView listviewDeAtividades = (ListView) findViewById(R.id.listaPaisConf);
                    lv = (ListView) findViewById(R.id.listaPaisConf);
                    lv.setAdapter(new AdapterListaConfirmaRecebido(ConfirmaCadAtividade.this, myList));
                    //lecjoolistviewDeAtividades.setAdapter(adapter);
                    lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //String codigoDisciplina = (String) obj.get(0).get("dis_cod");
                            //Log.i("codigo", codigoDisciplina);
                            //Toast.makeText(getApplicationContext(), codigoDisciplina, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    //listviewDeAtividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(getBaseContext(),"Clicado"+position,Toast.LENGTH_SHORT ).show();
                        }
                    });


                }
            });

        }
    }

}
