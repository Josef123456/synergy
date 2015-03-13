package synergy.engines.suggestion;

import synergy.models.Photo;
import synergy.models.Relationship;
import synergy.models.Tag;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<Relationship> sortedRelationships = mergeNLists(listRelationshipsForTags);
        //TODO implement the rest!
        return null;
    }

    public static List<Relationship> mergeRelationshipLists(List<Relationship> list1, List<Relationship> list2){
        Relationship a[] = (Relationship[])list1.toArray();
        Relationship b[] = (Relationship[])list2.toArray();
        Relationship c[] = new Relationship[a.length+b.length];

        int i = 0, j = 0, k = 0;

        while (i < a.length && j < b.length)
        {
            if (a[i].getOccurrences() > b[j].getOccurrences())
                c[k++] = a[i++];

            else
                c[k++] = b[j++];
        }

        while (i < a.length)
            c[k++] = a[i++];


        while (j < b.length)
            c[k++] = b[j++];

        return new ArrayList<>(Arrays.asList(c));
    }

    public static List<Relationship> mergeNLists(List<List<Relationship>> list){
        for(int i = 1; i<list.size()-1;i++){
            list.set(0,mergeRelationshipLists(list.get(0),list.get(i)));
        }
        return list.get(0);
    }
}
