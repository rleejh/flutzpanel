package com.flutzpanel.domain;

public class StepSequence extends Element{
    private ElementLevel level;
    private boolean choreoStep;


    public StepSequence() {
        super();
        this.level = ElementLevel.BASIC;
        this.choreoStep = false;
    }

    public boolean isValid() {
        if (this.level == null) {
            return false;
        }

        if (this.choreoStep && this.level != ElementLevel.ONE) {
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

    // Getter Method for Element Level
    public ElementLevel getLevel() {
        return this.level;
    }

    // Getter Method for Choreographic Step Status
    public boolean isChoreoStep() {
        return this.choreoStep;
    }


    // Setter Method for Element Level
    public void setLevel(ElementLevel level) {
        this.level = level;
    }

    // Setter Method for Choreographic Step Status
    public void setChoreoStep(boolean choreoStep) {
        this.choreoStep = choreoStep;
    }
}
