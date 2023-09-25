package cinema.Services;

import cinema.ErrorMsgs;
import cinema.Models.Cinema;
import cinema.Models.Seat;
import cinema.Models.Statistic;
import cinema.Models.Ticket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class BookingService {
    ObjectMapper objectMapper = new ObjectMapper();
    private final Cinema cinema;

    @Autowired
    public BookingService(Cinema cinema) {
        this.cinema = cinema;
    }

    public Cinema getAvailableSeatsInfo() {
        return cinema;
    }

    public ResponseEntity<String> purchaseTicket(Seat seat) {
        Optional<Seat> seatOpt = Arrays.stream(cinema.getSeats())
                .filter(s -> s.getRow() == seat.getRow() && s.getColumn() == seat.getColumn())
                .findFirst();

        if (seatOpt.isEmpty()) {
            return new ResponseEntity(Map.of("error", ErrorMsgs.OUT_OF_BOUNDS.toString()), HttpStatus.BAD_REQUEST);
        }

        if (!seatOpt.get().isAvailable()) {
            return new ResponseEntity(Map.of("error", ErrorMsgs.NOT_AVAILABLE_TICKET.toString()), HttpStatus.BAD_REQUEST);
        }

        Ticket ticket = new Ticket(seatOpt.get());
        changeSeatAvailability(seat, false);
        cinema.addPurchasedTicket(ticket);

        try {
            return new ResponseEntity(objectMapper.writeValueAsString(ticket), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity(Map.of("error", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<String> returnTicket(Ticket ticket) {
        Optional<Ticket> ticketOption = cinema.getPurchasedTicket().stream()
                .filter(t -> t.equals(ticket))
                .findFirst();

        if (ticketOption.isEmpty()) {
            return new ResponseEntity(Map.of("error", ErrorMsgs.WRONG_TOKEN.toString()), HttpStatus.BAD_REQUEST);
        }

        Seat returnedSeat = ticketOption.get().getSeat();
        changeSeatAvailability(returnedSeat, true);
        cinema.removePurchasedTicket(ticketOption.get());

        return new ResponseEntity(Map.of("returned_ticket", returnedSeat), HttpStatus.OK);
    }

    public ResponseEntity<Statistic> getStatistics(String password) {
        if (password.equals("super_secret")) {
            int totalIncome = Arrays.stream(cinema.getSeats())
                    .filter(Predicate.not(Seat::isAvailable))
                    .mapToInt(Seat::getPrice)
                    .sum();

            long availableSeats = Arrays.stream(cinema.getSeats())
                    .filter(Seat::isAvailable)
                    .count();

            long purchasedTickets = cinema.getPurchasedTicket().size();

            Statistic statistic = new Statistic(totalIncome, availableSeats, purchasedTickets);

            return new ResponseEntity<>(statistic, HttpStatus.OK);
        }

        return new ResponseEntity(Map.of("error", ErrorMsgs.WRONG_PASSWORD.toString()), HttpStatus.UNAUTHORIZED);

    }


    private void changeSeatAvailability(Seat seat, boolean availability) {
        Arrays.stream(cinema.getSeats())
                .filter(s -> s.equals(seat))
                .forEach(s -> s.setFree(availability));
    }


}
