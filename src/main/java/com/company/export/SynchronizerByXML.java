package com.company.export;

import com.company.DBIProvider;
import com.company.dao.JobDescriptionDao;
import com.company.model.JobDescription;
import com.company.util.StaxStreamProcessor;
import org.skife.jdbi.v2.sqlobject.Transaction;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SynchronizerByXML {
    private final JobDescriptionDao jobDescriptionDao = DBIProvider.getDao(JobDescriptionDao.class);

    public void synchronize(String pathFileName) throws Exception {
        try (InputStream is = new FileInputStream(new File(pathFileName))) {
            final StaxStreamProcessor processor = new StaxStreamProcessor(is);
            process(processor);
        }
    }

    @Transaction
    public void process(StaxStreamProcessor processor) throws XMLStreamException {

        List<JobDescription> jobsFromXML = new ArrayList<>();

        while (processor.doUntil(XMLEvent.START_ELEMENT, "ОписаниеДолжности")) {

            final int depCode = Integer.parseInt(processor.getAttribute("КодДепартамента"));
            final int depJob = Integer.parseInt(processor.getAttribute("КодДолжности"));
            final String description = processor.getReader().getElementText();

            jobsFromXML.add(new JobDescription(depCode, depJob, description));
        }

        final List<JobDescription> jobsFromDB = jobDescriptionDao.getAll();
        final List<JobDescription> jobsToDelete = new ArrayList<>();
        final List<JobDescription> jobsToInsert = new ArrayList<>();
        final Map<JobDescription, Boolean> mapForSynchronization = new HashMap<>();

        jobsFromXML.forEach(job -> mapForSynchronization.put(job, false));

        jobsFromDB.forEach(job -> {
            if (!mapForSynchronization.containsKey(job)) {
                jobsToDelete.add(job);
            } else {
                mapForSynchronization.entrySet().stream()
                        .filter(entry -> entry.getKey().equals(job))
                        .forEach(entry -> {
                            if (!entry.getKey().getDescription().equals(job.getDescription())) {
                                jobDescriptionDao.update(entry.getKey());
                            }

                            entry.setValue(true);
                        });
            }
        });

        for (Map.Entry<JobDescription, Boolean> entry : mapForSynchronization.entrySet()) {
            if (!entry.getValue()) jobsToInsert.add(entry.getKey());
        }
        if (jobsToInsert.size() > 0)
            jobDescriptionDao.save(jobsToInsert);
        if (jobsToDelete.size() > 0)
            jobDescriptionDao.delete(jobsToDelete);

    }
}
