package viasistemasweb.com.tulio;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pushbots.push.Pushbots;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import viasistemasweb.com.tulio.library.ChecaInternet;
import viasistemasweb.com.tulio.library.DataBaseHandler;
import viasistemasweb.com.tulio.library.SessionManager;
import viasistemasweb.com.tulio.library.UserFunctions;
import viasistemasweb.com.tulio.professor.PainelProfessor;


public class Login extends ActionBarActivity {
    EditText edCpf1,edCpf2,edCpf3,edCpf4,edCpf5,edCpf6,edCpf7,edCpf8,edCpf9,edCpf10,edCpf11;
    Button btnEntar;
    TextView loginErrorMsg;

    boolean login;
    String cpfDoUsuario;
    private Dialog mDialog;
    /**
     * Called when the activity is first created.
     */
    private static String KEY_SUCCESS = "success";
    private static String CPF_NODE = "cpf";

    DataBaseHandler db = new DataBaseHandler(Login.this);

    // Session Manager Class
    SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Session Manager
        //session = new SessionManager(getApplicationContext());
        //instancia a mensagem de erro de login
        loginErrorMsg = (TextView)findViewById(R.id.loginErrorMsg);
        //recebo o parametro da p�gina principal
        Intent i = getIntent();
        login = i.getBooleanExtra("login", false);
        Log.d("booleano login", String.valueOf(login));

        // passar o foca para o pr�ximo campo ao digitar 3 letras
        edCpf1 = (EditText)findViewById(R.id.edCpf1);
        edCpf2 = (EditText)findViewById(R.id.edCpf2);
        edCpf3 = (EditText)findViewById(R.id.edCpf3);
        edCpf4 = (EditText)findViewById(R.id.edCpf4);
        edCpf5 = (EditText)findViewById(R.id.edCpf5);
        edCpf6 = (EditText)findViewById(R.id.edCpf6);
        edCpf7 = (EditText)findViewById(R.id.edCpf7);
        edCpf8 = (EditText)findViewById(R.id.edCpf8);
        edCpf9 = (EditText)findViewById(R.id.edCpf9);
        edCpf10 = (EditText)findViewById(R.id.edCpf10);
        edCpf11 = (EditText)findViewById(R.id.edCpf11);



        /**
         * ####################################
         * Pular o foco para o campo seguinte
         * ####################################
         */
        
        //cpf1 para o cpf2
        edCpf1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edCpf1.getText().toString().length() == 1) {
                    edCpf1.clearFocus();
                    edCpf2.requestFocus();
                    edCpf2.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //cpf2 para o cpf3
        edCpf2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edCpf2.getText().toString().length() == 1) {
                    edCpf2.clearFocus();
                    edCpf3.requestFocus();
                    edCpf3.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //cpf3 para o cpf4
        edCpf3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edCpf3.getText().toString().length() == 1) {
                    edCpf3.clearFocus();
                    edCpf4.requestFocus();
                    edCpf4.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //cpf4 para o cpf5
        edCpf4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edCpf4.getText().toString().length() == 1) {
                    edCpf4.clearFocus();
                    edCpf5.requestFocus();
                    edCpf5.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //cpf5 para o cpf6
        edCpf5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edCpf5.getText().toString().length() == 1) {
                    edCpf5.clearFocus();
                    edCpf6.requestFocus();
                    edCpf6.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //cpf6 para o cpf7
        edCpf6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edCpf6.getText().toString().length() == 1) {
                    edCpf6.clearFocus();
                    edCpf7.requestFocus();
                    edCpf7.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //cpf7 para o cpf8
        edCpf7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edCpf7.getText().toString().length() == 1) {
                    edCpf7.clearFocus();
                    edCpf8.requestFocus();
                    edCpf8.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //cpf8 para o cpf9
        edCpf8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edCpf8.getText().toString().length() == 1) {
                    edCpf8.clearFocus();
                    edCpf9.requestFocus();
                    edCpf9.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //cpf9 para o cpf10
        edCpf9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edCpf9.getText().toString().length() == 1) {
                    edCpf9.clearFocus();
                    edCpf10.requestFocus();
                    edCpf10.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //cpf10 para o cpf11
        edCpf10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edCpf10.getText().toString().length() == 1) {
                    edCpf10.clearFocus();
                    edCpf11.requestFocus();
                    edCpf11.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //cpf11 para o botão Entrar
        edCpf10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edCpf11.getText().toString().length() == 1) {
                    edCpf11.clearFocus();
                    btnEntar.requestFocus();
                    btnEntar.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // fim do pular o foco para o campo seguinte

        Button btnEntrar = (Button)findViewById(R.id.btnEntrarLogin);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        /* verificando se tem internet */
                if(ChecaInternet.isInternetAvailable(Login.this)){
                    //Toast.makeText(Login.this, "Conectado", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder alerta = new AlertDialog.Builder(Login.this);
                    alerta.setTitle("ATENÇÃO");
                    alerta.setMessage("\nSeu dispositivo está Sem conexão à internet.\n O aplicativo só funciona conectado à internet. Verifique sua configuração de rede e tente novamente. ");
                    //M�todo executado se escolher ok
                    alerta.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton){
                            Toast.makeText(Login.this, "Ok", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alerta.setNegativeButton("SAIR", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.finishAffinity(Login.this);
                        }
                    });
                    alerta.show();
                }/* fim da verificação de internet */

                //valida se os campos do login (cpf) estão vazios
                if (  ( !edCpf1.getText().toString().equals("")) && ( !edCpf2.getText().toString().equals("")) && ( !edCpf3.getText().toString().equals("")) && ( !edCpf4.getText().toString().equals("")) && ( !edCpf5.getText().toString().equals("")) && ( !edCpf6.getText().toString().equals("")) && ( !edCpf7.getText().toString().equals("")) && ( !edCpf8.getText().toString().equals("")) && ( !edCpf9.getText().toString().equals("")) && ( !edCpf10.getText().toString().equals("")) && ( !edCpf1.getText().toString().equals("")) )
                {
                    NetAsync(v);
                }
                else if ( ( !edCpf1.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "O primeiro número do cpf é obrigatório. Verifique e tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if ( ( !edCpf2.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "O segundo número do cpf é obrigatório. Verifique e tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if ( ( !edCpf3.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "O terceiro número do cpf é obrigatório. Verifique e tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if ( ( !edCpf4.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "O quarto número do cpf é obrigatório. Verifique e tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if ( ( !edCpf5.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "O quinto número do cpf é obrigatório. Verifique e tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if ( ( !edCpf6.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "O sexto número do cpf é obrigatório. Verifique e tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if ( ( !edCpf7.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "O sétimo número do cpf é obrigatório. Verifique e tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if ( ( !edCpf8.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "O oitavo número do cpf é obrigatório. Verifique e tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if ( ( !edCpf9.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "O nono número do cpf é obrigatório. Verifique e tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if ( ( !edCpf10.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "O décimo número do cpf é obrigatório. Verifique e tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if ( ( !edCpf11.getText().toString().equals("")) )
                {
                    Toast.makeText(getApplicationContext(),
                            "O último número do cpf é obrigatório. Verifique se ele está vazio e tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }
                else
                {
                    Toast.makeText(getApplicationContext(),
                            "os números do cpf não podem ser vazios. Tente novamente", Toast.LENGTH_SHORT).show();
                    return;
                }



            }
        });



    }



    /**
     *
     * Classe interna AsyncTask
     * @return
     */

        /**
         * Async Task to check whether internet connection is working.
         **/

        private class NetCheck extends AsyncTask<String, Void, Boolean>
        {
            private ProgressDialog nDialog;



            @Override
            protected void onPreExecute(){
                super.onPreExecute();
                nDialog = new ProgressDialog(Login.this);
                nDialog.setTitle("Checando a sua conexão com a internet");
                nDialog.setMessage("Aguarde..");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(true);
                nDialog.show();
            }

            @Override
            protected Boolean doInBackground(String... args){

            /**
             * Gets current device state and checks for working internet connection by trying Google.
             **/
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
            try {
                URL url = new URL("http://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setConnectTimeout(3000);
                urlc.connect();
                if (urlc.getResponseCode() == 200) {
                    return true;
                }
            } catch (MalformedURLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
            return false;

        }
            @Override
            protected void onPostExecute(Boolean th){

                if(th){
                    nDialog.dismiss();
                }
                else{
                    nDialog.dismiss();
                    loginErrorMsg.setText("Erro Conexão de Internet");
                }
                EditText Cpf1 = (EditText) findViewById(R.id.edCpf1);
                EditText Cpf2 = (EditText) findViewById(R.id.edCpf2);
                EditText Cpf3 = (EditText) findViewById(R.id.edCpf3);
                EditText Cpf4 = (EditText) findViewById(R.id.edCpf4);
                EditText Cpf5 = (EditText) findViewById(R.id.edCpf5);
                EditText Cpf6 = (EditText) findViewById(R.id.edCpf6);
                EditText Cpf7 = (EditText) findViewById(R.id.edCpf7);
                EditText Cpf8 = (EditText) findViewById(R.id.edCpf8);
                EditText Cpf9 = (EditText) findViewById(R.id.edCpf9);
                EditText Cpf10 = (EditText) findViewById(R.id.edCpf10);
                EditText Cpf11 = (EditText) findViewById(R.id.edCpf11);
                String cpf;
                cpf = Cpf1.getText().toString()+Cpf2.getText().toString()+Cpf3.getText().toString()+Cpf4.getText().toString()+Cpf5.getText().toString()+Cpf6.getText().toString()+Cpf7.getText().toString()+Cpf8.getText().toString()+Cpf9.getText().toString()+Cpf10.getText().toString()+Cpf11.getText().toString();
                new ProcessLogin().execute(cpf);
            }
        }

        /**
         * Async Task to get and send data to My Sql database through JSON respone.
         **/
        private class ProcessLogin extends AsyncTask<String, Void, JSONObject> {
            private ProgressDialog pDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(Login.this);
                pDialog.setTitle("Conectando ao Servidor");
                pDialog.setMessage("Processando os dados ...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(true);
                pDialog.setMax(9000);
                pDialog.show();
            }

            @Override
            protected JSONObject doInBackground(String... args) {
                //Faz o login pegando os dados da internet
                UserFunctions userFunction = new UserFunctions();
                JSONObject json = userFunction.loginUser(args[0]);
                return json;
            }

            @Override
            protected void onPostExecute(JSONObject json) {
                try {
                    String ujson = json.toString();
                    if (json.getString(KEY_SUCCESS) != null) {
                        String res = json.getString(KEY_SUCCESS);
                        int resInt = Integer.parseInt(res);

                        if(resInt > 0){
                            /**
                             armazena os dados do JSON em variáveis conforme o tipo de usuário
                             **/
                            String cpfMySQL = json.getString("cpf");//pega o cpf retornado do json
                            Log.d("cpf do mysql", cpfMySQL);
                            String tipoUsuario = json.getString("tipo_usuario");
                            //valor padrão para a turma,
                            //para cada tipo de usuário a turma é pega no Json retornado
                            String turmaId = "0";
                            /**
                             * pega o tipo de usuário
                             * se p = parent (PAI) redireciona para o mainactivity
                             * se t = teacher (PROFESSOR) redireciona para o professor.paienlProfessor
                             * se s = student (ESTUDANTE) redireciona para o mainactivity
                             */
                            switch (tipoUsuario){
                                /** PAI */
                            /* se for Pai o JSON vem no formato abaixo */
                                /**{
                                 "success": "1",
                                 "cpf": "36985274108",
                                 "fic_id": 4,
                                 "tipo_usuario": "p",
                                 "pai_id": 3,
                                 "alunos": [
                                 {
                                 "id": 2,
                                 "alu_fic_id": 5,
                                 "alu_tur_id": 1,
                                 "created_at": "2016-09-20 23:43:37",
                                 "updated_at": "2016-09-20 23:43:37",
                                 "pivot": {
                                 "pai_id": 3,
                                 "alu_id": 2
                                 }
                                 }
                                 ]
                                 }*/
                                case "p":
                                    //O pai não tem ID da turma, então é zero, depois vai ser alterado

                                    /*  grava os dados no SharedPreferences
                                        chamanbdo o método que faz a gravação
                                     */
                                    String cpfUser = json.getString("cpf");
                                    Log.e("tipo_usuario", tipoUsuario);
                                    salvaSessaoUsuario(cpfUser, tipoUsuario, turmaId);
                                    //seta a TAG no Pushbots
                                    if (Pushbots.sharedInstance().isInitialized()) {
                                        //seta o PushBots
                                        setTagPushBots(cpfUser, turmaId);
                                    } else {
                                        pDialog.setMessage("O usuário não foi salvo. Houve um erro ao salvar o usuário");
                                        pDialog.setTitle("ERRO AO SALVAR USUÁRIO");
                                        break;
                                    }
                                    //passa os dados para a próxima Activity
                                    Intent t = new Intent(Login.this, MainActivity.class);
                                    t.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(t);
                                    break;
                                case "t":
                                    /* se for Professor o JSON vem no formato abaixo*/
                                    /**
                                     {
                                     "success":"1",
                                     "cpf":"12345678900",
                                     "fic_id":19,
                                     "tipo_usuario":"t",
                                     "turma_id":1
                                     }
                                     * */
                                    String cpfUserProfessor = json.getString("cpf");
                                    turmaId = json.getString("turma_id"); //pega a turma do professor
                                    if( salvaSessaoUsuario(cpfUserProfessor, tipoUsuario, turmaId) ) {
                                        //se tiver salvo os dados do usuário
                                        // na sessão com sharedpreferences
                                        //verifica se o pushbots foi iniciado
                                        //se sim seta a TAG turma no pushbots
                                        if (Pushbots.sharedInstance().isInitialized()) {
                                            //seta o PushBots
                                            setTagPushBots(cpfUserProfessor, turmaId);
                                        } else {
                                            pDialog.setMessage("O usuário não foi salvo. Houve um erro ao salvar o usuário");
                                            pDialog.setTitle("ERRO AO SALVAR USUÁRIO");
                                            break;
                                        }
                                    }else {
                                        pDialog.setMessage("O usuário não foi localizado. Houve um erro ao salvar o usuário");
                                        pDialog.setTitle("ERRO AO SALVAR USUÁRIO");
                                        break;
                                    }

                                    //passa os dados para a tela de professor
                                    Bundle b1 = new Bundle();
                                    b1.putBoolean("login", login);
                                    Intent t1 = new Intent(Login.this, PainelProfessor.class);
                                    t1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    t1.putExtras(b1);
                                    startActivity(t1);
                                    break;
                                case "s":
                                    /* se for Estudante o JSON vem no formato abaixo*/
                                    /**
                                     {
                                     "success":"1",
                                     "cpf":"12345678900",
                                     "fic_id":21,
                                     "tipo_usuario":"s",
                                     "turma_id":8
                                     }
                                     * */

                                    String cpfAluno = json.getString("cpf");
                                    String turmaAluno = json.getString("turma_id");//pega a turma do aluno
                                    salvaSessaoUsuario(cpfAluno, tipoUsuario, turmaAluno);
                                    //seta a TAG no Pushbots
                                    if (Pushbots.sharedInstance().isInitialized()) {
                                        //seta o PushBots
                                        setTagPushBots(cpfAluno, turmaAluno);
                                    } else {
                                        pDialog.setMessage("O usuário não foi salvo. Houve um erro ao salvar o usuário");
                                        pDialog.setTitle("ERRO AO SALVAR USUÁRIO");
                                        break;
                                    }
                                    //passa os dados para a próxima Activity

                                    Intent ta = new Intent(Login.this, MainActivity.class);
                                    ta.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(ta);
                                    break;
                            }
                            /* chama o método que salva o cpf e tipo de usuário no SharedPreferences
                            para depois pegar esses dados em outras Activitys
                             */
                            /**
                             * Close Login Screen
                             **/
                            finish();
                        }else{

                            pDialog.dismiss();
                            loginErrorMsg.setText("ERRO. CPF não cadastrado. Verifique se seu CPF foi cadastrado e tente novamente.");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
            public void NetAsync(View view){
                new NetCheck().execute();
            }
    /** FIM da classe internas */

    public boolean setTagPushBots(String cpf, String turmaId){
        if((cpf!=null && turmaId!=null) || (cpf!="") ) {
            //pushbots
            Pushbots.sharedInstance().regID();
            Pushbots.sharedInstance().setAlias(cpf);//setar o cpf do usuário no pushbots
            Pushbots.sharedInstance().tag(String.valueOf(turmaId));//setar uma tag com a turma do usuário no pushbots
            Pushbots.sharedInstance().getNotifyStatus();
            return true;
        }
        return false;
    }

    //Método ao clicar no botão Back, voltar do android
    @Override public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseja Sair?");
        builder.setMessage("Você quer realmente Fechar o Aplicativo?").setCancelable(false).setPositiveButton("SIM", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { Login.this.finish(); } }).setNegativeButton("Não", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { dialog.cancel(); } }); AlertDialog alert = builder.create(); alert.show();
    }



    /**
     * Método salvaSessaoUsuario
     * para salvar o CPF do usuário e persistir essa informação para as outras activity
     */
    public boolean salvaSessaoUsuario(String cpfDoUsuario, String tipoUsuario, String turmaId){

        session = new SessionManager(Login.this);
        session.createLoginSession(cpfDoUsuario,tipoUsuario,turmaId);
        return true;
        /*SharedPreferences sharedPreferences = getSharedPreferences("DadosDoUsuario", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cpf", cpfDoUsuario);
        editor.putString("tipo", tipoUsuario);
        editor.putInt("turma", turmaId);
        return editor.commit();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
