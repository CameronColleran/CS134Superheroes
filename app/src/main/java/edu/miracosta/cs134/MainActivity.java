/**
 * MainActivity.java: Serves as the primary screen which the user interacts with, displaying the
 * buttons and picture of the hero
 *
 * @author Cameron Colleran
 * @version 1.0
 */

package edu.miracosta.cs134;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.miracosta.cs134.model.JSONLoader;
import edu.miracosta.cs134.model.Superhero;

public class MainActivity extends AppCompatActivity
{
    /** Creating constants */
    public static final String TAG = "CS 134 Superheroes";
    public static final int HEROES_IN_QUIZ = 10;
    public static final String PREF_QUIZ_TYPE = "pref_quizType";

    /** Creating lists */
    private List<Superhero> allHeroesList;
    private List<Superhero> quizHeroesList;

    /** Creating Secure Random Number Generator */
    private SecureRandom rng;

    /** Creating Handler for delay effect */
    private Handler handler;

    /** Creating ints for answers */
    private int mTotalGuesses;
    private int mCorrectGuesses;

    /** Creating various views */
    private Button[] mButtons = new Button[4];
    private TextView mQuestionNumberTextView;
    private ImageView mHeroImageView;
    private TextView mAnswerTextView;
    private TextView guessTextView;

    /** Creating correct hero object */
    private Superhero mCorrectHero;

    /** Creating quiz type String variable */
    private String mQuizType = "Superhero Name";

    /** Creating String for correctAttribute of the hero */
    private String correctAttribute;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quizHeroesList = new ArrayList<>(HEROES_IN_QUIZ);
        rng = new SecureRandom();
        handler = new Handler();

        // Filling allHeroesList with JSON data
        try
        {
            allHeroesList = JSONLoader.loadJSONFromAsset(this);
        }
        catch (IOException e)
        {
            Log.e(TAG, "Error loading data from JSON to Main Activity", e);
        }

        // Wiring up views
        mQuestionNumberTextView = findViewById(R.id.questionNumberTextView);
        mHeroImageView = findViewById(R.id.heroImageView);
        mButtons[0] = findViewById(R.id.button);
        mButtons[1] = findViewById(R.id.button2);
        mButtons[2] = findViewById(R.id.button3);
        mButtons[3] = findViewById(R.id.button4);
        mAnswerTextView = findViewById(R.id.answerTextView);
        guessTextView = findViewById(R.id.guessTextView);

        // Setting question number text view to 1
        mQuestionNumberTextView.setText(getString(R.string.question,1, HEROES_IN_QUIZ));

        // Attach preference listener
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(mSharedPreferenceChangeListener);
        resetQuiz();
    }

    /**
     * Method which "resets" quiz by resetting all data
     */
    public void resetQuiz()
    {
        mCorrectGuesses = 0;
        mTotalGuesses = 0;

        quizHeroesList.clear();

        Superhero random;
        // Adding random heroes to the quiz list
        while (quizHeroesList.size() < HEROES_IN_QUIZ)
        {
            random = allHeroesList.get(rng.nextInt(allHeroesList.size()));
            if ((!quizHeroesList.contains(random)))
            {
                quizHeroesList.add(random);
            }
        }

        loadNextHero();
    }

    /**
     * Method which transitions view to display data for user to guess on
     */
    public void loadNextHero()
    {
        // Initialize the mCorrectCountry by removing the item at position 0 in the mQuizCountries
        mCorrectHero = quizHeroesList.remove(0);

        // Setting both guessTextView and correctAttribute dependent on type of quiz
        if (mQuizType.equals("Superhero Name"))
        {
            guessTextView.setText(R.string.guess_name);
            correctAttribute = mCorrectHero.getName();
        }
        else if (mQuizType.equals("Superpower"))
        {
            guessTextView.setText(R.string.guess_power);
            correctAttribute = mCorrectHero.getSuperpower();
        }
        else
        {
            guessTextView.setText(R.string.guess_one_thing);
            correctAttribute = mCorrectHero.getOneThing();
        }

        // Clear the mAnswerTextView so that it doesn't show text from the previous question
        mAnswerTextView.setText("");

        // Display current question number in the mQuestionNumberTextView
        mQuestionNumberTextView.setText(getString(R.string.question, (HEROES_IN_QUIZ - quizHeroesList.size()), HEROES_IN_QUIZ));

        // Use AssetManager to load next image from assets folder
        AssetManager am = getAssets();

        // Fill heroImageView with corresponding image of hero in assets
        try
        {
            InputStream stream = am.open(mCorrectHero.getImageName());
            Drawable image = Drawable.createFromStream(stream, mCorrectHero.getImageName());
            mHeroImageView.setImageDrawable(image);
        }
        catch (IOException e)
        {
            Log.e(TAG, "Error loading image", e);
        }

        // Shuffle the order of all the heroes (use Collections.shuffle)
        Collections.shuffle(allHeroesList);


        // Set all buttons to enabled and their text to appropriate version of quiz from allHeroesList
        for (int i = 0; i < mButtons.length; i++)
        {
            mButtons[i].setEnabled(true);
            int randIndex;

            // Purpose of do while loops is to prevent duplicate buttons (correct answer be randomly chosen again)
            if (mQuizType.equals("Superhero Name"))
            {
                do
                {
                    randIndex = rng.nextInt(allHeroesList.size());
                    mButtons[i].setText(allHeroesList.get(randIndex).getName());
                }
                while (allHeroesList.get(randIndex).getName().equals(correctAttribute));
            }
            else if (mQuizType.equals("Superpower"))
            {
                do
                {
                    randIndex = rng.nextInt(allHeroesList.size());
                    mButtons[i].setText(allHeroesList.get(randIndex).getSuperpower());
                }
                while (allHeroesList.get(randIndex).getSuperpower().equals(correctAttribute));
            }
            else
            {
                do
                {
                    randIndex = rng.nextInt(allHeroesList.size());
                    mButtons[i].setText(allHeroesList.get(randIndex).getOneThing());
                }
                while (allHeroesList.get(randIndex).getOneThing().equals(correctAttribute));
            }

        }

        // Set a random button to the correct attribute
        int randIndex = rng.nextInt(4);
        mButtons[randIndex].setText(correctAttribute);
    }

    /**
     * Method which updates data and informs user when a button is clicked
     * @param v View object which is cast as button
     */
    public void makeGuess(View v)
    {
        // Cast view into a button
        Button clickedButton = (Button) v;

        // Get name of hero that was clicked on the button
        String guess = clickedButton.getText().toString();

        // Increment number of total guesses
        mTotalGuesses++;

        // If guess was correct
        if (guess.equals(correctAttribute))
        {
            // Create media player and play "success" audio
            final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.success);
            mediaPlayer.start();

            // Increment number of correct guesses
            mCorrectGuesses++;

            // Disable all buttons
            for (int i = 0; i < mButtons.length; i++)
            {
                mButtons[i].setEnabled(false);
            }

            // Set color of correct answer to green
            mAnswerTextView.setTextColor(getResources().getColor(R.color.correct_answer));
            // Set text of correct answer to name of hero
            mAnswerTextView.setText(correctAttribute);

            // If not at last question
            if (mCorrectGuesses < HEROES_IN_QUIZ)
            {
                // Code a 2 second delay using a handler to load the next hero
                handler.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        loadNextHero();
                    }
                }, 2000);
            }
            // Else at last question
            else
            {
                // Create alert dialog to show user their score
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.results, mTotalGuesses, 100 * (double) mCorrectGuesses/mTotalGuesses));

                // Let user restart the quiz
                builder.setPositiveButton(getString(R.string.reset_quiz), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        resetQuiz();
                    }
                });

                // Prevent user from cancelling dialog and getting stuck at end of quiz
                builder.setCancelable(false);

                builder.create();
                builder.show();
            }
        }
        // Else guess was incorrect
        else
        {
            // Create media player and play "failed" audio
            final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.failed);
            mediaPlayer.start();

            // Disable incorrect button
            clickedButton.setEnabled(false);

            // Set text of answer text view to incorrect
            mAnswerTextView.setText(R.string.incorrect_answer);

            // Set color of answer text view to red
            mAnswerTextView.setTextColor(getResources().getColor(R.color.incorrect_answer));
        }

    }

    /**
     * Generated method for the Preference Manager
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Generated method for the Preference Manager
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method/Preference object which detects changes to quiz type preference and updates the quiz type variable
     */
    SharedPreferences.OnSharedPreferenceChangeListener mSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener()
    {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key)
        {
            switch (key)
            {
                case PREF_QUIZ_TYPE:
                    mQuizType = sharedPreferences.getString(PREF_QUIZ_TYPE, mQuizType);
                    updateBasedOnQuizChange();
                    Toast.makeText(MainActivity.this, R.string.restarting_quiz,
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * Called after a new preference change is detected to reset the quiz will the corresponding quiz type
     */
    public void updateBasedOnQuizChange()
    {
        resetQuiz();
    }

}
