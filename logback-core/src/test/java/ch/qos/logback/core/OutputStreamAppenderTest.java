/**
 * Logback: the reliable, generic, fast and flexible logging framework.
 * Copyright (C) 1999-2015, QOS.ch. All rights reserved.
 *
 * This program and the accompanying materials are dual-licensed under
 * either the terms of the Eclipse Public License v1.0 as published by
 * the Eclipse Foundation
 *
 *   or (per the licensee's choosing)
 *
 * under the terms of the GNU Lesser General Public License version 2.1
 * as published by the Free Software Foundation.
 */
package ch.qos.logback.core;

import java.io.ByteArrayOutputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.pattern.parser.SamplePatternLayout;

public class OutputStreamAppenderTest {

    Context context = new ContextBase();

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {
    }

    @Test
    public void smoke() {
        String FILE_HEADER = "FILE_HEADER ";
        String PRESENTATION_HEADER = "PRESENTATION_HEADER";
        String PRESENTATION_FOOTER = "PRESENTATION_FOOTER ";
        String FILE_FOOTER = "FILE_FOOTER";
        headerFooterCheck(FILE_HEADER, PRESENTATION_HEADER, PRESENTATION_FOOTER, FILE_FOOTER);
    }

    @Test
    public void nullFileHeader() {
        String FILE_HEADER = null;
        String PRESENTATION_HEADER = "PRESENTATION_HEADER";
        String PRESENTATION_FOOTER = "PRESENTATION_FOOTER ";
        String FILE_FOOTER = "FILE_FOOTER";
        headerFooterCheck(FILE_HEADER, PRESENTATION_HEADER, PRESENTATION_FOOTER, FILE_FOOTER);
    }

    @Test
    public void nullPresentationHeader() {
        String FILE_HEADER = "FILE_HEADER ";
        String PRESENTATION_HEADER = null;
        String PRESENTATION_FOOTER = "PRESENTATION_FOOTER ";
        String FILE_FOOTER = "FILE_FOOTER";
        headerFooterCheck(FILE_HEADER, PRESENTATION_HEADER, PRESENTATION_FOOTER, FILE_FOOTER);
    }

    @Test
    public void nullPresentationFooter() {
        String FILE_HEADER = "FILE_HEADER ";
        String PRESENTATION_HEADER = "PRESENTATION_HEADER";
        String PRESENTATION_FOOTER = null;
        String FILE_FOOTER = "FILE_FOOTER";
        headerFooterCheck(FILE_HEADER, PRESENTATION_HEADER, PRESENTATION_FOOTER, FILE_FOOTER);
    }

    @Test
    public void nullFileFooter() {
        String FILE_HEADER = "FILE_HEADER ";
        String PRESENTATION_HEADER = "PRESENTATION_HEADER";
        String PRESENTATION_FOOTER = "PRESENTATION_FOOTER ";
        String FILE_FOOTER = null;
        headerFooterCheck(FILE_HEADER, PRESENTATION_HEADER, PRESENTATION_FOOTER, FILE_FOOTER);
    }

    public void headerFooterCheck(String fileHeader, String presentationHeader, String presentationFooter,
            String fileFooter) {
        OutputStreamAppender<Object> osa = new OutputStreamAppender<Object>();
        osa.setContext(context);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        SamplePatternLayout<Object> spl = new SamplePatternLayout<Object>();
        spl.setContext(context);

        spl.setFileHeader(fileHeader);
        spl.setPresentationHeader(presentationHeader);
        spl.setPresentationFooter(presentationFooter);
        spl.setFileFooter(fileFooter);

        spl.start();
        LayoutWrappingEncoder<Object> encoder = new LayoutWrappingEncoder<Object>();
        encoder.setLayout(spl);
        encoder.setContext(context);

        osa.setEncoder(encoder);
        osa.setOutputStream(baos);
        osa.start();

        osa.stop();
        String result = baos.toString();

        String expectedHeader = emtptyIfNull(fileHeader) + emtptyIfNull(presentationHeader);

        System.out.println(result);

        Assertions.assertTrue(result.startsWith(expectedHeader), result);

        String expectedFooter = emtptyIfNull(presentationFooter) + emtptyIfNull(fileFooter);
        Assertions.assertTrue(result.endsWith(expectedFooter), result);
    }

    String emtptyIfNull(String s) {
        return s == null ? "" : s;
    }
}
