package synergy.SuggestionEngine;

import synergy.models.Photo;
import synergy.models.Tag;

import java.util.Arrays;
import java.util.List;

public class Engine {

    /**
     *This is the simple method of suggestion, which doesn't do anything special.
     *
     * Method takes a photo and returns a list of possible tags. For now, the method is static and it also fetches all the photos from the database. This is inefficient and will change, but
     * for the purpose of developing the suggestion engine, we can use this, and leave the method of inputting historical data to be changed later.

     * @param p
     * @return
     */
    public static List<Tag> simpleSuggest(Photo p){
        List<Photo> historicalPhotos = Photo.getAllPhotos(); // fetches all photos from database to be analysed.
        List<Tag> currentTags = p.getTags();

        if(historicalPhotos.isEmpty()){
            System.err.println("There are no photos in history");
            return null;
        }

        //If it doesn't have any tags we suggest by date.
        //In the simple implementation it returns all the tags of the previously taken picture.
        if(currentTags.isEmpty()){
            return historicalPhotos.get(0).getTags();
        }

        //In the simple version of suggestion, it will return a list of top 5 (the number can be changed, of most common used tags.
        Tag[] topTags = new Tag[5];
        int[] topTagsOcc = new int[5];

        for(Tag currentPhotoTag: p.getChildTags()){
            int occurrences = 0;
            for(Photo photoInHistory: historicalPhotos){
                for(Tag tag: photoInHistory.getChildTags()){
                    if(currentPhotoTag.getValue().equals(tag.getValue()))
                        occurrences++;
                }
            }
            for(int i=0;i<topTagsOcc.length;++i){
                if(occurrences>topTagsOcc[i]){
                    for(int j=topTagsOcc.length-1;j>i;j--){
                        topTagsOcc[j] = topTagsOcc[j+1];
                        topTags[j] = topTags [j+1];
                    }
                    topTags[i] = currentPhotoTag;
                    topTagsOcc[i] = occurrences;
                }

            }
        }

        return Arrays.asList(topTags);
    }
}
