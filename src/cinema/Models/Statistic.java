package cinema.Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Statistic {
    @JsonProperty("current_income")
    int income;
    @JsonProperty("number_of_available_seats")
    long numberOfAvailableSeats;
    @JsonProperty("number_of_purchased_tickets")
    long numberOfPurchasedTickets;

    Statistic() {}

    public Statistic(int income, long numberOfAvailableSeats, long numberOfPurchasedTickets) {
        this.income = income;
        this.numberOfAvailableSeats = numberOfAvailableSeats;
        this.numberOfPurchasedTickets = numberOfPurchasedTickets;
    }

    public int getIncome() {
        return income;
    }

    public long getNumberOfAvailableSeats() {
        return numberOfAvailableSeats;
    }

    public long getNumberOfPurchasedTickets() {
        return numberOfPurchasedTickets;
    }
}
