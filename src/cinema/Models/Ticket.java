package cinema.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.UUID;

public class Ticket {
    UUID token = UUID.randomUUID();
    @JsonProperty("ticket")
    Seat seat;

    public Ticket() {}
    public Ticket(Seat seat) {
        this.seat = seat;
    }
    public Seat getSeat() {
        return seat;
    }

    public UUID getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(token, ticket.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "token=" + token +
                '}';
    }
}
