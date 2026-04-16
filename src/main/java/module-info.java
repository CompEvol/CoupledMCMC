open module coupled.mcmc {
    requires beast.base;
    requires beast.pkgmgmt;
    requires static beast.fx;
    requires java.xml;
    requires org.apache.commons.statistics.distribution;

    exports coupledMCMC;

    provides beast.base.core.BEASTInterface with
            coupledMCMC.CoupledLogger,
            coupledMCMC.CoupledMCMC,
            coupledMCMC.DummyLogger,
            coupledMCMC.HeatedChain,
            coupledMCMC.HeatedChainLikelihoodOnly,
            coupledMCMC.MCMC2CoupledMCMC,
            coupledMCMC.MultiCoupledMCMC,
            // RemoteCoupledLogger excluded: no default constructor
            coupledMCMC.RemoteCoupledMCMC,
            coupledMCMC.RemoteMC3Server,
            coupledMCMC.SerialMCMC,
            coupledMCMC.SubsampledTreeLikelihood;
}
