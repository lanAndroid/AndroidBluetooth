
package com.example.mybluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NewAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private List<Device> items;

    public NewAdapter(Context context, List<Device> items) {
        this.context = context;
        this.items = items;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_main, null); // TODO
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(Device item, ViewHolder holder) {
        //TODO
        holder.name.setText(item.getName());
        holder.address.setText(item.getAddress());
        holder.judge.setText(item.getJudge());
    }

    @Override
    public Device getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    protected class ViewHolder {
        // TODO FIELDS
        private TextView name;
        private TextView address;
        private TextView judge;

        public ViewHolder(View view) {
            // TODO ASSIGNEMENTS
            name = view.findViewById(R.id.name);
            address = view.findViewById(R.id.address);
            judge = view.findViewById(R.id.judge);
        }
    }
}
                                