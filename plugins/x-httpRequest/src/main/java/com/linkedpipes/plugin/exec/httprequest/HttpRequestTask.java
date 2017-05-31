package com.linkedpipes.plugin.exec.httprequest;

import com.linkedpipes.etl.executor.api.v1.rdf.RdfToPojo;

import java.util.LinkedList;
import java.util.List;

@RdfToPojo.Type(iri = HttpRequestVocabulary.TASK_CLASS)
public class HttpRequestTask {

    @RdfToPojo.Type(iri = HttpRequestVocabulary.HEADER)
    public static class Header {

        @RdfToPojo.Property(iri = HttpRequestVocabulary.HAS_NAME)
        private String name;

        @RdfToPojo.Property(iri = HttpRequestVocabulary.HAS_VALUE)
        private String value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @RdfToPojo.Type(iri = HttpRequestVocabulary.CONTENT)
    public static class Content {

        @RdfToPojo.Property(iri = HttpRequestVocabulary.HAS_NAME)
        private String name;

        @RdfToPojo.Property(iri = HttpRequestVocabulary.HAS_VALUE)
        private String value;

        @RdfToPojo.Property(iri = HttpRequestVocabulary.HAS_FILE_NAME)
        private String fileName;

        @RdfToPojo.Property(iri = HttpRequestVocabulary.HAS_FILE_REFERENCE)
        private String fileReference;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileReference() {
            return fileReference;
        }

        public void setFileReference(String fileReference) {
            this.fileReference = fileReference;
        }

    }

    @RdfToPojo.Property(iri = HttpRequestVocabulary.HAS_URL)
    private String url;

    @RdfToPojo.Property(iri = HttpRequestVocabulary.HAS_METHOD)
    private String method;

    @RdfToPojo.Property(iri = HttpRequestVocabulary.HAS_HEADER)
    private List<Header> headers = new LinkedList<>();

    @RdfToPojo.Property(iri = HttpRequestVocabulary.HAS_CONTENT)
    private List<Content> content = new LinkedList<>();

    @RdfToPojo.Property(iri = HttpRequestVocabulary.HAS_FILE_NAME)
    private String outputFileName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(
            List<Header> headers) {
        this.headers = headers;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public String getOutputFileName() {
        return outputFileName;
    }

    public void setOutputFileName(String outputFileName) {
        this.outputFileName = outputFileName;
    }
}

