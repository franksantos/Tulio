package viasistemasweb.com.tulio.library;

import android.content.Context;

/**
 * Created by frank on 26/06/2016.
 */
public interface AutenticavelContract {
    boolean isLogged (Context context);
    boolean logar(String cpf);
    boolean logout(String cpf);
}
