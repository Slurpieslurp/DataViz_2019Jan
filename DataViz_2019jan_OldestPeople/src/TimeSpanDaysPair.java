public class TimeSpanDaysPair {
    private int days;
    private TimeSpan timeStamp;

    TimeSpanDaysPair(TimeSpan timeStamp, int days) {
        this.days = days;
        this.timeStamp = timeStamp;
    }

    public int getDays() {
        return days;
    }

    public TimeSpan getTimeStamp() {
        return timeStamp;
    }
}
