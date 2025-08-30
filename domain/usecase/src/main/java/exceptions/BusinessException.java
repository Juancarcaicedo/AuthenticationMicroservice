package exceptions;

public class BusinessException extends RuntimeException {
    private final BusinessRule rule;

    public BusinessException(BusinessRule rule) {
        super(rule.getMessage());
        this.rule = rule;
    }

    public BusinessRule getRule() {
        return rule;
    }
}

