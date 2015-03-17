package synergy.engines.suggestion;

import synergy.models.Photo;
import synergy.models.Tag;
import synergy.utilities.DateComparator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public static List<Tag> suggest(final Photo p){
        Photo photoFoundOnRight = null;
        Photo photoFoundOnLeft = null;
        final ExecutorService service = Executors.newFixedThreadPool(1);
        //final ExecutorService s = Executors.
        final Future<Photo> findOnRightTask = service.submit(() -> findTaggedPhotoOnRight(p));

        try {
            photoFoundOnLeft = findTaggedPhotoOnLeft(p);
            photoFoundOnRight = findOnRightTask.get();
            service.shutdownNow();
        }
        catch (Exception e){
            System.err.println(e);
        }


        if(photoFoundOnLeft == null){ //no photo on left
            if(photoFoundOnRight != null) { //check if photo on right
                List<Tag> tagsToReturn;
                tagsToReturn = photoFoundOnRight.getChildTags();
                while (tagsToReturn.size() < 3){
                    photoFoundOnRight = findTaggedPhotoOnRight(photoFoundOnRight);
                    if(photoFoundOnRight == null)
                        return tagsToReturn;
                    tagsToReturn.addAll(photoFoundOnRight.getChildTags());
                }
                System.out.println("SimpleDateEngine SUGGESTING: "+tagsToReturn);
                return tagsToReturn;
            }
        }
        else { //there is photo on left
            if(photoFoundOnRight == null) { //there is a photo on right
                List<Tag> tagsToReturn;
                tagsToReturn = photoFoundOnLeft.getChildTags();
                while (tagsToReturn.size() < 3){
                    photoFoundOnLeft = findTaggedPhotoOnLeft(photoFoundOnLeft);
                    if(photoFoundOnLeft == null)
                        return tagsToReturn;
                    tagsToReturn.addAll(photoFoundOnLeft.getChildTags());
                }
                System.out.println("SimpleDateEngine SUGGESTING: "+tagsToReturn);
                return tagsToReturn;
            }
            else { //there is a photo on both sides

                List<Tag> tagsToReturn;
                Photo closestPhoto = DateComparator.getClosestPhoto(p, photoFoundOnRight, photoFoundOnLeft);
                tagsToReturn = closestPhoto.getChildTags();

                while(tagsToReturn.size() < 3) {
                    Photo nextPhotoOnRight = findTaggedPhotoOnRight(closestPhoto);
                    Photo nextPhotoOnLeft = findTaggedPhotoOnLeft(closestPhoto);

                    if (nextPhotoOnLeft == null){
                        if (nextPhotoOnRight != null) {
                            closestPhoto = nextPhotoOnRight;
                        } else{
                            System.out.println("SimpleDateEngine SUGGESTING: "+tagsToReturn);
                            System.out.println("SimpleDateEngine SUGGESTING: "+tagsToReturn);
                            return tagsToReturn;
                        }
                    }
                    else {
                        if (nextPhotoOnRight == null) {
                            closestPhoto = nextPhotoOnLeft;
                        } else {
                            closestPhoto = DateComparator.getClosestPhoto(closestPhoto, nextPhotoOnRight, nextPhotoOnLeft);
                        }
                    }
                    if(closestPhoto == null){
                        System.out.println("SimpleDateEngine SUGGESTING: "+tagsToReturn);
                        return tagsToReturn;
                    }
                    tagsToReturn.addAll(closestPhoto.getChildTags());
                }
                System.out.println("SimpleDateEngine SUGGESTING: "+tagsToReturn);
                return tagsToReturn;
            }
        }
        System.out.println("SimpleDateEngine SUGGESTING: NULL!");
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
