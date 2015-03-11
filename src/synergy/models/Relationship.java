package synergy.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import synergy.database.RelationshipDao;

@DatabaseTable(tableName = "relationships")
public class Relationship {

    @DatabaseField(generatedId = true, columnName = _ID)
    private int ID = -1;
    @DatabaseField(canBeNull = false, columnName = COLUMN_KID1,uniqueCombo = true)
    private Tag kid1Tag;
    @DatabaseField(canBeNull = false, columnName = COLUMN_KID2,uniqueCombo = true)
    private Tag kid2Tag;
    @DatabaseField(canBeNull = false, columnName = COLUMN_OCCURRENCES)
    private int occurrences;

    public static final String COLUMN_KID1 = "KID1";
    public static final String COLUMN_KID2 = "KID2";
    public static final String _ID = "ID";
    public static final String COLUMN_OCCURRENCES = "Occurrences";

    public Relationship(Tag kid1Tag, Tag kid2Tag) {
        this.kid1Tag = kid1Tag;
        this.kid2Tag = kid2Tag;
        occurrences = 0;
    }

    public Relationship(){

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

    public int getOccurrences() { return occurrences; }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void addPair(Tag kid1Tag, Tag kid2Tag){
        this.kid1Tag = kid1Tag;
        this.kid2Tag = kid2Tag;
    }


}
