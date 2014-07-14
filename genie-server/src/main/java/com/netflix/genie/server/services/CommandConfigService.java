/*
 *
 *  Copyright 2014 Netflix, Inc.
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
package com.netflix.genie.server.services;

import com.netflix.genie.common.exceptions.GenieException;
import com.netflix.genie.common.model.Application;
import com.netflix.genie.common.model.Cluster;
import com.netflix.genie.common.model.Command;
import java.util.List;
import java.util.Set;

/**
 * Abstraction layer to encapsulate CommandConfig functionality.<br>
 * Classes implementing this abstraction layer must be thread-safe.
 *
 * @author amsharma
 * @author tgianos
 */
public interface CommandConfigService {

    /**
     * Create new command configuration.
     *
     * @param command encapsulates the command configuration information to
     * create
     * @return The command created
     * @throws GenieException
     */
    Command createCommand(final Command command) throws GenieException;

    /**
     * Gets command configuration for given id.
     *
     * @param id unique id for command configuration to get. Not null/empty.
     * @return The command configuration
     * @throws GenieException
     */
    Command getCommand(final String id) throws GenieException;

    /**
     * Get command configurations for given filter criteria.
     *
     * @param name name of command config (can be null)
     * @param userName the name of the user who created the configuration (can
     * be null)
     * @param page Page number to start results on
     * @param limit Max number of results per page
     * @return All the commands matching the specified criteria
     */
    List<Command> getCommands(
            final String name,
            final String userName,
            final int page,
            final int limit);

    /**
     * Update command configuration.
     *
     * @param id The id of the command configuration to update. Not null or
     * empty.
     * @param updateCommand contains the information to update the command with
     * @return The updated command
     * @throws GenieException
     */
    Command updateCommand(
            final String id,
            final Command updateCommand) throws GenieException;

    /**
     * Delete all commands from database.
     *
     * @return The deleted commands
     * @throws GenieException
     */
    List<Command> deleteAllCommands() throws GenieException;

    /**
     * Delete a command configuration from database.
     *
     * @param id unique if of the command configuration to delete
     * @return The deleted command configuration
     * @throws GenieException
     */
    Command deleteCommand(final String id) throws GenieException;

    /**
     * Add a configuration files to the command.
     *
     * @param id The id of the command to add the configuration file to. Not
     * null/empty/blank.
     * @param configs The configuration files to add. Not null/empty.
     * @return The active set of configurations
     * @throws GenieException
     */
    Set<String> addConfigsForCommand(
            final String id,
            final Set<String> configs) throws GenieException;

    /**
     * Get the set of configuration files associated with the command with given
     * id.
     *
     * @param id The id of the command to get the configuration files for. Not
     * null/empty/blank.
     * @return The set of configuration files as paths
     * @throws GenieException
     */
    Set<String> getConfigsForCommand(
            final String id) throws GenieException;

    /**
     * Update the set of configuration files associated with the command with
     * given id.
     *
     * @param id The id of the command to update the configuration files for.
     * Not null/empty/blank.
     * @param configs The configuration files to replace existing configurations
     * with. Not null/empty.
     * @return The active set of configurations
     * @throws GenieException
     */
    Set<String> updateConfigsForCommand(
            final String id,
            final Set<String> configs) throws GenieException;

    /**
     * Remove all configuration files from the command.
     *
     * @param id The id of the command to remove the configuration file from.
     * Not null/empty/blank.
     * @return The active set of configurations
     * @throws GenieException
     */
    Set<String> removeAllConfigsForCommand(
            final String id) throws GenieException;

    /**
     * Remove a configuration file from the command.
     *
     * @param id The id of the command to remove the configuration file from.
     * Not null/empty/blank.
     * @param config The configuration file to remove. Not null/empty/blank.
     * @return The active set of configurations
     * @throws GenieException
     */
    Set<String> removeConfigForCommand(
            final String id,
            final String config) throws GenieException;

    /**
     * Set the application for the command.
     *
     * @param id The id of the command to add the application file to. Not
     * null/empty/blank.
     * @param application The applications to set. Not null.
     * @return The application
     * @throws GenieException
     */
    Application setApplicationForCommand(
            final String id,
            final Application application) throws GenieException;

    /**
     * Get the application for a given command.
     *
     * @param id The id of the command to get the application for. Not
     * null/empty/blank.
     * @return The application or exception if none exists.
     * @throws GenieException
     */
    Application getApplicationForCommand(
            final String id) throws GenieException;

    /**
     * Remove the application from the command.
     *
     * @param id The id of the command to remove the application from. Not
     * null/empty/blank.
     * @return The removed application
     * @throws GenieException
     */
    Application removeApplicationForCommand(
            final String id) throws GenieException;

    /**
     * Get all the clusters the command with given id is associated with.
     *
     * @param id The id of the command to get the clusters for.
     * @return The clusters the command is available on.
     * @throws GenieException
     */
    Set<Cluster> getClustersForCommand(
            final String id) throws GenieException;
}
