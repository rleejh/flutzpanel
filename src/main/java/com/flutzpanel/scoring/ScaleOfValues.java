package com.flutzpanel.scoring;

import java.util.List;

public class ScaleOfValues {
    private List<ScoringElement> jumps;
    private List<ScoringElement> spins;
    private List<ScoringElement> stepSequences;
    private List<ScoringElement> choreographicElements;

    public List<ScoringElement> getJumps() {
        return jumps;
    }

    public void setJumps(List<ScoringElement> jumps) {
        this.jumps = jumps;
    }

    public List<ScoringElement> getSpins() {
        return spins;
    }

    public void setSpins(List<ScoringElement> spins) {
        this.spins = spins;
    }

    public List<ScoringElement> getStepSequences() {
        return stepSequences;
    }

    public void setStepSequences(List<ScoringElement> stepSequences) {
        this.stepSequences = stepSequences;
    }

    public List<ScoringElement> getChoreographicElements() {
        return choreographicElements;
    }

    public void setChoreographicElements(List<ScoringElement> choreographicElements) {
        this.choreographicElements = choreographicElements;
    }
}
