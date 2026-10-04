package com.flutzpanel.scoring;

import java.util.List;

public class ScaleOfValues {
    private List<ScoringElement> jumps;
    private List<ScoringElement> spins;
    private List<ScoringElement> stepSequences;
    private List<ScoringElement> choreographicSequences;

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

    public List<ScoringElement> getChoreographicSequences() {
        return choreographicSequences;
    }

    public void setChoreographicSequences(List<ScoringElement> choreographicSequences) {
        this.choreographicSequences = choreographicSequences;
    }
}
