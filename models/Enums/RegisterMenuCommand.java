package models.Enums;

public enum RegisterMenuCommand {
    REGISTER("^register\\s+-u\\s+(?<username>.+?)\\s+-p\\s+(?<password>.+?)\\s+" +
            "(?<confirmPassword>.+?)\\s+-n\\s+(?<nickname>.+?)\\s+-e\\s+(?<email>.+?)\\s+-g\\s+(?<gender>.+?)$"),
    RANDOM_PASSWORD("^\\s*random\\s+password\\s*$"),
    PICK_QUESTION("^pick question -q (?<questionNumber>\\d+) -a (?<answer>.+) -c (?<confirmAnswer>.+)$"),
    INVALID(".*");

    private final String regexPattern;

    RegisterMenuCommand(String regexPattern) {
        this.regexPattern = regexPattern;
    }

    public String getRegexPattern() {
        return regexPattern;
    }
}