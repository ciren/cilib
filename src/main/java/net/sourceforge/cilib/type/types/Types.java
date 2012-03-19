/**
 * Computational Intelligence Library (CIlib)
 * Copyright (C) 2003 - 2010
 * Computational Intelligence Research Group (CIRG@UP)
 * Department of Computer Science
 * University of Pretoria
 * South Africa
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.cilib.type.types;

import java.util.Iterator;
import net.sourceforge.cilib.container.visitor.Visitor;
import net.sourceforge.cilib.type.types.container.StructuredType;

/**
 *
 */
public final class Types {

    private Types() {
    }

    /**
     * Determine if the current type instance is within the defined bounds
     * of the domain.
     * @param candidateSolution
     * @return {@literal true} if it is in the bounds, {@literal false} otherwise.
     */
    public static boolean isInsideBounds(Type candidateSolution) {
        if (candidateSolution instanceof StructuredType) {
            StructuredType structuredType = (StructuredType) candidateSolution;
            BoundsVerificationVisitor visitor = new BoundsVerificationVisitor();
            structuredType.accept(visitor);
            return visitor.isValid();
        }

        if (candidateSolution instanceof Numeric) {
            Numeric boundedType = (Numeric) candidateSolution;
            Bounds bounds = boundedType.getBounds();
            return bounds.isInsideBounds(boundedType.doubleValue());
        }

        return false;
    }

    private static class BoundsVerificationVisitor implements Visitor<Type> {

        private boolean valid = true;
        private boolean isDone = false;

        @Override
        public void visit(Type o) {
            if (o instanceof Numeric) {
                Numeric numeric = (Numeric) o;
                if (!numeric.getBounds().isInsideBounds(numeric.doubleValue())) {
                    this.isDone = true;
                    this.valid = false;
                }

                return;
            }

            isDone = true;
            valid = false;
        }

        public boolean isValid() {
            return valid;
        }

        @Override
        public boolean isDone() {
            return isDone;
        }
    }

    /**
     * Get the representation of the type in the form expressed by the domain notation.
     * <p>
     * Examples:
     * <ul>
     * <li>R(-30,30)</li>
     * <li>Z(-7,4)</li>
     * <li>B</li>
     * </ul>
     * @param candidateSolution The type to represent.
     * @return A <code>String</code> representing the <code>Type</code> in domain notation.
     */
    public static String getRepresentation(Type candidateSolution) {
        StringBuilder builder = new StringBuilder();

        if (candidateSolution instanceof StructuredType) {
            StructuredType structuredType = (StructuredType) candidateSolution;

            builder.append("[");
            for (Iterator<?> iterator = structuredType.iterator(); iterator.hasNext();) {
                Type type = (Type) iterator.next();
                builder.append(getRepresentation(type));
                builder.append(",");
            }
            builder.append("]");
        }

        if (candidateSolution instanceof Numeric) {
            Numeric numeric = (Numeric) candidateSolution;
            builder.append(numeric.getRepresentation());
        }

        return builder.toString();
    }

    /**
     * Get the dimension of the provided Type. If the provided type is a
     * StructuredType, the size returned will
     * @param value The type to determine the dimension of.
     * @return The dimensionality of the provided type.
     */
    public static int dimensionOf(Type value) {
        if (value instanceof StructuredType) {
            StructuredType structuredType = (StructuredType) value;
            return structuredType.size();
        }

        return 1; // This is the size for all non-structured types.
    }
}
