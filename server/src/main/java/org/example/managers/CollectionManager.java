package org.example.managers;

import org.example.entity.Organization;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Класс для управления коллекцией {@code Organization}, включая добавление, удаление,
 * обновление, сортировку элементов, а также для работы с их идентификаторами и сохранения коллекции.
 */
public class CollectionManager {
    /**
     * Коллекция элементов типа {@code Organization}.
     */
    private static Stack<Organization> collection = new Stack<Organization>();
    /**
     * Хеш-карта для быстрого доступа к элементам коллекции по их идентификаторам.
     */
    private static Map<Integer, Organization> organizationMap = new HashMap<Integer, Organization>();
    private Date initTime;
    private Organization minElement = null;


    private DataBaseManager dataBaseManager = new DataBaseManager();
    private static final ReentrantLock lock = new ReentrantLock();



    /**
     * Конструктор для создания объекта {@code CollectionManager}.
     *
     *
     */
    public CollectionManager() throws SQLException {
        this.initTime = new Date();
    }

    /**
     * Статический метод для генерации нового id
     * @return минимальный несуществующий id
     */
    public static int generateFreeId() {
        lock.lock();
        try {
            if (collection.isEmpty()) return 1;
            Set<Integer> keys = new HashSet<>(organizationMap.keySet()); // Копируем ключи, чтобы избежать ConcurrentModificationException
            if (keys.isEmpty()) return 1;
            int maxId = Collections.max(keys); // Находим максимальный ID
            for (int i = 1; i < maxId; i++) {
                if (!organizationMap.containsKey(i)) return i;
            }
            return Collections.max(organizationMap.keySet()) + 1;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Очищает коллекцию {@code collection} и карту {@code organizationMap}.
     */
    public void clearCollection() {
        lock.lock();
        try {
            collection.clear();
            organizationMap.clear();
        } finally {
            lock.unlock();
        }
    }

    /**
     * Проверяет, все ли идентификаторы уникальны в переданной коллекции.
     *
     * @param collection Коллекция для проверки.
     * @return {@code true}, если все идентификаторы уникальны, иначе {@code false}.
     */
    public static boolean allIdAreUnique(Collection<Organization> collection) {
        HashSet<Integer> ids = new HashSet<>();
        for (Organization org: collection) {
            if (ids.contains(org.getId())) return false;
            ids.add(org.getId());
        }
        return true;
    }

    /**
     * Проверяет, существует ли элемент с указанным ID в коллекции.
     *
     * @param id ID элемента.
     * @return {@code true}, если элемент существует, иначе {@code false}.
     */
    public static boolean hasIdInCollection(Integer id) {
        return organizationMap.containsKey(id);
    }

    /**
     * Получает минимальный элемент коллекции.
     *
     * @return Минимальный элемент коллекции.
     */
    public Organization getMinElement() {
        lock.lock();
        try {
            return minElement;
        } finally {
            lock.unlock();
        }
    }


    public void updateMinElement() {
        lock.lock();
        try {
            minElement = collection.stream()
                    .min(Organization::compareTo)
                    .orElse(null);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Получение типа коллекции
     * @return класс объекта коллекции
     */
    public String getTypeOfCollection() {
        return collection.getClass().getName();
    }

    /**
     * Возвращает размер коллекции.
     *
     * @return Размер коллекции.
     */
    public int getCollectionSize() {
        lock.lock();
        try {
            return collection.size();
        } finally {
            lock.unlock();
        }
    }


    /**
     * @return  время инициализации.
     */
    public Date getInitTime() {
        return initTime;
    }

    /**
     * @return коллекция.
     */
    static public Stack<Organization> getCollection() {
        return collection;
    }
    static public Map<Integer, Organization> getOrganizationMap() { return organizationMap; }

    /**
     * @param id ID organization
     * @return Элемент коллекции по ID, если существует, иначе null.
     */
    public Organization byId(Integer id) {
        lock.lock();
        try {
            return organizationMap.get(id);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Проверка, содержит ли коллекция дракона
     *
     * @param organization элемент organization для проверки принадлежности к
     *                     коллекции
     * @return true, если коллекция содержит organization, иначе false.
     */
    public boolean isContain(Organization organization) {
        lock.lock();
        try {
            return organizationMap.containsKey(organization.getId());
        } finally {
            lock.unlock();
        }
    }

    /**
     * Сортировка коллекции collection элементов Organization по умолчанию
     */
    public void updateSort() {
        lock.lock();
        try {
            Collections.sort(collection);
            updateMinElement();
        } finally {
            lock.unlock();
        }
    }


    /**
     * Удаляет первый ��лемент коллекции
     * Если коллекция пуста, ничего не делает
     */
    public void removeFirstElementOfCollection() {
        lock.lock();
        try {
            if (collection.isEmpty()) return;
            collection.removeElementAt(0);
        } finally {
            lock.unlock();
        }
    }

    /**
     * Возвращает ID первого элемента коллекции
     * Если коллекция пуста, возвращает null
     *
     * @return ID первого элемента коллекции
     */
    public Integer getFirstElementId() {
        lock.lock();
        try {
            if (collection.isEmpty()) return null;
            return collection.get(0).getId();
        } finally {
            lock.unlock();
        }
    }
    /**
     * Добавляет элемент organization в коллекцию collection
     * Добавляет элемент в HashMap для быстрого поиска
     * Сортирует коллекцию по умолчанию
     * 
     * @param organization добавляемый элемент
     * 
     * @return true, если произошло добавление, иначе false
     */
    public boolean add(Organization organization) {
        lock.lock();
        try {
            if (isContain(organization))
                return false;
            updateMinElement();
            organizationMap.put(organization.getId(), organization);
            collection.push(organization);
            updateSort();
            return true;
        } finally {
            lock.unlock();
        }
    }

    public void setCollection(Stack<Organization> collection) {
        lock.lock();
        try {
            CollectionManager.collection = collection;
            Map<Integer, Organization> newMap = new HashMap<>();
            for (Organization organization : collection) {
                newMap.put(organization.getId(), organization);
            }
            CollectionManager.organizationMap = newMap;
            updateSort();
            updateMinElement();
        } finally {
            lock.unlock();
        }
    }

    public void loadCollection() {
        lock.lock();
        try {
            setCollection(dataBaseManager.createCollection());
        } finally {
            lock.unlock();
        }
    }





    /**
     * По ID organization удаляет элемент из коллекции
     * 
     * @param id ID удаляемого organization
     * @return true, если произошло удаление, иначе false
     */
    public boolean removeById(Integer id) {
        lock.lock();
        try {
            Organization organization = byId(id);
            if (organization == null)
                return false;
            organizationMap.remove(id);
            collection.remove(organization);
            updateMinElement();
            updateSort();
            return true;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Обновляет элемент в коллекции по ID
     *
     * @param id ID обновляемого э��емента
     * @param organization новый элемент
     */
    public void updateById(Integer id, Organization organization) {
        lock.lock();
        try {
            if (organizationMap.containsKey(id)) {
                organizationMap.put(id, organization);
                collection.remove(organization);
                collection.push(organization);
                updateSort();
                updateMinElement();
            }
        } finally {
            lock.unlock();
        }
    }




    /**
     * Сохраняет коллекицию в CSV-файл
     *
     */
//    public void saveCollection() {
//        dumpManager.writeCollection(collection);
//
//    }

    /**
     * Возвращает отсортированную по имени копию коллекции
     */
    static public List<Organization> getSortedByNameCollection() {
        lock.lock();
        try {
            return collection.stream()
                    .sorted(Comparator.comparing(Organization::getName))
                    .collect(Collectors.toList());
        } finally {
            lock.unlock();
        }
    }

    public void setMinElement(Organization minElement) {
        lock.lock();
        try {
            this.minElement = minElement;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        lock.lock();
        try {
            if (collection.isEmpty())
                return "Коллекция пуста!";
            StringBuilder info = new StringBuilder();

            for (Organization organization : collection) {
                info.append(organization + "\n");
            }

            return info.toString().trim();
        } finally {
            lock.unlock();
        }
    }

}

