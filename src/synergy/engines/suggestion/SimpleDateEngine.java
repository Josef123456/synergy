package synergy.engines.suggestion;

import synergy.utilities.DateComparator;
import synergy.models.Photo;
import synergy.models.Tag;

import java.util.List;
import java.util.concurrent.*;

/**
 * Created by sari on 27/02/15.
 */
public class SimpleDateEngine {
    static int photoIndex; //the index of the current photo
    /**
     * This method finds the nearest photo that contains tags and returns the contained tags.
     *
     * @param p
     * @return
     */
    public static List<Tag> suggest(final Photo p){

        Photo photoFoundOnRight = null;
        Photo photoFoundOnLeft = null;
        final ExecutorService service = Executors.newFixedThreadPool(1);
        final Future<Photo> findOnRightTask = service.submit(new Callable(){
            @Override
            public Photo call() throws Exception{
                return findTaggedPhotoOnRight(p);
            }

        });

        try {
            photoFoundOnLeft = findTaggedPhotoOnLeft(p);
            photoFoundOnRight = findOnRightTask.get();

            System.out.println("PHOTO FOUND ON LEFT: " + photoFoundOnLeft);
            System.out.println("PHOTO FOUND ON RIGHT: " + photoFoundOnRight);
        }
        catch (Exception e){
            System.err.println(e);
        }


        if(photoFoundOnLeft == null){
            if(photoFoundOnRight != null)
                return photoFoundOnRight.getChildTags();
        }
        else {
            if(photoFoundOnRight == null)
               return photoFoundOnLeft.getChildTags();
            else
                return DateComparator.getClosestPhoto(p, photoFoundOnRight, photoFoundOnLeft).getChildTags();
        }

        return null;

    }

    /**
     * Finds the closest tagged photo on the right of this photo(future or past depending on the order it was sorted).
     * @return
     */
    private static Photo findTaggedPhotoOnRight(Photo p){

        int index =  Engine.historicalPhotos.indexOf(p)+1;

        if(index == Engine.historicalPhotos.size())
            return null;

        while(Engine.historicalPhotos.get(index).getChildTags().isEmpty() && index < Engine.historicalPhotos.size()-1){
            index++;
        }

        if(!Engine.historicalPhotos.get(index).getChildTags().isEmpty()){ //if it has tags
            return Engine.historicalPhotos.get(index); //return the photo
        }

        return null; // nothing was found
    }

    /**
     *  Finds the closest tagged photo on the left of this photo(future or past depending on the order it was sorted)
     * @return
     */
    private static Photo findTaggedPhotoOnLeft(Photo p){

        int index =  Engine.historicalPhotos.indexOf(p)-1;

        if(index == -1)
            return null;


        while (Engine.historicalPhotos.get(index).getChildTags().isEmpty() && index>0){
            index--;
        }

        if(!Engine.historicalPhotos.get(index).getChildTags().isEmpty()){
            return Engine.historicalPhotos.get(index);
        }

        return null;
    }


}
