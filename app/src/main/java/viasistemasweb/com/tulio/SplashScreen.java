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
import android.widget.Toast;

import com.pushbots.push.Pushbots;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

        @Override
        protected Void doInBackground(Void... params) {

            //instancia o banco de dados
            DataBaseHandler db = new DataBaseHandler(getApplicationContext());

            SharedPreferences shared = getSharedPreferences("DadosDoUsuario", 0);
            String cpfSharedPreferences = shared.getString("cpf", "00000000000");
            String tipoDeUsuario = (shared.getString("tipo",""));
            Map<String, ?> tipo = shared.getAll();
            String t = String.valueOf(tipo.get("tipo"));
            int testes=0;
            /** verifico se o usuário está logado */
            UserFunctions u = new UserFunctions();
            boolean tet = u.isUserLoggedIn(SplashScreen.this);
            int teste =1;
            if(u.isUserLoggedIn(SplashScreen.this)){
                //ta logado

                switch(tipoDeUsuario){
                    case "p":
                        Intent l = new Intent(SplashScreen.this, MainActivity.class);
                        Bundle b = new Bundle();
                        b.putBoolean("login", true);
                        l.putExtras(b);
                        startActivity(l);
                        finish();
                        break;
                    case "t":
                        Intent l2 = new Intent(SplashScreen.this, PainelProfessor.class);
                        Bundle b2 = new Bundle();
                        b2.putBoolean("login", true);
                        l2.putExtras(b2);
                        startActivity(l2);
                        finish();
                        break;
                    default:
                        Toast.makeText(SplashScreen.this, "Não foi possível identificar \n " +
                                "o tipo de usuário Logado no app. Houve um erro." +
                                "", Toast.LENGTH_LONG).show();
                        finish();
                        break;
                }
            }else{
            /* -- Usuário não está logado --*/
                /** PushBots */
                //Pushbots.sharedInstance().init(this);
                //exibe mensagem e envia o usuário para a tela de login
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
