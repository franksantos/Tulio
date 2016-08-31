package viasistemasweb.com.tulio.professor;

import android.content.Context;

import viasistemasweb.com.tulio.library.AutenticavelContract;
import viasistemasweb.com.tulio.library.UserFunctions;

/**
 * Created by frank on 26/06/2016.
 */
public class Professor extends UserFunctions implements AutenticavelContract {

    public boolean isLogged(Context context) {
        return false;
    }

    public boolean logar(String cpf){
        return false;
    }
    public boolean logout(String cpf){
        return false;
    }
}
