package synergy.SuggestionEngine;

import synergy.models.Photo;
import synergy.models.Tag;

import java.util.Arrays;
import java.util.List;


public class Engine {

    protected static List<Photo> historicalPhotos;

    /**
     *Method takes a photo and returns a list of possible tags.
     *
     *This is the main suggestion method and it should be the only one used at anytime during the development process. The method that will be called by this method will be changed in time
     *so to avoid any confusion just use this one.
     *
     * @param p
     * @return
     */
    public static List<Tag> suggest(Photo p){
        return SimpleDateEngine.suggest(p);
    }

    /**
     * Prepares the engine by fetching the required data.
     */
    public static void prepare(){
        List<Photo> historicalPhotos = Photo.getAllPhotos(); // fetches all photos from database to be analysed.
    }
}
