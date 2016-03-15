package com.mygdx.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.XmlReader;

import java.io.IOException;

import org.jdom2.Document;


public class XMLReader {

    private static final String TAG = "XMLReader";

	public static XmlReader.Element readXML(String path) {
		System.out.println("Loading ressources from ["+path+"]");
        XmlReader reader = new XmlReader();
        XmlReader.Element element = null;
        try {
            element = reader.parse((Gdx.files.internal(path)));
        } catch (IOException e) {
            Gdx.app.log(TAG, "Error while loading xml from ["+path+"], "+e.getLocalizedMessage());
            //e.printStackTrace();
        }

        /*SAXBuilder builder = new SAXBuilder();
		Document doc = null;
		try {
			doc = builder.build(new File(path));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (doc.equals(null)) {
			System.out.println("Error : Can't load [" + path + "]");
			System.exit(1);
		}
		*/
		return element;
	}
}
