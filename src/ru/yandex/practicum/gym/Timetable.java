package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final HashMap<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();
    private final HashMap<Coach, Integer> coachTrainings =  new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeek = trainingSession.getDayOfWeek();
        TreeMap<TimeOfDay, List<TrainingSession>> timetableForDay = timetable.computeIfAbsent(dayOfWeek, k -> new TreeMap<>());
        List<TrainingSession> timetableForTimeAndDay = timetableForDay.computeIfAbsent(trainingSession.getTimeOfDay(), k -> new ArrayList<>());

        timetableForTimeAndDay.add(trainingSession);
        timetableForDay.put(trainingSession.getTimeOfDay(), timetableForTimeAndDay);
        timetable.put(dayOfWeek, timetableForDay);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.computeIfAbsent(dayOfWeek, k -> new TreeMap<>())
                .values().stream()
                .flatMap(Collection::stream)
                .toList(); //immutable -> нельзя изменить список извне
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.computeIfAbsent(dayOfWeek, k -> new TreeMap<>())
                .computeIfAbsent(timeOfDay, k -> new ArrayList<>());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        timetable.forEach(
                (k, v) -> v.values().stream()
                        .flatMap(Collection::stream)
                        .forEach(e -> coachTrainings.merge(e.getCoach(), 1, Integer::sum)));

        List<CounterOfTrainings> counterOfTrainings = new ArrayList<>();
        coachTrainings.forEach((k, v) -> counterOfTrainings.add(new CounterOfTrainings(k, v)));

        counterOfTrainings.sort((e1, e2) -> e2.getNumberOfTrainings() - e1.getNumberOfTrainings());
        return counterOfTrainings;
    }
}
