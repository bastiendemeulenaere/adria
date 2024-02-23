package be.howest.ti.adria.logic.data;

public abstract class Repository {
    protected final H2Connection h2Connection;
    protected Repository(H2Connection h2Connection) {
        this.h2Connection = h2Connection;
    }
}
