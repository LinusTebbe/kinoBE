package Database;

import Models.AbstractEntity;

import java.util.List;
import java.util.Map;

public class IdentityMap<T extends AbstractEntity> {
    private Map<Object, T> entities;

    public static Map<String, IdentityMap<? extends AbstractEntity>> INSTANCES;

    public static <T extends AbstractEntity> IdentityMap<T> getInstance(Class<T> clazz) {
        //noinspection unchecked
        IdentityMap<T> map = (IdentityMap<T>) IdentityMap.INSTANCES.get(clazz.getName());

        if (map != null) {
            map = new IdentityMap<>();
        }

        return map;
    }
}
