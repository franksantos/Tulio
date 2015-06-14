package viasistemasweb.com.tulio;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class AtividadeBD extends ActionBarActivity {

    ListView listaAtividades;
    String[] tituloAtividade = {
    "Português",
    "Matemática",
    "Ciências",
    "Geografia",
    "História",
    "Artes",
    "Religião"
} ;
    String[] descricaoAtividade = {
"Folha de leitura página 7",
"Exercício no caderno de atividades, memorização de soma, subtração e multiplicação",
"Caderno de matemática, caderno de regligião, caderno de ciências",
"Estudar a revião de Português para a prova",
"Atividade do livro página 308",
"Tarefa na folha de leitura",
"Pintar os desenhos e colar o que se pede no caderno",
};
    String[] dataAtividade = {
"22/04/2015",
"23/04/2015",
"24/04/2015",
"25/04/2015",
"26/04/2015",
"28/04/2015",
"29/04/2015",
} ;
    Integer[] imagemAtividade = {
            R.drawable.icone_de_portugues
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atividades);
        /** *
         * Dados vindos da notificação do pushbots
         * */
        Bundle extras = getIntent().getExtras();
        if(null !=extras && getIntent().getExtras().containsKey("txtDescAtiv") && getIntent().getExtras().containsKey("DisciplinaSelecionada") && getIntent().getExtras().containsKey("txtDataEntrega")){
            tituloAtividade = new String[] {extras.getString("DisciplinaSelecionada")};
            descricaoAtividade = new String[]{extras.getString("txtDescAtiv")};
            dataAtividade = new String[]{extras.getString("txtDataEntrega")};
        }else{
            tituloAtividade = new String[]{"SEM ATIVIDADE"};
            descricaoAtividade = new String[]{"Nenhuma atividade cadastrada"};
            //pegando a data atual

            dataAtividade = new String[]{"27-04-2015"};
            Toast.makeText(this, "ERRO. \nNão foi possível receber a notificação", Toast.LENGTH_SHORT).show();
        }
        /**
         * Populando o ListView com imagens
         */
        ListaAtividades adapter = new
                ListaAtividades(AtividadeBD.this, tituloAtividade, descricaoAtividade, dataAtividade, imagemAtividade);
        listaAtividades=(ListView)findViewById(R.id.listaAtividades);
        listaAtividades.setAdapter(adapter);
        listaAtividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(AtividadeBD.this, "Você clicou em " + tituloAtividade[+position] + "posição do meu" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_atividades, menu);
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

    public class ListaAtividades extends ArrayAdapter<String> {
        private final Activity contexto;
        private final String[] tituloAtividade;
        private final String[] descricaoAtividade;
        private final String[] dataAtividade;
        private final Integer[] imagemAtividade;
        //construtor
        public ListaAtividades(Activity contexto, String[] tituloAtividade, String[] descricaoAtividade, String[] dataAtividade, Integer[] imagemAtividade ){
            super(contexto, R.layout.item_atividade, tituloAtividade);
            this.contexto = contexto;
            this.tituloAtividade = tituloAtividade;
            this.descricaoAtividade = descricaoAtividade;
            this.dataAtividade = dataAtividade;
            this.imagemAtividade = imagemAtividade;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent){
            LayoutInflater inflater = contexto.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.item_atividade, null, true);
            TextView txtTituloAtiv = (TextView) rowView.findViewById(R.id.txtAtivTitulo);
            TextView txtDescAtiv = (TextView) rowView.findViewById(R.id.txtAtivDesc);
            TextView txtDataAtiv = (TextView) rowView.findViewById(R.id.txtAtivData);
            ImageView imagemProduto = (ImageView) rowView.findViewById(R.id.imageAtiv);
            txtTituloAtiv.setText(tituloAtividade[position]);
            txtDescAtiv.setText(descricaoAtividade[position]);
            txtDataAtiv.setText(dataAtividade[position]);
            imagemProduto.setImageResource(this.imagemAtividade[position]);
            return rowView;
        }
    }

    /**
     * Created by Frank on 19/05/2015.
     */
    public static class AndroidUtils {
        protected static final String TAG = "livroandroid";
        public static boolean isNetworkAvailable(Context context) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
            if (connectivity == null) {
                return false;
            } else {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        public static void alertDialog(final Context context, final int mensagem) {
            try {
                AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
                        context.getString(R.string.app_name)).setMessage(mensagem).create();
                dialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dialog.show();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        public static void alertDialog(final Context context, final String mensagem) {
            try {
                AlertDialog dialog = new AlertDialog.Builder(context).setTitle(
                        context.getString(R.string.app_name)).setMessage(mensagem)
                        .create();
                dialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dialog.show();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        // Retorna se e Android 3.x "honeycomb" ou superior (API Level 11)
        public static boolean isAndroid_3() {
            int apiLevel = Build.VERSION.SDK_INT;
            if (apiLevel >= 11) {
                return true;
            }
            return false;
        }
        // Retorna se a tela  large ou xlarge
        public static boolean isTablet(Context context) {
            return (context.getResources().getConfiguration().screenLayout
                    & Configuration.SCREENLAYOUT_SIZE_MASK)
                    >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        }
        // Retona se � um tablet com Android 3.x
        public static boolean isAndroid_3_Tablet(Context context) {
            return isAndroid_3() && isTablet(context);
        }
        // Fecha o teclado virtual se aberto (view com foque)
        public static boolean closeVirtualKeyboard(Context context, View view) {
            // Fecha o teclado virtual
            InputMethodManager imm = (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
            if(imm != null) {
                boolean b = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                return b;
            }
            return false;
        }
    }
}
