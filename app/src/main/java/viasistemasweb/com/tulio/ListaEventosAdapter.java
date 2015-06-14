package viasistemasweb.com.tulio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Frank on 06/05/2015.
 */
public class ListaEventosAdapter extends ArrayAdapter<ItemEvento> {
    private Context context;
    private ArrayList<ItemEvento> lista;

    public ListaEventosAdapter(Context context, ArrayList<ItemEvento> lista){
        super(context, 0, lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemEvento itemPosicao = this.lista.get(position);

        convertView = LayoutInflater.from(this.context).inflate(R.layout.item_eventos, null);

        TextView txtDescricao = (TextView)convertView.findViewById(R.id.tvDescEvento);
        txtDescricao.setText(itemPosicao.getDescricao());

        TextView txtData = (TextView)convertView.findViewById(R.id.tvDataAviso);
        txtData.setText(itemPosicao.getData());

        return convertView;


    }


}
