package cinema.Controllers;

import cinema.ErrorMsgs;
import cinema.Models.Cinema;
import cinema.Models.Seat;
import cinema.Models.Statistic;
import cinema.Models.Ticket;
import cinema.Services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/seats")
    public Cinema getAvailableSeatsInfo() {
        return bookingService.getAvailableSeatsInfo();
    }

    @GetMapping("/stats")
    public ResponseEntity<Statistic> getStatistics(@RequestParam(required = false) String password) {
        if (password == null) {
            return new ResponseEntity(Map.of("error", ErrorMsgs.WRONG_PASSWORD.toString()), HttpStatus.UNAUTHORIZED);
        }

        return bookingService.getStatistics(password);
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseTicket(@RequestBody Seat seat) {
        return bookingService.purchaseTicket(seat);
    }

    @PostMapping("/return")
    public ResponseEntity<String> returnTicket(@RequestBody Ticket ticket) {
        return bookingService.returnTicket(ticket);
    }
}