package lab7.server.factory;

import lab7.common.dto.HumanBeingRequest;

import lab7.common.models.HumanBeing;

public class HumanBeingFactory {
    public static HumanBeing create(HumanBeingRequest request) {
        return new HumanBeing(
                request.name,
                request.coordinates,
                request.realHero,
                request.hasToothpick,
                request.impactSpeed,
                request.soundtrackName,
                request.minutesOfWaiting,
                request.mood,
                request.car,
                request.userId
        );
    }
}
