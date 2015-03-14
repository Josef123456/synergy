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

        List<Tag> toReturn = new ArrayList<>();
        for(Relationship r:sortedRelationships) {
            System.out.println("In for: "+r);
            if(tagsInPhoto.contains(r.getKid1()) && !tagsInPhoto.contains(r.getKid2())){
                toReturn.add(r.getKid2());
                System.out.println("FIRST IF!");
            }
            else
                if(tagsInPhoto.contains(r.getKid2()) && !tagsInPhoto.contains(r.getKid1())){
                    toReturn.add(r.getKid1());
                    System.out.println("SECOND IF!");
                }
        }

        return toReturn;
    }

    public static List<Relationship> mergeRelationshipLists(List<Relationship> list1, List<Relationship> list2){
        List<Relationship> a = list1;
        List<Relationship> b = list2;
        Relationship c[] = new Relationship[a.size()+b.size()];

        int i = 0, j = 0, k = 0;

        while (i < a.size() && j < b.size())
        {
            if (a.get(i).getOccurrences() > b.get(j).getOccurrences())
                c[k++] = a.get(i++);

            else
                c[k++] = b.get(j++);
        }

        while (i < a.size())
            c[k++] = a.get(i++);


        while (j < b.size())
            c[k++] = b.get(j++);

        return new ArrayList<>(Arrays.asList(c));
    }

    public static List<Relationship> mergeNLists(List<List<Relationship>> list){
        System.out.println(list.size());
        for(int i = 1; i<list.size();i++){

            list.set(0, mergeRelationshipLists(list.get(0), list.get(i)));
        }
        System.out.println("MERGED: "+list.get(0));
        return list.get(0);
    }
}
