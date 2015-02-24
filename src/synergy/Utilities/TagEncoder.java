package synergy.Utilities;

import synergy.models.Tag;

/**
 * Created by alexstoick on 2/23/15.
 */
public class TagEncoder {

	private final static String TAG_SEPARATOR = "|";
	private final static String VALUE_SEPARATOR = ":";

	public static String encodeTagArray(Tag[] tags) {
		String result = "";
		for(Tag tag:tags) {
			result += encodeTag (tag) + TAG_SEPARATOR;
		}
		return result;
	}

	private static String encodeTag(Tag tag){
		return tag.getType () + VALUE_SEPARATOR + tag.getValue ();
	}
}
