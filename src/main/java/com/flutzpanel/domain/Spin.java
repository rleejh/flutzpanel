package com.flutzpanel.domain;

public class Spin extends Element{

    private ElementLevel level;
    private SpinType type;
    private boolean flying;
    private boolean changeFoot;
    private boolean vStatus;


    public Spin() {
        super();
        this.level = ElementLevel.BASIC;
        this.flying = false;
        this.changeFoot = false;
        this.vStatus = false;
    }

    public boolean isValid() {
        if (this.type == null) {
            return false;
        }

        return true;
    }


    //
    @Override
    public String getName() {
        return "";
    }

    //
    @Override
    public String getCode(){
        if (this.type == null) {
            throw new IllegalStateException("Spin type must be selected.");
        }

        String spinTypeCode = getSpinTypeCode();
        String spinLevelCode = getSpinLevelCode();
        String spinFlyingCode = "";
        String spinChangeFootCode = "";
        String spinVStatusCode = "";

        if (this.isFlying() == true) {
            spinFlyingCode = "F";
        }

        if (this.isChangeFoot() == true) {
            spinChangeFootCode = "C";
        }

        if (this.isVStatus() == true) {
            spinVStatusCode = "V";
        }


        return spinFlyingCode + spinChangeFootCode + spinTypeCode + "Sp" + spinLevelCode + spinVStatusCode;
    }

    // Helper function that returns code for spin type
    private String getSpinTypeCode() {
        return switch (this.type) {
            case UPRIGHT -> "U";
            case SIT -> "S";
            case CAMEL -> "C";
            case LAYBACK -> "L";
            case COMBINATION -> "Co";
        };
    }

    // Helper function that returns code for spin level
    private String getSpinLevelCode() {
        return switch (this.level) {
            case BASIC -> "B";
            case ONE -> "1";
            case TWO -> "2";
            case THREE -> "3";
            case FOUR -> "4";
        };
    }


    // Getter Method for Spin Level
    public ElementLevel getLevel() {
        return this.level;
    }

    // Getter Method for Spin Type
    public SpinType getSpinType() {
        return this.type;
    }

    // Getter Method for Flying Status
    public boolean isFlying() {
        return this.flying;
    }

    // Getter Method for Change of Foot Status
    public boolean isChangeFoot() {
        return this.changeFoot;
    }

    // Getter Method for V Status
    public boolean isVStatus() {
        return this.vStatus;
    }



    // Setter Method for Spin Level
    public void setLevel(ElementLevel level) {
        if (level == null) {
            throw new IllegalArgumentException("Spin level cannot be null.");
        }

        this.level = level;
    }

    // Setter Method for Spin Type
    public void setSpinType(SpinType type) {
        if (type == null) {
            throw new IllegalArgumentException("Spin type cannot be null.");
        }

        this.type = type;
    }
    // Setter Method for Flying Status
    public void setFlying(boolean flying) {
        this.flying = flying;
    }

    // Setter Method for Change of Foot Status
    public void setChangeFoot(boolean changeFoot) {
        this.changeFoot = changeFoot;
    }

    // Setter Method for V Status
    public void setVStatus(boolean vStatus) {
        this.vStatus = vStatus;
    }



}
