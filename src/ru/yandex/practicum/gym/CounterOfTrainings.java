package ru.yandex.practicum.gym;

public class CounterOfTrainings {
    private Coach coach;

    private int numberOfTrainings;

    public CounterOfTrainings(Coach coach, int numberOfTrainings) {
        this.coach = coach;
        this.numberOfTrainings = numberOfTrainings;
    }

    public Coach getCoach() {
        return coach;
    }

    public int getNumberOfTrainings() {
        return numberOfTrainings;
    }
}
