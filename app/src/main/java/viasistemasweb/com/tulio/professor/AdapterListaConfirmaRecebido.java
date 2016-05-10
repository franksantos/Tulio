package viasistemasweb.com.tulio.professor;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import viasistemasweb.com.tulio.R;

/**
 * Created by frank on 30/03/2016.
 */
public class AdapterListaConfirmaRecebido extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private final List<ItemConfirmacaoAtividade> listaConfirmacao;


    public AdapterListaConfirmaRecebido(Context context, List<ItemConfirmacaoAtividade> listaConfirmacao) {
        this.context = context;
        this.listaConfirmacao = listaConfirmacao;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount(){
        return listaConfirmacao != null ? listaConfirmacao.size() : 0;
    }
    @Override
    public Object getItem(int position){
        return listaConfirmacao != null ? listaConfirmacao.get(position) : null;
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null){
            //não existe a view no cache para esta linha, então cria uma nova
            holder = new ViewHolder();
            //Busca o layout para linha da lista
            int layout = R.layout.item_lista_pais_confirma;
            convertView = inflater.inflate(layout, null);
            convertView.setTag(holder);
            holder.txtNomeAluno = (TextView) convertView.findViewById(R.id.txtNomeAluno);
            holder.txtNomePai = (TextView) convertView.findViewById(R.id.txtNomePaiAluno);
            holder.tracoConfirma = (ImageView) convertView.findViewById(R.id.TracoConfirmacao);
        }else{
            //já existe no cache
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tracoConfirma.setImageBitmap(null);
        ItemConfirmacaoAtividade objItem = listaConfirmacao.get(position);
        // agora que temos a view podemos atualizar os valores
        holder.txtNomeAluno.setText(objItem.getNomeAluno());
        holder.txtNomePai.setText(objItem.getNomePai());
        holder.tracoConfirma.setImageResource(objItem.getStatus());
        return convertView;

    }

    static class ViewHolder{
        TextView txtNomeAluno;
        TextView txtNomePai;
        ImageView tracoConfirma;

    }
}
