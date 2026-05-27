package lab7.common.dto;

import lab7.common.models.Car;
import lab7.common.models.Coordinates;
import lab7.common.models.Mood;

import java.io.Serializable;

public class HumanBeingRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    public String name;
    public Coordinates coordinates;
    public Boolean realHero;
    public boolean hasToothpick;
    public double impactSpeed;
    public String soundtrackName;
    public double minutesOfWaiting;
    public Mood mood;
    public Car car;
    public long userId;

}
