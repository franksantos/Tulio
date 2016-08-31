package viasistemasweb.com.tulio.professor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

import java.util.HashMap;

import viasistemasweb.com.tulio.Login;
import viasistemasweb.com.tulio.R;
import viasistemasweb.com.tulio.library.AutenticavelContract;
import viasistemasweb.com.tulio.library.DataBaseHandler;
import viasistemasweb.com.tulio.library.SessionManager;
import viasistemasweb.com.tulio.library.UserFunctions;

/**
 * Created by Frank on 09/12/2015.
 */
public class PainelProfessor extends ActionBarActivity{
    // Session Manager Class
    SessionManager session;

    boolean login;
    ListView list;
    String[] nomeMenu = {
            "Atividades",
            "Avisos",
            /*"Boletim",*/
            "Eventos",
            "Pendências",
            "Sair"
    } ;
    Integer[] imagemMenu = {
            R.drawable.icone_atividade,
            R.drawable.icone_avisos,
            /*R.drawable.icone_boletim,*/
            R.drawable.icone_eventos,
            R.drawable.icone_ocorrencias,
            R.drawable.icone_sair
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_painel_professor);

        DataBaseHandler db = new DataBaseHandler(getApplicationContext());

       Professor objProfessor = new Professor();
        objProfessor.isUserLoggedIn(PainelProfessor.this);

        /** verifico se o usuário está logado */
        UserFunctions u = new UserFunctions();
        if(u.isUserLoggedIn(PainelProfessor.this)){
            //ta logado
            //Toast.makeText(MainActivity.this, "Usuario logado", Toast.LENGTH_SHORT).show();
            /**
             * Hashmap to load data from the Sqlite database
             **/
            HashMap userHash = new HashMap();
            userHash = db.getUserDetails();
            //String id = userHash.get()
            String cpf = userHash.get("cpf").toString();
            /** PushBots */
            Pushbots.sharedInstance().init(this);

            if(Pushbots.sharedInstance().isInitialized()){
                Pushbots.sharedInstance().tag(cpf);
                Pushbots.sharedInstance().getNotifyStatus();
            }else{


            }

        }else{
            Toast.makeText(PainelProfessor.this, "Usuario NÃO ESTÁ logado", Toast.LENGTH_SHORT).show();
            Intent l = new Intent(PainelProfessor.this, Login.class);
            Bundle b = new Bundle();
            b.putBoolean("login", true);
            l.putExtras(b);
            startActivity(l);
        }
        /**
         * Populando o ListView com imagens
         */
        ListaMenus adapter = new
                ListaMenus(PainelProfessor.this, nomeMenu, imagemMenu);
        list=(ListView)findViewById(R.id.menu);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "Você clicou em " + nomeMenu[+position] + "posição do meu" + position, Toast.LENGTH_SHORT).show();
                switch (position){
                    case 0:
                        Intent ativ = new Intent(getApplicationContext(), CadAtividade.class);
                        startActivity(ativ);
                        break;
                    case 1:
                        Intent aviso = new Intent(getApplicationContext(), CadAviso.class);
                        startActivity(aviso);
                        break;
                    /*case 2:
                        Intent bol = new Intent(getApplicationContext(), CadBoletim.class);
                        startActivity(bol);
                        break;*/
                    case 2:
                        Intent even = new Intent(getApplicationContext(), CadEvento.class);
                        startActivity(even);
                        break;
                    case 3:
                        Intent i = new Intent(getApplicationContext(), CadPendencia.class);
                        startActivity(i);
                        break;
                    case 4:
                        finish();

                        break;

                }
            }
        });
    }

    //Método ao clicar no botão Back, voltar do android
    @Override public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseja Sair?");
        builder.setMessage("Você quer realmente Fechar o Aplicativo?").setCancelable(false).setPositiveButton("SIM", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { PainelProfessor.this.finish(); } }).setNegativeButton("Não", new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int id) { dialog.cancel(); } }); AlertDialog alert = builder.create(); alert.show();
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

    public class ListaMenus extends ArrayAdapter<String> {
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
