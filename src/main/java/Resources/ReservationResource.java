package Resources;

import Database.DatabaseService;
import Database.Repository.PresentationRepository;
import Database.Repository.ReservationRepository;
import Database.Repository.SeatRepository;
import Exceptions.EntityNotFoundException;
import Models.Booking;
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
        DatabaseService databaseService = DatabaseService.getInstance();

        SeatRepository seatRepository = new SeatRepository();
        Presentation presentation = (new PresentationRepository()).getById(request.getPresentationId());

        Booking booking = new Booking();
        databaseService.persist(booking);

        for (int seatId: request.getSeatIds()) {
            Seat seat = seatRepository.getById(seatId);
            Reservation reservation = new Reservation(presentation, seat);
            seat.getReservations().add(reservation);
            booking.getReservations().add(reservation);
            databaseService.persist(reservation);
        }
        databaseService.flush();

        return request.getSeatIds();
    }
}
