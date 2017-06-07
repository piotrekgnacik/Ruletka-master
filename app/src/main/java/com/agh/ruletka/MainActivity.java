package com.agh.ruletka;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {

    ArrayList<BetModel> models;
    TextView infoTabele;
    GridAdapter adapter;

    MyUser user;
    //obiekty klasy button
    Button first;
    Button second;
    Button third;

    Button red;
    Button black;

    Button firstHalf;
    Button secondHalf;

    Button start;
    Button zero;

    //zmienne
    int obstawiles = 0;
    int redBet = 0;
    int blackBet = 0;

    int zeroBet = 0;
    int firstBet = 0;
    int secondBet = 0;
    int thirdBet = 0;

    int firstHalfBet = 0;
    int secondHalfBet = 0;


    //baza danych inicjalizacja
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("games");

    // lista moich obstawien
    ArrayList<BetModel> myBets;

    //lista osob i ich wynikow pobierana z bazy zanych
    ArrayList<MyUser> usersScores;

    //adapter sluzy do adaptowania listy na widok o okreslonej ilosci elementow
    UserScoresAdapter scoreAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // tabela informacyjna
        infoTabele = (TextView) findViewById(R.id.tabele);

        //inicjalizacja listy
        models = new ArrayList<>(36);
        myBets = new ArrayList<>();
        setNewModels();


        //dialog wyswietla sie aby utorzyc nowego uzytkownika
        if(user == null){
            final EditText input = new EditText(MainActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setNegativeButton("Wyjdź", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setPositiveButton("Zapisz!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    user = new MyUser(input.getText().toString());
                    dialog.dismiss();
                    tabeleRefresh();
                }
            });
            builder.setCancelable(false);
            builder.setView(input);
            builder.setTitle("Wpisz swoje imie");
            builder.setMessage("Twoje imię");

            AlertDialog dialog = builder.create();
            dialog.show();
        }
        else
            tabeleRefresh();

        //przyciski
        first = (Button) findViewById(R.id.first);
        second = (Button) findViewById(R.id.second);
        third = (Button) findViewById(R.id.third);
        red = (Button) findViewById(R.id.red);
        black = (Button) findViewById(R.id.black);
        firstHalf = (Button) findViewById(R.id.first_half);
        secondHalf = (Button) findViewById(R.id.second_half);
        start = (Button) findViewById(R.id.start);
        zero = (Button) findViewById(R.id.zero);

        //adapter listy graczy i ich wynikow
        usersScores = new ArrayList<>();
        scoreAdapter = new UserScoresAdapter(getBaseContext(), R.layout.score_element, usersScores);


        //przypisywanie akcji onclick do przyciskow
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (obstawiles > 0) {
                    user.addIloscLosowan();


                    //klasa random sluzy do losowania
                    Random random = new Random();
                    int number = random.nextInt(36);


                    //dodaje od poprawnego typu
                    for (BetModel myBet : myBets) {
                        if (myBet.getPosition() == number) {
                            user.putMoney(5 * 10);
                        }
                    }

                    try {
                        //dodaje od poprawnego koloru
                        if (models.get(number-1).getColor() == android.R.color.holo_red_dark)
                            user.putMoney(redBet);
                        else if (models.get(number-1).getColor() == android.R.color.black)
                            user.putMoney(blackBet);
                    }catch (Exception e){
                        Log.e("Wyjatek", "wylosowano zero");
                    }

                    if (number > 24)
                        user.putMoney(thirdBet * 2);

                    if (number <= 24 && number > 12)
                        user.putMoney(secondBet * 2);

                    if (number <= 11)
                        user.putMoney(firstBet * 2);

                    if (number <= 18)
                        user.putMoney(firstHalfBet);

                    if (number > 18)
                        user.putMoney(secondHalfBet);

                    resetValues();
                    tabeleRefresh(number);
                }
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getMoney() > 0) {
                    zeroBet += 5;
                    myBets.add(new BetModel(0, 5));
                    zero.setText("0\n" + zeroBet+"$");
                    user.putMoney(-5);
                    obstawiles += 5;

                    tabeleRefresh();

                }
            }
        });
        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getMoney() > 0) {
                    if (thirdBet > 0 && secondBet > 0) {
                    } else {
                        firstBet += 5;
                        first.setText("1 - 12\n" + firstBet + "$");
                        user.putMoney(-5);
                        obstawiles += 5;
                        tabeleRefresh();
                    }
                }
            }
        });
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getMoney() > 0) {
                    if (thirdBet > 0 && firstBet > 0) {
                    } else {
                        secondBet += 5;
                        second.setText("13 - 24\n" + secondBet + "$");
                        user.putMoney(-5);
                        obstawiles += 5;
                        tabeleRefresh();
                    }
                }
            }
        });
        third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getMoney() > 0) {
                    if (firstBet > 0 && secondBet > 0) {
                    } else {
                        thirdBet += 5;
                        third.setText("25 - 36\n" + thirdBet + "$");
                        user.putMoney(-5);
                        obstawiles += 5;
                        tabeleRefresh();
                    }
                }
            }
        });
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getMoney() >0) {
                    if (blackBet < 5) {
                        redBet += 5;
                        red.setText("\n" + redBet + "$");
                        user.putMoney(-5);
                        obstawiles += 5;
                        tabeleRefresh();
                    }
                }
            }
        });
        black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getMoney() >0) {
                    if (redBet < 5) {
                        blackBet += 5;
                        black.setText("\n" + blackBet + "$");
                        user.putMoney(-5);
                        obstawiles += 5;
                        tabeleRefresh();
                    }
                }
            }
        });
        firstHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getMoney() >0) {
                    if (secondHalfBet < 5) {
                        firstHalfBet += 5;
                        firstHalf.setText("1-18\n" + firstHalfBet + "$");
                        user.putMoney(-5);
                        obstawiles += 5;
                        tabeleRefresh();
                    }
                }
            }
        });
        secondHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getMoney() >0) {
                    if (firstHalfBet < 5) {
                        secondHalfBet += 5;
                        secondHalf.setText("1-18\n" + secondHalfBet + "$");
                        user.putMoney(-5);
                        obstawiles += 5;
                        tabeleRefresh();
                    }
                }
            }
        });

        //przypisywanie akcji do gridview za pomoca gridadaptera przechowujacego informacje
        adapter = new GridAdapter(getBaseContext(), R.layout.grid, models);
        GridView gridView = (GridView) findViewById(R.id.grids);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(user.getMoney()>0) {
                    models.get(position).addMoneyBet(5);
                    user.putMoney(-5);
                    obstawiles += 5;
                    myBets.add(new BetModel(5, position+1));
                }
                adapter.notifyDataSetChanged();

                tabeleRefresh();
            }
        });


    }

    //zerujemy wartości
    private void resetValues() {


        secondHalfBet = 0;
        secondHalf.setText("19-36\n0$");
        firstHalfBet = 0;
        firstHalf.setText("1-18\n0$");
        zeroBet = 0;
        zero.setText("0\n" + zeroBet + "$");
        firstBet = 0;
        secondBet = 0;
        thirdBet = 0;
        first.setText("1 - 12\n0$");
        second.setText("13 - 24\n0$");
        third.setText("25 - 36\n0$");
        myBets.clear();
        obstawiles = 0;
        redBet = 0;
        blackBet = 0;
        black.setText("\n" + blackBet+"$");
        red.setText("\n" + redBet+"$");

        setNewModels();
        adapter.notifyDataSetChanged();
    }

    //odswierza nam tabele wynikow
    private void tabeleRefresh() {
        infoTabele.setText(user.getName() + "\nPortfel: " + user.getMoney() +
                "$\nObstawileś: " + obstawiles + "$\n");
    }

    //odswierza nam tabele wynikow
    private void tabeleRefresh(int number) {
        infoTabele.setText(user.getName() + "\nPortfel: " + user.getMoney() +
                "$\nObstawileś: " + obstawiles + "$\nWYLOSOWANO: " + number);
    }


    //metoda generuje kolory
    void setNewModels(){
        models.clear();
        for (int i = 1; i < 37 ; i++ ){
            if(i<11) {
                if (i % 2 == 0)
                    models.add(new BetModel(0, i, android.R.color.black));
                else
                    models.add(new BetModel(0, i, android.R.color.holo_red_dark));
            }
            else if(i<18) {
                if (i % 2 == 0)
                    models.add(new BetModel(0, i, android.R.color.holo_red_dark));
                else
                    models.add(new BetModel(0, i, android.R.color.black));
            }
            else if(i<29) {
                if (i % 2 == 0)
                    models.add(new BetModel(0, i, android.R.color.black));
                else
                    models.add(new BetModel(0, i, android.R.color.holo_red_dark));
            }else{
                if (i % 2 == 0)
                    models.add(new BetModel(0, i, android.R.color.holo_red_dark));
                else
                    models.add(new BetModel(0, i, android.R.color.black));
            }
        }
    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myRef.child(user.getName()).setValue(user);
                finish();
                dialog.dismiss();
            }
        });

        builder.setNeutralButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                dialog.dismiss();
            }
        });
        builder.setTitle("Wyjście");
        builder.setMessage("Czy chcesz zapisać swoj wynik?");

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                usersScores.clear();

                // pobieramy wszystkie dane od nowa gdy zostanie wprowadzona zmiana
                for (DataSnapshot productsSnapshot : dataSnapshot.getChildren()) {
                    MyUser user = productsSnapshot.getValue(MyUser.class);
                    usersScores.add(user);
                }

                // ponownie generuje listView
                scoreAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Blad", "Failed to read value.", error.toException());
            }
        });
    }

    public void otworzListe(View view) {
        final GridView scores = new GridView(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        scores.setLayoutParams(lp);
        scores.setNumColumns(2);

        scores.setAdapter(scoreAdapter);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(scores);
        builder.setTitle("Lista graczy");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
