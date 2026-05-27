package lab8.server.factory;

import lab8.common.dto.HumanBeingRequest;

import lab8.common.models.HumanBeing;

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
