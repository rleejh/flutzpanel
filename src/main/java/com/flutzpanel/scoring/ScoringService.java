package com.flutzpanel.scoring;

import com.flutzpanel.domain.Element;
import com.flutzpanel.domain.Jump;
import com.flutzpanel.domain.JumpPass;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;



@Service
public class ScoringService {
    private final ScaleOfValues scaleOfValues;
    private static final BigDecimal BONUS_MULTIPLIER = new BigDecimal ("1.1");

    // Constructor
    public ScoringService(ScaleOfValues scaleOfValues) {
        this.scaleOfValues = scaleOfValues;
    }

    // Returns the ElementScore based on the inputted element
    public ElementScore scoreElement(Element element) {
        if (element instanceof JumpPass jumpPass) {
            return scoreJumpPass(jumpPass);
        } else {
            // Finds Code Of Points Values For Element
            ScoringElement scoringElement = findScoringElement(element.getCode());

            BigDecimal retBaseValue = scoringElement.getBaseValue();
            BigDecimal retGOEContribution;
            BigDecimal retTotal;

            // GOE Calculation
            BigDecimal goe = element.getGoe();
            Map<Integer, BigDecimal> goeTable = scoringElement.getGoe();
            if (goe.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
                retGOEContribution = goeTable.get(goe.intValue());
            } else if (goe.signum() > 0) {
                BigDecimal plusOneGOE = goeTable.get(1);
                retGOEContribution = plusOneGOE.multiply(goe);
            } else {
                BigDecimal minusOneGOE = goeTable.get(-1).abs();
                retGOEContribution = minusOneGOE.multiply(goe);
            }
            retGOEContribution = retGOEContribution.setScale(2, RoundingMode.HALF_UP); // rounds to 2 decimal places

            // Calculate Element Total Score
            retTotal = retBaseValue.add(retGOEContribution);

            return new ElementScore(retBaseValue, retGOEContribution, retTotal);
        }
    }

    // Helper function that returns the ElementScore of a JumpPass
    private ElementScore scoreJumpPass(JumpPass jumpPass) {

        List<Jump> listOfJumps = jumpPass.getJumps();

        if (listOfJumps.isEmpty()) {
            throw new IllegalArgumentException("Jump pass must contain at least one jump");
        }

        BigDecimal retBaseValue = new BigDecimal ("0");
        BigDecimal retGOEContribution;
        BigDecimal retTotal;

        // Base Value Calculation
        for (Jump currJump : listOfJumps) {
            ScoringElement scoringElement = findScoringElement(currJump.getCode());
            BigDecimal currJumpValue = scoringElement.getBaseValue();
            if (jumpPass.isBonus() == true) {
                currJumpValue = currJumpValue.multiply(BONUS_MULTIPLIER);
            }
            retBaseValue = retBaseValue.add(currJumpValue);
        }


        // GOE Calculation
        ScoringElement highestBVJump = findHighestBVJump(listOfJumps);
        Map<Integer, BigDecimal> goeTable = highestBVJump.getGoe();

        BigDecimal goe = jumpPass.getGoe();

        if (goe.remainder(BigDecimal.ONE).compareTo(BigDecimal.ZERO) == 0) {
            retGOEContribution = goeTable.get(goe.intValue());
        } else if (goe.signum() > 0) {
            BigDecimal plusOneGOE = goeTable.get(1);
            retGOEContribution = plusOneGOE.multiply(goe);
        } else {
            BigDecimal minusOneGOE = goeTable.get(-1).abs();
            retGOEContribution = minusOneGOE.multiply(goe);
        }

        retGOEContribution = retGOEContribution.setScale(2, RoundingMode.HALF_UP); // rounds to 2 decimal places

        retTotal = retBaseValue.add(retGOEContribution);


        return new ElementScore(retBaseValue, retGOEContribution, retTotal);
    }


    // Helper function that returns the ScoringElement information of the highest scoring jump of the combination/sequence
    private ScoringElement findHighestBVJump(List<Jump> listOfJumps) {

        if (listOfJumps.size() == 1) {
            return findScoringElement(listOfJumps.getFirst().getCode());
        }

        ScoringElement highestBVJump = null;

        for (Jump currJump: listOfJumps) {

            ScoringElement currJumpScoringElement = findScoringElement(currJump.getCode());
            if (highestBVJump == null) {
                highestBVJump = currJumpScoringElement;
            }


            BigDecimal currBaseValue = currJumpScoringElement.getBaseValue();

            if (currBaseValue.compareTo(highestBVJump.getBaseValue()) > 0) {
                highestBVJump = currJumpScoringElement;
            }
        }

        return highestBVJump;
    }



    // Returns the corresponding ScoringElement Object from given element code
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
