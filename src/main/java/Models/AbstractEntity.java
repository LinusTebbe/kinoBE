package Models;

import Util.Column;
import Util.Id;

public abstract class AbstractEntity {
    @Id
    @Column(name = "id")
    protected int id;

    public int getId() {
        return this.id;
    }
}
