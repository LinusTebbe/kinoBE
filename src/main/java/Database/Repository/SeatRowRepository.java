package Database.Repository;

import Models.Seat;
import Models.SeatRow;
import Util.Entity;

import java.sql.ResultSet;
import java.util.List;

public class SeatRowRepository extends AbstractRepository<SeatRow> {

    private static final String WHERE_SELECT_TEMPLATE = "SELECT * FROM %s WHERE %s = ? ORDER BY position_x ASC, position_y ASC";

    public SeatRowRepository() {
        super(SeatRow.class);
    }
}
