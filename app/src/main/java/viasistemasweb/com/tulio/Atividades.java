package viasistemasweb.com.tulio;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Calendar;


public class Atividades extends ActionBarActivity {
    ListView listaAtividades;
    String[] tituloAtividade;
            /*= {
            "Português",
            "Matemática",
            "Ciências",
            "Geografia",
            "História",
            "Artes",
            "Religião"
    } ;*/
    String[] descricaoAtividade;
                    /*= {
            "Folha de leitura página 7",
            "Exercício no caderno de atividades, memorização de soma, subtração e multiplicação",
            "Caderno de matemática, caderno de regligião, caderno de ciências",
            "Estudar a revião de Português para a prova",
            "Atividade do livro página 308",
            "Tarefa na folha de leitura",
            "Pintar os desenhos e colar o que se pede no caderno",
    } ;*/
    String[] dataAtividade;
                            /*= {
            "22/04/2015",
            "23/04/2015",
            "24/04/2015",
            "25/04/2015",
            "26/04/2015",
            "28/04/2015",
            "29/04/2015",
    } ;*/
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

            ArrayList<ItemAtividade> listaDeAtividades = new ArrayList<ItemAtividade>();
            ItemAtividade obj = new ItemAtividade();
            obj.setImagem(R.drawable.icone_de_portugues);
            obj.setTitulo(tituloAtividade[0]);
            obj.setDescricao(descricaoAtividade[0]);
            obj.setData(dataAtividade[0]);

            listaDeAtividades.add(obj);
            ListaAtividadesAdapter adapter = new ListaAtividadesAdapter(this, listaDeAtividades);
            ListView listView = (ListView)findViewById(R.id.listaAtividades);
            listView.setAdapter(adapter);
            //ItemAtividade obj1 = new ItemAtividade(R.drawable.icone_de_geografia, "");

        }else{
            Toast.makeText(this, "ERRO. \nNão foi possível receber a notificação", Toast.LENGTH_SHORT).show();
            /*Intent prox = new Intent(getApplicationContext(), AtividadeBD.class);
            startActivity(prox);*/
            ArrayList<ItemAtividade> listaDeAtividades = new ArrayList<ItemAtividade>();
            ItemAtividade obj = new ItemAtividade(R.drawable.icone_de_portugues, "Sem Atividade", "Nenhuma Atividade para este aluno", "28-04-2015");
            listaDeAtividades.add(obj);
            ListaAtividadesAdapter adapter = new ListaAtividadesAdapter(this, listaDeAtividades);
            ListView listView = (ListView)findViewById(R.id.listaAtividades);
            listView.setAdapter(adapter);

        }

        /**
         * Populando o ListView com imagens
         *
        ListaAtividades adapter = new
                ListaAtividades(Atividades.this, tituloAtividade, descricaoAtividade, dataAtividade, imagemAtividade);
        listaAtividades=(ListView)findViewById(R.id.listaAtividades);
        listaAtividades.setAdapter(adapter);
        listaAtividades.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(Atividades.this, "Você clicou em " + tituloAtividade[+position] + "posição do meu" + position, Toast.LENGTH_SHORT).show();
            }
        });*/
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

   /* public class ListaAtividades extends ArrayAdapter<String> {
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
    }*/
}
