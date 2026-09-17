# Figure Skating Calculator — MVP PRD

## 1. Overview

A web-based figure skating program builder and technical score calculator.

The application allows a user to construct a skating program by adding and configuring elements such as jumps, spins, and step sequences. The application calculates the value of each element and the program's total technical score.

The MVP focuses on the core program-building and scoring workflow. Saving/loading programs, user accounts, advanced statistics, and other features will be added later.

## 2. Problem

Calculating a figure skating program's technical score manually can be cumbersome and time-consuming, particularly when elements have different configurations, GOE, bonuses, and other scoring modifiers.

The application provides a structured way to build a program and automatically calculate the resulting technical score.

## 3. Target User

The primary user is someone who wants to:

- Calculate the technical score of an actual skating performance.
- Create a hypothetical program layout.
- Experiment with different element configurations and GOE values.

The initial version does not require user accounts.

## 4. MVP Goals

The MVP should allow a user to:

1. Create and configure skating elements.
2. Add elements to a program.
3. View all elements currently in the program.
4. See the base value and calculated value of each element.
5. See the program's current technical score.
6. Edit an element after adding it.
7. Delete an element from the program.

## 5. Core User Flow

```text
Create Program
      ↓
Select Element Type
      ↓
Configure Element
      ↓
Add Element to Program
      ↓
View Program Elements
      ↓
Edit / Delete Elements
      ↓
View Updated Technical Score
```

## 6. MVP Domain Concepts

### Program

Represents a skating program.

A program contains an ordered collection of skating elements.

Future versions may also contain component/artistic scores, deductions, metadata, and other program information.

### Element

Represents a skating element and contains information common to different element types.

Examples of common information include:

- Human-readable name
- Element code
- Grade of Execution (GOE)

Different element types have different properties and rules.

### Jump Pass

Represents a jump element or a group of jumps performed together.

A jump pass may contain multiple jumps, allowing combinations/sequences to be represented without treating the entire combination as one indivisible jump.

### Jump

Represents an individual jump.

Jump-specific information may include:

- Jump type
- Number of rotations
- Rotation/status information
- Bonus status
- Edge-related status where applicable
- Repeat-related status where applicable

### Spin

Represents a spin.

Spin-specific information may include:

- Spin type
- Level
- Flying/change-of-foot characteristics
- V status where applicable
- Choreographic spin variant where applicable

### Step Sequence

Represents a step sequence.

Step-sequence-specific information may include:

- Level
- Choreographic sequence variant where applicable

## 7. Scoring

The application will calculate element values based on the applicable scoring rules and reference data.

The MVP will initially support the current ruleset selected for the project.

Scoring reference data (such as element base values) should be treated separately from the application logic so that future rulesets can be supported without rewriting the entire application.

Specific scoring rules and reference values will be verified against authoritative skating documentation before implementation.

## 8. Validation

The application should prevent invalid element configurations.

Examples include:

- GOE outside the supported range.
- Mutually exclusive jump statuses being selected simultaneously.
- Invalid combinations of element properties.
- Invalid element configurations according to the applicable ruleset.

Validation should exist in the backend rather than relying solely on frontend controls.

## 9. Non-Goals for the MVP

The following are intentionally deferred:

- Saving/loading programs
- User accounts and authentication
- Program sharing
- Social features
- Historical statistics
- Advanced analytics
- Native mobile applications
- AI features
- Component/artistic score calculations
- Deductions

These may become future features.

## 10. Technical Direction

The application will be a web application with:

- A frontend responsible for the user interface.
- A backend responsible for application/domain logic and REST APIs.
- A relational database for persistent application data.

The specific technology stack will be selected separately based on learning value, project requirements, and the developer's goals.

The backend should maintain clear separation between:

- Domain concepts
- Application/business logic
- API/web concerns
- Persistence/database concerns

## 11. Future Direction

Potential future functionality includes:

- Saving and loading programs
- Supporting multiple skating rulesets/seasons
- Component/artistic scores
- Deductions
- Program comparison
- Historical scoring analysis
- Element usage statistics
- Personal program/performance tracking
- User accounts
- Program sharing
- Coach/student workflows

These features should not drive MVP implementation unless they directly affect a current architectural decision.

## 12. MVP Success Criteria

The MVP is successful when a user can:

1. Open the application.
2. Create a program.
3. Add different types of skating elements.
4. Configure their properties.
5. See the calculated value of each element.
6. See the running technical total.
7. Correct an element by editing it.
8. Remove an element.
9. Receive validation feedback when an invalid configuration is attempted.

The scoring calculations should be covered by automated tests.