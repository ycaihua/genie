import React from 'react';
import { render } from 'react-dom';

import { fetch } from '../utils';
import $ from 'jquery';

import InfoTable from './InfoTable';

export default class CommandDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      command: {
        configs: [],
      },
      clusters: [],
      applications: [],
    };
  }

  componentDidMount() {
    this.loadData(this.props);
  }

  componentWillReceiveProps(nextProps) {
    this.loadData(nextProps);
  }

  loadData(props) {
    const { commandUrl, clustersUrl, applicationsUrl } = props;
    $.when(fetch(commandUrl), fetch(clustersUrl), fetch(applicationsUrl))
      .done((command, clusters, applications) => {
        this.setState({
          command      : command[0],
          clusters     : clusters[0],
          applications : applications[0],
        });
    });
  }

  render() {
    return(
      <tr>
        <td colSpan="7">
          <button
            type="button"
            className="close pull-left"
            onClick={(e) => this.props.hideDetails()}
            aria-label="Close"
          >
            <i className="fa fa-times" aria-hidden="true"></i>
          </button>
          <div className="job-detail-row">
          <table className="table job-detail-table">
            <tbody>
              <tr>
                <td className="col-xs-2">Description:</td>
                <td>{this.state.command.description}</td>
              </tr>
              <tr>
                <td className="col-xs-2">Check Delay:</td>
                <td>{this.state.command.checkDelay}</td>
              </tr>
              <tr>
                <td className="col-xs-2">Setup File:</td>
                <td>{this.state.command.setupFile}</td>
              </tr>
              <tr>
                <td className="col-xs-2">Executable:</td>
                <td>{this.state.command.executable}</td>
              </tr>
              <tr>
                <td className="col-xs-2">Config:</td>
                <td>
                  <ul>
                    {this.state.command.configs.map((config, index) =>
                      <li key={index}>{config}</li>
                    )}
                  </ul>
               </td>
              </tr>
              <tr>
                <td className="col-xs-2">Clusters:</td>
                <td>
                  {this.state.clusters.length > 0 ?
                    <InfoTable data={this.state.clusters} type="clusters" />
                    :<div />
                  }
               </td>
              </tr>
              <tr>
                <td className="col-xs-2">Applications:</td>
                <td>
                  {this.state.applications.length > 0 ?
                    <InfoTable data={this.state.applications} type="applications" />
                    :<div />
                  }
               </td>
              </tr>
              </tbody>
            </table>
            </div>
        </td>
      </tr>
    );
  }
}
