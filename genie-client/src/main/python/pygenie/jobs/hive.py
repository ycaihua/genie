"""
genie.jobs.hive

This module implements creating Hive jobs.

"""


from __future__ import absolute_import, division, print_function, unicode_literals

import logging
import os

from .core import GenieJob
from .utils import (add_to_repr,
                    arg_string,
                    is_file)


logger = logging.getLogger('com.netflix.genie.jobs.hive')


class HiveJob(GenieJob):
    """
    Hive job.

    Example:
        >>> job = HiveJob() \\
        ...     .job_name('hive example') \\
        ...     .script('/Users/jsmith/my_script.hql') \\
        ...     .parameter('param_1', 'value_1') \\
        ...     .parameter('param_2', 'value_2') \\
        ...     .property('mapred.foo', 'fizz') \\
        ...     .property('mapred.bar', 'buzz') \\
        ...     .property_file('/Users/jsmith/my_properties.conf')
    """

    DEFAULT_SCRIPT_NAME = 'script.hive'

    def __init__(self, conf=None):
        super(HiveJob, self).__init__(conf=conf)

        self._filename = HiveJob.DEFAULT_SCRIPT_NAME
        self._parameters = dict()
        self._properties = dict()
        self._property_file = None
        self._script = None

    @property
    def cmd_args(self):
        """
        The constructed command line arguments using the job's definition. If the
        command line arguments are set explicitly (by calling
        :py:meth:`command_arguments`) this will be the same.
        """

        if self._command_arguments is not None:
            return self._command_arguments

        params_str = ' '.join([
            '-d {qu}{name}={value}{qu}' \
                .format(name=k, value=v, qu='"' if ' ' in str(v) else '') \
            for k, v in self._parameters.iteritems()
        ])

        props_str = ' '.join([
            '--hiveconf {name}={value}'.format(name=k, value=v) \
            for k, v in self._properties.iteritems()
        ])

        prop_file_str = '-i {}'.format(os.path.basename(self._property_file)) \
            if self._property_file \
            else ''

        return '{prop_file} {props} {params} -f {filename}' \
            .format(prop_file=prop_file_str,
                    props=props_str,
                    filename=self._filename,
                    params=params_str) \
            .strip()

    def headers(self):
        """
        Sets hive.cli.print.header so that if the hive query is outputing
        to stdout it will append headers to the result.

        Example:
            >>> job = HIveJob() \\
            ...     .headers()

        Returns:
            :py:class:`HiveJob`: self
        """
        self.tags('headers')
        return self.property('hive.cli.print.header', 'true')

    def hiveconf(self, name, value):
        """Alias for :py:meth:`HiveJob.property`"""
        return self.property(name, value)

    @add_to_repr('append')
    def property(self, name, value):
        """
        Sets a property for the job.

        Using the name and value passed in, the following will be constructed for
        the command-line when executing:
        '-Dname=value'

        Example:
            >>> # hive --hiveconf mapred.foo=fizz --hiveconf mapred.bar=buzz
            >>> job = HiveJob() \\
            ...     .property('mapred.foo', 'fizz') \\
            ...     .property('mapred.bar', 'buzz')

        Args:
            name (str): The property name.
            value (str): The property value.

        Returns:
            :py:class:`HiveJob`: self
        """

        self._properties[name] = value

        return self

    @arg_string
    @add_to_repr('overwrite')
    def property_file(self, _property_file):
        """
        Sets a property file to use for specifying properties for the job.

        Using the value passed in, the following will be constructed for the
        command-line when executing:
        '-P value'

        Example:
            >>> #hive -i my_properties.prop
            >>> job = HiveJob() \\
            ...     .property_file('/Users/jsmith/my_properties.prop')

        Args:
            _property_file (str): The full path to the property file.

        Returns:
            :py:class:`HiveJob`: self
        """

        self._add_dependency(_property_file)

        return self

    def query(self, script):
        """Alias for :py:meth:`HiveJob.script`"""

        return self.script(script)

    @arg_string
    @add_to_repr('overwrite')
    def script(self, _script):
        """
        Sets the script to run for the job. This can be a path to a script file or
        the code to execute.

        Example:
            >>> job = HiveJob() \\
            ...     .script('/Users/jsmith/my_script.hql')

        Args:
            script (str): A path to a script file or the code to run.

        Returns:
            :py:class:`HiveJob`: self
        """

        if is_file(_script):
            self._filename = os.path.basename(_script)
            self._add_dependency(_script)
        else:
            self._filename = HiveJob.DEFAULT_SCRIPT_NAME
            self._add_dependency({'name': self._filename, 'data': _script})

        return self
