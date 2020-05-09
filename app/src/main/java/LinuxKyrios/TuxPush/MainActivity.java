package LinuxKyrios.TuxPush;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Timer
    Integer counter = 0;

    //saved highest score
    Integer highestScore = 0;

    //logical values
    boolean isFirstUsage = true;
    boolean gameOn = false;

    //Display object
    TextView textClock;
    TextView textNumber;
    Button buttonStart;

    //To save highest score
    SharedPreferences.Editor editor;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textClock = (TextView) findViewById(R.id.textCounter);
        textNumber = (TextView) findViewById(R.id.textNumber);
        buttonStart = (Button) findViewById(R.id.buttonstart);

        //To add a possibility to save state of application
        pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        highestScore = pref.getInt("key_name", 0);

        //Start button with checking if game is for first use or not and setting game on
        buttonStart.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstUsage){
                    isFirstUsage = false;
                    newGame();
                }
                if (gameOn){
                    counter++;
                    textNumber.setText(counter.toString());
                }
            }
        });

        //If first usage is off and game is not on - starting new game
        buttonStart.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                if(!isFirstUsage && !gameOn){
                    newGame();
                }
                return false;
            }
        });
    }

    //function newGame with function onFinish and possibilities endings
    void newGame(){
        gameOn = true;
        counter = 0;
        buttonStart.setText("TAP!");
        textNumber.setText("0");

        CountDownTimer ct = new CountDownTimer(10000, 1000){
            @Override
            public void onTick(long millisUntilFinished){
                String v = String.format("%02d", millisUntilFinished / 60000);
                int va = (int) ((millisUntilFinished) / 1000);
                textClock.setText(v+":"+ String.format("%02d",va));
            }
            @Override
            public void onFinish(){
                gameOn = false;
                if(counter > highestScore){
                    editor = pref.edit();
                    highestScore = counter;
                    editor.putInt("key_name", highestScore);
                    editor.commit();
                    textClock.setText("Congratulations! The new highest score: " + highestScore);
                } else {
                    int savedScore = pref.getInt("key_name", 0);
                    textClock.setText("Try again! Beat: " + savedScore);
                }
                buttonStart.setText("Restart");
                Toast.makeText(getApplicationContext(), "przytrzymaj przycisk aby zacząć ponownie", Toast.LENGTH_LONG).show();
            }
        };
        ct.start();
    }
}
