/**
 * JSONLoader.java: Class used to read in the data from the JSON file
 *
 * @author Cameron Colleran
 * @version 1.0
 */
package edu.miracosta.cs134.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JSONLoader
{
    /**
     * Method which is used to read in data from the JSON file and store it in a list
     *
     * @param context the context
     * @return a list full with objects which have been read in from the JSON file
     * @throws IOException thrown if their is issue reading in the JSON file
     */
    public static List<Superhero> loadJSONFromAsset(Context context) throws IOException
    {
        List<Superhero> allHeroesList = new ArrayList<>();
        String json = null;
        InputStream is = context.getAssets().open("cs134superheroes.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");

        try
        {
            // Link up JSON object to the root object
            JSONObject jsonRootObject = new JSONObject(json);
            // Link up JSONArray to the JSON root object
            JSONArray allHeroesJSON = jsonRootObject.getJSONArray("CS134Superheroes");

            JSONObject obj;
            // Variables to store in added objects
            String imageName, name, superpower, oneThing;
            int length = allHeroesJSON.length();

            // Loop which reads in all objects from the JSON file and adds them to the list
            for (int i = 0; i < length; i++)
            {
                obj = allHeroesJSON.getJSONObject(i);
                imageName = obj.getString("FileName");
                name = obj.getString("Name");
                superpower = obj.getString("Superpower");
                oneThing = obj.getString("OneThing");
                allHeroesList.add(new Superhero(imageName, name, superpower, oneThing));
            }
        }
        catch (JSONException e)
        {
            Log.e("CS 134 Superheroes", "Error loading in JSON data", e);
        }

        return allHeroesList;
    }
}
