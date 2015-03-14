package synergy.engines.suggestion;

import synergy.models.Photo;
import synergy.models.Relationship;
import synergy.models.Tag;
import synergy.utilities.RelationshipMerger;

import java.util.ArrayList;
import java.util.List;


/**
 * engine for the tag suggestion by overall popularity.
 */
public class SimplePopularEngine {
    //RETURN TOP N RELATIONSHIPS(PAIR TAG)

    public static List<Tag> suggest(Photo p){
        List<Tag> tagsInPhoto = p.getChildTags();

        List<List<Relationship>> listRelationshipsForTags = new ArrayList<>();

        for(Tag t: tagsInPhoto){
            List<Relationship> relationshipsForTag = t.getRelationshipsForTagSortedByOccurrences();
            listRelationshipsForTags.add(relationshipsForTag);
        }
        List<Relationship> sortedRelationships = RelationshipMerger.mergeNLists(listRelationshipsForTags);

        List<Tag> toReturn = new ArrayList<>();
        for(Relationship r:sortedRelationships) {
            System.out.println("In for: "+r);
            if(tagsInPhoto.contains(r.getKid1()) && !tagsInPhoto.contains(r.getKid2()) && !toReturn.contains(r.getKid2())){
                toReturn.add(r.getKid2());
                System.out.println("ADDED: "+r.getKid2());
            }
           else
                if(tagsInPhoto.contains(r.getKid2()) && !tagsInPhoto.contains(r.getKid1()) && !toReturn.contains(r.getKid1())){
                    toReturn.add(r.getKid1());
                    System.out.println("ADDED: "+r.getKid1());
                }
        }

        return toReturn;
    }


}
