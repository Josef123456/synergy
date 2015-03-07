package synergy.engines.suggestion;

import synergy.models.Photo;
import synergy.models.Tag;
import synergy.utilities.DateComparator;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
            if(photoFoundOnRight != null) {
                List<Tag> tagsToReturn;
                tagsToReturn = photoFoundOnRight.getChildTags();
                while (tagsToReturn.size() < 3){
                    photoFoundOnRight = findTaggedPhotoOnRight(photoFoundOnRight);
                    //System.out.println("TAGSTORETURN SIZE" + tagsToReturn.size());
                    if(photoFoundOnRight == null)
                        return tagsToReturn;
                    tagsToReturn.addAll(photoFoundOnRight.getChildTags());
                }
                return tagsToReturn;


            }
        }
        else {
            if(photoFoundOnRight == null) {
                List<Tag> tagsToReturn;
                tagsToReturn = photoFoundOnLeft.getChildTags();
                while (tagsToReturn.size() < 3){
                    photoFoundOnLeft = findTaggedPhotoOnLeft(photoFoundOnLeft);
                    if(photoFoundOnLeft == null)
                        return tagsToReturn;
                    tagsToReturn.addAll(photoFoundOnRight.getChildTags());
                }
                return tagsToReturn;
            }
            else {

                List<Tag> tagsToReturn;
                Photo closestPhoto = DateComparator.getClosestPhoto(p, photoFoundOnRight, photoFoundOnLeft);
                tagsToReturn = closestPhoto.getChildTags();

                while(tagsToReturn.size() < 3) {
                    Photo nextPhotoOnRight = findTaggedPhotoOnRight(closestPhoto);
                    Photo nextPhotoOnLeft = findTaggedPhotoOnLeft(closestPhoto);

                    if (nextPhotoOnLeft == null){
                        if (nextPhotoOnRight != null) {
                            closestPhoto = nextPhotoOnRight;
                        } else
                            return tagsToReturn;
                    }
                    else {
                        if (nextPhotoOnRight == null) {
                            closestPhoto = nextPhotoOnRight;
                        } else {
                            closestPhoto = DateComparator.getClosestPhoto(closestPhoto, nextPhotoOnRight, nextPhotoOnLeft);
                        }
                    }
                    if(closestPhoto == null)
                        return tagsToReturn;
                    tagsToReturn.addAll(closestPhoto.getChildTags());
                }

                return tagsToReturn;
            }
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
        System.out.println(index +" "+Engine.historicalPhotos.indexOf(p));

        if(index == -1)
            return null;


        while (Engine.historicalPhotos.get(index).getChildTags().isEmpty() && index>0){
            index--;
        }
        Photo photo = Engine.historicalPhotos.get(index);
        System.out.println("LLLEEEEFFFFTTT" + photo.getChildTags()+ " "+ index+" "+ photo);
        if(!Engine.historicalPhotos.get(index).getChildTags().isEmpty()){
            return Engine.historicalPhotos.get(index);
        }

        return null;
    }


}
