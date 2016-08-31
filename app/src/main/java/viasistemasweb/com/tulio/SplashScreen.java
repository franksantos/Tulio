package viasistemasweb.com.tulio;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.pushbots.push.Pushbots;

import java.io.IOException;
import java.util.HashMap;

import viasistemasweb.com.tulio.library.DataBaseHandler;
import viasistemasweb.com.tulio.library.HttpHelper;
import viasistemasweb.com.tulio.library.UserFunctions;
import viasistemasweb.com.tulio.professor.PainelProfessor;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends ActionBarActivity {
    ImageView imageView, preloadImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);
        /** PushBots */
        Pushbots.sharedInstance().init(this);
        Log.d("abriu?", "abriu o splashscreen");

        preloadImage = (ImageView)findViewById(R.id.preloader_image);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splashscreeen_animation);
        preloadImage.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                /**
                 * Verificar se o cara tá logado
                 * SIM => verificar qual o tipo de usuário
                 * NÃO => redireciona para o Login
                 */
                new ChecaUsuarioLogado().execute();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    class ChecaUsuarioLogado extends AsyncTask<Void, Void, Void>
    {
        private ProgressDialog pDialog;
        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SplashScreen.this);
            pDialog.setTitle("Conectando ao Servidor");
            pDialog.setMessage("Processando os dados ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMax(9000);
            pDialog.show();
        }*/


        @Override
        protected Void doInBackground(Void... params) {

            //instancia o banco de dados
            DataBaseHandler db = new DataBaseHandler(getApplicationContext());
            /**
             * Hashmap to load data from the Sqlite database
             **/
            /*HashMap userHash = new HashMap();
            userHash = db.getUserDetails();
            int iteste = 1;
            //String id = userHash.get()*/

            SharedPreferences shared = getSharedPreferences("DadosDoUsuario", MODE_PRIVATE);
            String cpfSharedPreferences = (shared.getString("cpf", ""));
            String tipoDeUsuario = "p";


            /*String cpf = userHash.get("cpf").toString();
            String tipoDeUsuario = userHash.get("tipo_usuario").toString();
            Log.i("tipoDeUsuarioSC", tipoDeUsuario);
            String turmaId = userHash.get("turma").toString();
            Log.e("turmaId:", turmaId);*/

            try {
                /** acessa a internet e verifica o tipo de usuário */
                HttpHelper objHelper = new HttpHelper();
                String URL = "http://www.fegv.com.br/tulio_api/resposta_login_json.php?cpf=92700934334";
                String retorno = objHelper.doGet(URL,"UTF-8");
                db.getUserFromCpf(retorno);
                int maisumteste =0;

            } catch (IOException e) {
                e.printStackTrace();
            }
            /** verifico se o usuário está logado */
            UserFunctions u = new UserFunctions();
            boolean tet = u.isUserLoggedIn(SplashScreen.this);
            int teste =1;
            if(u.isUserLoggedIn(SplashScreen.this)){
                //ta logado


                if(tipoDeUsuario=="p"){
                    Intent l = new Intent(SplashScreen.this, MainActivity.class);
                    Bundle b = new Bundle();
                    b.putBoolean("login", true);
                    l.putExtras(b);
                    startActivity(l);
                    finish();
                }
                if(tipoDeUsuario=="t"){
                    Intent l = new Intent(SplashScreen.this, PainelProfessor.class);
                    Bundle b = new Bundle();
                    b.putBoolean("login", true);
                    l.putExtras(b);
                    startActivity(l);
                }
                finish();

            }else{
            /* -- Usuário não está logado --*/
                /** PushBots */
                //Pushbots.sharedInstance().init(this);
                //exibe mensagem e envia o usuário para a tela de login
                //Toast.makeText(MainActivity.this, "Usuario NÃO ESTÁ logado", Toast.LENGTH_SHORT).show();
                Intent l = new Intent(SplashScreen.this, Login.class);
                Bundle b = new Bundle();
                b.putBoolean("login", true);
                l.putExtras(b);
                startActivity(l);
            }
            return null;
        }
    }

}
