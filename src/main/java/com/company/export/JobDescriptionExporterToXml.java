package com.company.export;

import com.company.DBIProvider;
import com.company.dao.JobDescriptionDao;
import com.company.model.JobDescription;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JobDescriptionExporterToXml {
    public static final String ENTRIES = "ОписьДолжностей";
    public static final String ENTRY = "ОписаниеДолжности";
    public static final String DEP_CODE = "КодДепартамента";
    public static final String DEP_JOB = "КодДолжности";
    public static final String newLine = "\n";
    private final JobDescriptionDao jobDescriptionDao = DBIProvider.getDao(JobDescriptionDao.class);

    public void writeToXml(Path path) throws IOException, XMLStreamException {
        try (OutputStream os = Files.newOutputStream(path)) {
            XMLOutputFactory outputFactory = XMLOutputFactory.newFactory();
            XMLStreamWriter writer = null;
            try {
                writer = outputFactory.createXMLStreamWriter(os, "utf-8");

                writeTestsElem(writer, jobDescriptionDao.getAll());

            } finally {
                if (writer != null)
                    writer.close();
            }
        }
    }

    private void writeTestsElem(XMLStreamWriter writer, List<JobDescription> jobDescriptions) throws XMLStreamException {
        writer.writeStartDocument("utf-8", "1.0");

        writer.writeCharacters(newLine);

        writer.writeStartElement(ENTRIES);

        writer.writeCharacters(newLine);

        for (JobDescription jobDescription : jobDescriptions) {
            writeTestElem(writer, jobDescription);
        }


        writer.writeEndElement();

        writer.writeEndDocument();
    }

    private void writeTestElem(XMLStreamWriter writer, JobDescription jobDescription) throws XMLStreamException {
        writer.writeStartElement(ENTRY);
        writer.writeAttribute(DEP_CODE, String.valueOf(jobDescription.getDepCode()));
        writer.writeAttribute(DEP_JOB, String.valueOf(jobDescription.getDepJob()));

        writer.writeCharacters(String.valueOf(jobDescription.getDescription()));

        writer.writeEndElement();
        writer.writeCharacters(newLine);
    }
}
