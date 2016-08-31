package viasistemasweb.com.tulio.professor;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import viasistemasweb.com.tulio.library.HttpHelper;

/**
 * Created by frank on 26/06/2016.
 */
public class TurmaService {
    private static final String URL = "";
    private JSONObject jsonTurmas;


    /**
     * Método para pegar a lista de turmas as quais o professor ministra aulas
     * @param String fic_id (id da ficha do professor)
     * @return
     */
    public static List<Turma> getTurmasDoProfessor(String ficId){
        //vai no servidor e pega o JSON contendo a lista de turmas do determinado professor
        List<Turma> turmas = new ArrayList<Turma>();
        HttpHelper objHttpHelper = new HttpHelper();
        try {
            String listaDeTurmas = objHttpHelper.doGet(URL, "UTF-8");
            Log.i("listaDeTurmas", listaDeTurmas);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*JSONObject json = new JSONObject();
        json.getJSONObject()
        turmas.add();*/
        return turmas;
    }
}
