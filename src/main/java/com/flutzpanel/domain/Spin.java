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
        return "";
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
