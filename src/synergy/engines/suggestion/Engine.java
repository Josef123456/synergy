package synergy.engines.suggestion;

import synergy.models.Photo;
import synergy.models.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the usage of the SimpleDateEngine and SimplePopularEngine. This class should be prepared before getting suggestions.
 */
public class Engine {

    public static List<Photo> historicalPhotos;

    /**
     *Method takes a photo and returns a list of possible tags.
     *
     *This is the main suggestion method and it should be the only one used at anytime. The methods that will be called and the way they are called will be changed in time
     *so to avoid any confusion this one should be the only one that is used externally.
     *
     * @param p The photo that we want tags suggested for
     * @return Returns a list of suggested Tags.
     */
    public static List<Tag> suggest(Photo p){
        List<Tag> popularTags;
        if( p.getChildTags().isEmpty() ){
            return getUniqueDates(p);
        }
        else{
            popularTags = SimplePopularEngine.suggest(p);
            if(popularTags.isEmpty()){
                return getUniqueDates(p);
            }
        }
        List<Tag> toReturn = new ArrayList<>();
        int i = 0;
        while ( i<popularTags.size() && i<10){
            toReturn.add(popularTags.get(i++));
        }
        return toReturn;
    }

    /**
     * Prepares the engine by fetching the required data from the database.
     */
    public static void prepare(){
        historicalPhotos = Photo.getAllPhotos(); // fetches all photos from database to be analysed.
    }

    private static List<Tag> getUniqueDates(Photo p){
        List<Tag> dateTags = SimpleDateEngine.suggest(p);
        List<Tag> uniqueDateTags = new ArrayList<>();
        if(dateTags != null)
            dateTags.stream().filter(t -> !uniqueDateTags.contains(t) && !p.getChildTags().contains(t)).forEach(uniqueDateTags::add);

        return uniqueDateTags;

    }
}
