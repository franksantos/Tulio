package viasistemasweb.com.tulio.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frank on 25/05/2015.
 */
public class UserFunctions {
    //Parametro para o Progress Dialog
    private ProgressDialog pDialog;
    private static String cpf;

    private JSONParserLogin jsonParser;

    private static String loginURL = "http://www.fegv.com.br/tulio_api/resposta_login_json.php";
    private static String registerURL = "http://10.0.2.2/ah_login_api/";

    private static String login_tag = "login";
    private static String register_tag = "register";

    // constructor
    public UserFunctions() {
        jsonParser = new JSONParserLogin();
    }

    /**
     * function make Login Request
     *
     * @param cpf
     *
     */
    //public JSONObject loginUser(String email, String password){
    public JSONObject loginUser(String cpf) {
        //buildando parametros
        List params = new ArrayList();
        //params.add(new BasicNameValuePair("tag", login_tag));
        params.add(new BasicNameValuePair("cpf", cpf));
        JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
        try {
            String teste = json.get("cpf").toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String teste2 = "ver se o Json está passando o cpf";
        Log.d("json", json.toString());
        int teste=1;
        return json;
    }

    /**
     * function make Login Request
     *
     * @param name
     * @param email
     * @param password
     */
    public JSONObject registerUser(String name, String email, String password) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("name", name));
        params.add(new BasicNameValuePair("email", email));
        params.add(new BasicNameValuePair("password", password));

        // getting JSON Object
        JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
        // return json
        return json;
    }

    /**
     * Function get Login status
     */
    public boolean isUserLoggedIn(Context context) {
        DataBaseHandler db = new DataBaseHandler(context);
        int count = db.getRowCount();
        if (count > 0) {
            // user logged in
            return true;
        }
        return false;
    }

    /**
     * Function to logout user
     * Reset Database
     */
    public boolean logoutUser(Context context) {
        DataBaseHandler db = new DataBaseHandler(context);
        db.resetTables();
        return true;
    }
}
