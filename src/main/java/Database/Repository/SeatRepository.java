package Database.Repository;

import Models.Seat;
import Util.Entity;

import java.sql.ResultSet;
import java.util.List;

public class SeatRepository extends AbstractRepository<Seat> {

    private static final String WHERE_SELECT_TEMPLATE = "SELECT * FROM %s WHERE %s = ? ORDER BY position_x ASC, position_y ASC";

    public SeatRepository() {
        super(Seat.class);
    }

    protected ResultSet getWhereStatement(String field, Object value) {
        return this.databaseService.runPreparedQuery(
                String.format(
                        SeatRepository.WHERE_SELECT_TEMPLATE,
                        Seat.class.getAnnotation(Entity.class).name(),
                        field
                ),
                List.of(value)
        );
    }
}
