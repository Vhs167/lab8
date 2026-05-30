package lab8.client.gui;

import java.util.List;
import lab8.common.models.HumanBeing;
import prog.lab8.gui.api.FieldDescriptor;
import prog.lab8.gui.api.FieldType;
import prog.lab8.gui.api.ObjectDescriptor;

public final class HumanBeingDescriptor implements ObjectDescriptor<HumanBeing> {
    private static final double MIN_RADIUS = 10.0;
    private static final double MAX_RADIUS = 70.0;
    private static final double IMPACT_SCALE = 4.0;

    private final HumanBeingGateway gateway;

    public HumanBeingDescriptor(HumanBeingGateway gateway) {
        this.gateway = gateway;
    }

    @Override
    public long id(HumanBeing object) {
        return object.getId();
    }

    @Override
    public String owner(HumanBeing object) {
        return gateway.ownerName(object.getUserId());
    }

    @Override
    public double x(HumanBeing object) {
        return object.getCoordinates().getX();
    }

    @Override
    public double y(HumanBeing object) {
        return object.getCoordinates().getY();
    }

    @Override
    public double radius(HumanBeing object) {
        double radius = MIN_RADIUS + Math.abs(object.getImpactSpeed()) / IMPACT_SCALE;
        return Math.max(MIN_RADIUS, Math.min(MAX_RADIUS, radius));
    }

    @Override
    public List<FieldDescriptor<HumanBeing>> fields() {
        return List.of(
                field("id", "field.id", FieldType.INTEGER, HumanBeing::getId),
                field("name", "field.name", FieldType.TEXT, HumanBeing::getName),
                field("x", "field.x", FieldType.INTEGER, human -> human.getCoordinates().getX()),
                field("y", "field.y", FieldType.DECIMAL, human -> human.getCoordinates().getY()),
                field("creationDate", "field.creationDate", FieldType.DATE_TIME, HumanBeing::getCreationDate),
                field("realHero", "field.realHero", FieldType.BOOLEAN, HumanBeing::getRealHero),
                field("hasToothpick", "field.hasToothpick", FieldType.BOOLEAN, HumanBeing::getHasToothpick),
                field("impactSpeed", "field.impactSpeed", FieldType.DECIMAL, HumanBeing::getImpactSpeed),
                field("soundtrackName", "field.soundtrackName", FieldType.TEXT, HumanBeing::getSoundtrackName),
                field("minutesOfWaiting", "field.minutesOfWaiting", FieldType.DECIMAL, HumanBeing::getMinutesOfWaiting),
                field("mood", "field.mood", FieldType.ENUM, HumanBeing::getMood),
                field("carCool", "field.carCool", FieldType.BOOLEAN, human -> human.getCar().getCool()),
                field("owner", "field.owner", FieldType.TEXT, human -> gateway.ownerName(human.getUserId())));
    }

    private static FieldDescriptor<HumanBeing> field(
            String key,
            String labelKey,
            FieldType type,
            java.util.function.Function<HumanBeing, ?> extractor) {
        return FieldDescriptor.<HumanBeing>builder(key, labelKey, type).extractor(extractor).build();
    }
}
