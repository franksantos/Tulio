package viasistemasweb.com.tulio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Frank on 26/04/2015.
 */
public class ListaAtividadesAdapter extends ArrayAdapter<ItemAtividade> {
    private Context context;
    private ArrayList<ItemAtividade> lista;

    public ListaAtividadesAdapter(Context context, ArrayList<ItemAtividade> lista){
        super(context, 0, lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemAtividade itemPosicao = this.lista.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.item_atividade, null);

        ImageView imageView = (ImageView)convertView.findViewById(R.id.imageAtiv);
        imageView.setImageResource(itemPosicao.getImagem());

        TextView txtTitulo = (TextView)convertView.findViewById(R.id.txtAtivTitulo);
        txtTitulo.setText(itemPosicao.getTitulo());

        TextView txtDescricao = (TextView)convertView.findViewById(R.id.txtAtivDesc);
        txtDescricao.setText(itemPosicao.getDescricao());

        TextView txtData = (TextView)convertView.findViewById(R.id.txtAtivData);
        txtData.setText(itemPosicao.getData());

        return convertView;


    }
}
