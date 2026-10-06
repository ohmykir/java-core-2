package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        //Проверить, что за вторник не вернулось занятий
        assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        List<TrainingSession> thursdayTimetable = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        assertEquals(2, thursdayTimetable.size());
        assertEquals(13, thursdayTimetable.getFirst().getTimeOfDay().getHours());
        assertEquals(20, thursdayTimetable.getLast().getTimeOfDay().getHours());
        //Проверить, что за вторник не вернулось занятий
        assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY).size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        assertEquals(1, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        assertEquals(0, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)).size());
    }

    @Test
    void testGetTrainingSessionsForDayAndTimeMultiplySessions() {
        Timetable timetable = new Timetable();

        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Акробатика для взрослых",  Age.ADULT, 90);
        Coach coach1 = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");
        TrainingSession firstTrainingSession = new TrainingSession(group1, coach1,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession secondTrainingSession = new TrainingSession(group2, coach2,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(firstTrainingSession);
        timetable.addNewTrainingSession(secondTrainingSession);

        assertEquals(2, timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size());
    }

    @Test
    void testEmptyTimetable() {
        Timetable timetable = new Timetable();

        assertEquals(0, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size());
        assertEquals(0,
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)).size());
    }

    @Test
    void testDaySessionsAreSortedByTimeKeys() {
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Плавание для детей", Age.CHILD, 60);

        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(20, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(9,  0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertEquals(3, sessions.size());
        assertEquals(9,  sessions.get(0).getTimeOfDay().getHours());
        assertEquals(13, sessions.get(1).getTimeOfDay().getHours());
        assertEquals(20, sessions.get(2).getTimeOfDay().getHours());
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        List<CounterOfTrainings> countByCoaches = timetable.getCountByCoaches();

        assertTrue(countByCoaches.isEmpty());
    }

    @Test
    void testGetCountByCoachesSingleCoach() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Иванов", "Иван", "Иванович");
        Group group = new Group("Плавание", Age.CHILD, 60);

        TrainingSession trainingSession = new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(trainingSession);

        List<CounterOfTrainings> countByCoaches = timetable.getCountByCoaches();

        assertEquals(1, countByCoaches.size());
        assertEquals(1, countByCoaches.getFirst().getNumberOfTrainings());
    }

    @Test
    void testGetCountByCoachesMultipleCoaches() {
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Иванов", "Иван", "Иванович");
        Coach coach2 = new Coach("Петров", "Петр", "Петрович");
        Group group = new Group("Плавание для детей", Age.CHILD, 60);

        TrainingSession trainingSession1 = new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSession2 = new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSession3 = new TrainingSession(group, coach1, DayOfWeek.FRIDAY, new TimeOfDay(13, 0));
        TrainingSession trainingSession4 = new TrainingSession(group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(15, 0));
        TrainingSession trainingSession5 = new TrainingSession(group, coach2, DayOfWeek.THURSDAY, new TimeOfDay(15, 0));

        timetable.addNewTrainingSession(trainingSession1);
        timetable.addNewTrainingSession(trainingSession2);
        timetable.addNewTrainingSession(trainingSession3);
        timetable.addNewTrainingSession(trainingSession4);
        timetable.addNewTrainingSession(trainingSession5);

        List<CounterOfTrainings> countByCoaches = timetable.getCountByCoaches();

        assertEquals(2, countByCoaches.size());
        assertEquals(3, countByCoaches.getFirst().getNumberOfTrainings());
        assertEquals(2, countByCoaches.getLast().getNumberOfTrainings());
    }
}
