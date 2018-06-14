package gusssar.prometheus;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

    public class TradeListAdapter extends BaseAdapter {
        Context ctx;
        LayoutInflater lInflater;
        ArrayList<Product> objects;

        TradeListAdapter(Context context, ArrayList<Product> products) {
            ctx = context;
            objects = products;
            lInflater = (LayoutInflater) ctx
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        static class ViewHolder {                                                                                   //
            TextView txtItem1;                                                                                              //
            TextView txtItem2;
            TextView txtItem3;                                                                                      //
        }                                                                                                        //
        // кол-во элементов
        @Override
        public int getCount() {
            return objects.size();
        }

        // элемент по позиции
        @Override
        public Object getItem(int position) {
            return objects.get(position);
        }

        // id по позиции
        @Override
        public long getItemId(int position) {
            return position;
        }

        // пункт списка
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;                                                                              //
            // используем созданные, но не используемые view
            View view = convertView;
            if (view == null) {
                view =lInflater.inflate(R.layout.list_item,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.txtItem1=(TextView) view.findViewById(R.id.pair_list_name);
                viewHolder.txtItem2=(TextView) view.findViewById(R.id.buy_list);
                viewHolder.txtItem3=(TextView) view.findViewById(R.id.sell_list);
                    view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            Product p = getProduct(position);
            viewHolder.txtItem1.setText(p.name1);
            viewHolder.txtItem2.setText(p.name2);
            viewHolder.txtItem3.setText(p.name3);
           // ((TextView) view.findViewById(R.id.pair_list_name)).setText(p.name);
           // ((TextView) view.findViewById(R.id.buy_list)).setText(p.name2);
           // ((TextView) view.findViewById(R.id.sell_list)).setText(p.name3);
            return view;
        }

        // товар по позиции
        Product getProduct(int position) {
            return ((Product) getItem(position));
        }

    }