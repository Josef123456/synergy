package synergy.engines.suggestion;

import synergy.models.Photo;
import synergy.models.Tag;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Engine {

    public static List<Photo> historicalPhotos;

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
        List<Tag> unfilteredTags = SimpleDateEngine.suggest(p);
        Set<Tag> filteredTags = new HashSet<>();
        filteredTags.addAll(unfilteredTags);
        return new ArrayList<>(filteredTags);
    }

    /**
     * Prepares the engine by fetching the required data.
     */
    public static void prepare(){
        historicalPhotos = Photo.getAllPhotos(); // fetches all photos from database to be analysed.
        System.out.println(historicalPhotos);
    }
}
