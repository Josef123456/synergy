package synergy.engines.suggestion;

import synergy.models.Photo;
import synergy.models.Tag;

import java.util.ArrayList;
import java.util.List;


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
        List<Tag> dateTags = SimpleDateEngine.suggest(p);
        List<Tag> uniqueDateTags = new ArrayList<>();

        if( dateTags != null )
            for(Tag t:dateTags){
                if(!uniqueDateTags.contains(t))
                    uniqueDateTags.add(t);
            }

        if(p.getChildTags().isEmpty()){
            return uniqueDateTags;
        }
        else{
            List<Tag> popularTags = SimplePopularEngine.suggest(p);
            if(popularTags.isEmpty()){
                return uniqueDateTags;
            }
        }

        return SimplePopularEngine.suggest(p);
    }

    /**
     * Prepares the engine by fetching the required data.
     */
    public static void prepare(){
        historicalPhotos = Photo.getAllPhotos(); // fetches all photos from database to be analysed.
    }
}
