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
package net.sourceforge.cilib.type.parser;

import com.google.common.collect.Lists;
import net.sourceforge.cilib.type.types.Numeric;
import net.sourceforge.cilib.type.types.Type;
import net.sourceforge.cilib.type.types.container.StructuredType;
import net.sourceforge.cilib.type.types.container.TypeList;
import net.sourceforge.cilib.type.types.container.Vector;
import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;

import java.util.List;

/**
 * The domain parser converts a provided domain string representation into
 * a {@code StructuredType}. If the domain string defines a simple {@code Vector}
 * based representaton of {@code Numeric} types, then a {@code Vector} is returned.
 */
public final class DomainParser {

    private DomainParser() {
    }

    /**
     * Parse the provided domain string and return the constructed representation.
     * @param <E> The structured type.
     * @param domain The string to parse.
     * @return A {@code TypeList} is returned by default, but if the type is defined
     *         to consist of {@code Numeric} types, a {@code Vector} instance is returned.
     */
    public static <E extends StructuredType<? extends Type>> E parse(String domain) {
        final DomainParserGrammar.Parser parser = Parboiled.createParser(DomainParserGrammar.Parser.class);
        final ReportingParseRunner<?> runner = new ReportingParseRunner(parser.Domain());
        final ParsingResult<Type> result = (ParsingResult<Type>) runner.run(domain.replaceAll(" ", ""));

        if (result.hasErrors()) {
            for (ParseError e : result.parseErrors) {
                System.out.println(e.getErrorMessage());
            }
            throw new RuntimeException("Error in parsing domain: " + domain);
        }

        List<Type> l = Lists.newArrayList(result.valueStack);

        if (isVector(l)) {
            @SuppressWarnings("unchecked")
            E vector = (E) toVector(l);
            return vector;
        }

        return (E) toTypeList(l);
    }

    private static TypeList toTypeList(List<Type> l) {
        TypeList list = new TypeList();
        for (Type t : l) {
            list.prepend(t);
        }
        return list;
    }

    /**
     * Convert the {@code TypeList} into a {@code Vector} if all elements are
     * {@code Numeric} types.
     *
     *
     * @param representation The current data structure of the representation.
     * @return {@code true} if the structure should really be a {@code Vector},
     *         {@code false} otherwise.
     */
    private static boolean isVector(List<Type> representation) {
        for (Type type : representation) {
            if (!(type instanceof Numeric)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Convert the provided {@code representation} into a {@code Vector}.
     *
     * @param representation The {@code StructuredType} to convert.
     * @return The converted vector object.
     */
    private static Vector toVector(List<Type> representation) {
        Vector.Builder vector = Vector.newBuilder();

        for (Type type : representation) {
            vector.prepend((Numeric) type);
        }

        return vector.build();
    }
}
