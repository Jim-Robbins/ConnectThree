package connect3.jrobbins.com.connectthree;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer player1Audio;
    private MediaPlayer player2Audio;
    private MediaPlayer mAudio;
    private MediaPlayer tieAudio;
    private MediaPlayer winAudio;

    private TextView resultTxt;
    private byte playerTurn = 1;
    int [] gameState = {0,0,0,0,0,0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultTxt = (TextView) findViewById(R.id.resultTxt);

        player1Audio = MediaPlayer.create(this, R.raw.snd_1star);
        player2Audio = MediaPlayer.create(this, R.raw.snd_2stars);
        mAudio = MediaPlayer.create(this, R.raw.fanfare_loc_unlock);
        tieAudio = MediaPlayer.create(this, R.raw.fanfare_sad);
        winAudio = MediaPlayer.create(this, R.raw.fanfare_happy);
    }

    public View.OnClickListener onResetGame = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GridLayout myGrid = (GridLayout) findViewById(R.id.boardGridLayout);
            int count = myGrid.getChildCount();
            for(int i=0; i<count; i++)
            {
                View view = myGrid.getChildAt(i);
                view.setAlpha(0f);
                view.setOnClickListener(onStartGame);

                mAudio.start();
            }
        }
    };

    public View.OnClickListener onStartGame = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onPlaceChip(v);
        }
    };

    public void onPlaceChip(View view)
    {
        ImageView chip = (ImageView) view;
        Log.i("onPlaceChip","called");
        setChip(chip);
    }

    private void setChip(ImageView chip)
    {
        chip.setOnClickListener(null);
        int gameStateIndex = Integer.parseInt(chip.getTag().toString());

        gameState[gameStateIndex-1] =  playerTurn;
        boolean isWinner = checkWinner(playerTurn);

        int imgResId = getResources().getIdentifier("player"+playerTurn, "drawable", getPackageName());
        Log.i("setChip","imgResId"+imgResId);
        chip.setImageResource(imgResId);

        int stringResId;
        if(!isWinner) {
            if(!isTieGame())
            {
                if (playerTurn == 1) {
                    playerTurn = 2;
                    player1Audio.start();
                } else {
                    playerTurn = 1;
                    player2Audio.start();
                }
                stringResId = getResources().getIdentifier("player" + playerTurn, "string", getPackageName());
            }
            else
            {
                stringResId = getResources().getIdentifier("tieGame", "string", getPackageName());
                gameOver();

                tieAudio.start();
            }
        }
        else
        {

            winAudio.start();
            stringResId = getResources().getIdentifier("player" + playerTurn + "Win", "string", getPackageName());
            gameOver();
        }

        resultTxt.setText(stringResId);

        animateChip(chip);

    }

    private void gameOver() {
        GridLayout myGrid = (GridLayout) findViewById(R.id.boardGridLayout);
        int count = myGrid.getChildCount();
        for(int i=0; i<count; i++)
        {
            View view = myGrid.getChildAt(i);
            view.setOnClickListener(onResetGame);
            gameState[i] = 0;
        }
    }

    private boolean isTieGame() {
        for(int x: gameState){
            if(x == 0)
                return false;
        }
        return true;
    }

    private boolean checkWinner(byte playerTurn) {
        boolean isWinner = false;
        if ((gameState[0] == playerTurn && gameState[1] == playerTurn && gameState[2] == playerTurn) ||
                (gameState[0] == playerTurn && gameState[4] == playerTurn && gameState[8] == playerTurn) ||
                (gameState[1] == playerTurn && gameState[4] == playerTurn && gameState[7] == playerTurn) ||
                (gameState[2] == playerTurn && gameState[5] == playerTurn && gameState[8] == playerTurn) ||
                (gameState[0] == playerTurn && gameState[4] == playerTurn && gameState[8] == playerTurn) ||
                (gameState[0] == playerTurn && gameState[3] == playerTurn && gameState[6] == playerTurn) ||
                (gameState[3] == playerTurn && gameState[4] == playerTurn && gameState[5] == playerTurn) ||
                (gameState[6] == playerTurn && gameState[7] == playerTurn && gameState[8] == playerTurn) ||
                (gameState[2] == playerTurn && gameState[4] == playerTurn && gameState[6] == playerTurn))
        {
            isWinner = true;
        }

        return isWinner;
    }

    private void animateChip(ImageView chip)
    {
        chip.setScaleX(3f);
        chip.setScaleY(3f);

        chip.animate()
                .alpha(1.0f)
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(500);
    }
}
