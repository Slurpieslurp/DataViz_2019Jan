public class Person {
    private String birthPlace;
    private String name;
    private String dateBorn;
    private String dateDied;
    private TimeSpan age;
    private String race;
    private String sex;
    private String deathPlace;
    private String yearsWhenOldest;
    private String agesWhenOldest;
    private TimeSpan lengthOfReign;
    private float reignLength;
    private TimeSpan ageAtAccession;

    Person(String birthPlace, String name, String dateBorn, String dateDied, int age_years, int age_days,
           String race, String sex, String deathPlace, String yearsWhenOldest, String agesWhenOldest,
           int lengthOfReign_years, int lengthOfReign_days, float reignLength, int ageAtAccession_years,
           int ageAtAccession_days) {

        this.birthPlace = birthPlace;
        this.name = name;
        this.dateBorn = dateBorn;
        this.dateDied = dateDied;
        this.age = new TimeSpan(age_years, age_days);
        this.race = race;
        this.sex = sex;
        this.deathPlace = deathPlace;
        this.yearsWhenOldest = yearsWhenOldest;
        this.agesWhenOldest = agesWhenOldest;
        this.lengthOfReign = new TimeSpan(lengthOfReign_years, lengthOfReign_days);
        this.reignLength = reignLength;
        this.ageAtAccession = new TimeSpan(ageAtAccession_years, ageAtAccession_days);
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public String getName() {
        return name;
    }

    public String getDateBorn() {
        return dateBorn;
    }

    public String getDateDied() {
        return dateDied;
    }

    public TimeSpan getAge() {
        return age;
    }

    public String getRace() {
        return race;
    }

    public String getSex() {
        return sex;
    }

    public String getDeathPlace() {
        return deathPlace;
    }

    public String getYearsWhenOldest() {
        return yearsWhenOldest;
    }

    public String getAgesWhenOldest() {
        return agesWhenOldest;
    }

    public TimeSpan getLengthOfReign() {
        return lengthOfReign;
    }

    public float getReignLength() {
        return reignLength;
    }

    public void setReignLength(float reignLength) {
        this.reignLength = reignLength;
    }

    public TimeSpan getAgeAtAccession() {
        return ageAtAccession;
    }

    public void setAgeAtAccession(TimeSpan ageAtAccession) {
        this.ageAtAccession = ageAtAccession;
    }

    public float getAgeAtAccesionInYears() {
        return ageAtAccession.getYears() + (float) ageAtAccession.getDays() / 365;
    }
}
