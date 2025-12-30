package payments.service;

public record WithdrawResult(boolean success, String reason) {
    public static WithdrawResult ok() {
        return new WithdrawResult(true, null);
    }

    public static WithdrawResult fail(String reason) {
        return new WithdrawResult(false, reason);
    }
}
