package com.flutzpanel.domain;

public class Spin extends Element{

    private SpinLevel level;
    private SpinType type;
    private boolean flying;
    private boolean changeFoot;
    private boolean vStatus;
    private boolean choreoSpin;

    public Spin() {
        this.level = SpinLevel.BASIC;
        this.flying = false;
        this.changeFoot = false;
        this.vStatus = false;
        this.choreoSpin = false;
    }


    //
    @Override
    public String getName() {
        return "";
    }

    //
    @Override
    public String getCode(){
        return "";
    }

    // Getter Method for Spin Level
    public SpinLevel getSpinLevel() {
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

    // Getter Method for Choreographic Spin Status
    public boolean isChoreoSpin() {
        return this.choreoSpin;
    }


    // Setter Method for Spin Level
    public void setSpinLevel(SpinLevel level) {
        this.level = level;
    }

    // Setter Method for Spin Type
    public void setSpinType(SpinType type) {
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

    // Setter Method for Choreographic Spin Status
    public void setChoreoSpin(boolean choreoSpin) {
        this.choreoSpin = choreoSpin;
    }

}
