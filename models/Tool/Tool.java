package models.Tool;

import models.*;

import java.util.HashMap;

public interface Tool {
    void use(HashMap.Entry<Integer, Integer> coordinates);
    void reduceEnergy();
}
