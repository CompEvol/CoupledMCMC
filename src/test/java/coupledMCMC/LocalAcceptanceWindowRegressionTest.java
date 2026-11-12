package coupledMCMC;

import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LocalAcceptanceWindowRegressionTest {
    @Test
    void localAcceptanceUsesOnlyTheTraversedWindow() {
        CoupledMCMC controller = new CoupledMCMC();
        controller.targetAcceptanceProbabilityInput.setValue(0.5, controller);
        controller.maxAdaptationInput.setValue(1.0, controller);
        controller.totalSwaps = 100;
        controller.successfullSwaps = 80;
        // After removing the oldest: size=20, start=2, numerator=9, denominator=18.
        // Correct local rate .5 permits the positive global update; obsolete 9/20 blocks it.
        controller.acceptedSwaps = new ArrayList<>(Collections.nCopies(21, false));
        for (int index = 3; index < 12; index++) controller.acceptedSwaps.set(index, true);
        assertEquals(0.003, controller.getDelta(), 1e-15);
    }

    @Test
    void oppositeLocalAndGlobalSidesStillBlockAdaptation() {
        CoupledMCMC controller = new CoupledMCMC();
        controller.targetAcceptanceProbabilityInput.setValue(0.5, controller);
        controller.totalSwaps = 100;
        controller.successfullSwaps = 80;
        controller.acceptedSwaps = new ArrayList<>(Collections.nCopies(21, false));
        assertEquals(0.0, controller.getDelta(), 0.0);
    }

    @Test
    void correctedLocalRateBlocksNegativeAdaptationAcrossTarget() {
        CoupledMCMC controller = new CoupledMCMC();
        controller.targetAcceptanceProbabilityInput.setValue(0.53, controller);
        controller.maxAdaptationInput.setValue(1.0, controller);
        controller.totalSwaps = 100;
        controller.successfullSwaps = 20;
        // After removing the oldest: size=20, start=2, numerator=10, denominator=18.
        // Correct 10/18 is above .53 and blocks the negative global update;
        // obsolete 10/20 is below .53 and would permit -0.0033.
        controller.acceptedSwaps = new ArrayList<>(Collections.nCopies(21, false));
        for (int index = 3; index < 13; index++) controller.acceptedSwaps.set(index, true);
        assertEquals(0.0, controller.getDelta(), 0.0);
    }

    @Test
    void fullWindowLocalAcceptancePermitsPositiveAdaptation() {
        CoupledMCMC controller = new CoupledMCMC();
        controller.targetAcceptanceProbabilityInput.setValue(0.5, controller);
        controller.maxAdaptationInput.setValue(1.0, controller);
        controller.totalSwaps = 120;
        controller.successfullSwaps = 96;
        // After removing the oldest: size=100, start=10, numerator=46, denominator=90.
        // Correct 46/90 is above .5 and permits the positive global update;
        // obsolete 46/100 is below .5 and would block it.
        controller.acceptedSwaps = new ArrayList<>(Collections.nCopies(101, false));
        for (int index = 11; index < 57; index++) controller.acceptedSwaps.set(index, true);
        assertEquals(0.0025, controller.getDelta(), 1e-15);
    }

    @Test
    void fullWindowRemoteAcceptancePermitsPositiveAdaptation() {
        RemoteCoupledMCMC controller = new RemoteCoupledMCMC();
        controller.targetAcceptanceProbabilityInput.setValue(0.5, controller);
        // The remote cap is 1/(optimiseDelay*10) = .01, above the expected .0025.
        controller.optimiseDelayInput.setValue(10, controller);
        controller.totalSwaps = 120;
        controller.successfullSwaps = 96;
        // Same full window as the local regression; no remote services are initialized.
        controller.acceptedSwaps = new ArrayList<>(Collections.nCopies(101, false));
        for (int index = 11; index < 57; index++) controller.acceptedSwaps.set(index, true);
        assertEquals(0.0025, controller.getDelta(), 1e-15);
    }
}
