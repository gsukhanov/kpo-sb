package bank.utils;

public enum Overwrite {
    INSTANCE;

    boolean value;

    private Overwrite() {
        value = false;
    }
    public void set(boolean value) {
        this.value = value;
    }

    public boolean yes() {
        return value;
    }
}

