public class TimeSpan {
    private int years;
    private int days;

    TimeSpan(int years, int days) {
        this.years = years;
        this.days  = days;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getTotalDays() {
        return years * 365 + days;
    }

    public void incrementYears() {
        this.years++;
    }

    public void incrementDays() {
        this.days++;
    }

    public void resetDays() {
        this.days = 0;
    }
}
