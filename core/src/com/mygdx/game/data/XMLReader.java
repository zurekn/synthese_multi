package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;

import java.io.IOException;


public class XMLReader {

    private static final String TAG = "XMLReader";

	public static XmlReader.Element readXML(String path) {
		System.out.println("Loading ressources from ["+path+"]");
        XmlReader reader = new XmlReader();
        XmlReader.Element element = null;
        try {
            element = reader.parse((Gdx.files.internal(path)));
        } catch (IOException e) {
            Gdx.app.log(TAG, "Error while loading xml from [" + path + "], " + e.getLocalizedMessage());
        }
		return element;
	}
}
