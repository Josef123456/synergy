package synergy.models;

/**
 * Created by alexstoick on 2/6/15.
 */
public class Tag {
	private int ID;
	private TagType type;

	public enum TagType {
		    KID, PLACE, EXTRA
	}
}
