/*
 *
 *  Copyright 2015 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.genie.core.jobs;

import com.netflix.genie.common.dto.Application;
import com.netflix.genie.common.dto.Cluster;
import com.netflix.genie.common.dto.Command;
import com.netflix.genie.common.dto.JobRequest;
import com.netflix.genie.common.exceptions.GenieException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Encapsulates the details of the job, cluster , command and applications needed to run a job.
 *
 * @author amsharma
 * @since 3.0.0
 */
@Getter
@Slf4j
public class JobExecutionEnvironment {

    private JobRequest jobRequest;
    private Cluster cluster;
    private Command command;
    private List<Application> applications = new ArrayList<>();
    private File jobWorkingDir;

    /**
     * Constructor used by the builder build() method.
     *
     * @param builder The builder to use
     */
    public JobExecutionEnvironment(final Builder builder) {
        this.jobRequest = builder.bJobRequest;
        this.cluster = builder.bCluster;
        this.command = builder.bCommand;
        this.applications.addAll(builder.bApplications);
        this.jobWorkingDir = builder.bJobWorkingDir;
    }

    /**
     * Get the applications for this instance.
     *
     * @return A list of applications for this instance.
     */
    public List<Application> getApplications() {
        return Collections.unmodifiableList(this.applications);
    }

    /**
     * A builder to create Job Execution Environment objects.
     *
     * @author amsharma
     * @since 3.0.0
     */
    public static class Builder {
        private JobRequest bJobRequest;
        private Cluster bCluster;
        private Command bCommand;
        private List<Application> bApplications = new ArrayList<>();
        private File bJobWorkingDir;

        /**
         * Constructor.
         *
         * @param request The job request object.
         * @param clusterObj The cluster object.
         * @param commandObj The command object.
         * @param dir The directory location for this job.
         * @throws GenieException If there is an error.
         */
        public Builder(
            @NotNull(message = "Job Request cannot be null")
            final JobRequest request,
            @NotNull(message = "Cluster cannot be null")
            final Cluster clusterObj,
            @NotNull(message = "Command cannot be null")
            final Command commandObj,
            @NotBlank(message = "Job working directory cannot be empty")
            final File dir
        ) throws GenieException {
            this.bJobRequest = request;
            this.bCluster = clusterObj;
            this.bCommand = commandObj;
            this.bJobWorkingDir = dir;
        }

        /**
         * Set the applications needed for the jobs' execution.
         *
         * @param applications The list of application objects.
         * @return The builder
         */
        public Builder withApplications(final List<Application> applications) {
            if (applications != null) {
                this.bApplications.addAll(applications);
            }
            return this;
        }

        /**
         * Build the job execution environment object.
         *
         * @return Create the final read-only JobRequest instance
         * @throws GenieException If there is any problem.
         */
        public JobExecutionEnvironment build() throws GenieException {
            return new JobExecutionEnvironment(this);
        }
    }
}
