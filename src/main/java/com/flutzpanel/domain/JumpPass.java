package com.flutzpanel.domain;
import java.util.List;
import java.util.ArrayList;

public class JumpPass extends Element{
    private List<Jump> jumps = new ArrayList<>();
    private boolean bonus = false;

    // Constructor
    public JumpPass() {
        super();
    }

    public void addJump(Jump jump){
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
        return "";
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
    public boolean getBonus(){
        return this.bonus;
    }


}
