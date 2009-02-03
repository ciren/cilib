/*
 * Copyright (C) 2003 - 2008
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package net.sourceforge.cilib.coevolution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cilib.type.types.Resetable;
import net.sourceforge.cilib.type.types.Type;

/**
 * @author leo
 * This class maintains a list of competitors for a coevolution problem
 */
public class CoevolutionEvaluationList implements Type, Resetable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2894509571214982344L;
	/**
	 * 
	 */
	List< List<EvaluationEntity>> evaluationEntities;
	int amountEntitiesPerList;
	/**
	 * 
	 */
	public CoevolutionEvaluationList() {
		evaluationEntities = new ArrayList<List<EvaluationEntity>>();
		amountEntitiesPerList = 0;
	}
	
	public CoevolutionEvaluationList(CoevolutionEvaluationList other) {
		evaluationEntities = new ArrayList<List<EvaluationEntity>>();
		amountEntitiesPerList = 0;
		for(List<EvaluationEntity> subList: other.evaluationEntities){
			if(amountEntitiesPerList == 0)
				amountEntitiesPerList = subList.size();
			List<EvaluationEntity> nlist = new ArrayList<EvaluationEntity>();
			for(EvaluationEntity e: subList){
				nlist.add(e.getClone());
			}
			evaluationEntities.add(nlist);			
		}
		amountEntitiesPerList = 0;
	}
	
	public void addEntityList(List<EvaluationEntity> list){
		if(amountEntitiesPerList == 0) //set the default size to the size of the first list provided
			amountEntitiesPerList = list.size();
		else
			if(list.size() != amountEntitiesPerList)
				throw new RuntimeException("Entity list is not the right size, each subpopulation should have the same length");
		evaluationEntities.add(list);	
	}
	
	/**
	 * get one entry from each list at index
	 * @param index the specified index
	 * @return the requested list
	 */
	public List<EvaluationEntity> getEntitiesFromSubList(int index){
		List<EvaluationEntity> list = new ArrayList<EvaluationEntity>();
		for(List<EvaluationEntity> subList: evaluationEntities){
			if(index >= subList.size())
				throw new RuntimeException("index larger than list");
			list.add(subList.get(index));
		}
		return list;
	}		
	
	public int getAmountEntitesPerList(){
		return amountEntitiesPerList;
	}

	@Override
	public Type getClone() {
		return new CoevolutionEvaluationList(this); //does this make sense?
	}

	public int getDimension() {
		return evaluationEntities.size();//does this make sense?
	}

	public void reset() {
		evaluationEntities = new ArrayList<List<EvaluationEntity>>();//does this make sense?
		amountEntitiesPerList = 0;
	}
}
