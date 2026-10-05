package com.flutzpanel.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class JumpPass extends Element{
    private List<Jump> jumps = new ArrayList<>();
    private boolean bonus = false;

    // Constructor
    public JumpPass() {
        super();
    }

    public void addJump(Jump jump){
        if (jump == null) {
            throw new IllegalArgumentException("Jump cannot be null");
        }
        this.jumps.add(jump);
    }


    //
    @Override
    public String getName() {
        return "";
    }

    //
    @Override
    public String getCode(){
        if (this.jumps.isEmpty()) {
            throw new IllegalStateException("No Jumps found");
        }

        StringJoiner joiner = new StringJoiner("+");

        for (Jump jump : this.jumps) {
            joiner.add(jump.getCode());
        }

        return joiner.toString();
    }


    public boolean isValid() {
        return !this.jumps.isEmpty();
    }


    // Getter for List of Jumps
    public List<Jump> getJumps(){
        return this.jumps;
    }

    // Setting for Bonus Field
    public void setBonus(boolean bonus){
        this.bonus = bonus;
    }

    // Setting for Bonus Field
    public boolean isBonus(){
        return this.bonus;
    }


}
