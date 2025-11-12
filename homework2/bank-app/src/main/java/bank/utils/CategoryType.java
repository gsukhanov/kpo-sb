package bank.utils;

public enum CategoryType {
    GAIN, LOSS;

    @Override
    public String toString() {
        if (this == GAIN) return "Gain";
        if (this == LOSS) return "Loss";
        return "Critical error";
    }
}
