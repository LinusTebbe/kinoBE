package Resources;

import Database.DatabaseService;
import Database.Repository.PresentationRepository;
import Database.Repository.ReservationRepository;
import Database.Repository.SeatRepository;
import Exceptions.EntityNotFoundException;
import Models.Presentation;
import Models.Reservation;
import Models.Seat;
import Requests.ReservationRequest;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import java.util.List;

public class ReservationResource extends ServerResource {

    @Get("json")
    public List<Reservation> doGet() {
        ReservationRepository reservationRepository = new ReservationRepository();
        return reservationRepository.findAll();
    }

    @Post("json")
    public List<Integer> doPost(ReservationRequest request) throws EntityNotFoundException {

        SeatRepository seatRepository = new SeatRepository();
        Seat seat = seatRepository.getById(request.getSeatIds().stream().findFirst().get());
        Presentation presentation = (new PresentationRepository()).getById(request.getPresentationId());

        Reservation reservation = new Reservation(presentation, seat);

        DatabaseService databaseService = DatabaseService.getInstance();
        databaseService.persist(reservation);
        databaseService.flush();

        return request.getSeatIds();
    }
}
