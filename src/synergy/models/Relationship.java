package synergy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import synergy.database.RelationshipDao;

@DatabaseTable(tableName = "relationships")
public class Relationship {

    @DatabaseField(generatedId = true, columnName = _ID)
    private int ID = -1;
    @DatabaseField(foreign = true, canBeNull = false, columnName = COLUMN_KID1_ID,uniqueCombo = true)
    private Tag kid1Tag;
    @DatabaseField(foreign = true, canBeNull = false, columnName = COLUMN_KID2_ID,uniqueCombo = true)
    private Tag kid2Tag;
	// Set default value to 0
    @DatabaseField(canBeNull = false, columnName = COLUMN_OCCURRENCES, defaultValue = "0")
    private int occurrences = -1 ;

    public static final String COLUMN_KID1_ID = "kid1";
    public static final String COLUMN_KID2_ID = "kid2";
    public static final String _ID = "ID";
    public static final String COLUMN_OCCURRENCES = "occurrences";

    public Relationship(Tag kid1Tag, Tag kid2Tag) {
	    kid1Tag.save();
	    kid2Tag.save();
        this.kid1Tag = kid1Tag;
        this.kid2Tag = kid2Tag;
        save();
	    // Throw exception is tag is not of type kid
    }

    public Relationship() {}

	public void increaseOccurrences() {
		System.out.println(getID ());
		save ();
		++ occurrences ;
		save ();
	}

	public void setOccurrences (int occurrences) {
		this.occurrences = occurrences;
	}

	public void save() {
        try {
            RelationshipDao.getInstance().createOrUpdate(this);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public int getID() {
        return ID;
    }

    public Tag getKid1() { return kid1Tag; }

    public Tag getKid2() { return kid2Tag; }

    public int getOccurrences() {
	    return occurrences;
    }

    public Tag getPartner(Tag kid){
        if(kid.equals(kid1Tag))
            return kid2Tag;
        return kid1Tag;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void addPair(Tag kid1Tag, Tag kid2Tag){
        this.kid1Tag = kid1Tag;
        this.kid2Tag = kid2Tag;
    }

    @Override
    public String toString() {
        return "\nRelationship{" +
                "ID=" + ID +
                ", kid1='" + kid1Tag.getValue() +
                ", kid2=" + kid2Tag.getValue() +
                ", occurences=" + occurrences +
                "}\n";
    }


}
