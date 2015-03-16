package synergy.engines.suggestion;

import synergy.models.Photo;
import synergy.models.Relationship;
import synergy.models.Tag;
import synergy.utilities.RelationshipMerger;

import java.util.ArrayList;
import java.util.List;


/**
 * engine for the tag suggestion by overall popularity.
 * RETURN TOP N RELATIONSHIPS(PAIR TAG)
 */
public class SimplePopularEngine {


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
            if(tagsInPhoto.contains(r.getKid1()) && !tagsInPhoto.contains(r.getKid2())
                    && !toReturn.contains(r.getKid2 ()) ) {
	            toReturn.add(r.getKid2());
            }
            else {
	            if ( tagsInPhoto.contains (r.getKid2 ()) && !tagsInPhoto.contains (r.getKid1 ())
                        && !toReturn.contains (r.getKid1 ())) {
		            toReturn.add (r.getKid1 ());
	            }
            }
        }
        System.out.println("SimplePopularEngine SUGGESTING: "+toReturn);
        System.out.println("SimplePopularEngine SORTED RELATIONSHIPS: "+sortedRelationships);
        return toReturn;
    }
}
