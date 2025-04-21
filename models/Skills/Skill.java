package models.Skills;

public interface Skill {
    void setLevel(int level);
    void increaseLevel();
    boolean canGoToNextLevel();

}
