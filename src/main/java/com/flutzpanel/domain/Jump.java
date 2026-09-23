package com.flutzpanel.domain;

public class Jump{

    private int rotations;
    private JumpType type;
    private JumpRotation jumpRotation;
    private JumpEdge jumpEdge;

    public Jump() {
        this.rotations = 0;
        this.jumpRotation = JumpRotation.CLEAN;
        this.jumpEdge = JumpEdge.CORRECT;

    }



    // Getter Method for Rotations
    public int getRotations() {
        return this.rotations;
    }

    // Getter Method for Jump Type
    public JumpType getJumpType() {
        return this.type;
    }

    // Getter Method for Jump Rotation Status
    public JumpRotation getJumpRotation() {
        return this.jumpRotation;
    }

    // Getter Method for Jump Edge Status
    public JumpEdge getJumpEdge() {
        return this.jumpEdge;
    }


    // Setter Method for Rotations
    public void setRotations(int rotations) {
        if (rotations < 0 || rotations > 5) {
            throw new IllegalArgumentException ("Jumps Rotations should be between 0 and 5.");
        }

        this.rotations = rotations;
    }

    // Setter Method for Jump Type
    public void setJumpType(JumpType type) {
        this.type = type;
    }

    // Setter Method for Jump Rotation Status
    public void setJumpRotation(JumpRotation jumpRotation) {
        this.jumpRotation = jumpRotation;
    }

    // Getter Method for Jump Edge Status
    public void setJumpEdge(JumpEdge jumpEdge) {
        this.jumpEdge = jumpEdge;
    }

}
