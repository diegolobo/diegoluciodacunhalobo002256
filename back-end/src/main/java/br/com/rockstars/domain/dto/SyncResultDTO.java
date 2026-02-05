package br.com.rockstars.domain.dto;

public class SyncResultDTO {

    private int inserted;
    private int updated;
    private int deactivated;
    private int total;

    public SyncResultDTO() {
    }

    public SyncResultDTO(int inserted, int updated, int deactivated) {
        this.inserted = inserted;
        this.updated = updated;
        this.deactivated = deactivated;
        this.total = inserted + updated + deactivated;
    }

    public int getInserted() {
        return inserted;
    }

    public void setInserted(int inserted) {
        this.inserted = inserted;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(int deactivated) {
        this.deactivated = deactivated;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
