package com.flutzpanel.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Jump{

    private int rotations;

    @Enumerated(EnumType.STRING)
    private JumpType type;

    @Enumerated(EnumType.STRING)
    private JumpRotation jumpRotation;

    @Enumerated(EnumType.STRING)
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
            throw new IllegalArgumentException ("Jumps rotations should be between 0 and 5.");
        }

        this.rotations = rotations;
    }

    // Setter Method for Jump Type
    public void setJumpType(JumpType type) {
        if (type == null) {
            throw new IllegalArgumentException ("Jumps type cannot be null.");
        }
        this.type = type;
    }

    // Setter Method for Jump Rotation Status
    public void setJumpRotation(JumpRotation jumpRotation) {
        this.jumpRotation = jumpRotation;
    }

    // Setter Method for Jump Edge Status
    public void setJumpEdge(JumpEdge jumpEdge) {
        this.jumpEdge = jumpEdge;
    }

    // Returns the code identifier for the Jump element
    public String getCode() {
        if (this.type == null) {
            throw new IllegalStateException("Jump type must be selected.");
        }

        String jumpTypeCode = getJumpTypeCode();
        String jumpRotationCode = getJumpRotationCode();
        String jumpEdgeCode = getJumpEdgeCode();

        return this.rotations + jumpTypeCode + jumpRotationCode + jumpEdgeCode;
    }

    // Helper function that returns code for jump type
    private String getJumpTypeCode() {
        return switch (this.type) {
            case EULER -> "Eu";
            case TOE -> "T";
            case SALCHOW -> "S";
            case LOOP -> "Lo";
            case FLIP -> "F";
            case LUTZ -> "Lz";
            case AXEL -> "A";
        };
    }

    // Helper function that returns code for jump rotation
    private String getJumpRotationCode() {
        return switch (this.jumpRotation) {
            case CLEAN -> "";
            case QUARTER -> "q";
            case UNDERROTATED -> "<";
            case DOWNGRADED -> "<<";
        };
    }


    // Helper function that returns code for jump edge
    private String getJumpEdgeCode() {
        return switch (this.jumpEdge) {
            case CORRECT -> "";
            case UNCLEAR -> "!";
            case WRONG -> "e";
        };
    }



}
