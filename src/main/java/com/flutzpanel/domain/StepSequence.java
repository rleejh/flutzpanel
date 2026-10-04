package com.flutzpanel.domain;

public class StepSequence extends Element{
    private ElementLevel level;


    public StepSequence() {
        super();
        this.level = ElementLevel.BASIC;
    }

    public boolean isValid() {
        if (this.level == null) {
            return false;
        }

        return true;

    }


    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getCode(){
        return "";
    }

    // Getter Method for Element Level
    public ElementLevel getLevel() {
        return this.level;
    }


    // Setter Method for Element Level
    public void setLevel(ElementLevel level) {
        if (level == null) {
            throw new IllegalArgumentException("Step sequence level cannot be null.");
        }


        this.level = level;
    }

}
