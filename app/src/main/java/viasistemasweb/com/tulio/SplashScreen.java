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
import viasistemasweb.com.tulio.library.SessionManager;
import viasistemasweb.com.tulio.library.UserFunctions;
import viasistemasweb.com.tulio.professor.PainelProfessor;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends ActionBarActivity {
    ImageView imageView, preloadImage;
    SessionManager session;
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

    class ChecaUsuarioLogado extends AsyncTask<Void, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(Void... params) {

            /** verifico se o usuário está logado */
            session = new SessionManager(SplashScreen.this);
            return session.isLoggedIn();

//            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean){
                //tá logado
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
            }else{
                //não tá logado
                Intent j = new Intent(SplashScreen.this, Login.class);
                // Closing all the Activities
                j.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                j.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(j);
            }
        }
    }

}
