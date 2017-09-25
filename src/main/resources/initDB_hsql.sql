DROP TABLE job_description IF EXISTS;

CREATE TABLE job_description
(
  dep_code          INTEGER NOT NULL,
  dep_job           INTEGER NOT NULL,
  description       VARCHAR(255) NOT NULL,
  CONSTRAINT depcode_depjob_idx UNIQUE (dep_code, dep_job)
);