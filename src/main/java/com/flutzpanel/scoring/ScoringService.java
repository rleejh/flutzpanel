package com.flutzpanel.scoring;

import com.flutzpanel.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;



@Service
public class ScoringService {
    private final ScaleOfValues scaleOfValues;
    private final List<ProgramComponentFactor> programComponentFactors;
    private static final BigDecimal BONUS_MULTIPLIER = new BigDecimal ("1.1");

    // Constructor
    public ScoringService(ScaleOfValues scaleOfValues, List<ProgramComponentFactor> programComponentFactors) {
        this.scaleOfValues = scaleOfValues;
        this.programComponentFactors = programComponentFactors;
    }

    // Calculates technical total of the program
    public BigDecimal calculateTechnicalScore(Program program) {

        if (program == null) {
            throw new IllegalArgumentException("Program cannot be null.");
        }

        List<Element> listOfElements = program.getElements();

        BigDecimal retVal = new BigDecimal("0");

        for (Element e : listOfElements) {
            retVal = retVal.add(scoreElement(e).getTotal());
        }

        return retVal;
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



    // Returns PCS total of the program
    public BigDecimal calculateComponentScore(Program program) {
        BigDecimal factor = findComponentFactor(program);
        BigDecimal retVal = new BigDecimal ("0");

        List<ProgramComponent> pcs = program.getProgramComponents();

        for (ProgramComponent pcsCategory : pcs) {
            BigDecimal scoreFactored = pcsCategory.getScore().multiply(factor).setScale(2, RoundingMode.HALF_UP);
            retVal = retVal.add(scoreFactored);
        }

        return retVal;

    }

    // Returns the appropriate PCS factor based on discipline and type of program
    public BigDecimal findComponentFactor(Program program) {
        for (ProgramComponentFactor currentRules : programComponentFactors) {
            if(program.getSeason().equals(currentRules.getSeason())
            && program.getProgramType() == currentRules.getProgramType()
            && program.getDiscipline() ==  currentRules.getDiscipline()) {
                return currentRules.getFactor();
            }
        }

        throw new IllegalArgumentException("No component factor found for program.");
    }

    // Returns the total magnitude of deductions in program (method will return a positive number)
    public BigDecimal calculateDeductionsTotal(Program program) {
        BigDecimal retVal = BigDecimal.ZERO;

        List<Deduction> deductions = program.getDeductions();

        for (Deduction deduction : deductions) {
            retVal = retVal.add(BigDecimal.valueOf(deduction.getValue()));
        }

        return retVal;
    }

    // Returns total program score as a ProgramScore object
    public ProgramScore calculateTotalProgramScore(Program program) {
        BigDecimal technicalScore = calculateTechnicalScore(program);
        BigDecimal componentScore = calculateComponentScore(program);
        BigDecimal deductionsTotal = calculateDeductionsTotal(program);

        BigDecimal totalProgramScore = technicalScore
                .add(componentScore)
                .subtract(deductionsTotal);

        ProgramScore programScore = new ProgramScore();

        programScore.setTechnicalScore(technicalScore);
        programScore.setComponentScore(componentScore);
        programScore.setDeductionsTotal(deductionsTotal);
        programScore.setTotalProgramScore(totalProgramScore);

        return programScore;
    }





}
