package lab8.common.models;

import lab8.common.exceptions.InvalidFieldException;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Класс человека
 */

public class HumanBeing implements Comparable<HumanBeing>, Serializable {

    private long id;
    private String name;
    private Coordinates coordinates;
    private LocalDateTime creationDate;
    private Boolean realHero;
    private boolean hasToothpick;
    private double impactSpeed;
    private String soundtrackName;
    private double minutesOfWaiting;
    private Mood mood;
    private Car car;
    private long userId;

    public HumanBeing(
            long id,
            String name,
            Coordinates coordinates,
            LocalDateTime creationDate,
            Boolean realHero,
            boolean hasToothpick,
            double impactSpeed,
            String soundtrackName,
            double minutesOfWaiting,
            Mood mood,
            Car car,
            long userId
    ) {
        setId(id);
        setName(name);
        setCoordinates(coordinates);
        setCreationDate(creationDate);
        setRealHero(realHero);
        setHasToothpick(hasToothpick);
        setImpactSpeed(impactSpeed);
        setSoundtrackName(soundtrackName);
        setMinutesOfWaiting(minutesOfWaiting);
        setMood(mood);
        setCar(car);
        setUserId(userId);

    }

    public HumanBeing(
            String name,
            Coordinates coordinates,
            Boolean realHero,
            boolean hasToothpick,
            double impactSpeed,
            String soundtrackName,
            double minutesOfWaiting,
            Mood mood,
            Car car,
            long userId
    ) {
        setName(name);
        setCoordinates(coordinates);
        setRealHero(realHero);
        setHasToothpick(hasToothpick);
        setImpactSpeed(impactSpeed);
        setSoundtrackName(soundtrackName);
        setMinutesOfWaiting(minutesOfWaiting);
        setMood(mood);
        setCar(car);
        setUserId(userId);
    }

    public HumanBeing(){};

    @Override
    public int compareTo(HumanBeing other) {
        return Double.compare(this.impactSpeed, other.impactSpeed);
    }

    public void setId(long id) {
        if (id <= 0) {
            throw new InvalidFieldException("id должен быть больше 0");
        }
        this.id = id;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        if (creationDate == null) {
            throw new InvalidFieldException("creationDate не может быть null");
        }
        this.creationDate = creationDate;
    }

    private void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidFieldException("name не может быть пустым");
        }
        this.name = name;
    }

    private void setCoordinates(Coordinates coordinates) {
        if (coordinates == null) {
            throw new InvalidFieldException("coordinates не может быть null");
        }
        this.coordinates = coordinates;
    }

    private void setRealHero(Boolean realHero) {
        if (realHero == null) {
            throw new InvalidFieldException("realHero не может быть null");
        }
        this.realHero = realHero;
    }

    private void setHasToothpick(boolean hasToothpick) {
        this.hasToothpick = hasToothpick;
    }

    private void setImpactSpeed(double impactSpeed) {
        this.impactSpeed = impactSpeed;
    }

    private void setSoundtrackName(String soundtrackName) {
        if (soundtrackName == null) {
            throw new InvalidFieldException("soundtrackName не может быть null");
        }
        this.soundtrackName = soundtrackName;
    }

    private void setMinutesOfWaiting(double minutesOfWaiting) {
        this.minutesOfWaiting = minutesOfWaiting;
    }

    private void setMood(Mood mood) {
        this.mood = mood;
    }

    private void setCar(Car car) {
        if (car == null) {
            throw new InvalidFieldException("car не может быть null");
        }
        this.car = car;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }


    public long getId() {
        return id;
    }

    public boolean getRealHero() {
        return realHero;
    }

    public double getImpactSpeed() {
        return impactSpeed;
    }

    public String getSoundtrackName() {
        return soundtrackName;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public boolean getHasToothpick() {
        return hasToothpick;
    }

    public double getMinutesOfWaiting() {
        return minutesOfWaiting;
    }

    public Mood getMood() {
        return mood;
    }

    public Car getCar() {
        return car;
    }

    public void setUserId(long userId){
        this.userId = userId;
    }

    public long getUserId() { return userId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HumanBeing)) return false;
        HumanBeing that = (HumanBeing) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("id: ").append(id).append(" {");
        sb.append("\n добавлен ").append(creationDate);
        sb.append("\n имя: ").append(name);
        sb.append("\n координаты: ").append(coordinates);
        sb.append("\n настоящий герой: ").append(realHero);
        sb.append("\n сила удара: ").append(impactSpeed);
        sb.append("\n есть зубная щетка: ").append(hasToothpick);
        sb.append("\n название песни: ").append(soundtrackName);
        sb.append("\n минуты ожидания: ").append(minutesOfWaiting);
        sb.append("\n настроение: ").append(mood);
        sb.append("\n машина: ").append(car);
        sb.append("\n владелец объекта: ").append(userId).append("\n}");

        return sb.toString();
    }

}