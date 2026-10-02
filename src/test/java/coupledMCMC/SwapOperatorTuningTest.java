package coupledMCMC;

import beast.base.spec.domain.Real;
import beast.base.spec.inference.operator.RealRandomWalkOperator;
import beast.base.spec.inference.parameter.RealScalarParam;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SwapOperatorTuningTest {

    private static RealRandomWalkOperator operator(double windowSize) {
        RealScalarParam<Real> x = new RealScalarParam<>(0.0, Real.INSTANCE);
        RealRandomWalkOperator op = new RealRandomWalkOperator();
        op.initByName("scalar", x, "windowSize", windowSize, "weight", 1.0);
        return op;
    }

    private static List<Integer> counts(RealRandomWalkOperator op) {
        return List.of(op.get_m_nNrAccepted(), op.get_m_nNrRejected(),
                op.get_m_nNrAcceptedForCorrection(), op.get_m_nNrRejectedForCorrection());
    }

    @Test
    void swapTuningExchangesAcceptAndRejectCountsWithoutMixingThemUp() {
        RealRandomWalkOperator a = operator(1.0);
        RealRandomWalkOperator b = operator(2.0);
        a.setAcceptedRejected(3, 7, 2, 6);
        b.setAcceptedRejected(5, 1, 4, 0);

        CoupledMCMC.swapTuning(a, b);

        assertEquals(List.of(5, 1, 4, 0), counts(a));
        assertEquals(List.of(3, 7, 2, 6), counts(b));
    }

    @Test
    void swapTuningExchangesTheTunedParameterToo() {
        RealRandomWalkOperator a = operator(1.0);
        RealRandomWalkOperator b = operator(2.0);

        CoupledMCMC.swapTuning(a, b);

        assertEquals(2.0, a.getCoercableParameterValue());
        assertEquals(1.0, b.getCoercableParameterValue());
    }
}
