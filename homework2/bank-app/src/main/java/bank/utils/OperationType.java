package bank.utils;

public enum OperationType {
    GAIN, LOSS;

    @Override
    public String toString() {
        if (this == GAIN) return "Gain";
        if (this == LOSS) return "Loss";
        return "Critical error";
    }
}
