package com.flutzpanel.domain;

import jakarta.persistence.Entity;

@Entity
public class ChoreographicElement extends Element {
    private ChoreographicElementType type;


    public ChoreographicElement() {
        super();
    }

    public boolean isValid() {
        if (this.type == null) {
            return false;
        }
        return true;
    }

    // Setter Method for Choreo Element Type
    public void setChoreoType(ChoreographicElementType type) {
        if (type == null) {
            throw new IllegalArgumentException("Choreo type cannot be null.");
        }

        this.type = type;
    }

    // Getter Method for Choreo Element Type
    public ChoreographicElementType getChoreoType() {
        return this.type;
    }



    @Override
    public String getName() {
        return "";
    }


    @Override
    public String getCode(){

        if (this.type == null) {
            throw new IllegalStateException("Choreographic element type must be selected.");
        }

        return "ChSq1";
    }



}
