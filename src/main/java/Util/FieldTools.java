package Util;

import Models.AbstractEntity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FieldTools {

    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();

        do {
            fields.addAll(List.of(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        } while (clazz != null);

        for (Field field : fields) {
            field.setAccessible(true);
        }

        return fields;
    }

    public static void setInitialHash(AbstractEntity entity) {
        try {
            FieldTools.getAllFields(entity.getClass()).stream()
                    .filter(field -> field.getAnnotation(Hash.class) != null)
                    .findFirst()
                    .orElseThrow()
                    .set(entity, entity.hashCode());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
