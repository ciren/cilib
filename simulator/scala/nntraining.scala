import net.cilib.simulator.Simulation._
import net.sourceforge.cilib._

def gcpso = new pso.PSO() {
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
  setInitialisationStrategy {
    new algorithm.initialisation.ClonedPopulationInitialisationStrategy() {
      setEntityNumber(20)
      setEntityType {
        new pso.particle.StandardParticle() {
          setVelocityInitialisationStrategy(new entity.initialisation.ConstantInitialisationStrategy(0.0))
          setVelocityProvider {
            new pso.velocityprovider.GCVelocityProvider() {
              setDelegate {
                new pso.velocityprovider.StandardVelocityProvider(
                  controlparameter.ConstantControlParameter.of(0.729844),
                  controlparameter.ConstantControlParameter.of(1.496180),
                  controlparameter.ConstantControlParameter.of(1.496180)
                )
              }
            }
          }
          setNeighbourhoodBestUpdateStrategy(new pso.positionprovider.MemoryNeighbourhoodBestUpdateStrategy())
        }
      }
    }
  }
  setNeighbourhood {
    new entity.topologies.LBestNeighbourhood[pso.particle.StandardParticle]() {
      setNeighbourhoodSize(5)
    }.asInstanceOf[entity.topologies.Neighbourhood[pso.particle.Particle]]
  }
}

def gradientdecent = new gd.GradientDescentBackpropagationTraining() {
  setLearningRate(controlparameter.ConstantControlParameter.of(0.2))
  setMomentum(controlparameter.ConstantControlParameter.of(0.8))
  addStoppingCondition(new stoppingcondition.MeasuredStoppingCondition())
}

def nnSlidingWindow = new problem.nn.NNSlidingWindowDataTrainingProblem() {
  setWindowSize(50)
  setChangeFrequency(100)
  setTrainingSetPercentage(0.7)
  setGeneralisationSetPercentage(0.3)
  setDataTableBuilder {
    new io.DataTableBuilder() {
      setDataReader(new io.ARFFFileReader() { setSourceURL("../../library/src/test/resources/datasets/iris.arff") })
    }
  }
  setNeuralNetwork {
    new nn.NeuralNetwork() {
      setArchitecture {
        new nn.architecture.Architecture() {
          setArchitectureBuilder {
            new nn.architecture.builder.FeedForwardArchitectureBuilder() {
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(4) })
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(3); setActivationFunction(new functions.activation.Sigmoid()) })
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

def nnLambdaGamma = new problem.nn.NNDataTrainingProblem() {
  setTrainingSetPercentage(0.7)
  setGeneralisationSetPercentage(0.3)
  setDataTableBuilder {
    new io.DataTableBuilder() {
      setDataReader(new io.ARFFFileReader() { setSourceURL("../../library/src/test/resources/datasets/iris.arff") })
    }
  }
  setSolutionConversionStrategy(new nn.domain.LambdaGammaSolutionConversionStrategy())
  setNeuralNetwork {
    new nn.NeuralNetwork() {
      setArchitecture {
        new nn.architecture.Architecture() {
          setArchitectureBuilder {
            new nn.architecture.builder.FeedForwardArchitectureBuilder() {
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(4) })
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(3); setActivationFunction(new functions.activation.Sigmoid()) })
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(1) })
              setLayerBuilder {
                new nn.architecture.builder.PrototypeFullyConnectedLayerBuilder() {
                  setDomainProvider {
                    // change this to lambda gamma
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

def nnSig = new problem.nn.NNDataTrainingProblem() {
  setTrainingSetPercentage(0.7)
  setGeneralisationSetPercentage(0.3)
  setDataTableBuilder {
    new io.DataTableBuilder() {
      setDataReader(new io.ARFFFileReader() { setSourceURL("../../library/src/test/resources/datasets/iris.arff") })
    }
  }
  setNeuralNetwork {
    new nn.NeuralNetwork() {
      setArchitecture {
        new nn.architecture.Architecture() {
          setArchitectureBuilder {
            new nn.architecture.builder.FeedForwardArchitectureBuilder() {
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(4) })
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(3); setActivationFunction(new functions.activation.Sigmoid()) })
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

def nnLin = new problem.nn.NNDataTrainingProblem() {
  setTrainingSetPercentage(0.7)
  setGeneralisationSetPercentage(0.3)
  setDataTableBuilder {
    new io.DataTableBuilder() {
      setDataReader(new io.ARFFFileReader() { setSourceURL("../../library/src/test/resources/datasets/iris.arff") })
    }
  }
  setNeuralNetwork {
    new nn.NeuralNetwork() {
      setArchitecture {
        new nn.architecture.Architecture() {
          setArchitectureBuilder {
            new nn.architecture.builder.FeedForwardArchitectureBuilder() {
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(4) })
              addLayer(new nn.architecture.builder.LayerConfiguration() { setSize(3); setActivationFunction(new functions.activation.Linear()) })
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

runner(simulation(gradientdecent, nnSig), fitness, "data/gradientdecent-iris-sigmoid.txt", 1, 1)
runner(simulation(gradientdecent, nnLin), fitness, "data/gradientdecent-iris-linear.txt", 1, 1)
runner(simulation(gradientdecent, nnLambdaGamma), fitness, "data/gradientdecent-iris-lg.txt", 1, 1)
runner(simulation(gradientdecent, nnSlidingWindow), fitness, "data/gradientdecent-iris-sw.txt", 1, 1)
runner(simulation(gcpso, nnSig), fitness, "data/gcpso-iris-sigmoid.txt", 1, 1)
runner(simulation(gcpso, nnLin), fitness, "data/gcpso-iris-linear.txt", 1, 1)
runner(simulation(gcpso, nnLambdaGamma), fitness, "data/gcpso-iris-lg.txt", 1, 1)
runner(simulation(gcpso, nnSlidingWindow), fitness, "data/gcpso-iris-sw.txt", 1, 1)

