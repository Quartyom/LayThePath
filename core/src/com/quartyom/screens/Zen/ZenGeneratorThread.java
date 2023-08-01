package com.quartyom.screens.Zen;

public class ZenGeneratorThread extends Thread {
    ZenLevelGenerator zenLevelGenerator;

    public ZenGeneratorThread(ZenLevelGenerator zenLevelGenerator) {
        this.zenLevelGenerator = zenLevelGenerator;
    }

    @Override
    public void run() {     // просто без конца грузит новые уровни (по требованию, в другие моменты спит)
        while (true) {
            try {
                zenLevelGenerator.generateNewLevel();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
