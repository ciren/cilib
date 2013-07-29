import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def alg = new pso.PSO() {
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityNumber(10)
      setEntityType(new pso.dynamic.DynamicParticle())
    }
  }
  setIterationStrategy {
    new pso.dynamic.DynamicIterationStrategy() {
      setIterationStrategy(new pso.iterationstrategies.SynchronousIterationStrategy())
      setDetectionStrategy(new pso.dynamic.detectionstrategies.PeriodicDetectionStrategy() { setIterationsModulus(10) })
      setResponseStrategy {
        new pso.dynamic.responsestrategies.MultiReactionStrategy() {
          addResponseStrategy(new pso.dynamic.responsestrategies.CascadeNetworkExpansionReactionStrategy())
          addResponseStrategy(new pso.dynamic.responsestrategies.InitialiseNaNElementsReactionStrategy())
          addResponseStrategy(new pso.dynamic.responsestrategies.ReinitialiseCascadeNetworkOutputWeightsReactionStrategy())
          addResponseStrategy(new pso.dynamic.responsestrategies.ReevaluationReactionStrategy() { setReevaluationRatio(1.0) })
        }
      }
      addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition() { setTarget(100) })
      addStoppingCondition(new stoppingcondition.PerExpansionNNPerformanceChangeStoppingCondition())
    }
  }
}

def nnProblem = new problem.nn.NNDataTrainingProblem() {
  setTrainingSetPercentage(0.6)
  setGeneralisationSetPercentage(0.2)
  setValidationSetPercentage(0.2)
  setDataTableBuilder {
    new io.DataTableBuilder() {
      setDataReader(new io.ARFFFileReader() { setSourceURL("../../library/src/test/resources/datasets/iris.arff") })
    }
  }
  setNeuralNetwork {
    new nn.NeuralNetwork() {
      setOperationVisitor(new nn.architecture.visitors.CascadeVisitor())
      setArchitecture {
        new nn.architecture.Architecture() {
          setArchitectureBuilder {
            new nn.architecture.builder.CascadeArchitectureBuilder() {
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(4) })
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(1); setActivationFunction(new functions.activation.Sigmoid()) })
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(1) })
              setLayerBuilder {
                new nn.architecture.builder.PrototypeFullyConnectedLayerBuilder() {
                  setDomainProvider {
                    new nn.domain.PresetNeuronDomain() {
                      setWeightDomainPrototype(new `type`.StringBasedDomainRegistry() { setDomainString("R(-3:3)") })
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

def fitness = new simulator.MeasurementSuite() {
  addMeasurement(new measurement.single.MSEGeneralisationError())
  addMeasurement(new measurement.single.MSETrainingError())
  addMeasurement(new measurement.single.Solution())
}

runner(simulation(alg, nnProblem), fitness, "data/dynamicPSOCascadeNN.txt", 1, 1)

