package lab8.server.managers;

import lab8.common.dto.Response;
import lab8.common.models.HumanBeing;

import lab8.common.utils.DateUtils;
import lab8.server.database.HumanBeingRepository;
import lab8.server.utils.ServerLogger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * Класс оперирующий коллекцией
 */

public class CollectionManager {

    private final Set<HumanBeing> collection = ConcurrentHashMap.newKeySet();
    HumanBeingRepository repository = new HumanBeingRepository();
    private String initTime;

    public CollectionManager() {
        setInitTime();
    }


    public Set<HumanBeing> getCollection() {
        return collection;
    }

    public Response getAll() {
        collection.clear();
        collection.addAll(repository.selectAll());
        List<HumanBeing> result = collection.stream().sorted(Comparator
                        .comparing((HumanBeing h) -> h.getCoordinates().getX())
                        .thenComparing(h -> h.getCoordinates().getY()))
                .collect(Collectors.toList());

        String message = result.isEmpty() ? "Коллекция пуста! " : "Элементы коллекции: ";

        return new Response(result, message);
    }

    public Response removeById(long id, long userId) {

        if (repository.deleteById(id, userId) > 0) {
            List<HumanBeing> removed = collection.stream()
                    .filter(h -> h.getId() == id)
                    .collect(Collectors.toList());
            collection.removeIf(h -> h.getId() == id);

            ServerLogger.logger.info("Удален объект с id: " + id);
            return new Response(removed, "Объект удален: ");
        }
        return new Response(Collections.emptyList(), "Объект не найден!");
    }

    public Response clear(long userId) {
        if (repository.deleteAll(userId)) {
            collection.removeIf(h -> h.getUserId() == userId);
        }
        ServerLogger.logger.info("Коллекция очищена");
        return new Response(Collections.emptyList(), "Коллекция очищена");
    }


    public Response updateById(long id, HumanBeing newHuman, long userId) {
        HumanBeing oldHuman = findById(id);

        if (oldHuman == null) return new Response(Collections.emptyList(), "Элемент с таким id не найден");
        if (oldHuman.getUserId() != userId)
            return new Response(Collections.emptyList(), "У пользователя нет доступа к этому объекту");
        newHuman.setId(oldHuman.getId());
        newHuman.setCreationDate(oldHuman.getCreationDate());

        if (repository.update(id, newHuman, userId)) {
            collection.removeIf(h -> h.getId() == id);
            collection.add(newHuman);
        }
        ServerLogger.logger.info("Объект с id: " + id + " добавлен");
        return new Response(Collections.singletonList(newHuman), "Объект успешно обновлен");
    }

    public Response add(HumanBeing human, long userId) {

        human.setUserId(userId);

        long id = repository.insert(human, userId);

        if (id != -1) {
            human.setId(id);
            collection.add(human);
        }

        ServerLogger.logger.info("В коллекцию добален объект \n" + human);
        return new Response(Collections.singletonList(human), "Объект успешно добавлен");
    }

    public HumanBeing findById(long id) {
        return collection.stream()
                .filter(h -> h.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Response removeGreater(double impactSpeed, long userId) {

        List<HumanBeing> removed = collection.stream()
                .filter(h -> h.getImpactSpeed() > impactSpeed)
                .filter(h -> h.getUserId() == userId)
                .collect(Collectors.toList());

        int count = 0;

        for(HumanBeing human : removed){
            if(repository.deleteById(human.getId(), userId) > 0){
                collection.remove(human);
                count++;
            }
        }

        String message = count > 0 ? "Удалено объектов: " + count : "Элементы с ImpactSpeed больше " + impactSpeed + " не найдены";

        ServerLogger.logger.info("Удалены объекты: \n" + removed);
        return new Response(removed, message);
    }

    public Response removeLower(double impactSpeed, long userId) {
        List<HumanBeing> removed = collection.stream()
                .filter(h -> h.getImpactSpeed() < impactSpeed)
                .filter(h -> h.getUserId() == userId)
                .collect(Collectors.toList());

        int count = 0;

        for(HumanBeing human : removed){
            if(repository.deleteById(human.getId(), userId) > 0){
                collection.remove(human);
                count++;
            }
        }

        String message = count > 0 ? "Удалено объектов: " + count : "Элементы с ImpactSpeed меньше " + impactSpeed + " не найдены";

        ServerLogger.logger.info("Удалены объекты: \n" + removed);
        return new Response(removed, message);
    }

    public Map<Boolean, Long> countByRealHero() {
        return collection.stream()
                .collect(Collectors.groupingBy(
                        HumanBeing::getRealHero,
                        Collectors.counting()));
    }

    public Response filterGreaterThanSoundtrack(String soundtrack) {
        List<HumanBeing> filtered = collection.stream()
                .filter(h -> h.getSoundtrackName() != null &&
                        h.getSoundtrackName().compareToIgnoreCase(soundtrack) > 0)
                .collect(Collectors.toList());

        String message = filtered.isEmpty() ?
                "Элементы с заданным soundtrackName не найдены" :
                "Найденные элементы: ";

        return new Response(filtered, message);
    }

    public Response countByImpactSpeed(double impactSpeed) {
        long count = collection.stream()
                .filter(h -> Double.compare(h.getImpactSpeed(), impactSpeed) == 0)
                .count();

        String message = count != 0 ?
                "Количество человек с ImpactSpeed: " + impactSpeed + " равно " + count :
                "Таких элементов не найдено";

        return new Response(Collections.emptyList(), message);
    }

    public Response addIfMin(HumanBeing human, long userId) {

        HumanBeing min = collection.stream()
                .min(Comparator.comparingDouble(HumanBeing::getImpactSpeed))
                .orElse(null);

        if (min == null || human.getImpactSpeed() < min.getImpactSpeed()) {
            add(human, userId);

            ServerLogger.logger.info("В коллекцию добален объект \n" + human);
            return new Response(Collections.singletonList(human), "Успешно добавлено");
        }
        return new Response(Collections.emptyList(), "Не добавлено");
    }

    public void loadCollection() {
        collection.clear();
        collection.addAll(repository.selectAll());
    }

    public String getCollectionType() {
        return collection.getClass().getName();
    }

    public int getSize() {
        return collection.size();
    }

    public void setInitTime() {
        this.initTime = DateUtils.getDate();
    }

    public String getInitTime() {
        return initTime;
    }
}
