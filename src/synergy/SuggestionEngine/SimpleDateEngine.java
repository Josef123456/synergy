package synergy.SuggestionEngine;

import synergy.Utilities.DateComparator;
import synergy.models.Photo;
import synergy.models.Tag;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by sari on 27/02/15.
 */
public class SimpleDateEngine {

    /**
     * This method finds the nearest photo that contains tags and returns the contained tags.
     *
     * @param p
     * @return
     */
    public static List<Tag> suggest(Photo p){
        final int photoIndex= Engine.historicalPhotos.indexOf(p); //the index of the current photo

        Photo pastFoundPhoto; //the nearest tagged photo found in the past
        Photo futureFoundPhoto = null; // the nearest tagged photo found in the future

        final ExecutorService service;
        final Future<Photo> findOnRight;
        service = Executors.newFixedThreadPool(1);

        //Finds futureFoundPhoto
        findOnRight = service.submit(new Callable() {
            @Override
            public Photo call() throws Exception {

                int index = photoIndex+1;
                while(Engine.historicalPhotos.get(index).getChildTags().isEmpty() && index < Engine.historicalPhotos.size()-1){
                    index++;
                }
                if(!Engine.historicalPhotos.get(index).getChildTags().isEmpty()){ //if it has tags
                    return Engine.historicalPhotos.get(index); //return the photo
                }

                return null; // nothing was found

            }
        });

        //Finds pastFoundPhoto
        int index = photoIndex;
        while (Engine.historicalPhotos.get(index).getChildTags().isEmpty() && index>0){
            index--;
        }
        if(!Engine.historicalPhotos.get(index).getChildTags().isEmpty()){
            pastFoundPhoto = Engine.historicalPhotos.get(index);
        }
        else
            pastFoundPhoto = null;


        try {
            futureFoundPhoto = findOnRight.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if(pastFoundPhoto.equals(null)){
            if(!futureFoundPhoto.equals(null))
                return futureFoundPhoto.getChildTags();
        }
        else {
            if(futureFoundPhoto.equals(null))
               return pastFoundPhoto.getChildTags();
            else
                return DateComparator.getClosestPhoto(p, futureFoundPhoto, pastFoundPhoto).getChildTags();

        }

        return null;

    }


}
