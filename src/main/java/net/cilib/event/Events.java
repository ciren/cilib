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
package net.cilib.event;

import com.google.common.base.Preconditions;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;

/**
 *
 */
public final class Events {

    private Events() {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a new matcher instance which matches methods that publish
     * certain event types.
     * @param cls the event type published.
     * @return new matcher that matches the provided class.
     */
    public static Matcher<AnnotatedElement> raising(Class<? extends Event> cls) {
        return new RaisingTypeMatcher(cls);
    }

    /**
     * A matcher that matches based on the type of event that is published.
     */
    private static class RaisingTypeMatcher extends AbstractMatcher<AnnotatedElement> implements Serializable {

        public static final long serialVersionUID = 0L;
        private final Class<? extends Event> eventType;

        public RaisingTypeMatcher(final Class<? extends Event> eventType) {
            this.eventType = eventType;
        }

        @Override
        public boolean matches(AnnotatedElement element) {
            if (!element.isAnnotationPresent(CanRaise.class)) {
                return false;
            }
            CanRaise p = element.getAnnotation(CanRaise.class);
            return p.value().isAssignableFrom(eventType);
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof RaisingTypeMatcher
                    && ((RaisingTypeMatcher) other).eventType.equals(eventType);
        }

        @Override
        public int hashCode() {
            return 37 * eventType.hashCode();
        }

        @Override
        public String toString() {
            return "raising(" + eventType.getSimpleName() + ".class)";
        }
    }

    public static Matcher<Class> on(Class<?> cls) {
        return new ClassTypeMatcher(cls);
    }

    private static class ClassTypeMatcher extends AbstractMatcher<Class> {

        private final Class<?> cls;

        private ClassTypeMatcher(Class<?> cls) {
            this.cls = cls;
        }

        @Override
        public boolean matches(Class t) {
            Preconditions.checkNotNull(t);
            return t.getName().equals(cls.getName());
        }
        @Override
        public boolean equals(Object other) {
            return other instanceof ClassTypeMatcher
                    && ((ClassTypeMatcher) other).cls.equals(cls);
        }

        @Override
        public int hashCode() {
            return 37 * cls.hashCode();
        }

        @Override
        public String toString() {
            return "on(" + cls.getSimpleName() + ".class)";
        }
    }
}
