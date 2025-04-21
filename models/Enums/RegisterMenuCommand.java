package models.Enums;

public enum RegisterMenuCommand {
    REGISTER("^register\\s+-u\\s+(?<username>.+?)\\s+-p\\s+(?<password>.+?)\\s+" +
            "(?<confirmPassword>.+?)\\s+-n\\s+(?<nickname>.+?)\\s+-e\\s+(?<email>.+?)\\s+-g\\s+(?<gender>.+?)$"),
    INVALID(".*");

    private final String regexPattern;

    RegisterMenuCommand(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    public String getRegexPattern() {
        return regexPattern;
    }
}