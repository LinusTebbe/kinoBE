package Database.Repository;

import Models.Seat;
import Models.SeatRow;
import Util.Entity;

import java.sql.ResultSet;
import java.util.List;

public class SeatRowRepository extends AbstractRepository<SeatRow> {

    private static final String WHERE_SELECT_TEMPLATE = "SELECT * FROM %s WHERE %s = ? ORDER BY number ASC";

    public SeatRowRepository() {
        super(SeatRow.class);
    }

    protected ResultSet getWhereStatement(String field, Object value) {
        return this.databaseService.runPreparedQuery(
                String.format(
                        SeatRowRepository.WHERE_SELECT_TEMPLATE,
                        Seat.class.getAnnotation(Entity.class).name(),
                        field
                ),
                List.of(value)
        );
    }
}
