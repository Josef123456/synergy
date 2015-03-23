package synergy.utilities;

import synergy.models.Photo;

import java.util.Date;

/**
 * Class to implement comparators (for date).
 */
public class DateComparator {

    /**
     * Method returns the closest of two photos to a target photo.
     * @param target
     * @param p1
     * @param p2
     * @return Closest {@link synergy.models.Photo} to the date of the target Photo.
     */
    public static Photo getClosestPhoto(Photo target, Photo p1, Photo p2){
        Date targetDate = target.getDate();
        Date p1Date = p1.getDate();
        Date p2Date = p2.getDate();

        if(Math.abs(targetDate.getTime() - p1Date.getTime()) <= Math.abs(targetDate.getTime() - p2Date.getTime())){
            return p1;
        }

        return p2;
    }
}
