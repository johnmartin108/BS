package com.example.johnmartin.bs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class GameSetupActivity extends AppCompatActivity {

    private final String[] NUM_PLAYERS = new String[]{"3","4","5","6"};
    private final String[] GAME_MODE = new String[]{"Online", "Offline"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, NUM_PLAYERS);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner num_spinner = (Spinner) this.findViewById(R.id.player_number_spinner);
        num_spinner.setAdapter(adapter);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, GAME_MODE);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner mode_spinner = (Spinner) this.findViewById(R.id.game_mode_spinner);
        mode_spinner.setAdapter(adapter);
    }

    public void setUpGame() {
        Intent i = new Intent(this, GameActivity.class);
        int num_players = ((Spinner) this.findViewById(R.id.player_number_spinner)).getSelectedItemPosition() + 3; //position 0 is 3 players
        int game_mode = ((Spinner) this.findViewById(R.id.game_mode_spinner)).getSelectedItemPosition();
        i.putExtra("num_players", num_players);
        i.putExtra("game_mode", game_mode); //0 is online game, 1 is offline
        startActivity(i);
    }
}
