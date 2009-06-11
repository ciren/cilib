/**
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

package net.sourceforge.cilib.type.parser;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.cilib.type.creator.TypeCreator;
import net.sourceforge.cilib.type.parser.DomainParser.DomainNode;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.TypeList;

/**
 *
 * @author gpampara
 */
public class DomainParserVisitorImpl implements DomainParserVisitor {

    /**
     * Top level visit operation - no actions are performed.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return The result of the visit opertion.
     */
    @Override
    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    /**
     * The root starting point of the grammar. No actions are performed apart from
     * deferring to other production rules.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return The result of the visit opertion.
     */
    @Override
    public Object visit(ASTrootElement node, Object data) {
        ASTelement element = (ASTelement) node.jjtGetChild(0);
        return element.jjtAccept(this, data);
    }

    /**
     * Obtain the results of the visitation of the {@code domainElement}
     * and any possible {@code repeat}s.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return The result of the visit opertion.
     */
    @Override
    public Object visit(ASTelement node, Object data) {
        ASTdomainElement domainElement = (ASTdomainElement) node.jjtGetChild(0);
        domainElement.jjtAccept(this, data);

        if (node.jjtGetNumChildren() > 1) {
             ASTrepeat repeat = (ASTrepeat) node.jjtGetChild(1);
             repeat.jjtAccept(this, data);
        }

        return null;
    }

    /**
     * Obtain the data from the domainElement. Actual types are constructed based
     * on the determined type, bounds and exponent values.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return The result of the visit opertion.
     */
    @Override
    public Object visit(ASTdomainElement node, Object data) {
        int children = node.jjtGetNumChildren();
        TypeCreator creator = null;
        List<Double> bounds = null;
        int exponent = 1;

        // type()
        ASTtype type = (ASTtype) node.jjtGetChild(0);
        creator = (TypeCreator) type.jjtAccept(this, data);

        if (children > 1) {
            SimpleNode dimensionOrExponent = (SimpleNode) node.jjtGetChild(1);

            if (dimensionOrExponent instanceof ASTdimension) {
                ASTdimension dimension = (ASTdimension) dimensionOrExponent;
                bounds = (List<Double>) dimension.jjtAccept(this, data);

                if (children > 2) { // We have an exponent as well
                    exponent = getExponent(node, data, 2);
                }
            }

            if (dimensionOrExponent instanceof ASTexponent) {
                exponent = getExponent(node, data, 1);
            }
        }

        expandDomain(creator, bounds, exponent, data);
        return null;
    }

    /**
     * Obtain the type defined in the portion of the domain string.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return The result of the visit opertion.
     */
    @Override
    public Object visit(ASTtype node, Object data) {
        DomainNode domainNode = (DomainNode) node.jjtGetChild(0);
        TypeCreator instance = null;

        try {
            // create an instance of the TypeCreator
            Class<?> creatorClass = Class.forName("net.sourceforge.cilib.type.creator." + domainNode.getValue());
            instance = (TypeCreator) creatorClass.newInstance();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }

        return instance;
    }

    /**
     * Extract the exponent value for the portion of the domain string.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return The result of the visit opertion.
     */
    @Override
    public Object visit(ASTexponent node, Object data) {
        DomainNode domainNode = (DomainNode) node.jjtGetChild(0);
        return domainNode.getValue();
    }

    /**
     * Determine the dimension elements of the potion of the domain string.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return The result of the visit opertion.
     */
    @Override
    public Object visit(ASTdimension node, Object data) {
        ASTlowerDim lowerDim = (ASTlowerDim) node.jjtGetChild(0);
        return lowerDim.jjtAccept(this, data);
    }

    /**
     * Extract the lower bounds information from the portion of the domain string.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return The result of the visit opertion.
     */
    @Override
    public Object visit(ASTlowerDim node, Object data) {
        List<Double> bounds = new ArrayList<Double>();

        ASTnumber number1 = (ASTnumber) node.jjtGetChild(0);
        Double value1 = Double.valueOf((String) number1.jjtAccept(this, data));
        bounds.add(value1);

        SimpleNode remainder = (SimpleNode) node.jjtGetChild(1);
        if (remainder instanceof ASTupperDim) {
            ASTupperDim upper = (ASTupperDim) remainder;
            Double value2 = Double.valueOf((String) upper.jjtAccept(this, data));
            bounds.add(value2);

            if (value1.compareTo(value2) > 0)
                throw new UnsupportedOperationException("Parsed bound values are not in order." +
                    "Upper bound is less than lower bound.");
        }

        if (remainder instanceof ASTvalue) {
            ASTvalue value = (ASTvalue) remainder;
            value.jjtAccept(this, data);
        }

        return bounds;
    }

    /**
     * Obtain the value for the upper bound and return it.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return The result of the visit opertion.
     */
    @Override
    public Object visit(ASTupperDim node, Object data) {
        ASTnumber number = (ASTnumber) node.jjtGetChild(0);
        return number.jjtAccept(this, data);
    }

    /**
     * Perfrom no actions. This is a terminal node for bounds.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return {@code null} - no action is performed.
     */
    @Override
    public Object visit(ASTvalue node, Object data) {
        // Nothing to be done as this is a terminal node that simply ends a statement.
        return null;
    }

    /**
     * Apply the repeat action as defined in the grammar.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return The result of the visit opertion.
     */
    @Override
    public Object visit(ASTrepeat node, Object data) {
        ASTelement element = (ASTelement) node.jjtGetChild(0);
        return element.jjtAccept(this, data);
    }

    /**
     * Extract the number that is defined in the domain string.
     * @param node The abstract syntax tree node to visit.
     * @param data The passed in data object.
     * @return The result of the visit opertion.
     */
    @Override
    public Object visit(ASTnumber node, Object data) {
        DomainNode domainNode = (DomainNode) node.jjtGetChild(0);
        return domainNode.getValue();
    }

    private int getExponent(ASTdomainElement node, Object data, int index) throws NumberFormatException {
        ASTexponent astexponent = (ASTexponent) node.jjtGetChild(index);
        return Integer.valueOf((String) astexponent.jjtAccept(this, data)).intValue();
    }

    private void expandDomain(TypeCreator creator, List<Double> bounds, int exponent, Object data) {
        TypeList list = (TypeList) data;

        for (int i = 0; i < exponent; i++) {
            Type type = null;
            if (bounds == null || bounds.size() == 0)
                type = creator.create();
            else if (bounds.size() == 1)
                type = creator.create(bounds.get(0));
            else // 2 bound values provided
                type = creator.create(bounds.get(0), bounds.get(1));

            list.add(type);
        }
    }
}
