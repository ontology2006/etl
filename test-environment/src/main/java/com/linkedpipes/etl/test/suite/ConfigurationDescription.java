package com.linkedpipes.etl.test.suite;

import com.linkedpipes.etl.executor.api.v1.vocabulary.LP_OBJECTS;
import com.linkedpipes.etl.rdf.utils.RdfUtilsException;
import com.linkedpipes.etl.rdf.utils.pojo.RdfLoader;
import com.linkedpipes.etl.rdf.utils.vocabulary.RDF;

import java.util.LinkedList;
import java.util.List;

/**
 * Represent a description of a configuration object.
 */
class ConfigurationDescription implements RdfLoader.Loadable<String> {

    public static class Member implements RdfLoader.Loadable<String> {

        private final List<String> types = new LinkedList<>();

        private String iri;

        private String property;

        private String control;

        private boolean complex = false;

        public Member(String iri) {
            this.iri = iri;
        }

        @Override
        public RdfLoader.Loadable load(String predicate, String object)
                throws RdfUtilsException {
            switch (predicate) {
                case RDF.TYPE:
                    types.add(object);
                    break;
                case LP_OBJECTS.HAS_PROPERTY:
                    if (property != null) {
                        throw new RuntimeException("Multiple <" +
                                LP_OBJECTS.HAS_PROPERTY + "> values detected!");
                    }
                    property = object;
                    break;
                case LP_OBJECTS.HAS_CONTROL:
                    if (control != null) {
                        throw new RuntimeException("Multiple <" +
                                LP_OBJECTS.HAS_CONTROL + "> values detected!");
                    }
                    control = object;
                    break;
                case LP_OBJECTS.IS_COMPLEX:
                    if ("true".equalsIgnoreCase(object)) {
                        complex = true;
                    }
                    break;

            }
            return null;
        }

        public void validate() throws TestFailed {
            if (!types.contains(LP_OBJECTS.MEMBER)) {
                throw new TestFailed("Missing type: {}", iri);
            }
            if (property == null) {
                throw new TestFailed("Missing property: {}", iri);
            }
            if (control == null) {
                throw new TestFailed("Missing control: {}", iri);
            }
        }

        public String getProperty() {
            return property;
        }

        public String getControl() {
            return control;
        }

        public boolean isComplex() {
            return complex;
        }

    }

    private final String iri;

    private final List<String> types = new LinkedList<>();

    private String referencedType;

    private final List<Member> members = new LinkedList<>();

    public ConfigurationDescription(String iri) {
        this.iri = iri;
    }

    @Override
    public RdfLoader.Loadable load(String predicate, String object)
            throws RdfUtilsException {
        switch (predicate) {
            case RDF.TYPE:
                types.add(object);
                break;
            case LP_OBJECTS.HAS_DESCRIBE:
                if (referencedType != null) {
                    throw new RuntimeException("Multiple <" +
                            LP_OBJECTS.HAS_DESCRIBE + "> values detected!");
                }
                referencedType = object;
                break;
            case LP_OBJECTS.HAS_MEMBER:
                final Member newMember = new Member(object);
                members.add(newMember);
                return newMember;
        }
        return null;
    }

    public void validate() throws TestFailed {
        if (!types.contains(LP_OBJECTS.DESCRIPTION)) {
            throw new TestFailed("Missing description type.");
        }
        if (referencedType == null) {
            throw new TestFailed(
                    "Missing referenced type predicate.");
        }
        for (Member member : members) {
            member.validate();
        }
    }

    public String getIri() {
        return iri;
    }

    public String getReferencedType() {
        return referencedType;
    }

    public Member getMember(String property) throws TestFailed {
        for (Member member : members) {
            if (member.property.equals(property)) {
                return member;
            }
        }
        throw new TestFailed("Missing description for <{}>", property);
    }

}
