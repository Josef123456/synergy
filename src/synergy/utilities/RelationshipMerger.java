package synergy.utilities;

import synergy.models.Relationship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that helps merging two or more sorted lists in one sorted list.
 */
public class RelationshipMerger {
    /**
     * Merges two sorted lists of relationships into one sorted list.
     * @param list1 List to be merged.
     * @param list2 List to be merged.
     * @return A list of merged list1 and list2.
     */
    public static List<Relationship> mergeRelationshipLists(List<Relationship> list1, List<Relationship> list2){
        Relationship c[] = new Relationship[list1.size()+ list2.size()];

        int i = 0, j = 0, k = 0;

        while (i < list1.size() && j < list2.size())
        {
            if (list1.get(i).getOccurrences() > list2.get(j).getOccurrences())
                c[k++] = list1.get(i++);

            else
                c[k++] = list2.get(j++);
        }

        while (i < list1.size())
            c[k++] = list1.get(i++);


        while (j < list2.size())
            c[k++] = list2.get(j++);

        return new ArrayList<>(Arrays.asList(c));
    }

    /**
     * This class merges N sorted lists of relationships into one sorted list.
     * @param list Takes a list of sorted lists of relationships to merge.
     * @return Returns one list of sorted relationships.
     */
    public static List<Relationship> mergeNLists(List<List<Relationship>> list){
        for(int i = 1; i<list.size();i++){

            list.set(0, mergeRelationshipLists(list.get(0), list.get(i)));
        }
        return list.get(0);
    }
}
