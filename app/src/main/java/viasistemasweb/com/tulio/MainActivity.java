package viasistemasweb.com.tulio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pushbots.push.Pushbots;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import viasistemasweb.com.tulio.library.ChecaInternet;
import viasistemasweb.com.tulio.library.DataBaseHandler;
import viasistemasweb.com.tulio.library.HttpHelper;
import viasistemasweb.com.tulio.library.SessionManager;
import viasistemasweb.com.tulio.library.UserFunctions;
import viasistemasweb.com.tulio.professor.PainelProfessor;


public class    MainActivity extends ActionBarActivity {
    // Session Manager Class
    String cpf, tipoDeUsuario, turmaId;
    HashMap<String, String> dadosUsuario;

    boolean login;
    ListView list;
    String[] nomeMenu = {
            "Atividades",
            "Avisos",
            "Eventos",
            "Pendências",
            "Sair"
    } ;
    Integer[] imagemMenu = {
            R.drawable.icone_atividade,
            R.drawable.icone_avisos,
            R.drawable.icone_eventos,
            R.drawable.icone_ocorrencias,
            R.drawable.icone_sair
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //instancia o banco de dados
        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
        /** verifico se o usuário está logado */
        UserFunctions u = new UserFunctions();
        if(u.isUserLoggedIn(MainActivity.this)){
            //ta logado
            /**
             * Hashmap to load data from the Sqlite database
             **/
            HashMap userHash = new HashMap();
            userHash = db.getUserDetails();
            //String id = userHash.get()
            cpf = userHash.get("cpf").toString();
            tipoDeUsuario = userHash.get("tipo_usuario").toString();
            turmaId = userHash.get("turma").toString();
            Log.e("turmaId:", turmaId);
        }else{
            /* -- Usuário não está logado --*/
            /** PushBots */
            //Pushbots.sharedInstance().init(this);
            //exibe mensagem e envia o usuário para a tela de login
            //Toast.makeText(MainActivity.this, "Usuario NÃO ESTÁ logado", Toast.LENGTH_SHORT).show();
            Intent l = new Intent(MainActivity.this, Login.class);
            Bundle b = new Bundle();
            b.putBoolean("login", true);
            l.putExtras(b);
            startActivity(l);
        }

        /**
         * Verifico o tipo de usuário pelo SharedPreferences
         * Após verificar o tipo de usuário redireciono para a tela apropriada
         * tipo usuário = t, manda para a tela de professor
         * tipo usuário = p, manda para a tela de pai
         */
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String turma = preferences.getString("turma", "");
        //String turmaId = teste.getString("cpf", "");
        String tipoUsuario = preferences.getString("tipo", "");
        Log.e("turmaId:", turma);
        Log.e("TIPO_USUARIO:", tipoUsuario);




        switch (tipoUsuario){
            case "t":
                //startActivity(upanel);
                Intent t1 = new Intent(MainActivity.this, PainelProfessor.class);
                t1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(t1);
                finish();
                break;
        }
        /**
         * Populando o ListView com imagens
         */
        ListaMenus adapter = new
                ListaMenus(MainActivity.this, nomeMenu, imagemMenu);
        list=(ListView)findViewById(R.id.menu);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "Você clicou em " + nomeMenu[+position]+"posição do meu"+position, Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        Intent ativ = new Intent(getApplicationContext(), Atividades.class);
                        startActivity(ativ);
                        break;
                    case 1:
                        /**
                         * Check se tem internet
                         */
                        ChecaInternet on = new ChecaInternet();
                        Boolean online = on.isInternetAvailable(MainActivity.this);
                        if(online){
                            Intent aviso = new Intent(getApplicationContext(), Avisos.class);
                            startActivity(aviso);
                        }else{
                            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                            alerta.setTitle("Sem Conexão com a internet");
                            alerta.setMessage("Erro\n Você não tem conexão à internet.\n Verifique sua configuração de rede e tente novamente. ");
                            //Método executado se escolher ok
                            alerta.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int whichButton){

                                }
                            });
                            alerta.show();
                        }
                        break;
//                    case 2:
//                        Intent bol = new Intent(getApplicationContext(), Boletim.class);
//                        startActivity(bol);
//                        break;
                    case 2:
                        /**
                         * Check se tem internet
                         */
                        ChecaInternet on2 = new ChecaInternet();
                        Boolean online2 = on2.isInternetAvailable(MainActivity.this);
                        if(online2){
                            Intent even = new Intent(getApplicationContext(), Eventos.class);
                            startActivity(even);
                        }else{
                            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                            alerta.setTitle("Sem Conexão com a internet");
                            alerta.setMessage("Erro\n Você não tem conexão à internet.\n Verifique sua configuração de rede e tente novamente. ");
                            //Método executado se escolher ok
                            alerta.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int whichButton){

                                }
                            });
                            alerta.show();
                        }
                        break;
                    case 3:
                        /**
                         * Check se tem internet
                         */
                        ChecaInternet on3 = new ChecaInternet();
                        Boolean online3 = on3.isInternetAvailable(MainActivity.this);
                        if(online3){
                            Intent even = new Intent(getApplicationContext(), Pendencias.class);
                            startActivity(even);
                        }else{
                            AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                            alerta.setTitle("Sem Conexão com a internet");
                            alerta.setMessage("Erro\n Você não tem conexão à internet.\n Verifique sua configuração de rede e tente novamente. ");
                            //Método executado se escolher ok
                            alerta.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int whichButton){

                                }
                            });
                            alerta.show();
                        }
                        break;
                    case 4:
                        finish();

                        break;

                }
            }
        });
    }

    /**
     * método de teste para ler o JSON passado da URL
     *
     */
    public Message readMessage(JsonReader reader) throws IOException {
        long success = -1;
        String cpf = null;
        String fic_id = null;
        String tipo_usuario = null;



        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("success")) {
                success = reader.nextLong();
            } else if (name.equals("cpf")) {
                cpf = reader.nextString();
            } else if (name.equals("fic_id")) {
                fic_id = reader.nextString();
            } else if (name.equals("user")) {
                tipo_usuario = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Message();
    }

    //Método ao clicar no botão Back, voltar do android
    @Override public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseja Sair?");
        builder.setMessage("Você quer realmente Fechar o Aplicativo?").setCancelable(false).setPositiveButton("SIM", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { MainActivity.this.finish(); } }).setNegativeButton("Não", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { dialog.cancel(); } }); AlertDialog alert = builder.create(); alert.show();
    }

    public void sair(){
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public class ListaMenus extends ArrayAdapter<String>{
        private final Activity contexto;
        private final String[] nomeMenu;
        private final Integer[] imagemMenu;
        //construtor
        public ListaMenus(Activity contexto, String[] nomeMenu, Integer[] imagemMenu ){
            super(contexto, R.layout.item_menu, nomeMenu);
            this.contexto = contexto;
            this.nomeMenu = nomeMenu;
            this.imagemMenu = imagemMenu;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent){
            LayoutInflater inflater = contexto.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.item_menu, null, true);
            TextView txtNomeMenu = (TextView) rowView.findViewById(R.id.txtNomeMenu);
            ImageView imagemProduto = (ImageView) rowView.findViewById(R.id.imgFiguraMenu);
            txtNomeMenu.setText(nomeMenu[position]);
            imagemProduto.setImageResource(this.imagemMenu[position]);
            return rowView;
        }
    }
}
