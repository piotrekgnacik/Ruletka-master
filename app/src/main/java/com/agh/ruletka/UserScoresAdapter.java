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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class UserScoresAdapter extends ArrayAdapter<MyUser> { //dziedziczenie za pomoca extends po arrayadapter

    //adapter do inicjalizacji listy uzytkownikow i ich wynikow

    LayoutInflater mInflater;
    ArrayList<MyUser> myUsers;

    public UserScoresAdapter(@NonNull Context context, @LayoutRes int resource,
                       @NonNull ArrayList<MyUser> objects) {
        super(context, resource, objects); //dziedziczenie po arrayadapter

        myUsers = objects;
        mInflater = LayoutInflater.from(context);
    }

    private static class ViewHolder{
        TextView gridTitle;
        TextView gridScore;
        TextView gridGames;

        int position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.score_element, null);

            holder = new ViewHolder();
            holder.gridTitle = (TextView) convertView.findViewById(R.id.user_name);
            holder.gridGames = (TextView) convertView.findViewById(R.id.games);
            holder.gridScore = (TextView) convertView.findViewById(R.id.score);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.position = position;

        holder.gridTitle.setText(myUsers.get(position).getName());
        holder.gridScore.setText(myUsers.get(position).getMoney() + "$");
        holder.gridGames.setText("" + myUsers.get(position).getIloscLosowan());

        return convertView;
    }
}
