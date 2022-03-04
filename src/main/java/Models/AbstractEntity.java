package Models;

import Util.Column;
import Util.FieldTools;
import Util.Hash;
import Util.Id;
import com.fasterxml.jackson.annotation.JsonIgnore;
import flexjson.JSON;

import java.lang.reflect.Field;
import java.util.List;

public abstract class AbstractEntity {
    @Id
    @Column(name = "id")
    protected int id;

    @Hash
    private final int initialHash;

    public AbstractEntity() {
        this.initialHash = 0;
    }

    public int getId() {
        return this.id;
    }

    @JSON(include=false)
    public int getInitialHash() {
        return initialHash;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        for (Field field : FieldTools.getAllFields(this.getClass())) {

            field.setAccessible(true);
            try {
                Object value = field.get(this);
                if (value == null) {
                    continue;
                }
                if (
                        field.getAnnotation(Hash.class) != null ||
                        field.getType().equals(List.class)
                ) {
                    continue;
                }

                if (hashCode == 0) {
                    hashCode = value.hashCode();
                } else {
                    hashCode *= value.hashCode();
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return hashCode;
    }
}
