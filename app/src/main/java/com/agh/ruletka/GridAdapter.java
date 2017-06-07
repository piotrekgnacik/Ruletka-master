package com.agh.ruletka;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<BetModel> {


    //inicjalizacja adaptera ktory twozy nam plansze do gry
    LayoutInflater mInflater;
    ArrayList<BetModel> betModels;

    public GridAdapter(@NonNull Context context, @LayoutRes int resource,
                       @NonNull ArrayList<BetModel> objects) {
        super(context, resource, objects);

        betModels = objects;
        mInflater = LayoutInflater.from(context);
    }

    private static class ViewHolder{
        TextView text;
        TextView textBet;
        RelativeLayout background;

        int position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid, null);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.text);
            holder.textBet = (TextView) convertView.findViewById(R.id.bet_text);
            holder.background = (RelativeLayout) convertView.findViewById(R.id.background);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.position = position;
        holder.background.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 160));
        try {
            holder.text.setText("" + betModels.get(position).getPosition());
            holder.textBet.setText(betModels.get(position).getMoneyBet()+ "$");
            holder.background.setBackgroundResource(android.R.color.black);
            holder.background.setBackgroundResource(betModels.get(position).getColor());
        }

        catch (IndexOutOfBoundsException excep){
            Log.d("Index", excep.toString());
        }

        return convertView;
    }
}
