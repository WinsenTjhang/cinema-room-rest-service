package cinema.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class Cinema {
    @JsonProperty("total_rows")
    private final int totalRows = 9;
    @JsonProperty("total_columns")
    private final int totalColumns = 9;
    private Seat[] seats = new Seat[totalColumns * totalColumns];
    private List<Ticket> purchasedTicket = new ArrayList<>();

    public Cinema() {
        if (seats[0] == null) {
            createSeats();
        }
    }

    public int getTotalRows() {
        return totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    @JsonIgnore
    public Seat[] getSeats() {
        return seats;
    }

    public void setSeats(Seat[] seats) {
        this.seats = seats;
    }

    @JsonProperty("available_seats")
    public Seat[] getAvailableSeats() {
        return Arrays.stream(seats).filter(Seat::isAvailable).toArray(Seat[]::new);
    }

    @JsonIgnore
    public List<Ticket> getPurchasedTicket() {
        return purchasedTicket;
    }

    public void addPurchasedTicket(Ticket ticket) {
        purchasedTicket.add(ticket);
    }

    public boolean removePurchasedTicket(Ticket ticket) {
        boolean a = purchasedTicket.remove(ticket);
        System.out.println(a);
        return a;
    }

    private void createSeats() {
        int seatCounter = 0;
        for (int r = 1; r <= totalRows; r++) {
            for (int c = 1; c <= totalColumns; c++) {
                seats[seatCounter] = new Seat(r, c, true);
                seatCounter++;
            }
        }
    }
}