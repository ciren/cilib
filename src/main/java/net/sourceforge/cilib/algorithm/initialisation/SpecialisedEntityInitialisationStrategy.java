package net.sourceforge.cilib.algorithm.initialisation;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.algorithm.InitialisationException;
import net.sourceforge.cilib.entity.Entity;
import net.sourceforge.cilib.entity.Topology;
import net.sourceforge.cilib.problem.OptimisationProblem;

public class SpecialisedEntityInitialisationStrategy extends
		InitialisationStrategy {
	
	private List<Entity> entityList;
	
	public SpecialisedEntityInitialisationStrategy() {
		this.entityList = new ArrayList<Entity>(40);
	}

	public SpecialisedEntityInitialisationStrategy(SpecialisedEntityInitialisationStrategy copy) {
		this.entityList = new ArrayList<Entity>(copy.entityList.size());
		for (Entity entity : copy.entityList) {
			this.entityList.add(entity.clone());
		}
	}
	
	public SpecialisedEntityInitialisationStrategy clone() {
		return new SpecialisedEntityInitialisationStrategy(this);
	}

	@Override
	public Entity getEntityType() {
		// this needs to be looked at... generalisation breaks here
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialise(Topology topology, OptimisationProblem problem) {
		 if (problem == null)
			 throw new InitialisationException("No problem has been specified");
		 
		 if (this.entityList.size() == 0)
			 throw new InitialisationException("No prototype Entity object has been defined for the clone operation in the entity constrution process.");
		 
		 for (Entity entity : entityList) {
			 entity.initialise(problem);
			 topology.add(entity);
		 }
	}

	@Override
	public void setEntityType(Entity entity) {
		this.entityList.add(entity);
	}

}
