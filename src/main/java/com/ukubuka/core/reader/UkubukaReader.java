package com.ukubuka.core.reader;

import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.parser.impl.UkubukaXMLParser;
import com.ukubuka.core.utilities.Constants;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ukubuka Reader
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component
public class UkubukaReader {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaReader.class);

    @Autowired
    private XMLInputFactory inputFactory;

    /**
     * Read File
     * 
     * @param source
     * @param completeFileName
     * @param fileEncoding
     * @param endLineDelimiter
     * @return File Lines
     * @throws ReaderException
     */
    public List<String> readFile(final SupportedSource source,
            final String completeFileName, final String fileEncoding,
            final String endLineDelimiter) throws ReaderException {
        LOGGER.info(
                "Reading File - Source: {} | Location: {} | Encoding: {} | Delimiter: {}",
                source, completeFileName, fileEncoding, endLineDelimiter);
        return new ArrayList<>(Arrays
                .asList(readFileAsString(source, completeFileName, fileEncoding)
                        .split(StringUtils.isEmpty(endLineDelimiter)
                                ? Constants.DEFAULT_FILE_END_LINE_DELIMITER
                                : endLineDelimiter)));
    }

    /**
     * Read File
     * 
     * @param source
     * @param completeFileName
     * @param fileEncoding
     * @return File Lines
     * @throws ReaderException
     */
    public String readFileAsString(final SupportedSource source,
            final String completeFileName, final String fileEncoding)
            throws ReaderException {
        try {
            return FileUtils
                    .readFileToString(
                            source == SupportedSource.URL
                                    ? new File(
                                            new URL(completeFileName).toURI())
                                    : new File(completeFileName),
                            StringUtils.isEmpty(fileEncoding)
                                    ? Constants.DEFAULT_FILE_ENCODING
                                    : fileEncoding);
        } catch (IOException | URISyntaxException
                | IllegalArgumentException ex) {
            throw new ReaderException(ex);
        }
    }

    public String readXMLAsString(final SupportedSource source,
                                           final String completeFileName, final UkubukaXMLParser ukubukaXMLParser)
            throws ReaderException {
        try {
            InputStream in = new FileInputStream(source == SupportedSource.URL
                    ? new File(
                    new URL(completeFileName).toURI())
                    : new File(completeFileName));
            XMLStreamReader xmlStreamReader = inputFactory.createXMLStreamReader(in);
            return ukubukaXMLParser.extractDataFromStream(xmlStreamReader);

        } catch (IOException | URISyntaxException | XMLStreamException ex) {
            throw new ReaderException(ex);
        }
    }
}
