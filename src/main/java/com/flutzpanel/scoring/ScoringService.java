package com.flutzpanel.scoring;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScoringService {
    private final ScaleOfValues scaleOfValues;

    // Constructor
    public ScoringService(ScaleOfValues scaleOfValues) {
        this.scaleOfValues = scaleOfValues;
    }


    public ScoringElement findScoringElement(String code) {
        return findInList(this.scaleOfValues.getJumps(), code)
                .or(() -> findInList(this.scaleOfValues.getSpins(), code))
                .or(() -> findInList(this.scaleOfValues.getStepSequences(), code))
                .or(() -> findInList(this.scaleOfValues.getChoreographicElements(), code))
                .orElseThrow();
    }

    // Helper Method To Search ScaleOfValue lists for ScoringElement's id code
    private Optional<ScoringElement> findInList(List<ScoringElement> elements, String code) {
        return elements.stream().filter(x -> x.getId().equals(code)).findFirst();
    }


}
