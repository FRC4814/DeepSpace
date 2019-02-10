package frc.robot.utils;

public enum XboxButton {
    kButtonA(1),
    kButtonB(2),
    kButtonX(3),
    kButtonY(4),
    kBumperLeft(5),
    kBumperRight(6),
    kButtonBack(7),
    kButtonStart(8),
    kStickLeft(9),
    kStickRight(10);

    private int value;

    private XboxButton(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

};