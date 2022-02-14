package Models;

public class Seat {
    private final int positionX;
    private final int positionY;
    private final SeatType seatType;

    public Seat(int positionX, int positionY, SeatType seatType) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.seatType = seatType;
    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public SeatType getSeatType() {
        return seatType;
    }
}
